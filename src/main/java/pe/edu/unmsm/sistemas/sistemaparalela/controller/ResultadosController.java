package pe.edu.unmsm.sistemas.sistemaparalela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.TablaResultadosDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.service.ResultadosService;

import java.io.File;

@RestController
@RequestMapping("/api/resultados")
public class ResultadosController {
    
    @Autowired
    private ResultadosService resultadosService;

    /**
     * Endpoint: GET /api/resultados/tabla
     * ENTRADA:
     *   - Parámetro opcional de consulta `area` (String): puede ser A, B, C, D o E.
     * PROCESO:
     *   1. Si se proporciona un área:
     *       - Verifica que sea válida (A–E).
     *       - Si es válida, llama a `resultadosService.obtenerResultadosPorArea(area)` para filtrar resultados.
     *       - Si no es válida, devuelve un DTO con mensaje de error y código 400.
     *   2. Si no se proporciona área, llama a `resultadosService.obtenerTodosLosResultados()` para obtener todos.
     * SALIDA:
     *   - Objeto `TablaResultadosDTO` con:
     *       - lista de resultados (exámenes corregidos y sus puntajes u observaciones),
     *       - mensaje (si aplica),
     *       - total de registros.
     *   - Código HTTP:
     *       - 200 OK: si se obtiene correctamente,
     *       - 400 BAD_REQUEST: si el área es inválida,
     *       - 500 INTERNAL_SERVER_ERROR: si ocurre algún fallo inesperado.
     */
    @GetMapping(value = "/tabla", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TablaResultadosDTO> obtenerTablaResultados(
            @RequestParam(required = false) String area) {
        try {
            TablaResultadosDTO resultados;
            
            if (area != null && !area.isEmpty()) {
                // Validar que el área sea válida
                if (!area.matches("[ABCDE]")) {
                    TablaResultadosDTO error = new TablaResultadosDTO();
                    error.setMensaje("Área inválida. Debe ser A, B, C, D o E");
                    error.setTotalResultados(0);
                    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
                }
                resultados = resultadosService.obtenerResultadosPorArea(area);
            } else {
                resultados = resultadosService.obtenerTodosLosResultados();
            }
            
            return new ResponseEntity<>(resultados, HttpStatus.OK);
            
        } catch (Exception e) {
            TablaResultadosDTO error = new TablaResultadosDTO();
            error.setMensaje("Error al obtener resultados: " + e.getMessage());
            error.setTotalResultados(0);
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Endpoint: GET /api/resultados/pdf
     * ENTRADA:
     *   - Parámetro opcional de consulta `area` (String): puede ser A, B, C, D o E.
     * PROCESO:
     *   1. Si se proporciona un área, se valida que sea una letra de A a E.
     *       - Si no es válida, retorna HTTP 400 Bad Request.
     *   2. Llama a `resultadosService.generarPdfResultados(area)` para generar un archivo PDF.
     *   3. Verifica si el archivo existe en disco.
     *   4. Prepara el archivo como un recurso descargable (FileSystemResource).
     *   5. Configura los headers para forzar la descarga del PDF.
     * SALIDA:
     *   - Un archivo PDF como respuesta (application/pdf),
     *     con headers para descarga (`Content-Disposition: attachment`).
     *   - Código HTTP:
     *       - 200 OK: si el archivo fue generado correctamente,
     *       - 400 BAD_REQUEST: si el área es inválida,
     *       - 404 NOT_FOUND: si el archivo no fue encontrado,
     *       - 500 INTERNAL_SERVER_ERROR: si ocurre un error durante el proceso.
     */
    @GetMapping("/pdf")
    public ResponseEntity<Resource> generarPdfResultados(
            @RequestParam(required = false) String area) {
        try {
            // Validar área si se proporciona
            if (area != null && !area.isEmpty() && !area.matches("[ABCDE]")) {
                return ResponseEntity.badRequest().build();
            }
            
            // Generar el PDF
            String rutaArchivo = resultadosService.generarPdfResultados(area);
            File archivo = new File(rutaArchivo);
            
            if (!archivo.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            // Crear resource para descarga
            Resource resource = new FileSystemResource(archivo);
            
            // Determinar nombre del archivo para descarga
            String nombreDescarga = area != null && !area.isEmpty() 
                ? "Resultados_Area_" + area + ".pdf"
                : "Resultados_Todos.pdf";
            
            // Configurar headers para descarga
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreDescarga + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(archivo.length())
                    .body(resource);
                    
        } catch (Exception e) {
            System.err.println("Error generando PDF de resultados: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
