package pe.edu.unmsm.sistemas.sistemaparalela.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.FichaOpticaDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.repository.ExamenRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class CorreccionGenerador {
    
    @Autowired
    private ExamenRepository examenRepository;
    
    @Autowired
    private PersistenciaExamenService persistenciaExamenService;

    @Transactional(readOnly = true)
    public int[] corregir(List<FichaOpticaDTO> fichasOpticas) {
        // Pre-cargar todos los exámenes con sus datos completos ANTES del procesamiento paralelo
        Map<Long, ExamenCompleto> examenesCompletos = new HashMap<>();
        
        for (FichaOpticaDTO ficha : fichasOpticas) {
            if (!examenesCompletos.containsKey(ficha.getIdExamen())) {
                ExamenCompleto examenCompleto = cargarExamenCompleto(ficha.getIdExamen());
                if (examenCompleto != null) {
                    examenesCompletos.put(ficha.getIdExamen(), examenCompleto);
                }
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<ResultadoCorreccionIndividual>> resultados = new ArrayList<>();

        // Crear una tarea por cada examen
        for (int i = 0; i < fichasOpticas.size(); i++) {
            FichaOpticaDTO ficha = fichasOpticas.get(i);
            String nombreTarea = "CorreccionTarea" + (i + 1);
            
            ExamenCompleto examenCompleto = examenesCompletos.get(ficha.getIdExamen());
            
            CorreccionParalela tarea = new CorreccionParalela(
                ficha, examenCompleto, nombreTarea
            );
            resultados.add(executor.submit(tarea));
        }

        executor.shutdown();

        // Recopilar resultados
        List<ResultadoCorreccionIndividual> resultadosCorreccion = new ArrayList<>();
        int exitosos = 0;
        int errores = 0;

        for (Future<ResultadoCorreccionIndividual> futuro : resultados) {
            try {
                ResultadoCorreccionIndividual res = futuro.get();
                if (res != null) {
                    resultadosCorreccion.add(res);
                    if (res.isExitoso()) {
                        exitosos++;
                    } else {
                        errores++;
                        System.out.println("Error en corrección: " + res.getError());
                    }
                }
            } catch (Exception e) {
                errores++;
                System.out.println("Error al procesar futuro: " + e.getMessage());
            }
        }

        // Guardado asíncrono
        guardarResultadosAsincrono(resultadosCorreccion);

        return new int[]{exitosos, errores};
    }
    
    private ExamenCompleto cargarExamenCompleto(Long examenId) {
        try {
            // Buscar el examen con preguntas
            var examen = examenRepository.findByIdWithPreguntas(examenId).orElse(null);
            if (examen == null) {
                return null;
            }
            
            ExamenCompleto examenCompleto = new ExamenCompleto();
            examenCompleto.setExamenId(examen.getExamenid());
            examenCompleto.setPostulanteId(examen.getPostulante() != null ? examen.getPostulante().getPostulanteid() : null);
            
            List<ExamenCompleto.PreguntaCompleta> preguntasCompletas = new ArrayList<>();
            
            for (var pregunta : examen.getPreguntas()) {
                ExamenCompleto.PreguntaCompleta preguntaCompleta = new ExamenCompleto.PreguntaCompleta();
                preguntaCompleta.setPreguntaId(pregunta.getPreguntaid());
                preguntaCompleta.setEnunciado(pregunta.getEnunciadopregunta());
                
                // Forzar carga de respuestas y convertir a objetos simples
                List<ExamenCompleto.RespuestaSimple> respuestasSimples = new ArrayList<>();
                for (var respuesta : pregunta.getRespuestas()) {
                    ExamenCompleto.RespuestaSimple respuestaSimple = new ExamenCompleto.RespuestaSimple();
                    respuestaSimple.setRespuestaId(respuesta.getRespuestaid());
                    respuestaSimple.setTextoRespuesta(respuesta.getTextorespuesta());
                    respuestaSimple.setEsCorrecta(respuesta.isEscorrecta());
                    respuestasSimples.add(respuestaSimple);
                }
                
                preguntaCompleta.setRespuestas(respuestasSimples);
                preguntasCompletas.add(preguntaCompleta);
            }
            
            examenCompleto.setPreguntas(preguntasCompletas);
            return examenCompleto;
            
        } catch (Exception e) {
            System.out.println("Error cargando examen " + examenId + ": " + e.getMessage());
            return null;
        }
    }

    @Async
    public void guardarResultadosAsincrono(List<ResultadoCorreccionIndividual> resultados) {
        for (ResultadoCorreccionIndividual res : resultados) {
            if (res.isExitoso()) {
                try {
                    // Guardar en tabla resultados
                    persistenciaExamenService.guardarResultado(res.getPostulanteId(), res.getNota());
                } catch (Exception e) {
                    System.out.println("Error guardando resultado para postulante " + 
                        res.getPostulanteId() + ": " + e.getMessage());
                }
            }
        }
    }
}
