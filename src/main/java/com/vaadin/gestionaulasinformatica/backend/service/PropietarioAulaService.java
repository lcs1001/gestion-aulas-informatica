package com.vaadin.gestionaulasinformatica.backend.service;

import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.repository.IPropietarioAulaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

	public Collection<PropietarioAula> findAll() {
		return propietarioAulaRepository.findAll();
	}
	
	public Optional<PropietarioAula> findById(String id) {
		return propietarioAulaRepository.findById(id);
	}
	
	public List<String> findAllNombres(){
		return propietarioAulaRepository.findAllNombres();
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
	
	public Boolean existById(String id) {
		return propietarioAulaRepository.existsById(id);
	}
}
