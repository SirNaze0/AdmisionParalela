package pe.edu.unmsm.sistemas.sistemaparalela.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.Examen;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.Pregunta;

import java.util.List;
import java.util.Optional;

public interface ExamenRepository extends JpaRepository<Examen, Long> {
    
    @Query("SELECT e FROM Examen e LEFT JOIN FETCH e.preguntas WHERE e.examenid = :id")
    Optional<Examen> findByIdWithPreguntas(@Param("id") Long id);
    
    // Método comentado que causaba problemas de sintaxis
    // @Query("SELECT p FROM Pregunta p JOIN FETCH p.respuestas WHERE p.examen.examenid = :examenId ORDER BY p.preguntaid")
    // List<Pregunta> findPreguntasWithRespuestasByExamenId(@Param("examenId") Long examenId);
    
    // Método alternativo usando @EntityGraph (comentado por si quieres probarlo)
    // @EntityGraph(attributePaths = {"preguntas"})
    // Optional<Examen> findByExamenid(Long examenid);
}
