package gei.id.tutelado;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.junit.After;
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
import gei.id.tutelado.model.Curso;
import gei.id.tutelado.model.EntradaLog;
import gei.id.tutelado.model.Instructor;
import gei.id.tutelado.model.Persona;
import gei.id.tutelado.model.Usuario;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class P01_Curso {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProdutorDatosProba produtorDatos = new ProdutorDatosProba();

    private static Configuracion cfg;
    private static CursoDao cursDao;


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

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test01_Recuperacion() {

        Curso c;
        Instructor instructor, instructor2;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        produtorDatos.creaCursosSueltos();
        produtorDatos.creaInstructoresNuevos();
        produtorDatos.guardaInstructores();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de recuperación desde a BD de cursos por idCurso\n"
                + "\t\t\t\t Casos contemplados:\n"
                + "\t\t\t\t a) Recuperación por idCurso existente\n"
                + "\t\t\t\t b) Recuperacion por idCurso inexistente\n");

        //Situación de partida:
        //c0 desligado

        log.info("Probando recuperacion por idCurso EXISTENTE --------------------------------------------------");

        //Asigna instructor a curso
        instructor = produtorDatos.i0;
        produtorDatos.c0.setInstructor(instructor);
        instructor2 = produtorDatos.i1;
        produtorDatos.c1.setInstructor(instructor2);
        produtorDatos.guardaCursos();

        c = cursDao.recuperaPorId(produtorDatos.c0.getIdCurso());

        Assert.assertEquals(produtorDatos.c0.getIdCurso(), c.getIdCurso());
        Assert.assertEquals(produtorDatos.c0.getDuracion(), c.getDuracion());
        Assert.assertEquals(produtorDatos.c0.getFechaInicio(), c.getFechaInicio());
        Assert.assertEquals(produtorDatos.c0.getTipo(), c.getTipo());
        Assert.assertEquals(instructor, c.getInstructor());

        c = cursDao.recuperaTemas(c);

        Assert.assertEquals(produtorDatos.c0.getTemas(), c.getTemas());

        log.info("");
        log.info("Probando recuperacion por idCurso INEXISTENTE -----------------------------------------------");

        c = cursDao.recuperaPorId(78556465464L);
        Assert.assertNull(c);

    }


    @Test
    public void test02_Alta() {

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        produtorDatos.creaCursosSueltos();
        produtorDatos.creaInstructoresNuevos();
        produtorDatos.guardaInstructores();
        Instructor instructor = produtorDatos.i0;
        produtorDatos.c0.setInstructor(instructor);
        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de gravación na BD de novo curso (sen clases asociadas)\n");

        // Situación de partida:
        // c0 transitorio

        Assert.assertNull(produtorDatos.c0.getId());
        cursDao.alta(produtorDatos.c0);
        Assert.assertNotNull(produtorDatos.c0.getId());
    }

    @Test
    public void test03_Eliminacion() {

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

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de eliminación da BD de cursos sen clases asociadas\n");

        Assert.assertNotNull(cursDao.recuperaPorId(produtorDatos.c0.getIdCurso()));
        cursDao.elimina(produtorDatos.c0);
        Assert.assertNull(cursDao.recuperaPorId(produtorDatos.c0.getIdCurso()));

    }

    @Test
    public void test04_Modificacion() {

        Curso c1, c2;
        String nuevoTipo;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
        produtorDatos.creaCursosSueltos();
        produtorDatos.creaInstructoresNuevos(); // Agrega esta línea para crear instructores
        produtorDatos.guardaInstructores();

        Instructor instructor = produtorDatos.i0;
        produtorDatos.c0.setInstructor(instructor);
        Instructor instructor2 = produtorDatos.i1;
        produtorDatos.c1.setInstructor(instructor2);
        produtorDatos.guardaCursos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de modificación da información básica dun curso\n");

        // Situación de partida:
        // c0 desligado

        nuevoTipo = new String("Nome novo");

        c1 = cursDao.recuperaPorId(produtorDatos.c0.getIdCurso());
        Assert.assertNotEquals(nuevoTipo, c1.getTipo());
        c1.setTipo(nuevoTipo);

        cursDao.actualiza(c1);

        c2 = cursDao.recuperaPorId(produtorDatos.c0.getIdCurso());
        Assert.assertEquals(nuevoTipo, c2.getTipo());
    }

}

