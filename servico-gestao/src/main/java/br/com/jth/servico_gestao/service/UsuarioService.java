package br.com.jth.servico_gestao.service;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsuarioService {

    private static final Pattern SENHA_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-={}|:<>?]).{8,}$"
    );

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioEventProducer usuarioEventProducer;

    public UsuarioResponseDTO cadastrarUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException("E-mail informado já está em uso.");
        }
        UsuarioModel model = usuarioMapper.toEntity(dto);
        model.setSenha(passwordEncoder.encode(dto.getSenha()));
        model.setAtivo(true);

        UsuarioModel salvo = usuarioRepository.save(model);
        usuarioEventProducer.publicarUsuarioCriado(salvo);        
        return usuarioMapper.toResponse(salvo);
    }
    public UsuarioResponseDTO alterarUsuario(UUID id, UsuarioUpdateRequestDTO dto) {
        UsuarioModel model = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
        if (!model.getEmail().equals(dto.getEmail())) {
            if (usuarioRepository.existsByEmail(dto.getEmail())) {
                throw new EmailJaCadastradoException("E-mail informado já está em uso por outro profissional.");
            }
        }
        boolean senhaFoiInformada = dto.getSenha() != null && !dto.getSenha().isBlank();
        boolean confirmacaoFoiInformada = dto.getConfirmaSenha() != null && !dto.getConfirmaSenha().isBlank();

        if (senhaFoiInformada || confirmacaoFoiInformada) {
            if (!dto.getSenha().equals(dto.getConfirmaSenha())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "As senhas estão diferentes.");
            }
            if (!SENHA_PATTERN.matcher(dto.getSenha()).matches()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Senha inválida: ela deve conter ao menos 8 caracteres, incluindo " +
                                "letras maiúsculas, minúsculas, números e caracteres especiais.");
            }
            model.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        model.setNomeUsuario(dto.getNomeUsuario());
        model.setEmail(dto.getEmail());
        model.setCargo(dto.getCargo());
        model.setValorHora(dto.getValorHora());
        model.setNivelExperiencia(dto.getNivelExperiencia());

        UsuarioModel atualizado = usuarioRepository.save(model);
        usuarioEventProducer.publicarUsuarioAtualizado(atualizado);
        return usuarioMapper.toResponse(atualizado);
    }
    public UsuarioResponseDTO pegarUsuario(UUID id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
    }

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
    public List<UsuarioResponseDTO> listarUsuariosAtivos() {
        return usuarioRepository.findByAtivoTrue().stream()
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

    public void excluirUsuario(UUID id) {
        UsuarioModel model = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
        usuarioRepository.delete(model);
        usuarioEventProducer.publicarUsuarioDeletado(id);
    }
}