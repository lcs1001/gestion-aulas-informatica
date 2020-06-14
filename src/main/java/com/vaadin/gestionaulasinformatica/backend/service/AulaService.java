package com.vaadin.gestionaulasinformatica.backend.service;

import com.vaadin.gestionaulasinformatica.backend.entity.Aula;
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.repository.IAulaRepository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

/**
 * Service para la entidad Aula.
 * 
 * @author Lisa
 *
 */
@Service
public class AulaService {
	private static final Logger LOGGER = Logger.getLogger(AulaService.class.getName());
	private IAulaRepository aulaRepository;

	public AulaService(IAulaRepository aulaRepository) {
		this.aulaRepository = aulaRepository;
	}

	/**
	 * Función que devuelve una lista con todas las aulas que hay en la BD.
	 * 
	 * @return Lista con todas las aulas que hay en la BD
	 */
	public List<Aula> findAll() {
		return aulaRepository.findAll();
	}

	/**
	 * Función que devuelve una lista con todas las aulas de las que es propietario
	 * el centro o departamento seleccionado.
	 * 
	 * @param filtroPropietarioAula Propietario del que se quieren obtener las aulas
	 * 
	 * @return Lista con todas las aulas de las que es propietario el centro o
	 *         departamento seleccionado
	 */
	public List<Aula> findAll(PropietarioAula filtroPropietarioAula) {
		if (filtroPropietarioAula == null) {
			return aulaRepository.findAll();
		} else {
			return aulaRepository.search(filtroPropietarioAula);
		}
	}

	/**
	 * Función que elimina el aula pasada por parámetro de la BD.
	 * 
	 * @param aula Aula que se quiere eliminar
	 */
	public void delete(Aula aula) {
		aulaRepository.delete(aula);
	}

	/**
	 * Función que guarda el aula pasada por parámetro en la BD si no es
	 * null.
	 * 
	 * @param aula Aula que se quiere guardar
	 */
	public void save(Aula aula) {
		if (aula == null) {
			LOGGER.log(Level.SEVERE, "El aula que se quiere guardar es nulo.");
			return;
		}
		aulaRepository.save(aula);
	}

}
