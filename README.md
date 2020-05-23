# Proyecto Gestión Aulas Informática con Vaadin and Spring Boot

## Ejecutar la aplicación
Hay dos formas de ejecutar la aplicación:
- Ejecutando `mvn spring-boot:run` en la consola de comandos (cmd) 
- Ejecutando (Run) la clase `Application` desde el IDE.

#### Eclipse
- Click derecho en la carpeta del proyecto seleccionando `Run As` --> `Maven build..`, después de esto la ventana de configuración se abrirá.
- En la ventana poner hay que establecer el valor de **Goals** como `spring-boot:run`.
- Opcionalmente se puede seleccionar la opción `Skip tests`.
- Todas las demás configuraciones se pueden dejar como por defecto.

Una vez configurado, se puede lanzar la aplicación clicando en `Run`.

Después de esto, se puede ver en http://localhost:8080/ en el explorador.

Se puede ejecutar la aplicación de forma local en modo de producción con el comando `spring-boot:run -Pproduction`.

### Running Integration Tests

Integration tests are implemented using [Vaadin TestBench](https://vaadin.com/testbench). The tests take a few minutes to run and are therefore included in a separate Maven profile. To run the tests using Google Chrome, execute

`mvn verify -Pit`

and make sure you have a valid TestBench license installed. If the tests fail because of an old Chrome Driver or you want to use a different browser, you'll need to update the `webdrivers.xml` file in the project root.

Profile `it` adds the following parameters to run integration tests:
```sh
-Dwebdriver.chrome.driver=path_to_driver
-Dcom.vaadin.testbench.Parameters.runLocally=chrome
```

If you would like to run a separate test make sure you have added these parameters to VM Options of JUnit run configuration

## Project overview

Project follow the Maven's [standard directory layout structure](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html):
- Under the `srs/main/java` are located Application sources
   - `Application.java` is a runnable Java application class and a starting point
   - `GreetService.java` is a  Spring service class
   - `MainView.java` is a default view and entry point of the application
- Under the `srs/test` are located test files
- `src/main/resources` contains configuration files and static resources
- The `frontend` directory in the root folder contains client-side dependencies and resource files
   - All CSS styles used by the application are located under the root directory `frontend/styles`    
   - Templates would be stored under the `frontend/src`


## More Information and Next Steps

- Vaadin Basics [https://vaadin.com/docs](https://vaadin.com/docs)
- More components at [https://vaadin.com/components](https://vaadin.com/components) and [https://vaadin.com/directory](https://vaadin.com/directory)
- Download this and other examples at [https://vaadin.com/start](https://vaadin.com/start)
- Using Vaadin and Spring [https://vaadin.com/docs/v14/flow/spring/tutorial-spring-basic.html](https://vaadin.com/docs/v14/flow/spring/tutorial-spring-basic.html) article
- Join discussion and ask a question at [https://vaadin.com/forum](https://vaadin.com/forum)


## Notes

If you run application from a command line, remember to prepend a `mvn` to the command.
