package pe.edu.unmsm.sistemas.sistemaparalela.service;

import lombok.Data;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.Respuesta;

import java.util.List;

@Data
public class ExamenCompleto {
    private Long examenId;
    private Long postulanteId;
    private List<PreguntaCompleta> preguntas;
    
    @Data
    public static class PreguntaCompleta {
        private Long preguntaId;
        private String enunciado;
        private List<RespuestaSimple> respuestas;
    }
    
    @Data
    public static class RespuestaSimple {
        private Long respuestaId;
        private String textoRespuesta;
        private boolean esCorrecta;
    }
}
