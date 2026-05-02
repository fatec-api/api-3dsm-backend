package br.com.jth.servico_gestao.mapper;

import br.com.jth.servico_gestao.dto.request.ItemRequestDTO;
import br.com.jth.servico_gestao.dto.response.ItemResponseDTO;
import br.com.jth.servico_gestao.model.ItemModel;
import br.com.jth.servico_gestao.model.UsuarioModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigo", ignore = true)
    @Mapping(target = "usuarios", ignore = true)
    @Mapping(target = "projetoModel", ignore = true)
    ItemModel toEntity(ItemRequestDTO dto);

    @Mapping(source = "projetoModel.id", target = "projetoId")
    @Mapping(source = "projetoModel.nomeProjeto", target = "projetoNome")
    @Mapping(target = "usuarioNomes", expression = "java(mapUsuarioNomes(itemModel))")
    @Mapping(target = "usuarioIds", expression = "java(mapUsuarioIds(itemModel))")
    ItemResponseDTO toResponse(ItemModel itemModel);

    default List<String> mapUsuarioNomes(ItemModel item) {
        if (item.getUsuarios() == null) return Collections.emptyList();

        return item.getUsuarios()
                .stream()
                .map(UsuarioModel::getNomeUsuario)
                .toList();
    }

    default List<UUID> mapUsuarioIds(ItemModel item) {
        if (item.getUsuarios() == null) return Collections.emptyList();

        return item.getUsuarios()
                .stream()
                .map(UsuarioModel::getId)
                .toList();
    }
}