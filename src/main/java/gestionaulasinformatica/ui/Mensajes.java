package gestionaulasinformatica.ui;

/**
 * Enumeración que contiene los mensajes que se pueden mostrar al usuario.
 * 
 * @author Lisa
 *
 */
public enum Mensajes {
	
	// Campos incorrectos
	MSG_CONSULTA_HORA_DESDE_MAYOR(
			"La hora desde la que se quiere filtrar debe ser menor que la hora hasta la que se quiere filtrar"),

	MSG_CONSULTA_FECHA_DESDE_MAYOR(
			"La fecha desde la que se quiere filtrar debe ser menor que la fecha hasta la que se quiere filtrar"),
	
	MSG_MODIFICAR_SOLO_UNA_RESERVA("Solo se puede modificar una reserva"),

	MSG_RESERVA_HORA_INICIO_MAYOR("La hora de inicio de la reserva debe ser menor que la hora de fin"),
	
	MSG_RESERVA_FECHA_INICIO_MAYOR("La fecha de inicio de la reserva debe ser menor que la fecha de fin"),

	// No hay datos / No hay datos que concuerdan	
	MSG_NO_CONSULTA_RESERVAS("No hay reservas que concuerden con los filtros aplicados"),

	MSG_NO_CONSULTA_AULAS("No hay aulas que concuerden con los filtros aplicados"),

	MSG_NO_AULAS("No hay aulas asociadas al propietario seleccionado"),

	MSG_NO_OPERACIONES_HR("No se han realizado operaciones con las reservas en esas fechas"),

	MSG_NO_RESERVAS("No hay reservas realizadas a partir de la fecha actual"),

	MSG_NO_RESERVAS_SELECCIONADAS("No se ha seleccionado ninguna reserva"),	

	// Campos obligatorios
	MSG_CENTRO_DPTO_OBLIGATORIO("El Centro/Departamento es un campo obligatorio"),

	MSG_CONSULTA_RESERVA_FECHA_HORA_OBLIGATORIO(
			"Para filtrar por fecha y hora, \"Fecha desde\", \"Hora desde\" y \"Hora hasta\" son campos obligatorios"),
	
	MSG_TODOS_CAMPOS_OBLIGATORIOS("Todos los campos son obligatorios"), 
	
	// Errores
	MSG_ERROR_ACCION("Se ha producido un error, la acción no se puede llevar a cabo"),
	
	MSG_ERROR_GUARDAR("Se ha producido un error al guardar, revise los campos"),
	
	// Varios
	MSG_CONTACTAR_ADMIN("Póngase en contacto con el administrador");	

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
