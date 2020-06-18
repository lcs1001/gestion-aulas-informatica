package com.vaadin.gestionaulasinformatica.ui.views.reservaaulas;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
// Imports Java
import java.util.List;
import java.util.Locale;

// Imports Vaadin
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;

//Imports backend
import com.vaadin.gestionaulasinformatica.backend.entity.Aula;
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.service.AulaService;

/**
 * Clase que contiene el formulario para reservar aulas.
 * 
 * @author Lisa
 *
 */
public class ReservaAulasForm extends FormLayout {

	private static final long serialVersionUID = 1L;

	private AulaService aulaService;
	private List<PropietarioAula> lstCentros;
	private List<String> lstDiasSemana;

	protected DatePicker fechaInicio;
	protected DatePicker fechaFin;
	protected TimePicker horaInicio;
	protected TimePicker horaFin;
	protected ComboBox<PropietarioAula> centro;
	protected ComboBox<Aula> aula;
	protected ComboBox<String> diaSemana;
	protected TextField motivo;
	protected TextField aCargoDe;

	/**
	 * Constructor de la clase
	 * 
	 * @param propietarios Lista de centros que se muestra en el desplegable de
	 *                     centros
	 */
	public ReservaAulasForm(AulaService aulaService, List<PropietarioAula> centros) {
		try {
			addClassName("reserva-aulas-form");

			this.aulaService = aulaService;
			this.lstCentros = centros;
			this.lstDiasSemana = new ArrayList<String>(
					Arrays.asList("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"));

			setResponsiveSteps(new ResponsiveStep("25em", 1), new ResponsiveStep("25em", 2),
					new ResponsiveStep("25em", 3), new ResponsiveStep("25em", 4));

			configurarFiltros();

			add(fechaInicio, horaInicio);
			add(centro, 2);
			add(fechaFin, horaFin);
			add(aula, 2);
			add(diaSemana, 2);
			add(motivo, 2);
			add(aCargoDe, 2);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura los campos de filtrado.
	 */
	private void configurarFiltros() {
		Locale localeSpain;

		try {
			localeSpain = new Locale("es", "ES");

			fechaInicio = new DatePicker("Fecha");
			fechaInicio.setMin(LocalDate.now()); // Como mínimo debe ser la fecha actual
			fechaInicio.setPlaceholder("dd/MM/yyyy");
			fechaInicio.setLocale(localeSpain); // Formato dd/M/yyyy
			fechaInicio.setClearButtonVisible(true);

			fechaFin = new DatePicker("Fecha fin");
			fechaFin.setMin(fechaInicio.getValue()); // Como mínimo debe ser la fecha desde la que se ha filtrado
			fechaFin.setPlaceholder("dd/MM/yyyy");
			fechaFin.setLocale(localeSpain); // Formato dd/M/yyyy
			fechaFin.setClearButtonVisible(true);

			horaInicio = new TimePicker("Hora inicio");
			horaInicio.setMinTime(LocalTime.now());
			horaInicio.setPlaceholder("hh:mm");
			horaInicio.setLocale(localeSpain);
			horaInicio.setClearButtonVisible(true);

			horaFin = new TimePicker("Hora fin");
			horaFin.setPlaceholder("hh:mm");
			horaFin.setLocale(localeSpain);
			horaFin.setClearButtonVisible(true);

			centro = new ComboBox<PropietarioAula>("Centro");
			centro.setPlaceholder("Seleccione");
			centro.setItems(lstCentros);
			centro.setItemLabelGenerator(PropietarioAula::getNombrePropietarioAula);
			centro.addValueChangeListener(e -> cargarAulasCentro());

			aula = new ComboBox<Aula>("Aula");
			aula.setPlaceholder("Seleccione");
			aula.setItemLabelGenerator(Aula::getNombreAula);

			diaSemana = new ComboBox<String>("Día de la semana");
			diaSemana.setPlaceholder("Seleccione");
			diaSemana.setItems(lstDiasSemana);

			motivo = new TextField("Motivo");
			motivo.setPlaceholder("Motivo de la reserva");
			motivo.setMaxLength(50);
			motivo.setClearButtonVisible(true);

			aCargoDe = new TextField("A cargo de");
			aCargoDe.setPlaceholder("Persona a cargo de la reserva");
			aCargoDe.setMaxLength(50);
			aCargoDe.setClearButtonVisible(true);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Carga las aulas del centro seleccionado en el desplegable de aulas.
	 */
	private void cargarAulasCentro() {
		List<Aula> lstAulas;
		try {
			if (!centro.isEmpty()) {
				lstAulas = aulaService.findAll(centro.getValue());
				aula.setItems(lstAulas);
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
