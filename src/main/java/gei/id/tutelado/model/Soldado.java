package gei.id.tutelado.model;

import java.util.HashSet;
import java.util.Set;


import javax.persistence.*;

@NamedQueries ({
	@NamedQuery (name="Soldado.recuperaPorSoldado",
	 			 query="SELECT s FROM Soldado s where s.dni=:dni"),
	@NamedQuery (name="Soldado.recuperaTodosSoldado",
			 	 query="SELECT s FROM Soldado s ORDER BY s.dni"),
	@NamedQuery(name = "Soldado.obtenerSoldadosConOCuSinCursosOrdenados",
		    	query = "SELECT s, c FROM Soldado s LEFT JOIN s.cursos c ORDER BY s.nombre")
})

@Entity
@Table(name="t_sold_tcc")
public class Soldado extends Persona implements Comparable<Soldado>{

    @Column(unique = false, nullable = false)
    private String rango;

    @Column(unique = false, nullable = false)
    private Double peso;

    @Column(unique =false, nullable = false)
    private Double altura;

    @ManyToMany(mappedBy="soldados", fetch=FetchType.EAGER)
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
	    
	        getCursos().add(curso);
	        curso.getSoldados().add(this);
	    
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
	public int compareTo(Soldado otroSoldado) {
	    if (this.rango == null && otroSoldado.rango == null) {
	        return 0; // Ambos son nulos, considerados iguales
	    } else if (this.rango == null) {
	        return -1; // Este es nulo, considerado menor
	    } else if (otroSoldado.rango == null) {
	        return 1; // El otro es nulo, considerado mayor
	    }
	    return this.rango.compareTo(otroSoldado.rango);
	}

	@Override
	public String toString() {
		return "Soldado [rango=" + rango + ", peso=" + peso + ", altura=" + altura + ", cursos=" + cursos + "]";
	}

}
