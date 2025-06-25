package pe.edu.unmsm.sistemas.sistemaparalela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.unmsm.sistemas.sistemaparalela.dto.BancoPreguntaDTO;
import pe.edu.unmsm.sistemas.sistemaparalela.repository.BancoPreguntaRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/banco")
public class BancoPreguntaController {
    @Autowired
    private BancoPreguntaRepository bancoPreguntaRepository;
    @GetMapping
    public List<BancoPreguntaDTO> obtenerBancoPreguntas() {
        return bancoPreguntaRepository.findAll().stream().map( bancopregunta ->{
            BancoPreguntaDTO dto = new BancoPreguntaDTO();
            dto.setEnunciado(bancopregunta.getEnunciado());
            dto.setCurso(bancopregunta.getCurso());
            return dto;
        }).collect(Collectors.toList());
    }
}
