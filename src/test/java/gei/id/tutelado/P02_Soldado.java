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
import gei.id.tutelado.model.EntradaLog;
import gei.id.tutelado.model.PersonaDaoJPA;

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

		produtorDatos.creaSoldadosNuevos();
    	produtorDatos.guardaSoldados();


		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da recuperación (por codigo) de soldados soltas\n"   
		+ "\t\t\t\t Casos contemplados:\n"
		+ "\t\t\t\t a) Recuperación por codigo existente\n"
		+ "\t\t\t\t b) Recuperacion por codigo inexistente\n");     	

    	// Situación de partida:
    	// s1, s1A, s1B desligados
    	
		log.info("Probando recuperacion por codigo EXISTENTE --------------------------------------------------");

    	s = persDao.recuperaPorId(produtorDatos.s1A.());

    	Assert.assertEquals (produtorDatos.s1A.getDni(),     s.getDni());
    	Assert.assertEquals (produtorDatos.s1A.getNombre(), s.getNombre());
    	Assert.assertEquals (produtorDatos.s1A.getFechaAlta(),   s.getFechaAlta());
    	Assert.assertEquals (produtorDatos.s1A.getFechaNacimiento(),   s.getFechaNacimiento());
    	Assert.assertEquals (produtorDatos.s1A.getRango(),   s.getFechaNacimiento());
    	Assert.assertEquals (produtorDatos.s1A.getPeso(),   s.getPeso());
    	Assert.assertEquals (produtorDatos.s1A.getAltura(),   s.getAltura());
    	Assert.assertEquals (produtorDatos.s1A.getCursos(),   s.getCursos());

    	

    	log.info("");	
		log.info("Probando recuperacion por codigo INEXISTENTE --------------------------------------------------");
    	
    	s = persDao.recuperaPorId("iwbvyhuebvuwebvi");
    	Assert.assertNull (s);

    } 
	
	

}
