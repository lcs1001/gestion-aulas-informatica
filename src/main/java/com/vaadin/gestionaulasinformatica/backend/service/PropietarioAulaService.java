package com.vaadin.gestionaulasinformatica.backend.service;

import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.repository.IPropietarioAulaRepository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

/**
 * Service para la entidad PropietarioAula.
 * 
 * @author Lisa
 *
 */
@Service
public class PropietarioAulaService {
	private static final Logger LOGGER = Logger.getLogger(PropietarioAula.class.getName());
	private IPropietarioAulaRepository propietarioAulaRepository;

	public PropietarioAulaService(IPropietarioAulaRepository propietarioAulaRepository) {
		this.propietarioAulaRepository = propietarioAulaRepository;
	}

	/**
	 * Función que devuelve una lista con todos los propietarios de aulas que hay en
	 * el repositorio.
	 * 
	 * @return Lista con todos los propietarios de aulas que hay en el repositorio
	 */
	public List<PropietarioAula> findAll() {
		return propietarioAulaRepository.findAll();
	}

//	/**
//	 * Función que devuelve una lista con todos los nombres de los propietarios de
//	 * aulas que hay en el repositorio.
//	 * 
//	 * @return Lista con todos los nombres de los propietarios de aulas que hay en
//	 *         el repositorio
//	 */
//	public List<String> findAllNombres() {
//		return propietarioAulaRepository.findAllNombres();
//	}

	/**
	 * Función que devuelve el número de propietarios de aulas que hay en el
	 * repositorio.
	 * 
	 * @return Número de propietarios de aulas que hay en el repositorio
	 */
	public long count() {
		return propietarioAulaRepository.count();
	}

	/**
	 * Función que elimina el propietario de aula pasado por parámetro del
	 * repositorio.
	 * 
	 * @param propietario Propietario de aula que se quiere eliminar
	 */
	public void delete(PropietarioAula propietario) {
		propietarioAulaRepository.delete(propietario);
	}

	/**
	 * Función que guarda el propietario de aula pasado por parámetro en el
	 * repositorio si no es null.
	 * 
	 * @param propietario Propietario de aula que se quier guardar
	 */
	public void save(PropietarioAula propietario) {
		if (propietario == null) {
			LOGGER.log(Level.SEVERE, "El propietario del aula que se quiere guardar es nulo.");
			return;
		}
		propietarioAulaRepository.save(propietario);
	}
}
