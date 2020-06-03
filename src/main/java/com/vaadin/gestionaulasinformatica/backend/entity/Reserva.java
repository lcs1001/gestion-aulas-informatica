package com.vaadin.gestionaulasinformatica.backend.entity;

import java.io.Serializable;
import java.sql.*;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entidad que identifica a la tabla Reserva de la base de datos.
 * 
 * @author Lisa
 *
 */
@Entity
@Table(name = "Reserva")
@NamedQuery(name = "Reserva.findAll", query = "SELECT r FROM Reserva r")
public class Reserva implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "reserva_sequence", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reserva_sequence")
	private Integer idReserva;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date fechaInicio;

	/** Para reservas por rango de fechas */
	@Temporal(TemporalType.DATE)
	private Date fechaFin;

	@NotNull
	private Time horaInicio;

	@NotNull
	private Time horaFin;

	@NotNull
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "nombre_aula", insertable = false, updatable = false),
			@JoinColumn(name = "ubicacion_centro", insertable = false, updatable = false) })
	private Aula aula;

	/**
	 * Para reservas por rango de fechas, indica el día de la semana que se va a
	 * reservar
	 */
	private String diaSemana = "";

	/** Motivo de la reserva (examen, curso, reunión, etc) */
	@NotNull
	private String motivo = "";

	/**
	 * Persona que ha reservado el aula (no tiene por qué ser el responsable del
	 * centro o departamento)
	 */
	@NotNull
	private String aCargoDe = "";

	/** Centro o departamento responsable de hacer la reserva */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_propietario_aula", insertable = false, updatable = false)
	private PropietarioAula responsable;

	/** Booleano que indica si se trata de una reserva por rango de fechas */
	@NotNull
	private Boolean reservaRango = false;

	/**
	 * Asociación bidireccional ManyToOne con HistoricoReservas para almacenar las
	 * operaciones realizadas sobre esta reserva.
	 * 
	 * Fetch LAZY: se traen los items asociados bajo petición.
	 * 
	 * Cascade ALL: se realizan todas las operaciones (DETACH, MERGE, PERSIST,
	 * REFRESH, REMOVE)
	 */
	@OneToMany(mappedBy = "reserva")
	private Set<HistoricoReservas> listaOperacionesHR;

	/**
	 * Función que devuelve el id de la reserva.
	 * 
	 * @return ID de la reserva
	 */
	public Integer getIdReserva() {
		return this.idReserva;
	}

	/**
	 * Función que devuelve la fecha de inicio de la reserva.
	 * 
	 * @return Fecha de inicio de la reserva
	 */
	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	/**
	 * Función que establece la fecha de inicio de la reserva.
	 * 
	 * @param fechaInicio Fecha de inicio de la reserva
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * Función que devuelve la fecha de fin de la reserva (para reservas por rango
	 * de fechas).
	 * 
	 * @return Fecha de fin de la reserva
	 */
	public Date getFechaFin() {
		return fechaFin;
	}

	/**
	 * Función que establece la fecha de fin de la reserva (para reservas por rango
	 * de fechas).
	 * 
	 * @param fechaFin Fecha de fin de la reserva
	 */
	public void setFechaFin(Date fechaFin) {
		if (this.reservaRango) {
			this.fechaFin = fechaFin;
		}
	}

	/**
	 * Función que devuelve la hora de inicio de la reserva.
	 * 
	 * @return Hora de inicio de la reserva
	 */
	public Time getHoraInicio() {
		return horaInicio;
	}

	/**
	 * Función que establece la hora de inicio de la reserva.
	 * 
	 * @param horaInicio Hora de inicio de la reserva
	 */
	public void setHoraInicio(Time horaInicio) {
		this.horaInicio = horaInicio;
	}

	/**
	 * Función que devuelve la hora de fin de la reserva.
	 * 
	 * @return Hora de fin de la reserva
	 */
	public Time getHoraFin() {
		return horaFin;
	}

	/**
	 * Función que establece la hora de fin de la reserva.
	 * 
	 * @param horaFin Hora de fin de la reserva
	 */
	public void setHoraFin(Time horaFin) {
		this.horaFin = horaFin;
	}

	/**
	 * Función que devuelve el aula de la reserva.
	 * 
	 * @return Aula de la reserva
	 */
	public Aula getAula() {
		return this.aula;
	}
	
	/**
	 * Función que devuelve el nombre del aula de la reserva.
	 * 
	 * @return Aula de la reserva
	 */
	public String getNombreAula() {
		return this.aula.getIdAula().getNombreAula();
	}
	
	/**
	 * Función que devuelve el nombre del aula de la reserva.
	 * 
	 * @return Aula de la reserva
	 */
	public String getCentroAula() {
		return this.aula.getIdAula().getCentro();
	}

	/**
	 * Función que establece el aula que de la reserva.
	 * 
	 * @param aula Aula de la reserva
	 */
	public void setAula(Aula aula) {
		this.aula = aula;
	}

	/**
	 * Función que devuelve el día de la semana de la reserva (para reservas por
	 * rango de fechas).
	 * 
	 * @return Dia de la semana de la reserva
	 */
	public String getDiaSemana() {
		return this.diaSemana;
	}

	/**
	 * Función que establece el día de la semana de la reserva (para reservas por
	 * rango de fechas).
	 * 
	 * @param diaSemana Dia de la semana de la reserva
	 */
	public void setDiaSemana(String diaSemana) {
		if (this.reservaRango) {
			this.diaSemana = diaSemana;
		}
	}

	/**
	 * Función que devuelve el motivo de la reserva.
	 * 
	 * @return Motivo de la reserva
	 */
	public String getMotivo() {
		return this.motivo;
	}

	/**
	 * Función que establece el motivo de la reserva.
	 * 
	 * @param motivo Motivo de la reserva
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	/**
	 * Función que devuelve la persona que ha hecho la reserva.
	 * 
	 * @return Persona que ha hecho la reserva
	 */
	public String getACargoDe() {
		return this.aCargoDe;
	}

	/**
	 * Función que establece la persona que ha hecho la reserva.
	 * 
	 * @param aCargoDe Persona que ha hecho la reserva
	 */
	public void setACargoDe(String aCargoDe) {
		this.aCargoDe = aCargoDe;
	}

	/**
	 * Función que devuelve el centro o departamento responsable de hacer la
	 * reserva.
	 * 
	 * @return Centro o departamento responsable de hacer la reserva
	 */
	public PropietarioAula getResponsable() {
		return this.responsable;
	}

	/**
	 * Función que establece el centro o departamento responsable de hacer la
	 * reserva.
	 * 
	 * @param responsable Centro o departamento responsable de hacer la reserva
	 */
	public void setResponsable(PropietarioAula responsable) {
		this.responsable = responsable;
	}

	/**
	 * Función que devuelve un booleano indicando si se trata de una reserva por
	 * rango de fechas o no.
	 * 
	 * @return Booleano indicando si se trata de una reserva por rango de fechas o
	 *         no
	 */
	public Boolean getReservaRango() {
		return this.reservaRango;
	}

	/**
	 * Función que establece si se trata de una reserva por rango de fechas o no.
	 * 
	 * @param reservaRango Booleano indicando si se trata de una reserva por rango
	 *                     de fechas o no
	 */
	public void setReservaRango(Boolean reservaRango) {
		this.reservaRango = reservaRango;
	}

	public boolean isPersisted() {
		return idReserva != null;
	}

	@Override
	public int hashCode() {
		if (getIdReserva() != null) {
			return getIdReserva().hashCode();
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
		Reserva other = (Reserva) obj;
		if (getIdReserva() == null || other.getIdReserva() == null) {
			return false;
		}
		return getIdReserva().equals(other.getIdReserva());
	}

	@Override
	public String toString() {
		if (reservaRango) {
			return "Reserva [ID - " + this.getIdReserva() + ", Fecha inicio - " + this.getFechaInicio()
					+ ", Fecha fin - " + this.getFechaFin() + ", Hora inicio - " + this.getHoraInicio()
					+ ", Hora fin - " + this.getHoraFin() + ", Aula - " + this.getAula().getIdAula().getNombreAula()
					+ "-" + this.getAula().getIdAula().getCentro() + ", Dia semana - " + this.getDiaSemana()
					+ ", Motivo - " + this.getMotivo() + ", A cargo de - " + this.getACargoDe() + " ("
					+ this.getResponsable() + ")]";
		} else {
			return "Reserva [ID - " + this.getIdReserva() + ", Fecha inicio - " + this.getFechaInicio()
					+ ", Hora inicio - " + this.getHoraInicio() + ", Hora fin - " + this.getHoraFin() + ", Aula - "
					+ this.getAula().getIdAula().getNombreAula() + "-" + this.getAula().getIdAula().getCentro()
					+ ", Motivo - " + this.getMotivo() + ", A cargo de - " + this.getACargoDe() + " ("
					+ this.getResponsable() + ")]";
		}

	}
}
