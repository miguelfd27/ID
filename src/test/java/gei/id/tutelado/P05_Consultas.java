package gei.id.tutelado;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class P05_Consultas {

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
    public void test08_cursosConInstructoresYSoldados() {

        List<Curso> listaCursos;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        produtorDatos.creaCursosSueltos();
        produtorDatos.creaInstructoresNuevos();
        produtorDatos.guardaInstructores();
        produtorDatos.creaSoldadosNuevos();
        produtorDatos.s0.agregarCurso(produtorDatos.c0);    //c0 tiene alumno e instructor asociado y c1 no

        Instructor instructor = produtorDatos.i0;
        produtorDatos.c0.setInstructor(instructor);
        Instructor instructor2 = produtorDatos.i1;
        produtorDatos.c1.setInstructor(instructor2);

        produtorDatos.guardaCursos();


        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba da consulta Curso.CursosConInstructoresYSoldados\n");

        listaCursos = cursDao.CursosConInstructoresYSoldados();

        Assert.assertEquals(1, listaCursos.size());
        Assert.assertEquals(produtorDatos.c0, listaCursos.get(0));

    }

    @Test
    public void test09_soldadosConOCuSinCursosOrdenados() {

        List<Object[]> listaInstructoresyCursos;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        produtorDatos.creaCursosSueltos();
        produtorDatos.creaInstructoresNuevos(); // Agrega esta línea para crear instructores
        produtorDatos.guardaInstructores();
        produtorDatos.creaSoldadosNuevos();
        produtorDatos.s0.agregarCurso(produtorDatos.c0);
        produtorDatos.s1.agregarCurso(produtorDatos.c1);


        Instructor instructor = produtorDatos.i0;
        produtorDatos.c0.setInstructor(instructor);
        Instructor instructor2 = produtorDatos.i1;
        produtorDatos.c1.setInstructor(instructor2);
        produtorDatos.guardaCursos();
        cursDao.elimina(produtorDatos.c1);//creo dos cursos con soldados para guardarlos con el persist y luego borro el curso del soldado


        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba da consulta soldado.obtenerSoldadosConOCuSinCursosOrdenados\n");


        listaInstructoresyCursos = persDao.obtenerSoldadosConOCuSinCursosOrdenados();

        Assert.assertEquals(2, listaInstructoresyCursos.size());

    }

    @Test
    public void test10_instructoresConMasDeUnCurso() {

        List<Instructor> listaInstructores;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        produtorDatos.creaCursosSueltos();
        produtorDatos.creaInstructoresNuevos();
        produtorDatos.guardaInstructores();
        produtorDatos.creaSoldadosNuevos();
        produtorDatos.s0.agregarCurso(produtorDatos.c0);
        produtorDatos.s1.agregarCurso(produtorDatos.c1);

        Instructor instructor = produtorDatos.i0;
        produtorDatos.c0.setInstructor(instructor);
        produtorDatos.c1.setInstructor(instructor);
        produtorDatos.guardaCursos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba da consulta Instructor.obtenerInstructoresConMasDeUnCurso\n");

        listaInstructores = persDao.obtenerInstructoresConMasDeUnCurso();

        Assert.assertEquals(1, listaInstructores.size());

    }

    @Test
    public void test11_contarTotalSoldadosPorCurso() {
        List<Object[]> resultados;

        log.info("");
        log.info("Configurando situación de partida del test ----------------------------------------");

        produtorDatos.creaCursosSueltos();
        produtorDatos.creaInstructoresNuevos();
        produtorDatos.guardaInstructores();
        produtorDatos.creaSoldadosNuevos();
        produtorDatos.s0.agregarCurso(produtorDatos.c0);
        produtorDatos.s1.agregarCurso(produtorDatos.c1);

        Instructor instructor = produtorDatos.i0;
        produtorDatos.c0.setInstructor(instructor);
        produtorDatos.c1.setInstructor(instructor);
        produtorDatos.guardaCursos();


        log.info("");
        log.info("Inicio del test --------------------------------------------------------------------");
        log.info("Objetivo: Probar la consulta Soldado.contarTotalSoldadosPorCurso\n");

        resultados = persDao.contarTotalSoldadosPorCurso();

        Assert.assertEquals(2, resultados.size());
        Object[] primerResultado = resultados.get(0);
        Assert.assertEquals(2L, primerResultado[0]);
    }


}
