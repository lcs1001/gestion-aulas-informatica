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
@Table(name = "aula", schema = "public")
public class Aula implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_aula")
	private Integer idAula;

	@NotNull
	@NotEmpty
	@Column(name = "nombre_aula")
	private String nombreAula = "";

	@NotNull
	@Min(value = 0)
	@Column(name = "capacidad")
	private Integer capacidad = 0;

	@NotNull
	@Min(value = 0)
	@Column(name = "num_ordenadores")
	private Integer numOrdenadores = 0;

	/**
	 * Asociación bidireccional ManyToOne con PropietarioAula para indicar el centro
	 * en el que se encuentra el aula.
	 * 
	 * Cascade ALL: se realizan todas las operaciones (DETACH, MERGE, PERSIST,
	 * REFRESH, REMOVE)
	 */
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ubicacion_centro", referencedColumnName = "id_propietario_aula", updatable = false)
	private PropietarioAula ubicacionCentro;

	/**
	 * Asociación bidireccional ManyToOne con PropietarioAula para indicar el centro
	 * o departamento propietario del aula.
	 * 
	 * Cascade ALL: se realizan todas las operaciones (DETACH, MERGE, PERSIST,
	 * REFRESH, REMOVE)
	 */
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "propietario_aula", referencedColumnName = "id_propietario_aula")
	private PropietarioAula propietarioAula;

	/**
	 * Asociación bidireccional OneToMany con Reserva para indicar las reservas
	 * hechas sobre el aula.
	 */
	@OneToMany(mappedBy = "aula")
	private Set<Reserva> lstReservas;

	/**
	 * Constructor vacío de la clase.
	 */
	public Aula() {
	}

	/**
	 * Constructor de la clase con parámetros.
	 * 
	 * @param nombre          Nombre del aula
	 * @param capacidad       Capacidad del aula
	 * @param numOrdenadores  Número de ordenadores del aula
	 * @param ubicacionCentro Centro en el que se encuentra el aula
	 * @param propietario     Centro o departamento propietario del aula
	 */
	public Aula(String nombre, Integer capacidad, Integer numOrdenadores, PropietarioAula ubicacionCentro,
			PropietarioAula propietario) {
		this.nombreAula = nombre;
		this.capacidad = capacidad;
		this.numOrdenadores = numOrdenadores;
		this.ubicacionCentro = ubicacionCentro;
		this.propietarioAula = propietario;
	}

	/**
	 * Función que devuelve el id del aula.
	 * 
	 * @return ID del aula
	 */
	public Integer getIdAula() {
		return this.idAula;
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
	 * Función que devuelve la capacidad del aula como Double para el Mantenimiento
	 * de Aulas.
	 * 
	 * @return Capacidad del aula
	 */
	public Double getCapacidad() {
		return this.capacidad.doubleValue();
	}

	/**
	 * Función que devuelve la capacidad del aula como Integer.
	 * 
	 * @return Capacidad del aula
	 */
	public Integer getCapacidadInt() {
		return this.capacidad;
	}

	/**
	 * Función que establece la capacidad del aula pasada como Double para el
	 * Mantenimiento de Aulas.
	 * 
	 * @param capacidad Capacidad del aula
	 */
	public void setCapacidad(Double capacidad) {
		this.capacidad = capacidad.intValue();
	}

	/**
	 * Función que devuelve el número de ordenadores del aula como Double para el
	 * Mantenimiento de Aulas.
	 * 
	 * @return Número de ordenadores del aula
	 */
	public Double getNumOrdenadores() {
		return this.numOrdenadores.doubleValue();
	}

	/**
	 * Función que devuelve el número de ordenadores del aula como Integer.
	 * 
	 * @return Número de ordenadores del aula
	 */
	public Integer getNumOrdenadoresInt() {
		return this.numOrdenadores;
	}

	/**
	 * Función que establece el número de ordenadores del aula pasado como Double
	 * para el Mantenimiento de Aulas.
	 * 
	 * @param numOrdenadores Número de ordenadores del aula
	 */
	public void setNumOrdenadores(Double numOrdenadores) {
		this.numOrdenadores = numOrdenadores.intValue();
	}

	/**
	 * Función que devuelve el centro en el que se encuentra el aula.
	 * 
	 * @return Centro en el que se encuentra el aula
	 */
	public PropietarioAula getUbicacionCentro() {
		return this.ubicacionCentro;
	}

	/**
	 * Función que devuelve el nombre del centro en el que se encuentra el aula.
	 * 
	 * @return Centro en el que se encuentra el aula
	 */
	public String getNombreCentro() {
		return this.ubicacionCentro.getNombrePropietarioAula();
	}

	/**
	 * Función que establece el centro en el que se encuentra el aula.
	 * 
	 * @param centro Centro en el que se encuentra el aula
	 */
	public void setUbicacionCentro(PropietarioAula centro) {
		this.ubicacionCentro = centro;
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
		return this.lstReservas;
	}

	/**
	 * Función que establece la lista de reservas del aula.
	 * 
	 * @param reservas Lista de reservas del aula
	 */
	public void setReservasAula(Set<Reserva> reservas) {
		this.lstReservas = reservas;
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
		return "Aula [Nombre - " + this.getNombreAula() + ", Centro - "
				+ this.getUbicacionCentro().getNombrePropietarioAula() + ", Propietario - "
				+ this.getPropietarioAula().getNombrePropietarioAula() + "]";
	}
}