package gestionaulasinformatica.ui.views.responsable.reservaaulas;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import gestionaulasinformatica.app.security.SecurityUtils;
import gestionaulasinformatica.backend.data.TipoOperacionHR;
import gestionaulasinformatica.backend.entity.HistoricoReservas;
import gestionaulasinformatica.backend.entity.Reserva;
import gestionaulasinformatica.backend.entity.Usuario;
import gestionaulasinformatica.backend.service.AulaService;
import gestionaulasinformatica.backend.service.HistoricoReservasService;
import gestionaulasinformatica.backend.service.PropietarioAulaService;
import gestionaulasinformatica.backend.service.ReservaService;
import gestionaulasinformatica.backend.service.UsuarioService;
import gestionaulasinformatica.ui.Comunes;
import gestionaulasinformatica.ui.MainLayout;
import gestionaulasinformatica.ui.Mensajes;

/**
 * Ventana Reserva de Aulas.
 */
@Route(value = "reservaAulas", layout = MainLayout.class)
@PageTitle("Reserva de Aulas")
@Secured("RESPONSABLE")
public class ReservaAulasView extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ReservaAulasView.class.getName());

	private ReservaService reservaService;
	private PropietarioAulaService propietarioAulaService;
	private AulaService aulaService;
	private HistoricoReservasService historicoReservasService;
	private UsuarioService usuarioService;
	private Comunes comunes;

	private ReservaAulasForm formulario;
	private HorizontalLayout toolbar;

	private Usuario responsableLogeado;

	/**
	 * Constructor de la clase.
	 * 
	 * @param reservaService         Service de JPA de la entidad Reserva
	 * @param propietarioAulaService Service de JPA de la entidad PropietarioAula
	 * @param aulaService            Service de JPA de la entidad Aula
	 */
	public ReservaAulasView(ReservaService reservaService, PropietarioAulaService propietarioAulaService,
			AulaService aulaService, HistoricoReservasService historicoReservasService, UsuarioService usuarioService) {
		Div contenido;

		try {
			this.reservaService = reservaService;
			this.propietarioAulaService = propietarioAulaService;
			this.aulaService = aulaService;
			this.historicoReservasService = historicoReservasService;
			this.usuarioService = usuarioService;
			this.comunes = new Comunes();

			responsableLogeado = this.usuarioService.findByCorreoUsuarioIgnoreCase(SecurityUtils.getUsername());

			addClassName("reserva-aulas-view");
			setSizeFull();

			formulario = new ReservaAulasForm(this.aulaService,
					this.propietarioAulaService.findAllPropietariosResponsable(responsableLogeado), comunes,
					responsableLogeado);
			formulario.addListener(ReservaAulasForm.SaveEvent.class, this::guardarReserva);
			formulario.addListener(ReservaAulasForm.SaveRangeEvent.class, this::guardarReservaRango);

			configurarToolbar();

			contenido = new Div(comunes.getTituloVentana("Reserva de aulas"), formulario, toolbar);
			contenido.addClassName("reserva-aulas-contenido");
			contenido.setSizeFull();

			add(contenido);

			formulario.setVisible(false);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que configura el toolbar que contiene un botón para añadir la
	 * reserva.
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
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que permite reservar un aula.
	 */
	private void anadirReserva() {
		Reserva reserva;
		try {
			formulario.setVisible(true);
			toolbar.setVisible(false);
			reserva = new Reserva();
			formulario.setReserva(reserva, true);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que guarda la reserva (de un solo día), y la operación de creación en
	 * el histórico de reservas, en la base de datos.
	 * 
	 * @param evt Evento de guardado
	 */
	private void guardarReserva(ReservaAulasForm.SaveEvent evt) {
		Reserva reserva;
		String lugarReserva;
		HistoricoReservas operacionReserva;
		try {
			reserva = evt.getReserva();
			reservaService.save(reserva);

			lugarReserva = reserva.getAula().getNombreAula() + " - "
					+ reserva.getAula().getUbicacionCentro().getNombrePropietarioAula();
			operacionReserva = new HistoricoReservas(LocalDateTime.now(), TipoOperacionHR.CREACIÓN, reserva.getMotivo(),
					reserva.getFecha(), reserva.getHoraInicio(), reserva.getHoraFin(), lugarReserva,
					reserva.getACargoDe(), responsableLogeado.getNombreApellidosUsuario(),
					reserva.getAula().getPropietarioAula().getIdPropietarioAula());
			historicoReservasService.save(operacionReserva);

			formulario.setReserva(null, false);
			formulario.setVisible(false);
			toolbar.setVisible(true);

		} catch (Exception e) {
			comunes.mostrarNotificacion(Mensajes.MSG_ERROR_ACCION.getMensaje(), 3000, NotificationVariant.LUMO_ERROR);
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que guarda las reservas de rango, y la operación de creación en el
	 * histórico de reservas, en la base de datos.
	 * 
	 * @param evt Evento de guardado
	 */
	private void guardarReservaRango(ReservaAulasForm.SaveRangeEvent evt) {
		List<Reserva> lstReservas;
		String lugarReserva;
		HistoricoReservas operacionReserva;
		try {
			lstReservas = evt.getReservas();

			for (Reserva reserva : lstReservas) {
				reservaService.save(reserva);

				lugarReserva = reserva.getAula().getNombreAula() + " - "
						+ reserva.getAula().getUbicacionCentro().getNombrePropietarioAula();
				operacionReserva = new HistoricoReservas(LocalDateTime.now(), TipoOperacionHR.CREACIÓN,
						reserva.getMotivo(), reserva.getFecha(), reserva.getHoraInicio(), reserva.getHoraFin(),
						lugarReserva, reserva.getACargoDe(), responsableLogeado.getNombreApellidosUsuario(),
						reserva.getAula().getPropietarioAula().getIdPropietarioAula());
				historicoReservasService.save(operacionReserva);
			}

			formulario.setReserva(null, false);
			formulario.setVisible(false);
			toolbar.setVisible(true);

		} catch (Exception e) {
			comunes.mostrarNotificacion(Mensajes.MSG_ERROR_ACCION.getMensaje(), 3000, NotificationVariant.LUMO_ERROR);
			LOGGER.error(e.getMessage());
			throw e;
		}
	}
}
