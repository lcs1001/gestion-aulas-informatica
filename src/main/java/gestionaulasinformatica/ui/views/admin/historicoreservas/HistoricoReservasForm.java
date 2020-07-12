package gestionaulasinformatica.ui.views.admin.historicoreservas;

import java.time.LocalDate;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;

/**
 * Clase que contiene el formulario para filtrar el histórico de reservas por
 * fechas.
 * 
 * @author Lisa
 *
 */
public class HistoricoReservasForm extends FormLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(HistoricoReservasForm.class.getName());

	protected DatePicker fechaDesde;
	protected DatePicker fechaHasta;

	/**
	 * Constructor de la clase.
	 */
	public HistoricoReservasForm() {
		try {
			addClassName("historico-reservas-form");

			setResponsiveSteps(new ResponsiveStep("25em", 1), new ResponsiveStep("25em", 2),
					new ResponsiveStep("25em", 3), new ResponsiveStep("25em", 4));

			configurarFiltros();

			add(fechaDesde, 1);
			add(fechaHasta, 1);

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
			fechaDesde.setValue(LocalDate.now().minusDays(7)); // Por defecto desde 7 días antes de la fecha actual
			fechaDesde.setLocale(localeSpain); // Formato dd/M/yyyy
			fechaDesde.setClearButtonVisible(true);

			fechaHasta = new DatePicker("Fecha hasta");
			fechaHasta.setValue(LocalDate.now().plusDays(7)); // Por defecto hasta 7 días después de la fecha actual
			fechaHasta.setMin(fechaDesde.getValue()); // Como mínimo debe ser la fecha desde la que se ha filtrado
			fechaHasta.setLocale(localeSpain); // Formato dd/M/yyyy
			fechaHasta.setClearButtonVisible(true);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que limpia los valores de los filtros.
	 */
	protected void limpiarFiltros() {
		try {
			fechaDesde.clear();
			fechaHasta.clear();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

}
