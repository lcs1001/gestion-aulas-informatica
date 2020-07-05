package gestionaulasinformatica.backend.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Check;

import gestionaulasinformatica.backend.data.TipoPropietarioAula;

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
	@OneToOne
	@JoinColumn(name="id_responsable")
	private Usuario usuarioResponsable;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "tipo", insertable = false, updatable = false)
	private TipoPropietarioAula tipoPropietarioAula;

	/**
	 * Asociación bidireccional OneToMany con Aula para indicar las aulas de las que
	 * es responsable el centro o departamento.
	 */
	@OneToMany(mappedBy = "propietarioAula", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Aula> lstAulasPropiedad;

	/**
	 * Asociación bidireccional OnetoMany con Reserva para indicar las reservas que
	 * ha realizado el responsable del centro o departamento.
	 */
	@OneToMany(mappedBy = "responsable", cascade = CascadeType.ALL)
	private Set<Reserva> lstReservasPropietarioAula;

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
	 * @param id          ID del propietario (centro/departamento)
	 * @param nombre      Nombre del propietario (centro/departamento)
	 * @param responsable Responsable del centro/departamento
	 * @param tipo        Tipo de propietario de aula (centro o departamento)
	 */
	public PropietarioAula(String id, String nombre, Usuario responsable, TipoPropietarioAula tipo) {
		this.idPropietarioAula = id;
		this.nombrePropietarioAula = nombre;
		this.usuarioResponsable = responsable;
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
	 * Función que devuelve el usuario responsable del centro/departamento.
	 * 
	 * @return Usuario responsable del centro/departamento
	 */
	public Usuario getUsuarioResponsable() {
		return this.usuarioResponsable;
	}

	/**
	 * Función que establece el usuario responsable del centro/departamento.
	 * 
	 * @param usuario Usuario responsable del centro/departamento
	 */
	public void setUsuarioResponsable(Usuario usuario) {
		this.usuarioResponsable = usuario;
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
	 * Función que devuelve si el propietario tiene aulas bajo su responsabilidad o
	 * no.
	 * 
	 * @return Si tiene aulas bajo su responsabilidad o no
	 */
	public Boolean tieneAulasPropiedad() {
		return getAulasPropiedad().isEmpty() ? false : true;
	}

	/**
	 * Función que devuelve una lista de las aulas de las que es propietario.
	 * 
	 * @return Lista de aulas de las que es propietario
	 */
	public Set<Aula> getAulasPropiedad() {
		return this.lstAulasPropiedad;
	}

	/**
	 * Función que establece la lista de aulas de las que es propietario.
	 * 
	 * @param aulasPertenecientes Lista de aulas de las que es propietario
	 */
	public void setAulasPropiedad(Set<Aula> aulasPropiedad) {
		this.lstAulasPropiedad = aulasPropiedad;
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
		return this.lstReservasPropietarioAula;
	}

	/**
	 * Función que establece la lista de reservas de las que es responsable.
	 * 
	 * @param reservas Lista de reservas de las que es responsable
	 */
	public void setReservasPropietarioAula(Set<Reserva> reservas) {
		this.lstReservasPropietarioAula = reservas;
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
