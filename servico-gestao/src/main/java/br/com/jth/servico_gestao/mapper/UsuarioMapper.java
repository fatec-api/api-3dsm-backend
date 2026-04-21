package br.com.jth.servico_gestao.mapper;

import br.com.jth.servico_gestao.dto.request.UsuarioRequestDTO;
import br.com.jth.servico_gestao.dto.response.UsuarioResponseDTO;
import br.com.jth.servico_gestao.model.UsuarioModel;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioModel  toEntity(@Valid UsuarioRequestDTO usuaruoRequestDTO);
    UsuarioResponseDTO toResponse(UsuarioModel model);
}
