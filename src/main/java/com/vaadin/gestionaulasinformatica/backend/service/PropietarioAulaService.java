package com.vaadin.gestionaulasinformatica.backend.service;

import com.vaadin.gestionaulasinformatica.backend.entity.Centro;
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.repository.IPropietarioAulaRepository;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

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

	public Iterable<PropietarioAula> findAllAulas() {
		return propietarioAulaRepository.findAll();
	}

	public long count() {
		return propietarioAulaRepository.count();
	}

	public void delete(PropietarioAula propietario) {
		propietarioAulaRepository.delete(propietario);
	}

	public void save(PropietarioAula propietario) {
		if (propietario == null) { 
			LOGGER.log(Level.SEVERE,
					"El propietario del aula que se quiere guardar es nulo.");
			return;
		}
		propietarioAulaRepository.save(propietario);
	}
}
