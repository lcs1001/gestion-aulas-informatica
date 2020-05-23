package com.vaadin.gestionaulasinformatica;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.timepicker.TimePicker;

public class FiltrosConsulta extends FormLayout {
	private DatePicker fechaDesde;
	private DatePicker fechaHasta;
	private TimePicker horaDesde;
	private TimePicker horaHasta;
	private NumberField capacidad;
	private NumberField numOrdenadores;
	private Select<String> centroDepartamento;
	private Button btnConsultarReservas;
	private Button btnConsultarDispAulas;
	private HorizontalLayout lytBotones;

	public FiltrosConsulta() {
		try {
			// Se establecen 4 columnas
			setResponsiveSteps(new ResponsiveStep("25em", 1), new ResponsiveStep("25em", 2),
					new ResponsiveStep("25em", 3), new ResponsiveStep("25em", 4), new ResponsiveStep("25em", 5));

			// Se crean los campos para filtrar
			fechaDesde = new DatePicker();
			fechaDesde.setLabel("Fecha desde");

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
			
			centroDepartamento = new Select<>();
			centroDepartamento.setItems("", "Centro 1", "Centro 2", "Departamento 1", "Departamento 2");
			centroDepartamento.setPlaceholder("Seleccione");
			centroDepartamento.setLabel("Centro/Departamento");
			
			// Botones para consultar y limpiar filtros
			btnConsultarReservas = new Button("Consultar Reservas");
			btnConsultarReservas.addThemeVariants(ButtonVariant.LUMO_PRIMARY);		
			btnConsultarReservas.setSizeFull();
			
			btnConsultarDispAulas = new Button("Consultar Disponibilidad Aulas");
			btnConsultarDispAulas.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			btnConsultarDispAulas.setSizeFull();
			
			lytBotones = new HorizontalLayout();
			lytBotones.add(btnConsultarReservas, btnConsultarDispAulas);
			

			// Se añaden los campos al formulario
			add(fechaDesde, horaDesde, capacidad); 
			add(centroDepartamento,2);
			add(fechaHasta, horaHasta, numOrdenadores);
			add(lytBotones);

		} catch (Exception e) {
			throw e;
		}
	}
}
