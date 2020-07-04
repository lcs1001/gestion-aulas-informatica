package gestionaulasinformatica.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Clase de configuración de seguridad.
 * 
 * La anotación "@EnableWebSecurity" aplica Spring Security a la aplicación y
 * "@Configurarion" indica a Spring Boot que use esta clase para configurar la
 * seguridad.
 * 
 * @author Lisa
 *
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String LOGIN_PROCESSING_URL = "/login";
	private static final String LOGIN_FAILURE_URL = "/login?error";
	private static final String LOGIN_URL = "/login";
	private static final String LOGOUT_SUCCESS_URL = "/login";

	/**
	 * Función que establece como necesario el inicio de sesión para acceder a
	 * páginas internas y configura el formulario de inicio de sesión.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// No se usa Spring CSRF para poder usar HTML simple para acceder a la ventana
		// de inicio de sesión
		http.csrf().disable()

				// Registra el CustomRequestCache, que gaurda los intentos de acceso no
				// autorizados para redirigir al usuario después de iniciar sesión
				.requestCache().requestCache(new CustomRequestCache())

				// Restringe el acceso a la aplicación
				.and().authorizeRequests()

				// Permite todas las solicitudes internas de Vaadin
				.requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

				// Permite todas las solicitudes de usuarios logeados
				.anyRequest().authenticated()

				// Configura la ventana de login
				.and().formLogin().loginPage(LOGIN_URL).permitAll().loginProcessingUrl(LOGIN_PROCESSING_URL)
				.failureUrl(LOGIN_FAILURE_URL)

				// Configura el logout
				.and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		UserDetails user = User.withUsername("user").password("{noop}password").roles("USER").build();

		return new InMemoryUserDetailsManager(user);
	}

	/**
	 * Función que permite acceso a recursos estáticos, evitanto la seguridad de
	 * Spring.
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(
				// Client-side JS
				"/VAADIN/**",

				// the standard favicon URI
				"/favicon.ico",

				// the robots exclusion standard
				"/robots.txt",

				// web application manifest
				"/manifest.webmanifest", "/sw.js", "/offline.html",

				// icons and images
				"/icons/**", "/images/**", "/styles/**",

				// (development mode) H2 debugging console
				"/h2-console/**");
	}
}