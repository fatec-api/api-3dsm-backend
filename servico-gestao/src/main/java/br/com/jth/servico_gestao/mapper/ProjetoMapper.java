package br.com.jth.servico_gestao.mapper;

import br.com.jth.servico_gestao.dto.request.ProjetoRequestDTO;
import br.com.jth.servico_gestao.dto.response.ProjetoResponseDTO;
import br.com.jth.servico_gestao.model.ProjetoModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjetoMapper {

    @Mapping(target = "gestor", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "profissionalAlocado", ignore = true)
    ProjetoModel toEntity(ProjetoRequestDTO dto);

    @Mapping(source = "gestor.nomeUsuario", target = "nomeGestor")
    @Mapping(source = "cliente.nomeEmpresa", target = "nomeCliente")
    ProjetoResponseDTO toResponse(ProjetoModel model);
}