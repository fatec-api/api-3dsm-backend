package br.com.jth.servico_gestao.controller;

import br.com.jth.servico_gestao.dto.ProjetoUsuarioDTO;
import br.com.jth.servico_gestao.service.ProjetoUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjetoUsuarioController {
    @Autowired
    private ProjetoUsuarioService projetoUsuarioService;

    @PostMapping("/associar-projeto")
    public ResponseEntity<String> associar(@RequestBody @Valid ProjetoUsuarioDTO dto) {
        projetoUsuarioService.associar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Profissionais associados com sucesso ao projeto!");
    }
}
