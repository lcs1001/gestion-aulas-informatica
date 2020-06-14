package com.vaadin.gestionaulasinformatica.backend.entity;

import java.io.Serializable;
import java.time.*;
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
@Table(name = "reserva", schema = "public")
public class Reserva implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_reserva")
	private Integer idReserva;

	@NotNull
	@Column(name = "fecha_inicio")
	private LocalDate fechaInicio;

	/** Para reservas por rango de fechas */
	@Column(name = "fecha_fin")
	private LocalDate fechaFin;

	@NotNull
	@Column(name = "hora_inicio")
	private LocalTime horaInicio;

	@NotNull
	@Column(name = "hora_fin")
	private LocalTime horaFin;

	/**
	 * Asociación bidireccional ManyToOne con aula para indicar el aula reservada.
	 * 
	 * Cascade ALL: se realizan todas las operaciones (DETACH, MERGE, PERSIST,
	 * REFRESH, REMOVE)
	 */
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_aula", referencedColumnName = "id_aula", insertable = false, updatable = false)
	private Aula aula;

	/**
	 * Para reservas por rango de fechas, indica el día de la semana que se va a
	 * reservar
	 */
	@Column(name = "dia_semana")
	private String diaSemana = "";

	/** Motivo de la reserva (examen, curso, reunión, etc) */
	@NotNull
	@Column(name = "motivo")
	private String motivo = "";

	/**
	 * Persona que ha reservado el aula (no tiene por qué ser el responsable del
	 * centro o departamento)
	 */
	@NotNull
	@Column(name = "a_cargo_de")
	private String aCargoDe = "";

	/**
	 * Asociación bidireccional ManyToOne con PropietarioAula para indicar el centro
	 * o departamento responsable de hacer la reserva.
	 * 
	 * Cascade ALL: se realizan todas las operaciones (DETACH, MERGE, PERSIST,
	 * REFRESH, REMOVE)
	 */
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "responsable", referencedColumnName = "id_propietario_aula", insertable = false, updatable = false)
	private PropietarioAula responsable;

	/** Booleano que indica si se trata de una reserva por rango de fechas */
	@NotNull
	@Column(name = "reserva_rango")
	private Boolean reservaRango = false;

	/**
	 * Asociación bidireccional OneToMany con HistoricoReservas para indicar las
	 * operaciones realizadas sobre esta reserva.
	 */
	@OneToMany(mappedBy = "idOperacionHR.reserva")
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
	public LocalDate getFechaInicio() {
		return this.fechaInicio;
	}

	/**
	 * Función que establece la fecha de inicio de la reserva.
	 * 
	 * @param fechaInicio Fecha de inicio de la reserva
	 */
	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * Función que devuelve la fecha de fin de la reserva (para reservas por rango
	 * de fechas).
	 * 
	 * @return Fecha de fin de la reserva
	 */
	public LocalDate getFechaFin() {
		return this.fechaFin;
	}

	/**
	 * Función que establece la fecha de fin de la reserva (para reservas por rango
	 * de fechas).
	 * 
	 * @param fechaFin Fecha de fin de la reserva
	 */
	public void setFechaFin(LocalDate fechaFin) {
		if (this.reservaRango) {
			this.fechaFin = fechaFin;
		}
	}

	/**
	 * Función que devuelve la hora de inicio de la reserva.
	 * 
	 * @return Hora de inicio de la reserva
	 */
	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	/**
	 * Función que establece la hora de inicio de la reserva.
	 * 
	 * @param horaInicio Hora de inicio de la reserva
	 */
	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	/**
	 * Función que devuelve la hora de fin de la reserva.
	 * 
	 * @return Hora de fin de la reserva
	 */
	public LocalTime getHoraFin() {
		return horaFin;
	}

	/**
	 * Función que establece la hora de fin de la reserva.
	 * 
	 * @param horaFin Hora de fin de la reserva
	 */
	public void setHoraFin(LocalTime horaFin) {
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
		return this.getAula().getNombreAula();
	}

	/**
	 * Función que devuelve el centro en el que se encuentra el aula de la reserva.
	 * 
	 * @return Centro en el que se encuentra el aula de la reserva
	 */
	public String getNombreCentroAula() {
		return this.getAula().getUbicacionCentro().getNombrePropietarioAula();
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
					+ ", Hora fin - " + this.getHoraFin() + ", Aula - " + this.getNombreAula() + "-"
					+ this.getNombreCentroAula() + ", Dia semana - " + this.getDiaSemana() + ", Motivo - " + this.getMotivo()
					+ ", A cargo de - " + this.getACargoDe() + " (" + this.getResponsable() + ")]";
		} else {
			return "Reserva [ID - " + this.getIdReserva() + ", Fecha inicio - " + this.getFechaInicio()
					+ ", Hora inicio - " + this.getHoraInicio() + ", Hora fin - " + this.getHoraFin() + ", Aula - "
					+ this.getNombreAula() + "-" + this.getNombreCentroAula() + ", Motivo - " + this.getMotivo()
					+ ", A cargo de - " + this.getACargoDe() + " (" + this.getResponsable() + ")]";
		}

	}
}
