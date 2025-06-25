package pe.edu.unmsm.sistemas.sistemaparalela.service;

import pe.edu.unmsm.sistemas.sistemaparalela.entity.BancoPregunta;

public class ResultadoExamen {
    private final String archivoPdf;
    private final BancoPregunta[] preguntas;
    private final long postulanteId;

    public ResultadoExamen(String archivoPdf, BancoPregunta[] preguntas, long postulanteId) {
        this.archivoPdf = archivoPdf;
        this.preguntas = preguntas;
        this.postulanteId = postulanteId;
    }

    public String getArchivoPdf() {
        return archivoPdf;
    }

    public BancoPregunta[] getPreguntas() {
        return preguntas;
    }

    public long getPostulanteId() {
        return postulanteId;
    }
}
