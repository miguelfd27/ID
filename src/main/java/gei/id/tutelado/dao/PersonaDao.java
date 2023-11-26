package gei.id.tutelado.dao;

import java.util.List;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Instructor;
import gei.id.tutelado.model.Persona;
import gei.id.tutelado.model.Soldado;

public interface PersonaDao {
	

	void setup (Configuracion config);
	
	// OPERACIONS CRUD BASICAS
	Persona recuperaPorId (String dni);
	Persona alta (Persona persona);
	void elimina (Persona persona);
	Persona actualiza (Persona persona);

	//QUERIES ADICIONAIS
	List<Object[]> obtenerSoldadosConOCuSinCursosOrdenados();

	List<Instructor> obtenerInstructoresConMasDeUnCurso();

	List<Object[]> contarTotalSoldadosPorCurso();
}
