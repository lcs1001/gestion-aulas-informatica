package com.vaadin.gestionaulasinformatica;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import javax.annotation.PostConstruct;

/**
 * Ventana principal que permite consultar las reservas registradas y la
 * disponibilidad de las aulas.
 */
@Route("")
@PWA(name = "Gestión de Aulas de Informática", shortName = "Gestión Aulas Informática", enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class ConsultaReservasAulas extends VerticalLayout {

	private FiltrosConsulta filtrosConsulta;

	@PostConstruct
	public void init() {
		// Instanciación del formulario de filtros
		filtrosConsulta = new FiltrosConsulta();
		
		add(filtrosConsulta);
	}

}
