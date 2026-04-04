package com.example.app.controller;

import com.example.app.dto.request.AllocationRequestDTO;
import com.example.app.dto.response.UsuarioResponseDTO;
import com.example.app.service.AlocacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/alocacoes")
@CrossOrigin(origins = "http://localhost:5173")
public class AlocacaoController {

    @Autowired
    private AlocacaoService alocacaoService;



    @GetMapping("/profissionais/ativos")
    public ResponseEntity<List<UsuarioResponseDTO>> getTodosProfissionaisAtivos() {
        List<UsuarioResponseDTO> profissionais = alocacaoService.listarProfissionaisAtivos();
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/projeto/{projectId}")
    public ResponseEntity<List<UsuarioResponseDTO>> getProfissionaisDoProjeto(@PathVariable Long projectId) {
        List<UsuarioResponseDTO> equipe = alocacaoService.listarProfissionaisDoProjeto(projectId);
        return ResponseEntity.ok(equipe);
    }

    @PostMapping("/vincular")
    public ResponseEntity<String> vincular(@RequestBody AllocationRequestDTO request) {
        log.info("Recebida requisição de alocação: Projeto {}, Item {}, Profissionais: {}",
                request.getProjectId(), request.getItemId(), request.getProfessionalIds());

        alocacaoService.vincularProfissionais(request);

        log.info("Alocação processada com sucesso para o item ID: {}", request.getItemId());
        return ResponseEntity.ok("Alocação salva com sucesso!");
    }
}