package pe.edu.unmsm.sistemas.sistemaparalela.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TablaResultadosDTO {
    private List<ResultadoTablaDTO> resultados;
    private int totalResultados;
    private String mensaje;
    
    public TablaResultadosDTO(List<ResultadoTablaDTO> resultados, int totalResultados) {
        this.resultados = resultados;
        this.totalResultados = totalResultados;
        this.mensaje = "Tabla de resultados generada correctamente";
    }
}
