package gei.id.tutelado.dao;


import java.util.List;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Curso;


public interface CursoDao {
	
	void setup (Configuracion config);
	
	// OPERACIONS CRUD BASICAS
	Curso recuperaPorId (Long idCurso);
	Curso alta (Curso curso);
	void elimina (Curso curso);
	Curso actualiza (Curso curso);
	Curso recuperaTemas(Curso curso);


	//QUERIES ADICIONAIS
	List<Curso> recuperaTodosCursos(Curso c);
	List<Curso> CursosConInstructoresYSoldados();
	//List<Curso> cursosInstructor(Instructor instructor);


		


		
}
