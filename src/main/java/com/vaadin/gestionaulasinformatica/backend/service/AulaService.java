package com.vaadin.gestionaulasinformatica.backend.service;

import com.vaadin.gestionaulasinformatica.backend.entity.Aula;
import com.vaadin.gestionaulasinformatica.backend.repository.IAulaRepository;

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

	public Iterable<Aula> findAll() {
		return aulaRepository.findAll();
	}

	public long count() {
		return aulaRepository.count();
	}

	public void delete(Aula aula) {
		aulaRepository.delete(aula);
	}

	public void save(Aula aula) {
		if (aula == null) { 
			LOGGER.log(Level.SEVERE,
					"El aula que se quiere guardar es nulo.");
			return;
		}
		aulaRepository.save(aula);
	}

}
