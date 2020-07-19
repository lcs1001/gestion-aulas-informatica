package gestionaulasinformatica.ui.views.responsable.gestionreservas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
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
 * Ventana Gestión de Reservas.
 */
@Route(value = "gestionReservas", layout = MainLayout.class)
@PageTitle("Gestión de Reservas")
@Secured("RESPONSABLE")
public class GestionReservasView extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GestionReservasView.class.getName());

	private ReservaService reservaService;
	private AulaService aulaService;
	private HistoricoReservasService historicoReservasService;
	private UsuarioService usuarioService;
	private PropietarioAulaService propietarioAulaService;
	private Comunes comunes;

	private GestionReservasBusquedaForm formularioBusqueda;
	private GestionReservasForm formularioEdicion;
	private HorizontalLayout toolbar;
	private Grid<Reserva> gridReservas;

	private Usuario responsableLogeado;

	public GestionReservasView(ReservaService reservaService, AulaService aulaService,
			HistoricoReservasService historicoReservasService, UsuarioService usuarioService,
			PropietarioAulaService propietarioAulaService) {
		Div contenido;

		try {
			this.reservaService = reservaService;
			this.aulaService = aulaService;
			this.historicoReservasService = historicoReservasService;
			this.usuarioService = usuarioService;
			this.propietarioAulaService = propietarioAulaService;
			comunes = new Comunes();

			responsableLogeado = this.usuarioService.findByCorreoUsuario(SecurityUtils.getUsername());

			addClassName("gestion-reservas-view");
			setSizeFull();

			configurarToolbar();
			configurarGridReservas();

			formularioBusqueda = new GestionReservasBusquedaForm(comunes);

			formularioEdicion = new GestionReservasForm(this.aulaService,
					this.propietarioAulaService.findAllPropietariosResponsable(responsableLogeado), comunes);
			formularioEdicion.addListener(GestionReservasForm.SaveEvent.class, this::guardarReserva);
			formularioEdicion.addListener(GestionReservasForm.CloseEvent.class, e -> cerrarEditor());

			contenido = new Div(comunes.getTituloVentana("Gestión de reservas"), formularioBusqueda, formularioEdicion,
					toolbar, gridReservas);
			contenido.addClassName("gestion-reservas-contenido");
			contenido.setSizeFull();

			add(contenido);

			formularioEdicion.setVisible(false);
			actualizarReservas();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que configura el grid que muestra las reservas.
	 */
	private void configurarGridReservas() {
		try {
			gridReservas = new Grid<>();
			gridReservas.addClassName("gestion-reservas-grid");
			gridReservas.setHeightFull();

			gridReservas.addColumn(new LocalDateRenderer<>(Reserva::getFecha, "dd/MM/yyyy")).setHeader("Fecha")
					.setKey("fecha");
			gridReservas.addColumn(Reserva::getDiaSemana).setHeader("Día semana").setKey("diaSemana");
			gridReservas.addColumn(Reserva::getHoraInicio).setHeader("Hora inicio").setKey("horaInicio");
			gridReservas.addColumn(Reserva::getHoraFin).setHeader("Hora fin").setKey("horaFin");
			gridReservas.addColumn(Reserva::getNombreAula).setHeader("Aula").setKey("aula");
			gridReservas.addColumn(Reserva::getNombreCentroAula).setHeader("Centro").setKey("centro");
			gridReservas.addColumn(Reserva::getMotivo).setHeader("Motivo").setKey("motivo");
			gridReservas.addColumn(Reserva::getACargoDe).setHeader("A cargo de").setKey("aCargoDe");
			gridReservas.addColumn(Reserva::getNombrePropietarioResponsable).setHeader("Registrada por")
					.setKey("registradaPor");

			gridReservas.getColumns().forEach(columna -> columna.setAutoWidth(true));

			gridReservas.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
					GridVariant.LUMO_ROW_STRIPES);

			gridReservas.setSelectionMode(SelectionMode.MULTI);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que configura el toolbar de gestión de reservas que contiene: un
	 * botón para modificar la reserva, un botón para eliminar las reservas
	 * seleccionadas, y un botón para cerrar la gestión de reservas.
	 * 
	 * @return Toolbar para la gestión de reservas
	 */
	private void configurarToolbar() {
		Button btnBuscar;
		Button btnLimpiarFiltros;
		Button btnModificar;
		Button btnEliminar;
		Button btnInfo;

		try {
			btnBuscar = new Button("Buscar", click -> consultarReservas());
			btnBuscar.setIcon(new Icon(VaadinIcon.SEARCH));
			btnBuscar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			btnLimpiarFiltros = new Button("Limpiar filtros", click -> formularioBusqueda.limpiarFiltros());
			btnLimpiarFiltros.setIcon(new Icon(VaadinIcon.CLOSE));
			btnLimpiarFiltros.addThemeVariants(ButtonVariant.LUMO_ICON);

			btnModificar = new Button("Modificar", click -> modificarReserva(gridReservas.getSelectedItems()));
			btnModificar.setIcon(new Icon(VaadinIcon.PENCIL));
			btnModificar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			btnEliminar = new Button("Eliminar",
					click -> confirmarEliminacionReservas(gridReservas.getSelectedItems()));
			btnEliminar.setIcon(new Icon(VaadinIcon.TRASH));
			btnEliminar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

			btnInfo = new Button("", click -> mostrarInfoGestionReservas());
			btnInfo.setIcon(new Icon(VaadinIcon.INFO_CIRCLE));
			btnInfo.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			toolbar = new HorizontalLayout(btnBuscar, btnLimpiarFiltros, btnModificar, btnEliminar, btnInfo);
			toolbar.addClassName("toolbar");

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que muestra un cuadro de diálogo con la información acerca de la
	 * gestión de las reservas.
	 */
	private void mostrarInfoGestionReservas() {
		Dialog dialogInfo;
		Div info;
		Button btnCerrar;
		try {
			dialogInfo = new Dialog();
			info = new Div();

			info.setText("Sólo se pueden modificar las reservas de una en una."
					+ " Si desea modificar una reserva de un rango de fechas debe eliminar todas las reservas correspondientes al rango y realizar una nueva reserva de rango.");

			btnCerrar = new Button("Cerrar", click -> dialogInfo.close());
			btnCerrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			btnCerrar.addClassName("margin-20");

			dialogInfo.add(info, btnCerrar);
			dialogInfo.open();
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
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

		try {
			// Si la fecha desde la que se quiere filtrar es mayor que la fecha hasta la que
			// se quiere filtrar
			if (!formularioBusqueda.fechaDesde.isEmpty() && !formularioBusqueda.fechaHasta.isEmpty()) {
				if (formularioBusqueda.fechaDesde.getValue().compareTo(formularioBusqueda.fechaHasta.getValue()) > 0) {
					comunes.mostrarNotificacion(Mensajes.MSG_CONSULTA_FECHA_DESDE_MAYOR.getMensaje(), 5000,
							NotificationVariant.LUMO_ERROR);
					valido = false;
				}
			}

			// Si la hora desde la que se quiere filtrar es mayor o igual que la hora hasta
			// la que
			// se quiere filtrar
			if (!formularioBusqueda.horaDesde.isEmpty() && !formularioBusqueda.horaHasta.isEmpty()) {
				if (formularioBusqueda.horaDesde.getValue().compareTo(formularioBusqueda.horaHasta.getValue()) > 0
						|| formularioBusqueda.horaDesde.getValue()
								.compareTo(formularioBusqueda.horaHasta.getValue()) == 0) {
					comunes.mostrarNotificacion(Mensajes.MSG_CONSULTA_HORA_DESDE_MAYOR.getMensaje(), 5000,
							NotificationVariant.LUMO_ERROR);
					valido = false;
				}
			}
			return valido;

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que permite buscar las reservas con los filtros aplicados.
	 */
	private void consultarReservas() {
		List<Reserva> lstReservas;

		try {
			if (validarFiltrosConsultaReservas()) {
				lstReservas = reservaService.findAllReservasFiltros(formularioBusqueda.fechaDesde.getValue(),
						formularioBusqueda.fechaHasta.getValue(), formularioBusqueda.horaDesde.getValue(),
						formularioBusqueda.horaHasta.getValue(), formularioBusqueda.diaSemana.getValue(),
						propietarioAulaService.findAllPropietariosResponsable(responsableLogeado));

				if (!lstReservas.isEmpty()) {
					gridReservas.setItems(lstReservas);
				} else {
					comunes.mostrarNotificacion(Mensajes.MSG_NO_CONSULTA_RESERVAS.getMensaje(), 3000, null);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que cierra la edición de reservas.
	 */
	private void cerrarEditor() {
		try {
			formularioEdicion.setReserva(null); // Se limpian los valores antiguo
			formularioEdicion.setVisible(false);
			formularioBusqueda.setVisible(true);
			toolbar.setVisible(true);
			gridReservas.setVisible(true);
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que abre el formulario para modificar la reserva pasada por
	 * parámetro.
	 * 
	 * @param reserva Reserva que se quiere modificar
	 */
	private void abrirEditor(Reserva reserva) {
		try {
			if (reserva == null) {
				cerrarEditor();
			} else {
				formularioBusqueda.setVisible(false);
				formularioEdicion.setVisible(true);
				formularioEdicion.setReserva(reserva);
				toolbar.setVisible(false);
				gridReservas.setVisible(false);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que permite modificar una reserva.
	 * 
	 * Muestra un mensaje de alerta si no se ha seleccionado ninguna reserva o si se
	 * ha seleccionado más de una.
	 * 
	 * @param reservas Reservas seleccionadas en el grid
	 */
	private void modificarReserva(Set<Reserva> reservas) {
		try {
			if (!reservas.isEmpty()) {
				if (reservas.size() > 1) { // Si se ha seleccionado más de una reserva
					comunes.mostrarNotificacion(Mensajes.MSG_MODIFICAR_SOLO_UNA_RESERVA.getMensaje(), 3000,
							NotificationVariant.LUMO_ERROR);
				} else {
					reservas.iterator().next();
					abrirEditor(reservas.iterator().next());
				}
			} else { // Si no se ha seleccionado ninguna reserva
				comunes.mostrarNotificacion(Mensajes.MSG_NO_RESERVAS_SELECCIONADAS.getMensaje(), 3000,
						NotificationVariant.LUMO_ERROR);
			}
		} catch (Exception e) {
			comunes.mostrarNotificacion(Mensajes.MSG_ERROR_ACCION.getMensaje(), 3000, NotificationVariant.LUMO_ERROR);
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que guarda la reserva, y la operación de modificación en el histórico
	 * de reservas, en la base de datos.
	 * 
	 * @param e Evento de guardado
	 */
	private void guardarReserva(GestionReservasForm.SaveEvent evt) {
		Reserva reserva;
		String lugarReserva;
		HistoricoReservas operacionReserva;

		try {
			reserva = evt.getReserva();
			reservaService.save(reserva);

			lugarReserva = reserva.getAula().getNombreAula() + " - "
					+ reserva.getAula().getUbicacionCentro().getNombrePropietarioAula();
			operacionReserva = new HistoricoReservas(LocalDateTime.now(), TipoOperacionHR.MODIFICACIÓN,
					reserva.getMotivo(), reserva.getFecha(), reserva.getHoraInicio(), reserva.getHoraFin(),
					lugarReserva, reserva.getACargoDe(), responsableLogeado.getNombreApellidosUsuario(),
					reserva.getAula().getPropietarioAula().getIdPropietarioAula());
			historicoReservasService.save(operacionReserva);
			
			comunes.mostrarNotificacion(Mensajes.MSG_GUARDADO_CORRECTO.getMensaje(), 3000, NotificationVariant.LUMO_SUCCESS);

			formularioBusqueda.limpiarFiltros();
			actualizarReservas();
			cerrarEditor();
			
		} catch (Exception e) {
			comunes.mostrarNotificacion(Mensajes.MSG_ERROR_ACCION.getMensaje(), 3000, NotificationVariant.LUMO_ERROR);
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que muestra un mensaje de confirmación en un cuadro de diálogo cuando
	 * se quieren eliminar reservas (botón Eliminar).
	 * 
	 * Si no se ha seleccionado ninguna reserva se muestra un mensaje de alerta.
	 * 
	 * @param reservas Reservas que eliminar
	 */
	private void confirmarEliminacionReservas(Set<Reserva> reservas) {
		Dialog confirmacion;
		String mensajeConfirmacion;
		String mensajeEliminado;
		Button btnConfirmar;
		Button btnCancelar;

		try {
			// Si se ha seleccionado alguna reserva
			if (!reservas.isEmpty()) {

				mensajeConfirmacion = "¿Desea eliminar la/s reserva/s seleccionada/s definitivamente? Esta acción no se puede deshacer.";

				confirmacion = new Dialog(new Label(mensajeConfirmacion));
				confirmacion.setCloseOnEsc(false);
				confirmacion.setCloseOnOutsideClick(false);

				mensajeEliminado = "Se ha/n eliminado la/s reserva/s seleccionada/s correctamente";

				btnConfirmar = new Button("Confirmar", event -> {
					eliminarReservas(reservas);
					comunes.mostrarNotificacion(mensajeEliminado, 3000, NotificationVariant.LUMO_SUCCESS);
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

			} else { // Si no se ha seleccionado ninguna reserva
				comunes.mostrarNotificacion(Mensajes.MSG_NO_RESERVAS_SELECCIONADAS.getMensaje(), 3000,
						NotificationVariant.LUMO_ERROR);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que elimina las reservas pasadas por parámetro de la base de datos,
	 * guarda la operación de eliminación en el histórico de reservas en la base de
	 * datos, y actualiza el listado.
	 * 
	 * @param reservas Reservas que eliminar
	 */
	private void eliminarReservas(Set<Reserva> reservas) {
		String lugarReserva;
		HistoricoReservas operacionReserva;
		try {
			for (Reserva reserva : reservas) {
				reservaService.delete(reserva);

				lugarReserva = reserva.getAula().getNombreAula() + " - "
						+ reserva.getAula().getUbicacionCentro().getNombrePropietarioAula();
				operacionReserva = new HistoricoReservas(LocalDateTime.now(), TipoOperacionHR.ELIMINACIÓN,
						reserva.getMotivo(), reserva.getFecha(), reserva.getHoraInicio(), reserva.getHoraFin(),
						lugarReserva, reserva.getACargoDe(), responsableLogeado.getNombreApellidosUsuario(),
						reserva.getAula().getPropietarioAula().getIdPropietarioAula());
				historicoReservasService.save(operacionReserva);
			}

			formularioBusqueda.limpiarFiltros();
			actualizarReservas();
			
		} catch (Exception e) {
			comunes.mostrarNotificacion(Mensajes.MSG_ERROR_ACCION.getMensaje(), 3000, NotificationVariant.LUMO_ERROR);
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que actualiza el grid que muestra las reservas de los centros o
	 * departamentos de los que es responsable el usuario logeado.
	 */
	private void actualizarReservas() {
		List<Reserva> lstReservas;

		try {
			gridReservas.setVisible(false);

			lstReservas = reservaService.findAllReservasFiltros(LocalDate.now(), null, null, null, null,
					propietarioAulaService.findAllPropietariosResponsable(responsableLogeado));

			if (!lstReservas.isEmpty()) {
				gridReservas.setVisible(true);
				gridReservas.setItems(lstReservas);
			} else {
				comunes.mostrarNotificacion(Mensajes.MSG_NO_RESERVAS.getMensaje(), 3000, null);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}
}
