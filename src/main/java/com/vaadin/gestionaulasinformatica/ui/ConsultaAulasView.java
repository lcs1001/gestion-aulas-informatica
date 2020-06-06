package com.vaadin.gestionaulasinformatica.ui;

// Imports Vaadin
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.Route;

// Imports backend
import com.vaadin.gestionaulasinformatica.backend.entity.*;
import com.vaadin.gestionaulasinformatica.backend.service.*;

/**
 * Ventana principal que permite consultar las reservas registradas y la
 * disponibilidad de las aulas.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 * <p>
 */
@Route("")
public class ConsultaAulasView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private ReservaService reservaService;
	private PropietarioAulaService propietarioAulaService;

	private FiltrosConsulta filtrosConsulta;

	private Button btnConsultarReservas;
	private Button btnConsultarDispAulas;
	private Button btnLimpiarFiltros;

	private HorizontalLayout lytBotones;

	private Grid<Reserva> gridReservas;

	/**
	 * Constructor de la ventana ConsultaAulasView.
	 * 
	 * @param reservaService Service de JPA para acceder al backend
	 */
	public ConsultaAulasView(ReservaService reservaService, PropietarioAulaService propietarioAulaService) {
		this.reservaService = reservaService;
		this.propietarioAulaService = propietarioAulaService;

		addClassName("consulta-view");

		// Se ajusta el tamaño de la ventana al del navegador
		setSizeFull();

		// Se instancia el formulario de filtros y se configuran los botones
		filtrosConsulta = new FiltrosConsulta(this.propietarioAulaService);
		configurarBotonesConsulta();

		// Se instancia el grid de reservas y se configura
		// Sólo se muestra cuando se pulse en el botón "Consultar reservas"
		gridReservas = new Grid<>();
		gridReservas.setVisible(false);
		configurarGridReservas();

		add(filtrosConsulta, lytBotones, gridReservas);
	}

	private void configurarBotonesConsulta() {
		// Botones para consultar y limpiar filtros
		btnConsultarReservas = new Button("Consultar Reservas", event -> consultarReservas());
		btnConsultarReservas.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		btnConsultarReservas.setSizeFull();

		btnConsultarDispAulas = new Button("Consultar Disponibilidad Aulas", event -> consultarDisponibilidadAulas());
		btnConsultarDispAulas.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		btnConsultarDispAulas.setSizeFull();

		btnLimpiarFiltros = new Button("", new Icon(VaadinIcon.CLOSE), event -> filtrosConsulta.limpiarFiltros());
		btnLimpiarFiltros.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		lytBotones = new HorizontalLayout();
		lytBotones.add(btnConsultarReservas, btnConsultarDispAulas, btnLimpiarFiltros);
	}

	/**
	 * Función para configurar el grid que muestra las reservas.
	 */
	private void configurarGridReservas() {
		gridReservas.addClassName("reservas-grid");
		gridReservas.setSizeFull();

		gridReservas.addColumn(new LocalDateRenderer<>(Reserva::getFechaInicio, "dd/MM/yyyy")).setHeader("Fecha inicio")
				.setKey("fechaInicio");

		gridReservas.addColumn(new LocalDateRenderer<>(Reserva::getFechaFin, "dd/MM/yyyy")).setHeader("Fecha fin")
				.setKey("fechaFin");
		
		gridReservas.addColumn(Reserva::getDiaSemana).setHeader("Día semana").setKey("diaSemana");
		
		gridReservas.addColumn(Reserva::getHoraInicio).setHeader("Hora inicio").setKey("horaInicio");
		
		gridReservas.addColumn(Reserva::getHoraFin).setHeader("Hora fin").setKey("horaFin");

		gridReservas.addColumn(reserva -> {
			Aula aula = reserva.getAula();
			return aula == null ? "-" : aula.getIdAula().getNombreAula();
		}).setHeader("Aula").setKey("aula");
		
		gridReservas.addColumn(reserva -> {
			Aula aula = reserva.getAula();
			return aula == null ? "-" : aula.getIdAula().getCentro();
		}).setHeader("Centro").setKey("centro");
		
		gridReservas.addColumn(Reserva::getMotivo).setHeader("Motivo").setKey("motivo");

		gridReservas.addColumn(Reserva::getACargoDe).setHeader("A cargo de").setKey("aCargoDe");

		// Se establece el ancho de columna automático
		gridReservas.getColumns().forEach(columnaReserva -> columnaReserva.setAutoWidth(true));

		// Se da un formato específico al grid
		gridReservas.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
				GridVariant.LUMO_ROW_STRIPES);
	}

	/**
	 * Función que permite consultar las reservas con los filtros aplicados.
	 */
	protected void consultarReservas() {
		try {
			gridReservas.setVisible(true);
			gridReservas.setItems(reservaService.findAll());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que permite consultar la disponibilidad de aulas con los filtros
	 * aplicados.
	 */
	protected void consultarDisponibilidadAulas() {
		try {

		} catch (Exception e) {
			throw e;
		}
	}

}
