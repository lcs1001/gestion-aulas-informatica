package com.vaadin.gestionaulasinformatica.backend.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Clase para especificar la clave primaria de la tabla Aula de la base de
 * datos.
 * 
 * @author Lisa
 *
 */
@Embeddable
public class AulaPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "nombre_aula")
	private String nombreAula = "";

	/**
	 * Centro en el que se encuentra el aula (nombre corto del centro -
	 * idPropietarioAula).
	 * 
	 * Asociación bidireccional ManyToOne con PropietarioAula.
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "ubicacion_centro", referencedColumnName = "id_propietario_aula",  insertable = false, updatable = false)
	private PropietarioAula ubicacionCentro;

	/**
	 * Constructor vacío de la clase.
	 */
	public AulaPK() {
	}

	/**
	 * Constructor de la clase con parámetros.
	 * 
	 * @param nombre Nombre del aula.
	 * @param centro Centro en el que se encuentra el aula
	 */
	public AulaPK(String nombre, PropietarioAula centro) {
		this.nombreAula = nombre;
		this.ubicacionCentro = centro;
	}

	/**
	 * Función que devuelve el nombre del aula.
	 * 
	 * @return Nombre del aula
	 */
	public String getNombreAula() {
		return this.nombreAula;
	}

	/**
	 * Función que establece el nombre del aula.
	 * 
	 * @param nombreAula Nombre del aula
	 */
	public void setNombreAula(String nombreAula) {
		this.nombreAula = nombreAula;
	}

	/**
	 * Función que devuelve el centro en el que se encuentra el aula.
	 * 
	 * @return Centro en el que se encuentra el aula
	 */
	public PropietarioAula getCentro() {
		return this.ubicacionCentro;
	}

	/**
	 * Función que establece el centro en el que se encuentra el aula.
	 * 
	 * @param centro Centro en el que se encuentra el aula
	 */
	public void setCentro(PropietarioAula centro) {
		this.ubicacionCentro = centro;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AulaPK other = (AulaPK) obj;
		if (nombreAula == null) {
			if (other.nombreAula != null)
				return false;
		} else if (!nombreAula.equals(other.nombreAula))
			return false;
		if (ubicacionCentro == null) {
			if (other.ubicacionCentro != null)
				return false;
		} else if (!ubicacionCentro.equals(other.ubicacionCentro))
			return false;
		return true;
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.nombreAula.hashCode();
		hash = hash * prime + this.ubicacionCentro.hashCode();

		return hash;
	}

	@Override
	public String toString() {
		return "AulaPK [Nombre - " + this.getNombreAula() + ", Centro - " + this.getCentro() + "]";
	}
}
