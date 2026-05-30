package br.com.jth.servico_gestao.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.jth.servico_gestao.dto.ProjetoUsuarioDTO;
import br.com.jth.servico_gestao.dto.response.ProjetoUsuarioResponse;
import br.com.jth.servico_gestao.model.ProjetoModel;
import br.com.jth.servico_gestao.model.ProjetoUsuarioModel;
import br.com.jth.servico_gestao.model.UsuarioModel;
import br.com.jth.servico_gestao.repository.ProjetoRepository;
import br.com.jth.servico_gestao.repository.ProjetoUsuarioRepository;
import br.com.jth.servico_gestao.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProjetoUsuarioService {

    private final ProjetoUsuarioRepository projetoUsuarioRepository;
    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public void associar(ProjetoUsuarioDTO dto) {

        ProjetoModel projeto = projetoRepository.findById(dto.getProjetoId()).orElseThrow(() -> new RuntimeException("Projeto não encontrado."));

        List<UsuarioModel> usuarios = usuarioRepository.findAllById(dto.getUsuarioId());

        if (usuarios.isEmpty()) {
            throw new RuntimeException("Nenhum usuário válido encontrado.");
        }

        for (UsuarioModel usuario : usuarios) {
            if (!usuario.isAtivo()) {
                throw new RuntimeException("Este usuário não está ativo e não pode ser associado ao projeto.");
            }
            if (projetoUsuarioRepository.existsByProjetoAndUsuario(projeto, usuario)) {
                throw new RuntimeException("Usuário já associado a este projeto.");
            }

            ProjetoUsuarioModel associacao = new ProjetoUsuarioModel();
            associacao.setProjeto(projeto);
            associacao.setUsuario(usuario);
            associacao.setDataVinculo(LocalDate.now());

            projetoUsuarioRepository.save(associacao);
        }
    }

    @Transactional(readOnly = true)
    public List<ProjetoUsuarioResponse> listarAssociacoes() {
        return projetoUsuarioRepository.findAll().stream()
                .map(associacao -> {
                    ProjetoUsuarioResponse dto = new ProjetoUsuarioResponse();
                    ProjetoModel projeto = associacao.getProjeto();
                    dto.setProjetoId(projeto.getId());
                    dto.setProjetoNome(projeto.getNomeProjeto());
                    if (projeto.getGestor() != null) {
                        dto.setGestorId(projeto.getGestor().getId());
                        dto.setGestorNome(projeto.getGestor().getNomeUsuario());
                    }
                    dto.setUsuarioId(
                        List.of(associacao.getUsuario().getId())
                    );
                    dto.setUsuarioId(List.of(associacao.getUsuario().getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
