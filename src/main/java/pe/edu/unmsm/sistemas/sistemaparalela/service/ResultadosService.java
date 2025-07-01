package pe.edu.unmsm.sistemas.sistemaparalela.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.ResultadoTablaDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.TablaResultadosDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.Resultado;
import pe.edu.unmsm.sistemas.sistemaparalela.repository.ResultadoRepository;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResultadosService {
    
    @Autowired
    private ResultadoRepository resultadoRepository;
    
    @Autowired
    private PdfService pdfService;
    
    public TablaResultadosDTO obtenerTodosLosResultados() {
        List<Resultado> resultados = resultadoRepository.findAllWithPostulanteOrderByNotaDesc();
        
        List<ResultadoTablaDTO> resultadosDTO = new ArrayList<>();
        int posicion = 1;
        
        for (Resultado resultado : resultados) {
            ResultadoTablaDTO dto = new ResultadoTablaDTO();
            dto.setPosicion(posicion++);
            dto.setPostulanteId(resultado.getPostulante().getPostulanteid());
            dto.setNombres(resultado.getPostulante().getNombres());
            dto.setApellidos(resultado.getPostulante().getApellidos());
            dto.setDni(resultado.getPostulante().getDni());
            dto.setCarrera(resultado.getPostulante().getCarrera());
            dto.setArea(resultado.getPostulante().getArea());
            dto.setNota(resultado.getNota());
            
            resultadosDTO.add(dto);
        }
        
        return new TablaResultadosDTO(resultadosDTO, resultadosDTO.size());
    }
    
    public TablaResultadosDTO obtenerResultadosPorArea(String area) {
        List<Resultado> resultados = resultadoRepository.findByAreaOrderByNotaDesc(area);
        
        List<ResultadoTablaDTO> resultadosDTO = new ArrayList<>();
        int posicion = 1;
        
        for (Resultado resultado : resultados) {
            ResultadoTablaDTO dto = new ResultadoTablaDTO();
            dto.setPosicion(posicion++);
            dto.setPostulanteId(resultado.getPostulante().getPostulanteid());
            dto.setNombres(resultado.getPostulante().getNombres());
            dto.setApellidos(resultado.getPostulante().getApellidos());
            dto.setDni(resultado.getPostulante().getDni());
            dto.setCarrera(resultado.getPostulante().getCarrera());
            dto.setArea(resultado.getPostulante().getArea());
            dto.setNota(resultado.getNota());
            
            resultadosDTO.add(dto);
        }
        
        return new TablaResultadosDTO(resultadosDTO, resultadosDTO.size());
    }
    
    public String generarPdfResultados(String area) throws Exception {
        // Obtener los datos
        TablaResultadosDTO tablaResultados;
        String titulo;
        
        if (area != null && !area.isEmpty()) {
            tablaResultados = obtenerResultadosPorArea(area);
            titulo = "RESULTADOS DEL EXAMEN DE ADMISIÓN - ÁREA " + area;
        } else {
            tablaResultados = obtenerTodosLosResultados();
            titulo = "RESULTADOS DEL EXAMEN DE ADMISIÓN - TODOS LOS POSTULANTES";
        }
        
        // Preparar datos para la plantilla
        Map<String, Object> datos = new HashMap<>();
        datos.put("resultados", tablaResultados.getResultados());
        datos.put("totalResultados", tablaResultados.getTotalResultados());
        datos.put("fechaGeneracion", LocalDateTime.now());
        datos.put("titulo", titulo);
        
        // Crear el directorio para PDFs si no existe
        String basePath = System.getProperty("user.dir");
        String pdfDir = basePath + "/pdf";
        new File(pdfDir).mkdirs();
        
        // Generar nombre del archivo
        String nombreArchivo = area != null && !area.isEmpty() 
            ? "Resultados_Area_" + area + ".pdf"
            : "Resultados_Todos.pdf";
        String rutaCompleta = pdfDir + "/" + nombreArchivo;
        
        // Generar el PDF
        pdfService.generarPdfResultados(datos, rutaCompleta);
        
        return rutaCompleta;
    }
}
