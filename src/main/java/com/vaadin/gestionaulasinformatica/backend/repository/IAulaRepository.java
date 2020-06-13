package com.vaadin.gestionaulasinformatica.backend.repository;

// Imports Java
import java.util.List;

// Imports SpringFramework
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Imports backend
import com.vaadin.gestionaulasinformatica.backend.entity.Aula;

/**
 * Repositorio para la entidad Aula con clave primaria de tipo AulaPK.
 * 
 * @author Lisa
 *
 */
public interface IAulaRepository extends JpaRepository<Aula, Integer> {
	@Query("SELECT a FROM Aula a " + "WHERE lower(a.nombreAula) LIKE lower(concat('%', :filtroTexto, '%'))")
	List<Aula> search(@Param("filtroTexto") String filtroTexto);
}
