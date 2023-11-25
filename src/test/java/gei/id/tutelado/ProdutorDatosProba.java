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

    //Crea un conxunto de obxectos para utilizar nos casos de proba
    private EntityManagerFactory emf = null;
    public Curso c0, c1;
    public Soldado s0, s1;
    public Instructor i0, i1;
    public List<Curso> listaCursos;
    public List<Soldado> listaSoldados;
    public List<Instructor> listaInstructores;


    public void Setup(Configuracion config) {
        this.emf = (EntityManagerFactory) config.get("EMF");
    }

    public void creaCursosSueltos() {

        SortedSet<String> temas = new TreeSet<String>();
        temas.add("Tema 1");
        temas.add("Tema 2");
        temas.add("Tema 3");

        this.c0 = new Curso();
        this.c0.setIdCurso(1L);
        this.c0.setDuracion(60);
        this.c0.setFechaInicio(LocalDate.now());
        this.c0.setTipo("Terrestre");
        this.c0.setTemas(temas);
        this.c0.setInstructor(null);

        this.c1 = new Curso();
        this.c1.setIdCurso(2L);
        this.c1.setDuracion(70);
        this.c1.setFechaInicio(LocalDate.now());
        this.c1.setTipo("Aéreo");
        this.c1.setTemas(temas);
        this.c1.setInstructor(null);

        this.listaCursos = new ArrayList<Curso>();
        this.listaCursos.add(0, c0);
        this.listaCursos.add(1, c1);
    }

    public void creaSoldadosNuevos() {

        //Los cursos de soldado los agregamos en el test
        this.s0 = new Soldado();
        this.s0.setDni("12345678A");
        this.s0.setNombre("Soldado 0");
        this.s0.setFechaAlta(LocalDate.now());
        this.s0.setFechaNacimiento(LocalDate.of(2000, 2, 2));
        this.s0.setRango("Cabo");
        this.s0.setPeso(70.0);
        this.s0.setAltura(185.0);

        this.s1 = new Soldado();
        this.s1.setDni("12345678B");
        this.s1.setNombre("Soldado 1");
        this.s1.setFechaAlta(LocalDate.now());
        this.s1.setFechaNacimiento(LocalDate.of(1999, 1, 6));
        this.s1.setRango("Artillero");
        this.s1.setPeso(65.0);
        this.s1.setAltura(179.0);

        this.listaSoldados = new ArrayList<Soldado>();
        this.listaSoldados.add(0, s0);
        this.listaSoldados.add(1, s1);
    }

    public void creaInstructoresNuevos() {

        Set<String> condecoraciones = new HashSet<String>();
        condecoraciones.add("Batalla 1");
        condecoraciones.add("Batalla 2");
        condecoraciones.add("Batalla 3");

        this.i0 = new Instructor();
        this.i0.setDni("12345678C");
        this.i0.setNombre("Instructor 0");
        this.i0.setFechaAlta(LocalDate.now());
        this.i0.setFechaNacimiento(LocalDate.of(1965, 2, 2));
        this.i0.setGrado("Comandante");
        this.i0.setSalario(3000.0);
        this.i0.setCondecoraciones(condecoraciones);

        this.i1 = new Instructor();
        this.i1.setDni("12345678D");
        this.i1.setNombre("Instructor 1");
        this.i1.setFechaAlta(LocalDate.now());
        this.i1.setFechaNacimiento(LocalDate.of(1966, 1, 6));
        this.i1.setGrado("Almirante");
        this.i1.setSalario(4000.0);
        this.i1.setCondecoraciones(condecoraciones);

        this.listaInstructores = new ArrayList<Instructor>();
        this.listaInstructores.add(0, i0);
        this.listaInstructores.add(1, i1);
    }


    public void guardaCursos() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Iterator<Curso> itC = this.listaCursos.iterator();
            while (itC.hasNext()) {
                Curso c = itC.next();
                em.persist(c);
            }
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw (e);
            }
        }
    }

    public void guardaSoldados() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Iterator<Soldado> itS = this.listaSoldados.iterator();
            while (itS.hasNext()) {
                Soldado s = itS.next();
                em.persist(s);
            }
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw (e);
            }
        }
    }

    public void guardaInstructores() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Iterator<Instructor> itI = this.listaInstructores.iterator();
            while (itI.hasNext()) {
                Instructor i = itI.next();
                em.persist(i);
            }
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw (e);
            }
        }
    }


    public void limpaBD() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Iterator<Curso> itU = em.createNamedQuery("Curso.recuperaTodosCursos", Curso.class).getResultList().iterator();
            while (itU.hasNext()) em.remove(itU.next());
            Iterator<Soldado> itS = em.createNamedQuery("Soldado.recuperaTodosSoldado", Soldado.class).getResultList().iterator();
            while (itS.hasNext()) em.remove(itS.next());
            Iterator<Instructor> itI = em.createNamedQuery("Instructor.recuperaTodosInstructor", Instructor.class).getResultList().iterator();
            while (itI.hasNext()) em.remove(itI.next());

            em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idCurso'").executeUpdate();
            em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idSoldado'").executeUpdate();
            em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idInstructor'").executeUpdate();
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw (e);
            }
        }
    }


}
