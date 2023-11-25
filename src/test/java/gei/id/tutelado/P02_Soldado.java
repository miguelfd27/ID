package gei.id.tutelado;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.PersonaDao;
import gei.id.tutelado.dao.PersonaDaoJPA;
import gei.id.tutelado.model.EntradaLog;
import gei.id.tutelado.model.Instructor;
import gei.id.tutelado.model.Persona;
import gei.id.tutelado.model.Soldado;

public class P02_Soldado {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProdutorDatosProba produtorDatos = new ProdutorDatosProba();
    
    private static Configuracion cfg;
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

	@After
	public void tearDown() throws Exception {
	}	
	
    @Test 
    public void test01_Recuperacion() {
   	
    	Soldado s;
    	
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
    	log.info("Obxectivo: Proba da recuperación (por codigo) de soldados soltos\n"
				+ "\t\t\t\t Casos contemplados:\n"
				+ "\t\t\t\t a) Recuperación por codigo existente\n"
				+ "\t\t\t\t b) Recuperacion por codigo inexistente\n");


    	
		log.info("Probando recuperacion por codigo EXISTENTE --------------------------------------------------");

    	s = (Soldado) persDao.recuperaPorId(produtorDatos.s0.getDni());

    	Assert.assertEquals (produtorDatos.s0.getDni(), s.getDni());
    	Assert.assertEquals (produtorDatos.s0.getNombre(), s.getNombre());
    	Assert.assertEquals (produtorDatos.s0.getFechaAlta(), s.getFechaAlta());
    	Assert.assertEquals (produtorDatos.s0.getFechaNacimiento(), s.getFechaNacimiento());
    	Assert.assertEquals (produtorDatos.s0.getRango(), s.getRango());
    	Assert.assertEquals (produtorDatos.s0.getPeso(), s.getPeso());
    	Assert.assertEquals (produtorDatos.s0.getAltura(), s.getAltura());
    	Assert.assertEquals (produtorDatos.s0.getCursos(), s.getCursos());

    	log.info("");	
		log.info("Probando recuperacion por codigo INEXISTENTE --------------------------------------------------");
    	
    	s = (Soldado) persDao.recuperaPorId("iwbvyhuebvuwebvi");
    	Assert.assertNull (s);
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
		Instructor instructor2 = produtorDatos.i1;
		produtorDatos.c1.setInstructor(instructor2);
    	produtorDatos.guardaCursos();
    	produtorDatos.creaSoldadosNuevos();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da gravación de soldados soltos\n"
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Primeiro soldado vinculado a un curso\n"
    			+ "\t\t\t\t b) Novo soldado para un curso\n");


    	produtorDatos.s0.agregarCurso(produtorDatos.c0);

    	log.info("");	
		log.info("Gravando o primeiro soldado -------------------------------------------------------------------------------------");
    	Assert.assertNull(produtorDatos.s0.getId());
    	persDao.alta(produtorDatos.s0);
    	Assert.assertNotNull(produtorDatos.s0.getId());

    }
    
    @Test 
    public void test03_Modificacion() {

    	Soldado s1, s2;
    	Double nuevoPeso;
    	
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


    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de modificación da información dun soldado solto\n");
 

		nuevoPeso = Double.valueOf(75.69);

		s1 = (Soldado) persDao.recuperaPorId(produtorDatos.s0.getDni());

		Assert.assertNotEquals(nuevoPeso, s1.getPeso());
    	s1.setPeso(nuevoPeso);

    	persDao.actualiza(s1);    	
    	
		s2 = (Soldado) persDao.recuperaPorId(produtorDatos.s0.getDni());
		Assert.assertEquals (nuevoPeso, s2.getPeso());

    }
    
    @Test 
    public void test04_Eliminacion() {
    	
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
	    
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de eliminación de soldado solto\n");

		Assert.assertNotNull(persDao.recuperaPorId(produtorDatos.s0.getDni()));
    	persDao.elimina(produtorDatos.s0);    	
		Assert.assertNull(persDao.recuperaPorId(produtorDatos.s0.getDni()));

    }

}
