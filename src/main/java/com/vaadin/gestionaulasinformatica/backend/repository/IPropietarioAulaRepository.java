package com.vaadin.gestionaulasinformatica.backend.repository;

import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
/**
 * Repositorio para la entidad PropietarioAula con clave primaria de tipo String.
 * 
 * @author Lisa
 *
 */
public interface IPropietarioAulaRepository extends JpaRepository<PropietarioAula, String> {
	@Query("select p.nombrePropietarioAula from PropietarioAula p")
	List<String> findAllNombres();
}
