package pe.edu.unmsm.sistemas.sistemaparalela.dto;

import lombok.Data;

@Data
public class ResultadoCorreccionDTO {
    private String mensaje;
    private int examenesCorregidos;
    private int errores;
    
    public ResultadoCorreccionDTO(String mensaje, int examenesCorregidos, int errores) {
        this.mensaje = mensaje;
        this.examenesCorregidos = examenesCorregidos;
        this.errores = errores;
    }
}