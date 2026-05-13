package br.com.jth.servico_gestao.dto.response;

public record ClienteResponseDTO(
    String id,
    String nomeEmpresa,
    String cnpj,
    String email,
    boolean ativo
) {}