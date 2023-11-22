package gei.id.tutelado.model;

import java.time.LocalDate;

import javax.persistence.*;

@TableGenerator(name="xeradorIdsPersonas", table="taboa_ids",
pkColumnName="nome_id", pkColumnValue="idPersona",
valueColumnName="ultimo_valor_id",
initialValue=0, allocationSize=1)

@NamedQueries ({
	@NamedQuery (name="Persona.recuperaPorDni",
				 query="SELECT p FROM Persona p where p.dni=:dni"),
	@NamedQuery (name="Persona.recuperaPorSoldado",
	 query="SELECT p FROM Soldado p where p.dni=:dni"),
	
})

@Entity
@Inheritance (strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name="t_Pers_tcc")
public abstract class Persona  {	
	@Id
    @GeneratedValue(generator="xeradorIdsPersonas")
    @Column(name="id_pers")
    private Long id;
	
    @Column(unique = true, nullable = false)
    private String dni;

    @Column(unique = false, nullable = false)
    private String nombre;

    @Column(unique =false, nullable = false)
    private LocalDate fechaAlta;

    @Column(unique = false, nullable = false)
    private LocalDate fechaNacimiento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dni == null) ? 0 : dni.hashCode());
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
		Persona other = (Persona) obj;
		if (dni == null) {
			if (other.dni != null)
				return false;
		} else if (!dni.equals(other.dni))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Persona [id=" + id + ", dni=" + dni + ", nombre=" + nombre + ", fechaAlta=" + fechaAlta
				+ ", fechaNacimiento=" + fechaNacimiento + "]";
	}

	
	

}
