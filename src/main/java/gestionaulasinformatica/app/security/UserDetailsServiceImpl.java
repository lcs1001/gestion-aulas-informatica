package gestionaulasinformatica.app.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import gestionaulasinformatica.backend.entity.Usuario;
import gestionaulasinformatica.backend.repository.IUsuarioRepository;

/**
 * Implementa {@link UserDetailsService}.
 * 
 * La implementación busca las entidades {@link Usuario} por la dirección de
 * correo proporcionada en la ventana de login.
 */
@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

	private final IUsuarioRepository usuarioRepository;

	@Autowired
	public UserDetailsServiceImpl(IUsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	/**
	 * Recupera el {@link Usuario} de la base de datos usando el correo
	 * proporcionado en el login y devuelve un
	 * {@link org.springframework.security.core.userdetails.User}.
	 * 
	 * @param username Correo del usuario
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByCorreoUsuarioIgnoreCase(username);
		if (null == usuario) {
			throw new UsernameNotFoundException("No hay usuarios con ese nombre de usuario: " + username);
		} else {
			return new org.springframework.security.core.userdetails.User(usuario.getCorreoUsuario(),
					usuario.getContrasenaHash(),
					Collections.singletonList(new SimpleGrantedAuthority(usuario.getRolUsuario().toString())));
		}
	}
}