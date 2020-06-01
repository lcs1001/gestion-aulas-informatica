package com.vaadin.gestionaulasinformatica.backend.service;

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
	private IReservaRepository reservaRepository;

	public ReservaService(IReservaRepository reservaRepository) {
		this.reservaRepository = reservaRepository;
	}

	public Iterable<Reserva> findAllReservas() {
		return reservaRepository.findAll();
	}

	public long count() {
		return reservaRepository.count();
	}

	public void delete(Reserva reserva) {
		reservaRepository.delete(reserva);
	}

	public void save(Reserva reserva) {
		reservaRepository.save(reserva);
	}
}
