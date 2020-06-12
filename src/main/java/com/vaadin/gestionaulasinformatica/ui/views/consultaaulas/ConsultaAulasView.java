package com.vaadin.gestionaulasinformatica.ui.views.consultaaulas;

// Imports Vaadin
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
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
import com.vaadin.gestionaulasinformatica.ui.MainLayout;
import com.vaadin.gestionaulasinformatica.ui.Mensajes;

/**
 * Ventana Consulta de Reservas y Disponibilidad de Aulas.
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Consulta de Reservas y Disponibilidad de Aulas")
public class ConsultaAulasView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private ReservaService reservaService;
	private PropietarioAulaService propietarioAulaService;

	private ConsultaAulasForm formularioAulas;
	private HorizontalLayout toolbar;
	private Grid<Reserva> gridReservas;

	/**
	 * Constructor de la clase.
	 * 
	 * @param reservaService         Service de JPA de la entidad Reserva
	 * @param propietarioAulaService Service de JPA de la entidad PropietarioAula
	 */
	public ConsultaAulasView(ReservaService reservaService, PropietarioAulaService propietarioAulaService) {
		try {
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
			formularioAulas = new ConsultaAulasForm(this.propietarioAulaService.findAll());

			Div contenido = new Div(formularioAulas, crearButtonLayout(), gridReservas);
			contenido.addClassName("contenido");
			contenido.setSizeFull();

			add(contenido);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que crea el layout de botones para consultar las reservas, consultar
	 * la disponibilidad de aulas y para limpiar todos los filtros aplicados.
	 * 
	 * @return Layout de botones
	 */
	private HorizontalLayout crearButtonLayout() {
		Button btnConsultarReservas;
		Button btnConsultarDispAulas;
		Button btnLimpiarFiltros;
		
		try {
			// Se crean los botones y el layout que los contiene
			btnConsultarReservas = new Button("Consultar Reservas", event -> consultarReservas());
			btnConsultarReservas.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			btnConsultarDispAulas = new Button("Consultar Disponibilidad Aulas",
					event -> consultarDisponibilidadAulas());
			btnConsultarDispAulas.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			btnLimpiarFiltros = new Button("", new Icon(VaadinIcon.CLOSE), event -> formularioAulas.limpiarFiltros());
			btnLimpiarFiltros.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			toolbar = new HorizontalLayout(btnConsultarReservas, btnConsultarDispAulas, btnLimpiarFiltros);
			toolbar.addClassName("toolbar-consulta-aulas");
			
			return toolbar;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura el grid que muestra las reservas.
	 */
	private void configurarGridReservas() {
		try {
			gridReservas.addClassName("reservas-grid");
			gridReservas.setSizeFull();

			gridReservas.addColumn(new LocalDateRenderer<>(Reserva::getFechaInicio, "dd/MM/yyyy"))
					.setHeader("Fecha inicio").setKey("fechaInicio");

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
			gridReservas.getColumns().forEach(columna -> columna.setAutoWidth(true));

			// Se da un formato específico al grid
			gridReservas.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
					GridVariant.LUMO_ROW_STRIPES);
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
		Notification notificacion;
		String msgAlerta = "";
		
		try {
			// Si no se ha introducido el filtro de Centro/Departamento
			if (formularioAulas.responsable.getValue() == null) {
				msgAlerta += " " + Mensajes.MSG_CONSULTA_RESPONSABLE.getMensaje();
				valido = false;
			}

			// Si se intenta filtrar por capacidad
			if (formularioAulas.capacidad.getValue() != null) {
				msgAlerta += Mensajes.MSG_CONSULTA_RESERVA_CAP.getMensaje();
				valido = false;
			}

			// Si se intenta filtrar por número de ordenadores
			if (formularioAulas.numOrdenadores.getValue() != null) {
				msgAlerta += " " + Mensajes.MSG_CONSULTA_RESERVA_ORD.getMensaje();
				valido = false;
			}

			// Si la hora desde la que se quiere filtrar es mayor que la hora hasta la que
			// se quiere filtrar
			if (formularioAulas.horaDesde.getValue() != null && formularioAulas.horaHasta.getValue() != null) {
				if (formularioAulas.horaDesde.getValue().compareTo(formularioAulas.horaHasta.getValue()) > 0) {
					msgAlerta += " " + Mensajes.MSG_CONSULTA_HORA_DESDE_MAYOR.getMensaje();
					valido = false;
				}
			}

			// Si no es válido se muestra la alerta
			if (!valido) {
				notificacion = new Notification(msgAlerta, 5000);
				notificacion.addThemeVariants(NotificationVariant.LUMO_ERROR);
				notificacion.setPosition(Position.MIDDLE);
				notificacion.open();
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
			// Se oculta el grid por si ya se había mostrado
			gridReservas.setVisible(false);

			// Se validan los filtros aplicados
			if (validarFiltrosConsultaReservas()) {

				// Se hace visible el grid de reservas
				gridReservas.setVisible(true);

				// Se obtienen las reservas que cumplen con los filtros aplicados
				gridReservas.setItems(reservaService.findAll(formularioAulas.fechaDesde.getValue(),
						formularioAulas.fechaHasta.getValue(), formularioAulas.horaDesde.getValue(),
						formularioAulas.horaHasta.getValue(), formularioAulas.responsable.getValue()));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que comprueba si los filtros introducidos para consultar la
	 * disponibilidad de aulas son correctos.
	 * 
	 * @return Si los filtros introducidos para consultar la disponibilidad de aulas
	 *         son correctos
	 */
	private Boolean validarFiltrosConsultaAulas() {
		Boolean valido = false;
		try {

			return valido;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que permite consultar la disponibilidad de aulas con los filtros
	 * aplicados.
	 */
	private void consultarDisponibilidadAulas() {
		try {

		} catch (Exception e) {
			throw e;
		}
	}

}
