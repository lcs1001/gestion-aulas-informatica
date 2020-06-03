package com.vaadin.gestionaulasinformatica.ui;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.gestionaulasinformatica.backend.entity.*;
import com.vaadin.gestionaulasinformatica.backend.repository.IReservaRepository;
import com.vaadin.gestionaulasinformatica.backend.service.ReservaService;

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

	private FiltrosConsulta filtrosConsulta;

	private Grid<Reserva> gridReservas;

	/**
	 * Constructor de la ventana ConsultaAulasView.
	 * 
	 * @param reservaService Service de JPA para acceder al backend
	 */
	public ConsultaAulasView(ReservaService reservaService) {
		this.reservaService = reservaService;

		// Nombre de la clase CSS, para los estilos de CSS
		addClassName("consulta-view");

		// Tamaño de la ventana ajustado al del navegador
		setSizeFull();

		// Instanciación del formulario de filtros
		filtrosConsulta = new FiltrosConsulta();

		gridReservas = new Grid<>(Reserva.class);

		configurarGridReservas();

		add(filtrosConsulta, gridReservas);

		consultarReservas();		
	}

	/**
	 * Función para configurar el grid que muestra las reservas.
	 */
	private void configurarGridReservas() {
		gridReservas.addClassName("reservas-grid");
		gridReservas.setSizeFull();
		
		gridReservas.setColumns("fechaInicio", "fechaFin", "diaSemana", "horaInicio", "horaFin", "aula", "motivo");
		gridReservas.addColumn(Reserva::getACargoDe).setHeader("A cargo de").setKey("aCargoDe");

		// Ancho de columna automático
		gridReservas.getColumns().forEach(columnaReserva -> columnaReserva.setAutoWidth(true));

		// Formato visual del grid
		gridReservas.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
				GridVariant.LUMO_ROW_STRIPES);
	}

	private void consultarReservas() {
		
		gridReservas.setItems(reservaService.findAll());
	}

}
