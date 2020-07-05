package gestionaulasinformatica.ui.views.errors;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.templatemodel.TemplateModel;

import gestionaulasinformatica.exceptions.AccesoDenegadoException;
import gestionaulasinformatica.ui.MainLayout;

/**
 * Ventana Acceso Denegado.
 * 
 * @author Lisa
 *
 */
@Tag("acceso-denegado-view")
@JsModule("./src/views/errors/acceso-denegado-view.js")
@ParentLayout(MainLayout.class)
@PageTitle("Acceso denegado")
public class AccesoDenegadoView extends PolymerTemplate<TemplateModel>
		implements HasErrorParameter<AccesoDenegadoException> {

	private static final long serialVersionUID = 1L;

	/**
	 * Función que establece el parámetro de error.
	 */
	@Override
	public int setErrorParameter(BeforeEnterEvent beforeEnterEvent,
			ErrorParameter<AccesoDenegadoException> errorParameter) {
		return HttpServletResponse.SC_FORBIDDEN;
	}
}