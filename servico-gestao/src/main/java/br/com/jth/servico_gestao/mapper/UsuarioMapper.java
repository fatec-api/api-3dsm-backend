package br.com.jth.servico_gestao.mapper;

import br.com.jth.servico_gestao.dto.request.UsuarioRequestDTO;
import br.com.jth.servico_gestao.dto.response.UsuarioResponseDTO;
import br.com.jth.servico_gestao.model.UsuarioModel;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mapping(target = "cargos", ignore = true)  // cargo vem do Keycloak
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "criado_em", ignore = true)
    UsuarioModel toEntity(UsuarioRequestDTO dto);

    UsuarioResponseDTO toResponse(UsuarioModel model);
}
