package com.Calendar.ProyectoPrueba;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar.Events.CalendarImport;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@SuppressWarnings("serial")
@Theme("mytheme")
public class MyUI extends UI {

	private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	// Cambio del alcance de CALENDAR_READONLY a CALENDAR para poder insertar
	// eventos
	private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

	/**
	 * Crea un objeto Credential autorizado.
	 * 
	 * @param HTTP_TRANSPORT La red HTTP Transport.
	 * @return Un objeto Credential autorizado.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = CalendarImport.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	/***
	 * Funcion que muestra los siguientes 10 eventos del Google Calendar primario.
	 * 
	 * @param service Servicio del calendario
	 */
	private void getEventos(Calendar service) {
		DateTime now = new DateTime(System.currentTimeMillis());
		Events events;

		try {
			events = service.events().list("primary").setMaxResults(10).setTimeMin(now).setOrderBy("startTime")
					.setSingleEvents(true).execute();

			List<com.google.api.services.calendar.model.Event> items = events.getItems();

			VerticalLayout layout = new VerticalLayout();

			if (items.isEmpty()) {
				Label label = new Label("No se han encontrado eventos");
				layout.addComponent(label);
				setContent(layout);
			} else {
				Label label1 = new Label("Eventos:");
				layout.addComponent(label1);
				for (com.google.api.services.calendar.model.Event event : items) {
					DateTime start = ((com.google.api.services.calendar.model.Event) event).getStart().getDateTime();
					if (start == null) {
						start = event.getStart().getDate();
					}
					Label label2 = new Label(event.getSummary() + " " + start + " ");
					layout.addComponent(label2);
				}
				setContent(layout);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/***
	 * Función que inserta un evento en Google Calendar.
	 * 
	 * @param titulo Título del evento a insertar
	 * @param inicio Fecha y hora de inicio del evento
	 * @param fin    Fecha y hora de fin del evento
	 */
	private void setEvento(Calendar service, String titulo, String inicioE, String finE) {
		com.google.api.services.calendar.model.Event event;
		DateTime inicioEvento;
		DateTime finEvento;
		EventDateTime inicio;
		String calendarId;
		EventReminder[] reminderOverrides;

		try {
			event = new com.google.api.services.calendar.model.Event().setSummary(titulo);

			inicioEvento = new DateTime(inicioE);
			inicio = new EventDateTime().setDateTime(inicioEvento).setTimeZone("Europe/Madrid");
			event.setStart(inicio);

			finEvento = new DateTime(finE);
			EventDateTime end = new EventDateTime().setDateTime(finEvento).setTimeZone("Europe/Madrid");
			event.setEnd(end);

			reminderOverrides = new EventReminder[] { new EventReminder().setMethod("email").setMinutes(24 * 60),
					new EventReminder().setMethod("popup").setMinutes(10), };

			com.google.api.services.calendar.model.Event.Reminders reminders = new com.google.api.services.calendar.model.Event.Reminders()
					.setUseDefault(false).setOverrides(Arrays.asList(reminderOverrides));
			event.setReminders(reminders);

			calendarId = "primary";
			event = service.events().insert(calendarId, event).execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {

		// Build a new authorized API client service.
		NetHttpTransport HTTP_TRANSPORT;
		Calendar service;

		// Objetos para la insercion de un evento
		String tituloEvento;
		String inicioEvento;
		String finEvento;

		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
					.setApplicationName(APPLICATION_NAME).build();
			// Se incializan los objetos para crear el evento y se inserta
			tituloEvento = new String("Evento prueba inserción");
			inicioEvento = new String("2020-03-26T10:00:00.000+01:00");
			finEvento = new String("2020-03-26T22:00:00.000+01:00");

			//setEvento(service, tituloEvento, inicioEvento, finEvento);

			// Se obtienen los eventos para ver que se ha insertado correctamente
			getEventos(service);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@WebServlet(urlPatterns = "/*", name = "Servlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
	public static class Servlet extends VaadinServlet {
	}
}