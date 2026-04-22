package com.example.app.mapper;

import com.example.app.dto.request.ApontamentoRequestDTO;
import com.example.app.dto.request.ApontamentoUpdateRequestDTO;
import com.example.app.dto.response.ApontamentoResponseDTO;
import com.example.app.model.entity.ApontamentoModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApontamentoMapper {

    ApontamentoResponseDTO toResponse(ApontamentoModel entity);

    List<ApontamentoResponseDTO> toResponseList(List<ApontamentoModel> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "horasLiquidas", ignore = true)
    ApontamentoModel toEntity(ApontamentoRequestDTO request);
    
//    @Mapping(source = "item.id", target = "itemId")
//    @Mapping(source = "item.descricao", target = "itemDescricao")
//    @Mapping(source = "usuario.id", target = "usuarioId")
//    ApontamentoResponseDTO toResponse(ApontamentoModel entity);
//
//    List<ApontamentoResponseDTO> toResponseList(List<ApontamentoModel> entities);
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(source = "itemId", target = "item.id")
//    @Mapping(source = "usuarioId", target = "usuario.id")
//    @Mapping(target = "horasLiquidas", ignore = true)
//    ApontamentoModel toEntity(ApontamentoRequestDTO request);
}