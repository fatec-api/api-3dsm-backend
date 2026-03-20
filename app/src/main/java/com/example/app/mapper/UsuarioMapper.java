package com.example.app.mapper;

import com.example.app.dto.request.UsuarioRequestdto;
import com.example.app.model.entity.UsuarioModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioModel toEntity(UsuarioRequestdto usuarioRequestdto);
}
