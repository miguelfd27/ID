package gei.id.tutelado;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
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

		

}
