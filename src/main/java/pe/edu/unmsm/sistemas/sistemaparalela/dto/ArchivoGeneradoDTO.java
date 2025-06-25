package pe.edu.unmsm.sistemas.sistemaparalela.dto;

import lombok.Data;

@Data
public class ArchivoGeneradoDTO {
    private String nombreArchivo;
    private String urlDescarga;

    public ArchivoGeneradoDTO(String nombreArchivo, String urlDescargar) {
        this.nombreArchivo = nombreArchivo;
        this.urlDescarga=urlDescargar;
    }
}
