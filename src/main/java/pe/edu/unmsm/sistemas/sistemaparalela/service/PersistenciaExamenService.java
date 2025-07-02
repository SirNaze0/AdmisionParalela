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
    
    @Transactional
    public void limpiarBaseDatosCompleta() {
        // Limpiar en orden correcto para evitar problemas de FK
        entityManager.createQuery("DELETE FROM Respuesta").executeUpdate();
        entityManager.createQuery("DELETE FROM Pregunta").executeUpdate();
        entityManager.createQuery("DELETE FROM Examen").executeUpdate();
        entityManager.createQuery("DELETE FROM Resultado").executeUpdate();
        entityManager.createQuery("DELETE FROM Postulante").executeUpdate();
        entityManager.createQuery("DELETE FROM BancoPregunta").executeUpdate();
        System.out.println("Base de datos completamente limpiada");
    }

    @Transactional
    public void guardarResultado(Long postulanteId, int nota) {
        Postulante postulante = postulanteRepo.findById(postulanteId)
                .orElseThrow(() -> new RuntimeException("Postulante no encontrado"));

        Resultado resultado = new Resultado();
        resultado.setPostulante(postulante);
        resultado.setNota(nota);
        
        entityManager.persist(resultado);
    }
}