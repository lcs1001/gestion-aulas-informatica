package gestionaulasinformatica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import gestionaulasinformatica.app.security.SecurityConfiguration;
import gestionaulasinformatica.backend.entity.Usuario;
import gestionaulasinformatica.backend.repository.IUsuarioRepository;
import gestionaulasinformatica.backend.service.UsuarioService;
import gestionaulasinformatica.ui.MainLayout;

/**
 * Punto de entrada a la aplicación Spring Boot.
 * 
 * Se deshabilita la configuración Spring MVC, interfiere con el funcionamiento
 * de Vaadin y puede causar un comportamiento extraño de recarga.
 */
@SpringBootApplication(scanBasePackageClasses = { SecurityConfiguration.class, MainLayout.class, Application.class,
		UsuarioService.class }, exclude = ErrorMvcAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = { IUsuarioRepository.class })
@EntityScan(basePackageClasses = { Usuario.class })
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
}
