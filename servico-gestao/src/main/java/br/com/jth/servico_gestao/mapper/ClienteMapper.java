package br.com.jth.servico_gestao.mapper;

import br.com.jth.servico_gestao.dto.request.ClienteRequestDTO;
import br.com.jth.servico_gestao.dto.response.ClienteResponseDTO;
import br.com.jth.servico_gestao.model.ClienteModel;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteModel toModel(ClienteRequestDTO dto) {
        ClienteModel model = new ClienteModel();
        model.setNomeEmpresa(dto.getNomeEmpresa());
        model.setNomeResponsavel(dto.getNomeResponsavel());
        model.setEmail(dto.getEmail());
        model.setCnpj(dto.getCnpj());
        model.setTelefoneResponsavel(dto.getTelefoneResponsavel());
        model.setTelefoneEmpresa(dto.getTelefoneEmpresa());
        return model;
    }

    public ClienteResponseDTO toResponse(ClienteModel model) {
        return new ClienteResponseDTO(
                model.getId(),
                model.getNomeEmpresa(),
                model.getNomeResponsavel(),
                model.getEmail(),
                model.getCnpj(),
                model.getTelefoneResponsavel(),
                model.getTelefoneEmpresa(),
                model.getDataCadastro(),
                model.isAtivo()
        );
    }
}