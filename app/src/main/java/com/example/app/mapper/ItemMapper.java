package com.example.app.mapper;

import com.example.app.dto.request.ItemRequestdto;
import com.example.app.model.entity.ItemModel;
import org.mapstruct.Mapper;

import javax.swing.text.html.parser.Entity;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemModel toEntity(ItemRequestdto itemRequestdto );
}
