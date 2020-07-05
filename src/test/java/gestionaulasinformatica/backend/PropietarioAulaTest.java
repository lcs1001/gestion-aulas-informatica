package gestionaulasinformatica.backend;

import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import gestionaulasinformatica.backend.data.Rol;
import gestionaulasinformatica.backend.entity.Centro;
import gestionaulasinformatica.backend.entity.PropietarioAula;
import gestionaulasinformatica.backend.entity.Usuario;
import gestionaulasinformatica.backend.repository.IPropietarioAulaRepository;

/**
 * Clase para testear la entidad PropietarioAula, su Service y su Repository.
 * 
 * @author Lisa
 *
 */
@DataJpaTest
public class PropietarioAulaTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IPropietarioAulaRepository propietarioAulaRepository;

	private Usuario responsable;
	private PropietarioAula centro1;

	/**
	 * Se crean los datos de prueba, que se revierten y se recrean para cada test.
	 */
	public void establecerDatos() {
		try {
			responsable = new Usuario("rspCentro1@gmail.com", "1234", "Responsable", "Centro 1", "547854126", Rol.RESPONSABLE);
			centro1 = new Centro("Centro 1", "Centro 1", responsable);
			
			entityManager.persist(responsable);
			entityManager.persist(centro1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test que comprueba el método findById().
	 */
	@Test
	public void findByIdTest() {
		Optional<PropietarioAula> propietarioRecuperado;
		try {
			establecerDatos();

			propietarioRecuperado = propietarioAulaRepository.findById("Centro 1");

			System.out.println("\n\nTest findById:");

			if (propietarioRecuperado.isPresent()) {
				if (propietarioRecuperado.get().equals(centro1)) {
					System.out.println("\tSe ha recuperado el propietario "
							+ propietarioRecuperado.get().getNombrePropietarioAula() + " correctamente.");
				} else {
					System.out.println("\tEl propietario recuperado ("
							+ propietarioRecuperado.get().getNombrePropietarioAula()
							+ ") no se corresponde con el buscado (" + centro1.getNombrePropietarioAula() + ".");
				}
			} else {
				System.out.println("\tNo se ha encontrado el propietario de aula.");
			}

			Assert.assertEquals(propietarioRecuperado.get(), centro1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
