package gestionaulasinformatica.backend.data;

public class Rol {
	public static final String ADMIN = "ADMIN";
	public static final String RESPONSABLE = "RESPONSABLE";

	/**
	 * Constructor de la clase.
	 */
	private Rol() {
		// Static methods and fields only
	}

	/**
	 * Función que devuelve todos los roles de usuario.
	 * 
	 * @return Roles
	 */
	public static String[] getAllRoles() {
		return new String[] { ADMIN, RESPONSABLE };
	}
}