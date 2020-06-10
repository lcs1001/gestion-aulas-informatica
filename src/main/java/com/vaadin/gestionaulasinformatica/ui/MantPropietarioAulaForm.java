package com.vaadin.gestionaulasinformatica.ui;

// Import Vaadin
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

// Imports backend
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.entity.TipoPropietarioAula;

/**
 * Clase que contiene el formulario del Mantenimiento de Centros y
 * Departamentos.
 * 
 * @author Lisa
 *
 */
public class MantPropietarioAulaForm extends FormLayout {
	private static final long serialVersionUID = 1L;
	
	TextField idPropAula = new TextField("ID del Centro/Departamento");
	TextField nombrePropAula = new TextField("Nombre del Centro/Departamento");
	TextField nombreResponsable = new TextField("Nombre del Responsable");
	TextField apellidosResponsable = new TextField("Apellidos del Responsable");
	EmailField correoResponsable = new EmailField("Correo del Responsable");
	TextField telefonoResponsable = new TextField("Teléfono del Responsable");

	Button btnGuardar = new Button("Guardar");
	Button btnEliminar = new Button("Eliminar");
	Button btnCerrar = new Button("Cancelar");

	Binder<PropietarioAula> binder = new BeanValidationBinder<>(PropietarioAula.class);

	/**
	 * Constructor de la clase.
	 */
	public MantPropietarioAulaForm() {
		addClassName("form-mant-propietarios");
		
		binder.bindInstanceFields(this);
		binder.forField(idPropAula).bind("idPropietarioAula");
		binder.forField(nombrePropAula).bind("nombrePropietarioAula");

		add(idPropAula, nombrePropAula, nombreResponsable, apellidosResponsable, correoResponsable, telefonoResponsable,
				crearButtonsLayout());
	}

	/**
	 * Función que establece el propietario actual del binder.
	 * 
	 * @param propietario Propietario actual
	 */
	public void setPropietarioAula(PropietarioAula propietario) {
		binder.setBean(propietario);
	}

	/**
	 * Función que crea el layout de botones para guardar, eliminar o cerrar el
	 * editor.
	 * 
	 * @return Layout de botones
	 */
	private Component crearButtonsLayout() {
		btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);
		btnCerrar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		// Se guarda al pulsar Enter en el teclado
		btnGuardar.addClickShortcut(Key.ENTER);
		// Se cierra al pulsar ESC en el teclado
		btnCerrar.addClickShortcut(Key.ESCAPE);

		btnGuardar.addClickListener(click -> validarGuardar());
		btnEliminar.addClickListener(click -> fireEvent(new DeleteEvent(this, binder.getBean())));
		btnCerrar.addClickListener(click -> fireEvent(new CloseEvent(this)));

		binder.addStatusChangeListener(evt -> btnGuardar.setEnabled(binder.isValid()));

		return new HorizontalLayout(btnGuardar, btnEliminar, btnCerrar);
	}

	/**
	 * Función que valida el propietario y lo guarda (si es válido).
	 */
	private void validarGuardar() {
		if (binder.isValid()) {
			fireEvent(new SaveEvent(this, binder.getBean()));
		}
	}

	/**
	 * Clase estática y abstracta para definir los eventos del formulario de
	 * Mantenimiento de Centros y Departamentos.
	 * 
	 * @author Lisa
	 *
	 */
	public static abstract class MantPropietarioAulaFormEvent extends ComponentEvent<MantPropietarioAulaForm> {
		private PropietarioAula propietario;

		/**
		 * Constructor de la clase.
		 * 
		 * @param source      Origen
		 * @param propietario Propietario de aulas
		 */
		protected MantPropietarioAulaFormEvent(MantPropietarioAulaForm source, PropietarioAula propietario) {
			super(source, false);
			this.propietario = propietario;
		}

		/**
		 * Función que devuelve el propietario de aulas asociado a la clase.
		 * 
		 * @return Propietario de aulas asociado a la clase
		 */
		public PropietarioAula getPropietarioAula() {
			return propietario;
		}
	}

	/**
	 * Clase estática para definir el evento "Guardar" del formulario de
	 * Mantenimiento de Centros y Departamentos.
	 * 
	 * @author Lisa
	 *
	 */
	public static class SaveEvent extends MantPropietarioAulaFormEvent {
		SaveEvent(MantPropietarioAulaForm source, PropietarioAula propietario) {
			super(source, propietario);
		}
	}

	/**
	 * Clase estática para definir el evento "Eliminar" del formulario de
	 * Mantenimiento de Centros y Departamentos.
	 * 
	 * @author Lisa
	 *
	 */
	public static class DeleteEvent extends MantPropietarioAulaFormEvent {
		DeleteEvent(MantPropietarioAulaForm source, PropietarioAula propietario) {
			super(source, propietario);
		}

	}

	/**
	 * Clase estática para definir el evento "Cerrar" del formulario de
	 * Mantenimiento de Centros y Departamentos.
	 * 
	 * @author Lisa
	 *
	 */
	public static class CloseEvent extends MantPropietarioAulaFormEvent {
		CloseEvent(MantPropietarioAulaForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
}
