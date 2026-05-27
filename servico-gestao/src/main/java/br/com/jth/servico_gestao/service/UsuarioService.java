package br.com.jth.servico_gestao.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.jth.servico_gestao.dto.request.UsuarioRequestDTO;
import br.com.jth.servico_gestao.dto.request.UsuarioUpdateRequestDTO;
import br.com.jth.servico_gestao.dto.response.UsuarioResponseDTO;
import br.com.jth.servico_gestao.enums.usuario.Cargo;
import br.com.jth.servico_gestao.exception.EmailJaCadastradoException;
import br.com.jth.servico_gestao.exception.RecursoNaoEncontradoException;
import br.com.jth.servico_gestao.mapper.UsuarioMapper;
import br.com.jth.servico_gestao.mensageria.UsuarioEventProducer;
import br.com.jth.servico_gestao.model.UsuarioModel;
import br.com.jth.servico_gestao.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioEventProducer usuarioEventProducer;

    public UsuarioResponseDTO cadastrarUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException("E-mail informado já está em uso.");
        }
        UsuarioModel model = usuarioMapper.toEntity(dto);

        // cargos iniciam vazios — preenchidos via Keycloak

        UsuarioModel salvo = usuarioRepository.save(model);
        usuarioEventProducer.publicarUsuarioCriado(salvo);
        return toResponse(salvo);
    }

    public UsuarioResponseDTO alterarUsuario(UUID id, UsuarioUpdateRequestDTO dto) {
        UsuarioModel model = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));

        if (!model.getEmail().equals(dto.getEmail())) {
            if (usuarioRepository.existsByEmail(dto.getEmail())) {
                throw new EmailJaCadastradoException("E-mail informado já está em uso por outro profissional.");
            }
        }

        model.setNomeUsuario(dto.getNomeUsuario());
        model.setEmail(dto.getEmail());
        model.setValorHora(dto.getValorHora());
        model.setNivelExperiencia(dto.getNivelExperiencia());

        UsuarioModel atualizado = usuarioRepository.save(model);
        usuarioEventProducer.publicarUsuarioAtualizado(atualizado);
        return toResponse(atualizado);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO pegarUsuario(UUID id) {
        return usuarioRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarProfissionaisAtivos() {
        return usuarioRepository.findByAtivoTrueAndCargo(Cargo.PROFISSIONAL).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarUsuariosAtivos() {
        return usuarioRepository.findByAtivoTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void excluirUsuario(UUID id) {
        UsuarioModel model = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
        usuarioRepository.delete(model);
        usuarioEventProducer.publicarUsuarioDeletado(id);
    }

    private UsuarioResponseDTO toResponse(UsuarioModel u) {
        return new UsuarioResponseDTO(
                u.getId(),
                u.getNomeUsuario(),
                u.getEmail(),
                u.getValorHora(),
                u.getCargos(),
                u.getNivelExperiencia(),
                u.isAtivo(),
                u.getCriado_em()
        );
    }
}