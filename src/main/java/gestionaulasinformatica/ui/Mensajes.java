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
	
	MSG_AULA_CENTRO_EXISTENTE("Ya existe un aula con ese nombre en el centro que ha seleccionado"),

	// No hay datos / No hay datos que concuerdan
	MSG_NO_CONSULTA_RESERVAS("No hay reservas que cumplan con los filtros aplicados"),

	MSG_NO_CONSULTA_AULAS("No hay aulas que cumplan con los filtros aplicados"),

	MSG_NO_AULAS("No hay aulas asociadas al propietario seleccionado"),

	MSG_NO_OPERACIONES_HR("No se han realizado operaciones con las reservas en esas fechas"),

	MSG_NO_RESERVAS("No hay reservas realizadas a partir de la fecha actual o que cumplan con los filtros aplicados"),

	MSG_NO_RESERVAS_SELECCIONADAS("No se ha seleccionado ninguna reserva"),

	MSG_AULA_NO_DISPONIBLE("El aula no está disponible en la/s fecha/s y horas indicadas"),

	// Campos obligatorios
	MSG_CENTRO_DPTO_OBLIGATORIO("El Centro/Departamento es un campo obligatorio"),

	MSG_CONSULTA_RESERVA_FECHA_HORA_OBLIGATORIO(
			"Para filtrar por fecha y hora, \"Fecha desde\", \"Hora desde\" y \"Hora hasta\" son campos obligatorios"),

	MSG_TODOS_CAMPOS_OBLIGATORIOS("Todos los campos son obligatorios"),

	// Errores
	MSG_ERROR_ACCION("Se ha producido un error, la acción no se puede llevar a cabo"),

	MSG_ERROR_GUARDAR("Se ha producido un error al guardar, revise los campos"),

	MSG_LOGIN_INCORRECTO("El correo o la contraseña introducidos son incorrectos"),

	// Varios
	MSG_CONTACTAR_ADMIN("Póngase en contacto con el administrador"),

	ELIMINAR_USUARIO_BLOQUEADO_NO_PERMITIDO("El usuario está bloqueado y no se puede eliminar"),

	ELIMINAR_USUARIO_ACTUAL_NO_PERMITIDO("No puede eliminar su propio usuario"), 
	
	MSG_GUARDADO_CORRECTO("Se ha guardado correctamente"), 
	
	MSG_RESERVA_CORRECTA("Se ha registrado la reserva correctamente"), 
	
	MSG_BORRADO_CORRECTO("Se ha eliminado correctamente");

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
