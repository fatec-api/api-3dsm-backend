package br.com.jth.servico_gestao.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.jth.servico_gestao.dto.request.AllocationRequestDTO;
import br.com.jth.servico_gestao.dto.response.UsuarioResponseDTO;
import br.com.jth.servico_gestao.enums.usuario.Cargo;
import br.com.jth.servico_gestao.model.ItemModel;
import br.com.jth.servico_gestao.model.ProjetoModel;
import br.com.jth.servico_gestao.model.ProjetoUsuarioModel;
import br.com.jth.servico_gestao.model.UsuarioModel;
import br.com.jth.servico_gestao.repository.ItemRepository;
import br.com.jth.servico_gestao.repository.ProjetoRepository;
import br.com.jth.servico_gestao.repository.ProjetoUsuarioRepository;
import br.com.jth.servico_gestao.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AlocacaoService {

    private final ItemRepository itemRepository;

    private final UsuarioRepository usuarioRepository;

    private final ProjetoRepository projetoRepository;

    private final ProjetoUsuarioRepository projetoUsuarioRepository;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarProfissionaisAtivos() {
        return usuarioRepository.findByAtivoTrueAndCargo(Cargo.Profissional).stream()
                .map(user -> new UsuarioResponseDTO(
                        user.getId(),
                        user.getNomeUsuario(),
                        user.getEmail(),
                        user.getValorHora(),
                        user.getCargo(),
                        user.getNivelExperiencia(),
                        user.isAtivo(),
                        user.getCriado_em()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarProfissionaisDoProjeto(Long projectId) {
        log.info("Buscando profissionais vinculados ao projeto ID: {}", projectId);

        List<ProjetoUsuarioModel> vinculos = projetoUsuarioRepository
                .findByProjetoIdAndDataDesvinculoIsNull(projectId);

        return vinculos.stream()
                .map(vinculo -> {
                    UsuarioModel user = vinculo.getUsuario();
                    return new UsuarioResponseDTO(
                            user.getId(),
                            user.getNomeUsuario(),
                            user.getEmail(),
                            user.getValorHora(),
                            user.getCargo(),
                            user.getNivelExperiencia(),
                            user.isAtivo(),
                            user.getCriado_em());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void vincularProfissionais(AllocationRequestDTO request) {
        log.info("Iniciando alocação para o Item ID: {} no Projeto ID: {}",
                request.getItemId(), request.getProjetoId());

        ItemModel item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("Erro: Item não encontrado."));

        ProjetoModel projeto = projetoRepository.findById(request.getProjetoId())
                .orElseThrow(() -> new RuntimeException("Erro: Projeto não encontrado."));

        UUID profissionalId = request.getProfissionalIds().get(0);

        UsuarioModel profissional = usuarioRepository.findById(profissionalId)
                .orElseThrow(() -> new RuntimeException("Erro: Profissional não encontrado."));

        item.setUsuarioModel(profissional);
        itemRepository.save(item);

        if (!projetoUsuarioRepository.existsByProjetoAndUsuario(projeto, profissional)) {
            ProjetoUsuarioModel novoVinculo = new ProjetoUsuarioModel();
            novoVinculo.setProjeto(projeto);
            novoVinculo.setUsuario(profissional);
            novoVinculo.setDataVinculo(LocalDate.now());
            projetoUsuarioRepository.save(novoVinculo);

            log.info("Profissional {} vinculado ao item {} com sucesso.",
                    profissional.getNomeUsuario(), item.getDescricao());
        }
    }
}