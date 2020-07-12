package gestionaulasinformatica.ui;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

import gestionaulasinformatica.app.security.SecurityUtils;
import gestionaulasinformatica.ui.views.admin.historicoreservas.HistoricoReservasView;
import gestionaulasinformatica.ui.views.admin.mantenimientoaulas.MantAulasView;
import gestionaulasinformatica.ui.views.admin.mantenimientopropietarios.MantPropietariosView;
import gestionaulasinformatica.ui.views.admin.mantenimientousuarios.MantUsuariosView;
import gestionaulasinformatica.ui.views.consultaaulas.ConsultaAulasView;
import gestionaulasinformatica.ui.views.consultareservas.ConsultaReservasView;
import gestionaulasinformatica.ui.views.responsable.gestionreservas.GestionReservasView;
import gestionaulasinformatica.ui.views.responsable.reservaaulas.ReservaAulasView;

/**
 * Layout principal que define el formato de las ventanas.
 * 
 * @author Lisa
 *
 */
@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MainLayout.class.getName());

	/**
	 * Constructor de la clase.
	 */
	public MainLayout() {
		try {
			crearCabecera();
			crearMenuLateral();
			this.setDrawerOpened(false);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que crea la cabecera, que contiene el nombre de al app y el acceso al
	 * menú lateral.
	 */
	private void crearCabecera() {
		H1 titulo;
		Anchor logout;
		Anchor login;
		HorizontalLayout cabecera;

		try {
			titulo = new H1("Gestión Aulas Informática");
			titulo.addClassName("titulo");

			logout = new Anchor("logout", "Cerrar sesión    ");
			logout.add(new Icon(VaadinIcon.ARROW_CIRCLE_RIGHT_O));
			logout.getElement().setAttribute("router-ignore", true);

			login = new Anchor("login", "Iniciar sesión    ");
			login.add(new Icon(VaadinIcon.ARROW_CIRCLE_RIGHT_O));

			// Si se ha iniciado sesión se muestra "Cerrar sesión", si no "Iniciar sesión"
			if (SecurityUtils.getUsername() != null) {
				logout.setVisible(true);
				login.setVisible(false);
			} else {
				logout.setVisible(false);
				login.setVisible(true);
			}

			cabecera = new HorizontalLayout(new DrawerToggle(), titulo, logout, login);
			cabecera.addClassName("cabecera");
			cabecera.expand(titulo);
			cabecera.setWidth("100%");
			cabecera.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

			addToNavbar(cabecera);
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que crea el menú lateral de la app con los links a las diferentes
	 * ventanas.
	 */
	private void crearMenuLateral() {
		try {
			addToDrawer(new VerticalLayout(getVentanasDisponibles()));
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Función que obtiene las ventanas a las que puede acceder el usuario en
	 * función de su rol.
	 * 
	 * @return Lista de componentes que contiene los links a las ventanas a las que
	 *         puede acceder el usuario en función de su rol
	 */
	private Component[] getVentanasDisponibles() {
		final List<Component> ventanas = new ArrayList<>(7);

		RouterLink historicoReservasLink;
		RouterLink mantPropietariosLink;
		RouterLink mantAulasLink;
		RouterLink mantUsuariosLink;
		RouterLink reservaAulasLink;
		RouterLink gestionReservasLink;
		RouterLink consultaReservasLink;
		RouterLink consultaAulasLink;

		try {
			// Link a la ventana Histórico de Reservas
			if (SecurityUtils.isAccessGranted(HistoricoReservasView.class)) {
				historicoReservasLink = new RouterLink("Histórico de Reservas", HistoricoReservasView.class);
				historicoReservasLink.setHighlightCondition(HighlightConditions.sameLocation());

				ventanas.add(historicoReservasLink);
			}

			// Link a la ventana Mantenimiento de Centros y Departamentos
			if (SecurityUtils.isAccessGranted(MantPropietariosView.class)) {
				mantPropietariosLink = new RouterLink("Mantenimiento de Centros y Departamentos",
						MantPropietariosView.class);
				mantPropietariosLink.setHighlightCondition(HighlightConditions.sameLocation());

				ventanas.add(mantPropietariosLink);
			}

			// Link a la ventana Mantenimiento de Aulas
			if (SecurityUtils.isAccessGranted(MantAulasView.class)) {
				mantAulasLink = new RouterLink("Mantenimiento de Aulas", MantAulasView.class);
				mantAulasLink.setHighlightCondition(HighlightConditions.sameLocation());

				ventanas.add(mantAulasLink);
			}

			// Link a la ventana Mantenimiento de Usuarios
			if (SecurityUtils.isAccessGranted(MantUsuariosView.class)) {
				mantUsuariosLink = new RouterLink("Mantenimiento de Usuarios", MantUsuariosView.class);
				mantUsuariosLink.setHighlightCondition(HighlightConditions.sameLocation());

				ventanas.add(mantUsuariosLink);
			}

			// Link a la ventana Reserva de Aulas
			if (SecurityUtils.isAccessGranted(ReservaAulasView.class)) {
				reservaAulasLink = new RouterLink("Reserva de Aulas", ReservaAulasView.class);
				reservaAulasLink.setHighlightCondition(HighlightConditions.sameLocation());

				ventanas.add(reservaAulasLink);
			}

			// Link a la ventana Gestión de Reservas
			if (SecurityUtils.isAccessGranted(GestionReservasView.class)) {
				gestionReservasLink = new RouterLink("Gestión  de Reservas", GestionReservasView.class);
				gestionReservasLink.setHighlightCondition(HighlightConditions.sameLocation());

				ventanas.add(gestionReservasLink);
			}

			// Link a la ventana Consulta de Reservas
			consultaReservasLink = new RouterLink("Consulta de Reservas", ConsultaReservasView.class);
			consultaReservasLink.setHighlightCondition(HighlightConditions.sameLocation());

			ventanas.add(consultaReservasLink);

			// Link a la ventana Consulta de Disponibilidad de Aulas
			consultaAulasLink = new RouterLink("Consulta de Disponibilidad de Aulas", ConsultaAulasView.class);
			consultaAulasLink.setHighlightCondition(HighlightConditions.sameLocation());

			ventanas.add(consultaAulasLink);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}

		return ventanas.toArray(new Component[ventanas.size()]);

	}
}
