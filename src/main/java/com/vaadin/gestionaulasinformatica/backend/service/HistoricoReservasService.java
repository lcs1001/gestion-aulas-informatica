package com.vaadin.gestionaulasinformatica.backend.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.vaadin.gestionaulasinformatica.backend.entity.HistoricoReservas;
import com.vaadin.gestionaulasinformatica.backend.repository.IHistoricoReservasRepository;

/**
 * Service para la entidad HistoricoReservas.
 * 
 * @author Lisa
 *
 */
@Service
public class HistoricoReservasService {
	private static final Logger LOGGER = Logger.getLogger(HistoricoReservasService.class.getName());
	private IHistoricoReservasRepository historicoReservasRepository;

	public HistoricoReservasService(IHistoricoReservasRepository historicoReservasRepository) {
		this.historicoReservasRepository = historicoReservasRepository;
	}

	/**
	 * Función que devuelve una lista con todas las operaciones realizadas sobre las
	 * reservas que hay en el repositorio.
	 * 
	 * @return Lista con todas las operaciones realizadas sobre las reservas que hay
	 *         en el repositorio
	 */
	public List<HistoricoReservas> findAll() {
		return historicoReservasRepository.findAll();
	}

	/**
	 * Función que devuelve el número de operaciones realizadas sobre las reservas
	 * que hay en el repositorio.
	 * 
	 * @return Número de operaciones realizadas sobre las reservas que hay en el
	 *         repositorio
	 */
	public long count() {
		return historicoReservasRepository.count();
	}

	/**
	 * Función que guarda la operación realizada sobre una reserva pasada por
	 * parámetro en el repositorio si no es null.
	 * 
	 * @param operacion Operación realizada sobre una reserva que se quier guardar
	 */
	public void save(HistoricoReservas operacion) {
		if (operacion == null) {
			LOGGER.log(Level.SEVERE, "La operación sobre una reserva que se quiere guardar es nula.");
			return;
		}
		historicoReservasRepository.save(operacion);
	}
}
