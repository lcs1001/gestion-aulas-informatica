package com.vaadin.gestionaulasinformatica.backend.service;

// Imports Java
import java.time.*;
import java.util.*;
import java.util.logging.*;

//Imports SpringFramework
import org.springframework.stereotype.Service;

// Imports backend
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.entity.Reserva;
import com.vaadin.gestionaulasinformatica.backend.repository.IReservaRepository;
import com.vaadin.gestionaulasinformatica.backend.specification.ReservaSpecification;

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

	/**
	 * Función que devuelve una lista con todas las reservas que hay en el
	 * repositorio.
	 * 
	 * @return Lista con todas las reservas que hay en la BD
	 */
	public List<Reserva> findAll() {
		return reservaRepository.findAll();
	}

	/**
	 * Función que devuelve una lista con todas las reservas que hay en la BD que
	 * cumplen con los filtros aplicados.
	 * 
	 * @param fechaDesde  Fecha (de inicio) desde la que obtener las reservas
	 * @param fechaHasta  Fecha (de inicio) hasta la que obtener las reservas
	 * @param horaDesde   Hora (de inicio) de la reserva desde la que obtener las
	 *                    reservas
	 * @param horaHasta   Hora (de inicio) de la reserva hasta la que obtener las
	 *                    reservas
	 * @param responsable Responsable del aula de la reserva del que obtener las
	 *                    reservas
	 * 
	 * @return Lista con todas las reservas que hay en la BD que cumplen con los
	 *         filtros aplicados
	 */
	public List<Reserva> findAll(LocalDate fechaDesde, LocalDate fechaHasta, LocalTime horaDesde, LocalTime horaHasta,
			PropietarioAula responsable) {
		return reservaRepository
				.findAll(ReservaSpecification.findByFilters(fechaDesde, fechaHasta, horaDesde, horaHasta, responsable));
	}

	/**
	 * Función que devuelve el número de reservas que hay en la BD.
	 * 
	 * @return Número de reservas que hay en la BD
	 */
	public long count() {
		return reservaRepository.count();
	}

	/**
	 * Función que elimina la reserva pasada por parámetro de la BD.
	 * 
	 * @param reserva Reserva que se quiere eliminar
	 */
	public void delete(Reserva reserva) {
		reservaRepository.delete(reserva);
	}

	/**
	 * Función que guarda la reserva pasada por parámetro en la BD si no es null.
	 * 
	 * @param reserva Reserva que se quier guardar
	 */
	public void save(Reserva reserva) {
		if (reserva == null) {
			LOGGER.log(Level.SEVERE, "La reserva que se quiere guardar es nula.");
			return;
		}
		reservaRepository.save(reserva);
	}
}
