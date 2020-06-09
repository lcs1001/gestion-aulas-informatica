package com.vaadin.gestionaulasinformatica.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
// Imports backend
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.service.PropietarioAulaService;

/**
 * Ventana que permite realizar el mantenimiento (CRUD) de los propietarios de
 * las aulas (centros o departamentos).
 */
@Route("MantPropietarioAula")
public class MantPropietarioAulaView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private PropietarioAulaService propietarioAulaService;

	private final MantPropietarioAulaForm formulario;
	private Grid<PropietarioAula> gridPropietarios;
	private TextField filtroTexto = new TextField();

	/**
	 * Constructor de la ventana mantenimiento de propietarios de aulas.
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
		configurarFiltroTexto();
		
		formulario = new MantPropietarioAulaForm();

		add(formulario,filtroTexto, gridPropietarios);
		actualizarPropietarios();
	}

	/**
	 * Función para configurar el grid que muestra los propietarios de aulas.
	 */
	private void configurarGridPropietarios() {
		gridPropietarios.addClassName("propietarios-grid");
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
	}

	/**
	 * Función para configurar el filtro de texto que permite filtrar los
	 * propietarios de aulas por su nombre.
	 */
	private void configurarFiltroTexto() {
		filtroTexto.setPlaceholder("Buscar por nombre...");
		filtroTexto.setClearButtonVisible(true);
		filtroTexto.setValueChangeMode(ValueChangeMode.LAZY);
		filtroTexto.addValueChangeListener(e -> actualizarPropietarios());
	}

	/**
	 * Función que actualiza el grid que muestra los propietarios de aulas.
	 */
	private void actualizarPropietarios() {
		gridPropietarios.setItems(propietarioAulaService.findAll(filtroTexto.getValue()));
	}

}
