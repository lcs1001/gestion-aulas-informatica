package gestionaulasinformatica.ui.views.admin.mantenimientousuarios;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import gestionaulasinformatica.app.security.SecurityUtils;
import gestionaulasinformatica.backend.entity.Usuario;
import gestionaulasinformatica.backend.service.UsuarioService;
import gestionaulasinformatica.exceptions.UserFriendlyDataException;
import gestionaulasinformatica.ui.Comunes;
import gestionaulasinformatica.ui.MainLayout;
import gestionaulasinformatica.ui.Mensajes;

/**
 * Ventana Mantenimiento de Usuarios (CRUD de la entidad Usuario).
 */
@Route(value = "mantenimientoUsuarios", layout = MainLayout.class)
@PageTitle("Mantenimiento de Usuarios")
@Secured("ADMIN")
public class MantUsuariosView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private UsuarioService usuarioService;
	private PasswordEncoder passwordEncoder;
	private Comunes comunes;

	private final MantUsuariosForm formulario;
	private Grid<Usuario> gridUsuarios;
	private TextField filtroTexto;
	private HorizontalLayout toolbar;

	private Usuario usuarioLogeado;

	/**
	 * Constructorde la clase.
	 * 
	 * @param passwordEncoder Codificador de contraseñas
	 */
	public MantUsuariosView(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
		Div contenido;

		try {
			this.usuarioService = usuarioService;
			this.passwordEncoder = passwordEncoder;
			this.comunes = new Comunes();

			usuarioLogeado = usuarioService.findByCorreoUsuarioIgnoreCase(SecurityUtils.getUsername());

			addClassName("mant-usuarios-view");
			setSizeFull();

			configurarToolbar();
			configurarGridUsuarios();

			formulario = new MantUsuariosForm(this.passwordEncoder);
			formulario.addListener(MantUsuariosForm.SaveEvent.class, this::guardarUsuario);
			formulario.addListener(MantUsuariosForm.DeleteEvent.class, this::confirmarEliminacionUsuario);
			formulario.addListener(MantUsuariosForm.CloseEvent.class, e -> cerrarEditor());

			contenido = new Div(comunes.getTituloVentana("Mantenimiento de usuarios"), toolbar, formulario,
					gridUsuarios);
			contenido.addClassName("mant-usuarios-contenido");
			contenido.setSizeFull();

			add(contenido);

			actualizarUsuarios();
			cerrarEditor();

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura el grid que muestra los usuarios.
	 */
	private void configurarGridUsuarios() {
		try {
			gridUsuarios = new Grid<>();
			gridUsuarios.addClassName("mant-usuarios-grid");
			gridUsuarios.setHeightFull();

			gridUsuarios.addColumn(Usuario::getNombreApellidosUsuario).setHeader("Usuario")
					.setKey("nombreApellidosUsuario");

			gridUsuarios.addColumn(Usuario::getCorreoUsuario).setHeader("Correo").setKey("correoUsuario");

			gridUsuarios.addColumn(Usuario::getTelefonoUsuario).setHeader("Teléfono").setKey("telefonoUsuario");

			gridUsuarios.addColumn(Usuario::getRolUsuario).setHeader("Rol").setKey("rolUsuario");

			gridUsuarios.getColumns().forEach(columna -> columna.setAutoWidth(true));

			gridUsuarios.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
					GridVariant.LUMO_ROW_STRIPES);

			gridUsuarios.asSingleSelect().addValueChangeListener(e -> abrirEditor(e.getValue(), true));

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura el toolbar que contiene: un filtro de texto que permite
	 * filtrar los usuarios por su nombre o apellidos, un botón para añadir
	 * usuarios.
	 * 
	 * @return Toolbar
	 */
	private HorizontalLayout configurarToolbar() {
		Button btnAnadirUsuario;

		try {
			filtroTexto = new TextField();
			filtroTexto.setPlaceholder("Buscar por nombre o apellidos...");
			filtroTexto.setClearButtonVisible(true);
			filtroTexto.setValueChangeMode(ValueChangeMode.LAZY);
			filtroTexto.addValueChangeListener(e -> actualizarUsuarios());
			filtroTexto.setWidth("300px");

			btnAnadirUsuario = new Button("Añadir usuario", click -> anadirUsuario());
			btnAnadirUsuario.setIcon(new Icon(VaadinIcon.PLUS));
			btnAnadirUsuario.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			toolbar = new HorizontalLayout(filtroTexto, btnAnadirUsuario);
			toolbar.addClassName("toolbar");
			return toolbar;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función para cerrar el formulario para añadir, modificar o eliminar usuarios.
	 */
	private void cerrarEditor() {
		try {
			formulario.setUsuario(null); // Se limpian los valores antiguos
			formulario.setVisible(false);
			toolbar.setVisible(true);
			gridUsuarios.setVisible(true);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que abre el editor para añadir o modificar el usuario pasado por
	 * parámetro.
	 * 
	 * @param usuario Usuario que se quiere añadir o modificar
	 * @param editar  Booleano que indica si se trata de una modificación (true) o
	 *                una creación(false)
	 */
	private void abrirEditor(Usuario usuario, Boolean editar) {
		try {
			if (usuario == null) {
				cerrarEditor();

			} else {
				toolbar.setVisible(false);
				gridUsuarios.setVisible(false);
				formulario.setUsuario(usuario);
				formulario.setVisible(true);

				// Se oculta el botón "Eliminar" al añadir un usuario
				if (editar) {
					formulario.btnEliminar.setVisible(true);
				} else {
					formulario.btnEliminar.setVisible(false);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que permite añadir un usuario.
	 */
	private void anadirUsuario() {
		try {
			gridUsuarios.asSingleSelect().clear();
			abrirEditor(new Usuario(), false);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que guarda el usuario en la base de datos.
	 * 
	 * @param evt Evento de guardado
	 */
	private void guardarUsuario(MantUsuariosForm.SaveEvent evt) {
		try {
			usuarioService.save(evt.getUsuario());
			actualizarUsuarios();
			cerrarEditor();

		} catch (Exception e) {
			comunes.mostrarNotificacion(Mensajes.MSG_ERROR_GUARDAR.getMensaje(), 3000, null);
			throw e;
		}
	}

	/**
	 * Función que elimina el usuario de la base de datos.
	 * 
	 * @param usuario Usuario que se quiere eliminar
	 */
	private void eliminarUsuario(Usuario usuario) {
		try {
			usuarioService.delete(usuarioLogeado, usuario);
			actualizarUsuarios();
			cerrarEditor();

		} catch (UserFriendlyDataException e) {
			comunes.mostrarNotificacion(e.getMessage(), 3000, NotificationVariant.LUMO_ERROR);
			e.printStackTrace();

		} catch (Exception e) {
			comunes.mostrarNotificacion(Mensajes.MSG_ERROR_ACCION.getMensaje(), 3000, null);
			e.printStackTrace();
		}
	}

	/**
	 * Función que muestra un mensaje de confirmación en un cuadro de diálogo cuando
	 * se quiere eliminar un usuario (botón Eliminar).
	 * 
	 * @param evt Evento de eliminación de usuario
	 */
	private void confirmarEliminacionUsuario(MantUsuariosForm.DeleteEvent evt) {
		Usuario usuario;
		Dialog confirmacion;
		String mensajeConfirmacion;
		String mensajeEliminado;
		Button btnConfirmar;
		Button btnCancelar;

		try {
			usuario = evt.getUsuario();

			// Si el usuario que se quiere eliminar es el que está logeado
			if (usuario.equals(usuarioLogeado)) {
				comunes.mostrarNotificacion(Mensajes.ELIMINAR_USUARIO_ACTUAL_NO_PERMITIDO.getMensaje(), 5000,
						NotificationVariant.LUMO_ERROR);

			} else if (usuario.isBloqueado()) { // Si el usuario que se quiere eliminar está bloqueado
				comunes.mostrarNotificacion(Mensajes.ELIMINAR_USUARIO_BLOQUEADO_NO_PERMITIDO.getMensaje(), 5000,
						NotificationVariant.LUMO_ERROR);
			} else {

				// Si el usuario tiene un centro o departamento bajo su responsabilidad se
				// informa
				if (usuario.tienePropietariosResponsabilidad()) {
					mensajeConfirmacion = "El usuario " + usuario.getNombreApellidosUsuario()
							+ " tiene bajo su responsabilidad algún centro o departamento, reasígneles otro responsable primero";
					comunes.mostrarNotificacion(mensajeConfirmacion, 5000, NotificationVariant.LUMO_ERROR);

				} else { // Si no tiene un centro o departamento bajo su responsabilidad se elimina
					mensajeConfirmacion = "¿Desea eliminar al usuario " + usuario.getNombreApellidosUsuario()
							+ " definitivamente? Esta acción no se puede deshacer.";

					confirmacion = new Dialog(new Label(mensajeConfirmacion));
					confirmacion.setCloseOnEsc(false);
					confirmacion.setCloseOnOutsideClick(false);

					mensajeEliminado = "Se ha eliminado " + usuario.getNombreApellidosUsuario() + " correctamente";

					btnConfirmar = new Button("Confirmar", event -> {
						eliminarUsuario(usuario);
						comunes.mostrarNotificacion(mensajeEliminado, 3000, null);
						confirmacion.close();
					});
					btnConfirmar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
					btnConfirmar.addClassName("margin-20");

					btnCancelar = new Button("Cancelar", event -> {
						cerrarEditor();
						confirmacion.close();
					});
					btnCancelar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

					confirmacion.add(btnConfirmar, btnCancelar);

					confirmacion.open();
				}
			}

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que actualiza el grid que muestra los usuarios.
	 */
	private void actualizarUsuarios() {
		try {
			gridUsuarios.setItems(usuarioService.findAll(filtroTexto.getValue()));
		} catch (Exception e) {
			throw e;
		}
	}
}
