package br.com.jth.apontamento.controller;

import br.com.jth.apontamento.dto.request.ApontamentoAprovarRequestDTO;
import br.com.jth.apontamento.dto.request.ApontamentoRequestDTO;
import br.com.jth.apontamento.dto.request.ApontamentoUpdateRequestDTO;
import br.com.jth.apontamento.dto.response.ApontamentoAvaliacaoDTO;
import br.com.jth.apontamento.dto.response.ApontamentoResponseDTO;
import br.com.jth.apontamento.service.ApontamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/apontamentos")
@RequiredArgsConstructor
public class ApontamentoController {
    private final ApontamentoService service;

    @GetMapping
    public ResponseEntity<List<ApontamentoResponseDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listarApontamentos());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ApontamentoResponseDTO>> findPorUsarioId(@PathVariable UUID usuarioId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarApontamentoPorUsuarioId(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApontamentoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarApontamentoPorId(id));
    }

    @GetMapping("/pendente/projeto/{projetoId}")
    public ResponseEntity<List<ApontamentoResponseDTO>> findPendentesPorProjetoId(@PathVariable Long projetoId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarApontamentoPendentePorProjetoId(projetoId));
    }
    
    @PostMapping
    public ResponseEntity<ApontamentoResponseDTO> salvar(@RequestBody @Valid ApontamentoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvarApontamento(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApontamentoResponseDTO> atualizar(@PathVariable Long id,
                                                            @RequestBody @Valid ApontamentoUpdateRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(service.atualizar(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApontamentoResponseDTO> avaliar(@PathVariable Long id,
                                                          @RequestBody @Valid ApontamentoAvaliacaoDTO request) {
        return ResponseEntity.ok(service.avaliar(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}