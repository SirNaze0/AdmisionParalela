package pe.edu.unmsm.sistemas.sistemaparalela.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.ResultadoCargaDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.Postulante;
import pe.edu.unmsm.sistemas.sistemaparalela.repository.PostulanteRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
@Service
public class PostulanteService {
    @Autowired
    private PostulanteRepository postulanteRepository;
    @Autowired
    private SequenceResetService sequenceResetService;
    public ResultadoCargaDTO guardarPostulantes(MultipartFile archivo) throws IOException {
        List<Postulante> postulantes = new ArrayList<>();
        List<String> detalles = new ArrayList<>();
        int errores = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(archivo.getInputStream()))) {
            String linea;
            int numeroLinea = 0;

            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                if (numeroLinea == 1 && linea.toLowerCase().contains("nombres")) continue;

                try {
                    String[] partes = linea.split(",");
                    if (partes.length < 5) {
                        throw new IllegalArgumentException("Se esperaban 5 columnas");
                    }

                    Postulante p = new Postulante();
                    p.setNombres(partes[0].trim());
                    p.setApellidos(partes[1].trim());
                    p.setDni(partes[2].trim());
                    p.setCarrera(partes[3].trim());
                    p.setArea(partes[4].trim());

                    postulantes.add(p);
                    detalles.add(p.getNombres() + " " + p.getApellidos());
                } catch (Exception e) {
                    errores++;
                }
            }
        }

        postulanteRepository.saveAll(postulantes);
        return new ResultadoCargaDTO("Carga finalizada", postulantes.size(), errores, detalles);
    }
    public void limpiarPostulantes() {
        postulanteRepository.deleteAll();
        sequenceResetService.reiniciarSecuenciaPostulante(1000);
    }
}
