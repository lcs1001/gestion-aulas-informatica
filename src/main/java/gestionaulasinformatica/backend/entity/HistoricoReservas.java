package gestionaulasinformatica.backend.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import gestionaulasinformatica.backend.data.TipoOperacionHR;

/**
 * Entidad que identifica a la tabla HistoricoReserva de la base de datos.
 * 
 * @author Lisa
 *
 */
@Entity
@Table(name = "historico_reservas", schema = "public")
public class HistoricoReservas implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_operacion")
	private Integer idOperacion;

	@NotNull
	@Column(name = "fecha_operacion")
	private LocalDateTime fechaHoraOperacion;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_operacion")
	private TipoOperacionHR tipoOperacion;

	@NotNull
	@Column(name = "fecha_reserva")
	private LocalDate fechaReserva;

	@NotNull
	@Column(name = "hora_inicio_reserva")
	private LocalTime horaInicioReserva;

	@NotNull
	@Column(name = "hora_fin_reserva")
	private LocalTime horaFinReserva;

	@NotNull
	@Column(name = "lugar_reserva")
	private String lugarReserva;

	@NotNull
	@Column(name = "a_cargo_de_reserva")
	private String aCargoDeReserva;

	/* ID del propietario de aula (responsable logeado en la app) */
	@NotNull
	@Column(name = "responsable_operacion")
	private String responsableOperacion;

	/**
	 * Constructor de la clase sin parámetros.
	 */
	public HistoricoReservas() {
	}

	/**
	 * Constructor de la clase con parámetros.
	 * 
	 * @param fechaOperacion       Fecha y hora de la operación
	 * @param tipoOperacion        Tipo de operación realizada (creación,
	 *                             modificación, eliminación)
	 * @param fechaReserva         Fecha de la reserva a la que hace referencia la
	 *                             operación
	 * @param horaInicioReserva    Hora de inicio de la reserva a la que hace
	 *                             referencia la operación
	 * @param horaFinReserva       Hora de fin de la reserva a la que hace
	 *                             referencia la operación
	 * @param lugarReserva         Lugar de la reserva a la que hace referencia la
	 *                             operación
	 * @param aCargoDeReserva      Persona a cargo de la reserva a la que hace
	 *                             referencia la operación
	 * @param responsableOperacion Responsable (ID propietario aula) de realizar la
	 *                             operación
	 */
	public HistoricoReservas(LocalDateTime fechaOperacion, TipoOperacionHR tipoOperacion, LocalDate fechaReserva,
			LocalTime horaInicioReserva, LocalTime horaFinReserva, String lugarReserva, String aCargoDeReserva,
			String responsableOperacion) {
		this.fechaHoraOperacion = fechaOperacion;
		this.tipoOperacion = tipoOperacion;
		this.fechaReserva = fechaReserva;
		this.horaInicioReserva = horaInicioReserva;
		this.horaFinReserva = horaFinReserva;
		this.lugarReserva = lugarReserva;
		this.aCargoDeReserva = aCargoDeReserva;
		this.responsableOperacion = responsableOperacion;
	}

	/**
	 * Función que devuelve el id de la operación del histórico de reservas.
	 * 
	 * @return ID de la operación del histórico de reservas
	 */
	public Integer getIdOperacionHR() {
		return this.idOperacion;
	}

	/**
	 * Función que devuelve la fecha y hora en que se ha realizado la operación.
	 * 
	 * @return Fecha y hora en que se ha realizado la operación
	 */
	public LocalDateTime getFechaHoraOperacion() {
		return this.fechaHoraOperacion;
	}

	/**
	 * Función que devuelve la fecha en que se ha realizado la operación.
	 * 
	 * @return Fecha en que se ha realizado la operación
	 */
	public LocalDate getFechaOperacion() {
		return this.fechaHoraOperacion.toLocalDate();
	}

	/**
	 * Función que establece la fecha y hora en que se ha realizado la operación.
	 * 
	 * @param fecha Fecha y hora en que se ha realizado la operación
	 */
	public void setFechaHoraOperacion(LocalDateTime fechaHora) {
		this.fechaHoraOperacion = fechaHora;
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
	 * Función que devuelve la fecha de la reserva a la que hace referencia la
	 * operación.
	 * 
	 * @return Fecha de la reserva a la que hace referencia la operación
	 */
	public LocalDate getFechaReserva() {
		return this.fechaReserva;
	}

	/**
	 * Función que establece la fecha de la reserva a la que hace referencia la
	 * operación.
	 * 
	 * @param fecha Fecha de la reserva a la que hace referencia la operación
	 */
	public void setFechaReserva(LocalDate fecha) {
		this.fechaReserva = fecha;
	}

	/**
	 * Función que devuelve la hora de inicio de la reserva a la que hace referencia
	 * la operación.
	 * 
	 * @return Hora de inicio de la reserva a la que hace referencia la operación
	 */
	public LocalTime getHoraInicioReserva() {
		return this.horaInicioReserva;
	}

	/**
	 * Función que establece la hora de inicio de la reserva a la que hace
	 * referencia la operación.
	 * 
	 * @param horaInicio Hora de inicio de la reserva a la que hace referencia la
	 *                   operación
	 */
	public void setHoraInicioReserva(LocalTime horaInicio) {
		this.horaInicioReserva = horaInicio;
	}

	/**
	 * Función que devuelve la hora de fin de la reserva a la que hace referencia la
	 * operación.
	 * 
	 * @return Hora de fin de la reserva a la que hace referencia la operación
	 */
	public LocalTime getHoraFinReserva() {
		return this.horaFinReserva;
	}

	/**
	 * Función que establece la hora de fin de la reserva a la que hace referencia
	 * la operación.
	 * 
	 * @param horaFin Hora de fin de la reserva a la que hace referencia la
	 *                operación
	 */
	public void setHoraFinReserva(LocalTime horaFin) {
		this.horaFinReserva = horaFin;
	}

	/**
	 * Función que devuelve el lugar de la reserva a la que hace referencia la
	 * operación.
	 * 
	 * @return Lugar de la reserva a la que hace referencia la operación
	 */
	public String getLugarReserva() {
		return this.lugarReserva;
	}

	/**
	 * Función que establece el lugar de la reserva a la que hace referencia la
	 * operación.
	 * 
	 * @param lugar Lugar de la reserva a la que hace referencia la operación
	 */
	public void setLugarReserva(String lugar) {
		this.lugarReserva = lugar;
	}

	/**
	 * Función que devuelve la persona a cargo de la reserva a la que hace
	 * referencia la operación.
	 * 
	 * @return Persona a cargo de la reserva a la que hace referencia la operación
	 */
	public String getACargoDeReserva() {
		return this.aCargoDeReserva;
	}

	/**
	 * Función que establece la persona a cargo de la reserva a la que hace
	 * referencia la operación.
	 * 
	 * @param aCargoDe Persona a cargo de la reserva a la que hace referencia la
	 *                 operación
	 */
	public void setACargoDeReserva(String aCargoDe) {
		this.aCargoDeReserva = aCargoDe;
	}

	/**
	 * Función que devuelve el responsable de realizar la operación.
	 * 
	 * @return Responsable de realizar la operación
	 */
	public String getResponsableOperacion() {
		return this.responsableOperacion;
	}

	/**
	 * Función que establece el responsable de realizar la operación.
	 * 
	 * @param responsable Responsable de realizar la operación
	 */
	public void setResponsableOperacion(String responsable) {
		this.responsableOperacion = responsable;
	}

	public boolean isPersisted() {
		return idOperacion != null;
	}

	@Override
	public int hashCode() {
		if (getIdOperacionHR() != null) {
			return getIdOperacionHR().hashCode();
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
		HistoricoReservas other = (HistoricoReservas) obj;
		if (getIdOperacionHR() == null || other.getIdOperacionHR() == null) {
			return false;
		}
		return getIdOperacionHR().equals(other.getIdOperacionHR());
	}

	@Override
	public String toString() {
		return "Operación histórico reserva [Fecha operación - " + this.getFechaOperacion() + ", Tipo operación - "
				+ this.getTipoOperacion() + ", Fecha reserva - " + this.getFechaReserva() + ", Hora reserva"
				+ this.getHoraInicioReserva() + "-" + this.getHoraFinReserva() + ", Lugar reserva - "
				+ this.getLugarReserva() + ", A cargo de reserva- " + this.getACargoDeReserva()
				+ ", Responsable operación -" + this.getResponsableOperacion() + "]";

	}
}
