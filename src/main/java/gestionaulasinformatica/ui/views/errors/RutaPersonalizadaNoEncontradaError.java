package gestionaulasinformatica.ui.views.errors;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RouteNotFoundError;
import com.vaadin.flow.router.RouterLink;

import gestionaulasinformatica.ui.MainLayout;

@ParentLayout(MainLayout.class)
@PageTitle("Página no encontrada")
public class RutaPersonalizadaNoEncontradaError extends RouteNotFoundError {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor de la clase.
	 */
	public RutaPersonalizadaNoEncontradaError() {
		RouterLink link = Component.from(ElementFactory.createRouterLink("", "Ir a la página de inicio."),
				RouterLink.class);
		getElement().appendChild(new Text("Oops you hit a 404. ").getElement(), link.getElement());
	}

	/**
	 * Función que establece el parámetro de error.
	 */
	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
		return HttpServletResponse.SC_NOT_FOUND;
	}
}
