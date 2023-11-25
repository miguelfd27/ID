package gei.id.tutelado.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Curso;
import gei.id.tutelado.model.Instructor;
import gei.id.tutelado.model.Persona;
import gei.id.tutelado.model.Soldado;

public class PersonaDaoJPA implements PersonaDao {


	private EntityManagerFactory emf; 
	private EntityManager em;
    
	@Override
	public void setup (Configuracion config) {
		this.emf = (EntityManagerFactory) config.get("EMF");
	}

	@Override
	public Persona recuperaPorId(String dni) {

		List<Persona> personas=null;
		
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			
			personas = em.createNamedQuery("Persona.recuperaPorDni", Persona.class)
					.setParameter("dni", dni).getResultList(); 

			em.getTransaction().commit();
			em.close();
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return (personas.size()==0?null:personas.get(0));
	}
	
	@Override
	public Persona alta(Persona soldado) {
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			em.persist(soldado);

			em.getTransaction().commit();
			em.close();

		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return soldado;
	}
	

	@Override
	public void elimina(Persona persona) {
		try {
			
			em = emf.createEntityManager();
			em.getTransaction().begin();

			Persona personaTmp = em.find (Persona.class, persona.getId());
			if(personaTmp instanceof Soldado ) {
				Soldado soldado = (Soldado) personaTmp;
			Set<Curso> cursos = soldado.getCursos();
			for (Curso curso : cursos) {
			    soldado.eliminarCurso(curso);
			}}

			em.remove (personaTmp);

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
	public Persona actualiza(Persona persona) {
		try {
			
			em = emf.createEntityManager();
			em.getTransaction().begin();

			persona= em.merge (persona);

			em.getTransaction().commit();
			em.close();		
			
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return (persona);
	}
	
	@Override
	public List<Object[]> obtenerSoldadosConOCuSinCursosOrdenados() {
	    List<Object[]> resultados = null;

	    try {
	        em = emf.createEntityManager();
	        em.getTransaction().begin();

	        resultados = em.createNamedQuery("Soldado.obtenerSoldadosConOCuSinCursosOrdenados", Object[].class)
	                .getResultList();

	        em.getTransaction().commit();
	    } catch (Exception ex) {
	        if (em != null && em.isOpen()) {
	            if (em.getTransaction().isActive()) em.getTransaction().rollback();
	            throw ex;
	        }
	    } finally {
	        if (em != null && em.isOpen()) {
	            em.close();
	        }
	    }

	    return resultados;
	}


}
