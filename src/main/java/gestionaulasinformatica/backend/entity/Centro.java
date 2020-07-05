package gestionaulasinformatica.backend.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import gestionaulasinformatica.backend.data.TipoPropietarioAula;

/**
 * Clase concreta que define la entidad de un centro (PropietarioAula).
 * 
 * Un centro tiene una serie de aulas ubicadas físicamente en él
 * (listaAulasUbicacionCentro).
 * 
 * @author Lisa
 *
 */
@Entity
@DiscriminatorValue(value = "Centro")
public class Centro extends PropietarioAula implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Asociación bidireccional OneToMany con Aula para indicar las aulas ubicadas
	 * en el centro.
	 */
	@OneToMany(mappedBy = "ubicacionCentro", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Aula> listaAulasUbicacionCentro;

	/**
	 * Constructor de la clase sin parámetros, indicando el tipo de propietario al
	 * constructor del padre.
	 */
	public Centro() {
		super(TipoPropietarioAula.Centro);
	}

	/**
	 * Constructor de la clase con parámetros.
	 * 
	 * @param id          ID del centro
	 * @param nombre      Nombre del centro
	 * @param responsable Responsable del centro
	 */
	public Centro(String id, String nombre, Usuario responsable) {
		super(id, nombre, responsable, TipoPropietarioAula.Centro);
	}

	/**
	 * Función que devuelve si el centro tiene aulas ubicadas en él.
	 * 
	 * @return Si tiene aulas ubicadas en él
	 */
	public Boolean tieneAulasUbicacionCentro() {
		return getAulasUbicacionCentro().isEmpty() ? false : true;
	}

	/**
	 * Función que devuelve una lista de las aulas ubicadas en el centro.
	 * 
	 * @return Lista de aulas ubicadas en el centro
	 */
	public Set<Aula> getAulasUbicacionCentro() {
		return this.listaAulasUbicacionCentro;
	}

	/**
	 * Función que establece la lista de aulas ubicadas en el centro.
	 * 
	 * @param aulasPertenecientes Lista de aulas ubicadas en el centro
	 */
	public void setAulasUbicacionCentro(Set<Aula> aulasUbicacionCentro) {
		this.listaAulasUbicacionCentro = aulasUbicacionCentro;
	}

	/**
	 * Función que añade el aula pasada a la lista de aulas ubicadas en el centro
	 * 
	 * @param aula Aula que añadir a la lista de aulas ubicadas en el centro
	 * 
	 * @return Aula añadida
	 */
	public Aula addAulaUbicacionCentro(Aula aula) {
		this.getAulasUbicacionCentro().add(aula);

		return aula;
	}

	@Override
	public String toString() {
		return "Centro [ID - " + this.getIdPropietarioAula() + ", Nombre - " + this.getNombrePropietarioAula() + "]";

	}
}
