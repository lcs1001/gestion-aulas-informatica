package com.vaadin.gestionaulasinformatica.ui;

import com.vaadin.flow.component.Component;
// Imports Vaadin
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.orderedlayout.*;
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

	private ConsultaAulasForm consultaAulasForm;

	private Grid<Reserva> gridReservas;

	private Button btnConsultarReservas;
	private Button btnConsultarDispAulas;
	private Button btnLimpiarFiltros;

	/**
	 * Constructor de la ventana ConsultaAulasView.
	 * 
	 * @param reservaService         Service de JPA de la entidad Reserva
	 * @param propietarioAulaService Service de JPA de la entidad PropietarioAula
	 */
	public ConsultaAulasView(ReservaService reservaService, PropietarioAulaService propietarioAulaService) {
		this.reservaService = reservaService;
		this.propietarioAulaService = propietarioAulaService;

		addClassName("consulta-view");

		// Se ajusta el tamaño de la ventana al del navegador
		setSizeFull();

		// Se instancia el grid de reservas, se oculta (sólo se muestra cuando se pulse
		// en el botón "Consultar reservas") y se configura
		gridReservas = new Grid<>();
		gridReservas.setVisible(false);
		configurarGridReservas();

		// Se instancia el form de consulta (filtros)
		consultaAulasForm = new ConsultaAulasForm(this.propietarioAulaService.findAll());

		Div contenido = new Div(consultaAulasForm, crearButtonLayout(), gridReservas);
		contenido.addClassName("contenido");
		contenido.setSizeFull();

		add(contenido);
	}

	private Component crearButtonLayout() {
		// Se crean los botones y el layout que los contiene
		btnConsultarReservas = new Button("Consultar Reservas", event -> consultarReservas());
		btnConsultarReservas.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		btnConsultarDispAulas = new Button("Consultar Disponibilidad Aulas", event -> consultarDisponibilidadAulas());
		btnConsultarDispAulas.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		btnLimpiarFiltros = new Button("", new Icon(VaadinIcon.CLOSE), event -> consultaAulasForm.limpiarFiltros());
		btnLimpiarFiltros.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		return new HorizontalLayout(btnConsultarReservas, btnConsultarDispAulas, btnLimpiarFiltros);
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
			return aula == null ? "-" : aula.getIdAula().getCentro().getNombrePropietarioAula();
		}).setHeader("Centro").setKey("centro");

		gridReservas.addColumn(Reserva::getMotivo).setHeader("Motivo").setKey("motivo");

		gridReservas.addColumn(Reserva::getACargoDe).setHeader("A cargo de").setKey("aCargoDe");

		// Se establece el ancho de columna automático
		gridReservas.getColumns().forEach(columnaReserva -> columnaReserva.setAutoWidth(true));

		// Se da un formato específico al grid
		gridReservas.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
				GridVariant.LUMO_ROW_STRIPES);
	}

	public Boolean validarFiltrosConsultaReservas() {
		Boolean valido = false;
		try {

			// Si se intenta filtrar por capacidad o número de ordenadores se muestra un
			// mensaje de alerta
//			if(filtrosConsulta.getfil)

		} catch (Exception e) {
			throw e;
		}

		return valido;
	}

	public Boolean validarFiltros() {
		Boolean valido = false;
		try {

		} catch (Exception e) {
			throw e;
		}

		return valido;
	}

	/**
	 * Función que permite consultar las reservas con los filtros aplicados.
	 */
	protected void consultarReservas() {
		try {
			// Se validan los filtros aplicados
//			if (validarFiltros()) {

			// Se hace visible el grid de reservas
			gridReservas.setVisible(true);

			// Se obtienen las reservas que cumplen con los filtros aplicados
			gridReservas.setItems(reservaService.findAll(consultaAulasForm.getFiltroFechaDesde(),
					consultaAulasForm.getFiltroFechaHasta(), consultaAulasForm.getFiltroHoraDesde(),
					consultaAulasForm.getFiltroHoraHasta(), consultaAulasForm.getFiltroPropietarioAula()));
//			}
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
