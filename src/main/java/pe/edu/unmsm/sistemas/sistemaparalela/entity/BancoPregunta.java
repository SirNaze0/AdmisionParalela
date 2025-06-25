package pe.edu.unmsm.sistemas.sistemaparalela.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="bancopreguntas")
public class BancoPregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bancopreguntasid;
    @Column(columnDefinition = "text")
    private String enunciado;
    private String curso;
    @OneToMany(mappedBy = "bancoPregunta", cascade = CascadeType.ALL)
    private List<BancoRespuesta> bancoRespuestas;

    public BancoPregunta clonar() {
        BancoPregunta clon = new BancoPregunta();
        clon.setEnunciado(this.enunciado);

        List<BancoRespuesta> respuestasClonadas = new ArrayList<>();
        for (BancoRespuesta r : this.bancoRespuestas) {
            respuestasClonadas.add(r.clonar());
        }

        clon.setBancoRespuestas(respuestasClonadas);
        return clon;
    }
}
