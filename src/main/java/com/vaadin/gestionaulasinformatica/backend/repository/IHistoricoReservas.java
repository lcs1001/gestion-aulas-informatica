package com.vaadin.gestionaulasinformatica.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.gestionaulasinformatica.backend.entity.HistoricoReservas;

/**
 * Repositorio para la entidad HistoricoReservas con clave primaria de tipo
 * Integer.
 * 
 * @author Lisa
 *
 */
public interface IHistoricoReservas extends JpaRepository<HistoricoReservas, Integer> {

}
