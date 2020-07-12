package gestionaulasinformatica.backend.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import gestionaulasinformatica.backend.entity.Usuario;
import gestionaulasinformatica.backend.repository.IUsuarioRepository;
import gestionaulasinformatica.exceptions.UserFriendlyDataException;
import gestionaulasinformatica.ui.Mensajes;

/**
 * Service para la entidad Usuario.
 * 
 * @author Lisa
 *
 */
@Service
public class UsuarioService {

	private final IUsuarioRepository usuarioRepository;

	/**
	 * Constructor del service.
	 * 
	 * @param usuarioRepository Repositorio de la entidad usuario
	 */
	@Autowired
	public UsuarioService(IUsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	/**
	 * Función que devuelve una lista con todos los usuarios cuyo nombre o apellidos
	 * contengan el filtro de texto, o todos los usuarios en caso de que el filtro
	 * sea null, que hay en la BD.
	 * 
	 * @param filtroTexto Filtro que se quiere aplicar
	 * 
	 * @return Lista con todos los usuarios cuyo nombre o apellidos contengan el
	 *         filtro de texto, o todos los usuarios en caso de que el filtro sea
	 *         null, que hay en la BD
	 */
	public List<Usuario> findAll(String filtroTexto) {
		if (filtroTexto == null || filtroTexto.isEmpty()) {
			return usuarioRepository.findAll(Sort.by(Sort.Direction.ASC, "nombreUsuario"));
		} else {
			return usuarioRepository.buscarUsuario(filtroTexto);
		}
	}

	/**
	 * Función que devuelve una lista con todos los responsables que hay en la BD.
	 * 
	 * @return Lista con todos los responsables que hay en la BD
	 */
	public List<Usuario> findAllResponsables() {
		return usuarioRepository.findAllResponsables();
	}

	/**
	 * Función que devuelve el usuario asociado al correo pasado por parámetro.
	 * 
	 * @param correoUsuario Correo del usuario que que quiere obtener
	 * @return Usuario asociado al correo pasado por parámetro
	 */
	public Usuario findByCorreoUsuarioIgnoreCase(String correoUsuario) {
		return usuarioRepository.findByCorreoUsuarioIgnoreCase(correoUsuario);
	}

	/**
	 * Función que elimina el usuario "usuarioEliminar" pasado por parámetro de la
	 * BD, si no es el usuario logeado y si no está bloqueado.
	 * 
	 * @param usuarioActual   Usuario logeado
	 * @param usuarioEliminar Usuario que se quiere eliminar
	 */
	@Transactional
	public void delete(Usuario usuarioActual, Usuario usuarioEliminar) throws UserFriendlyDataException{
		throwIfDeletingSelf(usuarioActual, usuarioEliminar);
		throwIfUserLocked(usuarioEliminar);
		usuarioRepository.delete(usuarioEliminar);
	}

	/**
	 * Función que guarda la reserva pasada por parámetro en la BD si no es null.
	 * 
	 * @param reserva Reserva que se quiere guardar
	 */
	/**
	 * Función que guarda el usuario pasado por parámetro en la BD si no está
	 * bloqueado.
	 * 
	 * @param usuario Usuario que se quiere guardar
	 */
	public void save(Usuario usuario) {
		throwIfUserLocked(usuario);
		usuarioRepository.save(usuario);
	}

	/**
	 * Función que lanza una excepción si se intenta borrar el usuario que está
	 * logeado.
	 * 
	 * @param currentUser Usuario actual
	 * @param user        Usuario
	 */
	private void throwIfDeletingSelf(Usuario currentUser, Usuario user) {
		if (currentUser.equals(user)) {
			throw new UserFriendlyDataException(Mensajes.ELIMINAR_USUARIO_ACTUAL_NO_PERMITIDO.getMensaje());
		}
	}

	/**
	 * Función que lanza una excepción si se intenta borrar un usuario bloqueado.
	 * 
	 * @param usuario Usuario
	 */
	private void throwIfUserLocked(Usuario usuario) {
		if (usuario != null && usuario.isBloqueado()) {
			throw new UserFriendlyDataException(Mensajes.ELIMINAR_USUARIO_BLOQUEADO_NO_PERMITIDO.getMensaje());
		}
	}
}
