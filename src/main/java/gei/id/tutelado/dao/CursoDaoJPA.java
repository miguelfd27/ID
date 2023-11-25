package gei.id.tutelado.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.LazyInitializationException;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Curso;
import gei.id.tutelado.model.Instructor;

public class CursoDaoJPA implements CursoDao {

	private EntityManagerFactory emf; 
	private EntityManager em;
    
	@Override
	public void setup (Configuracion config) {
		this.emf = (EntityManagerFactory) config.get("EMF");
	}
	
	
	@Override
	public Curso recuperaPorId(Long idCurso) {

		List<Curso> cursos=null;
		
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			cursos = em.createNamedQuery("Curso.recuperaPorId", Curso.class)
					.setParameter("idCurso", idCurso).getResultList(); 

			em.getTransaction().commit();
			em.close();
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return (cursos.size()==0?null:cursos.get(0));
	}


	@Override
	public Curso alta(Curso curso) {

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			em.persist(curso);

			em.getTransaction().commit();
			em.close();

		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return curso;
	}


	@Override
	public void elimina(Curso curso) {

		try {
			
			em = emf.createEntityManager();
			em.getTransaction().begin();

			Curso cursoTmp = em.find (Curso.class, curso.getId());
			em.remove (cursoTmp);

			em.getTransaction().commit();
			em.close();
			
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		
	}


	@Override
	public Curso actualiza(Curso curso) {
		try {
			
			em = emf.createEntityManager();
			em.getTransaction().begin();

			curso= em.merge (curso);

			em.getTransaction().commit();
			em.close();		
			
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return (curso);
	}



	@Override
	public List<Curso> recuperaTodosCursos(Curso c) {
		List <Curso> cursos=null;

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			cursos = em.createNamedQuery("Curso.recuperaTodosCursos", Curso.class).setParameter("c", c).getResultList();

			em.getTransaction().commit();
			em.close();

		}
		catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return cursos;
	}

	@Override
	public Curso recuperaTemas(Curso curso) {

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			try {
				curso.getTemas().size();
			}
			catch (Exception ex2) {
				if (ex2 instanceof LazyInitializationException) {
					curso = em.merge(curso);
					curso.getTemas().size();

				}
				else {
					throw ex2;
				}
			}
			em.getTransaction().commit();
			em.close();
		}
		catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return (curso);
	}

	@Override
	public List<Curso> CursosConInstructoresYSoldados() {
    	List<Curso> cursos = null;

    	try {
        	em = emf.createEntityManager();
        	em.getTransaction().begin();

        	cursos = em.createNamedQuery("Curso.CursosConInstructoresYSoldados", Curso.class).getResultList();

        	em.getTransaction().commit();
		}
		catch (Exception ex) {
        	if (em != null && em.isOpen()) {
            	if (em.getTransaction().isActive()) em.getTransaction().rollback();
            	throw ex;
        	}
    	}
		finally {
        	if (em != null && em.isOpen()) {
            	em.close();
        	}
    	}
    	return cursos;
	}



	@Override
	public List<Curso> cursosInstructor(Instructor instructor) {
		List<Curso> cursosInstructor=null;

    	try {
       		em = emf.createEntityManager();
       		em.getTransaction().begin();

       		cursosInstructor = em.createNamedQuery("Instructor.cursosInstructor", Curso.class).setParameter("instructor", instructor).getResultList();

       		em.getTransaction().commit();
       		em.close();

    	}
    	catch (Exception ex ) {
			if (em != null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw (ex);
			}
		}
		return cursosInstructor;
	}
}



