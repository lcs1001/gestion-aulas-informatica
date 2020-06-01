package com.vaadin.gestionaulasinformatica.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Ventana principal que permite consultar las reservas registradas y la
 * disponibilidad de las aulas.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 * <p>
 */
@Route("")
public class ConsultaReservasAulas extends VerticalLayout {

	private FiltrosConsulta filtrosConsulta;
	
	/**
	 * Constructor de la clase.
	 */
	public ConsultaReservasAulas() {

		// Instanciación del formulario de filtros
		filtrosConsulta = new FiltrosConsulta();

		add(filtrosConsulta);
	}

}
