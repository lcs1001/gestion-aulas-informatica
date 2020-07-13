package gestionaulasinformatica.ui.views.consultaaulas;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.timepicker.TimePicker;

import gestionaulasinformatica.backend.entity.Aula;
import gestionaulasinformatica.backend.entity.PropietarioAula;
import gestionaulasinformatica.backend.service.AulaService;
import gestionaulasinformatica.ui.Comunes;

/**
 * Clase que contiene el formulario para filtrar la disponibilidad de aulas.
 * 
 * @author Lisa
 *
 */
public class ConsultaAulasForm extends FormLayout {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsultaAulasForm.class.getName());

	private List<PropietarioAula> lstPropietariosAulas;
	private Comunes comunes;
	private AulaService aulaService;

	protected DatePicker fechaDesde;
	protected DatePicker fechaHasta;
	protected TimePicker horaDesde;
	protected TimePicker horaHasta;
	protected NumberField capacidad;
	protected NumberField numOrdenadores;
	protected ComboBox<String> diaSemana;
	protected ComboBox<PropietarioAula> propietario;
	protected ComboBox<Aula> aula;

	/**
	 * Constructor de la clase
	 * 
	 * @param propietarios Lista de responsables (PropietarioAula) que se muestra en
	 *                     el desplegable de responsables
	 * @param comunes      Objeto Comunes para tener acceso a las funciones comunes
	 * @param aulaService  Service de JPA de la entidad Aula
	 */
	public ConsultaAulasForm(List<PropietarioAula> propietarios, Comunes comunes, AulaService aulaService) {
		try {
			addClassName("consulta-aulas-form");

			this.lstPropietariosAulas = propietarios;
			this.comunes = comunes;
			this.aulaService = aulaService;

			setResponsiveSteps(new ResponsiveStep("25em", 1), new ResponsiveStep("25em", 2),
					new ResponsiveStep("25em", 3), new ResponsiveStep("25em", 4), new ResponsiveStep("25em", 5));

			configurarFiltros();

			add(fechaDesde, horaDesde, capacidad);
			add(propietario, 2);
			add(fechaHasta, horaHasta, numOrdenadores);
			add(aula, 2);
			add(diaSemana, 1);
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
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
			horaDesde.addValueChangeListener(e -> establecerMinHoraHasta());

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

			diaSemana = new ComboBox<String>("Día de la semana");
			diaSemana.setPlaceholder("Seleccione");
			diaSemana.setItems(comunes.getDiasSemana());
			diaSemana.setClearButtonVisible(true);

			propietario = new ComboBox<PropietarioAula>("Centro/Departamento");
			propietario.setPlaceholder("Seleccione");
			propietario.setItems(lstPropietariosAulas);
			propietario.setItemLabelGenerator(PropietarioAula::getNombrePropietarioAula);
			propietario.addValueChangeListener(e -> cargarAulasPropietario());
			propietario.setRequired(true); // Campo obligatorio
			propietario.setRequiredIndicatorVisible(true);

			aula = new ComboBox<Aula>("Aula");
			aula.setPlaceholder("Seleccione");
			aula.setItemLabelGenerator(Aula::getNombreAula);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
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
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Carga las aulas del propietario seleccionado en el desplegable de
	 * propietarios.
	 */
	private void cargarAulasPropietario() {
		List<Aula> lstAulas;
		try {
			if (!propietario.isEmpty()) {
				lstAulas = aulaService.findAllAulasPropietario(propietario.getValue());
				aula.setItems(lstAulas);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
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
			diaSemana.clear();
			propietario.clear();
			aula.clear();
			aula.setItems(new ArrayList<Aula>());
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}
}
