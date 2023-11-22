package gei.id.tutelado.model;

import java.util.HashSet;
import java.util.Set;


import javax.persistence.*;

@Entity
@Table(name="t_sold_tcc")
public class Soldado extends Persona{
	

    @Column(unique = false, nullable = false)
    private String rango;

    @Column(unique = false, nullable = false)
    private Double peso;

    @Column(unique =false, nullable = false)
    private Double altura;

    @ManyToMany(mappedBy="soldados", cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
    private Set<Curso> cursos = new HashSet<Curso>();
    
    

	public String getRango() {
		return rango;
	}

	public void setRango(String rango) {
		this.rango = rango;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public Double getAltura() {
		return altura;
	}

	public void setAltura(Double altura) {
		this.altura = altura;
	}

	public Set<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(Set<Curso> cursos) {
		this.cursos = cursos;
	}
	
	public void agregarCurso(Curso curso) {
	    if (curso == null || this == null) {
	        throw new RuntimeException("");
	    }
	    if (!getCursos().contains(curso)) {
	        getCursos().add(curso);
	        if (!curso.getSoldados().contains(this)) {
	            curso.getSoldados().add(this);
	        }
	    }
	}
	
	public void eliminarCurso(Curso curso) {
	    if (getCursos().contains(curso)) {
	        if (curso.getSoldados().contains(this)) {
	            curso.getSoldados().remove(this);
	            getCursos().remove(curso);
	        }
	    }
	}


	
	@Override
	public String toString() {
		return "Soldado [rango=" + rango + ", peso=" + peso + ", altura=" + altura + ", cursos=" + cursos + "]";
	}
    
    
	

}
