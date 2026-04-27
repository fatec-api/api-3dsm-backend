package br.com.jth.servico_gestao.mapper;

import br.com.jth.servico_gestao.dto.request.ItemRequestDTO;
import br.com.jth.servico_gestao.dto.response.ItemResponseDTO;
import br.com.jth.servico_gestao.model.ItemModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigo", ignore = true)
    @Mapping(target = "usuarioModel", ignore = true)
    @Mapping(target = "projetoModel", ignore = true)
    ItemModel toEntity(ItemRequestDTO dto);

    @Mapping(source = "projetoModel.id", target = "projetoId")
    @Mapping(source = "projetoModel.nomeProjeto", target = "projetoNome")
    @Mapping(source = "usuarioModel.nomeUsuario", target = "usuarioNome")
    ItemResponseDTO toResponse(ItemModel itemModel);
}
