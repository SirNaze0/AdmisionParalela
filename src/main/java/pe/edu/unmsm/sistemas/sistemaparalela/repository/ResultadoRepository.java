package pe.edu.unmsm.sistemas.sistemaparalela.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.Resultado;

import java.util.List;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Long> {
    
    @Query("SELECT r FROM Resultado r JOIN FETCH r.postulante ORDER BY r.nota DESC, r.postulante.apellidos, r.postulante.nombres")
    List<Resultado> findAllWithPostulanteOrderByNotaDesc();
    
    @Query("SELECT r FROM Resultado r JOIN FETCH r.postulante WHERE r.postulante.area = :area ORDER BY r.nota DESC, r.postulante.apellidos, r.postulante.nombres")
    List<Resultado> findByAreaOrderByNotaDesc(String area);
}
