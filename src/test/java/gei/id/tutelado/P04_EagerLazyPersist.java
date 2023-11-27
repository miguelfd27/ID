package gei.id.tutelado;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.CursoDao;
import gei.id.tutelado.dao.CursoDaoJPA;
import gei.id.tutelado.dao.PersonaDao;
import gei.id.tutelado.dao.PersonaDaoJPA;
import gei.id.tutelado.model.Curso;
import gei.id.tutelado.model.Instructor;
import gei.id.tutelado.model.Soldado;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)


public class P04_EagerLazyPersist {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProdutorDatosProba produtorDatos = new ProdutorDatosProba();

    private static Configuracion cfg;
    private static CursoDao cursDao;
    private static PersonaDao persDao;


    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            log.info("");
            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            log.info("Iniciando test: " + description.getMethodName());
            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        }

        protected void finished(Description description) {
            log.info("");
            log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
            log.info("Finalizado test: " + description.getMethodName());
            log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
        }
    };

    @BeforeClass
    public static void init() throws Exception {
        cfg = new ConfiguracionJPA();
        cfg.start();

        cursDao = new CursoDaoJPA();
        cursDao.setup(cfg);

        persDao = new PersonaDaoJPA();
        persDao.setup(cfg);

        produtorDatos = new ProdutorDatosProba();
        produtorDatos.Setup(cfg);
    }

    @AfterClass
    public static void endclose() throws Exception {
        cfg.endUp();
    }

    @Before
    public void setUp() throws Exception {
        log.info("");
        log.info("Limpando BD --------------------------------------------------------------------------------------------");
        produtorDatos.limpaBD();
    }


    @Test
    public void test06_EAGER() {

        Curso c;
        Boolean excepcion;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        produtorDatos.creaCursosSueltos();
        produtorDatos.creaInstructoresNuevos();
        produtorDatos.guardaInstructores();
        produtorDatos.creaSoldadosNuevos();
        produtorDatos.s0.agregarCurso(produtorDatos.c0);

        Instructor instructor = produtorDatos.i0;
        produtorDatos.c0.setInstructor(instructor);
        Instructor instructor2 = produtorDatos.i1;
        produtorDatos.c1.setInstructor(instructor2);

        produtorDatos.guardaCursos();

        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba da recuperación de propiedades EAGER\n");

        log.info("Probando (que non hai excepcion tras) acceso inicial a propiedade EAGER fora de sesion ----------------------------------------");

        c = cursDao.recuperaPorId(produtorDatos.c0.getIdCurso());
        Soldado s = (Soldado) persDao.recuperaPorId(produtorDatos.s0.getDni());

        log.info("Acceso a curso");

        try {
            Assert.assertEquals(produtorDatos.i0, c.getInstructor());
            Assert.assertEquals(1, s.getCursos().size());

            excepcion = false;
        }
        catch (LazyInitializationException ex) {
            excepcion = true;
            log.info(ex.getClass().getName());
        }

        Assert.assertFalse(excepcion);
    }


    @Test
    public void test05_Propagacion_Persist() {

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        produtorDatos.creaCursosSueltos();
        produtorDatos.creaInstructoresNuevos();
        produtorDatos.guardaInstructores();
        produtorDatos.creaSoldadosNuevos();
        produtorDatos.s0.agregarCurso(produtorDatos.c0);
        produtorDatos.s1.agregarCurso(produtorDatos.c0);


        Instructor instructor = produtorDatos.i0;
        produtorDatos.c0.setInstructor(instructor);


        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba da gravación de novo curso con soldados (novos) asociados\n");

        Assert.assertNull(produtorDatos.c0.getId());
        Assert.assertNull(produtorDatos.s0.getId());
        Assert.assertNull(produtorDatos.s1.getId());

        log.info("Gravando na BD curso soldados -----------------------------------------------------------------------------------");

        cursDao.alta(produtorDatos.c0);

        Assert.assertNotNull(produtorDatos.c0.getId());
        Assert.assertNotNull(produtorDatos.s0.getId());
        Assert.assertNotNull(produtorDatos.s1.getId());
    }

    @Test
    public void test07_LAZY() {

        Curso c;

        Boolean excepcion;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        produtorDatos.creaCursosSueltos();
        produtorDatos.creaInstructoresNuevos();
        produtorDatos.guardaInstructores();

        Instructor instructor = produtorDatos.i0;
        produtorDatos.c0.setInstructor(instructor);
        Instructor instructor2 = produtorDatos.i1;
        produtorDatos.c1.setInstructor(instructor2);

        produtorDatos.guardaCursos();

        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba da recuperación de propiedades LAZY\n"
                + "\t\t\t\t Casos contemplados:\n"
                + "\t\t\t\t a) Recuperación de curso con colección (LAZY) de Temas\n"
                + "\t\t\t\t b) Carga forzada de colección LAZY da dita coleccion\n"
                + "\t\t\t\t c) Recuperacion de Temas suelta con referencia (EAGER) a Curso\n");


        log.info("Probando (excepcion tras) recuperacion LAZY ---------------------------------------------------------------------");

        c = cursDao.recuperaPorId(produtorDatos.c1.getIdCurso());
        log.info("Acceso a temas de Curso");

        try {
            Assert.assertEquals(3, c.getTemas().size());
            Assert.assertEquals("Tema 1", c.getTemas().first());
            Assert.assertEquals("Tema 3", c.getTemas().last());
            excepcion = false;
        }
        catch (LazyInitializationException ex) {
            excepcion = true;
            log.info(ex.getClass().getName());
        }

        Assert.assertTrue(excepcion);

        log.info("");
        log.info("Probando carga forzada de coleccion LAZY ------------------------------------------------------------------------");

        c = cursDao.recuperaPorId(produtorDatos.c1.getIdCurso());   // Curso c con proxy sen inicializar
        c = cursDao.recuperaTemas(c);                               // Curso c con proxy xa inicializado

        Assert.assertEquals(3, c.getTemas().size());
        Assert.assertEquals("Tema 1", c.getTemas().first());
        Assert.assertEquals("Tema 3", c.getTemas().last());

        log.info("");
        log.info("Probando acceso a referencia EAGER ------------------------------------------------------------------------------");

        c = cursDao.recuperaPorId(produtorDatos.c0.getIdCurso());
        Assert.assertEquals(produtorDatos.i0, c.getInstructor());
    }


}
