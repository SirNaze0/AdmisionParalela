package pe.edu.unmsm.sistemas.sistemaparalela.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SequenceResetService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void reiniciarSecuenciaPostulante(long siguienteValor) {
        entityManager.createNativeQuery("ALTER SEQUENCE pregunta_seq RESTART WITH " + siguienteValor)
                .executeUpdate();
    }
}