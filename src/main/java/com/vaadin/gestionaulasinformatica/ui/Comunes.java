package com.vaadin.gestionaulasinformatica.ui;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;

/**
 * Clase que contiene las funciones comunes para utilizar en cualquier clase.
 * 
 * @author Lisa
 *
 */
public class Comunes {

	/**
	 * Función que muestra la notificación con el mensaje pasado por parámetro.
	 * 
	 * @param mensaje          Mensaje de la notificación que se quiere mostrar.
	 * @param duracion         Duración de la notificación
	 * @param tipoNotificacion Tipo de notificación a mostrar (estilo)
	 */
	public void mostrarNotificacion(String mensaje, Integer duracion, NotificationVariant tipoNotificacion) {
		Label lblMensaje;
		Notification notificacion;

		try {
			lblMensaje = new Label(mensaje);

			notificacion = new Notification(lblMensaje);
			notificacion.setDuration(duracion);
			notificacion.setPosition(Position.MIDDLE);

			if (tipoNotificacion != null) {
				notificacion.addThemeVariants(tipoNotificacion);
			}

			notificacion.open();

		} catch (Exception e) {
			throw e;
		}
	}
}
