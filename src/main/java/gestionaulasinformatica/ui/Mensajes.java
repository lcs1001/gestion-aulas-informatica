package gestionaulasinformatica.ui;

/**
 * Enumeración que contiene los mensajes que se pueden mostrar al usuario.
 * 
 * @author Lisa
 *
 */
public enum Mensajes {
	MSG_CONSULTA_HORA_DESDE_MAYOR(
			"La hora desde la que se quiere filtrar debe ser menor que la hora hasta la que se quiere filtrar."),
	MSG_CONSULTA_FECHA_DESDE_MAYOR(
			"La fecha desde la que se quiere filtrar debe ser menor que la fecha hasta la que se quiere filtrar."),
	MSG_CONSULTA_RESPONSABLE("El Centro/Departamento es un campo obligatorio."),
	MSG_CONSULTA_RESERVA_FECHA_HORA(
			"Para filtrar por fecha y hora, \"Fecha desde\", \"Hora desde\" y \"Hora hasta\" son campos obligatorios."),
	MSG_NO_CONSULTA_RESERVAS("No hay reservas que concuerden con los filtros aplicados"),
	MSG_NO_CONSULTA_AULAS("No hay aulas que concuerden con los filtros aplicados"),
	MSG_NO_AULAS("No hay aulas asociadas al propietario seleccionado"),
	MSG_NO_OPERACIONES_HR("No se han realizado operaciones con las reservas en esas fechas"),
	MSG_NO_RESERVAS("No hay reservas realizadas a partir de la fecha actual"), 
	MSG_NO_RESERVAS_SELECCIONADAS("No se ha seleccionado ninguna reserva"), 
	MSG_MODIFICAR_SOLO_UNA_RESERVA("Solo se puede modificar una reserva");

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
