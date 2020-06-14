package com.vaadin.gestionaulasinformatica.ui.views.mantenimientoaulas;

// Imports Java
import java.util.List;

// Imports Vaadin
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

// Imports backend
import com.vaadin.gestionaulasinformatica.backend.entity.Aula;
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;

/**
 * Clase que contiene el formulario del Mantenimiento de Aulas.
 * 
 * @author Lisa
 *
 */
public class MantAulasForm extends FormLayout {
	private static final long serialVersionUID = 1L;

	private List<PropietarioAula> lstPropietarios;
	private List<PropietarioAula> lstCentros;

	protected TextField nombreAula;
	protected NumberField capacidad;
	protected NumberField numOrdenadores;
	protected ComboBox<PropietarioAula> ubicacionCentro;
	protected ComboBox<PropietarioAula> propietarioAula;

	private Button btnGuardar = new Button("Guardar");
	private Button btnEliminar = new Button("Eliminar");
	private Button btnCerrar = new Button("Cancelar");

	private Binder<Aula> binder = new BeanValidationBinder<>(Aula.class);
	private Aula aula;

	/**
	 * Constructor de la clase.
	 * 
	 * @param propietarios Lista con todos los propietarios de aulas que hay en la
	 *                     BD
	 * @param centros      Lista con todos los centros que hay en la BD
	 */
	public MantAulasForm(List<PropietarioAula> propietarios, List<PropietarioAula> centros) {
		addClassName("mant-aulas-form");

		this.lstPropietarios = propietarios;
		this.lstCentros = centros;

		configurarCamposFormulario();

		binder.bindInstanceFields(this);

		add(nombreAula, capacidad, numOrdenadores, ubicacionCentro, propietarioAula, crearBotonesMantenimiento());
	}

	/**
	 * Función que configura los campos del formulario.
	 */
	private void configurarCamposFormulario() {
		try {
			nombreAula = new TextField("Nombre del Aula");

			capacidad = new NumberField("Capacidad");
			capacidad.setMin(0);
			capacidad.setValue((double) 0);
			capacidad.setHasControls(true);
			
			numOrdenadores = new NumberField("Número de Ordenadores");
			numOrdenadores.setMin(0);
			numOrdenadores.setValue((double) 0);
			numOrdenadores.setHasControls(true);

			ubicacionCentro = new ComboBox<>("Centro");
			ubicacionCentro.setPlaceholder("Seleccione");
			ubicacionCentro.setItems(lstCentros);
			ubicacionCentro.setItemLabelGenerator(PropietarioAula::getNombrePropietarioAula);
			ubicacionCentro.setRequiredIndicatorVisible(true);

			propietarioAula = new ComboBox<>("Propietario del Aula");
			propietarioAula.setPlaceholder("Seleccione");
			propietarioAula.setItems(lstPropietarios);
			propietarioAula.setItemLabelGenerator(PropietarioAula::getNombrePropietarioAula);
			propietarioAula.setRequiredIndicatorVisible(true);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que crea el layout de botones para guardar, eliminar o cerrar el
	 * editor.
	 * 
	 * @return Layout de botones
	 */
	private HorizontalLayout crearBotonesMantenimiento() {
		HorizontalLayout botonesMantenimiento;

		try {
			btnGuardar.addClickListener(click -> validarGuardar());
			btnGuardar.addClickShortcut(Key.ENTER); // Se guarda al pulsar Enter en el teclado
			btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			btnEliminar.addClickListener(click -> fireEvent(new DeleteEvent(this, binder.getBean())));
			btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);

			btnCerrar.addClickListener(click -> fireEvent(new CloseEvent(this)));
			btnCerrar.addClickShortcut(Key.ESCAPE); // Se cierra al pulsar ESC en el teclado
			btnCerrar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

			binder.addStatusChangeListener(evt -> btnGuardar.setEnabled(binder.isValid()));

			botonesMantenimiento = new HorizontalLayout(btnGuardar, btnEliminar, btnCerrar);
			botonesMantenimiento.addClassName("mant-aulas-botones-form");

			return botonesMantenimiento;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que establece el aula actual del binder.
	 * 
	 * @param aula Aula actual
	 */
	public void setAula(Aula aula) {
		try {
			this.aula = aula;
			binder.readBean(aula);
			
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que valida el aula y la guarda (si es válido).
	 */
	private void validarGuardar() {
		try {
			binder.writeBean(aula);
			fireEvent(new SaveEvent(this, aula));

		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clase estática y abstracta para definir los eventos del formulario de
	 * Mantenimiento de Centros y Departamentos.
	 * 
	 * @author Lisa
	 *
	 */
	public static abstract class MantAulasFormEvent extends ComponentEvent<MantAulasForm> {
		private static final long serialVersionUID = 1L;
		private Aula aula;

		/**
		 * Constructor de la clase.
		 * 
		 * @param source Origen
		 * @param aula   Aula
		 */
		protected MantAulasFormEvent(MantAulasForm source, Aula aula) {
			super(source, false);
			this.aula = aula;
		}

		/**
		 * Función que devuelve el aula asociada a la clase.
		 * 
		 * @return Aula asociada a la clase
		 */
		public Aula getAula() {
			return aula;
		}
	}

	/**
	 * Clase estática para definir el evento "Guardar" del formulario de
	 * Mantenimiento de Aulas.
	 * 
	 * @author Lisa
	 *
	 */
	public static class SaveEvent extends MantAulasFormEvent {
		private static final long serialVersionUID = 1L;

		SaveEvent(MantAulasForm source, Aula aula) {
			super(source, aula);
		}
	}

	/**
	 * Clase estática para definir el evento "Eliminar" del formulario de
	 * Mantenimiento de Aulas.
	 * 
	 * @author Lisa
	 *
	 */
	public static class DeleteEvent extends MantAulasFormEvent {
		private static final long serialVersionUID = 1L;

		DeleteEvent(MantAulasForm source, Aula aula) {
			super(source, aula);
		}

	}

	/**
	 * Clase estática para definir el evento "Cerrar" del formulario de
	 * Mantenimiento de Aulas.
	 * 
	 * @author Lisa
	 *
	 */
	public static class CloseEvent extends MantAulasFormEvent {
		private static final long serialVersionUID = 1L;

		CloseEvent(MantAulasForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

}
