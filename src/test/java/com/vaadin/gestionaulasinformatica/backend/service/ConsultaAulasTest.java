package com.vaadin.gestionaulasinformatica.backend.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop;
import com.vaadin.gestionaulasinformatica.Application;
import com.vaadin.gestionaulasinformatica.backend.entity.Aula;
import com.vaadin.gestionaulasinformatica.backend.entity.Centro;
import com.vaadin.gestionaulasinformatica.backend.entity.Departamento;
import com.vaadin.gestionaulasinformatica.backend.entity.PropietarioAula;
import com.vaadin.gestionaulasinformatica.backend.entity.Reserva;
import com.vaadin.gestionaulasinformatica.backend.repository.IAulaRepository;
import com.vaadin.gestionaulasinformatica.backend.repository.IPropietarioAulaRepository;
import com.vaadin.gestionaulasinformatica.backend.repository.IReservaRepository;
import com.vaadin.gestionaulasinformatica.backend.service.AulaService;
import com.vaadin.gestionaulasinformatica.backend.service.PropietarioAulaService;
import com.vaadin.gestionaulasinformatica.backend.service.ReservaService;
import com.vaadin.gestionaulasinformatica.backend.specification.AulaSpecification;

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
	public void aula1NoDisponible() {
		List<Aula> lstAulasDisponiblesExpected;
		try {
			establecerDatos();
			
			System.out.println("Test Aula 1 no disponible del 21-07-2020 de 10:30 a 11:30");
			lstAulasDisponiblesExpected = new ArrayList<Aula>();
			lstAulasDisponiblesExpected.add(aula2);
			
			lstAulasDisponibles = aulaRepository.findAll(AulaSpecification.findByFilters(LocalDate.of(2020, 07, 21), LocalDate.of(2020, 07, 21),
					LocalTime.of(10, 30), LocalTime.of(11, 30), null, null, null, null));
			
			Assert.assertEquals(lstAulasDisponiblesExpected,lstAulasDisponibles);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
