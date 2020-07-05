package gestionaulasinformatica.backend.service;

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

	@Autowired
	public UsuarioService(IUsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	public IUsuarioRepository getRepository() {
		return usuarioRepository;
	}
	
	@Transactional
	public void delete(Usuario currentUser, Usuario userToDelete) {
		throwIfDeletingSelf(currentUser, userToDelete);
		throwIfUserLocked(userToDelete);
		getRepository().delete(userToDelete);
	}
	
	public Usuario save(Usuario currentUser, Usuario entity) {
		throwIfUserLocked(entity);
		return getRepository().saveAndFlush(entity);
	}

	private void throwIfDeletingSelf(Usuario currentUser, Usuario user) {
		if (currentUser.equals(user)) {
			throw new UserFriendlyDataException(DELETING_SELF_NOT_PERMITTED);
		}
	}

	private void throwIfUserLocked(Usuario entity) {
		if (entity != null && entity.isBloqueado()) {
			throw new UserFriendlyDataException(MODIFY_LOCKED_USER_NOT_PERMITTED);
		}
	}
}
