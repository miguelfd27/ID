package gei.id.tutelado;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ P01_Curso.class, P02_Soldado.class, P03_Instructor.class, P04_EagerLazyPersist.class, P05_Consultas.class} )
public class AllTests {

}
