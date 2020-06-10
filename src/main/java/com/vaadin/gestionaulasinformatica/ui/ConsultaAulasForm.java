package com.vaadin.gestionaulasinformatica.ui;

//Imports Java
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

//Imports Vaadin
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.timepicker.TimePicker;

//Imports backend
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;

/**
 * Clase que contiene el formulario para filtrar las reservas y la
 * disponibilidad de aulas.
 * 
 * @author Lisa
 *
 */
public class ConsultaAulasForm extends FormLayout {
	private static final long serialVersionUID = 1L;

	private List<PropietarioAula> lstPropietariosAulas;

	protected DatePicker fechaDesde = new DatePicker("Fecha Desde");
	protected DatePicker fechaHasta = new DatePicker("Fecha hasta");
	protected TimePicker horaDesde = new TimePicker("Hora desde");
	protected TimePicker horaHasta = new TimePicker("Hora hasta");
	protected NumberField capacidad = new NumberField("Capacidad");
	protected NumberField numOrdenadores = new NumberField("Nº ordenadores");
	protected ComboBox<PropietarioAula> responsable = new ComboBox<PropietarioAula>("Centro/Departamento");

	public ConsultaAulasForm(List<PropietarioAula> propietarios) {
		try {
			addClassName("consulta-aulas-form");

			this.lstPropietariosAulas = propietarios;

			// Se establecen 4 columnas
			setResponsiveSteps(new ResponsiveStep("25em", 1), new ResponsiveStep("25em", 2),
					new ResponsiveStep("25em", 3), new ResponsiveStep("25em", 4), new ResponsiveStep("25em", 5));

			// Se configuran los campos de filtrado
			configurarFiltrosConsulta();

			// Se añaden los campos al formulario
			add(fechaDesde, horaDesde, capacidad);
			add(responsable, 2);
			add(fechaHasta, horaHasta, numOrdenadores);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura los campos de filtrado.
	 */
	private void configurarFiltrosConsulta() {
		try {
			// Campo obligatorio - Por defecto con la fecha actual
			fechaDesde.setValue(LocalDate.now());
			fechaDesde.setLocale(Locale.ITALY); // Para establecer el formato dd/MM/yyyy
			fechaDesde.setClearButtonVisible(true);

			fechaHasta.setMin(fechaDesde.getValue()); // Como mínimo debe ser la fecha desde la que se ha filtrado
			fechaHasta.setLocale(Locale.ITALY); // Para establecer el formato dd/MM/yyyy
			fechaHasta.setClearButtonVisible(true);

			horaDesde.setClearButtonVisible(true);

			horaHasta.setClearButtonVisible(true);

			capacidad.setHasControls(true);
			capacidad.setClearButtonVisible(true);

			numOrdenadores.setHasControls(true);
			numOrdenadores.setClearButtonVisible(true);

			// Campo obligatorio
			responsable.setPlaceholder("Seleccione");
			responsable.setItems(lstPropietariosAulas);
			responsable.setItemLabelGenerator(PropietarioAula::getNombrePropietarioAula);
			responsable.setRequiredIndicatorVisible(true);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que limpia todos los filtros aplicados, elimina sus valores o
	 * establece los de por defecto.
	 */
	protected void limpiarFiltros() {
		try {
			fechaDesde.setValue(LocalDate.now());
			fechaHasta.clear();
			horaDesde.clear();
			horaHasta.clear();
			capacidad.clear();
			numOrdenadores.clear();
			responsable.clear();
		} catch (Exception e) {
			throw e;
		}
	}
}
