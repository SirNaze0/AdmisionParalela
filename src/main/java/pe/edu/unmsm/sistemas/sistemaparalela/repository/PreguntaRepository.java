package pe.edu.unmsm.sistemas.sistemaparalela.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.Pregunta;

public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
}
