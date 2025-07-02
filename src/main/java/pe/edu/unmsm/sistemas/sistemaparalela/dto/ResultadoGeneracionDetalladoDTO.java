package pe.edu.unmsm.sistemas.sistemaparalela.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoGeneracionDetalladoDTO {
    private String mensaje;
    private int totalExamenes;
    private String nombreZip;
    private String urlZipCompleto;
    private List<ExamenGeneradoDTO> examenesGenerados;
    private String tiempoGeneracion;
}
