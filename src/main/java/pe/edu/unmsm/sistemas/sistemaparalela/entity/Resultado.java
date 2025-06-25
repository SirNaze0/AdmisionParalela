package pe.edu.unmsm.sistemas.sistemaparalela.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="resultados")
public class Resultado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultadoid;
    private Integer nota;
    @ManyToOne
    @JoinColumn(name = "postulanteid")
    private Postulante postulante;
}
