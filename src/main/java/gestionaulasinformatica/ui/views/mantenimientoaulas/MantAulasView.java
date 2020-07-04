package gestionaulasinformatica.ui.views.mantenimientoaulas;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import gestionaulasinformatica.backend.entity.Aula;
import gestionaulasinformatica.backend.entity.PropietarioAula;
import gestionaulasinformatica.backend.service.AulaService;
import gestionaulasinformatica.backend.service.PropietarioAulaService;
import gestionaulasinformatica.ui.Comunes;
import gestionaulasinformatica.ui.MainLayout;
import gestionaulasinformatica.ui.Mensajes;

/**
 * Ventana Mantenimiento Aulas (CRUD de la entidad Aula).
 */
@Route(value = "mantenimientoAulas", layout = MainLayout.class)
@PageTitle("Mantenimiento de Aulas")
public class MantAulasView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private AulaService aulaService;
	private PropietarioAulaService propietarioAulaService;
	private Comunes comunes;

	private MantAulasForm formulario;
	private Grid<Aula> gridAulas;
	private ComboBox<PropietarioAula> filtroPropietarioAula;
	private HorizontalLayout toolbar;

	public MantAulasView(AulaService aulaService, PropietarioAulaService propietarioAulaService) {
		Div contenido;

		try {
			this.aulaService = aulaService;
			this.propietarioAulaService = propietarioAulaService;
			this.comunes = new Comunes();

			addClassName("mant-aulas-view");
			setSizeFull();

			configuarToolbar();
			configurarGridAulas();

			formulario = new MantAulasForm(this.propietarioAulaService.findAll(),
					this.propietarioAulaService.findAllCentros());
			formulario.addListener(MantAulasForm.SaveEvent.class, this::guardarAula);
			formulario.addListener(MantAulasForm.DeleteEvent.class, this::confirmarEliminacionAula);
			formulario.addListener(MantAulasForm.CloseEvent.class, e -> cerrarEditor());

			contenido = new Div(comunes.getTituloVentana("Mantenimiento de aulas"), toolbar, formulario,
					gridAulas);
			contenido.addClassName("mant-aulas-contenido");
			contenido.setSizeFull();

			add(contenido);

			// Sólo se muestra el grid cuando se selecciona un centro o departamento
			gridAulas.setVisible(false);
			cerrarEditor();

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura el grid que muestra las aulas.
	 */
	private void configurarGridAulas() {
		try {
			gridAulas = new Grid<>();
			gridAulas.addClassName("mant-aulas-grid");

			gridAulas.addColumn(Aula::getNombreAula).setHeader("Aula").setKey("nombreAula");
			gridAulas.addColumn(Aula::getNombreCentro).setHeader("Centro").setKey("centro");
			gridAulas.addColumn(Aula::getCapacidadInt).setHeader("Capacidad").setKey("capacidad");
			gridAulas.addColumn(Aula::getNumOrdenadoresInt).setHeader("Número de ordenadores").setKey("numOrdenadores");

			gridAulas.getColumns().forEach(columna -> columna.setAutoWidth(true));

			gridAulas.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
					GridVariant.LUMO_ROW_STRIPES);

			gridAulas.asSingleSelect().addValueChangeListener(e -> abrirEditor(e.getValue(), true));

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que configura el toolbar que contiene: un filtro de texto que permite
	 * filtrar las aulas por su nombre y un botón para añadir aulas.
	 * 
	 * @return Toolbar
	 */
	private HorizontalLayout configuarToolbar() {
		Button btnAnadir;

		try {
			filtroPropietarioAula = new ComboBox<>("Centro/Departamento");
			filtroPropietarioAula.setPlaceholder("Seleccione");
			filtroPropietarioAula.setItems(propietarioAulaService.findAll());
			filtroPropietarioAula.setItemLabelGenerator(PropietarioAula::getNombrePropietarioAula);
			filtroPropietarioAula.addValueChangeListener(e -> actualizarAulas());
			filtroPropietarioAula.addClassName("mant-aulas-filtro-responsable");

			btnAnadir = new Button("Añadir aula", click -> anadirAula());
			btnAnadir.setIcon(new Icon(VaadinIcon.PLUS));
			btnAnadir.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			btnAnadir.addClassName("mant-aulas-btn-anadir");

			toolbar = new HorizontalLayout(filtroPropietarioAula, btnAnadir);
			toolbar.addClassName("toolbar");

			return toolbar;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que cierra el formulario para añadir, modificar o eliminar aulas.
	 */
	private void cerrarEditor() {
		try {
			formulario.setAula(null); // Se limpian los valores antiguos
			formulario.setVisible(false);
			toolbar.setVisible(true);
			gridAulas.setVisible(true);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que abre el editor para añadir o modificar el aula pasada por
	 * parámetro.
	 * 
	 * @param aula   Aula que se quiere añadir o modificar
	 * @param editar Booleano que indica si se trata de una modificación (true) o
	 *               una creación(false)
	 */
	private void abrirEditor(Aula aula, Boolean editar) {
		try {
			if (aula == null) {
				cerrarEditor();
			} else {
				toolbar.setVisible(false);
				gridAulas.setVisible(false);
				formulario.setAula(aula);
				formulario.setVisible(true);

				// No se puede editar el centro en el que se encuentra el aula
				// Se oculta el botón "Eliminar" al añadir un aula
				if (editar) {
					formulario.ubicacionCentro.setReadOnly(true);
					formulario.btnEliminar.setVisible(true);
				} else {
					formulario.ubicacionCentro.setReadOnly(false);
					formulario.btnEliminar.setVisible(false);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que permite añadir un aula.
	 */
	private void anadirAula() {
		try {
			gridAulas.asSingleSelect().clear();
			abrirEditor(new Aula(), false);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que guarda el aula en la base de datos.
	 * 
	 * @param e Evento de guardado
	 */
	private void guardarAula(MantAulasForm.SaveEvent evt) {
		try {
			aulaService.save(evt.getAula());
			actualizarAulas();
			cerrarEditor();

		} catch (Exception e) {
			comunes.mostrarNotificacion(Mensajes.MSG_ERROR_GUARDAR.getMensaje(), 3000, null);
			throw e;
		}
	}

	/**
	 * Función que elimina el aula de la base de datos.
	 * 
	 * @param aula Aula que se quiere eliminar
	 */
	private void eliminarAula(Aula aula) {
		try {
			aulaService.delete(aula);
			actualizarAulas();
			cerrarEditor();

		} catch (Exception e) {
			comunes.mostrarNotificacion(Mensajes.MSG_ERROR_ACCION.getMensaje(), 3000, null);
			throw e;
		}
	}

	/**
	 * Función que muestra un mensaje de confirmación en un cuadro de diálogo cuando
	 * se quiere eliminar un aula (botón Eliminar).
	 * 
	 * @param evt Evento de eliminación de aula
	 */
	private void confirmarEliminacionAula(MantAulasForm.DeleteEvent evt) {
		Aula aula;
		Dialog confirmacion;
		String mensajeConfirmacion;
		String mensajeEliminado;
		Button btnConfirmar;
		Button btnCancelar;

		try {
			aula = evt.getAula();

			mensajeConfirmacion = "¿Desea eliminar " + aula.getNombreAula() + " de " + aula.getNombreCentro()
					+ " definitivamente? Esta acción no se puede deshacer.";

			confirmacion = new Dialog(new Label(mensajeConfirmacion));
			confirmacion.setCloseOnEsc(false);
			confirmacion.setCloseOnOutsideClick(false);

			mensajeEliminado = "Se ha eliminado el aula " + aula.getNombreAula() + " correctamente";

			btnConfirmar = new Button("Confirmar", event -> {
				eliminarAula(aula);
				comunes.mostrarNotificacion(mensajeEliminado, 3000, null);
				confirmacion.close();
			});
			btnConfirmar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			btnConfirmar.addClassName("margin-20");

			btnCancelar = new Button("Cancelar", event -> {
				cerrarEditor();
				confirmacion.close();
			});
			btnCancelar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

			confirmacion.add(btnConfirmar, btnCancelar);

			confirmacion.open();

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que actualiza el grid que muestra las aulas del centro o departamento
	 * seleccionado.
	 */
	private void actualizarAulas() {
		List<Aula> lstAulas;

		try {
			if (!filtroPropietarioAula.isEmpty()) {
				lstAulas = aulaService.findAll(filtroPropietarioAula.getValue());

				if (!lstAulas.isEmpty()) {
					gridAulas.setVisible(true);
					gridAulas.setItems(lstAulas);
				} else {
					comunes.mostrarNotificacion(Mensajes.MSG_NO_AULAS.getMensaje(), 3000, null);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
