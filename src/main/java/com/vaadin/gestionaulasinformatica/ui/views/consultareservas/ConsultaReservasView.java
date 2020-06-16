package com.vaadin.gestionaulasinformatica.ui.views.consultareservas;

// Imports Vaadin
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

// Imports backend
import com.vaadin.gestionaulasinformatica.backend.entity.Aula;
import com.vaadin.gestionaulasinformatica.backend.entity.Reserva;
import com.vaadin.gestionaulasinformatica.backend.service.PropietarioAulaService;
import com.vaadin.gestionaulasinformatica.backend.service.ReservaService;

// Imports UI
import com.vaadin.gestionaulasinformatica.ui.Comunes;
import com.vaadin.gestionaulasinformatica.ui.MainLayout;
import com.vaadin.gestionaulasinformatica.ui.Mensajes;

/**
 * Ventana Consulta de Reservas.
 */
@Route(value = "consultaReservas", layout = MainLayout.class)
@PageTitle("Consulta de Reservas")
public class ConsultaReservasView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private ReservaService reservaService;
	private PropietarioAulaService propietarioAulaService;
	private Comunes comunes;

	private Grid<Reserva> gridReservas;
	private ConsultaReservasForm formulario;
	private HorizontalLayout toolbar;

	/**
	 * Constructor de la clase.
	 * 
	 * @param reservaService         Service de JPA de la entidad Reserva
	 * @param propietarioAulaService Service de JPA de la entidad PropietarioAula
	 */
	public ConsultaReservasView(ReservaService reservaService, PropietarioAulaService propietarioAulaService) {
		Div contenido;

		try {
			this.reservaService = reservaService;
			this.propietarioAulaService = propietarioAulaService;
			this.comunes = new Comunes();

			addClassName("consulta-reservas-view");
			setSizeFull();

			configurarGrid();

			formulario = new ConsultaReservasForm(this.propietarioAulaService.findAll());

			contenido = new Div(formulario, crearButtonLayout(), gridReservas);
			contenido.addClassName("consulta-reservas-contenido");
			contenido.setSizeFull();

			add(contenido);

			// Sólo se muestra el grids cuando se hace una consulta válida
			comunes.ocultarGrid(gridReservas);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que crea el layout de botones para consultar las reservas y limpiar
	 * todos los filtros aplicados.
	 * 
	 * @return Layout de botones
	 */
	private HorizontalLayout crearButtonLayout() {
		Button btnConsultar;
		Button btnLimpiarFiltros;

		try {
			btnConsultar = new Button("Consultar Reservas", event -> consultarReservas());
			btnConsultar.setIcon(new Icon(VaadinIcon.SEARCH));
			btnConsultar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			btnLimpiarFiltros = new Button("", event -> limpiarFiltros());
			btnLimpiarFiltros.setIcon(new Icon(VaadinIcon.CLOSE));
			btnLimpiarFiltros.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			btnLimpiarFiltros.getElement().setProperty("title", "Limpiar filtros");

			toolbar = new HorizontalLayout(btnConsultar, btnLimpiarFiltros);
			toolbar.addClassName("consulta-reservas-toolbar");

			return toolbar;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura el grid que muestra las reservas.
	 */
	private void configurarGrid() {
		try {
			gridReservas = new Grid<>();
			gridReservas.addClassName("reservas-grid");
			gridReservas.setSizeFull();

			gridReservas.addColumn(new LocalDateRenderer<>(Reserva::getFecha, "dd/MM/yyyy")).setHeader("Fecha inicio")
					.setKey("fechaInicio");

			gridReservas.addColumn(Reserva::getDiaSemana).setHeader("Día semana").setKey("diaSemana");

			gridReservas.addColumn(Reserva::getHoraInicio).setHeader("Hora inicio").setKey("horaInicio");

			gridReservas.addColumn(Reserva::getHoraFin).setHeader("Hora fin").setKey("horaFin");

			gridReservas.addColumn(reserva -> {
				Aula aula = reserva.getAula();
				return aula == null ? "-" : aula.getNombreAula();
			}).setHeader("Aula").setKey("aula");

			gridReservas.addColumn(reserva -> {
				Aula aula = reserva.getAula();
				return aula == null ? "-" : aula.getNombreCentro();
			}).setHeader("Centro").setKey("centro");

			gridReservas.addColumn(Reserva::getMotivo).setHeader("Motivo").setKey("motivo");

			gridReservas.addColumn(Reserva::getACargoDe).setHeader("A cargo de").setKey("aCargoDe");

			gridReservas.getColumns().forEach(columna -> columna.setAutoWidth(true));

			gridReservas.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
					GridVariant.LUMO_ROW_STRIPES);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que limpia todos los filtros aplicados y oculta los grids.
	 */
	private void limpiarFiltros() {
		try {
			formulario.limpiarFiltros();
			comunes.ocultarGrid(gridReservas);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que comprueba si los filtros introducidos para consultar las reservas
	 * son correctos.
	 * 
	 * @return Si los filtros introducidos para consultar las reservas son correctos
	 */
	private Boolean validarFiltrosConsultaReservas() {
		Boolean valido = true;
		String msgAlerta = "";

		try {
			// Si no se ha introducido el filtro de Centro/Departamento
			if (formulario.responsable.getValue() == null) {
				msgAlerta += " " + Mensajes.MSG_CONSULTA_RESPONSABLE.getMensaje();
				valido = false;
			}

			// Si la hora desde la que se quiere filtrar es mayor que la hora hasta la que
			// se quiere filtrar
			if (formulario.horaDesde.getValue() != null && formulario.horaHasta.getValue() != null) {
				if (formulario.horaDesde.getValue().compareTo(formulario.horaHasta.getValue()) > 0) {
					msgAlerta += " " + Mensajes.MSG_CONSULTA_HORA_DESDE_MAYOR.getMensaje();
					valido = false;
				}
			}

			// Si no es válido se muestra la alerta
			if (!valido) {
				comunes.mostrarNotificacion(msgAlerta, 5000, NotificationVariant.LUMO_ERROR);
			}
			return valido;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que permite consultar las reservas con los filtros aplicados.
	 */
	private void consultarReservas() {
		try {
			comunes.ocultarGrid(gridReservas);

			if (validarFiltrosConsultaReservas()) {
				gridReservas.setVisible(true);

				gridReservas.setItems(reservaService.findAll(formulario.fechaDesde.getValue(),
						formulario.fechaHasta.getValue(), formulario.horaDesde.getValue(),
						formulario.horaHasta.getValue(), formulario.responsable.getValue()));
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
