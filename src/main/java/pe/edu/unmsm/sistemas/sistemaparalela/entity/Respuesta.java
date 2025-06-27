package pe.edu.unmsm.sistemas.sistemaparalela.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

@Entity
@BatchSize(size = 300)
@Data
@Table(name="respuestas")
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pregunta_seq")
    @SequenceGenerator(name = "pregunta_seq", sequenceName = "pregunta_seq", allocationSize = 300)
    private Long respuestaid;
    private String textorespuesta;
    private boolean escorrecta;
    @ManyToOne
    @JoinColumn(name="preguntaid")
    private Pregunta pregunta;
}
