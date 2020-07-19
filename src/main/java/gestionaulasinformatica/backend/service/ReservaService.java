package gestionaulasinformatica.backend.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import gestionaulasinformatica.backend.entity.Aula;
import gestionaulasinformatica.backend.entity.PropietarioAula;
import gestionaulasinformatica.backend.entity.Reserva;
import gestionaulasinformatica.backend.repository.IReservaRepository;
import gestionaulasinformatica.backend.specification.ReservaSpecification;

/**
 * Service para la entidad Reserva.
 * 
 * @author Lisa
 *
 */
@Service
public class ReservaService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReservaService.class.getName());

	private IReservaRepository reservaRepository;

	/**
	 * Constructor del service.
	 * 
	 * @param reservaRepository Repositorio de la entidad Reserva
	 */
	@Autowired
	public ReservaService(IReservaRepository reservaRepository) {
		this.reservaRepository = reservaRepository;
	}

	/**
	 * Función que devuelve una lista con todas las reservas que hay en la BD.
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
	 * @param fechaDesde           Fecha (de inicio) desde la que obtener las
	 *                             reservas
	 * @param fechaHasta           Fecha (de inicio) hasta la que obtener las
	 *                             reservas
	 * @param horaDesde            Hora (de inicio) de la reserva desde la que
	 *                             obtener las reservas
	 * @param horaHasta            Hora (de inicio) de la reserva hasta la que
	 *                             obtener las reservas
	 * @param diaSemana            Día de la semana del que obtener las reservas
	 * @param lstPropietariosAulas Lista de posibles propietarios del aula de la
	 *                             reserva de los que obtener las reservas
	 * 
	 * @return Lista con todas las reservas que hay en la BD que cumplen con los
	 *         filtros aplicados
	 */
	public List<Reserva> findAllReservasFiltros(LocalDate fechaDesde, LocalDate fechaHasta, LocalTime horaDesde,
			LocalTime horaHasta, String diaSemana, List<PropietarioAula> lstPropietariosAulas) {
		return reservaRepository.findAll(ReservaSpecification.findByFilters(fechaDesde, fechaHasta, horaDesde,
				horaHasta, diaSemana, lstPropietariosAulas), Sort.by(Sort.Direction.ASC, "fecha"));
	}

	/**
	 * Función que devuelve una lista con todas las reservas que hay en la BD del
	 * aula pasada y a partir de la fecha pasada.
	 * 
	 * @param idAula Aula de la que obtener las reservas.
	 * @param fecha  Fecha a partir de la cual obtener las reservas
	 * 
	 * @return Lista con todas las reservas que hay en la BD del aula pasada y a
	 *         partir de la fecha pasada
	 */
	public List<Reserva> findAllReservasAulaAndFechaDesde(Aula aula, LocalDate fecha) {
		return reservaRepository.findAllReservasAulaAndFechaDesde(aula, fecha);
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
	 * @param reserva Reserva que se quiere guardar
	 */
	public void save(Reserva reserva) {
		if (reserva == null) {
			LOGGER.error("La reserva que se quiere guardar es nula.");
			return;
		}
		reservaRepository.save(reserva);
	}
}
