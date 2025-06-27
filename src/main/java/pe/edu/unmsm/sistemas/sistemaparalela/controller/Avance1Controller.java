package pe.edu.unmsm.sistemas.sistemaparalela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.ArchivoGeneradoDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.service.Avance1Service;
import pe.edu.unmsm.sistemas.sistemaparalela.service.PersistenciaExamenService;

import java.io.IOException;

@RestController
@RequestMapping("/api/avance1")
public class Avance1Controller {
    @Autowired
    private Avance1Service avance1Service;
    @Autowired
    private PersistenciaExamenService persistenciaExamenService;
    //Endpoint para obtener los examenes y fichas opticas.
    @GetMapping(value = "/generar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArchivoGeneradoDTO> generarArchivos(@RequestParam Integer cantidad) throws IOException {
        long inicio = System.currentTimeMillis();
        ArchivoGeneradoDTO respuesta= avance1Service.comenzar(cantidad);
        long fin = System.currentTimeMillis();
        long duracion = fin - inicio;
        System.out.println("Tiempo total del request: " + duracion + " ms");
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
    //Endpoint para limpiar las tablas(Respuesta,Pregunta y Examen)
    @PostMapping("/limpiar")
    public ResponseEntity<Void> generarExamenes() {
        // Reiniciar tablas antes de ejecutar tod
        persistenciaExamenService.reiniciarTablas();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //Endpoint para guardar datos en H2(BP Y BR) POST PIRLO (RECIBE JSON(proveniente de un .csv))
    //Endpoint para guardar datos en H2(Postulantes) POST NAZE
    //Endpoint para Limpiar tablas en H2(BP y BR) POST PIRLO
    //Endpoint para Limpiar tabla en H2(Postulantes) POST NAZE

}
