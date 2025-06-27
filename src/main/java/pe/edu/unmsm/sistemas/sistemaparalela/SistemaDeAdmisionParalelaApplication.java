package pe.edu.unmsm.sistemas.sistemaparalela;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SistemaDeAdmisionParalelaApplication {

    public static void main(String[] args) {
        
        SpringApplication.run(SistemaDeAdmisionParalelaApplication.class, args);
    }
    @Bean
    public CommandLineRunner initDatabase(JdbcTemplate jdbcTemplate) {
        return args -> {
            jdbcTemplate.execute("""
                INSERT INTO BancoPreguntas (enunciado, curso) VALUES
                ('¿Cuál es la capital de España?', 'Geografía'),
                ('¿Qué es SQL?', 'Tecnología'),
                ('¿Quién pintó la Mona Lisa?', 'Arte'),
                ('¿Cuánto es 5 + 7?', 'Matemáticas'),
                ('¿Qué es la fotosíntesis?', 'Biología'),
                ('¿Cuál es el valor de pi?','Matemáticas');
            """);
            jdbcTemplate.execute("""
            
                    INSERT INTO postulantes (postulanteid, nombres, apellidos, dni, carrera, area) VALUES
                                                                   (1, 'Amando', 'Gomila Quiroga', '80132944', 'Psicología', 'D'),
                                                                   (2, 'Albina', 'Folch Arteaga', '64402030', 'Administración', 'E'),
                                                                   (3, 'Elisa', 'Cobos Casals', '22252056', 'Psicología', 'D'),
                                                                   (4, 'Hilario', 'Navarro Valenzuela', '61213558', 'Derecho', 'C'),
                                                                   (5, 'Jordana', 'Bastida Carranza', '99118468', 'Ingeniería', 'A'),
                                                                   (6, 'Florentina', 'Díez Frías', '13785385', 'Derecho', 'C'),
                                                                   (7, 'Claudia', 'Caballero Benito', '77688295', 'Administración', 'E'),
                                                                   (8, 'Leocadio', 'Navarrete Velasco', '13723274', 'Medicina', 'B'),
                                                                   (9, 'Emiliana', 'López Frías', '11014440', 'Derecho', 'C'),
                                                                   (10, 'Remigio', 'Amo Pedraza', '58479761', 'Medicina', 'B'),
                                                                   (11, 'Consuela', 'Aguado Suárez', '61442297', 'Derecho', 'C'),
                                                                   (12, 'Hugo', 'Lozano Lumbreras', '32436043', 'Administración', 'E'),
                                                                   (13, 'Macario', 'Mesa Valdés', '61965666', 'Medicina', 'B'),
                                                                   (14, 'Eulalia', 'Sandoval Torrents', '37451085', 'Ingeniería', 'A'),
                                                                   (15, 'Dafne', 'Tolosa Vicens', '64367012', 'Medicina', 'B'),
                                                                   (16, 'Pastora', 'España Ortega', '76422234', 'Ingeniería', 'A'),
                                                                   (17, 'Benito', 'Soria Lumbreras', '23064070', 'Derecho', 'C'),
                                                                   (18, 'Virgilio', 'Lloret Somoza', '77968554', 'Derecho', 'C'),
                                                                   (19, 'Sofía', 'Rosales Alvarado', '29661468', 'Psicología', 'D'),
                                                                   (20, 'Juanito', 'Gelabert Calderon', '44698863', 'Medicina', 'B'),
                                                                   (21, 'Severino', 'Alcalde Verdugo', '26517804', 'Administración', 'E'),
                                                                   (22, 'Íngrid', 'Castañeda Moya', '21235560', 'Psicología', 'D'),
                                                                   (23, 'Trinidad', 'Lledó Camps', '58038292', 'Administración', 'E'),
                                                                   (24, 'María Fernanda', 'Zabaleta Amor', '46262536', 'Derecho', 'C'),
                                                                   (25, 'Renata', 'Alcántara Jáuregui', '14009471', 'Ingeniería', 'A'),
                                                                   (26, 'Gisela', 'Gallego Calvet', '78534830', 'Derecho', 'C'),
                                                                   (27, 'Lara', 'Olmedo Echevarría', '89696860', 'Ingeniería', 'A'),
                                                                   (28, 'Reyes', 'Bonilla Juárez', '95928560', 'Medicina', 'B'),
                                                                   (29, 'Rosalva', 'Iriarte Clavero', '40129571', 'Administración', 'E'),
                                                                   (30, 'Goyo', 'Cobos Soto', '28380014', 'Administración', 'E'),
                                                                   (31, 'Benigno', 'Badía Tomas', '92507260', 'Medicina', 'B'),
                                                                   (32, 'Armida', 'Rodríguez Romero', '65331070', 'Medicina', 'B'),
                                                                   (33, 'Marisol', 'Riba Ruano', '49785997', 'Medicina', 'B'),
                                                                   (34, 'Baldomero', 'Haro Mancebo', '37096910', 'Psicología', 'D'),
                                                                   (35, 'Viviana', 'Múñiz Alcaraz', '13012406', 'Ingeniería', 'A'),
                                                                   (36, 'Aránzazu', 'Aller Noguera', '45539218', 'Administración', 'E'),
                                                                   (37, 'Narciso', 'Pozuelo Menendez', '73065078', 'Derecho', 'C'),
                                                                   (38, 'Fortunata', 'Quesada Cáceres', '11023964', 'Medicina', 'B'),
                                                                   (39, 'Rufino', 'Valdés Haro', '96951363', 'Medicina', 'B'),
                                                                   (40, 'Noa', 'Pont Torrens', '34904595', 'Ingeniería', 'A'),
                                                                   (41, 'Albert', 'Pinto Mur', '10603348', 'Ingeniería', 'A'),
                                                                   (42, 'Gervasio', 'Planas Arrieta', '73618078', 'Administración', 'E'),
                                                                   (43, 'Clímaco', 'Suárez Parra', '17646125', 'Administración', 'E'),
                                                                   (44, 'Elba', 'Olivares Salgado', '44023713', 'Ingeniería', 'A'),
                                                                   (45, 'Alma', 'Luján Paredes', '23847022', 'Ingeniería', 'A'),
                                                                   (46, 'Chuy', 'Perea Cantero', '28616063', 'Medicina', 'B'),
                                                                   (47, 'Florina', 'Matas Berenguer', '28854375', 'Medicina', 'B'),
                                                                   (48, 'Casemiro', 'Oller Pazos', '47666487', 'Ingeniería', 'A'),
                                                                   (49, 'Che', 'Ariza Calderón', '35701392', 'Administración', 'E'),
                                                                   (50, 'Edu', 'Marcos Torrecilla', '32789268', 'Administración', 'E');
                                                                   
            """);
            jdbcTemplate.execute("""
            
                    INSERT INTO postulantes (postulanteid, nombres, apellidos, dni, carrera, area) VALUES
                                                                                                                                       (51, 'Paloma', 'Jáuregui Rey', '93593035', 'Medicina', 'B'),
                                                                                                                                       (52, 'Isabela', 'Torres Solís', '73521263', 'Ingeniería', 'A'),
                                                                                                                                       (53, 'Sofía', 'Salazar Prats', '13463774', 'Psicología', 'D'),
                                                                                                                                       (54, 'Trinidad', 'Chico Blanes', '62404669', 'Psicología', 'D'),
                                                                                                                                       (55, 'Emma', 'Rocha Blasco', '69577230', 'Ingeniería', 'A'),
                                                                                                                                       (56, 'Fortunata', 'Vidal Guzmán', '42658757', 'Derecho', 'C'),
                                                                                                                                       (57, 'Montserrat', 'Corral Avilés', '82179090', 'Ingeniería', 'A'),
                                                                                                                                       (58, 'Benita', 'Mendoza Rozas', '44267651', 'Medicina', 'B'),
                                                                                                                                       (59, 'Delfina', 'Vázquez Bermúdez', '99218238', 'Medicina', 'B'),
                                                                                                                                       (60, 'Roque', 'Segovia Castilla', '72605299', 'Derecho', 'C'),
                                                                                                                                       (61, 'Esteban', 'Perelló Oliver', '43410408', 'Administración', 'E'),
                                                                                                                                       (62, 'Flavia', 'Caballero Borja', '40727684', 'Psicología', 'D'),
                                                                                                                                       (63, 'Claudio', 'Barranco Barreda', '22895604', 'Psicología', 'D'),
                                                                                                                                       (64, 'Carmina', 'Fuentes Palomar', '58341042', 'Ingeniería', 'A'),
                                                                                                                                       (65, 'Paola', 'Calderon Quevedo', '60380201', 'Psicología', 'D'),
                                                                                                                                       (66, 'Jessica', 'Álvarez Tolosa', '75718997', 'Ingeniería', 'A'),
                                                                                                                                       (67, 'Ruben', 'Cruz Patiño', '95774715', 'Medicina', 'B'),
                                                                                                                                       (68, 'Xiomara', 'Serna Blanca', '54419440', 'Ingeniería', 'A'),
                                                                                                                                       (69, 'Emilia', 'Losa Ripoll', '35345442', 'Derecho', 'C'),
                                                                                                                                       (70, 'Gerónimo', 'Cueto Quirós', '76297276', 'Derecho', 'C'),
                                                                                                                                       (71, 'Joel', 'Ángel Luque', '73734234', 'Ingeniería', 'A'),
                                                                                                                                       (72, 'Carina', 'Franco Sevilla', '41434895', 'Psicología', 'D'),
                                                                                                                                       (73, 'Macarena', 'Alfaro Peralta', '88073893', 'Psicología', 'D'),
                                                                                                                                       (74, 'Andrés Felipe', 'Vidal Castejón', '20954195', 'Administración', 'E'),
                                                                                                                                       (75, 'Baudelio', 'Ferrándiz Morán', '79518352', 'Psicología', 'D'),
                                                                                                                                       (76, 'Mamen', 'Mendoza Criado', '83958531', 'Psicología', 'D'),
                                                                                                                                       (77, 'Juan Carlos', 'Ortega Montenegro', '83881729', 'Ingeniería', 'A'),
                                                                                                                                       (78, 'Valeria', 'Madrigal Soto', '56294811', 'Administración', 'E'),
                                                                                                                                       (79, 'Juan Antonio', 'Atienza Quiroga', '74064512', 'Medicina', 'B'),
                                                                                                                                       (80, 'Rufina', 'Vicens Salamanca', '35935017', 'Administración', 'E'),
                                                                                                                                       (81, 'Domingo', 'Mateu Gisbert', '33569396', 'Administración', 'E'),
                                                                                                                                       (82, 'Ernesto', 'Blanes Burgos', '86764392', 'Administración', 'E'),
                                                                                                                                       (83, 'Juan José', 'Carballo Borrego', '76831684', 'Ingeniería', 'A'),
                                                                                                                                       (84, 'Elvira', 'Palmer Toro', '14865319', 'Ingeniería', 'A'),
                                                                                                                                       (85, 'Eloy', 'Ramírez Abril', '82071235', 'Derecho', 'C'),
                                                                                                                                       (86, 'Modesta', 'Luque Múgica', '35807917', 'Administración', 'E'),
                                                                                                                                       (87, 'Roberta', 'Córdoba Roda', '39142405', 'Derecho', 'C'),
                                                                                                                                       (88, 'Máximo', 'Vazquez Barón', '20066425', 'Administración', 'E'),
                                                                                                                                       (89, 'Trini', 'Romero Godoy', '43678527', 'Administración', 'E'),
                                                                                                                                       (90, 'Reyes', 'Manjón Duque', '15220432', 'Administración', 'E'),
                                                                                                                                       (91, 'Angelina', 'Millán Corbacho', '28292976', 'Psicología', 'D'),
                                                                                                                                       (92, 'Zacarías', 'Reina Olivé', '58588217', 'Medicina', 'B'),
                                                                                                                                       (93, 'Vidal', 'Cobo Pozo', '93742043', 'Medicina', 'B'),
                                                                                                                                       (94, 'Graciana', 'Canals Ramis', '54886169', 'Derecho', 'C'),
                                                                                                                                       (95, 'Domingo', 'Arteaga Beltran', '96958401', 'Derecho', 'C'),
                                                                                                                                       (96, 'Bernabé', 'Urrutia Villaverde', '12014530', 'Ingeniería', 'A'),
                                                                                                                                       (97, 'Elpidio', 'Gordillo Losada', '27119310', 'Administración', 'E'),
                                                                                                                                       (98, 'Pascual', 'Montoya Díaz', '55165548', 'Administración', 'E'),
                                                                                                                                       (99, 'María Teresa', 'Cárdenas Ramirez', '26595119', 'Medicina', 'B'),
                                                                                                                                       (100, 'Jenaro', 'Rojas Sobrino', '92764136', 'Derecho', 'C');
                                                                                                                                       
                                                                   
            """);
            jdbcTemplate.execute("""
                INSERT INTO BancoRespuestas (bancoPreguntasId, textoRespuesta, esCorrecta)
                VALUES
                    (1, 'Madrid', TRUE), (1, 'Barcelona', FALSE), (1, 'Sevilla', FALSE), (1, 'Valencia', FALSE), (1, 'Bilbao', FALSE),
                    (1, 'Granada', FALSE), (1, 'Zaragoza', FALSE), (1, 'Alicante', FALSE), (1, 'Murcia', FALSE), (1, 'Oviedo', FALSE),
                    (2, 'Un lenguaje de consulta estructurada', TRUE), (2, 'Un lenguaje de programación', FALSE),
                    (2, 'Un sistema operativo', FALSE), (2, 'Un programa de diseño gráfico', FALSE),
                    (2, 'Un servidor de bases de datos', FALSE), (2, 'Un lenguaje de marcado', FALSE),
                    (2, 'Un compilador', FALSE), (2, 'Una herramienta de edición de texto', FALSE),
                    (2, 'Un entorno de desarrollo', FALSE), (2, 'Una aplicación web', FALSE),
                    (3, 'Leonardo da Vinci', TRUE), (3, 'Van Gogh', FALSE), (3, 'Pablo Picasso', FALSE),
                    (3, 'Rembrandt', FALSE), (3, 'Salvador Dalí', FALSE), (3, 'Claude Monet', FALSE),
                    (3, 'Edvard Munch', FALSE), (3, 'Michelangelo', FALSE), (3, 'Frida Kahlo', FALSE), (3, 'Andy Warhol', FALSE),
                    (4, '12', TRUE), (4, '14', FALSE), (4, '10', FALSE), (4, '11', FALSE), (4, '13', FALSE),
                    (4, '15', FALSE), (4, '16', FALSE), (4, '9', FALSE), (4, '8', FALSE), (4, '7', FALSE),
                    (5, 'Un proceso donde las plantas convierten la luz en energía', TRUE),
                    (5, 'Un proceso de respiración en animales', FALSE), (5, 'Un proceso de digestión en plantas', FALSE),
                    (5, 'Un proceso de producción de oxígeno en animales', FALSE), (5, 'Un proceso de reproducción en bacterias', FALSE),
                    (5, 'Un proceso de transpiración en plantas', FALSE), (5, 'Un proceso de absorción de agua', FALSE),
                    (5, 'Un proceso de excreción en plantas', FALSE), (5, 'Un proceso de fotosíntesis en hongos', FALSE),
                    (5, 'Un proceso de digestión en animales marinos', FALSE),
                    (6, '3.1416', TRUE), (6, '3.14', TRUE), (6, '2.718', FALSE), (6, '1.618', FALSE), (6, '1.414', FALSE),
                    (6, '3.145', FALSE), (6, '3.13', FALSE), (6, '3.0', FALSE), (6, '3.142', FALSE), (6,'2.71',FALSE);
            """);

            System.out.println("Datos insertados correctamente.");
        };
    }

}
