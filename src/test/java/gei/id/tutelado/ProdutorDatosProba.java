package gei.id.tutelado;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.*;

public class ProdutorDatosProba {


    // Crea un conxunto de obxectos para utilizar nos casos de proba
    
    private EntityManagerFactory emf=null;
    
    public Usuario u0, u1;
    public Curso c0, c1;
    public Soldado s0, s1;
    public List<Usuario> listaxeU;
    public List<Curso> listaCursos;
    public List<Soldado> listaSoldados;
    
    public EntradaLog e1A, e1B;
    public List<EntradaLog> listaxeE;
    
    
    
    public void Setup (Configuracion config) {
       this.emf=(EntityManagerFactory) config.get("EMF");
    }
    /*
    public void creaUsuariosSoltos() {

       // Crea dous usuarios EN MEMORIA: u0, u1
       // SEN entradas de log
       
       this.u0 = new Usuario();
        this.u0.setNif("000A");
        this.u0.setNome("Usuario cero");
        this.u0.setDataAlta(LocalDate.now());

        this.u1 = new Usuario();
        this.u1.setNif("111B");
        this.u1.setNome("Usuaria un");
        this.u1.setDataAlta(LocalDate.now());

        this.listaxeU = new ArrayList<Usuario> ();
        this.listaxeU.add(0,u0);
        this.listaxeU.add(1,u1);        

    }
    */
    
    public void creaCursosSueltos() {
       
       SortedSet<String> temas = new TreeSet<String>();
       temas.add("Tema 1");
       temas.add("Tema 2");
       temas.add("Tema 3");
       
      // SortedSet<Soldado> soldados = new TreeSet<Soldado>();

       this.c0 = new Curso();
        this.c0.setIdCurso(00001L);
        this.c0.setDuracion(60);
        this.c0.setFechaInicio(LocalDate.now());
        this.c0.setTipo("Terrestre");
        this.c0.setTemas(temas);
        this.c0.setInstructor(null);
 //       this.c0.setSoldados(soldados);
        
        this.c1 = new Curso();
        this.c1.setIdCurso(00002L);
        this.c1.setDuracion(70);
        this.c1.setFechaInicio(LocalDate.now());
        this.c1.setTipo("Aéreo");
        this.c1.setTemas(temas);
        this.c1.setInstructor(null);
     //   this.c1.setSoldados(soldados);

        this.listaCursos = new ArrayList<Curso> ();
        this.listaCursos.add(0,c0);
        this.listaCursos.add(1,c1);        

    }
    
    public void creaSoldadosNuevos() {
 

        this.s0 = new Soldado();
        this.s0.setDni("12345678A");
        this.s0.setNombre("Soldado 0");
        this.s0.setFechaAlta(LocalDate.now());
        this.s0.setFechaNacimiento(LocalDate.of(2000, 2, 2));
        this.s0.setRango("Cabo");
        this.s0.setPeso(70.0);
        this.s0.setAltura(185.0);
        //s0.agregarCurso(c0);
        //s0.agregarCurso(c1);

        this.s1 = new Soldado();
        this.s1.setDni("12345678B");
        this.s1.setNombre("Soldado 1");
        this.s1.setFechaAlta(LocalDate.now());
        this.s1.setFechaNacimiento(LocalDate.of(1999, 1, 6));
        this.s1.setRango("Artillero");
        this.s1.setPeso(65.0);
        this.s1.setAltura(179.0);
       //s1.agregarCurso(c0);
        //s1.agregarCurso(c1);

        this.listaSoldados = new ArrayList<Soldado>();
        this.listaSoldados.add(0, s0);
        this.listaSoldados.add(1, s1);
    }
    
    public void creaEntradasLogSoltas () {

       // Crea duas entradas de log EN MEMORIA: e1a, e1b
       // Sen usuario asignado (momentaneamente)
       
       this.e1A=new EntradaLog();
        this.e1A.setCodigo("E001");
        this.e1A.setDescricion ("Modificado contrasinal por defecto");
        this.e1A.setDataHora(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        this.e1B=new EntradaLog();
        this.e1B.setCodigo("E002");
        this.e1B.setDescricion ("Acceso a zona reservada");
        this.e1B.setDataHora(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        this.listaxeE = new ArrayList<EntradaLog> ();
        this.listaxeE.add(0,this.e1A);
        this.listaxeE.add(1,this.e1B);        

    }
    /*
    public void creaUsuariosConEntradasLog () {

       this.creaUsuariosSoltos();
       this.creaEntradasLogSoltas();
       
        this.u1.engadirEntradaLog(this.e1A);
        this.u1.engadirEntradaLog(this.e1B);

    }
    */
    /*
    public void gravaUsuarios() {
       EntityManager em=null;
       try {
          em = emf.createEntityManager();
          em.getTransaction().begin();

          Iterator<Usuario> itU = this.listaxeU.iterator();
          while (itU.hasNext()) {
             Usuario u = itU.next();
             em.persist(u);
             // DESCOMENTAR SE A PROPAGACION DO PERSIST NON ESTA ACTIVADA
             /*
             Iterator<EntradaLog> itEL = u.getEntradasLog().iterator();
             while (itEL.hasNext()) {
                em.persist(itEL.next());
             }
             
          }
          em.getTransaction().commit();
          em.close();
       } catch (Exception e) {
          if (em!=null && em.isOpen()) {
             if (em.getTransaction().isActive()) em.getTransaction().rollback();
             em.close();
             throw (e);
          }
       }  
    }
    */
    
    public void guardaCursos() {
       EntityManager em=null;
       try {
          em = emf.createEntityManager();
          em.getTransaction().begin();

          Iterator<Curso> itC = this.listaCursos.iterator();
          while (itC.hasNext()) {
             Curso c = itC.next();
             em.persist(c);
             // DESCOMENTAR SE A PROPAGACION DO PERSIST NON ESTA ACTIVADA
             
           /*  Iterator<Soldado> itEL = c.getSoldados().iterator();
             while (itEL.hasNext()) {
                em.persist(itEL.next());
             }*/
             
          }
          em.getTransaction().commit();
          em.close();
       } catch (Exception e) {
          if (em!=null && em.isOpen()) {
             if (em.getTransaction().isActive()) em.getTransaction().rollback();
             em.close();
             throw (e);
          }
       }  
    }

    public void guardaSoldados() {
       EntityManager em=null;
       try {
          em = emf.createEntityManager();
          em.getTransaction().begin();

          Iterator<Soldado> itS = this.listaSoldados.iterator();
          while (itS.hasNext()) {
             Soldado s = itS.next();
             em.persist(s);
              //DESCOMENTAR SE A PROPAGACION DO PERSIST NON ESTA ACTIVADA
             /*
             Iterator<Curso> itEL = s.getCursos().iterator();
             while (itEL.hasNext()) {
                em.persist(itEL.next());
             }*/
             
          }
          em.getTransaction().commit();
          em.close();
       } catch (Exception e) {
          if (em!=null && em.isOpen()) {
             if (em.getTransaction().isActive()) em.getTransaction().rollback();
             em.close();
             throw (e);
          }
       }
    }
    
    
    public void limpaBD () {
       EntityManager em=null;
       try {
          em = emf.createEntityManager();
          em.getTransaction().begin();
          
          Iterator <Curso> itU = em.createNamedQuery("Curso.recuperaTodosCursos", Curso.class).getResultList().iterator();
          while (itU.hasNext()) em.remove(itU.next());
         Iterator <Soldado> itP = em.createNamedQuery("Soldado.recuperaTodosSoldado", Soldado.class).getResultList().iterator();
         while (itP.hasNext()) em.remove(itP.next());

          
          em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idCurso'" ).executeUpdate();
          em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idSoldado'" ).executeUpdate();

          em.getTransaction().commit();
          em.close();
       } catch (Exception e) {
          if (em!=null && em.isOpen()) {
             if (em.getTransaction().isActive()) em.getTransaction().rollback();
             em.close();
             throw (e);
          }
       }
    }
    
    
}