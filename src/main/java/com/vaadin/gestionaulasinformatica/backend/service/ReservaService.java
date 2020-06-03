package com.vaadin.gestionaulasinformatica.backend.service;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.vaadin.gestionaulasinformatica.backend.entity.Reserva;
import com.vaadin.gestionaulasinformatica.backend.repository.IReservaRepository;

/**
 * Service para la entidad Reserva.
 * 
 * @author Lisa
 *
 */
@Service
public class ReservaService {
	private static final Logger LOGGER = Logger.getLogger(ReservaService.class.getName());
	private IReservaRepository reservaRepository;

	public ReservaService(IReservaRepository reservaRepository) {
		this.reservaRepository = reservaRepository;
	}

	public Collection<Reserva> findAll() {
		return reservaRepository.findAll();
	}

	public long count() {
		return reservaRepository.count();
	}

	public void delete(Reserva reserva) {
		reservaRepository.delete(reserva);
	}

	public void save(Reserva reserva) {
		if (reserva == null) { 
			LOGGER.log(Level.SEVERE,
					"La reserva que se quiere guardar es nula.");
			return;
		}
		reservaRepository.save(reserva);
	}
}
