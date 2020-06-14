package com.vaadin.gestionaulasinformatica.ui;

// Imports Vaadin
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

//Imports Views
import com.vaadin.gestionaulasinformatica.ui.views.consultaaulas.ConsultaAulasView;
import com.vaadin.gestionaulasinformatica.ui.views.historicoreservas.HistoricoReservasView;
import com.vaadin.gestionaulasinformatica.ui.views.mantenimientoaulas.MantAulasView;
import com.vaadin.gestionaulasinformatica.ui.views.mantenimientopropietarios.MantPropietariosView;

/**
 * Layout principal que define el formato de las ventanas.
 * 
 * @author Lisa
 *
 */
@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor de la clase.
	 */
	public MainLayout() {
		try {
			crearCabecera();
			crearMenuLateral();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que crea la cabecera, que contiene el nombre de al app y el acceso al
	 * menú lateral.
	 */
	private void crearCabecera() {
		H1 titulo;
		HorizontalLayout cabecera;

		try {
			titulo = new H1("Gestión Aulas Informática");
			titulo.addClassName("titulo");

			cabecera = new HorizontalLayout(new DrawerToggle(), titulo);
			cabecera.addClassName("cabecera");
			cabecera.setWidth("100%");
			cabecera.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

			addToNavbar(cabecera);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que crea el menú lateral de la app con los links a las diferentes
	 * ventanas.
	 */
	private void crearMenuLateral() {
		RouterLink consultaAulasLink;
		RouterLink mantPropietariosLink;
		RouterLink historicoReservasLink;
		RouterLink mantAulasLink;

		try {
			// Link a la ventana Consulta de Reservas y Disponibilidad de Aulas
			consultaAulasLink = new RouterLink("Consulta de Reservas y Aulas", ConsultaAulasView.class);
			consultaAulasLink.setHighlightCondition(HighlightConditions.sameLocation());

			// Link a la ventana Mantenimiento de Centros y Departamentos
			mantPropietariosLink = new RouterLink("Mantenimiento de Centros y Departamentos",
					MantPropietariosView.class);
			mantPropietariosLink.setHighlightCondition(HighlightConditions.sameLocation());

			// Link a la ventana Histórico de Reservas
			historicoReservasLink = new RouterLink("Histórico de Reservas", HistoricoReservasView.class);
			historicoReservasLink.setHighlightCondition(HighlightConditions.sameLocation());

			// Link a la ventana Mantenimiento de Aulas
			mantAulasLink = new RouterLink("Mantenimiento de Aulas", MantAulasView.class);
			mantAulasLink.setHighlightCondition(HighlightConditions.sameLocation());

			addToDrawer(new VerticalLayout(historicoReservasLink, mantPropietariosLink, mantAulasLink ,consultaAulasLink));
		} catch (Exception e) {
			throw e;
		}
	}

}
