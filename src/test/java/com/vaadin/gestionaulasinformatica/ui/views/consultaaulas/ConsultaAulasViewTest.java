package com.vaadin.gestionaulasinformatica.ui.views.consultaaulas;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.vaadin.gestionaulasinformatica.ui.views.consultaaulas.ConsultaAulasView;

/**
 * Clase para testear la ventana Consulta de Reservas.
 * 
 * Con las anotaciones @ExtendWith y @SpringBootTest aseguran que la aplicación
 * Spring Boot se inicialice antes de ejecutar las pruebas y permiten
 * utilizar @Autowire en la prueba.
 * 
 * @author Lisa
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ConsultaAulasViewTest {
	@Autowired
	private ConsultaAulasView consultaAulasView;

	@Test
	public void formShownWhenContactSelected() {
	}
}
