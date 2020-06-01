package com.vaadin.gestionaulasinformatica.backend.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "D")
public class Departamento extends PropietarioAula implements Serializable {

	private static final long serialVersionUID = 1L;

	public Departamento(String id, String nombre, char centro, String nombreResponsable, String apellidosResponsable,
			String correoResponsable, String telefonoResponsable) {
		super(id, nombre, nombreResponsable, apellidosResponsable, correoResponsable, telefonoResponsable);
	}

	@Override
	public String toString() {
		return "Departamento [ID - " + this.getIdPropietarioAula() + ", Nombre - " + this.getNombrePropietarioAula()
				+ ", Aulas bajo propiedad " + "[" + this.getAulasPropiedad() + "]";
	}

}
