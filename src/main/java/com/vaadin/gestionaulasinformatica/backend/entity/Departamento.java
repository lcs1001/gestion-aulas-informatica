package com.vaadin.gestionaulasinformatica.backend.entity;

import java.io.Serializable;
import javax.persistence.*;

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
	 * @param id                   ID del propietario (centro/departamento)
	 * @param nombre               Nombre del propietario (centro/departamento)
	 * @param nombreResponsable    Nombre del responsable del centro/departamento
	 * @param apellidosResponsable Apellidos del responsable del centro/departamento
	 * @param correoResponsable    Correo del responsable del centro/departamento
	 * @param telefonoResponsable  Teléfono del responsable del centro/departamento
	 */
	public Departamento(String id, String nombre, char centro, String nombreResponsable, String apellidosResponsable,
			String correoResponsable, String telefonoResponsable) {
		super(id, nombre, nombreResponsable, apellidosResponsable, correoResponsable, telefonoResponsable,
				TipoPropietarioAula.Departamento);
	}

	@Override
	public String toString() {
		return "Departamento [ID - " + this.getIdPropietarioAula() + ", Nombre - " + this.getNombrePropietarioAula()
				+ "]";
	}

}
