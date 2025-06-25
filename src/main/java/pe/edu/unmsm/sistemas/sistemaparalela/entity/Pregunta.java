package pe.edu.unmsm.sistemas.sistemaparalela.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Entity
@BatchSize(size = 500)
@Data
@Table(name="preguntas")
public class Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pregunta_seq")
    @SequenceGenerator(name = "pregunta_seq", sequenceName = "pregunta_seq", allocationSize = 500)
    private Long preguntaid;
    @Column(columnDefinition = "text")
    private String enunciadopregunta;
    @ManyToOne
    @JoinColumn(name="examenid")
    private Examen examen;
    @OneToMany(mappedBy = "pregunta",cascade = CascadeType.ALL)
    private List<Respuesta> respuestas;

}
