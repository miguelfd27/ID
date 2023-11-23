package gei.id.tutelado.model;

import java.time.LocalDate;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.*;

@TableGenerator(name="xeradorIdsCursos", table="taboa_ids",
pkColumnName="nome_id", pkColumnValue="idCurso",
valueColumnName="ultimo_valor_id",
initialValue=0, allocationSize=1)

@NamedQueries ({
	@NamedQuery (name="Curso.recuperaPorId",
				 query="SELECT c FROM Curso c where c.idCurso=:idCurso"),
	@NamedQuery (name="Curso.recuperaTodosCursos",
				 query="SELECT c FROM Curso c ORDER BY c.idCurso")
})


@Entity
@Table(name="Curso")
public class Curso {

    @Id
    @GeneratedValue(generator="xeradorIdsCursos")
    @Column(name="id_curso")
    private Long id;

    @Column(unique = true, nullable = false)
    private Long idCurso;

    @Column(unique = false, nullable = false)
    private int duracion;

    @Column(unique =false, nullable = false)
    private LocalDate fechaInicio;

    @Column(unique = false, nullable = false)
    private String tipo;
    
    @ElementCollection
    @CollectionTable (name="t_curso_temas",joinColumns=@JoinColumn(name="id_curso"))
    @Column(name="temas", nullable=false)
    @OrderBy
    private SortedSet<String> temas = new TreeSet<String>();
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "id_inst", nullable = false)
    private Instructor instructor;

    @ManyToMany(fetch=FetchType.LAZY,cascade=CascadeType.PERSIST)
    @JoinTable(
        name = "t_curso_sold",
        joinColumns = @JoinColumn(name = "id_curso"),
        inverseJoinColumns = @JoinColumn(name = "id_sold"))
    @OrderBy("rango")
    private SortedSet<Soldado> soldados = new TreeSet <Soldado>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(Long idCurso) {
		this.idCurso = idCurso;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public SortedSet<String> getTemas() {
		return temas;
	}

	public void setTemas(SortedSet<String> temas) {
		this.temas = temas;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public SortedSet<Soldado> getSoldados() {
		return soldados;
	}

	public void setSoldados(SortedSet<Soldado> soldados) {
		this.soldados = soldados;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCurso == null) ? 0 : idCurso.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Curso other = (Curso) obj;
		if (idCurso == null) {
			if (other.idCurso != null)
				return false;
		} else if (!idCurso.equals(other.idCurso))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Curso [id=" + id + ", idCurso=" + idCurso + ", duracion=" + duracion + ", fechaInicio=" + fechaInicio
				+ ", tipo=" + tipo + ", temas=" + temas + ", instructor=" + instructor + ", soldados=" + soldados + "]";
	}
    

    
}

