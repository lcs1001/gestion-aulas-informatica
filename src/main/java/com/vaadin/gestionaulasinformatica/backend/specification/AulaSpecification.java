package com.vaadin.gestionaulasinformatica.backend.specification;

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

import com.vaadin.gestionaulasinformatica.backend.entity.Aula;
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.entity.Reserva;

/**
 * Clase que contiene las especificaciones para filtrar las aulas.
 * 
 * @author Lisa
 *
 */
public class AulaSpecification {
	/**
	 * Función que devuelve todas las reservas que cumplen con los filtros
	 * aplicados.
	 * 
	 * @param fechaDesde      Fecha (de inicio) desde la que obtener las reservas
	 * @param fechaHasta      Fecha (de inicio) hasta la que obtener las reservas
	 * @param horaDesde       Hora (de inicio) de la reserva desde la que obtener
	 *                        las reservas
	 * @param horaHasta       Hora (de inicio) de la reserva hasta la que obtener
	 *                        las reservas
	 * @param propietarioAula Propietario del aula de la reserva del que obtener las
	 *                        reservas
	 * @return Reservas que cumplen con los filtros aplicados
	 */
	public static Specification<Aula> findByFilters(LocalDate fechaDesde, LocalDate fechaHasta, LocalTime horaDesde,
			LocalTime horaHasta, Integer capacidad, Integer numOrdenadores, PropietarioAula propietarioAula) {
		return new Specification<Aula>() {

			@Override
			public Predicate toPredicate(Root<Aula> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				final List<Predicate> predicates = new ArrayList<>();
				List<Predicate> predicatesSubconsulta = new ArrayList<>();

				// Se obtienen las aulas que no están presentes en reservas entre fechaDesde y
				// fechaHasta y entre horaDesde y horaHasta
				if (!StringUtils.isEmpty(fechaDesde) && !StringUtils.isEmpty(horaDesde)
						&& !StringUtils.isEmpty(horaHasta)) {

					Subquery<Long> subquery = query.subquery(Long.class);
					Root<Reserva> subRoot = subquery.from(Reserva.class);
					subquery.select(subRoot.get("aula").get("idAula"));

					// reserva.fecha BETWEEN fechaDesde AND fechaHasta
					Predicate fechaReservaEntre = cb.between(subRoot.get("fecha"), fechaDesde, fechaHasta);

					// horaDesde BETWEEN reserva.horaInicio AND reserva.horaFin
					Predicate horaDesdeEntre = cb.between(cb.literal(horaDesde), subRoot.get("horaInicio"),
							subRoot.get("horaFin"));

					// horaHasta BETWEEN reserva.horaInicio AND reserva.horaFin
					Predicate horaHastaEntre = cb.between(cb.literal(horaHasta), subRoot.get("horaInicio"),
							subRoot.get("horaFin"));
					
					// horaDesde <= reserva.horaInicio
					Predicate horaDesdeMenor = cb.greaterThanOrEqualTo(subRoot.get("horaInicio"), horaDesde);
					
					// horaHasta >= reserva.horaFin
					Predicate horaHastaMayor = cb.lessThanOrEqualTo(subRoot.get("horaFin"), horaHasta);

					Predicate and1 = cb.and(horaDesdeMenor, horaHastaMayor);
					Predicate or = cb.or(horaDesdeEntre, horaHastaEntre, and1);		
					Predicate and2 = cb.and(fechaReservaEntre, or);
					
					predicatesSubconsulta.add(and2);
					subquery.where(predicatesSubconsulta.toArray(new Predicate[0]));
					
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

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}
