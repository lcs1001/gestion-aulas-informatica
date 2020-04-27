package com.Calendar.ProyectoPrueba;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

// Google API imports
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
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.Calendar.Events.CalendarImport;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.vaadin.flow.component.html.Label;
//Vaadin imports
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;

@Route
public class MainView extends VerticalLayout implements PageConfigurator {
	public MainView() {
		// Crea un nuevo servicio API client autorizado
		NetHttpTransport HTTP_TRANSPORT;
		Calendar service;

		// Calendario
		String nombreCalendario;
		com.google.api.services.calendar.model.Calendar calendario;

		// Evento
		String tituloEvento;
		String inicioEvento;
		String finEvento;

		try {

			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

			// Inicialización del servicio Calendar con las credenciales OAuth válidas
			service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
					.setApplicationName(APPLICATION_NAME).build();

			// Inserción del calendario
			nombreCalendario = new String("Calendario Prueba");
			calendario = insertCalendar(service, nombreCalendario);

			// Inserción del evento en un determinado calendario
			tituloEvento = new String("Evento prueba inserción en el calendario " + nombreCalendario);
			inicioEvento = new String("2020-12-26T10:00:00.000+01:00");
			finEvento = new String("2020-12-26T22:00:00.000+01:00");

			insertEvent(service, calendario.getId(), tituloEvento, inicioEvento, finEvento);

			// Obtención de los eventos de un determinado calendario
			getEvents(service, calendario.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void configurePage(InitialPageSettings settings) {
		settings.addInlineWithContents("<meta name=\"google-signin-scope\" content=\"profile email\">",
				InitialPageSettings.WrapMode.NONE);
		settings.addInlineWithContents(
				"<meta name=\"google-signin-client_id\" content=\"871400351809-l38aa149odc6kj5bhg9oqb1d5g4lh2mm.apps.googleusercontent.com\">",
				InitialPageSettings.WrapMode.NONE);
		settings.addInlineWithContents("<script src=\"https://apis.google.com/js/platform.js\" async defer></script>",
				InitialPageSettings.WrapMode.NONE);
		settings.addInlineWithContents(
				"<div class=\"g-signin2\" data-theme=\"light\"></div>",
				InitialPageSettings.WrapMode.NONE);
	}

	private static final String APPLICATION_NAME = "Proyecto Prueba Google Calendar API";
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
		InputStream in;
		GoogleClientSecrets clientSecrets;
		GoogleAuthorizationCodeFlow flow;
		LocalServerReceiver receiver;
		Credential credentials = null;

		try {
			// Carga client secrets
			in = CalendarImport.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
			if (in == null) {
				throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
			}
			clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

			// Build flow and trigger user authorization request.
			flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
					.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
					.setAccessType("offline").build();
			receiver = new LocalServerReceiver.Builder().setPort(8888).build();
			credentials = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return credentials;

	}

	/***
	 * Funcion que muestra los siguientes 10 eventos del Google Calendar pasado.
	 * 
	 * @param service Servicio Calendar con las credenciales OAuth válidas
	 */
	private void getEvents(Calendar service, String idCalendario) {
		Events events;
		List<com.google.api.services.calendar.model.Event> items;
		String pageToken = null;

		try {

			// Se itera sobre los eventos del calendario pasado por parámetro
			do {
				events = service.events().list(idCalendario).setPageToken(pageToken).execute();
				items = events.getItems();

				if (items.isEmpty()) {
					Label label = new Label("No se han encontrado eventos");
					add(label);
				} else {
					Label label = new Label("Eventos:");
					add(label);

					for (com.google.api.services.calendar.model.Event event : items) {
						DateTime start = ((com.google.api.services.calendar.model.Event) event).getStart()
								.getDateTime();
						if (start == null) {
							start = event.getStart().getDate();
						}
						Label label2 = new Label(event.getSummary() + " " + start);
						add(label2);
					}
				}
				pageToken = events.getNextPageToken();
			} while (pageToken != null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/***
	 * Función que inserta un evento en un calendario.
	 * 
	 * @param service      Servicio Calendar con las credenciales OAuth válidas
	 * @param Idcalendario Calendario en el que insertar el evento
	 * @param titulo       Título del evento a insertar
	 * @param inicioE      Fecha y hora de inicio del evento
	 * @param finE         Fecha y hora de fin del evento
	 */
	private void insertEvent(Calendar service, String idCalendario, String titulo, String inicioE, String finE) {
		com.google.api.services.calendar.model.Event event;
		DateTime inicioEvento;
		DateTime finEvento;
		EventDateTime inicio;

		try {
			event = new com.google.api.services.calendar.model.Event().setSummary(titulo);

			inicioEvento = new DateTime(inicioE);
			inicio = new EventDateTime().setDateTime(inicioEvento).setTimeZone("Europe/Madrid");
			event.setStart(inicio);

			finEvento = new DateTime(finE);
			EventDateTime end = new EventDateTime().setDateTime(finEvento).setTimeZone("Europe/Madrid");
			event.setEnd(end);

			event = service.events().insert(idCalendario, event).execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * Crea e inserta un nuevo calendario.
	 * 
	 * @param service Servicio Calendar con las credenciales OAuth válidas
	 * @param nombre  Nombre del calendario a crear
	 * @return Calendario creado
	 */
	private com.google.api.services.calendar.model.Calendar insertCalendar(Calendar service, String nombre) {
		com.google.api.services.calendar.model.Calendar calendario;
		com.google.api.services.calendar.model.Calendar calendarioCreado = null;

		try {
			// Crea el calendario
			calendario = new com.google.api.services.calendar.model.Calendar();
			calendario.setSummary(nombre);
			calendario.setTimeZone("Europe/Madrid");

			// Inserta el calendario
			calendarioCreado = service.calendars().insert(calendario).execute();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return calendarioCreado;
	}
}
