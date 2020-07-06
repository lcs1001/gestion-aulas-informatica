package gestionaulasinformatica.ui.views.login;

import java.util.Collections;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import gestionaulasinformatica.ui.Comunes;
import gestionaulasinformatica.ui.Mensajes;

@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;
	
	private Comunes comunes;
	private LoginForm login;

	/**
	 * Constructor de la clase.
	 */
	public LoginView() {
		Anchor consultaReservas;

		try {
			addClassName("login-view");
			setSizeFull();
			
			comunes = new Comunes();

			// Contenido centrado en la ventana
			setJustifyContentMode(JustifyContentMode.CENTER);
			setAlignItems(Alignment.CENTER);

			login = new LoginForm();

			// Se pasa el inicio de sesión a Spring Security
			login.setAction("login");
			
			login.addForgotPasswordListener(e -> comunes.mostrarNotificacion(Mensajes.MSG_CONTACTAR_ADMIN.getMensaje(), 3000, null));
			
			consultaReservas = new Anchor("", "Acceso Consulta");
			
			add(new H1("Gestión de Aulas"), login, consultaReservas);

		} catch (Exception e) {
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
			throw e;
		}

	}
}
