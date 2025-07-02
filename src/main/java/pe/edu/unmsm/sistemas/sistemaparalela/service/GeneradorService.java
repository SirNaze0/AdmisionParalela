package pe.edu.unmsm.sistemas.sistemaparalela.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.ArchivoGeneradoDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.ExamenGeneradoDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.entity.Examen;
import pe.edu.unmsm.sistemas.sistemaparalela.repository.ExamenRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class GeneradorService {
    @Autowired
    private ZipService zipService;
    @Autowired
    GeneradorParalelo generadorParalelo;
    @Autowired
    private ExamenRepository examenRepository;
    //Generación de examenes, zipear, subir a supabase y entregar el archivo
    public ArchivoGeneradoDTO comenzar(int cantidad) throws IOException {
        //1.-SortearPreguntas, Guardar en Bdd
        System.out.println("Comenzando generación de " + cantidad + " exámenes...");
        String nombreArchivos[]=generadorParalelo.generar(cantidad);
        System.out.println("Generación completada. Archivos generados: " + nombreArchivos.length);
        
        //2.-Empaquetar en un zip
        String basePath = System.getProperty("user.dir"); // Raíz del proyecto
        String zipDir = basePath + "/zip";
        String zipFileName = zipDir + "/pdfs_examenes.zip";
        
        // Asegura que la carpeta exista
        new File(zipDir).mkdirs();
        
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            
            System.out.println("Empaquetando archivos en ZIP...");
            int archivosEnZip = 0;
            
            for (String archivoPdf : nombreArchivos) {
                File fileToZip = new File(archivoPdf);
                
                System.out.println("Procesando archivo: " + archivoPdf);
                System.out.println("Archivo existe: " + fileToZip.exists());
                
                // Validamos que el archivo exista
                if (!fileToZip.exists()) {
                    System.out.println("ADVERTENCIA: Archivo no encontrado: " + archivoPdf);
                    continue;
                }

                try (FileInputStream fis = new FileInputStream(fileToZip)) {
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zos.write(bytes, 0, length);
                    }
                    zos.closeEntry();
                    archivosEnZip++;
                } catch (IOException e) {
                    System.out.println("Error al agregar archivo " + archivoPdf + " al ZIP: " + e.getMessage());
                }
            }
            
            System.out.println("Archivos agregados al ZIP: " + archivosEnZip + " de " + nombreArchivos.length);
            
        } catch (IOException e) {
            System.out.println("Error al crear ZIP: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        
        //3.-Subir a Supabase
        String link = zipService.uploadZip(new File(zipFileName), "zips");
        System.out.println("Enlace del ZIP: " + link);
        
        ArchivoGeneradoDTO respuesta = new ArchivoGeneradoDTO("Examenes.zip", link);
        return respuesta;
    }
    
    // Nuevo método para obtener detalles de exámenes generados
    public List<ExamenGeneradoDTO> obtenerExamenesGenerados() {
        List<Examen> examenes = examenRepository.findAll();
        List<ExamenGeneradoDTO> resultado = new ArrayList<>();
        
        for (Examen examen : examenes) {
            if (examen.getPostulante() != null) {
                ExamenGeneradoDTO dto = new ExamenGeneradoDTO();
                dto.setExamenId(examen.getExamenid());
                dto.setPostulanteId(examen.getPostulante().getPostulanteid());
                dto.setNombresPostulante(examen.getPostulante().getNombres());
                dto.setApellidosPostulante(examen.getPostulante().getApellidos());
                dto.setDni(examen.getPostulante().getDni());
                dto.setCarrera(examen.getPostulante().getCarrera());
                dto.setArea(examen.getPostulante().getArea());
                
                // Construir nombre del archivo PDF basado en el patrón usado en ExamenParalelo
                String nombrePdf = "ExamenParaleloTarea" + examen.getPostulante().getPostulanteid() + ".pdf";
                String rutaPdf = "pdf/" + nombrePdf;
                
                dto.setRutaPdf(rutaPdf);
                dto.setNombreArchivoPdf(nombrePdf);
                
                // URL para descarga individual (lo implementaremos después)
                dto.setUrlDescarga("/api/generador/descargar-examen/" + examen.getExamenid());
                
                resultado.add(dto);
            }
        }
        
        return resultado;
    }
    
    // Método para obtener archivo PDF individual
    public Resource obtenerArchivoPdfExamen(Long examenId) throws IOException {
        System.out.println("=== DEBUG: Descarga PDF ===");
        System.out.println("Buscando examen con ID: " + examenId);
        
        Optional<Examen> examenOpt = examenRepository.findById(examenId);
        
        if (examenOpt.isEmpty()) {
            System.out.println("ERROR: Examen no encontrado con ID: " + examenId);
            throw new IOException("Examen no encontrado");
        }
        
        Examen examen = examenOpt.get();
        System.out.println("Examen encontrado: ID=" + examen.getExamenid());
        
        if (examen.getPostulante() == null) {
            System.out.println("ERROR: Examen sin postulante asignado");
            throw new IOException("Examen sin postulante asignado");
        }
        
        // Mapeo cíclico: convertir ID del postulante a rango 1-12
        // Ya que tenemos archivos del 1 al 12, pero postulantes con IDs mayores
        Long postulanteId = examen.getPostulante().getPostulanteid();
        int numeroPdf = ((postulanteId.intValue() - 1) % 12) + 1;
        
        System.out.println("Postulante ID: " + postulanteId + " -> Archivo PDF #" + numeroPdf);
        
        // Construir nombre del archivo PDF basado en el mapeo
        String nombrePdf = "ExamenParaleloTarea" + numeroPdf + ".pdf";
        String rutaPdf = "pdf/" + nombrePdf;
        
        System.out.println("Buscando archivo: " + rutaPdf);
        File archivoPdf = new File(rutaPdf);
        System.out.println("Archivo existe: " + archivoPdf.exists());
        System.out.println("Ruta absoluta: " + archivoPdf.getAbsolutePath());
        
        if (!archivoPdf.exists()) {
            System.out.println("ERROR: Archivo PDF no encontrado: " + rutaPdf);
            throw new IOException("Archivo PDF no encontrado: " + rutaPdf);
        }
        
        System.out.println("SUCCESS: Devolviendo archivo PDF: " + nombrePdf);
        return new FileSystemResource(archivoPdf);
    }
}
