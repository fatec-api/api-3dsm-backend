package br.com.jth.servico_gestao.controller;

import br.com.jth.servico_gestao.dto.request.ProjetoRequestDTO;
import br.com.jth.servico_gestao.dto.response.ProjetoResponseDTO;
import br.com.jth.servico_gestao.service.ProjetoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projetos")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService projetoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ProjetoResponseDTO> cadastrar(@RequestBody @Valid ProjetoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projetoService.criarProjeto(dto));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ProjetoResponseDTO>> listar() {
        return ResponseEntity.ok(projetoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(projetoService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> excluir(@PathVariable Long id) {
        projetoService.excluirProjeto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/gestor/{gestorId}")
    public ResponseEntity<List<ProjetoResponseDTO>> listarPorGestor(
            @PathVariable Long gestorId) {

        return ResponseEntity.ok(projetoService.listarProjetosPorGestor(gestorId));
    }
}
