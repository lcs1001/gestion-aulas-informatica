package com.vaadin.gestionaulasinformatica.backend.service;

import com.vaadin.gestionaulasinformatica.backend.entity.Aula;
import com.vaadin.gestionaulasinformatica.backend.repository.IAulaRepository;
import org.springframework.stereotype.Service;

/**
 * Service para la entidad Aula.
 * 
 * @author Lisa
 *
 */
@Service
public class AulaService {
	private IAulaRepository aulaRepository;

	public AulaService(IAulaRepository aulaRepository) {
		this.aulaRepository = aulaRepository;
	}

	public Iterable<Aula> findAllAulas() {
		return aulaRepository.findAll();
	}

	public long count() {
		return aulaRepository.count();
	}

	public void delete(Aula aula) {
		aulaRepository.delete(aula);
	}

	public void save(Aula aula) {
		aulaRepository.save(aula);
	}

}
