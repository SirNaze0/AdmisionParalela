package pe.edu.unmsm.sistemas.sistemaparalela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.ResultadoCorreccionDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.service.CorreccionService;

import java.io.IOException;

@RestController
@RequestMapping("/api/correccion")
public class CorreccionController {
    
    @Autowired
    private CorreccionService correccionService;
    // Endpoint: POST /api/correccion/procesar
    // ENTRADA:
    //   - Un archivo CSV cargado como formulario multipart (parametro "archivo").
    // PROCESO:
    //   1. Verifica si el archivo está vacío.
    //      - Si lo está, retorna una respuesta con mensaje "Archivo vacío" y estado HTTP 400 (BAD_REQUEST).
    //   2. Verifica que el archivo tenga extensión `.csv`.
    //      - Si no lo es, retorna una respuesta con mensaje "Solo se permiten archivos CSV" y estado HTTP 400.
    //   3. Si las validaciones son exitosas, se llama a `correccionService.procesar(archivo)` que:
    //        - Lee las fichas ópticas contenidas en el archivo,
    //        - Corrige las respuestas comparándolas con las respuestas correctas,
    //        - Calcula y devuelve resultados agregados como:
    //            - cantidad de exámenes corregidos,
    //            - cantidad de errores,
    //            - mensaje de estado.
    // SALIDA:
    //   - Un objeto `ResultadoCorreccionDTO` con:
    //       - mensaje descriptivo del resultado,
    //       - número de exámenes corregidos exitosamente,
    //       - número de errores encontrados.
    //   - Código HTTP 200 (OK) si la operación fue exitosa.
    //   - Código HTTP 400 si el archivo es inválido.
    //   - Código HTTP 500 si ocurre un error al procesar el archivo.
    @PostMapping(value = "/procesar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultadoCorreccionDTO> procesarFichasOpticas(
            @RequestParam("archivo") MultipartFile archivo) {
        
        try {
            // Validaciones básicas
            if (archivo.isEmpty()) {
                return new ResponseEntity<>(
                    new ResultadoCorreccionDTO("Archivo vacío", 0, 1), 
                    HttpStatus.BAD_REQUEST
                );
            }
            
            if (!archivo.getOriginalFilename().toLowerCase().endsWith(".csv")) {
                return new ResponseEntity<>(
                    new ResultadoCorreccionDTO("Solo se permiten archivos CSV", 0, 1), 
                    HttpStatus.BAD_REQUEST
                );
            }
            
            // Procesar
            ResultadoCorreccionDTO resultado = correccionService.procesar(archivo);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
            
        } catch (IOException e) {
            return new ResponseEntity<>(
                new ResultadoCorreccionDTO("Error procesando archivo: " + e.getMessage(), 0, 1), 
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                new ResultadoCorreccionDTO("Error inesperado: " + e.getMessage(), 0, 1), 
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}