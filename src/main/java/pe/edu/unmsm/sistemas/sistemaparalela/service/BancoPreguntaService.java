package pe.edu.unmsm.sistemas.sistemaparalela.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.ResultadoCargaDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.BancoPregunta;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.BancoRespuesta;
import pe.edu.unmsm.sistemas.sistemaparalela.repository.BancoPreguntaRepository;
import pe.edu.unmsm.sistemas.sistemaparalela.repository.BancoRespuestaRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class BancoPreguntaService {
    @Autowired
    private BancoPreguntaRepository bancoPreguntaRepository;
    @Autowired
    private BancoRespuestaRepository bancoRespuestaRepository;
    public void limpiarBancoPreguntasYRespuestas() {
        bancoRespuestaRepository.deleteAll();
        bancoPreguntaRepository.deleteAll();
    }
    public ResultadoCargaDTO guardarBPBR(MultipartFile archivo) throws IOException {
        List<String> detalles = new ArrayList<>();
        int errores = 0;
        int registrosCorrectos = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(archivo.getInputStream()))) {
            String linea;
            int lineaNum = 0;

            while ((linea = reader.readLine()) != null) {
                lineaNum++;

                // Ignorar encabezado
                if (lineaNum == 1 && linea.toLowerCase().contains("enunciado")) continue;

                try {
                    String[] partes = linea.split(",", -1); // mantener campos vacíos

                    if (partes.length < 7)
                        throw new IllegalArgumentException("Faltan columnas (esperado: 7)");

                    // Crear pregunta
                    BancoPregunta pregunta = new BancoPregunta();
                    pregunta.setEnunciado(partes[0].trim());
                    pregunta.setCurso(partes[1].trim());
                    List<BancoRespuesta> respuestas = new ArrayList<>();

                    // Letras correspondientes a las opciones
                    String[] letras = {"A", "B", "C", "D"};
                    String letraCorrecta = partes[6].trim().toUpperCase();

                    for (int i = 0; i < 4; i++) {
                        BancoRespuesta respuesta = new BancoRespuesta();
                        respuesta.setTextorespuesta(partes[i + 2].trim());
                        respuesta.setEscorrecta(letras[i].equals(letraCorrecta));
                        respuesta.setBancoPregunta(pregunta); // vínculo bidireccional
                        respuestas.add(respuesta);
                    }

                    pregunta.setBancoRespuestas(respuestas);
                    bancoPreguntaRepository.save(pregunta); // guarda en cascada con respuestas
                    registrosCorrectos++;
                    detalles.add(pregunta.getEnunciado());

                } catch (Exception e) {
                    errores++;
                }
            }
        }

        return new ResultadoCargaDTO("Carga finalizada", registrosCorrectos, errores, detalles);
    }

}
