package pe.edu.unmsm.sistemas.sistemaparalela.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class GeneradorParalelo {
    @Autowired
    private BancoPreguntaRepository bancoPreguntaRepository;
    @Autowired
    private PostulanteRepository postulanteRepository;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private PersistenciaExamenService persistenciaExamenService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String[] generar(int cant) throws IOException {
        List<BancoPregunta> bancoPreguntas = bancoPreguntaRepository.findAll();
        List<Postulante> postulantes = postulanteRepository.findAll();

        // Validar que hay suficientes postulantes
        if (postulantes.size() < cant) {
            throw new IllegalArgumentException(
                    "No hay suficientes postulantes cargados. Se necesitan " + cant +
                            " pero solo hay " + postulantes.size() + ". Debe cargar más postulantes primero."
            );
        }

        long cantidad = cant;
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<ResultadoExamen>> resultados = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            String nombre = "Tarea" + (i + 1);
            List<BancoPregunta> copia = bancoPreguntas.stream()
                    .map(BancoPregunta::clonar)
                    .collect(Collectors.toList());
            long postulanteId = postulantes.get(i).getPostulanteid(); // Usar ID real del postulante
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
        // Usar directamente SQL Batch - LA MÁS RÁPIDA
        guardarConSqlBatch(resultados);
    }

    private void guardarConSqlBatch(List<ResultadoExamen> resultados) {
        System.out.println("Iniciando guardado batch para " + resultados.size() + " exámenes...");
        long startTime = System.currentTimeMillis();

        try {
            // Preparar todos los datos en memoria
            List<Object[]> examenesData = new ArrayList<>();
            List<Object[]> preguntasData = new ArrayList<>();
            List<Object[]> respuestasData = new ArrayList<>();

            // Usar nextval directamente en el loop (más simple)
            for (ResultadoExamen res : resultados) {
                // Obtener ID para el examen
                Long examenId = jdbcTemplate.queryForObject("SELECT nextval('pregunta_seq')", Long.class);

                // Datos del examen
                examenesData.add(new Object[]{
                        examenId,
                        "paralelo", // tipoexamen
                        res.getPostulanteId() // postulanteid
                });

                // Procesar preguntas - ajustar según tu clase ResultadoExamen
                for (BancoPregunta bp : res.getPreguntas()) { // Cambiar por el metodo correcto
                    Long preguntaId = jdbcTemplate.queryForObject("SELECT nextval('pregunta_seq')", Long.class);

                    // Datos de la pregunta
                    preguntasData.add(new Object[]{
                            preguntaId,
                            bp.getEnunciado(), // enunciadopregunta
                            examenId // examenid
                    });

                    // Procesar respuestas
                    for (BancoRespuesta br : bp.getBancoRespuestas()) {
                        Long respuestaId = jdbcTemplate.queryForObject("SELECT nextval('pregunta_seq')", Long.class);

                        respuestasData.add(new Object[]{
                                respuestaId,
                                br.getTextorespuesta(), // textorespuesta
                                br.getEscorrecta(), // escorrecta (boolean directo)
                                preguntaId // preguntaid
                        });
                    }
                }
            }

            System.out.println("Datos preparados: " + examenesData.size() + " exámenes, " +
                    preguntasData.size() + " preguntas, " + respuestasData.size() + " respuestas");

            // Ejecutar inserts en batch usando los nombres reales de tu BD
            jdbcTemplate.batchUpdate(
                    "INSERT INTO examenes (examenid, tipoexamen, postulanteid) VALUES (?, ?, ?)",
                    examenesData
            );
            System.out.println("✓ Exámenes guardados");

            jdbcTemplate.batchUpdate(
                    "INSERT INTO preguntas (preguntaid, enunciadopregunta, examenid) VALUES (?, ?, ?)",
                    preguntasData
            );
            System.out.println("✓ Preguntas guardadas");

            jdbcTemplate.batchUpdate(
                    "INSERT INTO respuestas (respuestaid, textorespuesta, escorrecta, preguntaid) VALUES (?, ?, ?, ?)",
                    respuestasData
            );
            System.out.println("✓ Respuestas guardadas");

            long endTime = System.currentTimeMillis();
            System.out.println("🚀 Guardado batch completado en " + (endTime - startTime) + "ms para " + resultados.size() + " exámenes");

        } catch (Exception e) {
            System.err.println("❌ Error en guardado batch: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error en guardado batch", e);
        }
    }

}
