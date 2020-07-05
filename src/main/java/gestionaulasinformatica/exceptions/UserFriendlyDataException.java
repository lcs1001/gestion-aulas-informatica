package gestionaulasinformatica.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

/**
 * Clase que define la excepción de violación de integridad de los datos, que
 * contiene un mensaje que se muestra al usuario.
 * 
 * @author Lisa
 *
 */
public class UserFriendlyDataException extends DataIntegrityViolationException {

	private static final long serialVersionUID = 1L;

	public UserFriendlyDataException(String message) {
		super(message);
	}

}