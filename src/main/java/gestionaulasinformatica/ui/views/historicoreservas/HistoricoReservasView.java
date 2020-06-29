package gestionaulasinformatica.ui.views.historicoreservas;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import gestionaulasinformatica.backend.entity.HistoricoReservas;
import gestionaulasinformatica.backend.service.HistoricoReservasService;
import gestionaulasinformatica.ui.Comunes;
import gestionaulasinformatica.ui.MainLayout;
import gestionaulasinformatica.ui.Mensajes;

/**
 * Ventana Histórico de Reservas, que muestra todas las operaciones realizadas
 * sobre una reserva (creación, modificación, borrado).
 */
@Route(value = "historicoReservas", layout = MainLayout.class)
@PageTitle("Histórico de Reservas")
public class HistoricoReservasView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private HistoricoReservasService historicoReservasService;

	private Comunes comunes;

	private Grid<HistoricoReservas> gridHistorico;
	private HistoricoReservasForm formulario;
	private HorizontalLayout toolbar;

	public HistoricoReservasView(HistoricoReservasService historicoReservasService) {
		Div contenido;

		try {
			this.historicoReservasService = historicoReservasService;
			this.comunes = new Comunes();

			addClassName("historico-reservas-view");
			setSizeFull();

			formulario = new HistoricoReservasForm();

			configurarToolbar();
			configurarGridHistorico();

			contenido = new Div(comunes.getTituloVentana("Histórico de reservas"), formulario, toolbar, gridHistorico);
			contenido.addClassName("historico-reservas-contenido");
			contenido.setSizeFull();

			add(contenido);
			actualizarHR();

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura el layout de botones para filtrar el histórico.
	 * 
	 * @return Layout de botones
	 */
	private HorizontalLayout configurarToolbar() {
		Button btnBuscar;
		Button btnLimpiarFiltros;

		try {
			btnBuscar = new Button("Buscar", event -> actualizarHR());
			btnBuscar.setIcon(new Icon(VaadinIcon.SEARCH));
			btnBuscar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			btnLimpiarFiltros = new Button("", event -> limpiarFiltros());
			btnLimpiarFiltros.setIcon(new Icon(VaadinIcon.CLOSE));
			btnLimpiarFiltros.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			btnLimpiarFiltros.getElement().setProperty("title", "Limpiar filtros");

			toolbar = new HorizontalLayout(btnBuscar, btnLimpiarFiltros);
			toolbar.addClassName("toolbar");

			return toolbar;
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * Función que configura el grid que muestra el histórico de reservas.
	 */
	private void configurarGridHistorico() {
		try {
			gridHistorico = new Grid<>();
			gridHistorico.addClassName("historico-reservas-grid");

			gridHistorico.addColumn(new LocalDateRenderer<>(HistoricoReservas::getFechaOperacion, "dd/MM/yyyy"))
					.setHeader("Fecha").setKey("fechaOperacion");
			gridHistorico.addColumn(HistoricoReservas::getTipoOperacion).setHeader("Operación").setKey("tipoOperacion");
			gridHistorico.addColumn(new LocalDateRenderer<>(HistoricoReservas::getFechaReserva, "dd/MM/yyyy"))
					.setHeader("Fecha reserva").setKey("fechaReserva");
			gridHistorico.addColumn(HistoricoReservas::getHoraInicioReserva).setHeader("Hora inicio")
					.setKey("horaInicioReserva");
			gridHistorico.addColumn(HistoricoReservas::getHoraFinReserva).setHeader("Hora fin")
					.setKey("horaFinReserva");
			gridHistorico.addColumn(HistoricoReservas::getLugarReserva).setHeader("Lugar").setKey("lugarReserva");
			gridHistorico.addColumn(HistoricoReservas::getACargoDeReserva).setHeader("A cargo de")
					.setKey("aCargoDeReserva");
			gridHistorico.addColumn(HistoricoReservas::getResponsableOperacion).setHeader("Registrada por")
					.setKey("responsableOperacion");

			gridHistorico.getColumns().forEach(columna -> columna.setAutoWidth(true));

			gridHistorico.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
					GridVariant.LUMO_ROW_STRIPES);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que limpia los filtros aplicados y actualiza el grid.
	 */
	private void limpiarFiltros() {
		try {
			formulario.limpiarFiltros();
			actualizarHR();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que comprueba si los filtros introducidos para consultar el histórico
	 * de reservas son correctos.
	 * 
	 * @return Si los filtros introducidos para consultar el histórico de reservas
	 *         son correctos
	 */
	private Boolean validarFiltrosHR() {
		Boolean valido = true;

		try {
			// Si la fecha desde la que se quiere filtrar es mayor que la fecha hasta la que
			// se quiere filtrar
			if (!formulario.fechaDesde.isEmpty() && !formulario.fechaHasta.isEmpty()) {
				if (formulario.fechaDesde.getValue().compareTo(formulario.fechaHasta.getValue()) > 0) {
					comunes.mostrarNotificacion(Mensajes.MSG_CONSULTA_FECHA_DESDE_MAYOR.getMensaje(), 5000,
							NotificationVariant.LUMO_ERROR);
					valido = false;
				}
			}
			return valido;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que actualiza el grid que muestra las operaciones realizadas sobre
	 * las reservas.
	 */
	private void actualizarHR() {
		List<HistoricoReservas> lstOperacionesHR;
		try {
			gridHistorico.setVisible(false);

			if (validarFiltrosHR()) {
				lstOperacionesHR = historicoReservasService.findAll(formulario.fechaDesde.getValue(),
						formulario.fechaHasta.getValue());

				if (!lstOperacionesHR.isEmpty()) {
					gridHistorico.setVisible(true);
					gridHistorico.setItems(lstOperacionesHR);
				} else {
					comunes.mostrarNotificacion(Mensajes.MSG_NO_OPERACIONES_HR.getMensaje(), 5000, null);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
