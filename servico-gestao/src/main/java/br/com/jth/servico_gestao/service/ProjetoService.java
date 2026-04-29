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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.BigInteger;
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

        if (dto.getDataFim().isBefore(dto.getDataInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A data de término não pode ser anterior à data de início.");
        }

        if (dto.getValorOrcamento().compareTo(new BigDecimal("100000")) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Valor de orçamento muito alto.");
        }

        UsuarioModel gestor = usuarioRepository.findById(dto.getGestorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Gestor não encontrado."));

        UsuarioModel profissional = null;
        if (dto.getProfissionalAlocadoId() != null) {
            profissional = usuarioRepository.findById(dto.getProfissionalAlocadoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Profissional não encontrado."));

            if (!profissional.isAtivo()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "O profissional alocado não está ativo.");
            }
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

    @Transactional
    public ProjetoResponseDTO buscarPorId(Long id) {
        ProjetoModel projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Projeto não encontrado: " + id));

        calcularHoras(projeto);
        return projetoMapper.toResponse(projeto);
    }

    @Transactional
    public List<ProjetoResponseDTO> listarTodos() {
        return projetoRepository.findAll()
                .stream()
                .peek(this::calcularHoras)
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

    private void calcularHoras(ProjetoModel projeto) {
        BigInteger totalPrevistas = projeto.getItens().stream()
                .filter(item -> item.getPrevisaoHoras() != null)
                .map(item -> BigInteger.valueOf(item.getPrevisaoHoras()))
                .reduce(BigInteger.ZERO, BigInteger::add);

        projeto.setHorasPrevistasTotal(totalPrevistas);

        // garante que nunca vem null (projetos criados antes dos eventos)
        if (projeto.getHorasRealizadasTotal() == null) projeto.setHorasRealizadasTotal(0.0);
        if (projeto.getHorasPendentesTotal() == null)  projeto.setHorasPendentesTotal(0.0);

        if (totalPrevistas.compareTo(BigInteger.ZERO) > 0) {
            double progresso = projeto.getHorasRealizadasTotal() / totalPrevistas.doubleValue() * 100;
            projeto.setProgressoProjeto(progresso);
        } else {
            projeto.setProgressoProjeto(0.0);
        }
    }
}