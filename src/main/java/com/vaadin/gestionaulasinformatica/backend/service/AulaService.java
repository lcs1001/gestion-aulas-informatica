package com.vaadin.gestionaulasinformatica.backend.service;

import com.vaadin.gestionaulasinformatica.backend.entity.Aula;
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
	 * Función que devuelve una lista con todas las aulas que hay en el repositorio.
	 * 
	 * @return Lista con todas las aulas que hay en el repositorio
	 */
	public List<Aula> findAll() {
		return aulaRepository.findAll();
	}

	/**
	 * Función que devuelve una lista con todas las aulas cuyo nombre contenga el
	 * filtro de texto, o todas las aulas en caso de que el filtro sea null.
	 * 
	 * @param filtroTexto Filtro que se quiere aplicar
	 * 
	 * @return Lista con todas las aulas cuyo nombre contenga el filtro de texto, o
	 *         todas las aulas en caso de que el filtro sea null
	 */
	public List<Aula> findAll(String filtroTexto) {
		if (filtroTexto == null || filtroTexto.isEmpty()) {
			return aulaRepository.findAll();
		} else {
			return aulaRepository.search(filtroTexto);
		}
	}

	/**
	 * Función que devuelve el número de aulas que hay en el repositorio.
	 * 
	 * @return Número de aulas que hay en el repositorio
	 */
	public long count() {
		return aulaRepository.count();
	}

	/**
	 * Función que elimina el aula pasada por parámetro del repositorio.
	 * 
	 * @param aula Aula que se quiere eliminar
	 */
	public void delete(Aula aula) {
		aulaRepository.delete(aula);
	}

	/**
	 * Función que guarda el aula pasada por parámetro en el repositorio si no es
	 * null.
	 * 
	 * @param aula Aula que se quier guardar
	 */
	public void save(Aula aula) {
		if (aula == null) {
			LOGGER.log(Level.SEVERE, "El aula que se quiere guardar es nulo.");
			return;
		}
		aulaRepository.save(aula);
	}

}
