package pe.edu.unmsm.sistemas.sistemaparalela.service;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ZipService {
    
    // Implementación temporal que retorna la ruta local
    public String uploadZip(File zipFile, String bucketName) {
        if (!zipFile.exists() || !zipFile.getName().endsWith(".zip")) {
            throw new IllegalArgumentException("Debe proporcionar un archivo .zip válido.");
        }
        
        // Retorna la ruta absoluta del archivo como enlace local
        return "file://" + zipFile.getAbsolutePath();
    }
}
