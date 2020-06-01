package com.vaadin.gestionaulasinformatica.backend.repository;

import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repositorio para la entidad PropietarioAula con clave primaria de tipo String.
 * 
 * @author Lisa
 *
 */
public interface IPropietarioAulaRepository extends JpaRepository<PropietarioAula, String> {
}
