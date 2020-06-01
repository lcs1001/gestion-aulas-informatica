package com.vaadin.gestionaulasinformatica.ui;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.timepicker.TimePicker;

public class FiltrosConsulta extends FormLayout {
	
	private final SimpleDateFormat formatoFecha;
	
	private DatePicker fechaDesde;
	private DatePicker fechaHasta;
	private TimePicker horaDesde;
	private TimePicker horaHasta;
	private NumberField capacidad;
	private NumberField numOrdenadores;
	private Select<String> centroDepartamento;
	
	private Button btnConsultarReservas;
	private Button btnConsultarDispAulas;
	private Button btnLimpiarFiltros;
	
	private HorizontalLayout lytBotones;
	

	public FiltrosConsulta() {
		try {
			formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
			
			// Se establecen 4 columnas
			setResponsiveSteps(new ResponsiveStep("25em", 1), new ResponsiveStep("25em", 2),
					new ResponsiveStep("25em", 3), new ResponsiveStep("25em", 4), new ResponsiveStep("25em", 5));

			// Se crean los campos para filtrar

			// Campo obligatorio - Por defecto con la fecha actual
			fechaDesde = new DatePicker();
			fechaDesde.setLabel("Fecha desde");
			fechaDesde.setRequiredIndicatorVisible(true);
			fechaDesde.setValue(LocalDate.now());

			fechaHasta = new DatePicker();
			fechaHasta.setLabel("Fecha hasta");

			horaDesde = new TimePicker();
			horaDesde.setLabel("Hora desde");

			horaHasta = new TimePicker();
			horaHasta.setLabel("Hora hasta");

			capacidad = new NumberField();
			capacidad.setLabel("Capacidad");
			capacidad.setHasControls(true);

			numOrdenadores = new NumberField();
			numOrdenadores.setLabel("Nº Ordenadores");
			numOrdenadores.setHasControls(true);

			// Campo obligatorio
			centroDepartamento = new Select<>();
			centroDepartamento.setLabel("Centro/Departamento");
			centroDepartamento.setItems("", "Centro 1", "Centro 2", "Departamento 1", "Departamento 2");
			centroDepartamento.setPlaceholder("Seleccione");
			centroDepartamento.setRequiredIndicatorVisible(true);

			// Botones para consultar y limpiar filtros
			btnConsultarReservas = new Button("Consultar Reservas", event -> consultarReservas());
			btnConsultarReservas.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			btnConsultarReservas.setSizeFull();

			btnConsultarDispAulas = new Button("Consultar Disponibilidad Aulas",
					event -> consultarDisponibilidadAulas());
			btnConsultarDispAulas.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			btnConsultarDispAulas.setSizeFull();

			btnLimpiarFiltros = new Button("", new Icon(VaadinIcon.CLOSE), event -> limpiarFiltros());
			btnLimpiarFiltros.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			lytBotones = new HorizontalLayout();
			lytBotones.add(btnConsultarReservas, btnConsultarDispAulas, btnLimpiarFiltros);

			// Se añaden los campos al formulario
			add(fechaDesde, horaDesde, capacidad);
			add(centroDepartamento, 2);
			add(fechaHasta, horaHasta, numOrdenadores);
			add(lytBotones, 2);

		} catch (Exception e) {
			throw e;
		}
	}

	private void consultarReservas() {
		try {

		} catch (Exception e) {
			throw e;
		}
	}

	private void consultarDisponibilidadAulas() {
		try {

		} catch (Exception e) {
			throw e;
		}
	}

	private Boolean validarFiltros() {
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
	private void limpiarFiltros() {
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
