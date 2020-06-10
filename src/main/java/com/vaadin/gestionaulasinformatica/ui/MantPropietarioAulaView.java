package com.vaadin.gestionaulasinformatica.ui;

// Imports Vaadin
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.gestionaulasinformatica.backend.entity.Centro;
import com.vaadin.gestionaulasinformatica.backend.entity.Departamento;
// Imports backend
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.service.PropietarioAulaService;

/**
 * Ventana para el Mantenimiento de Centros y Departamentos (CRUD de la entidad
 * PropietarioAula).
 */
@Route("MantPropietarioAula")
@CssImport("./styles/shared-styles.css")
public class MantPropietarioAulaView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private PropietarioAulaService propietarioAulaService;

	private final MantPropietarioAulaForm formulario;
	private Grid<PropietarioAula> gridPropietarios;
	private TextField filtroTexto;
	private Button btnAnadirCentro;
	private Button btnAnadirDepartamento;
	private HorizontalLayout toolbar;

	/**
	 * Constructorde la clase.
	 * 
	 * @param propietarioAulaService Service de JPA de la entidad PropietarioAula
	 */
	public MantPropietarioAulaView(PropietarioAulaService propietarioAulaService) {
		this.propietarioAulaService = propietarioAulaService;

		addClassName("mant-propietarios-view");

		// Se ajusta el tamaño de la ventana al del navegador
		setSizeFull();

		gridPropietarios = new Grid<>();
		configurarGridPropietarios();

		formulario = new MantPropietarioAulaForm();
		formulario.addListener(MantPropietarioAulaForm.SaveEvent.class, this::guardarPropietario);
		formulario.addListener(MantPropietarioAulaForm.DeleteEvent.class, this::eliminarPropietario);
		formulario.addListener(MantPropietarioAulaForm.CloseEvent.class, e -> cerrarEditor());

		add(formulario, getToolbar(), gridPropietarios);
		actualizarPropietarios();
		cerrarEditor();
	}

	/**
	 * Función que configura el grid que muestra los propietarios de aulas.
	 */
	private void configurarGridPropietarios() {
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

		// Se establece el ancho de columna automático
		gridPropietarios.getColumns().forEach(columna -> columna.setAutoWidth(true));

		// Se da un formato específico al grid
		gridPropietarios.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
				GridVariant.LUMO_ROW_STRIPES);

		// Sólo se permite editar una fila a la vez
		gridPropietarios.asSingleSelect().addValueChangeListener(e -> abrirEditor(e.getValue(), true));
	}

	/**
	 * Función que configura el toolbar que contiene: - Un filtro de texto que
	 * permite filtrar los propietarios de aulas por su nombre. - Un botón para
	 * añadir propietarios
	 * 
	 * @return Toolbar
	 */
	private HorizontalLayout getToolbar() {
		filtroTexto = new TextField();
		filtroTexto.setPlaceholder("Buscar por nombre...");
		filtroTexto.setClearButtonVisible(true);
		filtroTexto.setValueChangeMode(ValueChangeMode.LAZY);
		filtroTexto.addValueChangeListener(e -> actualizarPropietarios());

		btnAnadirCentro = new Button("Añadir centro", click -> anadirCentro());
		btnAnadirCentro.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		btnAnadirDepartamento = new Button("Añadir departamento", click -> anadirDepartamento());
		btnAnadirDepartamento.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		toolbar = new HorizontalLayout(filtroTexto, btnAnadirCentro, btnAnadirDepartamento);
		toolbar.addClassName("toolbar-propietarios");

		return toolbar;
	}

	/**
	 * Función para cerrar el formulario para añadir, modificar o eliminar
	 * propietarios de aulas.
	 */
	private void cerrarEditor() {
		formulario.setVisible(false);
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
		if (propietario == null) {
			cerrarEditor();
		} else {
			formulario.setPropietarioAula(propietario);
			formulario.setVisible(true);
			
			// No se pueden editar el ID del propietario ni el tipo (se ocultan)
			if(editar) {
				formulario.idPropAula.setVisible(false);
			} else {
				formulario.idPropAula.setVisible(true);
			}
		}
	}

	/**
	 * Función que permite añadir un centro.
	 */
	private void anadirCentro() {
		gridPropietarios.asSingleSelect().clear();
		abrirEditor(new Centro(), false);
	}
	
	/**
	 * Función que permite añadir un departamento.
	 */
	private void anadirDepartamento() {
		gridPropietarios.asSingleSelect().clear();
		abrirEditor(new Departamento(), false);
	}

	/**
	 * Función que guarda el propietario en la base de datos.
	 * 
	 * @param e Evento de guardado
	 */
	private void guardarPropietario(MantPropietarioAulaForm.SaveEvent e) {		
		propietarioAulaService.save(e.getPropietarioAula());
		actualizarPropietarios();
		cerrarEditor();
	}

	/**
	 * Función que elimina el propietario de la base de datos.
	 * 
	 * @param e Evento de eliminación
	 */
	private void eliminarPropietario(MantPropietarioAulaForm.DeleteEvent e) {
		propietarioAulaService.delete(e.getPropietarioAula());
		actualizarPropietarios();
		cerrarEditor();
	}

	/**
	 * Función que actualiza el grid que muestra los propietarios de aulas.
	 */
	private void actualizarPropietarios() {
		gridPropietarios.setItems(propietarioAulaService.findAll(filtroTexto.getValue()));
	}

}
