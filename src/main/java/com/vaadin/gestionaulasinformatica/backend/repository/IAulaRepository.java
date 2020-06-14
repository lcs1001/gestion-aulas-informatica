package com.vaadin.gestionaulasinformatica.backend.repository;

// Imports Java
import java.util.List;

// Imports SpringFramework
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Imports backend
import com.vaadin.gestionaulasinformatica.backend.entity.Aula;
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;

/**
 * Repositorio para la entidad Aula con clave primaria de tipo AulaPK.
 * 
 * @author Lisa
 *
 */
public interface IAulaRepository extends JpaRepository<Aula, Integer> {
	@Query("SELECT a FROM Aula a "
			+ "WHERE lower(a.propietarioAula) LIKE lower(concat('%', :filtroPropietarioAula, '%'))")
	List<Aula> search(@Param("filtroPropietarioAula") PropietarioAula filtroPropietarioAula);
}
