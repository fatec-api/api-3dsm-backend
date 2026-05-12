package br.com.jth.servico_gestao.service;


import br.com.jth.servico_gestao.dto.request.ClienteRequestDTO;
import br.com.jth.servico_gestao.dto.response.ClienteResponseDTO;
import br.com.jth.servico_gestao.exception.RecursoNaoEncontradoException;
import br.com.jth.servico_gestao.mapper.ClienteMapper;
import br.com.jth.servico_gestao.model.ClienteModel;
import br.com.jth.servico_gestao.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ClienteService {


    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;


    public ClienteResponseDTO cadastrar(ClienteRequestDTO dto) {
        String nomeEmpresa = dto.getNomeEmpresa().trim();
        String email       = normalizarEmail(dto.getEmail());
        String cnpj        = normalizarCnpj(dto.getCnpj());


        validarCnpjMatematico(cnpj);
        validarEmailUnico(email, null);
        validarCnpjUnico(cnpj, null);


        ClienteModel model = clienteMapper.toModel(dto);
        model.setNomeEmpresa(nomeEmpresa);
        model.setEmail(email);
        model.setCnpj(cnpj);


        return clienteMapper.toResponse(clienteRepository.save(model));
    }


    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toResponse)
                .collect(Collectors.toList());
    }


    public List<ClienteResponseDTO> listarAtivos() {
        return clienteRepository.findByAtivoTrue()
                .stream()
                .map(clienteMapper::toResponse)
                .collect(Collectors.toList());
    }


    public List<ClienteResponseDTO> buscar(String termo) {
        String t = termo == null ? "" : termo.trim();
        return clienteRepository
                .findByAtivoTrueAndNomeEmpresaContainingIgnoreCaseOrAtivoTrueAndCnpjContaining(t, t)
                .stream()
                .map(clienteMapper::toResponse)
                .collect(Collectors.toList());
    }


    public ClienteResponseDTO buscarPorId(Long id) {
        return clienteMapper.toResponse(buscarModelPorId(id));
    }


    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        ClienteModel model = buscarModelPorId(id);


        String nomeEmpresa = dto.getNomeEmpresa().trim();
        String email       = normalizarEmail(dto.getEmail());
        String cnpj        = normalizarCnpj(dto.getCnpj());


        validarCnpjMatematico(cnpj);
        validarEmailUnico(email, id);
        validarCnpjUnico(cnpj, id);


        model.setNomeEmpresa(nomeEmpresa);
        model.setEmail(email);
        model.setCnpj(cnpj);


        return clienteMapper.toResponse(clienteRepository.save(model));
    }


    public void inativar(Long id) {
        ClienteModel model = buscarModelPorId(id);
        model.setAtivo(false);
        clienteRepository.save(model);
    }


    public void deletar(Long id) {
        buscarModelPorId(id);
        clienteRepository.deleteById(id);
    }


    private ClienteModel buscarModelPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado com id: " + id));
    }


    private void validarEmailUnico(String email, Long idIgnorar) {
        clienteRepository.findByEmail(email).ifPresent(existente -> {
            if (!existente.getId().equals(idIgnorar)) {
                throw new IllegalArgumentException("E-mail já cadastrado: " + email);
            }
        });
    }


    private void validarCnpjUnico(String cnpj, Long idIgnorar) {
        clienteRepository.findByCnpj(cnpj).ifPresent(existente -> {
            if (!existente.getId().equals(idIgnorar)) {
                throw new IllegalArgumentException("CNPJ já cadastrado: " + cnpj);
            }
        });
    }


    // formatação do cnpj para evitar erros de cadastro
    String normalizarCnpj(String cnpj) {
        if (cnpj == null) return "";
        return cnpj.replaceAll("[.\\-/\\s]", "").trim();
    }


    // formatação do e-mail para evitar erros de cadastro
    String normalizarEmail(String email) {
        if (email == null) return "";
        return email.trim().toLowerCase();
    }


    void validarCnpjMatematico(String cnpj) {
        if (cnpj == null || cnpj.length() != 14 || !cnpj.matches("\\d{14}")) {
            throw new IllegalArgumentException(
                    "CNPJ deve conter exatamente 14 dígitos numéricos");
        }


        // aceita apenas CNPJ mascarado
        if (cnpj.chars().distinct().count() == 1) {
            throw new IllegalArgumentException("CNPJ inválido");
        }


        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};


        int digito1 = calcularDigitoVerificador(cnpj, pesos1);
        int digito2 = calcularDigitoVerificador(cnpj, pesos2);


        boolean valido =
                digito1 == Character.getNumericValue(cnpj.charAt(12)) &&
                digito2 == Character.getNumericValue(cnpj.charAt(13));


        if (!valido) {
            throw new IllegalArgumentException("CNPJ inválido");
        }
    }


    private int calcularDigitoVerificador(String cnpj, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}

