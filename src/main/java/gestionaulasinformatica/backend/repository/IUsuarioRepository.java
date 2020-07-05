package gestionaulasinformatica.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import gestionaulasinformatica.backend.entity.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

	Usuario findByCorreoUsuarioIgnoreCase(String correoUsuario);

	Page<Usuario> findBy(Pageable pageable);

	Page<Usuario> findByCorreoUsuarioLikeIgnoreCaseOrNombreUsuarioLikeIgnoreCaseOrApellidosUsuarioLikeIgnoreCaseOrRolUsuarioLike(
			String correoUsuarioLike, String nombreUsuarioLike, String apellidosUsuarioLike, String rolUsuarioLike, Pageable pageable);

	long countByCorreoUsuarioLikeIgnoreCaseOrNombreUsuarioLikeIgnoreCaseOrApellidosUsuarioLikeIgnoreCaseOrRolUsuarioLike(
			String correoUsuarioLike, String nombreUsuarioLike, String apellidosUsuarioLike, String rolUsuarioLike);
}