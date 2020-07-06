package gestionaulasinformatica.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import gestionaulasinformatica.backend.entity.Usuario;
import gestionaulasinformatica.backend.repository.IUsuarioRepository;

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

	private final UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public SecurityConfiguration(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Codificador de contraseña para encriptar las contraseñas.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CurrentUser currentUser(IUsuarioRepository usuarioRepository) {
		final String username = SecurityUtils.getUsername();
		Usuario usuario = username != null ? usuarioRepository.findByCorreoUsuarioIgnoreCase(username) : null;
		return () -> usuario;
	}

	/**
	 * REgistra el UserDetailsService propio y el codificador de contraseñas para
	 * usar en el login.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	/**
	 * Función que establece como necesario el inicio de sesión para acceder a
	 * páginas internas y configura el formulario de inicio de sesión.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// No se usa Spring CSRF para poder usar HTML simple para acceder a la ventana
		// de inicio de sesión
		http.csrf().disable()

				// Registra el CustomRequestCache, que guarda los intentos de acceso no
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

				// Registra el controlador de éxito que redirige a los usuarios a la página a la
				// que intentaron acceder por última vez
				.successHandler(new SavedRequestAwareAuthenticationSuccessHandler())

				// Configura el logout
				.and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
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

				// (development mode) static resources
				"/frontend/**",

				// (development mode) webjars
				"/webjars/**",

				// (development mode) H2 debugging console
				"/h2-console/**");
	}
}