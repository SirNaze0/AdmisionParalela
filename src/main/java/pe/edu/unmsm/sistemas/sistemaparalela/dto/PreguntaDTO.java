package pe.edu.unmsm.sistemas.sistemaparalela.dto;

import lombok.Data;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.Respuesta;

import java.util.List;

@Data
public class PreguntaDTO {
    private String enunciadopregunta;
    private List<Respuesta> respuestas;
}
