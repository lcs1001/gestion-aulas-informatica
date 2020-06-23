package gestionaulasinformatica.ui.views.reservaaulas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import gestionaulasinformatica.backend.entity.Reserva;
import gestionaulasinformatica.backend.service.AulaService;
import gestionaulasinformatica.backend.service.PropietarioAulaService;
import gestionaulasinformatica.backend.service.ReservaService;
import gestionaulasinformatica.ui.Comunes;
import gestionaulasinformatica.ui.MainLayout;

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
	private Comunes comunes;

	private ReservaAulasForm formulario;
	private HorizontalLayout toolbar;

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
			this.comunes = new Comunes();

			addClassName("reserva-aulas-view");
			setSizeFull();

			formulario = new ReservaAulasForm(this.aulaService, this.propietarioAulaService.findAllCentros(), comunes);
			formulario.addListener(ReservaAulasForm.SaveEvent.class, this::guardarReserva);
			
			configurarToolbar();

			contenido = new Div(formulario, toolbar);
			contenido.addClassName("consulta-reservas-contenido");
			contenido.setSizeFull();

			add(contenido);

			formulario.setVisible(false);
			
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura el toolbar que contiene un botón para
	 * añadir la reserva.
	 * 
	 * @return Toolbar
	 */
	private void configurarToolbar() {
		Button btnAnadirReserva;
		try {
			btnAnadirReserva = new Button("Añadir reserva", click -> anadirReserva());
			btnAnadirReserva.setIcon(new Icon(VaadinIcon.PLUS));
			btnAnadirReserva.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			toolbar = new HorizontalLayout(btnAnadirReserva);
			toolbar.addClassName("toolbar");

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que permite reservar un aula.
	 */
	private void anadirReserva() {
		try {
			formulario.setVisible(true);
			toolbar.setVisible(false);
			formulario.setReserva(new Reserva());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que guarda la reserva en la base de datos.
	 * 
	 * @param e Evento de guardado
	 */
	private void guardarReserva(ReservaAulasForm.SaveEvent evt) {
		try {
			// TODO Validar reserva
			reservaService.save(evt.getReserva());
			formulario.setReserva(null);
			formulario.setVisible(false);
			toolbar.setVisible(true);

		} catch (Exception e) {
			throw e;
		}
	}
}
