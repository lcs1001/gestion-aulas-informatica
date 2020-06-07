package com.vaadin.gestionaulasinformatica.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vaadin.gestionaulasinformatica.backend.entity.Reserva;

/**
 * Repositorio para la entidad Reserva con clave primaria de tipo Integer.
 * 
 * @author Lisa
 *
 */
public interface IReservaRepository extends JpaRepository<Reserva, Integer>, JpaSpecificationExecutor<Reserva>{

}
