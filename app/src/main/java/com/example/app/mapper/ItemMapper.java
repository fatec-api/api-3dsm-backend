package com.example.app.mapper;

import com.example.app.dto.request.ItemRequestdto;
import com.example.app.dto.response.ItemResponsedto;
import com.example.app.model.entity.ItemModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioModel", ignore = true)
    @Mapping(target = "projetoModel", ignore = true)
    ItemModel toEntity(ItemRequestdto dto);

    @Mapping(source = "projetoModel.id", target = "projetoId")
    @Mapping(source = "projetoModel.nome", target = "projetoNome")
    @Mapping(source = "usuarioModel.nome", target = "usuarioNome")
    ItemResponsedto toResponse(ItemModel itemModel);
}