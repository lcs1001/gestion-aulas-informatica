package gestionaulasinformatica.backend.specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import gestionaulasinformatica.backend.entity.Aula;
import gestionaulasinformatica.backend.entity.PropietarioAula;
import gestionaulasinformatica.backend.entity.Reserva;

/**
 * Clase que contiene las especificaciones para filtrar las aulas.
 * 
 * @author Lisa
 *
 */
public class AulaSpecification {
	/**
	 * Función que devuelve todas las aulas disponibles que hay en la BD que cumplen
	 * con los filtros aplicados.
	 * 
	 * @param fechaDesde     Fecha desde la que debe estar disponible el aula
	 * @param fechaHasta     Fecha hasta la que debe estar disponible el aula
	 * @param horaDesde      Hora desde la que debe estar disponible el aula
	 * @param horaHasta      Hora hasta la que debe estar disponible el aula
	 * @param capacidad      Capacidad mínima del aula
	 * @param numOrdenadores Número de ordenadores mínimo que debe tener el aula
	 * @param diaSemana      Día de la semana que debe estar disponible el aula
	 * @param propietario    Propietario del aula
	 * @param idAula         ID del aula que debe estar disponible
	 * @param idReserva      ID de la reserva que se está modificando, para
	 *                       comprobar la disponibilidad del aula sin tener en
	 *                       cuenta esta reserva
	 * 
	 * @return Aulas disponibles que cumplen con los filtros aplicados
	 */
	public static Specification<Aula> findByFilters(LocalDate fechaDesde, LocalDate fechaHasta, LocalTime horaDesde,
			LocalTime horaHasta, Integer capacidad, Integer numOrdenadores, String diaSemana,
			PropietarioAula propietarioAula, Integer idAula, Integer idReserva) {
		return new Specification<Aula>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Aula> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				final List<Predicate> predicates = new ArrayList<>();
				List<Predicate> predicatesSubconsulta = new ArrayList<>();

				// Se obtienen las aulas que no están presentes en reservas entre fechaDesde y
				// fechaHasta y entre horaDesde y horaHasta

				// SELECT a.*
				// FROM aula a
				// WHERE id_aula NOT IN (
				// SELECT id_aula
				// FROM reserva r
				// WHERE a.id_aula = r.id_aula AND
				// r.fecha BETWEEN fechaDesde AND fechaHasta AND
				// (horaHasta > r.hora_inicio AND horaDesde < r.hora_fin)
				// )
				if (!StringUtils.isEmpty(fechaDesde) && !StringUtils.isEmpty(fechaDesde) && !StringUtils.isEmpty(horaDesde)
						&& !StringUtils.isEmpty(horaHasta)) {

					Subquery<Long> subquery = query.subquery(Long.class);
					Root<Reserva> subRoot = subquery.from(Reserva.class);
					subquery.select(subRoot.get("aula").get("idAula"));

					// reserva.fecha BETWEEN fechaDesde AND fechaHasta
					Predicate fechaReservaEntre = cb.between(subRoot.get("fecha"), fechaDesde, fechaHasta);

					// horaHasta > reserva.horaInicio AND horaDesde < reserva.horaFin
					Predicate horaHastaMayorInicio = cb.lessThan(subRoot.get("horaInicio"), horaHasta);
					Predicate horaDesdeMenorFin = cb.greaterThan(subRoot.get("horaFin"), horaDesde);

					Predicate andHoras = cb.and(horaHastaMayorInicio, horaDesdeMenorFin);
					Predicate andFechaHoras = cb.and(fechaReservaEntre, andHoras);
					
					predicatesSubconsulta.add(andFechaHoras);

					// Si se ha filtrado por día de la semana, se añade el AND a la subconsulta
					if (!StringUtils.isEmpty(diaSemana)) {
						// r.diaSemana = diaSemana
						Predicate diaSemanaP = cb.equal(subRoot.get("diaSemana"), diaSemana);						
						predicatesSubconsulta.add(diaSemanaP);
					}
//						Predicate andFechaHorasDia = cb.and(andFechaHoras, diaSemanaP);
//
//						predicatesSubconsulta.add(andFechaHorasDia);
//					} else {
//						predicatesSubconsulta.add(andFechaHoras);
//					}
					
					// Si se ha pasado el ID de la reserva, se añade el AND a la subconsulta
					if(!StringUtils.isEmpty(idReserva)) {
						Predicate idReservaP = cb.notEqual(subRoot.get("idReserva"), idReserva);						
						predicatesSubconsulta.add(idReservaP);
					}

					subquery.where(cb.and(predicatesSubconsulta.toArray(new Predicate[0])));

					Predicate fechaHoraPredicate = cb.in(root.get("idAula")).value(subquery).not();

					predicates.add(fechaHoraPredicate);
				}

				// Se obtienen las aulas que tienen una capacidad mayor o igual
				if (!StringUtils.isEmpty(capacidad)) {
					final Predicate capacidadPredicate = cb.greaterThanOrEqualTo(root.get("capacidad"), capacidad);
					predicates.add(capacidadPredicate);
				}

				// Se obtienen las aulas que tienen un número de ordenadores mayor o igual
				if (!StringUtils.isEmpty(numOrdenadores)) {
					final Predicate numOrdenadoresPredicate = cb.greaterThanOrEqualTo(root.get("numOrdenadores"),
							numOrdenadores);
					predicates.add(numOrdenadoresPredicate);
				}

				// Se obtienen las aulas de un determinado centro o departamento
				// (propietario del aula)
				if (!StringUtils.isEmpty(propietarioAula)) {
					final Predicate propietarioAulaPredicate = cb.equal(root.get("propietarioAula"), propietarioAula);
					predicates.add(propietarioAulaPredicate);
				}

				// Se obtiene el aula con el ID pasado
				if (!StringUtils.isEmpty(idAula)) {
					final Predicate idAulaPredicate = cb.equal(root.get("idAula"), idAula);
					predicates.add(idAulaPredicate);
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}
