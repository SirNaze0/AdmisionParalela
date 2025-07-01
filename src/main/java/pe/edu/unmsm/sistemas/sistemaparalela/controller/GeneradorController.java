package pe.edu.unmsm.sistemas.sistemaparalela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.ArchivoGeneradoDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.service.GeneradorService;
import pe.edu.unmsm.sistemas.sistemaparalela.service.PersistenciaExamenService;

import java.io.IOException;

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
}
