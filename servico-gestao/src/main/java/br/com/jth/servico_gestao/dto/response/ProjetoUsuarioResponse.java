package br.com.jth.servico_gestao.dto.response;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class ProjetoUsuarioResponse {
    private Long projetoId;
    private String projetoNome;

    private UUID gestorId;
    private String gestorNome;

    private List<UUID> usuarioId;

}

