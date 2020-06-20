package com.vaadin.gestionaulasinformatica.backend.service;

// Imports Java
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// Imports SpringFramework
import org.springframework.stereotype.Service;

// Imports backend
import com.vaadin.gestionaulasinformatica.backend.entity.Aula;
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.repository.IAulaRepository;
import com.vaadin.gestionaulasinformatica.backend.specification.AulaSpecification;

/**
 * Service para la entidad Aula.
 * 
 * @author Lisa
 *
 */
@Service
public class AulaService {
	private static final Logger LOGGER = Logger.getLogger(AulaService.class.getName());
	private IAulaRepository aulaRepository;

	public AulaService(IAulaRepository aulaRepository) {
		this.aulaRepository = aulaRepository;
	}

	/**
	 * Función que devuelve una lista con todas las aulas que hay en la BD.
	 * 
	 * @return Lista con todas las aulas que hay en la BD
	 */
	public List<Aula> findAll() {
		return aulaRepository.findAll();
	}

	/**
	 * Función que devuelve una lista con todas las aulas de las que es propietario
	 * el centro o departamento seleccionado.
	 * 
	 * @param filtroPropietarioAula Propietario del que se quieren obtener las aulas
	 * 
	 * @return Lista con todas las aulas de las que es propietario el centro o
	 *         departamento seleccionado
	 */
	public List<Aula> findAll(PropietarioAula filtroPropietarioAula) {
		if (filtroPropietarioAula == null) {
			return aulaRepository.findAll();
		} else {
			return aulaRepository.search(filtroPropietarioAula);
		}
	}

	/**
	 * Función que devuelve una lista con todas las aulas disponibles que hay en la
	 * BD que cumplen con los filtros aplicados.
	 * 
	 * @param fechaDesde     Fecha desde la que debe estar disponible el aula
	 * @param fechaHasta     Fecha hasta la que debe estar disponible el aula
	 * @param horaDesde      Hora desde la que debe estar disponible el aula
	 * @param horaHasta      Hora hasta la que debe estar disponible el aula
	 * @param capacidad      Capacidad mínima del aula
	 * @param numOrdenadores Número de ordenadores mínimo que debe tener el aula
	 * @param diaSemana      Día de la semana que debe estar disponible el aula
	 * @param propietario    Propietario del aula
	 * 
	 * @return Lista con todas las aulas disponibles que hay en la BD que cumplen
	 *         con los filtros aplicados
	 */
	public List<Aula> findAll(LocalDate fechaDesde, LocalDate fechaHasta, LocalTime horaDesde, LocalTime horaHasta,
			Integer capacidad, Integer numOrdenadores, String diaSemana, PropietarioAula propietario) {
		return aulaRepository.findAll(AulaSpecification.findByFilters(fechaDesde, fechaHasta, horaDesde, horaHasta,
				capacidad, numOrdenadores, diaSemana, propietario));
	}

	/**
	 * Función que elimina el aula pasada por parámetro de la BD.
	 * 
	 * @param aula Aula que se quiere eliminar
	 */
	public void delete(Aula aula) {
		aulaRepository.delete(aula);
	}

	/**
	 * Función que guarda el aula pasada por parámetro en la BD si no es null.
	 * 
	 * @param aula Aula que se quiere guardar
	 */
	public void save(Aula aula) {
		if (aula == null) {
			LOGGER.log(Level.SEVERE, "El aula que se quiere guardar es nulo.");
			return;
		}
		aulaRepository.save(aula);
	}

}
