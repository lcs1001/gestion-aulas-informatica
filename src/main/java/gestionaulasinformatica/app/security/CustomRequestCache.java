package gestionaulasinformatica.app.security;

import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Clase que realiza un seguimiento de la URL a la que se está intentando
 * acceder.
 * 
 * @author Lisa
 *
 */
class CustomRequestCache extends HttpSessionRequestCache {

	/**
	 * Función que guarda las solicitudes no autenticadas para poder redirigir al
	 * usuario a la página que intentaba acceder una vez haya iniciado sesión.
	 */
	@Override
	public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
		if (!SecurityUtils.isFrameworkInternalRequest(request)) {
			super.saveRequest(request, response);
		}
	}

}