package gestionaulasinformatica.backend.repository;

// Imports Java
import java.util.List;

// Imports SpringFramework
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gestionaulasinformatica.backend.entity.PropietarioAula;
import gestionaulasinformatica.backend.entity.Usuario;

/**
 * Repositorio para la entidad PropietarioAula con clave primaria de tipo
 * String.
 * 
 * @author Lisa
 *
 */
public interface IPropietarioAulaRepository extends JpaRepository<PropietarioAula, String> {
	@Query("SELECT p FROM PropietarioAula p "
			+ "WHERE lower(p.nombrePropietarioAula) LIKE lower(concat('%', :filtroTexto, '%')) "
			+ "ORDER BY nombrePropietarioAula ASC")
	List<PropietarioAula> buscarPropietario(@Param("filtroTexto") String filtroTexto);

	@Query("SELECT c FROM Centro c ORDER BY nombrePropietarioAula ASC")
	List<PropietarioAula> findAllCentros();

	@Query("SELECT p FROM PropietarioAula p " + "WHERE p.usuarioResponsable = :usuarioLogeado " + "ORDER BY nombrePropietarioAula ASC")
	List<PropietarioAula> findAllPropietariosResponsable(@Param("usuarioLogeado") Usuario usuarioLogeado);
}
