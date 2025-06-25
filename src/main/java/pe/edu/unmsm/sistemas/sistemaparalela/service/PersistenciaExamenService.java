package pe.edu.unmsm.sistemas.sistemaparalela.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.*;
import pe.edu.unmsm.sistemas.sistemaparalela.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersistenciaExamenService {
    @PersistenceContext
    private EntityManager entityManager;
    private final PostulanteRepository postulanteRepo;
    private final ExamenRepository examenRepo;
    private final PreguntaRepository preguntaRepo;
    private final RespuestaRepository respuestaRepo;

    public Examen crearExamenConPostulante(long postulanteId, String tipoExamen) {
        Postulante postulante = postulanteRepo.findById(postulanteId)
                .orElseThrow(() -> new RuntimeException("Postulante no encontrado"));

        Examen examen = new Examen();
        examen.setPostulante(postulante);
        examen.setTipoexamen(tipoExamen);

        return examenRepo.save(examen);
    }

    public Pregunta crearPregunta(Examen examen, String enunciado) {
        Pregunta pregunta = new Pregunta();
        pregunta.setExamen(examen);
        pregunta.setEnunciadopregunta(enunciado);

        return preguntaRepo.save(pregunta);
    }
    @Transactional
    public void guardarTodo(List<Pregunta> preguntas) {
        preguntaRepo.saveAll(preguntas); // Hibernate guardará las respuestas por cascada
    }


    @Transactional
    public void reiniciarTablas() {
        entityManager.createQuery("DELETE FROM Respuesta").executeUpdate();
        entityManager.createQuery("DELETE FROM Pregunta").executeUpdate();
        entityManager.createQuery("DELETE FROM Examen").executeUpdate();
    }


}