package pe.edu.unmsm.sistemas.sistemaparalela.dto;

import lombok.Data;

@Data
public class FichaOpticaDTO {
    private Long codigoPostulante;
    private Long idExamen;
    private String respuestas; // "ABB*DE"
    
    public FichaOpticaDTO(Long codigoPostulante, Long idExamen, String respuestas) {
        this.codigoPostulante = codigoPostulante;
        this.idExamen = idExamen;
        this.respuestas = respuestas;
    }
}