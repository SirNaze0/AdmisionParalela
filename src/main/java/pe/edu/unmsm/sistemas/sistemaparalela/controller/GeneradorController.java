package pe.edu.unmsm.sistemas.sistemaparalela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.ArchivoGeneradoDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.ExamenGeneradoDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.ResultadoGeneracionDetalladoDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.service.GeneradorService;
import pe.edu.unmsm.sistemas.sistemaparalela.service.PersistenciaExamenService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/generador")
public class GeneradorController {
    @Autowired
    private GeneradorService generadorService;
    @Autowired
    private PersistenciaExamenService persistenciaExamenService;
    // Endpoint: GET /api/generador/generar
    // ENTRADA:
    //   - Parámetro de consulta (query param) `cantidad`: número de exámenes a generar.
    // PROCESO:
    //   1. Se registra el tiempo de inicio.
    //   2. Llama al servicio `generadorService.comenzar(cantidad)` que genera:
    //        - una lista de exámenes con sus preguntas y alternativas,
    //        - las fichas ópticas correspondientes.
    //   3. Se mide el tiempo total de ejecución del proceso.
    // SALIDA:
    //   - Objeto `ArchivoGeneradoDTO` que contiene:
    //        - lista de exámenes generados,
    //        - fichas ópticas o archivos relacionados (según implementación del DTO).
    //   - Código HTTP 200 OK.
    @GetMapping(value = "/generar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArchivoGeneradoDTO> generarArchivos(@RequestParam Integer cantidad) throws IOException {
        long inicio = System.currentTimeMillis();
        ArchivoGeneradoDTO respuesta= generadorService.comenzar(cantidad);
        long fin = System.currentTimeMillis();
        long duracion = fin - inicio;
        System.out.println("Tiempo total del request: " + duracion + " ms");
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
    // Endpoint: POST /api/generador/limpiar
    // ENTRADA:
    //   - No recibe parámetros.
    // PROCESO:
    //   1. Llama al servicio `persistenciaExamenService.reiniciarTablas()`,
    //      el cual elimina o reinicia las tablas relacionadas con:
    //        - exámenes,
    //        - preguntas de examen,
    //        - respuestas.
    // SALIDA:
    //   - Código HTTP 200 OK sin cuerpo.
    @PostMapping("/limpiar")
    public ResponseEntity<Void> limpiarExamenes() {
        // Reiniciar tablas antes de ejecutar tod
        persistenciaExamenService.reiniciarTablas();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    // Endpoint: POST /api/generador/limpiar-todo
    // ENTRADA: No recibe parámetros.
    // PROCESO: Elimina TODOS los datos de la base de datos (postulantes, exámenes, resultados, banco de preguntas).
    // SALIDA: Código HTTP 200 OK.
    @PostMapping("/limpiar-todo")
    public ResponseEntity<Void> limpiarBaseDatosCompleta() {
        persistenciaExamenService.limpiarBaseDatosCompleta();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    // Endpoint: GET /api/generador/examenes-detalle
    // ENTRADA: No recibe parámetros.
    // PROCESO: Obtiene todos los exámenes generados con información del postulante asignado.
    // SALIDA: Lista de ExamenGeneradoDTO con detalles completos de cada examen.
    @GetMapping("/examenes-detalle")
    public ResponseEntity<List<ExamenGeneradoDTO>> obtenerExamenesDetalle() {
        List<ExamenGeneradoDTO> examenes = generadorService.obtenerExamenesGenerados();
        return new ResponseEntity<>(examenes, HttpStatus.OK);
    }
    
    // Endpoint: GET /api/generador/descargar-examen/{examenId}
    // ENTRADA: ID del examen como parámetro de la URL.
    // PROCESO: Busca el archivo PDF del examen y lo devuelve para descarga.
    // SALIDA: Archivo PDF del examen individual.
    @GetMapping("/descargar-examen/{examenId}")
    public ResponseEntity<org.springframework.core.io.Resource> descargarExamenIndividual(@PathVariable Long examenId) {
        try {
            org.springframework.core.io.Resource archivo = generadorService.obtenerArchivoPdfExamen(examenId);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + archivo.getFilename() + "\"")
                    .body(archivo);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
