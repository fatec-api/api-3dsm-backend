package br.com.jth.servico_gestao.controller;

import java.util.List;

import br.com.jth.servico_gestao.dto.request.AllocationRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jth.servico_gestao.dto.response.UsuarioResponseDTO;
import br.com.jth.servico_gestao.service.AlocacaoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/alocacoes")
@CrossOrigin(origins = "http://localhost:5173")
public class AlocacaoController {

    private final AlocacaoService alocacaoService;

    @GetMapping("/profissionais-ativos")
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
                request.getProjetoId(), request.getItemId(), request.getProfissionalIds());

        alocacaoService.vincularProfissionais(request);

        log.info("Alocação processada com sucesso para o item ID: {}", request.getItemId());
        return ResponseEntity.ok("Alocação salva com sucesso!");
    }
}
