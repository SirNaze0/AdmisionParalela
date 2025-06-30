package pe.edu.unmsm.sistemas.sistemaparalela.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoTablaDTO {
    private Long postulanteId;
    private String nombres;
    private String apellidos;
    private String dni;
    private String carrera;
    private String area;
    private Integer nota;
    private Integer posicion; // Para ranking
}
