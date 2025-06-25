package pe.edu.unmsm.sistemas.sistemaparalela.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="bancorespuestas")
public class BancoRespuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bancorespuestaid;
    private String textorespuesta;
    private Boolean escorrecta;
    @ManyToOne
    @JoinColumn(name="bancopreguntasid") // nombre de la columna en la BD
    private BancoPregunta bancoPregunta;  // nombre de la propiedad usada en mappedBy
    public BancoRespuesta clonar() {
        BancoRespuesta clon = new BancoRespuesta();
        clon.setTextorespuesta(this.textorespuesta);
        clon.setEscorrecta(this.escorrecta);
        return clon;
    }
}
