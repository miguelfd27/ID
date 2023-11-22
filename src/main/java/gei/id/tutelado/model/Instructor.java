package gei.id.tutelado.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="t_inst_tcc")
public class Instructor extends Persona{
	

    @Column(unique = false, nullable = false)
    private Double salario;

    @Column(unique = false, nullable = false)
    private String grado;

    @ElementCollection(fetch=FetchType.LAZY)
    @CollectionTable (name="t_inst_cond",joinColumns=@JoinColumn(name="id_pers"))
    @Column(name="condecoraciones", nullable=false)
    @OrderBy
    private Set<String> condecoraciones = new HashSet<String>();

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	public String getGrado() {
		return grado;
	}

	public void setGrado(String grado) {
		this.grado = grado;
	}

	public Set<String> getCondecoraciones() {
		return condecoraciones;
	}

	public void setCondecoraciones(Set<String> condecoraciones) {
		this.condecoraciones = condecoraciones;
	}


	@Override
	public String toString() {
		return "Instructor [salario=" + salario + ", grado=" + grado + ", condecoraciones=" + condecoraciones + "]";
	}

    
    
    
 

}