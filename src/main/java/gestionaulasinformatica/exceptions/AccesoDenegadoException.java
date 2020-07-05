package gestionaulasinformatica.exceptions;

/**
 * Clase que define la excepción de acceso denegado.
 * 
 * @author Lisa
 *
 */
public class AccesoDenegadoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AccesoDenegadoException() {
	}

	public AccesoDenegadoException(String mensaje) {
		super(mensaje);
	}

}
