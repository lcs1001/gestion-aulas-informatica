package gestionaulasinformatica.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import gestionaulasinformatica.backend.entity.HistoricoReservas;
import gestionaulasinformatica.backend.repository.IHistoricoReservasRepository;
import gestionaulasinformatica.backend.specification.HistoricoReservasSpecification;

/**
 * Service para la entidad HistoricoReservas.
 * 
 * @author Lisa
 *
 */
@Service
public class HistoricoReservasService {
	private static final Logger LOGGER = LoggerFactory.getLogger(HistoricoReservasService.class.getName());
	private IHistoricoReservasRepository historicoReservasRepository;

	/**
	 * Constructor del service
	 * 
	 * @param historicoReservasRepository Repositorio de la entidad
	 *                                    HistoricoReservas
	 */
	@Autowired
	public HistoricoReservasService(IHistoricoReservasRepository historicoReservasRepository) {
		this.historicoReservasRepository = historicoReservasRepository;
	}

	/**
	 * Función que devuelve una lista con todas las operaciones realizadas sobre las
	 * reservas que hay en la BD.
	 * 
	 * @return Lista con todas las operaciones realizadas sobre las reservas que hay
	 *         en la BD
	 */
	public List<HistoricoReservas> findAll() {
		return historicoReservasRepository.findAll();
	}

	/**
	 * Función que devuelve una lista con todas las operaciones realizadas sobre las
	 * reservas que hay en la BD que cumplen con los filtros aplicados.
	 * 
	 * @param fechaDesde Fecha desde la que obtener las reservas
	 * @param fechaHasta Fecha hasta la que obtener las reservas
	 * 
	 * @return Lista con todas las operaciones realizadas sobre las reservas que hay
	 *         en la BD que cumplen con los filtros aplicados
	 */
	public List<HistoricoReservas> findAll(LocalDate fechaDesde, LocalDate fechaHasta) {
		return historicoReservasRepository.findAll(HistoricoReservasSpecification.findByFilters(fechaDesde, fechaHasta),
				Sort.by(Sort.Direction.DESC, "fechaHoraOperacion"));
	}

	/**
	 * Función que devuelve el número de operaciones realizadas sobre las reservas
	 * que hay en la BD.
	 * 
	 * @return Número de operaciones realizadas sobre las reservas que hay en la BD
	 */
	public long count() {
		return historicoReservasRepository.count();
	}

	/**
	 * Función que guarda la operación realizada sobre una reserva pasada por
	 * parámetro en la BD si no es null.
	 * 
	 * @param operacion Operación realizada sobre una reserva que se quiere guardar
	 */
	public void save(HistoricoReservas operacion) {
		if (operacion == null) {
			LOGGER.error("La operación sobre una reserva que se quiere guardar es nula.");
			return;
		}
		historicoReservasRepository.save(operacion);
	}
}
