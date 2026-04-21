package br.com.jth.servico_gestao.service;

import br.com.jth.servico_gestao.dto.request.UsuarioRequestDTO;
import br.com.jth.servico_gestao.dto.request.UsuarioUpdateRequestDTO;
import br.com.jth.servico_gestao.dto.response.UsuarioResponseDTO;
import br.com.jth.servico_gestao.exception.EmailJaCadastradoException;
import br.com.jth.servico_gestao.exception.RecursoNaoEncontradoException;
import br.com.jth.servico_gestao.mapper.UsuarioMapper;
import br.com.jth.servico_gestao.mensageria.UsuarioEventProducer;
import br.com.jth.servico_gestao.model.UsuarioModel;
import br.com.jth.servico_gestao.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    private static final Pattern SENHA_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-={}|:<>?]).{8,}$"
    );

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private UsuarioMapper usuarioMapper;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private UsuarioEventProducer usuarioEventProducer;


    public UsuarioResponseDTO cadastrarUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException("E-mail informado já está em uso.");
        }
        UsuarioModel model = usuarioMapper.toEntity(dto);
        model.setSenha(bCryptPasswordEncoder.encode(dto.getSenha()));
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
            model.setSenha(bCryptPasswordEncoder.encode(dto.getSenha()));
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
    public void excluirUsuario(UUID id) {
        UsuarioModel model = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
        usuarioRepository.delete(model);
        usuarioEventProducer.publicarUsuarioDeletado(id);
    }
}