package gestionaulasinformatica.security;

import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

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

	/**
	 * Función que comprueba si algún usuario está autenticado. Como Spring Security
	 * siempre crea un {@link AnonymousAuthenticationToken}, hay que ignorar esos
	 * tokens explícitamente.
	 * 
	 * @return Si algún usuario está autenticado
	 */
	static boolean isUserLoggedIn() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && !(authentication instanceof AnonymousAuthenticationToken)
				&& authentication.isAuthenticated();
	}
}