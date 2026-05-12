package br.com.jth.servico_gestao.controller;

import br.com.jth.servico_gestao.dto.request.ClienteRequestDTO;
import br.com.jth.servico_gestao.dto.response.ClienteResponseDTO;
import br.com.jth.servico_gestao.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastrar(
            @RequestBody @Valid ClienteRequestDTO dto) {

        ClienteResponseDTO response = clienteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}