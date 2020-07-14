package gestionaulasinformatica.ui.views.perfilusuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import gestionaulasinformatica.backend.entity.Usuario;
import gestionaulasinformatica.ui.Comunes;
import gestionaulasinformatica.ui.Mensajes;

/**
 * Clase que contiene el formulario del Perfil de Usuario.
 * 
 * @author Lisa
 *
 */
public class PerfilUsuarioForm extends FormLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(PerfilUsuarioForm.class.getName());
	
	private Comunes comunes;
	private PasswordEncoder passwordEncoder;

	protected TextField nombreUsuario;
	protected TextField apellidosUsuario;
	protected EmailField correoUsuario;
	protected PasswordField contrasena;
	protected TextField telefonoUsuario;

	private HorizontalLayout toolbar;
	private Button btnGuardar;

	private Binder<Usuario> binder;
	private Usuario usuario;

	/**
	 * Constructor de la clase.
	 * 
	 * @param passwordEncoder Codificador de contraseñas
	 */
	public PerfilUsuarioForm(Comunes comunes,PasswordEncoder passwordEncoder) {
		try {
			this.comunes = comunes;
			this.passwordEncoder = passwordEncoder;
			
			addClassName("perfil-usuario-form");

			setResponsiveSteps(new ResponsiveStep("25em", 1), new ResponsiveStep("25em", 2),
					new ResponsiveStep("25em", 3), new ResponsiveStep("25em", 4));

			configurarCamposFormulario();

			binder = new BeanValidationBinder<>(Usuario.class);
			binder.bindInstanceFields(this);
			binder.forField(contrasena)
					.withValidator(cont -> cont.matches("^(|(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})$"),
							"Debe contener 6 o más caracteres, incluyendo dígitos, letras minúsculas y mayúsculas")
					.bind(user -> contrasena.getEmptyValue(), (user, cont) -> {
						if (!contrasena.getEmptyValue().equals(cont)) {
							user.setContrasenaHash(this.passwordEncoder.encode(cont));
						}
					});
			
			binder.forField(telefonoUsuario)
			.withValidator(telf -> telf.matches("^(|(?=.*\\d).{9,9})$"),
					"Debe tener 9 dígitos")
			.bind(user -> telefonoUsuario.getValue(), (user, telf) -> {
				if (!telefonoUsuario.getEmptyValue().equals(telf)) {
					user.setTelefonoUsuario(telf);
				}
			});
			
			configurarToolbar();

			add(nombreUsuario, 2);
			add(apellidosUsuario, 2);
			add(correoUsuario, 2);
			add(contrasena, 2);
			add(telefonoUsuario, 2);
			add(toolbar, 4);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que configura los campos del formulario.
	 */
	private void configurarCamposFormulario() {
		try {
			nombreUsuario = new TextField("Nombre del usuario");
			nombreUsuario.setClearButtonVisible(true);

			apellidosUsuario = new TextField("Apellidos del usuario");
			apellidosUsuario.setClearButtonVisible(true);

			correoUsuario = new EmailField("Correo del usuario");
			correoUsuario.setClearButtonVisible(true);

			contrasena = new PasswordField("Contraseña");
			contrasena.setRequiredIndicatorVisible(true);

			telefonoUsuario = new TextField("Teléfono del usuario");
			telefonoUsuario.setClearButtonVisible(true);
			telefonoUsuario.setMinLength(9);
			telefonoUsuario.setMaxLength(9);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que configura el toolbar que contiene: un botón para guardar los
	 * cambios y un botón para cancelar la modificación.
	 */
	private void configurarToolbar() {
		try {
			btnGuardar = new Button("Guardar");
			btnGuardar.setIcon(new Icon(VaadinIcon.CHECK));
			btnGuardar.addClickListener(click -> validarGuardar());
			btnGuardar.addClickShortcut(Key.ENTER); // Se guarda al pulsar Enter en el teclado
			btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			binder.addStatusChangeListener(evt -> btnGuardar.setEnabled(binder.isValid()));

			toolbar = new HorizontalLayout(btnGuardar);
			toolbar.addClassName("toolbar");

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que establece el usuario actual del binder.
	 * 
	 * @param usuario Usuario actual
	 */
	public void setUsuario(Usuario usuario) {
		try {
			this.usuario = usuario;
			binder.readBean(usuario);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}
	
	private Boolean validarUsuario() {
		Boolean valido = true;
		try {
			if(nombreUsuario.isEmpty() || apellidosUsuario.isEmpty() || correoUsuario.isEmpty() || contrasena.isEmpty() || telefonoUsuario.isEmpty()) {
				comunes.mostrarNotificacion(Mensajes.MSG_TODOS_CAMPOS_OBLIGATORIOS.getMensaje(), 3000,
						NotificationVariant.LUMO_ERROR);
				valido = false;
			}
		}catch(Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
		
		return valido;
	}

	/**
	 * Función que valida el usuario y lo guarda.
	 */
	private void validarGuardar() {
		try {
			if(validarUsuario()) {
				binder.writeBean(usuario);
				fireEvent(new SaveEvent(this, usuario));
			}
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Clase estática y abstracta para definir los eventos del formulario de
	 * Mantenimiento de Usuarios.
	 * 
	 * @author Lisa
	 *
	 */
	public static abstract class PerfilUsuarioFormEvent extends ComponentEvent<PerfilUsuarioForm> {
		private static final long serialVersionUID = 1L;
		private Usuario usuario;

		/**
		 * Constructor de la clase.
		 * 
		 * @param source  Origen
		 * @param usuario Usuario
		 */
		protected PerfilUsuarioFormEvent(PerfilUsuarioForm source, Usuario usuario) {
			super(source, false);
			this.usuario = usuario;
		}

		/**
		 * Función que devuelve el usuario asociado a la clase.
		 * 
		 * @return Usuario asociado a la clase
		 */
		public Usuario getUsuario() {
			return usuario;
		}
	}

	/**
	 * Clase estática para definir el evento "Guardar" del formulario de Perfil de
	 * Usuario.
	 * 
	 * @author Lisa
	 *
	 */
	public static class SaveEvent extends PerfilUsuarioFormEvent {
		private static final long serialVersionUID = 1L;

		SaveEvent(PerfilUsuarioForm source, Usuario usuario) {
			super(source, usuario);
		}
	}

//	/**
//	 * Clase estática para definir el evento "Cerrar" del formulario de Perfil de
//	 * Usuario.
//	 * 
//	 * @author Lisa
//	 *
//	 */
//	public static class CloseEvent extends PerfilUsuarioFormEvent {
//		private static final long serialVersionUID = 1L;
//
//		CloseEvent(PerfilUsuarioForm source) {
//			super(source, null);
//		}
//	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
}
