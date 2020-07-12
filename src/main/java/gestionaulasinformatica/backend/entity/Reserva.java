package gestionaulasinformatica.backend.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_reserva")
	private Integer idReserva;

	@NotNull
	@Column(name = "fecha")
	private LocalDate fecha;

	@NotNull
	@Column(name = "hora_inicio")
	private LocalTime horaInicio;

	@NotNull
	@Column(name = "hora_fin")
	private LocalTime horaFin;

	@NotNull
	@Size(max = 10)
	@Column(name = "dia_semana")
	private String diaSemana = "";

	/**
	 * Asociación bidireccional ManyToOne con Aula para indicar el aula reservada.
	 */
	@NotNull
	@ManyToOne
	@JoinColumn(name = "id_aula", referencedColumnName = "id_aula")
	private Aula aula;

	/** Motivo de la reserva (examen, curso, reunión, etc) */
	@NotNull
	@Size(max = 50)
	@Column(name = "motivo")
	private String motivo = "";

	/**
	 * Persona que ha reservado el aula (no tiene por qué ser el responsable del
	 * centro o departamento)
	 */
	@NotNull
	@Size(max = 50)
	@Column(name = "a_cargo_de")
	private String aCargoDe = "";

	/**
	 * Nombre y apellidos del usuario logeado que ha registrado la reserva.
	 */
	@NotNull
	@Size(max = 100)
	@Column(name = "usuario_responsable")
	private String usuarioResponsable;

	/**
	 * Asociación bidireccional ManyToOne con PropietarioAula para indicar el centro
	 * o departamento responsable de hacer la reserva.
	 */
	@NotNull
	@ManyToOne
	@JoinColumn(name = "propietario_responsable", referencedColumnName = "id_propietario_aula")
	private PropietarioAula propietarioResponsable;

	/**
	 * Constructor de la clase sin parámetros.
	 */
	public Reserva() {
	}

	/**
	 * Constructor de la clase con parámetros.
	 * 
	 * @param fecha                  Fecha de la reserva
	 * @param horaInicio             Hora de inicio de la reserva
	 * @param horaFin                Hora de fin de la reserva
	 * @param diaSemana              Día de la semana de la reserva
	 * @param aula                   Aula que se reserva
	 * @param motivo                 Motivo de la reserva
	 * @param aCargoDe               Persona a cargo de la reserva
	 * @param usuarioResponsable     Usuario responsable de hacer la reserva
	 * @param propietarioResponsable Centro o departamento responsable de hacer la
	 *                               reserva
	 */
	public Reserva(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String diaSemana, Aula aula, String motivo,
			String aCargoDe, String usuarioResponsable, PropietarioAula propietarioResponsable) {
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.diaSemana = diaSemana;
		this.aula = aula;
		this.motivo = motivo;
		this.aCargoDe = aCargoDe;
		this.usuarioResponsable = usuarioResponsable;
		this.propietarioResponsable = propietarioResponsable;
	}

	/**
	 * Función que devuelve el id de la reserva.
	 * 
	 * @return ID de la reserva
	 */
	public Integer getIdReserva() {
		return this.idReserva;
	}

	/**
	 * Función que devuelve la fecha de la reserva.
	 * 
	 * @return Fecha de la reserva
	 */
	public LocalDate getFecha() {
		return this.fecha;
	}

	/**
	 * Función que establece la fecha de la reserva.
	 * 
	 * @param fechaInicio Fecha de la reserva
	 */
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
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
		this.diaSemana = diaSemana;
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
	 * Función que devuelve el usuario responsable de hacer la reserva.
	 * 
	 * @return Usuario responsable de hacer la reserva
	 */
	public String getUsuarioResponsable() {
		return this.usuarioResponsable;
	}

	/**
	 * Función que establece el usuario responsable de hacer la reserva.
	 * 
	 * @param usuario Usuario responsable de hacer la reserva
	 */
	public void setUsuarioResponsable(String usuario) {
		this.usuarioResponsable = usuario;
	}

	/**
	 * Función que devuelve el centro o departamento responsable de hacer la
	 * reserva.
	 * 
	 * @return Centro o departamento responsable de hacer la reserva
	 */
	public PropietarioAula getPropietarioResponsable() {
		return this.propietarioResponsable;
	}

	/**
	 * Función que devuelve el nombre del centro o departamento responsable de hacer
	 * la reserva.
	 * 
	 * @return Nombre del centro o departamento responsable de hacer la reserva
	 */
	public String getNombrePropietarioResponsable() {
		return this.propietarioResponsable.getNombrePropietarioAula();
	}

	/**
	 * Función que establece el centro o departamento responsable de hacer la
	 * reserva.
	 * 
	 * @param responsable Centro o departamento responsable de hacer la reserva
	 */
	public void setPropietarioResponsable(PropietarioAula responsable) {
		this.propietarioResponsable = responsable;
	}

	/**
	 * Función que devuelve el usuario que ha realizado la reserva y el centro o
	 * departamento en nombre del que ha realizado la reserva.
	 * 
	 * @return Usuario que ha realizado la reserva y el centro o departamento en
	 *         nombre del que ha realizado la reserva
	 */
	public String getRegistradaPor() {
		return this.usuarioResponsable + " - " + this.propietarioResponsable.getIdPropietarioAula();
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
		return "Reserva [Fecha - " + this.getFecha() + ", Dia semana - " + this.getDiaSemana() + ", Hora inicio - "
				+ this.getHoraInicio() + ", Hora fin - " + this.getHoraFin() + ", " + this.getNombreAula() + "-"
				+ this.getNombreCentroAula() + ", Motivo - " + this.getMotivo() + ", A cargo de - " + this.getACargoDe()
				+ " (" + this.getUsuarioResponsable() + " - "
				+ this.getPropietarioResponsable().getNombrePropietarioAula() + ")]";

	}
}
