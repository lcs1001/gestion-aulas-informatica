package gestionaulasinformatica.backend;

import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import gestionaulasinformatica.backend.data.Rol;
import gestionaulasinformatica.backend.entity.Usuario;
import gestionaulasinformatica.backend.repository.IUsuarioRepository;

/**
 * Clase para testear la entidad Usuario, su Service y su Repository.
 * 
 * @author Lisa
 *
 */
@DataJpaTest
public class UsuarioTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IUsuarioRepository usuarioRepository;

	private Usuario usuario;

	/**
	 * Se crean los datos de prueba, que se revierten y se recrean para cada test.
	 */
	public void establecerDatos() {
		try {			
			usuario = new Usuario("usuario@gmail.com", "1234", "Usuario", "Test", "547854126",
					Rol.RESPONSABLE);

			entityManager.persist(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test que comprueba el método findById().
	 */
	@Test
	public void findByIdTest() {
		Optional<Usuario> usuarioRecuperado;
		try {
			establecerDatos();

			usuarioRecuperado = usuarioRepository.findById(1);

			System.out.println("\n\nTest findById:");

			if (usuarioRecuperado.isPresent()) {
				if (usuarioRecuperado.get().equals(usuario)) {
					System.out.println("\tSe ha recuperado el usuario " + usuarioRecuperado.get().getNombreUsuario()
							+ " " + usuarioRecuperado.get().getApellidosUsuario() + " correctamente.");
				} else {
					System.out.println("\tEl usuario recuperado (" + usuarioRecuperado.get().getNombreUsuario() + " "
							+ usuarioRecuperado.get().getApellidosUsuario() + ") no se corresponde con el buscado ("
							+ usuario.getNombreUsuario() + " " + usuario.getApellidosUsuario() + ".");
				}
			} else {
				System.out.println("\tNo se ha encontrado el propietario de aula.");
			}

			Assert.assertEquals(usuarioRecuperado.get(), usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
