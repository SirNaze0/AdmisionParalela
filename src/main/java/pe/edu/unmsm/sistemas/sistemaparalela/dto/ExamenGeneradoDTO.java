package pe.edu.unmsm.sistemas.sistemaparalela.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamenGeneradoDTO {
    private Long examenId;
    private Long postulanteId;
    private String nombresPostulante;
    private String apellidosPostulante;
    private String dni;
    private String carrera;
    private String area;
    private String rutaPdf;
    private String nombreArchivoPdf;
    private String urlDescarga;
}
