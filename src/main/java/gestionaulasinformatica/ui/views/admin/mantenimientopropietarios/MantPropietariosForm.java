package gestionaulasinformatica.ui.views.admin.mantenimientopropietarios;

import java.util.List;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import gestionaulasinformatica.backend.entity.PropietarioAula;
import gestionaulasinformatica.backend.entity.Usuario;

/**
 * Clase que contiene el formulario del Mantenimiento de Centros y
 * Departamentos.
 * 
 * @author Lisa
 *
 */
public class MantPropietariosForm extends FormLayout {
	private static final long serialVersionUID = 1L;

	private List<Usuario> lstResponsables;

	protected TextField idPropAula;
	protected TextField nombrePropAula;
	protected ComboBox<Usuario> responsable;

	private Button btnGuardar;
	protected Button btnEliminar;
	private Button btnCancelar;

	private Binder<PropietarioAula> binder;
	private PropietarioAula propietarioAula;

	/**
	 * Constructor de la clase.
	 */
	public MantPropietariosForm(List<Usuario> responsables) {
		try {
			addClassName("mant-propietarios-form");

			this.lstResponsables = responsables;

			configurarCamposFormulario();

			binder = new BeanValidationBinder<>(PropietarioAula.class);
			binder.bind(idPropAula, "idPropietarioAula");
			binder.bind(nombrePropAula, "nombrePropietarioAula");
			binder.bind(responsable, "usuarioResponsable");

			add(idPropAula, nombrePropAula, responsable, getFormToolbar());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura los campos del formulario.
	 */
	private void configurarCamposFormulario() {
		try {
			idPropAula = new TextField("ID del centro/departamento");
			nombrePropAula = new TextField("Nombre del centro/departamento");

			responsable = new ComboBox<>("Responsable");
			responsable.setPlaceholder("Seleccione");
			responsable.setItems(lstResponsables);
			responsable.setItemLabelGenerator(Usuario::getNombreApellidosUsuario);
			responsable.setRequiredIndicatorVisible(true);

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
	private HorizontalLayout getFormToolbar() {
		HorizontalLayout formToolbar;

		try {

			btnGuardar = new Button("Guardar");
			btnGuardar.addClickListener(click -> validarGuardar());
			btnGuardar.addClickShortcut(Key.ENTER); // Se guarda al pulsar Enter en el teclado
			btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			btnEliminar = new Button("Eliminar");
			btnEliminar.addClickListener(click -> fireEvent(new DeleteEvent(this, propietarioAula)));
			btnEliminar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

			btnCancelar = new Button("Cancelar");
			btnCancelar.addClickListener(click -> fireEvent(new CloseEvent(this)));
			btnCancelar.addClickShortcut(Key.ESCAPE); // Se cierra al pulsar ESC en el teclado
			btnCancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);

			binder.addStatusChangeListener(evt -> btnGuardar.setEnabled(binder.isValid()));

			formToolbar = new HorizontalLayout(btnGuardar, btnEliminar, btnCancelar);
			formToolbar.addClassName("toolbar");

			return formToolbar;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que establece el propietario actual del binder.
	 * 
	 * @param propietario Propietario actual
	 */
	public void setPropietarioAula(PropietarioAula propietario) {
		try {
			this.propietarioAula = propietario;
			binder.readBean(propietario);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que valida el propietario y lo guarda.
	 */
	private void validarGuardar() {
		try {
			binder.writeBean(propietarioAula);
			fireEvent(new SaveEvent(this, propietarioAula));

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
	public static abstract class MantPropietariosAulaFormEvent extends ComponentEvent<MantPropietariosForm> {
		private static final long serialVersionUID = 1L;
		private PropietarioAula propietario;

		/**
		 * Constructor de la clase.
		 * 
		 * @param source      Origen
		 * @param propietario Propietario de aulas
		 */
		protected MantPropietariosAulaFormEvent(MantPropietariosForm source, PropietarioAula propietario) {
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
	public static class SaveEvent extends MantPropietariosAulaFormEvent {
		private static final long serialVersionUID = 1L;

		SaveEvent(MantPropietariosForm source, PropietarioAula propietario) {
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
	public static class DeleteEvent extends MantPropietariosAulaFormEvent {
		private static final long serialVersionUID = 1L;

		DeleteEvent(MantPropietariosForm source, PropietarioAula propietario) {
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
	public static class CloseEvent extends MantPropietariosAulaFormEvent {
		private static final long serialVersionUID = 1L;

		CloseEvent(MantPropietariosForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
}
