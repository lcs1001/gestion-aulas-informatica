package com.vaadin.gestionaulasinformatica.ui;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;

public class MantPropietarioAulaForm extends FormLayout {
	TextField idPropAula = new TextField("ID del Centro/Departamento");
	TextField nombrePropAula = new TextField("Nombre del Centro/Departamento");
	TextField nombreResponsable = new TextField("Nombre del Responsable");
	TextField apellidosResponsable = new TextField("Apellidos del Responsable");
	EmailField correoResponsable = new EmailField("Correo del Responsable");
	TextField telefonoResponsable = new TextField("Teléfono del Responsable");

	Button guardar = new Button("Guardar");
	Button eliminar = new Button("Eliminar");
	Button cerrar = new Button("Cancelar");

	Binder<PropietarioAula> binder = new BeanValidationBinder<>(PropietarioAula.class);

	public MantPropietarioAulaForm() {
		addClassName("mant-propietarios-form");
		
		idPropAula.setRequiredIndicatorVisible(true);
		nombrePropAula.setRequiredIndicatorVisible(true);

		binder.bindInstanceFields(this);

		add(idPropAula, nombrePropAula, nombreResponsable, apellidosResponsable, correoResponsable, telefonoResponsable,
				crearButtonsLayout());
	}

	private Component crearButtonsLayout() {
		guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		eliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cerrar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		// Se guarda al pulsar Enter en el teclado
		guardar.addClickShortcut(Key.ENTER);
		// Se cierra al pulsar ESC en el teclado
		cerrar.addClickShortcut(Key.ESCAPE);

		guardar.addClickListener(click -> validarGuardar());
		eliminar.addClickListener(click -> fireEvent(new EliminarEvent(this, binder.getBean())));
		cerrar.addClickListener(click -> fireEvent(new CerrarEvent(this)));

		binder.addStatusChangeListener(evt -> guardar.setEnabled(binder.isValid()));

		return new HorizontalLayout(guardar, eliminar, cerrar);
	}

	/**
	 * Función que valida el propietario y lo guarda (si es válido).
	 */
	private void validarGuardar() {
		if (binder.isValid()) {
			fireEvent(new GuardarEvent(this, binder.getBean()));
		}
	}

	// Eventos
	public static abstract class MantPropietarioAulaFormEvent extends ComponentEvent<MantPropietarioAulaForm> {
		private PropietarioAula propietario;

		protected MantPropietarioAulaFormEvent(MantPropietarioAulaForm source, PropietarioAula propietario) {
			super(source, false);
			this.propietario = propietario;
		}

		public PropietarioAula getPropietarioAula() {
			return propietario;
		}
	}

	public static class GuardarEvent extends MantPropietarioAulaFormEvent {
		GuardarEvent(MantPropietarioAulaForm source, PropietarioAula propietario) {
			super(source, propietario);
		}
	}

	public static class EliminarEvent extends MantPropietarioAulaFormEvent {
		EliminarEvent(MantPropietarioAulaForm source, PropietarioAula propietario) {
			super(source, propietario);
		}

	}

	public static class CerrarEvent extends MantPropietarioAulaFormEvent {
		CerrarEvent(MantPropietarioAulaForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
}
