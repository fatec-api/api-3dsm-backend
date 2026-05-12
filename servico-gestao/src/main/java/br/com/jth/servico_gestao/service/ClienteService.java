package br.com.jth.servico_gestao.service;

import br.com.jth.servico_gestao.dto.request.ClienteRequestDTO;
import br.com.jth.servico_gestao.dto.response.ClienteResponseDTO;
import br.com.jth.servico_gestao.mapper.ClienteMapper;
import br.com.jth.servico_gestao.model.ClienteModel;
import br.com.jth.servico_gestao.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteResponseDTO cadastrar(ClienteRequestDTO dto) {

        String nomeEmpresa = dto.getNomeEmpresa().trim();
        String email = normalizarEmail(dto.getEmail());
        String cnpj = normalizarCnpj(dto.getCnpj());

        validarCnpjMatematico(cnpj);

        ClienteModel model = clienteMapper.toModel(dto);

        model.setNomeEmpresa(nomeEmpresa);
        model.setEmail(email);
        model.setCnpj(cnpj);

        return clienteMapper.toResponse(clienteRepository.save(model));
    }

    String normalizarCnpj(String cnpj) {
        if (cnpj == null)
            return "";
        return cnpj.replaceAll("[.\\-/\\s]", "").trim();
    }

    String normalizarEmail(String email) {
        if (email == null)
            return "";
        return email.trim().toLowerCase();
    }

    void validarCnpjMatematico(String cnpj) {

        if (cnpj == null || cnpj.length() != 14 || !cnpj.matches("\\d{14}")) {
            throw new IllegalArgumentException(
                    "CNPJ deve conter exatamente 14 dígitos numéricos");
        }

        if (cnpj.chars().distinct().count() == 1) {
            throw new IllegalArgumentException("CNPJ inválido");
        }

        int[] pesos1 = { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
        int[] pesos2 = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

        int digito1 = calcularDigitoVerificador(cnpj, pesos1);
        int digito2 = calcularDigitoVerificador(cnpj, pesos2);

        boolean valido = digito1 == Character.getNumericValue(cnpj.charAt(12)) &&
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
