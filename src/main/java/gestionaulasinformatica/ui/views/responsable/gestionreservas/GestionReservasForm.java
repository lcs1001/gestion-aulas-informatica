package gestionaulasinformatica.ui.views.responsable.gestionreservas;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
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
import gestionaulasinformatica.backend.service.AulaService;
import gestionaulasinformatica.ui.Comunes;
import gestionaulasinformatica.ui.Mensajes;

public class GestionReservasForm extends FormLayout {

	private static final long serialVersionUID = 1L;

	private AulaService aulaService;
	private List<PropietarioAula> lstPropietarios;
	private Comunes comunes;

	protected DatePicker fecha;
	protected TimePicker horaInicio;
	protected TimePicker horaFin;
	protected ComboBox<PropietarioAula> propietarioAula;
	protected ComboBox<Aula> aula;
	protected TextField diaSemana;
	protected TextField motivo;
	protected TextField aCargoDe;

	protected HorizontalLayout toolbar;
	private Button btnGuardar;
	private Button btnCancelar;

	private Binder<Reserva> binder;
	private Reserva reserva;

	/**
	 * Constructor de la clase.
	 * 
	 * @param aulaService Service de JPA de la entidad Aula
	 * @param responsable Usuario que ha accedido a la aplicación
	 * @param comunes     Objeto Comunes para tener acceso a las funciones comunes
	 */
	public GestionReservasForm(AulaService aulaService, List<PropietarioAula> propietarios, Comunes comunes) {
		try {
			addClassName("gestion-reservas-form");

			this.aulaService = aulaService;
			this.lstPropietarios = propietarios;
			this.comunes = comunes;

			setResponsiveSteps(new ResponsiveStep("25em", 1), new ResponsiveStep("25em", 2),
					new ResponsiveStep("25em", 3), new ResponsiveStep("25em", 4));

			binder = new BeanValidationBinder<>(Reserva.class);
			
			configurarCampos();
			configurarToolbar();

			binder.bindInstanceFields(this);
			binder.bind(propietarioAula, "aula.propietarioAula");
			binder.bind(aula, "aula");

			add(fecha, horaInicio);
			add(propietarioAula, 2);
			add(diaSemana, horaFin);
			add(aula, 2);
			add(motivo, 2);
			add(aCargoDe, 2);
			add(toolbar, 4);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura los campos del formulario.
	 */
	private void configurarCampos() {
		try {
			fecha = new DatePicker("Fecha");
			fecha.setMin(LocalDate.now()); // Como mínimo debe ser la fecha actual
			fecha.setPlaceholder("dd/MM/yyyy");
			fecha.setLocale(comunes.getLocaleES()); // Formato dd/M/yyyy
			fecha.setClearButtonVisible(true);
			fecha.addValueChangeListener(e -> cambiarDiaSemana());

			horaInicio = new TimePicker("Hora inicio");
			horaInicio.setMinTime(LocalTime.now());
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

			aula = new ComboBox<Aula>("Aula");
			aula.setPlaceholder("Seleccione");
			aula.setItemLabelGenerator(Aula::getNombreAula);

			diaSemana = new TextField("Día de la semana");
			diaSemana.setEnabled(false);

			motivo = new TextField("Motivo");
			motivo.setPlaceholder("Motivo de la reserva");
			motivo.setMaxLength(50);
			motivo.setClearButtonVisible(true);

			aCargoDe = new TextField("A cargo de");
			aCargoDe.setPlaceholder("Persona a cargo de la reserva");
			aCargoDe.setMaxLength(50);
			aCargoDe.setClearButtonVisible(true);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que crea el layout de botones para las reservas.
	 * 
	 * @return Layout de botones
	 */
	private void configurarToolbar() {
		try {
			btnGuardar = new Button("Guardar");
			btnGuardar.addClickListener(click -> validarGuardar());
			btnGuardar.addClickShortcut(Key.ENTER); // Se guarda al pulsar Enter en el teclado
			btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			btnCancelar = new Button("Cancelar");
			btnCancelar.addClickListener(click -> fireEvent(new CloseEvent(this)));
			btnCancelar.addClickShortcut(Key.ESCAPE); // Se cierra al pulsar ESC en el teclado
			btnCancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);

			binder.addStatusChangeListener(evt -> btnGuardar.setEnabled(binder.isValid()));

			toolbar = new HorizontalLayout(btnGuardar, btnCancelar);
			toolbar.addClassName("toolbar");

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
	 * Carga las aulas del propietario seleccionado en el desplegable de propietarios.
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
	 * Cambia el día de la semana según la fecha seleccionada que se muestra en para
	 * almacenarlo.
	 */
	private void cambiarDiaSemana() {
		try {
			if (!fecha.isEmpty())
				diaSemana.setValue(comunes.getDiaSemana(fecha.getValue().getDayOfWeek().getValue()));
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que establece la reserva actual del binder.
	 * 
	 * @param reserva Reserva actual
	 */
	public void setReserva(Reserva reserva) {
		try {
			this.reserva = reserva;
			binder.readBean(reserva);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que comprueba si los campos de la reserva son correctos.
	 * 
	 * @return Si los filtros introducidos para consultar las reservas son correctos
	 */
	private Boolean validarReserva() {
		Boolean valido = true;

		try {
			// Si la hora inicio es mayor o igual que la hora fin
			if (!horaInicio.isEmpty() && !horaFin.isEmpty()) {
				if (horaInicio.getValue().compareTo(horaFin.getValue()) > 0
						|| horaInicio.getValue().compareTo(horaFin.getValue()) == 0) {
					comunes.mostrarNotificacion(Mensajes.MSG_RESERVA_HORA_INICIO_MAYOR.getMensaje(), 5000,
							NotificationVariant.LUMO_ERROR);
					valido = false;
				}
			}
			return valido;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que valida la reserva y la guarda (si es válido).
	 */
	private void validarGuardar() {
		try {
			if (validarReserva()) {
				binder.writeBean(reserva);
				fireEvent(new SaveEvent(this, reserva));
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
	public static abstract class GestionReservasFormEvent extends ComponentEvent<GestionReservasForm> {
		private static final long serialVersionUID = 1L;
		private Reserva reserva;

		/**
		 * Constructor de la clase.
		 * 
		 * @param source  Origen
		 * @param reserva Reserva
		 */
		protected GestionReservasFormEvent(GestionReservasForm source, Reserva reserva) {
			super(source, false);
			this.reserva = reserva;
		}

		/**
		 * Función que devuelve la reserva asociada a la clase.
		 * 
		 * @return Reserva asociada a la clase
		 */
		public Reserva getReserva() {
			return reserva;
		}
	}

	/**
	 * Clase estática para definir el evento "Guardar" del formulario de Gestión de
	 * Reservas.
	 * 
	 * @author Lisa
	 *
	 */
	public static class SaveEvent extends GestionReservasFormEvent {
		private static final long serialVersionUID = 1L;

		SaveEvent(GestionReservasForm gestionReservasForm, Reserva reserva) {
			super(gestionReservasForm, reserva);
		}
	}

	/**
	 * Clase estática para definir el evento "Cancelar" del formulario de Gestión de
	 * Reservas.
	 * 
	 * @author Lisa
	 *
	 */
	public static class CloseEvent extends GestionReservasFormEvent {
		private static final long serialVersionUID = 1L;

		CloseEvent(GestionReservasForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
}
