package com.vaadin.gestionaulasinformatica.ui.views.historicoreservas;

// Imports Vaadin
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.vaadin.gestionaulasinformatica.ui.MainLayout;

//Imports backend
import com.vaadin.gestionaulasinformatica.backend.service.HistoricoReservasService;
import com.vaadin.gestionaulasinformatica.backend.entity.HistoricoReservas;
import com.vaadin.gestionaulasinformatica.backend.entity.HistoricoReservasPK;
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.entity.Reserva;

/**
 * Ventana Histórico de Reservas, que muestra todas las operaciones realizadas
 * sobre una reserva (creación, modificación, borrado).
 */
@Route(value = "HistoricoReservas", layout = MainLayout.class)
@PageTitle("Histórico de Reservas")
public class HistoricoReservasView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private HistoricoReservasService historicoReservasService;

	private Grid<HistoricoReservas> gridHistorico;
	private HistoricoReservasForm formulario;
	private HorizontalLayout toolbar;

	public HistoricoReservasView(HistoricoReservasService historicoReservasService) {
		Div contenido;

		try {
			this.historicoReservasService = historicoReservasService;

			addClassName("historico-reservas-view");

			setSizeFull();

			gridHistorico = new Grid<>();
			configurarGridHistorico();

			formulario = new HistoricoReservasForm();

			contenido = new Div(formulario, getToolbar(), gridHistorico);
			contenido.addClassName("contenido");
			contenido.setSizeFull();

			add(contenido);

			actualizarHistorico();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que crea el layout de botones para filtrar el histórico.
	 * 
	 * @return Layout de botones
	 */
	private HorizontalLayout getToolbar() {
		Button btnBuscar;
		Button btnLimpiarFiltros;

		try {
			btnBuscar = new Button("Buscar", event -> actualizarHistorico());
			btnBuscar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			btnLimpiarFiltros = new Button("", new Icon(VaadinIcon.CLOSE), event -> formulario.limpiarFiltros());
			btnLimpiarFiltros.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			toolbar = new HorizontalLayout(btnBuscar, btnLimpiarFiltros);
			toolbar.addClassName("historico-reservas-toolbar");

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
			gridHistorico.addClassName("grid-historico-reservas");
			gridHistorico.setSizeFull();

			gridHistorico.addColumn(new LocalDateRenderer<>(HistoricoReservas::getFechaOperacion, "dd/MM/yyyy"))
					.setHeader("Fecha").setKey("fechaOperacion");

			gridHistorico.addColumn(operacion -> {
				HistoricoReservasPK historicoPK = operacion.getIdOperacionHR();
				return historicoPK == null ? "-" : historicoPK.getTipoOperacion();
			}).setHeader("Operación").setKey("operacion");

			gridHistorico.addColumn(operacion -> {
				Reserva reserva = operacion.getIdOperacionHR().getReserva();
				return reserva == null ? "-" : reserva.getNombreAula() + " - " + reserva.getNombreCentroAula();
			}).setHeader("Lugar").setKey("lugarReserva");

			gridHistorico
					.addColumn(new LocalDateRenderer<>(HistoricoReservas::getFechaInicioReservaOperacion, "dd/MM/yyyy"))
					.setHeader("Fecha inicio").setKey("fechaInicioReserva");

			gridHistorico
					.addColumn(new LocalDateRenderer<>(HistoricoReservas::getFechaFinReservaOperacion, "dd/MM/yyyy"))
					.setHeader("Fecha fin").setKey("fechaFinReserva");

			gridHistorico.addColumn(operacion -> {
				Reserva reserva = operacion.getIdOperacionHR().getReserva();
				return reserva == null ? "-" : (reserva.getHoraInicio() + " - " + reserva.getHoraFin());
			}).setHeader("Hora").setKey("horaReserva");

			gridHistorico.addColumn(operacion -> {
				PropietarioAula responsable = operacion.getResponsableOperacion();
				return responsable == null ? "-" : responsable.getNombrePropietarioAula();
			}).setHeader("Registrada por").setKey("responsableOperacion");

			gridHistorico.addColumn(operacion -> {
				Reserva reserva = operacion.getIdOperacionHR().getReserva();
				return reserva == null ? "-" : (reserva.getACargoDe());
			}).setHeader("A cargo de").setKey("aCargoDeReserva");

			gridHistorico.getColumns().forEach(columna -> columna.setAutoWidth(true));

			gridHistorico.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
					GridVariant.LUMO_ROW_STRIPES);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que actualiza el grid que muestra las operaciones realizadas sobre
	 * las reservas.
	 */
	private void actualizarHistorico() {
		try {
			gridHistorico.setItems(historicoReservasService.findAll(formulario.fechaDesde.getValue(),
					formulario.fechaHasta.getValue()));
		} catch (Exception e) {
			throw e;
		}
	}

}
