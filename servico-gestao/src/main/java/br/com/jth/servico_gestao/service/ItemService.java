package br.com.jth.servico_gestao.service;

import br.com.jth.servico_gestao.dto.request.ItemRequestDTO;
import br.com.jth.servico_gestao.dto.response.ItemResponseDTO;
import br.com.jth.servico_gestao.mapper.ItemMapper;
import br.com.jth.servico_gestao.mensageria.ItemEventProducer;
import br.com.jth.servico_gestao.model.ItemModel;
import br.com.jth.servico_gestao.model.ProjetoModel;
import br.com.jth.servico_gestao.model.UsuarioModel;
import br.com.jth.servico_gestao.repository.ItemRepository;
import br.com.jth.servico_gestao.repository.ProjetoRepository;
import br.com.jth.servico_gestao.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ItemEventProducer itemEventProducer;

    public ItemResponseDTO cadastrarItem(ItemRequestDTO dto) {

        ProjetoModel projeto = projetoRepository.findById(dto.getProjetoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Projeto não encontrado com id: " + dto.getProjetoId()));

        String codigo = gerarCodigo(projeto);

        ItemModel item = itemMapper.toEntity(dto);
        item.setCodigo(codigo);
        item.setProjetoModel(projeto);

        if (item.getDataAtribuicao() == null) {
            item.setDataAtribuicao(LocalDate.now());
        }

        if (dto.getUsuarioId() != null) {
            UsuarioModel usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Usuário não encontrado com id: " + dto.getUsuarioId()));
            item.setUsuarioModel(usuario);
        }

        ItemModel salvo = itemRepository.save(item);
        itemEventProducer.publicarItemCriado(salvo);

        return itemMapper.toResponse(salvo);
    }

    public void excluirItem(Long id) {
        ItemModel item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Item não encontrado com id: " + id));

        itemRepository.delete(item);
        itemEventProducer.publicarItemDeletado(id);
    }

    public List<ItemResponseDTO> listarPorProjeto(Long projetoId) {
        return itemRepository.findByProjetoModelId(projetoId).stream()
                .map(itemMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ItemResponseDTO> listarPorProfissional(UUID usuarioId) {
        return itemRepository.findByUsuarioModelId(usuarioId).stream()
                .map(itemMapper::toResponse)
                .collect(Collectors.toList());
    }

    private String gerarCodigo(ProjetoModel projeto) {
        String letras = projeto.getNomeProjeto()
                .replaceAll("[^a-zA-Z]", "")
                .toUpperCase();

        String prefixo = letras.length() >= 3
                ? letras.substring(0, 3)
                : String.format("%-3s", letras).replace(' ', 'X');

        long total = itemRepository.countByProjetoModel(projeto);
        String sufixo = String.format("%04d", total + 1);

        return prefixo + sufixo;
    }
}