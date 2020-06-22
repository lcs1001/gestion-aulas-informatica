package gestionaulasinformatica.backend.specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import gestionaulasinformatica.backend.entity.HistoricoReservas;

/**
 * Clase que contiene las especificaciones para filtrar el histórico de
 * reservas.
 * 
 * @author Lisa
 *
 */
public class HistoricoReservasSpecification {
	/**
	 * Función que devuelve todas las operaciones realizadas sobre las reservas que
	 * cumplen con los filtros aplicados.
	 * 
	 * @param fechaDesde Fecha (de inicio) desde la que obtener las reservas
	 * @param fechaHasta Fecha (de inicio) hasta la que obtener las reservas
	 * 
	 * @return Operaciones realizadas sobre las reservas que cumplen con los filtros
	 *         aplicados
	 */
	public static Specification<HistoricoReservas> findByFilters(LocalDate fechaDesde, LocalDate fechaHasta) {
		return new Specification<HistoricoReservas>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<HistoricoReservas> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				final List<Predicate> predicates = new ArrayList<>();

				// Se obtienen las operaciones realizadas desde una fecha determinada
				if (!StringUtils.isEmpty(fechaDesde)) {
					final Predicate fechaDesdePredicate = cb.greaterThanOrEqualTo(root.get("fechaOperacion"), fechaDesde);
					predicates.add(fechaDesdePredicate);
				}

				// Se obtienen las operaciones realizadas hasta una fecha determinada
				if (!StringUtils.isEmpty(fechaHasta)) {
					final Predicate fechaHastaPredicate = cb.lessThanOrEqualTo(root.get("fechaOperacion"), fechaHasta);
					predicates.add(fechaHastaPredicate);
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}
