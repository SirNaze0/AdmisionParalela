package pe.edu.unmsm.sistemas.sistemaparalela.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Entity
@Data
@BatchSize(size = 500)
@Table(name="postulantes")
public class Postulante {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pregunta_seq")
    @SequenceGenerator(name = "pregunta_seq", sequenceName = "pregunta_seq", allocationSize = 500)
    private Long postulanteid;
    private String nombres;
    private String apellidos;
    private String dni;
    private String carrera;
    private String area;
    @OneToMany(mappedBy = "postulante",cascade = CascadeType.ALL)
    private List<Examen> examenes;
    @OneToMany(mappedBy = "postulante",cascade = CascadeType.ALL)
    private List<Resultado> resultados;
    @OneToMany(mappedBy = "postulante",cascade = CascadeType.ALL)
    private List<FichaOptica> fichaOpticas;
}
