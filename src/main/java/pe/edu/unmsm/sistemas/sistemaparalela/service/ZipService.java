package pe.edu.unmsm.sistemas.sistemaparalela.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pe.edu.unmsm.sistemas.sistemaparalela.config.SupabaseConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
@Service
public class ZipService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SupabaseConfig supabaseConfig;

    private String generateUniqueFileName(String originalFilename) {
        int lastDotIndex = originalFilename.lastIndexOf('.');
        String extension = "";

        if (lastDotIndex != -1) {
            extension = originalFilename.substring(lastDotIndex);
        }

        return UUID.randomUUID() + extension;
    }

    public String uploadZip(File zipFile, String bucketName) throws IOException {
        if (!zipFile.exists() || !zipFile.getName().endsWith(".zip")) {
            throw new IllegalArgumentException("Debe proporcionar un archivo .zip válido.");
        }

        String zipName = generateUniqueFileName(zipFile.getName());

        String url = supabaseConfig.getSupabaseUrl() + "/storage/v1/object/" + bucketName + "/" + zipName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setBearerAuth(supabaseConfig.getSupabaseKey());
        byte[] fileBytes = Files.readAllBytes(zipFile.toPath());
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(fileBytes, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IOException("Error al subir ZIP a Supabase: " + response.getStatusCode());
        }

        // URL pública (si el bucket es público)
        return supabaseConfig.getSupabaseUrl() + "/storage/v1/object/public/" + bucketName + "/" + zipName;
    }
}
