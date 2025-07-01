package pe.edu.unmsm.sistemas.sistemaparalela.service;

import pe.edu.unmsm.sistemas.sistemaparalela.dto.FichaOpticaDTO;

import java.util.List;
import java.util.concurrent.Callable;

public class CorreccionParalela implements Callable<ResultadoCorreccionIndividual> {
    private final FichaOpticaDTO fichaOptica;
    private final ExamenCompleto examenCompleto;
    private final String nombreTarea;

    public CorreccionParalela(FichaOpticaDTO fichaOptica, ExamenCompleto examenCompleto, String nombreTarea) {
        this.fichaOptica = fichaOptica;
        this.examenCompleto = examenCompleto;
        this.nombreTarea = nombreTarea;
    }

    @Override
    public ResultadoCorreccionIndividual call() {
        try {
            System.out.println("Iniciando corrección: " + nombreTarea);
            
            // 1. Validar que tenemos el examen
            if (examenCompleto == null) {
                return new ResultadoCorreccionIndividual(
                    fichaOptica.getCodigoPostulante(), 
                    fichaOptica.getIdExamen(), 
                    0, false, 
                    "Examen no encontrado"
                );
            }

            // 2. Parsear respuestas de la ficha óptica
            String[] respuestasMarcadas = parsearRespuestas(fichaOptica.getRespuestas());
            
            // 3. Obtener preguntas del examen
            List<ExamenCompleto.PreguntaCompleta> preguntas = examenCompleto.getPreguntas();
            
            if (preguntas.size() != respuestasMarcadas.length) {
                return new ResultadoCorreccionIndividual(
                    fichaOptica.getCodigoPostulante(), 
                    fichaOptica.getIdExamen(), 
                    0, false, 
                    "Número de respuestas no coincide con preguntas"
                );
            }

            // 4. Calificar
            int nota = calcularNota(preguntas, respuestasMarcadas);
            
            System.out.println("Terminó corrección: " + nombreTarea + " - Nota: " + nota);
            
            return new ResultadoCorreccionIndividual(
                fichaOptica.getCodigoPostulante(), 
                fichaOptica.getIdExamen(), 
                nota, true, null
            );
            
        } catch (Exception e) {
            System.out.println("Error en corrección " + nombreTarea + ": " + e.getMessage());
            return new ResultadoCorreccionIndividual(
                fichaOptica.getCodigoPostulante(), 
                fichaOptica.getIdExamen(), 
                0, false, 
                "Error: " + e.getMessage()
            );
        }
    }

    private String[] parsearRespuestas(String respuestas) {
        // "ABB*DE" -> ["A", "B", "B", "*", "D", "E"]
        String[] resultado = new String[respuestas.length()];
        for (int i = 0; i < respuestas.length(); i++) {
            resultado[i] = String.valueOf(respuestas.charAt(i));
        }
        return resultado;
    }

    private int calcularNota(List<ExamenCompleto.PreguntaCompleta> preguntas, String[] respuestasMarcadas) {
        int correctas = 0;
        
        for (int i = 0; i < preguntas.size(); i++) {
            String respuestaMarcada = respuestasMarcadas[i];
            // Si es nulo (*) o vacío, no suma ni resta
            if ("*".equals(respuestaMarcada) || " ".equals(respuestaMarcada)) {
                continue;
            }
            // Buscar la respuesta correcta
            List<ExamenCompleto.RespuestaSimple> respuestas = preguntas.get(i).getRespuestas();
            String respuestaCorrecta = encontrarRespuestaCorrecta(respuestas);
            
            if (respuestaCorrecta != null && respuestaCorrecta.equals(respuestaMarcada)) {
                correctas=correctas+20;
            }else{
                correctas=correctas-2;
            }
        }
        return correctas;
    }

    private String encontrarRespuestaCorrecta(List<ExamenCompleto.RespuestaSimple> respuestas) {
        // Las respuestas están en orden A, B, C, D, E
        for (int i = 0; i < respuestas.size(); i++) {
            if (respuestas.get(i).isEsCorrecta()) {
                return String.valueOf((char)('A' + i));
            }
        }
        return null;
    }
}
