package com.vaadin.gestionaulasinformatica.ui.views.reservaaulas;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
// Imports Vaadin
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

//Imports backend
import com.vaadin.gestionaulasinformatica.backend.service.ReservaService;
import com.vaadin.gestionaulasinformatica.backend.service.AulaService;
import com.vaadin.gestionaulasinformatica.backend.service.PropietarioAulaService;

// Imports UI
import com.vaadin.gestionaulasinformatica.ui.MainLayout;

/**
 * Ventana Reserva de Aulas.
 */
@Route(value = "reservaAulas", layout = MainLayout.class)
@PageTitle("Reserva de Aulas")
public class ReservaAulasView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private ReservaService reservaService;
	private PropietarioAulaService propietarioAulaService;
	private AulaService aulaService;

	private ReservaAulasForm formulario;
	private HorizontalLayout toolbar;
	private Checkbox chkReservaRango;

	/**
	 * Constructor de la clase.
	 * 
	 * @param reservaService         Service de JPA de la entidad Reserva
	 * @param propietarioAulaService Service de JPA de la entidad PropietarioAula
	 * @param aulaService            Service de JPA de la entidad Aula
	 */
	public ReservaAulasView(ReservaService reservaService, PropietarioAulaService propietarioAulaService,
			AulaService aulaService) {
		Div contenido;

		try {
			this.reservaService = reservaService;
			this.propietarioAulaService = propietarioAulaService;
			this.aulaService = aulaService;

			addClassName("reserva-aulas-view");
			setSizeFull();

			formulario = new ReservaAulasForm(aulaService, this.propietarioAulaService.findAllCentros());

			contenido = new Div(formulario, getToolbar());
			contenido.addClassName("consulta-reservas-contenido");
			contenido.setSizeFull();

			add(contenido);

			// Se deshabilitan los campos "Fecha fin" y "Día de la semana" hasta que se
			// marca la reserva por rango de fechas
			formulario.fechaFin.setEnabled(false);
			formulario.diaSemana.setEnabled(false);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que crea el layout de botones para las reservas.
	 * 
	 * @return Layout de botones
	 */
	private HorizontalLayout getToolbar() {
		Button btnReservar;

		try {
			btnReservar = new Button("Reservar");
			btnReservar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			chkReservaRango = new Checkbox("Reserva por Rango de Fechas");
			chkReservaRango.addClickListener(e -> reservaRango());
			chkReservaRango.addClassName("resreva-chk-reserva-rango");

			toolbar = new HorizontalLayout(btnReservar, chkReservaRango);
			toolbar.addClassName("toolbar");

			return toolbar;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que muestra u oculta los campos de las reservas de rango (fecha fin y
	 * día de la semana) dependiendo de si se ha seleccionado el checkbox o no.
	 */
	private void reservaRango() {
		try {
			// Si no está seleccionado
			if (chkReservaRango.isEmpty()) {
				formulario.fechaInicio.setLabel("Fecha");
				formulario.fechaFin.setEnabled(false);
				formulario.diaSemana.setEnabled(false);
			} else {
				formulario.fechaInicio.setLabel("Fecha inicio");
				formulario.fechaFin.setEnabled(true);
				formulario.diaSemana.setEnabled(true);
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
