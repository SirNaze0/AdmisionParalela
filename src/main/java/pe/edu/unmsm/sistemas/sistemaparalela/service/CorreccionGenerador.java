package pe.edu.unmsm.sistemas.sistemaparalela.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.FichaOpticaDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.Resultado;
import pe.edu.unmsm.sistemas.sistemaparalela.repository.ExamenRepository;
import pe.edu.unmsm.sistemas.sistemaparalela.repository.PostulanteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class CorreccionGenerador {

    @Autowired
    private ExamenRepository examenRepository;

    @Autowired
    private PostulanteRepository postulanteRepository;

    @Autowired
    private PersistenciaExamenService persistenciaExamenService;

    public int[] corregir(List<FichaOpticaDTO> fichasOpticas) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<ResultadoCorreccionIndividual>> resultados = new ArrayList<>();

        // Crear una tarea por cada examen
        for (int i = 0; i < fichasOpticas.size(); i++) {
            FichaOpticaDTO ficha = fichasOpticas.get(i);
            String nombreTarea = "CorreccionTarea" + (i + 1);

            CorreccionParalela tarea = new CorreccionParalela(
                    ficha, examenRepository, nombreTarea
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