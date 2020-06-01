package com.vaadin.gestionaulasinformatica.backend.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Entidad que identifica a la tabla Aula de la base de datos.
 * 
 * @author Lisa
 *
 */
@Entity
@Table(name = "Aula")
@NamedQuery(name = "Aula.findAll", query = "SELECT a FROM Aula a")
public class Aula implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AulaPK idAula;

	/**
	 * Centro en el que se encuentra el aula (nombre corto del centro -
	 * idPropietarioAula).
	 * 
	 * Asociación bidireccional ManyToOne con PropietarioAula.
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "idPropietarioAula", insertable = false, updatable = false)
	private PropietarioAula ubicacionCentro;

	/**
	 * Centro/Departamento propietario del aula (nombre corto del centro -
	 * idPropietarioAula).
	 * 
	 * Asociación bidireccional ManyToOne con PropietarioAula.
	 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "idPropietarioAula", insertable = false, updatable = false)
	private PropietarioAula propietarioAula;

	/**
	 * Asociación bidireccional ManyToOne con Reserva para indicar el aula reservada
	 * 
	 * Fetch LAZY: se traen los items asociados bajo petición.
	 * 
	 * Cascade ALL: se realizan todas las operaciones (DETACH, MERGE, PERSIST,
	 * REFRESH, REMOVE)
	 */
	@OneToMany(mappedBy = "aula", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Reserva> listaReservas;

	/**
	 * Constructor vacío de la clase.
	 */
	public Aula() {
	}

	/**
	 * Función que devuelve el id del aula (nombre aula + ubicación centro).
	 * 
	 * @return ID del aula
	 */
	public AulaPK getIdAula() {
		return this.idAula;
	}

	/**
	 * Función que establece el id del aula.
	 * 
	 * @param nombre Nombre del aula
	 * @param centro Centro en el que se encuentar el aula
	 */
	public void setIdAula(String nombre, PropietarioAula centro) {
		this.idAula.setNombreAula(nombre);
		this.idAula.setCentro(centro);
	}

	/**
	 * Función que devuelve el centro/departamento propietario del aula.
	 * 
	 * @return Centro/departamento propietario del aula
	 */
	public PropietarioAula getPropietarioAula() {
		return this.propietarioAula;
	}

	/**
	 * Función que establece el centro/departamento propietario del aula.
	 * 
	 * @param propietarioAula Centro/departamento propietario del aula
	 */
	public void setPropietarioAula(PropietarioAula propietarioAula) {
		this.propietarioAula = propietarioAula;
	}

	/**
	 * Función que devuelve una lista de las reservas del aula.
	 * 
	 * @return Lista de reservas del aula
	 */
	public Set<Reserva> getReservasAula() {
		return this.listaReservas;
	}

	/**
	 * Función que establece la lista de reservas del aula.
	 * 
	 * @param reservas Lista de reservas del aula
	 */
	public void setReservasAula(Set<Reserva> reservas) {
		this.listaReservas = reservas;
	}

	/**
	 * Función que añade la reserva pasada a la lista de reservas del aula.
	 * 
	 * @param reserva Reserva que añadir a la lista de reservas del aula
	 * 
	 * @return Reserva añadida
	 */
	public Reserva addReservaAula(Reserva reserva) {
		this.getReservasAula().add(reserva);

		return reserva;
	}

	/**
	 * Función que elimina la reserva pasada de la lista de reservas del aula.
	 * 
	 * @param reserva Reserva que eliminar de la lista de reservas del aula
	 * 
	 * @return Reserva eliminada
	 */
	public Reserva removeReservaAula(Reserva reserva) {
		this.getReservasAula().remove(reserva);

		return reserva;
	}

	public boolean isPersisted() {
		return idAula != null;
	}

	@Override
	public int hashCode() {
		if (getIdAula() != null) {
			return getIdAula().hashCode();
		}
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Aula other = (Aula) obj;
		if (getIdAula() == null || other.getIdAula() == null) {
			return false;
		}
		return getIdAula().equals(other.getIdAula());
	}

	@Override
	public String toString() {
		return "Aula [Nombre - " + this.getIdAula() + ", Propietario - " + this.propietarioAula + "]";
	}

}