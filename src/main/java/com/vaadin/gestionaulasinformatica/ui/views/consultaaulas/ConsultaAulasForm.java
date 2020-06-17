package com.vaadin.gestionaulasinformatica.ui.views.consultaaulas;

//Imports Java
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
* Clase que contiene el formulario para filtrar la
* disponibilidad de aulas.
* 
* @author Lisa
*
*/
public class ConsultaAulasForm extends FormLayout {
	private static final long serialVersionUID = 1L;

	private List<PropietarioAula> lstPropietariosAulas;

	protected DatePicker fechaDesde;
	protected DatePicker fechaHasta;
	protected TimePicker horaDesde;
	protected TimePicker horaHasta;
	protected NumberField capacidad;
	protected NumberField numOrdenadores;
	protected ComboBox<PropietarioAula> propietario;

	/**
	 * Constructor de la clase
	 * 
	 * @param propietarios Lista de responsables (PropietarioAula) que se muestra en
	 *                     el desplegable de responsables
	 */
	public ConsultaAulasForm(List<PropietarioAula> propietarios) {
		try {
			addClassName("consulta-aulas-form");

			this.lstPropietariosAulas = propietarios;

			setResponsiveSteps(new ResponsiveStep("25em", 1), new ResponsiveStep("25em", 2),
					new ResponsiveStep("25em", 3), new ResponsiveStep("25em", 4), new ResponsiveStep("25em", 5));

			configurarFiltros();

			add(fechaDesde, horaDesde, capacidad);
			add(propietario, 2);
			add(fechaHasta, horaHasta, numOrdenadores);
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
			fechaDesde.setPlaceholder("dd/MM/yyyy");
			fechaDesde.setLocale(localeSpain); // Formato dd/M/yyyy
			fechaDesde.setClearButtonVisible(true);

			fechaHasta = new DatePicker("Fecha hasta");
			fechaHasta.setMin(fechaDesde.getValue()); // Como mínimo debe ser la fecha desde la que se ha filtrado
			fechaHasta.setPlaceholder("dd/MM/yyyy");
			fechaHasta.setLocale(localeSpain); // Formato dd/M/yyyy
			fechaHasta.setClearButtonVisible(true);

			horaDesde = new TimePicker("Hora desde");
			horaDesde.setPlaceholder("hh:mm");
			horaDesde.setLocale(localeSpain);
			horaDesde.setClearButtonVisible(true);

			horaHasta = new TimePicker("Hora hasta");
			horaHasta.setPlaceholder("hh:mm");
			horaHasta.setLocale(localeSpain);
			horaHasta.setClearButtonVisible(true);

			capacidad = new NumberField("Capacidad");
			capacidad.setMin(0);
			capacidad.setPlaceholder("0");
			capacidad.setHasControls(true);
			capacidad.setClearButtonVisible(true);

			numOrdenadores = new NumberField("Número de ordenadores");
			numOrdenadores.setMin(0);
			numOrdenadores.setPlaceholder("0");
			numOrdenadores.setHasControls(true);
			numOrdenadores.setClearButtonVisible(true);

			propietario = new ComboBox<PropietarioAula>("Centro/Departamento");
			propietario.setPlaceholder("Seleccione");
			propietario.setItems(lstPropietariosAulas);
			propietario.setItemLabelGenerator(PropietarioAula::getNombrePropietarioAula);
			propietario.setRequired(true); // Campo obligatorio
			propietario.setRequiredIndicatorVisible(true);

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
			fechaDesde.clear();
			fechaHasta.clear();
			horaDesde.clear();
			horaHasta.clear();
			capacidad.clear();
			numOrdenadores.clear();
			propietario.clear();
		} catch (Exception e) {
			throw e;
		}
	}
}
