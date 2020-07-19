package gestionaulasinformatica.ui.views.perfilusuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import gestionaulasinformatica.app.security.SecurityUtils;
import gestionaulasinformatica.backend.data.Rol;
import gestionaulasinformatica.backend.entity.PropietarioAula;
import gestionaulasinformatica.backend.entity.Usuario;
import gestionaulasinformatica.backend.service.PropietarioAulaService;
import gestionaulasinformatica.backend.service.UsuarioService;
import gestionaulasinformatica.ui.Comunes;
import gestionaulasinformatica.ui.MainLayout;
import gestionaulasinformatica.ui.Mensajes;

/**
 * Ventana Perfil Usuario.
 */
@Route(value = "perfilUsuario", layout = MainLayout.class)
@PageTitle("Perfil")
@Secured({ "ADMIN", "RESPONSABLE" })
public class PerfilUsuarioView extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(PerfilUsuarioView.class.getName());

	private UsuarioService usuarioService;
	private PropietarioAulaService propietarioAulaService;
	private PasswordEncoder passwordEncoder;
	private Comunes comunes;

	private final PerfilUsuarioForm formulario;
	private Grid<PropietarioAula> gridPropietarios = new Grid<>();

	private Usuario usuarioLogeado;

	public PerfilUsuarioView(UsuarioService usuarioService, PropietarioAulaService propietarioAulaService,
			PasswordEncoder passwordEncoder) {
		Div contenido;
		try {
			this.usuarioService = usuarioService;
			this.propietarioAulaService = propietarioAulaService;
			this.passwordEncoder = passwordEncoder;
			this.comunes = new Comunes();

			usuarioLogeado = usuarioService.findByCorreoUsuario(SecurityUtils.getUsername());

			addClassName("perfil-usuario-view");
			setSizeFull();

			// Si se ha logeado un responsable se configura el grid de propietarios
			if (usuarioLogeado.getRolUsuario().equals(Rol.RESPONSABLE)) {
				configurarGridPropietarios();
			} else {
				gridPropietarios.setVisible(false);
			}

			formulario = new PerfilUsuarioForm(this.comunes, this.passwordEncoder);
			formulario.addListener(PerfilUsuarioForm.SaveEvent.class, this::guardarUsuario);
			formulario.setUsuario(usuarioLogeado);

			contenido = new Div(comunes.getTituloVentana("Perfil"), formulario, gridPropietarios);
			contenido.addClassName("perfil-usuario-contenido");
			contenido.setSizeFull();

			add(contenido);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que configura el grid que muestra los propietarios de aulas de los
	 * que es responsable el usuario logueado.
	 */
	private void configurarGridPropietarios() {
		try {
			gridPropietarios.addClassName("perfil-usuario-grid");
			gridPropietarios.setHeightFull();

			gridPropietarios.addColumn(PropietarioAula::getIdPropietarioAula).setHeader("ID").setKey("idPropietario");
			gridPropietarios.addColumn(PropietarioAula::getNombrePropietarioAula).setHeader("Centro/Departamento")
					.setKey("nombrePropietario");

			gridPropietarios.getColumns().forEach(columna -> columna.setAutoWidth(true));

			gridPropietarios.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
					GridVariant.LUMO_ROW_STRIPES);

			gridPropietarios.setItems(propietarioAulaService.findAllPropietariosResponsable(usuarioLogeado));

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que guarda el usuario en la base de datos.
	 * 
	 * @param evt Evento de guardado
	 */
	private void guardarUsuario(PerfilUsuarioForm.SaveEvent evt) {
		try {
			usuarioService.save(evt.getUsuario());			
			comunes.mostrarNotificacion(Mensajes.MSG_GUARDADO_CORRECTO.getMensaje(), 3000, NotificationVariant.LUMO_SUCCESS);
			
		} catch (Exception e) {
			comunes.mostrarNotificacion(Mensajes.MSG_ERROR_GUARDAR.getMensaje(), 3000, NotificationVariant.LUMO_ERROR);
			LOGGER.error(e.getMessage());
			throw e;
		}
	}
}
