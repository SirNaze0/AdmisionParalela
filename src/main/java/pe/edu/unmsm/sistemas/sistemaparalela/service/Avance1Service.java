package pe.edu.unmsm.sistemas.sistemaparalela.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.ArchivoGeneradoDTO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class Avance1Service {
    @Autowired
    private ZipService zipService;
    @Autowired
    GeneradorParalelo generadorParalelo;
    //Generación de examenes, zipear, subir a supabase y entregar el archivo
    public ArchivoGeneradoDTO comenzar(int cantidad) throws IOException {
        //1.-SortearPreguntas, Guardar en Bdd
        System.out.println("Comenzando...");
        String nombreArchivos[]=generadorParalelo.generar(cantidad);
        System.out.println("Terminando...");
        //2.-Empaquetar en un zip
        String basePath = System.getProperty("user.dir"); // Raíz del proyecto
        String zipDir = basePath + "/zip";
        String zipFileName = zipDir + "/pdfs_examenes.zip";
        // Asegura que la carpeta exista
        new File(zipDir).mkdirs();
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (String archivoPdf : nombreArchivos) {
                File fileToZip = new File(archivoPdf);

                // Validamos que el archivo exista
                if (!fileToZip.exists()) continue;

                try (FileInputStream fis = new FileInputStream(fileToZip)) {
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zos.write(bytes, 0, length);
                    }
                    zos.closeEntry();
                }
            }
            zos.finish(); // opcional, algunos lo omiten
        } catch (IOException e) {
            e.printStackTrace(); // Manejo básico de error
        }
        //3.-Subir a Supabase
        String link = zipService.uploadZip(new File(zipFileName), "zips");
        System.out.println(" Enlace del ZIP: " + link);
        ArchivoGeneradoDTO respuesta= new ArchivoGeneradoDTO("Examenes.zip", link);
        return respuesta;
    }
}
