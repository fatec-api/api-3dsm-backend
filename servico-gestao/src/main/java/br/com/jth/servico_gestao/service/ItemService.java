package br.com.jth.servico_gestao.service;

import br.com.jth.servico_gestao.dto.request.ItemRequestDTO;
import br.com.jth.servico_gestao.dto.response.HorasPorAtividadeDTO;
import br.com.jth.servico_gestao.dto.response.ItemResponseDTO;
import br.com.jth.servico_gestao.enums.item.NivelAtividade;
import br.com.jth.servico_gestao.mapper.ItemMapper;
import br.com.jth.servico_gestao.mensageria.ItemEventProducer;
import br.com.jth.servico_gestao.model.ItemModel;
import br.com.jth.servico_gestao.model.ProjetoModel;
import br.com.jth.servico_gestao.model.UsuarioModel;
import br.com.jth.servico_gestao.repository.ItemRepository;
import br.com.jth.servico_gestao.repository.ProjetoRepository;
import br.com.jth.servico_gestao.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ItemEventProducer itemEventProducer;

    public ItemResponseDTO cadastrarItem(ItemRequestDTO dto) {

        ProjetoModel projeto = projetoRepository.findById(dto.getProjetoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Projeto não encontrado com id: " + dto.getProjetoId()));

        ItemModel item = itemMapper.toEntity(dto);
        item.setCodigo(dto.getTitulo());
        item.setProjetoModel(projeto);

        if (item.getDataAtribuicao() == null) {
            item.setDataAtribuicao(LocalDate.now());
        }

        if (dto.getUsuarioIds() != null && !dto.getUsuarioIds().isEmpty()) {
            List<UsuarioModel> usuarios = usuarioRepository.findAllById(dto.getUsuarioIds());
            item.setUsuarios(new ArrayList<>(usuarios));
        }

        ItemModel salvo = itemRepository.save(item);
        itemEventProducer.publicarItemCriado(salvo);

        return itemMapper.toResponse(salvo);
    }

    public void vincularProfissionais(Long itemId, List<UUID> usuarioIds) {
        ItemModel item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Item não encontrado com id: " + itemId));

        List<UsuarioModel> novosUsuarios = usuarioRepository.findAllById(usuarioIds);

        // sem valores duplicados e sem sobrescrever os existentes
        Set<UsuarioModel> usuariosAtuais = new HashSet<>(item.getUsuarios());
        usuariosAtuais.addAll(novosUsuarios);

        item.setUsuarios(new ArrayList<>(usuariosAtuais));

        itemRepository.save(item);
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
        return itemRepository.findByUsuariosId(usuarioId).stream()
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

    public List<HorasPorAtividadeDTO> buscarHorasPorAtividade(Long projetoId) {
        if (!projetoRepository.existsById(projetoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Projeto não encontrado com id: " + projetoId);
        }

        return itemRepository.somarHorasPorNivelAtividade(projetoId)
                .stream()
                .map(row -> {
                    NivelAtividade nivel = (NivelAtividade) row[0];
                    Integer previstas = ((Number) row[1]).intValue();
                    Integer realizadas = 2; // placeholder até apontamentos
                    Double percentual = previstas > 0
                            ? (realizadas.doubleValue() / previstas.doubleValue()) * 100
                            : 0.0;
                    return new HorasPorAtividadeDTO(nivel, previstas, realizadas, percentual);
                })
                .toList();
    }
}