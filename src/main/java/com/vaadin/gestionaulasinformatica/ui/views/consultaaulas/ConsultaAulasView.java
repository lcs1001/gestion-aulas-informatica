package com.vaadin.gestionaulasinformatica.ui.views.consultaaulas;

import java.time.LocalDate;
import java.util.List;

//Imports Vaadin
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

//Imports backend
import com.vaadin.gestionaulasinformatica.backend.entity.Aula;
import com.vaadin.gestionaulasinformatica.backend.service.AulaService;
import com.vaadin.gestionaulasinformatica.backend.service.PropietarioAulaService;

//Imports UI
import com.vaadin.gestionaulasinformatica.ui.Comunes;
import com.vaadin.gestionaulasinformatica.ui.MainLayout;
import com.vaadin.gestionaulasinformatica.ui.Mensajes;

/**
 * Ventana Consulta de Disponibilidad de Aulas.
 */
@Route(value = "consultaAulas", layout = MainLayout.class)
@PageTitle("Consulta de Disponibilidad de Aulas")
public class ConsultaAulasView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private AulaService aulaService;
	private PropietarioAulaService propietarioAulaService;
	private Comunes comunes;

	private Grid<Aula> gridAulas;
	private ConsultaAulasForm formulario;
	private HorizontalLayout toolbar;

	/**
	 * Constructor de la clase.
	 * 
	 * @param aulaService            Service de JPA de la entidad Aula
	 * @param propietarioAulaService Service de JPA de la entidad PropietarioAula
	 */
	public ConsultaAulasView(AulaService aulaService, PropietarioAulaService propietarioAulaService) {
		Div contenido;

		try {
			this.aulaService = aulaService;
			this.propietarioAulaService = propietarioAulaService;
			this.comunes = new Comunes();

			addClassName("consulta-aulas-view");
			setSizeFull();

			configurarGrid();

			formulario = new ConsultaAulasForm(this.propietarioAulaService.findAll());

			contenido = new Div(formulario, crearButtonLayout(), gridAulas);
			contenido.addClassName("consulta-aulas-contenido");
			contenido.setSizeFull();

			add(contenido);

			// Sólo se muestra el grids cuando se hace una consulta válida
			comunes.ocultarGrid(gridAulas);
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
		Button btnConsultar;
		Button btnLimpiarFiltros;

		try {
			btnConsultar = new Button("Consultar Disponibilidad Aulas", event -> consultarDisponibilidadAulas());
			btnConsultar.setIcon(new Icon(VaadinIcon.SEARCH));
			btnConsultar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			btnLimpiarFiltros = new Button("", event -> limpiarFiltros());
			btnLimpiarFiltros.setIcon(new Icon(VaadinIcon.CLOSE));
			btnLimpiarFiltros.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			btnLimpiarFiltros.getElement().setProperty("title", "Limpiar filtros");

			toolbar = new HorizontalLayout(btnConsultar, btnLimpiarFiltros);
			toolbar.addClassName("consulta-toolbar");

			return toolbar;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura el grid que muestra las aulas.
	 */
	private void configurarGrid() {
		try {
			gridAulas = new Grid<>();
			gridAulas.addClassName("aulas-grid");
			gridAulas.setSizeFull();

			gridAulas.addColumn(Aula::getNombreAula).setHeader("Aula").setKey("nombreAula");
			gridAulas.addColumn(Aula::getNombreCentro).setHeader("Centro").setKey("centro");
			gridAulas.addColumn(Aula::getCapacidadInt).setHeader("Capacidad").setKey("capacidad");
			gridAulas.addColumn(Aula::getNumOrdenadoresInt).setHeader("Número de ordenadores").setKey("numOrdenadores");

			gridAulas.getColumns().forEach(columna -> columna.setAutoWidth(true));

			gridAulas.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
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
			comunes.ocultarGrid(gridAulas);
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
		Boolean valido = true;
		String msgAlerta = "";

		try {
			// Si no se ha introducido el filtro de Centro/Departamento
			if (formulario.propietario.isEmpty()) {
				msgAlerta += " " + Mensajes.MSG_CONSULTA_RESPONSABLE.getMensaje();
				valido = false;
			}

			// Para filtrar por fecha y hora se deben rellenar los filtros "Fecha desde",
			// "Hora desde" y "Hora hasta"
			// Si no se rellena el de "Fecha hasta", se asume que es la misma que "Fecha
			// desde"
			if (!formulario.fechaDesde.isEmpty() || !formulario.fechaHasta.isEmpty() || !formulario.horaDesde.isEmpty()
					|| !formulario.horaHasta.isEmpty()) {

				if (formulario.fechaDesde.isEmpty() || formulario.horaDesde.isEmpty()
						|| formulario.horaHasta.isEmpty()) {
					msgAlerta += " " + Mensajes.MSG_CONSULTA_RESERVA_FECHA_HORA.getMensaje();
					valido = false;
				}
			}

			// Si la hora desde la que se quiere filtrar es mayor que la hora hasta la que
			// se quiere filtrar
			if (!formulario.horaDesde.isEmpty() && !formulario.horaHasta.isEmpty()) {
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
	 * Función que permite consultar la disponibilidad de aulas con los filtros
	 * aplicados.
	 */
	private void consultarDisponibilidadAulas() {
		LocalDate fechaDesde;
		LocalDate fechaHasta;
		Double capacidad;
		Double numOrdenadores;
		List<Aula> lstAulas;

		try {
			comunes.ocultarGrid(gridAulas);

			if (validarFiltrosConsultaAulas()) {
				fechaDesde = formulario.fechaDesde.getValue();
				fechaHasta = formulario.fechaHasta.getValue();
				capacidad = formulario.capacidad.getValue();
				numOrdenadores = formulario.numOrdenadores.getValue();

				// Si no se ha indicado la fechaHasta, se pasa la misma que en fechaDesde
				fechaHasta = fechaHasta == null ? fechaDesde : fechaHasta;

				// Si no se ha indicado la capacidad o el número de ordenadores, se pasa un 0
				capacidad = capacidad == null ? 0 : capacidad;
				numOrdenadores = numOrdenadores == null ? 0 : numOrdenadores;

				lstAulas = aulaService.findAll(fechaDesde, fechaHasta, formulario.horaDesde.getValue(),
						formulario.horaHasta.getValue(), capacidad.intValue(), numOrdenadores.intValue(),
						formulario.propietario.getValue());

				if (!lstAulas.isEmpty()) {
					gridAulas.setVisible(true);
					gridAulas.setItems(lstAulas);
				} else {
					comunes.mostrarNotificacion(Mensajes.MSG_NO_CONSULTA_AULAS.getMensaje(), 3000, null);
				}

			}
		} catch (Exception e) {
			throw e;
		}
	}
}
