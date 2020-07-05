package gestionaulasinformatica.backend.entity;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import gestionaulasinformatica.backend.data.TipoPropietarioAula;

/**
 * Clase concreta que define la entidad de un departamento (PropietarioAula).
 * 
 * @author Lisa
 *
 */
@Entity
@DiscriminatorValue(value = "Departamento")
public class Departamento extends PropietarioAula implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor de la clase sin parámetros, indicando el tipo de propietario al
	 * constructor del padre.
	 */
	public Departamento() {
		super(TipoPropietarioAula.Departamento);
	}

	/**
	 * Constructor de la clase con parámetros.
	 * 
	 * @param id          ID del departamento
	 * @param nombre      Nombre del departamento
	 * @param responsable Responsable del departamento
	 */
	public Departamento(String id, String nombre, Usuario responsable) {
		super(id, nombre, responsable, TipoPropietarioAula.Departamento);
	}

	@Override
	public String toString() {
		return "Departamento [ID - " + this.getIdPropietarioAula() + ", Nombre - " + this.getNombrePropietarioAula()
				+ "]";
	}

}
