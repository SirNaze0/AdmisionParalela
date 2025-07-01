package pe.edu.unmsm.sistemas.sistemaparalela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.ResultadoCargaDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.service.PostulanteService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/Postulante")
public class PostulanteController {
    @Autowired
    private PostulanteService postulanteService;
    // Endpoint: POST /api/Postulante/guardar
    // ENTRADA:
    //   - Un archivo CSV enviado como formulario multipart (parametro "archivo").
    // PROCESO:
    //   1. Verifica si el archivo está vacío; si lo está, retorna BAD_REQUEST.
    //   2. Verifica que la extensión del archivo sea .csv; si no lo es, retorna BAD_REQUEST.
    //   3. Llama al mEtodo `postulanteService.guardarPostulantes(archivo)` que:
    //        - lee el contenido del archivo CSV,
    //        - guarda los postulantes en la base de datos H2.
    // SALIDA:
    //   - Un objeto `ResultadoCargaDTO` con:
    //       - mensaje del resultado,
    //       - cantidad de registros correctos,
    //       - cantidad de errores,
    //       - lista de errores (si los hay).
    //   - Código HTTP 200 OK si la carga fue exitosa.
    //   - Código HTTP 400 o 500 si hay errores de validación o procesamiento.
    @PostMapping(value = "/guardar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultadoCargaDTO> guardarDesdeCSVPostulantes(@RequestParam("archivo") MultipartFile archivo) {
        try {
            if (archivo.isEmpty()) {
                return new ResponseEntity<>(
                        new ResultadoCargaDTO("Archivo vacío", 0, 1, List.of()),
                        HttpStatus.BAD_REQUEST
                );
            }

            if (!archivo.getOriginalFilename().toLowerCase().endsWith(".csv")) {
                return new ResponseEntity<>(
                        new ResultadoCargaDTO("Solo se permiten archivos CSV", 0, 1, List.of()),
                        HttpStatus.BAD_REQUEST
                );
            }

            ResultadoCargaDTO resultado = postulanteService.guardarPostulantes(archivo);
            return new ResponseEntity<>(resultado, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(
                    new ResultadoCargaDTO("Error procesando archivo: " + e.getMessage(), 0, 1, List.of()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    // Endpoint: POST /api/Postulante/limpiar-postulantes
    // ENTRADA:
    //   - No recibe parámetros.
    // PROCESO:
    //   1. Llama al mEtodo `postulanteService.limpiarPostulantes()` para borrar
    //      todos los registros de postulantes en la base de datos H2.
    // SALIDA:
    //   - Código HTTP 200 OK sin contenido.
    @PostMapping("/limpiar-postulantes")
    public ResponseEntity<Void> limpiarPostulantes() {
        postulanteService.limpiarPostulantes();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
