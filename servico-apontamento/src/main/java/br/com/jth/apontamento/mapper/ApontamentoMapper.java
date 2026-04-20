package br.com.jth.apontamento.mapper;

import br.com.jth.apontamento.dto.request.ApontamentoRequestDTO;
import br.com.jth.apontamento.dto.response.ApontamentoResponseDTO;
import br.com.jth.apontamento.model.ApontamentoModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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