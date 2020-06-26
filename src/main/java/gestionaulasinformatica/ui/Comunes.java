package gestionaulasinformatica.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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

	/**
	 * Función que devuelve el locale de España.
	 * 
	 * @return Locale de España,
	 */
	public Locale getLocaleES() {
		return new Locale("es", "ES");
	}

	/**
	 * Función que devuelve una lista con todos los días de la semana.
	 * 
	 * @return Lista con todos los días de la semana
	 */
	public List<String> getDiasSemana() {
		return new ArrayList<String>(
				Arrays.asList("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"));
	}

	/**
	 * Función que devuelve el día de la semana que se corresponde al número pasado.
	 * 
	 * @return Lista con todos los días de la semana
	 */
	public String getDiaSemana(Integer numDia) {
		switch (numDia) {
			case 1: return "Lunes";
			case 2: return "Martes";
			case 3: return "Miércoles";
			case 4: return "Jueves";
			case 5: return "Viernes";
			case 6: return "Sábado";
			case 7: return "Domingo";
			default: return "";
		}
	}
	
	public Label getTituloVentana(String nombre) {
		Label titulo = new Label(nombre);
		titulo.addClassName("titulo-pagina");
		
		return titulo;
	}
}
