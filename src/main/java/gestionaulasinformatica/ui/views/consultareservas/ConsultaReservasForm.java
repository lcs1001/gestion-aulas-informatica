package gestionaulasinformatica.ui.views.consultareservas;

import java.time.LocalDate;
import java.util.List;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.timepicker.TimePicker;

import gestionaulasinformatica.backend.entity.PropietarioAula;
import gestionaulasinformatica.ui.Comunes;

/**
 * Clase que contiene el formulario para filtrar las reservas.
 * 
 * @author Lisa
 *
 */
public class ConsultaReservasForm extends FormLayout {
	private static final long serialVersionUID = 1L;

	private List<PropietarioAula> lstPropietariosAulas;
	private Comunes comunes;

	protected DatePicker fechaDesde;
	protected DatePicker fechaHasta;
	protected TimePicker horaDesde;
	protected TimePicker horaHasta;
	protected ComboBox<String> diaSemana;
	protected ComboBox<PropietarioAula> propietario;

	/**
	 * Constructor de la clase.
	 * 
	 * @param propietarios Lista de responsables (PropietarioAula) que se muestra en
	 *                     el desplegable de responsables
	 * @param comunes      Objeto Comunes para tener acceso a las funciones comunes
	 */
	public ConsultaReservasForm(List<PropietarioAula> propietarios, Comunes comunes) {
		try {
			addClassName("consulta-reservas-form");

			this.lstPropietariosAulas = propietarios;
			this.comunes = comunes;

			setResponsiveSteps(new ResponsiveStep("25em", 1), new ResponsiveStep("25em", 2),
					new ResponsiveStep("25em", 3), new ResponsiveStep("25em", 4));

			configurarFiltros();

			add(fechaDesde, horaDesde);
			add(diaSemana, 2);
			add(fechaHasta, horaHasta);
			add(propietario, 2);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura los campos de filtrado.
	 */
	private void configurarFiltros() {
		try {

			fechaDesde = new DatePicker("Fecha desde");
			fechaDesde.setValue(LocalDate.now()); // Por defecto la fecha actual
			fechaDesde.setLocale(comunes.getLocaleES()); // Formato dd/M/yyyy

			fechaHasta = new DatePicker("Fecha hasta");
			fechaHasta.setMin(fechaDesde.getValue()); // Como mínimo debe ser la fecha desde la que se ha filtrado
			fechaHasta.setPlaceholder("dd/MM/yyyy");
			fechaHasta.setLocale(comunes.getLocaleES()); // Formato dd/M/yyyy
			fechaHasta.setClearButtonVisible(true);

			horaDesde = new TimePicker("Hora desde");
			horaDesde.setPlaceholder("hh:mm");
			horaDesde.setLocale(comunes.getLocaleES());
			horaDesde.setClearButtonVisible(true);
			horaDesde.addValueChangeListener(e -> establecerMinHoraHasta());

			horaHasta = new TimePicker("Hora hasta");
			horaHasta.setPlaceholder("hh:mm");
			horaHasta.setLocale(comunes.getLocaleES());
			horaHasta.setClearButtonVisible(true);

			diaSemana = new ComboBox<String>("Día de la semana");
			diaSemana.setPlaceholder("Seleccione");
			diaSemana.setItems(comunes.getDiasSemana());
			diaSemana.setClearButtonVisible(true);

			propietario = new ComboBox<PropietarioAula>("Centro/Departamento");
			propietario.setPlaceholder("Seleccione");
			propietario.setItems(lstPropietariosAulas);
			propietario.setItemLabelGenerator(PropietarioAula::getNombrePropietarioAula);
			propietario.setRequiredIndicatorVisible(true); // Campo obligatorio

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que establece como "hora hasta" mínima la "hora desde" elegida.
	 */
	private void establecerMinHoraHasta() {
		try {
			if (!horaDesde.isEmpty()) {
				horaHasta.setMinTime(horaDesde.getValue());
			}
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
			diaSemana.clear();
			propietario.clear();
		} catch (Exception e) {
			throw e;
		}
	}
}
