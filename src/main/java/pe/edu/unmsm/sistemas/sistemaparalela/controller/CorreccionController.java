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