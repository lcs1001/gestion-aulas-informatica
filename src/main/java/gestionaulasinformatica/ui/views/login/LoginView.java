package gestionaulasinformatica.ui.views.login;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import gestionaulasinformatica.backend.data.Rol;
import gestionaulasinformatica.backend.entity.Usuario;
import gestionaulasinformatica.backend.service.UsuarioService;
import gestionaulasinformatica.ui.Comunes;
import gestionaulasinformatica.ui.Mensajes;

@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginView.class.getName());

	private UsuarioService usuarioService;
	private Comunes comunes;
	private LoginForm login;

	/**
	 * Constructor de la clase.
	 */
	public LoginView(UsuarioService usuarioService) {
		Anchor consultaReservas;

		try {
			addClassName("login-view");
			setSizeFull();

			this.usuarioService = usuarioService;
			this.comunes = new Comunes();

			// Contenido centrado en la ventana
			setJustifyContentMode(JustifyContentMode.CENTER);
			setAlignItems(Alignment.CENTER);

			login = new LoginForm();

			// Se pasa el inicio de sesión a Spring Security
			login.setAction("login");

			login.addForgotPasswordListener(e -> contrasenaOlvidada());

			consultaReservas = new Anchor("", "Acceso consulta de reservas y disponibilidad de aulas");

			add(new H1("Gestión de Aulas"), login, consultaReservas);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que muestra una notificación en caso de que se pulse en "Forgot password".
	 */
	private void contrasenaOlvidada() {
		String mensaje = "";
		List<Usuario> admin;
		String correoAdmin = "";
		try {
			mensaje += Mensajes.MSG_CONTACTAR_ADMIN.getMensaje();
			
			admin = usuarioService.findByRolUsuario(Rol.ADMIN);
			
			for (Usuario usuario : admin) {
				correoAdmin += usuario.getCorreoUsuario();
			}
			
			mensaje += " (" + correoAdmin + ")";
			comunes.mostrarNotificacion(mensaje, 3000, null);
			
		}catch(Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que controla el evento "antes de entrar".
	 */
	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		try {
			// Informa al usuario de un error de autenticación
			if (!beforeEnterEvent.getLocation().getQueryParameters().getParameters()
					.getOrDefault("Error", Collections.emptyList()).isEmpty()) {
				login.setError(true);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}
}
