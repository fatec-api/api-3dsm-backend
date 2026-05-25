package br.com.jth.servico_gestao.service;

import br.com.jth.servico_gestao.controller.ProjetoUsuarioController;
import br.com.jth.servico_gestao.dto.request.ProjetoRequestDTO;
import br.com.jth.servico_gestao.dto.request.ProjetoUpdateRequestDTO;
import br.com.jth.servico_gestao.dto.response.ProjetoResponseDTO;
import br.com.jth.servico_gestao.enums.projeto.StatusOrcamento;
import br.com.jth.servico_gestao.enums.usuario.Cargo;
import br.com.jth.servico_gestao.mapper.ProjetoMapper;
import br.com.jth.servico_gestao.mensageria.ProjetoEventProducer;
import br.com.jth.servico_gestao.model.ClienteModel;
import br.com.jth.servico_gestao.model.ProjetoModel;
import br.com.jth.servico_gestao.model.ProjetoUsuarioModel;
import br.com.jth.servico_gestao.model.UsuarioModel;
import br.com.jth.servico_gestao.repository.ClienteRepository;
import br.com.jth.servico_gestao.repository.ProjetoRepository;
import br.com.jth.servico_gestao.repository.ProjetoUsuarioRepository;
import br.com.jth.servico_gestao.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final ProjetoMapper projetoMapper;
    private final ProjetoEventProducer projetoEventProducer;
    private final ProjetoUsuarioRepository projetoUsuarioRepository;

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

    @Transactional
    public ProjetoResponseDTO editarProjeto(Long id, ProjetoUpdateRequestDTO dto) {

        ProjetoModel projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Projeto não encontrado: " + id));

        if (dto.getDataInicio() != null || dto.getDataFim() != null) {
            LocalDate inicio = dto.getDataInicio() != null ? dto.getDataInicio() : projeto.getDataInicio();
            LocalDate fim    = dto.getDataFim()    != null ? dto.getDataFim()    : projeto.getDataFim();
            if (fim.isBefore(inicio)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "A data de término não pode ser anterior à data de início.");
            }
        }

        if (dto.getValorOrcamento() != null &&
                dto.getValorOrcamento().compareTo(new BigDecimal("100000")) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Valor de orçamento muito alto.");
        }

        if (dto.getNomeProjeto()    != null) projeto.setNomeProjeto(dto.getNomeProjeto());
        if (dto.getTipoProjeto()    != null) projeto.setTipoProjeto(dto.getTipoProjeto());
        if (dto.getValorOrcamento() != null) projeto.setValorOrcamento(dto.getValorOrcamento());
        if (dto.getDataInicio()     != null) projeto.setDataInicio(dto.getDataInicio());
        if (dto.getDataFim()        != null) projeto.setDataFim(dto.getDataFim());
        if (dto.getStatus()         != null) projeto.setStatus(dto.getStatus());

        if (dto.getGestorId() != null) {
            UsuarioModel gestor = usuarioRepository.findById(dto.getGestorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Gestor não encontrado."));

            // Validação de cargo
            if (gestor.getCargo() != Cargo.Gestor) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "O usuário selecionado não é Gestor.");
            }

            projeto.setGestor(gestor);
        }

        if (dto.getClienteId() != null) {
            ClienteModel cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Cliente não encontrado."));
            projeto.setCliente(cliente);
        }
        if (dto.getProfissionalAlocadoIds() != null) {
            // Remove todos os vínculos atuais
            projetoUsuarioRepository.deleteByProjeto(projeto);

            // Insere os novos
            for (UUID uid : dto.getProfissionalAlocadoIds()) {
                UsuarioModel profissional = usuarioRepository.findById(uid)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Usuário não encontrado: " + uid));

                ProjetoUsuarioModel vinculo = new ProjetoUsuarioModel();
                vinculo.setProjeto(projeto);
                vinculo.setUsuario(profissional);
                vinculo.setDataVinculo(LocalDate.now());
                projetoUsuarioRepository.save(vinculo);
            }
        }

        ProjetoModel salvo = projetoRepository.save(projeto);
        calcularHoras(salvo);
        projetoEventProducer.publicarProjetoAtualizado(salvo);

        return projetoMapper.toResponse(salvo);
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

        if (projeto.getHorasRealizadasTotal() == null)
            projeto.setHorasRealizadasTotal(0.0);
        if (projeto.getHorasPendentesTotal() == null)
            projeto.setHorasPendentesTotal(0.0);

        if (totalPrevistas.compareTo(BigInteger.ZERO) > 0) {
            double progresso = projeto.getHorasRealizadasTotal() / totalPrevistas.doubleValue() * 100;
            projeto.setProgressoProjeto(progresso);
        } else {
            projeto.setProgressoProjeto(0.0);
        }
        calcularOrcamento(projeto);
    }

    public List<ProjetoResponseDTO> listarProjetosPorGestor(UUID gestorId) {

        if (!usuarioRepository.existsById(gestorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Gestor não encontrado.");
        }

        List<ProjetoModel> projetos = projetoRepository.findByGestorId(gestorId);

        // retorna lista vazia se não tiver projetos
        return projetos.stream()
                .peek(this::calcularHoras)
                .map(projetoMapper::toResponse)
                .toList();
    }

    public List<ProjetoResponseDTO> listarProjetosPorUsuarioLogado(UUID usuarioId){

        if(!usuarioRepository.existsById(usuarioId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Usuário não encontrado.");
        }

        List<ProjetoUsuarioModel> projetos = projetoUsuarioRepository.findByUsuarioIdAndDataDesvinculoIsNull(usuarioId);

        return projetos.stream()
                .map(vinculo -> vinculo.getProjeto())
                .map(projetoMapper :: toResponse)
                .toList();
    }

    private void calcularOrcamento(ProjetoModel projeto) {
        projeto.setStatusOrcamento(
                calcularStatusOrcamento(projeto.getValorOrcamento(), projeto.getCustoRealTotal())
        );
    }

    private StatusOrcamento calcularStatusOrcamento(BigDecimal valorOrcamento, Double custoRealTotal) {
        if (valorOrcamento == null || custoRealTotal == null || valorOrcamento.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }

        BigDecimal custo = BigDecimal.valueOf(custoRealTotal);
        BigDecimal percentual = custo.divide(valorOrcamento, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        if (percentual.compareTo(BigDecimal.valueOf(75)) < 0) {
            return StatusOrcamento.DENTRO_DO_ORCAMENTO;
        } else if (percentual.compareTo(BigDecimal.valueOf(100)) <= 0) {
            return StatusOrcamento.ATENCAO;
        } else {
            return StatusOrcamento.EXCEDIDO;
        }
    }
}