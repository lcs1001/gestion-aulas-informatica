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
	 * Función que devuelve todas las aulas disponibles que hay en la BD que cumplen
	 * con los filtros aplicados.
	 * 
	 * @param fechaDesde     Fecha desde la que debe estar disponible el aula
	 * @param fechaHasta     Fecha hasta la que debe estar disponible el aula
	 * @param horaDesde      Hora desde la que debe estar disponible el aula
	 * @param horaHasta      Hora hasta la que debe estar disponible el aula
	 * @param capacidad      Capacidad mínima del aula
	 * @param numOrdenadores Número de ordenadores mínimo que debe tener el aula
	 * @param propietario    Propietario del aula
	 * 
	 * @return Aulas disponibles que cumplen con los filtros aplicados
	 */
	public static Specification<Aula> findByFilters(LocalDate fechaDesde, LocalDate fechaHasta, LocalTime horaDesde,
			LocalTime horaHasta, Integer capacidad, Integer numOrdenadores, PropietarioAula propietarioAula) {
		return new Specification<Aula>() {

			private static final long serialVersionUID = 1L;

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
