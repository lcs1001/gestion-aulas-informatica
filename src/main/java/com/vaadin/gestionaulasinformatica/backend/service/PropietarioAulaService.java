package com.vaadin.gestionaulasinformatica.backend.service;

import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.repository.IPropietarioAulaRepository;
import org.springframework.stereotype.Service;

/**
 * Service para la entidad PropietarioAula.
 * 
 * @author Lisa
 *
 */
@Service
public class PropietarioAulaService {
	private IPropietarioAulaRepository propietarioAulaRepository;

	public PropietarioAulaService(IPropietarioAulaRepository propietarioAulaRepository) {
		this.propietarioAulaRepository = propietarioAulaRepository;
	}

	public Iterable<PropietarioAula> findAllPropietariosAulas() {
		return propietarioAulaRepository.findAll();
	}

	public long count() {
		return propietarioAulaRepository.count();
	}

	public void delete(PropietarioAula propietario) {
		propietarioAulaRepository.delete(propietario);
	}

	public void save(PropietarioAula propietario) {
		propietarioAulaRepository.save(propietario);
	}
}
