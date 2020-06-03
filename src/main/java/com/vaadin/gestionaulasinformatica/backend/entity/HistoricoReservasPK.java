package com.vaadin.gestionaulasinformatica.backend.entity;

import java.io.Serializable;

import javax.persistence.*;

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

	@Column(insertable = false, updatable = false)
	private Integer idReserva;

	@Enumerated(EnumType.STRING)
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
	public HistoricoReservasPK(Integer idReserva, TipoOperacionHR tipoOperacion) {
		this.idReserva = idReserva;
		this.tipoOperacion = tipoOperacion;
	}

	/**
	 * Función que devuelve el id de la reserva sobre la que se ha realizado la operación.
	 * 
	 * @return Id de la reserva sobre la que se ha realizado la operación
	 */
	public Integer getIdReserva() {
		return this.idReserva;
	}

	/**
	 * Función que establece el id de la reserva sobre la que se ha realizado la operación.
	 * 
	 * @param idReserva Id de la reserva sobre la que se ha realizado la operación
	 */
	public void setIdReserva(Integer idReserva) {
		this.idReserva = idReserva;
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
		if (idReserva == null) {
			if (other.idReserva != null)
				return false;
		} else if (!idReserva.equals(other.idReserva))
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
		hash = hash * prime + this.idReserva.hashCode();
		hash = hash * prime + this.tipoOperacion.hashCode();

		return hash;
	}

	@Override
	public String toString() {
		return "HistoricoReservasPK [Reserva - " + this.getIdReserva() + ", Tipo de operación - "
				+ this.getTipoOperacion() + "]";
	}
}