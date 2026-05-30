package br.com.jth.servico_gestao.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.jth.servico_gestao.dto.ProjetoUsuarioDTO;
import br.com.jth.servico_gestao.dto.response.ProjetoUsuarioResponse;
import br.com.jth.servico_gestao.service.ProjetoUsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ProjetoUsuarioController {
    private final ProjetoUsuarioService projetoUsuarioService;

    @PostMapping("/associar-projeto")
    public ResponseEntity<String> associar(@RequestBody @Valid ProjetoUsuarioDTO dto) {
        projetoUsuarioService.associar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Profissionais associados com sucesso ao projeto!");
    }

    @GetMapping("/associacoes")
    public ResponseEntity<List<ProjetoUsuarioResponse>> listarAssociacoes() {
        List<ProjetoUsuarioResponse> associacoes = projetoUsuarioService.listarAssociacoes();
        return ResponseEntity.ok(associacoes);
    }
}
