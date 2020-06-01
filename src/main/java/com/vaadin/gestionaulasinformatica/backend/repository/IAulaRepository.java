package com.vaadin.gestionaulasinformatica.backend.repository;

import com.vaadin.gestionaulasinformatica.backend.entity.Aula;
import com.vaadin.gestionaulasinformatica.backend.entity.AulaPK;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repositorio para la entidad Aula con clave primaria de tipo AulaPK.
 * 
 * @author Lisa
 *
 */
public interface IAulaRepository extends JpaRepository<Aula, AulaPK> {
}
