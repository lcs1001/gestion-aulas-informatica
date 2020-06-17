package com.vaadin.gestionaulasinformatica.ui.views.consultareservas;

//Imports Java
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

//Imports Vaadin
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.timepicker.TimePicker;

//Imports backend
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;

/**
 * Clase que contiene el formulario para filtrar las reservas.
 * 
 * @author Lisa
 *
 */
public class ConsultaReservasForm extends FormLayout {
	private static final long serialVersionUID = 1L;

	private List<PropietarioAula> lstPropietariosAulas;

	protected DatePicker fechaDesde;
	protected DatePicker fechaHasta;
	protected TimePicker horaDesde;
	protected TimePicker horaHasta;
	protected ComboBox<PropietarioAula> responsable;

	/**
	 * Constructor de la clase
	 * 
	 * @param propietarios Lista de responsables (PropietarioAula) que se muestra en
	 *                     el desplegable de responsables
	 */
	public ConsultaReservasForm(List<PropietarioAula> propietarios) {
		try {
			addClassName("consulta-reservas-form");

			this.lstPropietariosAulas = propietarios;

			configurarFiltros();

			add(fechaDesde, horaDesde, fechaHasta, horaHasta, responsable);
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

			fechaDesde = new DatePicker("Fecha desde");
			fechaDesde.setValue(LocalDate.now()); // Por defecto la fecha actual
			fechaDesde.setLocale(localeSpain); // Formato dd/M/yyyy

			fechaHasta = new DatePicker("Fecha hasta");
			fechaHasta.setMin(fechaDesde.getValue()); // Como mínimo debe ser la fecha desde la que se ha filtrado
			fechaHasta.setLocale(localeSpain); // Formato dd/M/yyyy
			fechaHasta.setClearButtonVisible(true);

			horaDesde = new TimePicker("Hora desde");
			horaDesde.setLocale(localeSpain);
			horaDesde.setClearButtonVisible(true);

			horaHasta = new TimePicker("Hora hasta");
			horaHasta.setLocale(localeSpain);
			horaHasta.setClearButtonVisible(true);

			responsable = new ComboBox<PropietarioAula>("Centro/Departamento");
			responsable.setPlaceholder("Seleccione");
			responsable.setItems(lstPropietariosAulas);
			responsable.setItemLabelGenerator(PropietarioAula::getNombrePropietarioAula);
			responsable.setRequiredIndicatorVisible(true); // Campo obligatorio

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
			responsable.clear();
		} catch (Exception e) {
			throw e;
		}
	}
}
