package gestionaulasinformatica.backend;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import gestionaulasinformatica.backend.entity.Aula;
import gestionaulasinformatica.backend.entity.Centro;
import gestionaulasinformatica.backend.entity.Departamento;
import gestionaulasinformatica.backend.entity.PropietarioAula;
import gestionaulasinformatica.backend.entity.Reserva;
import gestionaulasinformatica.backend.repository.IAulaRepository;
import gestionaulasinformatica.backend.specification.AulaSpecification;

/**
 * Clase para testear la Consulta de Disponibilidad de Aulas.
 * 
 * @author Lisa
 *
 */
@DataJpaTest
public class ConsultaAulasTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IAulaRepository aulaRepository;

	private PropietarioAula centro1;
	private PropietarioAula dpto1;
	private Aula aula1;
	private Aula aula2;
	private Reserva reserva1;
	private Reserva reserva2;
	private Reserva reserva3;

	private List<Aula> lstAulasDisponibles;

	/**
	 * Se crean los datos de prueba, que se revierten y se recrean para cada test.
	 */
	public void establecerDatos() {
		try {
			centro1 = new Centro("Centro 1", "Centro 1", "Responsable", "Centro 1", "rspCentro1@gmail.com",
					"547854126");
			dpto1 = new Departamento("DPTO 1", "Departamento 1", "Responsable", "Departamento 1", "rspDpto1@gmail.com",
					"247863221");

			entityManager.persist(centro1);
			entityManager.persist(dpto1);

			aula1 = new Aula("Aula 1", 50, 25, centro1, dpto1);
			aula2 = new Aula("Aula 2", 100, 0, centro1, dpto1);

			entityManager.persist(aula1);
			entityManager.persist(aula2);

			reserva1 = new Reserva(LocalDate.of(2020, 07, 20), LocalTime.of(11, 00), LocalTime.of(12, 00), "Lunes",
					aula1, "Examen 1", "Persona 1", dpto1);
			reserva2 = new Reserva(LocalDate.of(2020, 07, 21), LocalTime.of(11, 00), LocalTime.of(12, 00), "Martes",
					aula1, "Examen 2", "Persona 2", dpto1);
			reserva3 = new Reserva(LocalDate.of(2020, 07, 22), LocalTime.of(11, 00), LocalTime.of(12, 00), "Miércoles",
					aula1, "Examen 3", "Persona 3", dpto1);

			entityManager.persist(reserva1);
			entityManager.persist(reserva2);
			entityManager.persist(reserva3);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test que comprueba que el aula 1 no está disponible el día 21-07-2020 de
	 * 10:30 a 11:30.
	 */
	@Test
	public void aula1NoDisponibleFechaHoras() {
		try {
			establecerDatos();
			
			lstAulasDisponibles = aulaRepository.findAll(AulaSpecification.findByFilters(LocalDate.of(2020, 07, 21), LocalDate.of(2020, 07, 21),
					LocalTime.of(10, 30), LocalTime.of(11, 30), null, null, null, null));
			
			System.out.println("\n\nTest Aula 1 no disponible el 21-07-2020 de 10:30 a 11:30:");
			System.out.println("\tAulas disponibles: " + lstAulasDisponibles+ "\n\n");
			
			Assert.assertTrue(!lstAulasDisponibles.contains(aula1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test que comprueba que el aula 1 no está disponible el día 21-07-2020 de
	 * 10:30 a 11:30, añadiendo el filtro de diaSemana = "Martes".
	 */
	@Test
	public void aula1NoDisponibleFechaHorasDiaSemana() {
		try {
			establecerDatos();
			
			lstAulasDisponibles = aulaRepository.findAll(AulaSpecification.findByFilters(LocalDate.of(2020, 07, 21), LocalDate.of(2020, 07, 21),
					LocalTime.of(10, 30), LocalTime.of(11, 30), null, null, "Martes", null));
			
			System.out.println("\n\nTest Aula 1 no disponible el 21-07-2020 de 10:30 a 11:30 (con filtro día de la semana):");
			System.out.println("\tAulas disponibles: " + lstAulasDisponibles + "\n\n");
			
			Assert.assertTrue(!lstAulasDisponibles.contains(aula1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test que comprueba que el aula 1 no está disponible del día 21-07-2020 al 23-07-2020 de
	 * 10:30 a 11:30.
	 */
	@Test
	public void aula1NoDisponibleRangoFechasHoras() {
		try {
			establecerDatos();
			
			lstAulasDisponibles = aulaRepository.findAll(AulaSpecification.findByFilters(LocalDate.of(2020, 07, 21), LocalDate.of(2020, 07, 23),
					LocalTime.of(10, 30), LocalTime.of(11, 30), null, null, null, null));
			
			System.out.println("\n\nTest Aula 1 no disponible del 21-07-2020 al 23-07-2020 de 10:30 a 11:30:");
			System.out.println("\tAulas disponibles: " + lstAulasDisponibles + "\n\n");
			
			Assert.assertTrue(!lstAulasDisponibles.contains(aula1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
