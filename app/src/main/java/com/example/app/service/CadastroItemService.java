package com.example.app.service;

import com.example.app.dto.request.ItemRequestdto;
import com.example.app.dto.response.ItemResponsedto;
import com.example.app.mapper.ItemMapper;
import com.example.app.model.entity.ItemModel;
import com.example.app.model.entity.ProjetoModel;
import com.example.app.model.entity.UsuarioModel;
import com.example.app.repository.ItemRepository;
import com.example.app.repository.ProjetoRepository;
import com.example.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CadastroItemService {

    @Autowired private ItemRepository itemRepository;
    @Autowired private ItemMapper itemMapper;
    @Autowired private ProjetoRepository projetoRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public ItemResponsedto cadastrarItem(ItemRequestdto dto) {
        ItemModel item = itemMapper.toEntity(dto);

        ProjetoModel projeto = projetoRepository.findById(dto.getProjetoId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Projeto não encontrado com id: " + dto.getProjetoId()));
        item.setProjetoModel(projeto);

        if (dto.getUsuarioId() != null) {
            UsuarioModel usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuário não encontrado com id: " + dto.getUsuarioId()));
            item.setUsuarioModel(usuario);
        }

        return itemMapper.toResponse(itemRepository.save(item));
    }
}
