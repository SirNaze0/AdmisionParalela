package pe.edu.unmsm.sistemas.sistemaparalela.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.*;
import pe.edu.unmsm.sistemas.sistemaparalela.repository.BancoPreguntaRepository;
import pe.edu.unmsm.sistemas.sistemaparalela.repository.PostulanteRepository;

import java.io.*;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class GeneradorParalelo{
    @Autowired
    private BancoPreguntaRepository bancoPreguntaRepository;
    @Autowired
    private PostulanteRepository postulanteRepository;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private PersistenciaExamenService persistenciaExamenService;

    public String[] generar(int cant) throws IOException {
        List<BancoPregunta> bancoPreguntas = bancoPreguntaRepository.findAll();
        long cantidad = cant;

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<ResultadoExamen>> resultados = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            String nombre = "Tarea" + (i + 1);
            List<BancoPregunta> copia = bancoPreguntas.stream()
                    .map(BancoPregunta::clonar)
                    .collect(Collectors.toList());
            long postulanteId = i + 1;
            ExamenParalelo tarea = new ExamenParalelo(
                    copia, nombre, pdfService, persistenciaExamenService, postulanteId
            );
            resultados.add(executor.submit(tarea));
        }
        executor.shutdown();
        List<String> nombresArchivos = new ArrayList<>();
        List<ResultadoExamen> resultadosExamen = new ArrayList<>();

        for (Future<ResultadoExamen> futuro : resultados) {
            try {
                ResultadoExamen res = futuro.get();
                if (res != null) {
                    nombresArchivos.add(res.getArchivoPdf());
                    resultadosExamen.add(res);
                }
            } catch (Exception e) {
                System.out.println("Error al procesar futuro: " + e.getMessage());
            }
        }
        // Guardado asincrónico en segundo plano
        guardarAsincrono(resultadosExamen);
        return nombresArchivos.toArray(new String[0]);
    }
    @Async
    public void guardarAsincrono(List<ResultadoExamen> resultados) {
        for (ResultadoExamen res : resultados) {
            try {
                Examen examen = persistenciaExamenService.crearExamenConPostulante(res.getPostulanteId(), "paralelo");
                List<Pregunta> preguntasBatch = new ArrayList<>();
                for (BancoPregunta bp : res.getPreguntas()) {
                    Pregunta pregunta = new Pregunta();
                    pregunta.setEnunciadopregunta(bp.getEnunciado());
                    pregunta.setExamen(examen);

                    List<Respuesta> respuestas = new ArrayList<>();
                    for (BancoRespuesta br : bp.getBancoRespuestas()) {
                        Respuesta respuesta = new Respuesta();
                        respuesta.setPregunta(pregunta); // relación inversa
                        respuesta.setTextorespuesta(br.getTextorespuesta());
                        respuesta.setEscorrecta(br.getEscorrecta());
                        respuestas.add(respuesta);
                    }
                    // Relación bidireccional
                    pregunta.setRespuestas(respuestas);

                    preguntasBatch.add(pregunta);
                }
                // Solo pasas preguntas — respuestas se guardan por cascade
                persistenciaExamenService.guardarTodo(preguntasBatch);
            } catch (Exception e) {
                System.out.println("Error en guardado asincrónico para postulante " + res.getPostulanteId() + ": " + e.getMessage());
            }
        }
    }
}
