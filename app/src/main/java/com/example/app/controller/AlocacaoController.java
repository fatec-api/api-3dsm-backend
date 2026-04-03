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


    @GetMapping("/projeto/{projectId}/profissionais")
    public ResponseEntity<List<UsuarioResponseDTO>> getProfissionaisParaAlocacao(@PathVariable Long projectId) {
        List<UsuarioResponseDTO> profissionais = alocacaoService.listarProfissionaisDisponiveis(projectId);
        return ResponseEntity.ok(profissionais);
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