package com.vaadin.gestionaulasinformatica.ui;

// Imports Java
import java.time.LocalDate;
import java.util.List;

// Imports Vaadin
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.timepicker.TimePicker;

// Imports backend
import com.vaadin.gestionaulasinformatica.backend.service.*;

public class FiltrosConsulta extends FormLayout {

	private static final long serialVersionUID = 1L;
	
	private DatePicker fechaDesde;
	private DatePicker fechaHasta;
	private TimePicker horaDesde;
	private TimePicker horaHasta;
	private NumberField capacidad;
	private NumberField numOrdenadores;
	private Select<String> centroDepartamento;
	
	private PropietarioAulaService propietarioAulaService;

	public FiltrosConsulta(PropietarioAulaService propietarioAulaService) {
		try {

			this.propietarioAulaService = propietarioAulaService;
			
			// Se establecen 4 columnas
			setResponsiveSteps(new ResponsiveStep("25em", 1), new ResponsiveStep("25em", 2),
					new ResponsiveStep("25em", 3), new ResponsiveStep("25em", 4), new ResponsiveStep("25em", 5));

			// Se crean los campos para filtrar

			// Campo obligatorio - Por defecto con la fecha actual
			fechaDesde = new DatePicker();
			fechaDesde.setLabel("Fecha desde");
			fechaDesde.setRequiredIndicatorVisible(true);
			fechaDesde.setClearButtonVisible(true);
			fechaDesde.setValue(LocalDate.now());

			fechaHasta = new DatePicker();
			fechaHasta.setLabel("Fecha hasta");
			fechaDesde.setClearButtonVisible(true);

			horaDesde = new TimePicker();
			horaDesde.setLabel("Hora desde");
			fechaDesde.setClearButtonVisible(true);

			horaHasta = new TimePicker();
			horaHasta.setLabel("Hora hasta");
			fechaDesde.setClearButtonVisible(true);

			capacidad = new NumberField();
			capacidad.setLabel("Capacidad");
			capacidad.setHasControls(true);
			fechaDesde.setClearButtonVisible(true);

			numOrdenadores = new NumberField();
			numOrdenadores.setLabel("Nº Ordenadores");
			numOrdenadores.setHasControls(true);
			fechaDesde.setClearButtonVisible(true);

			// Campo obligatorio
			centroDepartamento = new Select<>();
			centroDepartamento.setLabel("Centro/Departamento");
			centroDepartamento.setItems(this.propietarioAulaService.findAllNombres());
			centroDepartamento.setPlaceholder("Seleccione");
			centroDepartamento.setRequiredIndicatorVisible(true);
			fechaDesde.setClearButtonVisible(true);

			// Se añaden los campos al formulario
			add(fechaDesde, horaDesde, capacidad);
			add(centroDepartamento, 2);
			add(fechaHasta, horaHasta, numOrdenadores);
		} catch (Exception e) {
			throw e;
		}
	}

	protected Boolean validarFiltros() {
		Boolean valido = false;
		try {

		} catch (Exception e) {
			throw e;
		}

		return valido;
	}

	/**
	 * Se limpian todos los filtros aplicados, se vacían los campos.
	 */
	protected void limpiarFiltros() {
		try {
			fechaDesde.setValue(LocalDate.now());
			fechaHasta.clear();
			horaDesde.clear();
			horaHasta.clear();
			capacidad.clear();
			numOrdenadores.clear();
			centroDepartamento.clear();
		} catch (Exception e) {
			throw e;
		}
	}
}
