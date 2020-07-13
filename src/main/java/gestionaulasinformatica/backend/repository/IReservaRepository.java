package gestionaulasinformatica.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import gestionaulasinformatica.backend.entity.Aula;
import gestionaulasinformatica.backend.entity.Reserva;

/**
 * Repositorio para la entidad Reserva con clave primaria de tipo Integer.
 * 
 * @author Lisa
 *
 */
public interface IReservaRepository extends JpaRepository<Reserva, Integer>, JpaSpecificationExecutor<Reserva> {

	List<Reserva> findByAulaAndFecha(Aula aula, LocalDate fecha);

}
