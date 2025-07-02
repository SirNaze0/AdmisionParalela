package pe.edu.unmsm.sistemas.sistemaparalela.service;

import lombok.Getter;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.BancoPregunta;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.BancoRespuesta;
import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;


public class ExamenParalelo implements Callable<ResultadoExamen> {
    private List<BancoPregunta> bancoPreguntas;
    @Getter
    private BancoPregunta[] bancoPreguntasEscogidas= new BancoPregunta[5];
    private String archivopdf;
    private String _nombre;
    private final long postulanteId;
    private final PersistenciaExamenService persistenciaService;
    private PdfService pdfService;
    public ExamenParalelo(List<BancoPregunta> bancoPreguntas, String _nombre,PdfService pdfService, PersistenciaExamenService persistenciaService,
                          long postulanteId) {
        this.bancoPreguntas = bancoPreguntas;
        this.archivopdf = "pdf/ExamenParalelo";
        this._nombre = _nombre;
        this.pdfService=pdfService;
        this.persistenciaService = persistenciaService;
        this.postulanteId = postulanteId;
    }
    @Override
    public ResultadoExamen call() {
        try{
            //Escogemos las preguntas y aleatorizamos el orden de sus respuestas.
            int cantidadPreguntas =bancoPreguntas.size();
            int random;
            for(int i=0;i<5;i++){
                random=(int)(Math.random()*(cantidadPreguntas));
                sorteador(random);
                bancoPreguntasEscogidas[i]=bancoPreguntas.get(random);
                bancoPreguntas.remove(random);
                cantidadPreguntas--;
            }
            System.out.println("Terminamos de escoger");
            //Crear pdf
            // Generar el mapa de datos del PDF
            Map<String, Object> datos = new HashMap<>();
            for (int i = 0; i < 5; i++) {
                datos.put("pregunta" + (i + 1), bancoPreguntasEscogidas[i].getEnunciado());
                List<BancoRespuesta> respuestas = bancoPreguntasEscogidas[i].getBancoRespuestas();
                for (int j = 0; j < 5; j++) {
                    char letra = (char) ('A' + j);
                    datos.put("respuesta" + (i + 1) + letra, respuestas.get(j).getTextorespuesta());
                }
            }

            // Construir la ruta del archivo
            archivopdf = "pdf/ExamenParalelo" + _nombre + ".pdf";

            // Crear directorio si no existe
            File directorio = new File("pdf");
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            pdfService.generarPdf(datos,archivopdf);
            System.out.println("Finalizo la "+_nombre);

        }catch(Exception e){
            System.out.println("Error en tarea " + _nombre + ": " + e.getMessage());
        }
        return new ResultadoExamen(archivopdf, bancoPreguntasEscogidas, postulanteId);
    }
    public String getArchivoPdf(){
        return archivopdf;
    }
    public void sorteador(int k) {
        List<BancoRespuesta> todas = bancoPreguntas.get(k).getBancoRespuestas();
        // Filtrar respuestas con boolean no null
        List<BancoRespuesta> correctas = todas.stream()
                .filter(r -> Boolean.TRUE.equals(r.getEscorrecta()))
                .collect(Collectors.toList());
        List<BancoRespuesta> incorrectas = todas.stream()
                .filter(r -> Boolean.FALSE.equals(r.getEscorrecta()))
                .collect(Collectors.toList());
        // Validar que hay suficientes respuestas
        if (correctas.size() < 1 || incorrectas.size() < 4) {
            System.out.println("ADVERTENCIA: La pregunta " + k + " (" + bancoPreguntas.get(k).getEnunciado() + ") no tiene suficientes respuestas válidas.");
            System.out.println("  - Respuestas correctas: " + correctas.size());
            System.out.println("  - Respuestas incorrectas: " + incorrectas.size());
            throw new IllegalArgumentException("La pregunta " + k + " no tiene suficientes respuestas válidas (mínimo 1 correcta y 4 incorrectas).");
        }
        // Elegir 1 correcta al azar
        BancoRespuesta correcta = correctas.get((int)(Math.random() * correctas.size()));
        // Mezclar y elegir 4 incorrectas al azar
        Collections.shuffle(incorrectas);
        List<BancoRespuesta> seleccionadas = new ArrayList<>();
        seleccionadas.add(correcta);
        seleccionadas.addAll(incorrectas.subList(0, 4));
        // Mezclar las 5 respuestas finales
        Collections.shuffle(seleccionadas);
        // Reemplazar en la pregunta
        bancoPreguntas.get(k).setBancoRespuestas(seleccionadas);
    }

}
