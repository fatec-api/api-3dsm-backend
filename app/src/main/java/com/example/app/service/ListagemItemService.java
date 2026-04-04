package com.example.app.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.dto.response.ItemResponsedto;
import com.example.app.mapper.ItemMapper;
import com.example.app.repository.ItemRepository;

@Service
public class ListagemItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemMapper itemMapper;

    public List<ItemResponsedto> listarPorProjeto(Long projetoId) {
        return itemRepository.findByProjetoModelId(projetoId).stream()
                .map(itemMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ItemResponsedto> listarPorProfissional(UUID usuarioId) {
        return itemRepository.findByUsuarioModelId(usuarioId).stream()
                .map(itemMapper::toResponse)
                .collect(Collectors.toList());
    }
}