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
public class PropietarioAula implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@NotEmpty
	@Column(name = "id_propietario_aula")
	private String idPropietarioAula = "";

	@NotNull
	@NotEmpty
	@Column(name = "nombre_propietario_aula", unique = true)
	private String nombrePropietarioAula = "";

	@NotNull
	@NotEmpty
	@Column(name = "nombre_responsable")
	private String nombreResponsable = "";

	@NotNull
	@NotEmpty
	@Column(name = "apellidos_responsable")
	private String apellidosResponsable = "";

	@NotNull
	@NotEmpty
	@Email
	@Column(name = "correo_responsable")
	private String correoResponsable = "";

	@NotNull
	@NotEmpty
	@Column(name = "telefono_responsable")
	private String telefonoResponsable = "";

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "tipo", insertable = false, updatable = false)
	private TipoPropietarioAula tipoPropietarioAula;

	/**
	 * Asociación bidireccional OneToMany con Aula para indicar las aulas de las que
	 * es responsable el centro o departamento.
	 */
	@OneToMany(mappedBy = "propietarioAula")
	private Set<Aula> listaAulasPropiedad;

	/**
	 * Asociación bidireccional OnetoMany con Reserva para indicar las reservas que
	 * ha realizado el responsable del centro o departamento.
	 */
	@OneToMany(mappedBy = "responsable")
	private Set<Reserva> listaReservasPropietarioAula;

	/**
	 * Asociación bidireccional OneToMany con HistoricoReservas para indicar las
	 * operaciones sobre las reservas (añadir, modificar o eliminar) que ha
	 * realizado el responsable del centro o departamento, que se almacenan en el
	 * histórico de reservas.
	 */
	@OneToMany(mappedBy = "responsableOperacion")
	private Set<HistoricoReservas> listaOperacionesHR;

	/**
	 * Constructor de la clase sin parámetros.
	 */
	public PropietarioAula() {
	}

	/**
	 * Constructor de la clase con el tipo de propietario (centro o departamento).
	 */
	public PropietarioAula(TipoPropietarioAula tipo) {
		this.tipoPropietarioAula = tipo;
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
			String correoResponsable, String telefonoResponsable, TipoPropietarioAula tipo) {
		this.idPropietarioAula = id;
		this.nombrePropietarioAula = nombre;
		this.nombreResponsable = nombreResponsable;
		this.apellidosResponsable = apellidosResponsable;
		this.correoResponsable = correoResponsable;
		this.telefonoResponsable = telefonoResponsable;
		this.tipoPropietarioAula = tipo;
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
	 * Función que devuelve el nombre del responsable del centro/departamento;
	 * 
	 * @return Nombre del responsable del centro/departamento
	 */
	public String getNombreResponsable() {
		return this.nombreResponsable;
	}

	/**
	 * Función que devuelve los apellidos del responsable del centro/departamento.
	 * 
	 * @return Apellidos del responsable del centro/departamento
	 */
	public String getApellidosResponsable() {
		return this.apellidosResponsable;
	}

	/**
	 * Función que establece el nombre del responsable del centro/departamento.
	 * 
	 * @param nombre Nombre del responsable del centro/departamento
	 */
	public void setNombreResponsable(String nombre) {
		this.nombreResponsable = nombre;
	}

	/**
	 * Función que establece los apellidos del responsable del centro/departamento.
	 * 
	 * @param apellidos Apellidos del responsable del centro/departamento
	 */
	public void setApellidosResponsable(String apellidos) {
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
	public TipoPropietarioAula getTipoPropietarioAula() {
		return this.tipoPropietarioAula;
	}

	/**
	 * Función que establece el tipo de propietario (centro o departamento).
	 * 
	 * @param tipo Tipo de propietario (centro o departamento)
	 */
	public void setTipoPropietarioAula(TipoPropietarioAula tipo) {
		this.tipoPropietarioAula = tipo;
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
