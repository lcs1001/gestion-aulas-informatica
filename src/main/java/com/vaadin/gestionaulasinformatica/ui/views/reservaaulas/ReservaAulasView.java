package com.vaadin.gestionaulasinformatica.ui.views.reservaaulas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.gestionaulasinformatica.backend.service.ReservaService;
import com.vaadin.gestionaulasinformatica.backend.entity.Reserva;
import com.vaadin.gestionaulasinformatica.backend.service.AulaService;
import com.vaadin.gestionaulasinformatica.backend.service.PropietarioAulaService;
import com.vaadin.gestionaulasinformatica.ui.MainLayout;
import com.vaadin.gestionaulasinformatica.ui.Mensajes;
import com.vaadin.gestionaulasinformatica.ui.Comunes;

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
	private Grid<Reserva> gridReservas;
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
			this.comunes = new Comunes();

			addClassName("reserva-aulas-view");
			setSizeFull();

			configurarGridReservas();

			formulario = new ReservaAulasForm(this.aulaService, this.propietarioAulaService.findAllCentros(), comunes);
			formulario.addListener(ReservaAulasForm.SaveEvent.class, this::guardarReserva);
			formulario.addListener(ReservaAulasForm.DeleteEvent.class, this::confirmarEliminacionAula);
			formulario.addListener(ReservaAulasForm.CloseEvent.class, e -> cerrarEditor());

			contenido = new Div(formulario, gridReservas, getToolbar());
			contenido.addClassName("consulta-reservas-contenido");
			contenido.setSizeFull();

			add(contenido);

			// Se deshabilitan los campos "Fecha fin" y "Día de la semana" hasta que se
			// marca la reserva por rango de fechas
			formulario.fechaFin.setEnabled(false);
			formulario.diaSemana.setEnabled(false);

			gridReservas.setVisible(false);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura el grid que muestra las reservas.
	 */
	private void configurarGridReservas() {
		try {
			gridReservas = new Grid<>();
			gridReservas.addClassName("reserva-aulas-grid");
			gridReservas.setSizeFull();

			gridReservas.addColumn(new LocalDateRenderer<>(Reserva::getFecha, "dd/MM/yyyy")).setHeader("Fecha")
					.setKey("fecha");
			gridReservas.addColumn(Reserva::getDiaSemana).setHeader("Día semana").setKey("diaSemana");
			gridReservas.addColumn(Reserva::getHoraInicio).setHeader("Hora inicio").setKey("horaInicio");
			gridReservas.addColumn(Reserva::getHoraFin).setHeader("Hora fin").setKey("horaFin");
			gridReservas.addColumn(Reserva::getNombreAula).setHeader("Aula").setKey("aula");
			gridReservas.addColumn(Reserva::getNombreCentroAula).setHeader("Centro").setKey("centro");
			gridReservas.addColumn(Reserva::getMotivo).setHeader("Motivo").setKey("motivo");
			gridReservas.addColumn(Reserva::getACargoDe).setHeader("A cargo de").setKey("aCargoDe");

			gridReservas.getColumns().forEach(columna -> columna.setAutoWidth(true));

			gridReservas.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
					GridVariant.LUMO_ROW_STRIPES);

			gridReservas.asSingleSelect().addValueChangeListener(e -> abrirEditor(e.getValue(), true));

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura el toolbar que contiene: un botón para generar la
	 * reserva, un checkbox para indicar si se trata de una reserva por rango de
	 * fechas, y un botón para gestionar las reservas.
	 * 
	 * @return Toolbar
	 */
	private HorizontalLayout getToolbar() {
		Button btnReservar;
		Button btnGestionarReservas;
		try {
			btnReservar = new Button("Reservar", click -> anadirReserva());
			btnReservar.addClickShortcut(Key.ENTER); // Se guarda al pulsar Enter en el teclado
			btnReservar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			btnGestionarReservas = new Button("Gestionar reservas", click -> gestionarReservas());
			btnGestionarReservas.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			chkReservaRango = new Checkbox("Reserva por Rango de Fechas");
			chkReservaRango.addClickListener(e -> reservaRango());

			toolbar = new HorizontalLayout(btnReservar, chkReservaRango, btnGestionarReservas);
			toolbar.addClassName("toolbar");

			return toolbar;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que muestra el grid de reservas y los botones para gestionarlas.
	 */
	private void gestionarReservas() {
		try {
			formulario.setReserva(null); // Se limpian los valores antiguos
			toolbar.setVisible(false);
			formulario.formToolbar.setVisible(true);
			gridReservas.setVisible(true);
			actualizarReservas();
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

	/**
	 * Función que oculta la edición de reservas.
	 */
	private void cerrarEditor() {
		try {
			formulario.setReserva(null); // Se limpian los valores antiguos
			toolbar.setVisible(true);
			formulario.formToolbar.setVisible(false);
			gridReservas.setVisible(false);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que permite modificar la reserva pasada por parámetro.
	 * 
	 * @param reserva Reserva que se quiere modificar
	 * @param editar  Booleano que indica si se trata de una modificación (true) o
	 *                una creación(false)
	 */
	private void abrirEditor(Reserva reserva, Boolean editar) {
		try {
			if (reserva == null) {
				cerrarEditor();
			} else {
				formulario.setReserva(reserva);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que permite reservar un aula.
	 */
	private void anadirReserva() {
		try {
			gridReservas.asSingleSelect().clear();
			abrirEditor(new Reserva(), false);

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
			actualizarReservas();
			cerrarEditor();

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que elimina la reserva de la base de datos.
	 * 
	 * @param reserva Reserva que se quiere eliminar
	 */
	private void eliminarReserva(Reserva reserva) {
		try {
			reservaService.delete(reserva);
			actualizarReservas();
			cerrarEditor();

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que muestra un mensaje de confirmación en un cuadro de diálogo cuando
	 * se quiere eliminar una reserva (botón Eliminar).
	 * 
	 * @param evt Evento de eliminación de reserva
	 */
	private void confirmarEliminacionAula(ReservaAulasForm.DeleteEvent evt) {
		Reserva reserva;
		Dialog confirmacion;
		String mensajeConfirmacion;
		String mensajeEliminado;
		Button btnConfirmar;
		Button btnCancelar;

		try {
			reserva = evt.getReserva();

			mensajeConfirmacion = "¿Desea eliminar la reserva " + reserva.getMotivo() + " del " + reserva.getFecha()
					+ " definitivamente? Esta acción no se puede deshacer.";

			confirmacion = new Dialog(new Label(mensajeConfirmacion));
			confirmacion.setCloseOnEsc(false);
			confirmacion.setCloseOnOutsideClick(false);

			mensajeEliminado = "Se ha eliminado la reserva " + reserva.getMotivo() + " correctamente";

			btnConfirmar = new Button("Confirmar", event -> {
				comunes.mostrarNotificacion(mensajeEliminado, 3000, null);
				eliminarReserva(reserva);
				confirmacion.close();
			});
			btnConfirmar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			btnConfirmar.addClassName("margin-20");

			btnCancelar = new Button("Cancelar", event -> {
				cerrarEditor();
				confirmacion.close();
			});
			btnCancelar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

			confirmacion.add(btnConfirmar, btnCancelar);

			confirmacion.open();

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que actualiza el grid que muestra las reservas del centro o
	 * departamento.
	 */
	private void actualizarReservas() {
		List<Reserva> lstReservas;

		try {
			// Se obtienen las reservas a partir de la fecha y hora actual
			lstReservas = reservaService.findReservasAPartirMomentoActual();

			if (!lstReservas.isEmpty()) {
				gridReservas.setVisible(true);
				gridReservas.setItems(lstReservas);
			} else {
				comunes.mostrarNotificacion(Mensajes.MSG_NO_RESERVAS.getMensaje(), 3000, null);
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
