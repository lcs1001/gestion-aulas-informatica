package gestionaulasinformatica.app.security;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.shared.ApplicationConstants;

import gestionaulasinformatica.ui.views.errors.AccesoDenegadoView;
import gestionaulasinformatica.ui.views.errors.RutaPersonalizadaNoEncontradaError;
import gestionaulasinformatica.ui.views.login.LoginView;

/**
 * Clase que se encarga de todas las operaciones estáticas relacionadas con la
 * seguridad y derechos de consulta de los beans de la interfaz de usuario.
 * 
 * @author Lisa
 *
 */
public final class SecurityUtils {

	private SecurityUtils() {
		// Util methods only
	}

	/**
	 * Gets the user name of the currently signed in user. Función que obtiene el
	 * nombre del usuario logeado
	 *
	 * @return El nombre del usuario logeado o null si no se ha logeado nadie
	 */
	public static String getUsername() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context != null && context.getAuthentication() != null) {
			Object principal = context.getAuthentication().getPrincipal();
			if (principal instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
				return userDetails.getUsername();
			}
		}
		// Anónimo o sin autenticar
		return null;
	}

	/**
	 * Comprueba si el acceso está permitido para el usuario logueado y la ventana
	 * asegurada (anotación @Secured) de la ventana.
	 *
	 * @param securedClass Clase de la ventana
	 * 
	 * @return Si el acceso está permitido o no
	 */
	public static boolean isAccessGranted(Class<?> securedClass) {
		final boolean publicView = LoginView.class.equals(securedClass) || AccesoDenegadoView.class.equals(securedClass)
				|| RutaPersonalizadaNoEncontradaError.class.equals(securedClass);

		// Siempre se permite acceso a ventanas públicas
		if (publicView) {
			return true;
		}

		Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();

		// All other views require authentication
		// Las demás ventanas requieren autenticación
		if (!isUserLoggedIn(userAuthentication)) {
			return false;
		}

		// Allow if no roles are required.
		Secured secured = AnnotationUtils.findAnnotation(securedClass, Secured.class);
		if (secured == null) {
			return true;
		}

		List<String> allowedRoles = Arrays.asList(secured.value());
		return userAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.anyMatch(allowedRoles::contains);
	}
	
	/**
	 * Función que comprueba si el usuario está logeado.
	 *
	 * @return Si el usuario está logeado o no
	 */
	public static boolean isUserLoggedIn() {
		return isUserLoggedIn(SecurityContextHolder.getContext().getAuthentication());
	}

	/**
	 * Función que comprueba si el usuario está logeado. Como Spring Security
	 * siempre crea un {@link AnonymousAuthenticationToken}, hay que ignorar esos
	 * tokens explícitamente.
	 * 
	 * @return Si el usuario está logeado o no
	 */
	private static boolean isUserLoggedIn(Authentication authentication) {
		return authentication != null
			&& !(authentication instanceof AnonymousAuthenticationToken);
	}
	
	/**
	 * Función que comprueba si la solicitud es de marco interno. Verifica si el
	 * parámetro de solicitud está presente y si su valor es consistente con alguno
	 * de los tipos de solicitud conocidos.
	 * 
	 * @param request Solicitud HTTP {@link HttpServletRequest}
	 * 
	 * @return Si la solicitud es interna a Vaadin o no
	 */
	static boolean isFrameworkInternalRequest(HttpServletRequest request) {
		final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
		return parameterValue != null && Stream.of(HandlerHelper.RequestType.values())
				.anyMatch(r -> r.getIdentifier().equals(parameterValue));
	}
}