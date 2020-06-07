package com.vaadin.gestionaulasinformatica.backend.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.Check;

/**
 * Clase abstracta que define la entidad que identifica a la tabla
 * PropietarioAula de la base de datos.
 * 
 * Una única tabla para toda la jerarquía, el discriminante es el caracter
 * "tipoPropietarioAula".
 * 
 * @author Lisa
 *
 */
@Entity
@Table(name = "propietario_aula", schema = "public")
@Check(constraints = "tipo IN ('Centro','Departamento')")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class PropietarioAula implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_propietario_aula")
	private String idPropietarioAula = "";

	@NotNull
	@Column(name = "nombre_propietario_aula", unique = true)
	private String nombrePropietarioAula = "";

	@NotNull
	@Column(name = "nombre_responsable")
	private String nombreResponsable = "";

	@NotNull
	@Column(name = "apellidos_responsable")
	private String apellidosResponsable = "";

	@NotNull
	@Email
	@Column(name = "correo_responsable")
	private String correoResponsable = "";

	@NotNull
	@Column(name = "telefono_responsable")
	private String telefonoResponsable = "";

	@NotNull
	@Column(name = "tipo", insertable = false, updatable = false)
	private String tipo = "";

	/**
	 * Asociación bidireccional ManyToOne con Aula para indicar las aulas de las que
	 * dispone el centro.
	 * 
	 * Fetch LAZY: se traen los items asociados bajo petición.
	 * 
	 * Cascade ALL: se realizan todas las operaciones (DETACH, MERGE, PERSIST,
	 * REFRESH, REMOVE)
	 */
	@OneToMany(mappedBy = "propietarioAula")
	private Set<Aula> listaAulasPropiedad;

	/**
	 * Asociación bidireccional ManyToOne con Reserva para indicar el responsable de
	 * la reserva.
	 * 
	 * Fetch LAZY: se traen los items asociados bajo petición.
	 * 
	 * Cascade ALL: se realizan todas las operaciones (DETACH, MERGE, PERSIST,
	 * REFRESH, REMOVE)
	 */
	@OneToMany(mappedBy = "responsable")
	private Set<Reserva> listaReservasPropietarioAula;

	/**
	 * Asociación bidireccional ManyToOne con HistoricoReservas para indicar el
	 * responsable de la operación realizada con una reserva, que se almacena en el
	 * histórico de reservas.
	 * 
	 * Fetch LAZY: se traen los items asociados bajo petición.
	 * 
	 * Cascade ALL: se realizan todas las operaciones (DETACH, MERGE, PERSIST,
	 * REFRESH, REMOVE)
	 */
	@OneToMany(mappedBy = "responsableOperacion")
	private Set<HistoricoReservas> listaOperacionesHR;

	/**
	 * Constructor de la clase sin parámetros.
	 */
	public PropietarioAula() {
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
	public PropietarioAula(String id, String nombre, String nombreResponsable, String apellidosResponsable,
			String correoResponsable, String telefonoResponsable) {
		super();
		this.setIdPropietarioAula(id);
		this.setNombrePropietarioAula(nombre);
		this.nombreResponsable = nombreResponsable;
		this.apellidosResponsable = apellidosResponsable;
		this.correoResponsable = correoResponsable;
		this.telefonoResponsable = telefonoResponsable;
	}

	/**
	 * Función que devuelve el ID del centro/departamento.
	 * 
	 * @return ID del centro/departamento
	 */
	public String getIdPropietarioAula() {
		return this.idPropietarioAula;
	}

	/**
	 * Función que establece el ID del centro/departamento.
	 * 
	 * @param nombre Nombre del centro/departamento
	 */
	public void setIdPropietarioAula(String idPropietarioAula) {
		this.idPropietarioAula = idPropietarioAula;
	}

	/**
	 * Función que devuelve el nombre del centro/departamento.
	 * 
	 * @return Nombre del centro/departamento
	 */
	public String getNombrePropietarioAula() {
		return this.nombrePropietarioAula;
	}

	/**
	 * Función que establece el nombre del centro/departamento.
	 * 
	 * @param nombre Nombre del centro/departamento
	 */
	public void setNombrePropietarioAula(String nombre) {
		this.nombrePropietarioAula = nombre;
	}

	/**
	 * Función que devuelve el nombre y apellidos del responsable del
	 * centro/departamento.
	 * 
	 * @return Nombre y apellidos del responsable del centro/departamento
	 */
	public String getNombreApellidosResponsable() {
		return this.nombreResponsable + " " + this.apellidosResponsable;
	}

	/**
	 * Función que establece el nombre y apellidos del responsable del
	 * centro/departamento.
	 * 
	 * @param nombre    Nombre del responsable del centro/departamento
	 * @param apellidos Apellidos del responsable del centro/departamento
	 */
	public void setNombreApellidosResponsable(String nombre, String apellidos) {
		this.nombreResponsable = nombre;
		this.apellidosResponsable = apellidos;
	}

	/**
	 * Función que devuelve el correo del responsable del centro/departamento.
	 * 
	 * @return Correo del responsable del centro/departamento
	 */
	public String getCorreoResponsable() {
		return this.correoResponsable;
	}

	/**
	 * Función que establece el correo del responsable del centro/departamento.
	 * 
	 * @param correo Correo del responsable del centro/departamento
	 */
	public void setCorreoResponsable(String correo) {
		this.correoResponsable = correo;
	}

	/**
	 * Función que devuelve el teléfono del responsable del centro/departamento.
	 * 
	 * @return Teléfono del responsable del centro/departamento
	 */
	public String getTelefonoResponsable() {
		return this.telefonoResponsable;
	}

	/**
	 * Función que establece el teléfono del responsable del centro/departamento.
	 * 
	 * @param telefono Teléfono del responsable del centro/departamento
	 */
	public void setTelefonoResponsable(String telefono) {
		this.telefonoResponsable = telefono;
	}

	/**
	 * Función que devuelve el tipo de propietario (centro o departamento).
	 * 
	 * @return Tipo de propietario (centro o departamento)
	 */
	public String getTipoPropietarioAula() {
		return this.tipo;
	}

	/**
	 * Función que establece el tipo de propietario (centro o departamento).
	 * 
	 * @param tipo Tipo de propietario (centro o departamento)
	 */
	public void setTipoPropietarioAula(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * Función que devuelve una lista de las aulas de las que es propietario.
	 * 
	 * @return Lista de aulas de las que es propietario
	 */
	public Set<Aula> getAulasPropiedad() {
		return this.listaAulasPropiedad;
	}

	/**
	 * Función que establece la lista de aulas de las que es propietario.
	 * 
	 * @param aulasPertenecientes Lista de aulas de las que es propietario
	 */
	public void setAulasPropiedad(Set<Aula> aulasPropiedad) {
		this.listaAulasPropiedad = aulasPropiedad;
	}

	/**
	 * Función que añade el aula pasada a la lista de aulas de las que es
	 * propietario
	 * 
	 * @param aula Aula que añadir a la lista de aulas de las que es propietario
	 * 
	 * @return Aula añadida
	 */
	public Aula addAulaResponsable(Aula aula) {
		this.getAulasPropiedad().add(aula);

		return aula;
	}

	/**
	 * Función que devuelve una lista de reservas de las que es responsable.
	 * 
	 * @return Lista de reservas de las que es responsable
	 */
	public Set<Reserva> getReservasPropietarioAula() {
		return this.listaReservasPropietarioAula;
	}

	/**
	 * Función que establece la lista de reservas de las que es responsable.
	 * 
	 * @param reservas Lista de reservas de las que es responsable
	 */
	public void setReservasPropietarioAula(Set<Reserva> reservas) {
		this.listaReservasPropietarioAula = reservas;
	}

	/**
	 * Función que añade la reserva pasada a la lista de reservas de las que es
	 * responsable.
	 * 
	 * @param reserva Reserva que añadir a la lista de reservas de las que es
	 *                responsable
	 * 
	 * @return Reserva añadida
	 */
	public Reserva addReservaPropietarioAula(Reserva reserva) {
		this.getReservasPropietarioAula().add(reserva);

		return reserva;
	}

	/**
	 * Función que elimina la reserva pasada de la lista de reservas de las que es
	 * responsable.
	 * 
	 * @param reserva Reserva que eliminar de la lista de reservas de las que es
	 *                responsable
	 * 
	 * @return Reserva eliminada
	 */
	public Reserva removeReservaPropietarioAula(Reserva reserva) {
		getReservasPropietarioAula().remove(reserva);

		return reserva;
	}

	/**
	 * Función que devuelve una lista de operaciones que ha realizado sobre una
	 * reserva.
	 * 
	 * @return Lista de reservas de las operaciones que ha realizado sobre una
	 *         reserva
	 */
	public Set<HistoricoReservas> getOperacionesHR() {
		return this.listaOperacionesHR;
	}

	/**
	 * Función que establece la lista de operaciones que ha realizado sobre una
	 * reserva.
	 * 
	 * @param operacionesHR Lista de operaciones que ha realizado sobre una reserva
	 */
	public void setOperacionesHR(Set<HistoricoReservas> operacionesHR) {
		this.listaOperacionesHR = operacionesHR;
	}

	/**
	 * Función que añade la operación pasada a la lista de operaciones que ha
	 * realizado sobre una reserva.
	 * 
	 * @param operacionHR Operación del histórico de reservas que añadir a la lista
	 *                    de operaciones que ha realizado sobre una reserva
	 * 
	 * @return Operación del histórico de reservas añadida
	 */
	public HistoricoReservas addOperacionHR(HistoricoReservas operacionHR) {
		this.getOperacionesHR().add(operacionHR);

		return operacionHR;
	}

	public boolean isPersisted() {
		return idPropietarioAula != null;
	}

	@Override
	public int hashCode() {
		if (getIdPropietarioAula() != null) {
			return getIdPropietarioAula().hashCode();
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
		PropietarioAula other = (PropietarioAula) obj;
		if (getIdPropietarioAula() == null || other.getIdPropietarioAula() == null) {
			return false;
		}
		return getIdPropietarioAula().equals(other.getIdPropietarioAula());
	}

}
