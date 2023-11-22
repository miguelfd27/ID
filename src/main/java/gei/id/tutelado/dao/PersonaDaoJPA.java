package gei.id.tutelado.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import gei.id.tutelado.configuracion.Configuracion;
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
	public Soldado recuperaPorId(String dni) {

		List<Soldado> personas=null;
		
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			personas = em.createNamedQuery("Soldado.recuperaPorSoldado", Soldado.class)
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

}


