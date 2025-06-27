package pe.edu.unmsm.sistemas.sistemaparalela.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Data
@Entity
@BatchSize(size = 300)
@Table(name="examenes")
public class Examen {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pregunta_seq")
    @SequenceGenerator(name = "pregunta_seq", sequenceName = "pregunta_seq", allocationSize = 300)
    private Long examenid;
    private String tipoexamen;
    @OneToMany(mappedBy = "examen", cascade = CascadeType.ALL)
    private List<Pregunta> preguntas;
    @ManyToOne
    @JoinColumn(name="postulanteid")
    private Postulante postulante;
}
