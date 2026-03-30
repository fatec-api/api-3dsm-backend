package com.example.app.service;

import com.example.app.dto.request.ItemRequestdto;
import com.example.app.dto.response.ItemResponsedto;
import com.example.app.mapper.ItemMapper;
import com.example.app.model.entity.ItemModel;
import com.example.app.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastroItemService {

    @Autowired private ItemRepository itemRepository;
    @Autowired private ItemMapper itemMapper;

    public ItemResponsedto cadastrarItem(ItemRequestdto dto) {
        ItemModel item = itemMapper.toEntity(dto);
        return itemMapper.toResponse(itemRepository.save(item));
    }
}