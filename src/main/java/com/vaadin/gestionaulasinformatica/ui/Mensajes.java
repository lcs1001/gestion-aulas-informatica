package com.vaadin.gestionaulasinformatica.ui;

/**
 * Enumeración que contiene los mensajes que se pueden mostrar al usuario.
 * 
 * @author Lisa
 *
 */
public enum Mensajes {
	MSG_CONSULTA_HORA_DESDE_MAYOR("La hora desde la que se quiere filtrar debe ser menor que la hora hasta la que se quiere filtrar."),
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
