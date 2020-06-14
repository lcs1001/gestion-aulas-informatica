package com.vaadin.gestionaulasinformatica.backend.entity;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Clase para especificar la clave primaria de la tabla HistoricoReservas de la
 * base de datos.
 * 
 * @author Lisa
 *
 */
@Embeddable
public class HistoricoReservasPK implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Asociación bidireccional ManyToOne con Reserva para indicar la reserva sobre
	 * la que se ha realizado la operación.
	 * 
	 * Cascade ALL: se realizan todas las operaciones (DETACH, MERGE, PERSIST,
	 * REFRESH, REMOVE)
	 */
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_reserva", referencedColumnName = "id_reserva", insertable = false, updatable = false)
	private Reserva reserva;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_operacion")
	private TipoOperacionHR tipoOperacion;

	/**
	 * Constructor vacío de la clase.
	 */
	public HistoricoReservasPK() {
	}

	/**
	 * Constructor de la clase con parámetros.
	 * 
	 * @param reserva       Reserva sobre la que se ha realizado la operación
	 * @param tipoOperacion Tipo de operación que se ha realizado
	 */
	public HistoricoReservasPK(Reserva reserva, TipoOperacionHR tipoOperacion) {
		this.reserva = reserva;
		this.tipoOperacion = tipoOperacion;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoricoReservasPK other = (HistoricoReservasPK) obj;
		if (reserva == null) {
			if (other.reserva != null)
				return false;
		} else if (!reserva.equals(other.reserva))
			return false;
		if (tipoOperacion == null) {
			if (other.tipoOperacion != null)
				return false;
		} else if (!tipoOperacion.equals(other.tipoOperacion))
			return false;
		return true;
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.reserva.hashCode();
		hash = hash * prime + this.tipoOperacion.hashCode();

		return hash;
	}

	@Override
	public String toString() {
		return "HistoricoReservasPK [Reserva - " + this.getReserva().getIdReserva() + ", Tipo de operación - "
				+ this.getTipoOperacion() + "]";
	}
}