package pe.edu.unmsm.sistemas.sistemaparalela.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.FichaOpticaDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.ResultadoCorreccionDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CorreccionService {
    
    @Autowired
    private CorreccionGenerador correccionGenerador;

    public ResultadoCorreccionDTO procesar(MultipartFile archivo) throws IOException {
        long inicio = System.currentTimeMillis();
        
        // 1. Parsear CSV
        List<FichaOpticaDTO> fichasOpticas = parsearCsv(archivo);
        
        if (fichasOpticas.isEmpty()) {
            return new ResultadoCorreccionDTO("No se encontraron fichas válidas", 0, 0);
        }
        
        System.out.println("Fichas parseadas: " + fichasOpticas.size());
        
        // 2. Procesar en paralelo
        int[] resultados = correccionGenerador.corregir(fichasOpticas);
        int exitosos = resultados[0];
        int errores = resultados[1];
        
        long fin = System.currentTimeMillis();
        long duracion = fin - inicio;
        
        System.out.println("Corrección completada en: " + duracion + " ms");
        
        String mensaje = String.format("Corrección completada en %d ms", duracion);
        return new ResultadoCorreccionDTO(mensaje, exitosos, errores);
    }

    private List<FichaOpticaDTO> parsearCsv(MultipartFile archivo) throws IOException {
        List<FichaOpticaDTO> fichas = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(archivo.getInputStream()))) {
            String linea;
            int numeroLinea = 0;
            
            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                
                // Saltar header si existe
                if (numeroLinea == 1 && linea.toLowerCase().contains("postulante")) {
                    continue;
                }
                
                try {
                    FichaOpticaDTO ficha = parsearLinea(linea);
                    if (ficha != null) {
                        fichas.add(ficha);
                    }
                } catch (Exception e) {
                    System.out.println("Error en línea " + numeroLinea + ": " + e.getMessage());
                }
            }
        }
        
        return fichas;
    }

    private FichaOpticaDTO parsearLinea(String linea) {
        // Formato esperado: codigoPostulante,idExamen,respuestas
        // Ejemplo: 123,456,ABB*DE
        
        String[] partes = linea.split(",");
        if (partes.length != 3) {
            throw new IllegalArgumentException("Formato inválido, se esperan 3 columnas");
        }
        
        try {
            Long codigoPostulante = Long.parseLong(partes[0].trim());
            Long idExamen = Long.parseLong(partes[1].trim());
            String respuestas = partes[2].trim();
            
            // Validar respuestas básicamente
            if (respuestas.length() != 5) {
                throw new IllegalArgumentException("Las respuestas deben tener exactamente 5 caracteres");
            }
            
            return new FichaOpticaDTO(codigoPostulante, idExamen, respuestas);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Códigos deben ser numéricos");
        }
    }
}