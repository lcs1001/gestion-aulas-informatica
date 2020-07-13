package gestionaulasinformatica.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gestionaulasinformatica.backend.entity.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

	Usuario findByCorreoUsuarioIgnoreCase(String correoUsuario);

	Page<Usuario> findByCorreoUsuarioLikeIgnoreCaseOrNombreUsuarioLikeIgnoreCaseOrApellidosUsuarioLikeIgnoreCaseOrRolUsuarioLikeIgnoreCase(
			String correoUsuarioLike, String nombreUsuarioLike, String apellidosUsuarioLike, String rolUsuarioLike,
			Pageable pageable);

	long countByCorreoUsuarioLikeIgnoreCaseOrNombreUsuarioLikeIgnoreCaseOrApellidosUsuarioLikeIgnoreCaseOrRolUsuarioLikeIgnoreCase(
			String correoUsuarioLike, String nombreUsuarioLike, String apellidosUsuarioLike, String rolUsuarioLike);

	@Query("SELECT u FROM Usuario u WHERE rolUsuario LIKE 'RESPONSABLE' ORDER BY nombreUsuario ASC")
	List<Usuario> findAllResponsables();

	@Query("SELECT u FROM Usuario u " + "WHERE lower(u.nombreUsuario) LIKE lower(concat('%', :filtroTexto, '%'))"
			+ "OR lower(u.apellidosUsuario) like lower(concat('%', :filtroTexto, '%'))" + "ORDER BY nombreUsuario ASC")
	List<Usuario> buscarUsuario(@Param("filtroTexto") String filtroTexto);

	List<Usuario> findByRolUsuarioLikeIgnoreCase(String rolUsuario);
}