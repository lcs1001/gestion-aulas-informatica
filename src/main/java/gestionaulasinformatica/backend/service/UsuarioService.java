package gestionaulasinformatica.backend.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gestionaulasinformatica.backend.entity.Usuario;
import gestionaulasinformatica.backend.repository.IUsuarioRepository;
import gestionaulasinformatica.exceptions.UserFriendlyDataException;

/**
 * Service para la entidad Usuario.
 * 
 * @author Lisa
 *
 */
@Service
public class UsuarioService {

	public static final String MODIFY_LOCKED_USER_NOT_PERMITTED = "El usuario se ha bloqueado y no se puede modificar ni eliminar";
	private static final String DELETING_SELF_NOT_PERMITTED = "No puedes eliminar tu propio usuario";
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
	 * Función que devuelve una lista con todos los responsables que hay en la BD.
	 * 
	 * @return Lista con todos los responsables que hay en la BD
	 */
	public List<Usuario> findAllResponsables() {
		return usuarioRepository.findAllResponsables();
	}

	/**
	 * Función que elimina el usuario "usuarioEliminar" pasado por parámetro de la
	 * BD, si no es el usuario logeado y si no está bloqueado.
	 * 
	 * @param usuarioActual   Usuario logeado
	 * @param usuarioEliminar Usuario que se quiere eliminar
	 */
	@Transactional
	public void delete(Usuario usuarioActual, Usuario usuarioEliminar) {
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
	 * bloqueado,
	 * 
	 * @param usuario Usuario que se quiere guardar
	 */
	public void save(Usuario usuario) {
		throwIfUserLocked(usuario);
		usuarioRepository.saveAndFlush(usuario);
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
			throw new UserFriendlyDataException(DELETING_SELF_NOT_PERMITTED);
		}
	}

	/**
	 * Función que lanza una excepción si se intenta borrar un usuario bloqueado.
	 * 
	 * @param usuario Usuario
	 */
	private void throwIfUserLocked(Usuario usuario) {
		if (usuario != null && usuario.isBloqueado()) {
			throw new UserFriendlyDataException(MODIFY_LOCKED_USER_NOT_PERMITTED);
		}
	}
}
