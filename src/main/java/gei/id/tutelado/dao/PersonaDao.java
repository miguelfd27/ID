package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Instructor;
import gei.id.tutelado.model.Persona;
import gei.id.tutelado.model.Soldado;

public interface PersonaDao {
	

	void setup (Configuracion config);
	
	// OPERACIONS CRUD BASICAS
		Soldado recuperaPorId (String dni);
		Persona alta (Persona persona);
		void elimina (Soldado soldado);
		Persona actualiza (Persona persona);
		Instructor recuperaPorId2(String dni);

		void eliminaInstructor(Instructor instructor);
		
		//QUERIES ADICIONAIS


}
