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
import gei.id.tutelado.model.Instructor;
import gei.id.tutelado.model.Soldado;

public class P03_Instructor {
	
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
   	
    	Instructor i;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaInstructoresNuevos();
		produtorDatos.guardaInstructores();



		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da recuperación (por codigo) de soldados soltas\n"   
		+ "\t\t\t\t Casos contemplados:\n"
		+ "\t\t\t\t a) Recuperación por codigo existente\n"
		+ "\t\t\t\t b) Recuperacion por codigo inexistente\n");     	

    	// Situación de partida:
    	// s1, s1A, s1B desligados
    	
		log.info("Probando recuperacion por codigo EXISTENTE --------------------------------------------------");

    	i = persDao.recuperaPorId2(produtorDatos.i0.getDni());
    	

    	Assert.assertEquals (produtorDatos.i0.getDni(),     i.getDni());
    	Assert.assertEquals (produtorDatos.i0.getNombre(), i.getNombre());
    	Assert.assertEquals (produtorDatos.i0.getFechaAlta(),   i.getFechaAlta());
    	Assert.assertEquals (produtorDatos.i0.getFechaNacimiento(),   i.getFechaNacimiento());
    	Assert.assertEquals (produtorDatos.i0.getSalario(),   i.getSalario());
    	Assert.assertEquals (produtorDatos.i0.getGrado(),    i.getGrado());
    	Assert.assertNotNull ( i.getCondecoraciones());

    	

    	log.info("");	
		log.info("Probando recuperacion por codigo INEXISTENTE --------------------------------------------------");
    	
    	i = persDao.recuperaPorId2("iwbvyhuebvuwebvi");
    	Assert.assertNull (i);

    } 	
    
    @Test
    public void test02_Alta() {


    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		
    	produtorDatos.creaInstructoresNuevos();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da gravación de entradas de log soltas\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Primeira entrada de log vinculada a un usuario\n"
    			+ "\t\t\t\t b) Nova entrada de log para un usuario con entradas previas\n");     	

    	// Situación de partida:
    	// u1 desligado    	
    	// e1A, e1B transitorios


		
    	log.info("");	
		log.info("Gravando primeira entrada de log dun usuario --------------------------------------------------------------------");
    	Assert.assertNull(produtorDatos.i0.getId());
    	persDao.alta(produtorDatos.i0);
    	Assert.assertNotNull(produtorDatos.i0.getId());


    }
    
    @Test 
    public void test03_Modificacion() {

    	Instructor i1, i2;
    	String nuevoGrado;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
  
		produtorDatos.creaInstructoresNuevos();
    	produtorDatos.guardaInstructores();


    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de modificación da información dunha entrada de log solta\n");
 
    	
    	// Situación de partida:
    	// e1A desligado
    	
		nuevoGrado = new String("Cabo Superior");

		i1 = persDao.recuperaPorId2(produtorDatos.i0.getDni());

		Assert.assertNotEquals(nuevoGrado, i1.getGrado());
    	i1.setGrado(nuevoGrado);

    	persDao.actualiza(i1);    	
    	
		i2 = persDao.recuperaPorId2(produtorDatos.i0.getDni());
		Assert.assertEquals (nuevoGrado, i2.getGrado());

    	// NOTA: Non probamos modificación de usuario da entrada porque non ten sentido no dominio considerado

    }
    
    @Test 
    public void test04_Eliminacion() {
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaInstructoresNuevos();
    	produtorDatos.guardaInstructores();
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de eliminación de entrada de log solta (asignada a usuario)\n");
    	
    	// Situación de partida:
    	// e1A desligado

		Assert.assertNotNull(persDao.recuperaPorId2(produtorDatos.i0.getDni()));
    	persDao.eliminaInstructor(produtorDatos.i0);    	
		Assert.assertNull(persDao.recuperaPorId2(produtorDatos.i0.getDni()));

    } 	
    
    
    
	
	
	

}
