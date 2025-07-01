package pe.edu.unmsm.sistemas.sistemaparalela.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.util.Map;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.OutputStream;
@Service
public class PdfService {
    private final TemplateEngine templateEngine;

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    public void generarPdf(Map<String, Object> datos, String outputPath) throws Exception {
        // 1. Renderiza HTML con Thymeleaf
        Context context = new Context();
        context.setVariables(datos);
        String html = templateEngine.process("examen", context);

        // 2. Convierte HTML a PDF con Flying Saucer
        try (OutputStream os = new FileOutputStream(outputPath)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(os);
        }
    }
    
    public void generarPdfResultados(Map<String, Object> datos, String outputPath) throws Exception {
        // 1. Renderiza HTML con Thymeleaf usando la plantilla de resultados
        Context context = new Context();
        context.setVariables(datos);
        String html = templateEngine.process("resultados", context);

        // 2. Convierte HTML a PDF con Flying Saucer
        try (OutputStream os = new FileOutputStream(outputPath)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(os);
        }
    }
}
