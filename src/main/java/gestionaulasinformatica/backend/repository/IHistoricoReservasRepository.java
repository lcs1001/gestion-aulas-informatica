package gestionaulasinformatica.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import gestionaulasinformatica.backend.entity.HistoricoReservas;
import gestionaulasinformatica.backend.entity.HistoricoReservasPK;

/**
 * Repositorio para la entidad HistoricoReservas con clave primaria de tipo
 * Integer.
 * 
 * @author Lisa
 *
 */
public interface IHistoricoReservasRepository
		extends JpaRepository<HistoricoReservas, HistoricoReservasPK>, JpaSpecificationExecutor<HistoricoReservas> {
}
