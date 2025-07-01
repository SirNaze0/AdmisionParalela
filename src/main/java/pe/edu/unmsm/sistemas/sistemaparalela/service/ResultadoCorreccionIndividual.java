package pe.edu.unmsm.sistemas.sistemaparalela.service;

public class ResultadoCorreccionIndividual {
    private final Long postulanteId;
    private final Long examenId;
    private final int nota;
    private final boolean exitoso;
    private final String error;

    public ResultadoCorreccionIndividual(Long postulanteId, Long examenId, int nota, boolean exitoso, String error) {
        this.postulanteId = postulanteId;
        this.examenId = examenId;
        this.nota = nota;
        this.exitoso = exitoso;
        this.error = error;
    }

    // Getters
    public Long getPostulanteId() { return postulanteId; }
    public Long getExamenId() { return examenId; }
    public int getNota() { return nota; }
    public boolean isExitoso() { return exitoso; }
    public String getError() { return error; }
}