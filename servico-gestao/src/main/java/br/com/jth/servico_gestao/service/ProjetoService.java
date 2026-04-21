package br.com.jth.servico_gestao.service;

import br.com.jth.servico_gestao.dto.request.ProjetoRequestDTO;
import br.com.jth.servico_gestao.dto.response.ProjetoResponseDTO;
import br.com.jth.servico_gestao.mapper.ProjetoMapper;
import br.com.jth.servico_gestao.mensageria.ProjetoEventProducer;
import br.com.jth.servico_gestao.model.ClienteModel;
import br.com.jth.servico_gestao.model.ProjetoModel;
import br.com.jth.servico_gestao.model.UsuarioModel;
import br.com.jth.servico_gestao.repository.ClienteRepository;
import br.com.jth.servico_gestao.repository.ProjetoRepository;
import br.com.jth.servico_gestao.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final ProjetoMapper projetoMapper;
    private final ProjetoEventProducer projetoEventProducer;

    public ProjetoResponseDTO criarProjeto(ProjetoRequestDTO dto) {
        if (dto.getDataFim().isBefore(dto.getDataInicio()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A data de término não pode ser anterior à data de início.");

        if (dto.getValorOrcamento().compareTo(new BigDecimal("100000")) > 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Valor de orçamento muito alto.");

        UsuarioModel gestor = usuarioRepository.findById(dto.getGestorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Gestor não encontrado."));

        UsuarioModel profissional = null;
        if (dto.getProfissionalAlocadoId() != null) {
            profissional = usuarioRepository.findById(dto.getProfissionalAlocadoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Profissional não encontrado."));
            if (!profissional.isAtivo())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "O profissional alocado não está ativo.");
        }

        ClienteModel cliente = null;
        if (dto.getClienteId() != null) {
            cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Cliente não encontrado."));
        }

        ProjetoModel model = projetoMapper.toEntity(dto);
        model.setGestor(gestor);
        model.setProfissionalAlocado(profissional);
        model.setCliente(cliente);

        ProjetoModel salvo = projetoRepository.save(model);

        projetoEventProducer.publicarProjetoCriado(salvo);

        return projetoMapper.toResponse(salvo);
    }

    public ProjetoResponseDTO buscarPorId(Long id) {
        return projetoMapper.toResponse(
                projetoRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Projeto não encontrado: " + id))
        );
    }

    public List<ProjetoResponseDTO> listarTodos() {
        return projetoRepository.findAll()
                .stream()
                .map(projetoMapper::toResponse)
                .toList();
    }

    public void excluirProjeto(Long id) {
        ProjetoModel model = projetoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Projeto não encontrado: " + id));
        projetoRepository.delete(model);
        projetoEventProducer.publicarProjetoDeletado(id);
    }
}