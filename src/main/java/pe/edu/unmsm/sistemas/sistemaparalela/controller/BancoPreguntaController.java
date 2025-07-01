package pe.edu.unmsm.sistemas.sistemaparalela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.BancoPreguntaDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.ResultadoCargaDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.repository.BancoPreguntaRepository;
import pe.edu.unmsm.sistemas.sistemaparalela.service.BancoPreguntaService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/banco")
public class BancoPreguntaController {
    @Autowired
    private BancoPreguntaRepository bancoPreguntaRepository;
    @Autowired
    private BancoPreguntaService bancoPreguntaService;
    // Endpoint: GET /api/banco
    // ENTRADA: No recibe parámetros.
    // PROCESO:
    //   1. Obtiene todas las preguntas del repositorio `BancoPreguntaRepository`.
    //   2. Convierte cada entidad BancoPregunta a un DTO (`BancoPreguntaDTO`)
    //      extrayendo solo el enunciado y el curso.
    // SALIDA: Lista de objetos `BancoPreguntaDTO` con datos resumidos de las preguntas.
    @GetMapping
    public List<BancoPreguntaDTO> obtenerBancoPreguntas() {
        return bancoPreguntaRepository.findAll().stream().map( bancopregunta ->{
            BancoPreguntaDTO dto = new BancoPreguntaDTO();
            dto.setEnunciado(bancopregunta.getEnunciado());
            dto.setCurso(bancopregunta.getCurso());
            return dto;
        }).collect(Collectors.toList());
    }

    // Endpoint: POST /api/banco/guardar
    // ENTRADA: Un archivo .csv enviado como multipart/form-data (parametro "archivo").
    // PROCESO:
    //   1. Verifica que el archivo no esté vacío.
    //   2. Verifica que el archivo tenga extensión .csv.
    //   3. Llama al servicio `bancoPreguntaService.guardarBPBR()` para procesar y guardar
    //      preguntas y respuestas en la base de datos desde el CSV.
    // SALIDA: Un objeto `ResultadoCargaDTO` con:
    //   - mensaje de estado,
    //   - cantidad de registros insertados correctamente,
    //   - cantidad de errores,
    //   - detalles de los errores (lista de strings).
    @PostMapping(value = "/guardar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultadoCargaDTO> guardarDesdeCSVBPBR(@RequestParam("archivo") MultipartFile archivo) {
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

            ResultadoCargaDTO resultado = bancoPreguntaService.guardarBPBR(archivo);
            return new ResponseEntity<>(resultado, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(
                    new ResultadoCargaDTO("Error procesando archivo: " + e.getMessage(), 0, 1, List.of()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    // Endpoint: POST /api/banco/limpiar-bpbr
    // ENTRADA: No recibe parámetros.
    // PROCESO:
    //   1. Llama al servicio `bancoPreguntaService.limpiarBancoPreguntasYRespuestas()`
    //      para eliminar todos los registros de las tablas BancoPregunta y BancoRespuesta.
    // SALIDA: Código HTTP 200 (OK) sin cuerpo.
    @PostMapping("/limpiar-bpbr")
    public ResponseEntity<Void> limpiarBanco() {
        bancoPreguntaService.limpiarBancoPreguntasYRespuestas();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
