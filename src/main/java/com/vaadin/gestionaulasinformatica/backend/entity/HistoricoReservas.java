package com.vaadin.gestionaulasinformatica.backend.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entidad que identifica a la tabla HistoricoReserva de la base de datos.
 * 
 * @author Lisa
 *
 */
@Entity
@Table(name = "HistoricoReservas")
@NamedQuery(name = "HistoricoReservas.findAll", query = "SELECT hr FROM HistoricoReservas hr")
public class HistoricoReservas implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "historicoReserva_sequence", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historicoReserva_sequence")
	private Integer idOperacionHR;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "idReserva", updatable = false, nullable = false)
	private Reserva reserva;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date fechaOperacion;

	@NotNull
	private TipoOperacionHR tipoOperacion;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "idPropietarioAula", updatable = false, nullable = false)
	private PropietarioAula responsableOperacion;

	/**
	 * Función que devuelve el id de la operación del histórico de reservas.
	 * 
	 * @return ID de la operación del histórico de reservas
	 */
	public Integer getIdOperacionHR() {
		return this.idOperacionHR;
	}

	/**
	 * Función que devuelve la reserva sobre la que se ha realizado la operación.
	 * 
	 * @return Reserva sobre la que se ha realizado la operación
	 */
	public Reserva getReserva() {
		return this.reserva;
	}

	/**
	 * Función que establece la reserva sobre la que se ha realizado la operación.
	 * 
	 * @param reserva Reserva sobre la que se ha realizado la operación
	 */
	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	/**
	 * Función que devuelve la fecha en que se ha realizado la operación.
	 * 
	 * @return Fecha en que se ha realizado la operación
	 */
	public Date getFechaOperacion() {
		return this.fechaOperacion;
	}

	/**
	 * Función que establece la fecha en que se ha realizado la operación.
	 * 
	 * @param fecha Fecha en que se ha realizado la operación
	 */
	public void setFechaOperacion(Date fecha) {
		this.fechaOperacion = fecha;
	}

	/**
	 * Función que devuelve el tipo de operación que se ha realizado.
	 * 
	 * @return Tipo de operación que se ha realizado
	 */
	public TipoOperacionHR getTipoOperacion() {
		return this.tipoOperacion;
	}

	/**
	 * Función que establece el tipo de operación que se ha realizado.
	 * 
	 * @param tipoOperacion Tipo de operación que se ha realizado
	 */
	public void setTipoOperacion(TipoOperacionHR tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	/**
	 * Función que devuelve el responsable de la operación que se ha realizado.
	 * 
	 * @return Responsable de la operación que se ha realizado
	 */
	public PropietarioAula getResponsableOperacion() {
		return this.responsableOperacion;
	}

	/**
	 * Función que establece el responsable de la operación que se ha realizado.
	 * 
	 * @param responsableOperacion Responsable de la operación que se ha realizado
	 */
	public void setResponsableOperacion(PropietarioAula responsableOperacion) {
		this.responsableOperacion = responsableOperacion;

	}
}
