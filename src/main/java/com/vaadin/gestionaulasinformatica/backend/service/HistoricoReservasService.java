package com.vaadin.gestionaulasinformatica.backend.service;

import java.util.logging.Level;
import java.util.logging.Logger;

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
	private static final Logger LOGGER = Logger.getLogger(HistoricoReservasService.class.getName());
	private IHistoricoReservas historicoReservasRepository;

	public HistoricoReservasService(IHistoricoReservas historicoReservasRepository) {
		this.historicoReservasRepository = historicoReservasRepository;
	}

	public Iterable<HistoricoReservas> findAll() {
		return historicoReservasRepository.findAll();
	}

	public long count() {
		return historicoReservasRepository.count();
	}

	public void save(HistoricoReservas operacion) {
		if (operacion == null) { 
			LOGGER.log(Level.SEVERE,
					"La operación sobre una reserva que se quiere guardar es nula.");
			return;
		}
		historicoReservasRepository.save(operacion);
	}
}
