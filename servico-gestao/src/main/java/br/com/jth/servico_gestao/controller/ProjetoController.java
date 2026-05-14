package br.com.jth.servico_gestao.controller;

import br.com.jth.servico_gestao.dto.request.ProjetoRequestDTO;
import br.com.jth.servico_gestao.dto.request.ProjetoUpdateRequestDTO;
import br.com.jth.servico_gestao.dto.response.ProjetoResponseDTO;
import br.com.jth.servico_gestao.repository.ProjetoUsuarioRepository;
import br.com.jth.servico_gestao.service.ProjetoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projetos")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService projetoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ProjetoResponseDTO> cadastrar(@RequestBody @Valid ProjetoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projetoService.criarProjeto(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> editar(@PathVariable Long id,
                                                     @RequestBody @Valid ProjetoUpdateRequestDTO dto) {
        return ResponseEntity.ok(projetoService.editarProjeto(id, dto));
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
            @PathVariable UUID gestorId) {

        return ResponseEntity.ok(projetoService.listarProjetosPorGestor(gestorId));
    }

    @GetMapping("/projeto/usuario/{usuarioId}")
    public ResponseEntity<List<ProjetoResponseDTO>> listarPorUsuarioLogado(@PathVariable UUID usuarioId){
        return ResponseEntity.ok(projetoService.listarProjetosPorUsuarioLogado(usuarioId));
    }

}
