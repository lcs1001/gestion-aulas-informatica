package gestionaulasinformatica.ui.views.admin.mantenimientopropietarios;

import org.springframework.security.access.annotation.Secured;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import gestionaulasinformatica.backend.entity.Centro;
import gestionaulasinformatica.backend.entity.Departamento;
import gestionaulasinformatica.backend.entity.PropietarioAula;
import gestionaulasinformatica.backend.entity.Usuario;
import gestionaulasinformatica.backend.service.AulaService;
import gestionaulasinformatica.backend.service.PropietarioAulaService;
import gestionaulasinformatica.backend.service.UsuarioService;
import gestionaulasinformatica.ui.Comunes;
import gestionaulasinformatica.ui.MainLayout;
import gestionaulasinformatica.ui.Mensajes;

/**
 * Ventana Mantenimiento de Centros y Departamentos (CRUD de la entidad
 * PropietarioAula).
 */
@Route(value = "mantenimientoPropietarios", layout = MainLayout.class)
@PageTitle("Mantenimiento de Centros y Departamentos")
@Secured("ADMIN")
public class MantPropietariosView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private PropietarioAulaService propietarioAulaService;
	private UsuarioService usuarioService;
	private AulaService aulaService;
	private Comunes comunes;

	private final MantPropietariosForm formulario;
	private Grid<PropietarioAula> gridPropietarios;
	private TextField filtroTexto;
	private HorizontalLayout toolbar;

	/**
	 * Constructorde la clase.
	 * 
	 * @param propietarioAulaService Service de JPA de la entidad PropietarioAula
	 * @param usuarioService         Service de JPA de la entidad Usuario
	 */
	public MantPropietariosView(PropietarioAulaService propietarioAulaService, UsuarioService usuarioService, AulaService aulaService) {
		Div contenido;

		try {
			this.propietarioAulaService = propietarioAulaService;
			this.usuarioService = usuarioService;
			this.aulaService = aulaService;
			this.comunes = new Comunes();

			addClassName("mant-propietarios-view");
			setSizeFull();

			configurarToolbar();
			configurarGridPropietarios();

			formulario = new MantPropietariosForm(this.usuarioService.findAllResponsables());
			formulario.addListener(MantPropietariosForm.SaveEvent.class, this::guardarPropietario);
			formulario.addListener(MantPropietariosForm.DeleteEvent.class, this::confirmarEliminacionPropietario);
			formulario.addListener(MantPropietariosForm.CloseEvent.class, e -> cerrarEditor());

			contenido = new Div(comunes.getTituloVentana("Mantenimiento de centros y departamentos"), toolbar,
					formulario, gridPropietarios);
			contenido.addClassName("mant-propietarios-contenido");
			contenido.setSizeFull();

			add(contenido);

			actualizarPropietarios();
			cerrarEditor();

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura el grid que muestra los propietarios de aulas.
	 */
	private void configurarGridPropietarios() {
		try {
			gridPropietarios = new Grid<>();
			gridPropietarios.addClassName("mant-propietarios-grid");
			gridPropietarios.setHeightFull();

			gridPropietarios.addColumn(PropietarioAula::getNombrePropietarioAula).setHeader("Centro/Departamento")
					.setKey("nombrePropietario");
			gridPropietarios.addColumn(propietario -> {
				Usuario responsable = propietario.getUsuarioResponsable();
				return responsable == null ? "-"
						: responsable.getNombreUsuario() + " " + responsable.getApellidosUsuario();
			}).setHeader("Responsable").setKey("nombreApellidosResponsable");

			gridPropietarios.addColumn(propietario -> {
				Usuario responsable = propietario.getUsuarioResponsable();
				return responsable == null ? "-" : responsable.getCorreoUsuario();
			}).setHeader("Correo responsable").setKey("correoResponsable");

			gridPropietarios.addColumn(propietario -> {
				Usuario responsable = propietario.getUsuarioResponsable();
				return responsable == null ? "-" : responsable.getTelefonoUsuario();
			}).setHeader("Telefono responsable").setKey("telefonoResponsable");

			gridPropietarios.getColumns().forEach(columna -> columna.setAutoWidth(true));

			gridPropietarios.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
					GridVariant.LUMO_ROW_STRIPES);

			gridPropietarios.asSingleSelect().addValueChangeListener(e -> abrirEditor(e.getValue(), true));

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura el toolbar que contiene: un filtro de texto que permite
	 * filtrar los propietarios de aulas por su nombre, un botón para añadir centros
	 * y un botón para añadir departamentos.
	 * 
	 * @return Toolbar
	 */
	private HorizontalLayout configurarToolbar() {
		Button btnAnadirCentro;
		Button btnAnadirDepartamento;

		try {
			filtroTexto = new TextField();
			filtroTexto.setPlaceholder("Buscar por nombre...");
			filtroTexto.setClearButtonVisible(true);
			filtroTexto.setValueChangeMode(ValueChangeMode.LAZY);
			filtroTexto.addValueChangeListener(e -> actualizarPropietarios());

			btnAnadirCentro = new Button("Añadir centro", click -> anadirCentro());
			btnAnadirCentro.setIcon(new Icon(VaadinIcon.PLUS));
			btnAnadirCentro.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			btnAnadirDepartamento = new Button("Añadir departamento", click -> anadirDepartamento());
			btnAnadirDepartamento.setIcon(new Icon(VaadinIcon.PLUS));
			btnAnadirDepartamento.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			toolbar = new HorizontalLayout(filtroTexto, btnAnadirCentro, btnAnadirDepartamento);
			toolbar.addClassName("toolbar");
			return toolbar;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función para cerrar el formulario para añadir, modificar o eliminar
	 * propietarios de aulas.
	 */
	private void cerrarEditor() {
		try {
			formulario.setPropietarioAula(null); // Se limpian los valores antiguos
			formulario.setVisible(false);
			toolbar.setVisible(true);
			gridPropietarios.setVisible(true);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que abre el editor para añadir o modificar el propietario pasado por
	 * parámetro.
	 * 
	 * @param propietario Propietario que se quiere añadir o modificar
	 * @param editar      Booleano que indica si se trata de una modificación (true)
	 *                    o una creación(false)
	 */
	private void abrirEditor(PropietarioAula propietario, Boolean editar) {
		try {
			if (propietario == null) {
				cerrarEditor();
			} else {
				toolbar.setVisible(false);
				gridPropietarios.setVisible(false);
				formulario.setPropietarioAula(propietario);
				formulario.setVisible(true);

				// No se puede editar el ID del propietario
				// Se oculta el botón "Eliminar" al añadir un centro o departamento
				if (editar) {
					formulario.idPropAula.setReadOnly(true);
					formulario.btnEliminar.setVisible(true);
				} else {
					formulario.idPropAula.setReadOnly(false);
					formulario.btnEliminar.setVisible(false);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que permite añadir un centro.
	 */
	private void anadirCentro() {
		try {
			gridPropietarios.asSingleSelect().clear();
			abrirEditor(new Centro(), false);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que permite añadir un departamento.
	 */
	private void anadirDepartamento() {
		try {
			gridPropietarios.asSingleSelect().clear();
			abrirEditor(new Departamento(), false);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que guarda el propietario en la base de datos.
	 * 
	 * @param e Evento de guardado
	 */
	private void guardarPropietario(MantPropietariosForm.SaveEvent evt) {
		try {
			propietarioAulaService.save(evt.getPropietarioAula());
			actualizarPropietarios();
			cerrarEditor();

		} catch (Exception e) {
			comunes.mostrarNotificacion(Mensajes.MSG_ERROR_GUARDAR.getMensaje(), 3000, null);
			throw e;
		}
	}

	/**
	 * Función que elimina el propietario de la base de datos.
	 * 
	 * @param propietario Propietario que se quiere eliminar
	 */
	private void eliminarPropietario(PropietarioAula propietario) {
		try {
			propietarioAulaService.delete(propietario);
			actualizarPropietarios();
			cerrarEditor();

		} catch (Exception e) {
			comunes.mostrarNotificacion(Mensajes.MSG_ERROR_ACCION.getMensaje(), 3000, null);
			throw e;
		}
	}

	/**
	 * Función que muestra un mensaje de confirmación en un cuadro de diálogo cuando
	 * se quiere eliminar un propietario de aulas (botón Eliminar)
	 * 
	 * @param evt Evento de eliminación de propietario de aula
	 */
	private void confirmarEliminacionPropietario(MantPropietariosForm.DeleteEvent evt) {
		PropietarioAula propietario;
		Dialog confirmacion;
		String mensajeConfirmacion;
		String mensajeEliminado;
		Button btnConfirmar;
		Button btnCancelar;

		try {
			propietario = evt.getPropietarioAula();

			if(!aulaService.findAllAulasPropietario(propietario).isEmpty()) {
				mensajeConfirmacion = propietario.getNombrePropietarioAula()
						+ " tiene aulas asignadas, ¿desea eliminarle definitivamente junto a todas sus aulas? Esta acción no se puede deshacer.";
			} else {
				mensajeConfirmacion = "¿Desea eliminar " + propietario.getNombrePropietarioAula()
						+ " definitivamente? Esta acción no se puede deshacer.";
			}

			confirmacion = new Dialog(new Label(mensajeConfirmacion));
			confirmacion.setCloseOnEsc(false);
			confirmacion.setCloseOnOutsideClick(false);

			mensajeEliminado = "Se ha eliminado " + propietario.getNombrePropietarioAula() + " correctamente";

			btnConfirmar = new Button("Confirmar", event -> {
				eliminarPropietario(propietario);
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

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que actualiza el grid que muestra los propietarios de aulas.
	 */
	private void actualizarPropietarios() {
		try {
			gridPropietarios.setItems(propietarioAulaService.findAll(filtroTexto.getValue()));
		} catch (Exception e) {
			throw e;
		}
	}

}
