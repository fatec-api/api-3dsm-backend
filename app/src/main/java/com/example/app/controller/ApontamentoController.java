package com.example.app.controller;

import com.example.app.dto.request.ApontamentoRequestDTO;
import com.example.app.dto.response.ApontamentoResponseDTO;
import com.example.app.service.ApontamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apontamentos")
@RequiredArgsConstructor
public class ApontamentoController {
    private final ApontamentoService service;

    @GetMapping
    public ResponseEntity<List<ApontamentoResponseDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listarApontamentos());
    }

    @GetMapping
    public ResponseEntity<ApontamentoResponseDTO> findById(Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarApontamentoPorId(id));
    }

    @PostMapping
    public ResponseEntity<ApontamentoResponseDTO> salvar(ApontamentoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvarApontamento(request));
    }

    @PatchMapping("{/id}")
    public ResponseEntity<ApontamentoResponseDTO> atualizar(Long id, ApontamentoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(service.atualizar(id, request));
    }

    @DeleteMapping("{/id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
