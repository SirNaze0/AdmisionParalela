package pe.edu.unmsm.sistemas.sistemaparalela.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="fichaopticas")
public class FichaOptica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fichaopticaid;
    private String respuestas;
    @ManyToOne
    @JoinColumn(name = "postulante")
    private Postulante postulante;
}
