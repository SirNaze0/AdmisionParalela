package pe.edu.unmsm.sistemas.sistemaparalela.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.Examen;

public interface ExamenRepository extends JpaRepository<Examen, Long> {
}
