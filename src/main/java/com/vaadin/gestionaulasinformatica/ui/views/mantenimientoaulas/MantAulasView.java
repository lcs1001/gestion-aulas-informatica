package com.vaadin.gestionaulasinformatica.ui.views.mantenimientoaulas;

//Imports Vaadin
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

// Imports backend 
import com.vaadin.gestionaulasinformatica.backend.entity.Aula;
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.service.AulaService;
import com.vaadin.gestionaulasinformatica.backend.service.PropietarioAulaService;

// Imports UI
import com.vaadin.gestionaulasinformatica.ui.MainLayout;

/**
 * Ventana Mantenimiento Aulas (CRUD de la entidad Aula).
 */
@Route(value = "MantAula", layout = MainLayout.class)
@PageTitle("Mantenimiento de Aulas")
public class MantAulasView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private AulaService aulaService;
	private PropietarioAulaService propietarioAulaService;

	private MantAulasForm formulario;
	private Grid<Aula> gridAulas;
	private ComboBox<PropietarioAula> filtroPropietarioAula;
	private HorizontalLayout toolbar;

	public MantAulasView(AulaService aulaService, PropietarioAulaService propietarioAulaService) {
		try {
			this.aulaService = aulaService;
			this.propietarioAulaService = propietarioAulaService;

			addClassName("mant-aulas-view");
			setSizeFull();

			configurarGridAulas();

			formulario = new MantAulasForm(this.propietarioAulaService.findAll(),
					this.propietarioAulaService.findAllCentros());
			formulario.addListener(MantAulasForm.SaveEvent.class, this::guardarAula);
			formulario.addListener(MantAulasForm.DeleteEvent.class, this::eliminarAula);
			formulario.addListener(MantAulasForm.CloseEvent.class, e -> cerrarEditor());

			add(getToolbar(), formulario, gridAulas);

			// Sólo se muestra el grid cuando se selecciona un centro o departamento
			ocultarGrid();
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
			gridAulas.setSizeFull();

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
	private HorizontalLayout getToolbar() {
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
			toolbar.addClassName("mant-aulas-toolbar");

			return toolbar;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que oculta el grid.
	 */
	private void ocultarGrid() {
		try {
			gridAulas.setVisible(false);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función para cerrar el formulario para añadir, modificar o eliminar aulas.
	 */
	private void cerrarEditor() {
		try {
			formulario.setAula(null); // Se limpian los valores antiguos
			formulario.setVisible(false);

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
				formulario.setAula(aula);
				formulario.setVisible(true);

				// No se puede editar el centro en el que se encuentra el aula
				if (editar) {
					formulario.ubicacionCentro.setVisible(false);
				} else {
					formulario.ubicacionCentro.setVisible(true);
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
			throw e;
		}
	}

	/**
	 * Función que elimina el aula de la base de datos.
	 * 
	 * @param e Evento de eliminación
	 */
	private void eliminarAula(MantAulasForm.DeleteEvent evt) {
		try {
			aulaService.delete(evt.getAula());
			actualizarAulas();
			cerrarEditor();

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Función que actualiza el grid que muestra las aulas del centro o departamento
	 * seleccionado.
	 */
	private void actualizarAulas() {
		PropietarioAula propietario;

		try {
			propietario = filtroPropietarioAula.getValue();
			if (propietario != null) {
				gridAulas.setVisible(true);
				gridAulas.setItems(aulaService.findAll(propietario));
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
