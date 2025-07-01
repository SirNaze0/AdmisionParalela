package pe.edu.unmsm.sistemas.sistemaparalela.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class ResultadoCargaDTO {
        private String mensaje;
        private int registrosExitosos;
        private int registrosErroneos;
        private List<String> detalles; // Ej: lista de enunciados o nombres que sí se guardaron
}
