package com.vaadin.gestionaulasinformatica.backend.specification;

import java.time.*;
import java.util.*;

import javax.persistence.criteria.*;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.vaadin.gestionaulasinformatica.backend.entity.*;

/**
 * Clase que contiene las especificaciones para filtrar las reservas.
 * 
 * @author Lisa
 *
 */
public class ReservaSpecification {

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
	public static Specification<Reserva> findByFilters(LocalDate fechaDesde, LocalDate fechaHasta, LocalTime horaDesde,
			LocalTime horaHasta, String propietarioAula) {
		return new Specification<Reserva>() {

			@Override
			public Predicate toPredicate(Root<Reserva> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				final List<Predicate> predicates = new ArrayList<>();

				// Se obtienen las reservas realizadas desde una fecha (de incio) determinada
				if (!StringUtils.isEmpty(fechaDesde)) {
					final Predicate fechaDesdePredicate = cb.greaterThanOrEqualTo(root.get("fechaInicio"), fechaDesde);
					predicates.add(fechaDesdePredicate);
				}

				// Se obtienen las reservas realizadas hasta una fecha (de inicio) determinada
				if (!StringUtils.isEmpty(fechaHasta)) {
					final Predicate fechaHastaPredicate = cb.lessThanOrEqualTo(root.get("fechaInicio"), fechaHasta);
					predicates.add(fechaHastaPredicate);
				}

				// Se obtienen las reservas realizadas a partir de una hora (de inicio) del día
				if (!StringUtils.isEmpty(horaDesde)) {
					final Predicate horaDesdePredicate = cb.greaterThanOrEqualTo(root.get("horaInicio"), horaDesde);
					predicates.add(horaDesdePredicate);
				}

				// Se obtienen las reservas realizadas hasta una hora (de inicio) del día
				if (!StringUtils.isEmpty(horaHasta)) {
					final Predicate horaHastaPredicate = cb.lessThanOrEqualTo(root.get("horaInicio"), horaHasta);
					predicates.add(horaHastaPredicate);
				}

				// Se obtienen las reservas realizadas de un determinado centro o departamento
				// (propietario del aula de la reserva)
				if (!StringUtils.isEmpty(propietarioAula)) {
					final Join<Reserva, Aula> aula = root.join("aula");
					final Join<PropietarioAula, Aula> propietarioAula = aula.join("propietarioAula");

					final Predicate propietarioAulaPredicate = cb.equal(propietarioAula.get("nombrePropietarioAula"),
							propietarioAula);
					query.distinct(true);
					predicates.add(propietarioAulaPredicate);
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}
