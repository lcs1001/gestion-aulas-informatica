package com.vaadin.gestionaulasinformatica.ui;

/**
 * Enumeración que contiene los mensajes que se pueden mostrar al usuario.
 * 
 * @author Lisa
 *
 */
public enum Mensajes {
	MSG_CONSULTA_RESERVA_CAP("No se pueden consultar reservas por la capacidad del aula."),
	MSG_CONSULTA_RESERVA_ORD("No se pueden consultar reservas por el número de ordenadores del aula."),
	MSG_CONSULTA_RESPONSABLE("El Centro/Departamento es un campo obligatorio.");

	private final String mensaje;

	Mensajes(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * Función que devuelve el mensaje asociado.
	 * 
	 * @return Mensaje asociado
	 */
	public String getMensaje() {
		return this.mensaje;
	}

}
