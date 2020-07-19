package gestionaulasinformatica.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gestionaulasinformatica.backend.entity.Aula;
import gestionaulasinformatica.backend.entity.Reserva;

/**
 * Repositorio para la entidad Reserva con clave primaria de tipo Integer.
 * 
 * @author Lisa
 *
 */
public interface IReservaRepository extends JpaRepository<Reserva, Integer>, JpaSpecificationExecutor<Reserva> {

	@Query("SELECT r FROM Reserva r WHERE r.aula = :aulaP AND r.fecha >= :fechaP")
	List<Reserva> findAllReservasAulaAndFechaDesde(@Param("aulaP") Aula aula, @Param("fechaP") LocalDate fecha);

}
