package gestionaulasinformatica.backend;

import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import gestionaulasinformatica.backend.data.Rol;
import gestionaulasinformatica.backend.entity.Centro;
import gestionaulasinformatica.backend.entity.PropietarioAula;
import gestionaulasinformatica.backend.entity.Usuario;
import gestionaulasinformatica.backend.repository.IPropietarioAulaRepository;
import gestionaulasinformatica.backend.repository.IUsuarioRepository;

/**
 * Clase para testear la entidad PropietarioAula, su Service y su Repository.
 * 
 * @author Lisa
 *
 */
@DataJpaTest
public class PropietarioAulaTest {
	
	@Autowired IUsuarioRepository usuarioRepository;

	@Autowired
	private IPropietarioAulaRepository propietarioAulaRepository;
	
	private Usuario responsable;
	private PropietarioAula centro1;

	/**
	 * Se crean los datos de prueba, que se revierten y se recrean para cada test.
	 */
	public void establecerDatos() {
		try {
			responsable = new Usuario("rspCentro1@gmail.com", "12345", "Responsable", "Centro 1", "547854126",
					Rol.RESPONSABLE);
			
			usuarioRepository.save(responsable);
			
			centro1 = new Centro("Centro 1", "Centro 1", responsable);
			
			propietarioAulaRepository.save(centro1);

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
							+ propietarioRecuperado.get().getNombrePropietarioAula() + " correctamente.\n");
				} else {
					System.out.println("\tEl propietario recuperado ("
							+ propietarioRecuperado.get().getNombrePropietarioAula()
							+ ") no se corresponde con el buscado (" + centro1.getNombrePropietarioAula() + ".\n");
				}
			} else {
				System.out.println("\tNo se ha encontrado el propietario de aula.\n");
			}

			Assert.assertEquals(propietarioRecuperado.get(), centro1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test que comprueba el método delete(), que se elimina correctamente el centro
	 * 1 del repositorio.
	 */
	@Test
	public void deleteCentro1Test() {
		List<PropietarioAula> propietarios;
		try {
			establecerDatos();

			System.out.println("\n\nTest delete:");

			// Se recuperan todos los propietarios antes de eliminar
			propietarios = propietarioAulaRepository.findAll();

			System.out
					.println("\n\tPropietarios recuperados con findAll antes de eliminar: " + propietarios.toString());

			System.out.println("\tEliminando el Centro 1...:");

			// Se elimina el propietario
			propietarioAulaRepository.delete(centro1);

			// Se recuperan todos los propietarios después de eliminar
			propietarios = propietarioAulaRepository.findAll();

			// Si el propietario eliminado (Centro 1) está en la lista de propietarios
			// recuperados tras eliminar, el método delete no está funcionando
			if (propietarios.contains(centro1)) {
				System.out.println("\n\tNo se ha eliminado el propietario " + centro1.getNombrePropietarioAula()
						+ " del repositorio.");
				System.out.println(
						"\n\tPropietarios recuperados con findAll después de eliminar: " + propietarios.toString() + "\n");
			} else {
				System.out.println("\n\tSe ha eliminado el propietario " + centro1.getNombrePropietarioAula()
						+ " del repositorio correctamente.");
				System.out.println(
						"\n\tPropietarios recuperados con findAll después de eliminar: " + propietarios.toString()  + "\n");
			}

			Assert.assertFalse(propietarios.contains(centro1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
