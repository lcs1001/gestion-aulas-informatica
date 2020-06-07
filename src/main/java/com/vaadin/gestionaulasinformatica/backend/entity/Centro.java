package com.vaadin.gestionaulasinformatica.backend.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

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
	 * Asociación bidireccional ManyToOne con Aula para indicar la ubicación del
	 * centro en que se encuentra el aula.
	 * 
	 * Fetch LAZY: se traen los items asociados bajo petición.
	 * 
	 * Cascade ALL: se realizan todas las operaciones (DETACH, MERGE, PERSIST,
	 * REFRESH, REMOVE)
	 */
	@OneToMany(mappedBy = "idAula.ubicacionCentro")
	private Set<Aula> listaAulasUbicacionCentro;

	/**
	 * Constructor de la clase sin parámetros.
	 */
	public Centro() {
		super();
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
	public Centro(String id, String nombre, String nombreResponsable, String apellidosResponsable,
			String correoResponsable, String telefonoResponsable) {
		super(id, nombre, nombreResponsable, apellidosResponsable, correoResponsable, telefonoResponsable);
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
//				+ ", Aulas ubicadas en el centro [" + this.getAulasUbicacionCentro() + "], Aulas bajo propiedad " + "["
//				+ this.getAulasPropiedad() + "]";

	}
}
