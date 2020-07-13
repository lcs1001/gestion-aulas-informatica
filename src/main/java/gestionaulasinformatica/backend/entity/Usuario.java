package gestionaulasinformatica.backend.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "usuario", schema = "public")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Integer idUsuario;

	@NotNull
	@Email
	@Size(max = 100)
	@Column(name = "correo", unique = true)
	private String correoUsuario;

	@NotNull
	@Size(min = 5, max = 100)
	@Column(name = "contrasena")
	private String contrasenaHash;

	@NotNull
	@NotEmpty
	@Size(max = 50)
	@Column(name = "nombre")
	private String nombreUsuario;

	@NotNull
	@NotEmpty
	@Size(max = 50)
	@Column(name = "apellidos")
	private String apellidosUsuario;

	@NotNull
	@NotEmpty
	@Size(min = 9, max = 9)
	@Column(name = "telefono")
	private String telefonoUsuario = "";

	@NotNull
	@NotEmpty
	@Size(max = 20)
	@Column(name = "rol")
	private String rolUsuario;

	@Column(name = "bloqueado")
	private Boolean bloqueado = false;

	/**
	 * Asociación bidireccional OneToMany con PropietarioAula para indicar los
	 * centros o departamentos de los que es responsable el usuario.
	 */
	@OneToMany(mappedBy = "usuarioResponsable", cascade = CascadeType.ALL)
	private Set<PropietarioAula> lstPropietariosResponsabilidad;

	/**
	 * Función que prepara los datos del correo antes de guardarlos.
	 */
	@PrePersist
	@PreUpdate
	private void prepareData() {
		this.correoUsuario = correoUsuario == null ? null : correoUsuario.toLowerCase();
	}

	/**
	 * Contructor de la clase sin parámetros.
	 */
	public Usuario() {
	}

	/**
	 * Constructor de la clase con parámetros.
	 * 
	 * @param correo     Correo del usuario
	 * @param contrasena Contraseña hasheada del usuario
	 * @param nombre     Nombre del usuario
	 * @param apellidos  Apellidos del usuario
	 * @param telefono   Teléfono del usuario
	 * @param rol        Rol del usuario
	 */
	public Usuario(String correo, String contrasena, String nombre, String apellidos, String telefono, String rol) {
		this.correoUsuario = correo;
		this.contrasenaHash = contrasena;
		this.nombreUsuario = nombre;
		this.apellidosUsuario = apellidos;
		this.telefonoUsuario = telefono;
		this.rolUsuario = rol;
	}

	/**
	 * Función que devuelve el ID del usuario.
	 * 
	 * @return ID del usuario
	 */
	public Integer getIdUsuario() {
		return this.idUsuario;
	}

	/**
	 * Función que devuelve el correo del usuario
	 * 
	 * @return Correo del usuario
	 */
	public String getCorreoUsuario() {
		return this.correoUsuario;
	}

	/**
	 * Función que establece el correo del usuario.
	 * 
	 * @param correo Correo del usuario
	 */
	public void setCorreoUsuario(String correo) {
		this.correoUsuario = correo;
	}

	/**
	 * Función que devuelve la contraseña hasheada del usuario.
	 * 
	 * @return Contraseña hasheada del usuario
	 */
	public String getContrasenaHash() {
		return this.contrasenaHash;
	}

	/**
	 * Función que establece la contraseña hasheada del usuario.
	 * 
	 * @param contrasena Contraseña hasheada del usuario
	 */
	public void setContrasenaHash(String contrasena) {
		this.contrasenaHash = contrasena;
	}

	/**
	 * Función que devuelve el nombre del usuario.
	 * 
	 * @return Nombre del usuario
	 */
	public String getNombreUsuario() {
		return this.nombreUsuario;
	}

	/**
	 * Función que establece el nombre del usuario.
	 * 
	 * @param nombre Nombre del usuario
	 */
	public void setNombreUsuario(String nombre) {
		this.nombreUsuario = nombre;
	}

	/**
	 * Función que devuelve los apellidos del usuario.
	 * 
	 * @return Apellidos del usuario
	 */
	public String getApellidosUsuario() {
		return this.apellidosUsuario;
	}

	/**
	 * Función que establece los apellidos del usuario.
	 * 
	 * @param apellidos Apellidos del usuario
	 */
	public void setApellidosUsuario(String apellidos) {
		this.apellidosUsuario = apellidos;
	}

	/**
	 * Función que devuelve el nombre y los apellidos del usuario.
	 * 
	 * @return Nombre y apellidos del usuario
	 */
	public String getNombreApellidosUsuario() {
		return this.nombreUsuario + " " + this.apellidosUsuario;
	}

	/**
	 * Función que devuelve el teléfono del usuario.
	 * 
	 * @return Teléfono del usuario
	 */
	public String getTelefonoUsuario() {
		return this.telefonoUsuario;
	}

	/**
	 * Función que establece el telefono del usuario.
	 * 
	 * @param telefono Teléfono del usuario
	 */
	public void setTelefonoUsuario(String telefono) {
		this.telefonoUsuario = telefono;
	}

	/**
	 * Función que devuelve el rol del usuario.
	 * 
	 * @return Rol del usuario
	 */
	public String getRolUsuario() {
		return this.rolUsuario;
	}

	/**
	 * Función que establece el rol del usuario.
	 * 
	 * @param rol Rol del usuario
	 */
	public void setRolUsuario(String rol) {
		this.rolUsuario = rol;
	}

	/**
	 * Función que devuelve si el usuario está bloqueado o no.
	 * 
	 * @return Si el usuario está bloqueado o no
	 */
	public Boolean isBloqueado() {
		return bloqueado;
	}

	/**
	 * Función que establece si el usuario está bloqueado o no.
	 * 
	 * @param bloqueado Si el usuario está bloqueado o no
	 */
	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	/**
	 * Función que devuelve una lista con los centros o departamentos de los que es
	 * responasble el usuario.
	 * 
	 * @return Lista con los centros o departamentos de los que es responasble el
	 *         usuario
	 */
	public Set<PropietarioAula> getPropietariosResponsabilidad() {
		return this.lstPropietariosResponsabilidad;
	}

	/**
	 * Función que establece los centros o departamentos de los que es responasble
	 * el usuario.
	 * 
	 * @param propietarios Centros o departamentos de los que es responasble el
	 *                     usuario
	 */
	public void setPropietariosResponsabilidad(Set<PropietarioAula> propietarios) {
		this.lstPropietariosResponsabilidad = propietarios;
	}

	/**
	 * Función que añade el centro o departamento pasado a la lista de propietarios
	 * de los que es responsable.
	 * 
	 * @param propietario Centro o departamento que añadir a la lista de
	 *                    propietarios de los que es responsable
	 * 
	 * @return Centro o departamento añadido
	 */
	public PropietarioAula addPropietarioResponsabilidad(PropietarioAula propietario) {
		this.getPropietariosResponsabilidad().add(propietario);

		return propietario;
	}

	/**
	 * Función que elimina el centro o departamento pasado de la lista de
	 * propietarios de los que es responsable.
	 * 
	 * @param propietario Centro o departamento que eliminar de la lista de
	 *                    propietarios de los que es responsable
	 * 
	 * @return Centro o departamento eliminado
	 */
	public PropietarioAula removePropietarioResponsabilidad(PropietarioAula propietario) {
		this.getPropietariosResponsabilidad().remove(propietario);

		return propietario;
	}

	public boolean isPersisted() {
		return idUsuario != null;
	}

	@Override
	public int hashCode() {
		if (getIdUsuario() != null) {
			return getIdUsuario().hashCode();
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
		Usuario other = (Usuario) obj;
		if (getIdUsuario() == null || other.getIdUsuario() == null) {
			return false;
		}
		return getIdUsuario().equals(other.getIdUsuario());
	}

	@Override
	public String toString() {
		return "Usuario [Nombre - " + this.getNombreUsuario() + " " + this.getApellidosUsuario() + ", Correo - "
				+ this.getCorreoUsuario() + ", Telefono - " + this.getTelefonoUsuario() + ", Rol - "
				+ this.getRolUsuario() + "]";
	}
}
