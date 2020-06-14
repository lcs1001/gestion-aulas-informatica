package com.vaadin.gestionaulasinformatica.ui.views.mantenimientopropietarios;

// Imports Vaadin
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.gestionaulasinformatica.backend.entity.Centro;
import com.vaadin.gestionaulasinformatica.backend.entity.Departamento;
// Imports backend
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.service.PropietarioAulaService;
import com.vaadin.gestionaulasinformatica.ui.MainLayout;

/**
 * Ventana Mantenimiento de Centros y Departamentos (CRUD de la entidad
 * PropietarioAula).
 */
@Route(value = "MantPropietarioAula", layout = MainLayout.class)
@PageTitle("Mantenimiento de Centros y Departamentos")
public class MantPropietariosView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private PropietarioAulaService propietarioAulaService;

	private final MantPropietariosForm formulario;
	private Grid<PropietarioAula> gridPropietarios;
	private TextField filtroTexto;
	private HorizontalLayout toolbar;

	/**
	 * Constructorde la clase.
	 * 
	 * @param propietarioAulaService Service de JPA de la entidad PropietarioAula
	 */
	public MantPropietariosView(PropietarioAulaService propietarioAulaService) {
		try {
			this.propietarioAulaService = propietarioAulaService;

			addClassName("mant-propietarios-view");
			setSizeFull();

			configurarGridPropietarios();

			formulario = new MantPropietariosForm();
			formulario.addListener(MantPropietariosForm.SaveEvent.class, this::guardarPropietario);
			formulario.addListener(MantPropietariosForm.DeleteEvent.class, this::eliminarPropietario);
			formulario.addListener(MantPropietariosForm.CloseEvent.class, e -> cerrarEditor());

			add(formulario, getToolbar(), gridPropietarios);
			
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
			gridPropietarios.addClassName("grid-propietarios");
			gridPropietarios.setSizeFull();

			gridPropietarios.addColumn(PropietarioAula::getNombrePropietarioAula).setHeader("Propietario Aula")
					.setKey("nombrePropietario");
			
			gridPropietarios
					.addColumn(PropietarioAula -> PropietarioAula.getNombreResponsable() + " "
							+ PropietarioAula.getApellidosResponsable())
					.setHeader("Responsable").setKey("nombreApellidosResponsable");
			
			gridPropietarios.addColumn(PropietarioAula::getCorreoResponsable).setHeader("Correo")
					.setKey("correoResponsable");
			
			gridPropietarios.addColumn(PropietarioAula::getTelefonoResponsable).setHeader("Teléfono")
					.setKey("telefonoResponsable");

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
	private HorizontalLayout getToolbar() {
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
			toolbar.addClassName("mant-propietarios-toolbar");
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
				formulario.setPropietarioAula(propietario);
				formulario.setVisible(true);

				// No se puede editar el ID del propietario
				if (editar) {
					formulario.idPropAula.setVisible(false);
				} else {
					formulario.idPropAula.setVisible(true);
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
			throw e;
		}
	}

	/**
	 * Función que elimina el propietario de la base de datos.
	 * 
	 * @param e Evento de eliminación
	 */
	private void eliminarPropietario(MantPropietariosForm.DeleteEvent evt) {
		try {
			propietarioAulaService.delete(evt.getPropietarioAula());
			actualizarPropietarios();
			cerrarEditor();
			
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
