package com.vaadin.gestionaulasinformatica.backend.service;

import org.springframework.stereotype.Service;

import com.vaadin.gestionaulasinformatica.backend.entity.HistoricoReservas;
import com.vaadin.gestionaulasinformatica.backend.repository.IHistoricoReservas;

/**
 * Service para la entidad HistoricoReservas.
 * 
 * @author Lisa
 *
 */
@Service
public class HistoricoReservasService {
	private IHistoricoReservas historicoReservasRepository;

	public HistoricoReservasService(IHistoricoReservas historicoReservasRepository) {
		this.historicoReservasRepository = historicoReservasRepository;
	}

	public Iterable<HistoricoReservas> findAllOperacionesHR() {
		return historicoReservasRepository.findAll();
	}

	public long count() {
		return historicoReservasRepository.count();
	}

	public void save(HistoricoReservas operacion) {
		historicoReservasRepository.save(operacion);
	}
}
