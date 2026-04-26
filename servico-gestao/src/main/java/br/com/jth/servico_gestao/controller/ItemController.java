package br.com.jth.servico_gestao.controller;

import br.com.jth.servico_gestao.dto.request.ItemRequestDTO;
import br.com.jth.servico_gestao.dto.response.HorasPorAtividadeDTO;
import br.com.jth.servico_gestao.dto.response.ItemResponseDTO;
import br.com.jth.servico_gestao.dto.response.ProjetoResponseDTO;
import br.com.jth.servico_gestao.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/itens")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PreAuthorize("hasRole('GESTOR')")
    @PostMapping("/cadastrar")
    public ResponseEntity<ItemResponseDTO> cadastrar(@RequestBody @Valid ItemRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.cadastrarItem(dto));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ItemResponseDTO>> listarItensPorProfissional(@PathVariable UUID usuarioId) {
        List<ItemResponseDTO> itens = itemService.listarPorProfissional(usuarioId);

        if (itens.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(itens);
    }

    @PreAuthorize("hasAnyRole('GESTOR', 'DESENVOLVEDOR')")
    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<List<ItemResponseDTO>> listarItensPorProjeto(@PathVariable Long projetoId) {
        List<ItemResponseDTO> itens = itemService.listarPorProjeto(projetoId);

        if (itens.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(itens);
    }

    @GetMapping("/projeto/{projetoId}/horas")
    public ResponseEntity<List<HorasPorAtividadeDTO>> buscarHorasPorAtividade(@PathVariable Long projetoId) {
        return ResponseEntity.ok(itemService.buscarHorasPorAtividade(projetoId));
    }

}
