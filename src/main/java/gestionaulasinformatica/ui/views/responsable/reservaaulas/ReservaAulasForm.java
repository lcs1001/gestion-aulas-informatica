package gestionaulasinformatica.ui.views.responsable.reservaaulas;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import gestionaulasinformatica.backend.entity.Aula;
import gestionaulasinformatica.backend.entity.PropietarioAula;
import gestionaulasinformatica.backend.entity.Reserva;
import gestionaulasinformatica.backend.entity.Usuario;
import gestionaulasinformatica.backend.service.AulaService;
import gestionaulasinformatica.ui.Comunes;
import gestionaulasinformatica.ui.Mensajes;

/**
 * Clase que contiene el formulario para reservar aulas.
 * 
 * @author Lisa
 *
 */
public class ReservaAulasForm extends FormLayout {

	private static final long serialVersionUID = 1L;

	private AulaService aulaService;
	private List<PropietarioAula> lstPropietarios;
	private Comunes comunes;
	private Usuario responsableLogeado;

	protected DatePicker fechaInicio;
	protected DatePicker fechaFin;
	protected TimePicker horaInicio;
	protected TimePicker horaFin;
	protected ComboBox<PropietarioAula> propietarioAula;
	protected ComboBox<Aula> aula;
	protected ComboBox<String> diaSemana;
	protected TextField motivo;
	protected TextField aCargoDe;

	protected HorizontalLayout toolbar;
	private Button btnReservar;
	private Button btnLimpiarCampos;
	protected Checkbox chkReservaRango;

	private Binder<Reserva> binder;
	private Reserva reserva;

	/**
	 * Constructor de la clase.
	 * 
	 * @param aulaService        Service de JPA de la entidad Aula
	 * @param propietarios       Lista de propietarios de aulas bajo la
	 *                           responsabilidad del usuario logeado que se muestra
	 *                           en el desplegable de centros
	 * @param comunes            Objeto de la clase Comunes para acceder a las
	 *                           funciones comunes
	 * @param responsableLogeado Responsable logeado en la app
	 */
	public ReservaAulasForm(AulaService aulaService, List<PropietarioAula> propietarios, Comunes comunes,
			Usuario responsableLogeado) {
		try {
			addClassName("reserva-aulas-form");

			this.aulaService = aulaService;
			this.lstPropietarios = propietarios;
			this.comunes = comunes;
			this.responsableLogeado = responsableLogeado;

			setResponsiveSteps(new ResponsiveStep("25em", 1), new ResponsiveStep("25em", 2),
					new ResponsiveStep("25em", 3), new ResponsiveStep("25em", 4));

			configurarCampos();
			configurarToolbar();

			binder = new BeanValidationBinder<>(Reserva.class);
			binder.bindInstanceFields(this);
			binder.bind(fechaInicio, "fecha");

			add(fechaInicio, horaInicio);
			add(propietarioAula, 2);
			add(fechaFin, horaFin);
			add(aula, 2);
			add(diaSemana, 2);
			add(motivo, 2);
			add(aCargoDe, 2);
			add(toolbar, 4);

			// Se deshabilitan los campos "Fecha fin" y "Día de la semana" hasta que se
			// marca la reserva por rango de fechas
			fechaFin.setEnabled(false);
			diaSemana.setEnabled(false);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura los campos del
	 */
	private void configurarCampos() {
		List<String> diasCombo;
		try {
			fechaInicio = new DatePicker("Fecha");
			fechaInicio.setMin(LocalDate.now()); // Como mínimo debe ser la fecha actual
			fechaInicio.setPlaceholder("dd/MM/yyyy");
			fechaInicio.setLocale(comunes.getLocaleES()); // Formato dd/M/yyyy
			fechaInicio.setRequiredIndicatorVisible(true);
			fechaInicio.setClearButtonVisible(true);
			fechaInicio.addValueChangeListener(e -> comprobarFechaActual());

			fechaFin = new DatePicker("Fecha fin");
			fechaFin.setMin(fechaInicio.getValue()); // Como mínimo debe ser la fecha desde la que se ha filtrado
			fechaFin.setPlaceholder("dd/MM/yyyy");
			fechaFin.setLocale(comunes.getLocaleES()); // Formato dd/M/yyyy
			fechaFin.setClearButtonVisible(true);

			horaInicio = new TimePicker("Hora inicio");
			horaInicio.setPlaceholder("hh:mm");
			horaInicio.setLocale(comunes.getLocaleES());
			horaInicio.setClearButtonVisible(true);
			horaInicio.addValueChangeListener(e -> establecerMinHoraFin());

			horaFin = new TimePicker("Hora fin");
			horaFin.setPlaceholder("hh:mm");
			horaFin.setLocale(comunes.getLocaleES());
			horaFin.setClearButtonVisible(true);

			propietarioAula = new ComboBox<PropietarioAula>("Centro/Departamento");
			propietarioAula.setPlaceholder("Seleccione");
			propietarioAula.setItems(lstPropietarios);
			propietarioAula.setItemLabelGenerator(PropietarioAula::getNombrePropietarioAula);
			propietarioAula.addValueChangeListener(e -> cargarAulasPropietario());
			propietarioAula.setRequiredIndicatorVisible(true);

			aula = new ComboBox<Aula>("Aula");
			aula.setPlaceholder("Seleccione");
			aula.setItemLabelGenerator(Aula::getNombreAula);

			diasCombo = new ArrayList<String>();
			diasCombo.add("Seleccione");
			diasCombo.addAll(comunes.getDiasSemana());
			diaSemana = new ComboBox<String>("Día de la semana");
			diaSemana.setItems(diasCombo);

			motivo = new TextField("Motivo");
			motivo.setPlaceholder("Motivo de la reserva");
			motivo.setMaxLength(50);
			motivo.setClearButtonVisible(true);
			motivo.setRequiredIndicatorVisible(true);

			aCargoDe = new TextField("A cargo de");
			aCargoDe.setPlaceholder("Persona a cargo de la reserva");
			aCargoDe.setMaxLength(50);
			aCargoDe.setClearButtonVisible(true);
			aCargoDe.setRequiredIndicatorVisible(true);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura el toolbar que contiene: un botón para generar la
	 * reserva, un botón para limpiar todos los campos y un checkbox para indicar si
	 * se trata de una reserva por rango de fechas.
	 * 
	 * @return Toolbar
	 */
	private void configurarToolbar() {
		try {
			btnReservar = new Button("Reservar", click -> validarGuardar());
			btnReservar.setIcon(new Icon(VaadinIcon.CHECK));
			btnReservar.addClickShortcut(Key.ENTER); // Se guarda al pulsar Enter en el teclado
			btnReservar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			btnLimpiarCampos = new Button("Limpiar campos", click -> limpiarCampos());
			btnLimpiarCampos.setIcon(new Icon(VaadinIcon.CLOSE));
			btnLimpiarCampos.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			chkReservaRango = new Checkbox("Reserva por Rango de Fechas");
			chkReservaRango.addClickListener(e -> reservaRango());

			toolbar = new HorizontalLayout(btnReservar, btnLimpiarCampos, chkReservaRango);
			toolbar.addClassName("toolbar");

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Carga las aulas del propietario seleccionado en el desplegable de
	 * propietarios.
	 */
	private void cargarAulasPropietario() {
		List<Aula> lstAulas;
		try {
			if (!propietarioAula.isEmpty()) {
				lstAulas = aulaService.findAllAulasPropietario(propietarioAula.getValue());
				aula.setItems(lstAulas);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que comprueba si la fecha escogida es la fecha actual para establecer
	 * la hora de inicio mínima (hora actual). Si no, no hay hora mínima.
	 */
	private void comprobarFechaActual() {
		try {
			if (!fechaInicio.isEmpty()) {
				if (fechaInicio.getValue().equals(LocalDate.now())) {
					horaInicio.setMinTime(LocalTime.now());

				} else {
					horaInicio.setMinTime(null);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que establece como hora de fin mínima la hora de inicio elegida.
	 */
	private void establecerMinHoraFin() {
		try {
			if (!horaInicio.isEmpty()) {
				horaFin.setMinTime(horaInicio.getValue());
			}
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
				fechaInicio.setLabel("Fecha");
				fechaFin.setEnabled(false);
				fechaFin.setRequiredIndicatorVisible(false);
				diaSemana.setEnabled(false);
			} else {
				fechaInicio.setLabel("Fecha inicio");
				fechaFin.setEnabled(true);
				fechaFin.setMin(fechaInicio.getValue());
				fechaFin.setRequiredIndicatorVisible(true);
				diaSemana.setEnabled(true);
				diaSemana.setValue("Seleccione");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que limpia todos los campos.
	 */
	protected void limpiarCampos() {
		try {
			fechaInicio.clear();
			fechaFin.clear();
			horaInicio.clear();
			horaFin.clear();
			propietarioAula.clear();
			aula.clear();
			diaSemana.setValue("Seleccione");
			motivo.clear();
			aCargoDe.clear();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que establece la reserva actual del binder.
	 * 
	 * @param reserva Reserva actual
	 * @param nueva   Si se trata de una nueva reserva o de una modificación
	 */
	public void setReserva(Reserva reserva, Boolean nueva) {
		try {
			this.reserva = reserva;
			binder.readBean(reserva);

			if (nueva)
				reserva.setDiaSemana(comunes.getDiaSemana(LocalDate.now().getDayOfWeek().getValue()));

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que comprueba si los campos de la reserva son correctos.
	 * 
	 * @return Si los campos introducidos para reservar un aula son correctos
	 */
	private Boolean validarCamposReserva() {
		Boolean valido = true;

		try {
			// Si la fecha de inicio es mayor o igual que la fecha de fin en una reserva de
			// rango de fechas
			if (!fechaInicio.isEmpty() && !fechaFin.isEmpty()) {
				if (fechaInicio.getValue().compareTo(fechaFin.getValue()) > 0
						|| fechaInicio.getValue().compareTo(fechaFin.getValue()) == 0) {
					comunes.mostrarNotificacion(Mensajes.MSG_RESERVA_FECHA_INICIO_MAYOR.getMensaje(), 5000,
							NotificationVariant.LUMO_ERROR);
					valido = false;
				}
			}

			// Si la hora de inicio es mayor o igual que la hora de fin
			if (!horaInicio.isEmpty() && !horaFin.isEmpty()) {
				if (horaInicio.getValue().compareTo(horaFin.getValue()) > 0
						|| horaInicio.getValue().compareTo(horaFin.getValue()) == 0) {
					comunes.mostrarNotificacion(Mensajes.MSG_RESERVA_HORA_INICIO_MAYOR.getMensaje(), 5000,
							NotificationVariant.LUMO_ERROR);
					valido = false;
				}
			}

			// Si se ha dejado algún campo vacío
			if (fechaInicio.isEmpty() || horaInicio.isEmpty() || horaFin.isEmpty() || propietarioAula.isEmpty()
					|| aula.isEmpty() || motivo.isEmpty() || aCargoDe.isEmpty()
					|| (!chkReservaRango.isEmpty() && (fechaFin.isEmpty()))) {
				comunes.mostrarNotificacion(Mensajes.MSG_TODOS_CAMPOS_OBLIGATORIOS.getMensaje(), 5000,
						NotificationVariant.LUMO_ERROR);
				valido = false;
			}

			return valido;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que comprueba si una reserva es válida, es decir, si el aula que se
	 * quiere reservar está disponible en esa/s fecha/s y horas.
	 * 
	 * @param reserva Reserva que se quiere validar
	 * 
	 * @return Si la reserva es válida o no
	 */
	private Boolean validarReserva() {
		List<Aula> aulaConsulta;
		Aula aulaReserva;
		Boolean valida = false;

		try {

			aulaReserva = aula.getValue();

			// Si es una reserva de un solo día
			if (chkReservaRango.isEmpty()) {
				aulaConsulta = aulaService.findAllAulasDisponiblesFiltros(fechaInicio.getValue(), fechaInicio.getValue(),
						horaInicio.getValue(), horaFin.getValue(), aulaReserva.getCapacidadInt(),
						aulaReserva.getNumOrdenadoresInt(), null, null, aulaReserva.getIdAula(), null);

				if (aulaConsulta.contains(aulaReserva))
					valida = true;

			} else {
				aulaConsulta = aulaService.findAllAulasDisponiblesFiltros(fechaInicio.getValue(), fechaFin.getValue(), horaInicio.getValue(),
						horaFin.getValue(), aulaReserva.getCapacidadInt(), aulaReserva.getNumOrdenadoresInt(),
						diaSemana.getValue(), null, aulaReserva.getIdAula(), null);

				if (aulaConsulta.contains(aulaReserva))
					valida = true;
			}

			// Si el aula no está disponible se muestra un notificación de error
			if (!valida)
				comunes.mostrarNotificacion(Mensajes.MSG_AULA_NO_DISPONIBLE.getMensaje(), 3000,
						NotificationVariant.LUMO_ERROR);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return valida;
	}

	/**
	 * Función que valida la reserva y la guarda.
	 * 
	 * Si es una reserva por rango de fechas:
	 * 
	 * 1. Se crea una lista de reservas (una por cada día del rango o por cada día
	 * de la semana elegido de ese rango).
	 * 
	 * 2. Se lanza el evento de guardado de rango (SaveRangeEvent).
	 */
	private void validarGuardar() {
		List<Reserva> lstReservasGuardar;
		long diasReserva;
		Reserva reservaG;
		LocalDate fecha;
		String diaSemanaG = "";
		try {
			if (validarCamposReserva() & validarReserva()) {

				// Si es una reserva de un solo día
				if (chkReservaRango.isEmpty()) {
					reserva.setDiaSemana(comunes.getDiaSemana(fechaInicio.getValue().getDayOfWeek().getValue()));
					binder.writeBean(reserva);
					fireEvent(new SaveEvent(this, reserva));

				} else {
					lstReservasGuardar = new ArrayList<Reserva>();
					diasReserva = ChronoUnit.DAYS.between(fechaInicio.getValue(), fechaFin.getValue()) + 1;
					fecha = fechaInicio.getValue();

					// Si se ha seleccionado un día de la semana para reservar
					if (!diaSemana.getValue().equalsIgnoreCase("Seleccione")) {
						diaSemanaG = diaSemana.getValue();
					}

					for (int i = 1; i <= diasReserva; i++) {
						// Si se ha seleccionado un día de la semana para reservar
						if (!diaSemanaG.isEmpty()) {
							if (comunes.getDiaSemana(fecha.getDayOfWeek().getValue()).equals(diaSemanaG)) {
								reservaG = new Reserva(fecha, horaInicio.getValue(), horaFin.getValue(), diaSemanaG,
										aula.getValue(), motivo.getValue(), aCargoDe.getValue(),
										responsableLogeado.getNombreApellidosUsuario(),
										aula.getValue().getPropietarioAula());
								lstReservasGuardar.add(reservaG);
							}
						} else {
							reservaG = new Reserva(fecha, horaInicio.getValue(), horaFin.getValue(),
									comunes.getDiaSemana(fecha.getDayOfWeek().getValue()), aula.getValue(),
									motivo.getValue(), aCargoDe.getValue(),
									responsableLogeado.getNombreApellidosUsuario(),
									aula.getValue().getPropietarioAula());

							lstReservasGuardar.add(reservaG);
						}
						fecha = fecha.plusDays(1);
					}

					fireEvent(new SaveRangeEvent(this, lstReservasGuardar));
				}
			}
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clase estática y abstracta para definir los eventos del formulario de Gestión
	 * de Reservas.
	 * 
	 * @author Lisa
	 *
	 */
	public static abstract class ReservaAulasFormEvent extends ComponentEvent<ReservaAulasForm> {
		private static final long serialVersionUID = 1L;
		private Reserva reserva;
		private List<Reserva> lstReservas;

		/**
		 * Constructor de la clase.
		 * 
		 * @param source  Origen
		 * @param reserva Reserva
		 */
		protected ReservaAulasFormEvent(ReservaAulasForm source, Reserva reserva) {
			super(source, false);
			this.reserva = reserva;
		}

		/**
		 * Constructor de la clase.
		 * 
		 * @param source   Origen
		 * @param reservas Lista de reservas
		 */
		protected ReservaAulasFormEvent(ReservaAulasForm source, List<Reserva> reservas) {
			super(source, false);
			this.lstReservas = reservas;
		}

		/**
		 * Función que devuelve la reserva asociada a la clase.
		 * 
		 * @return Reserva asociada a la clase
		 */
		public Reserva getReserva() {
			return reserva;
		}

		/**
		 * Función que devuelve la lista de reservas asociada a la clase.
		 * 
		 * @return Lista de reservas asociada a la clase
		 */
		public List<Reserva> getReservas() {
			return lstReservas;
		}
	}

	/**
	 * Clase estática para definir el evento "Reservar" (un solo día) del formulario
	 * de Reserva de Aulas.
	 * 
	 * @author Lisa
	 *
	 */
	public static class SaveEvent extends ReservaAulasFormEvent {
		private static final long serialVersionUID = 1L;

		SaveEvent(ReservaAulasForm source, Reserva reserva) {
			super(source, reserva);
		}
	}

	/**
	 * Clase estática para definir el evento "Reservar" (un rango de fechas) del
	 * formulario de Reserva de Aulas.
	 * 
	 * @author Lisa
	 *
	 */
	public static class SaveRangeEvent extends ReservaAulasFormEvent {
		private static final long serialVersionUID = 1L;

		SaveRangeEvent(ReservaAulasForm source, List<Reserva> reservas) {
			super(source, reservas);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
}
