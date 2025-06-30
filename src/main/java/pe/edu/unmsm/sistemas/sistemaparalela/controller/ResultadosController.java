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
     * Endpoint para obtener todos los resultados en formato tabla JSON
     * GET /api/resultados/tabla
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
     * Endpoint para generar y descargar PDF con los resultados
     * GET /api/resultados/pdf
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
