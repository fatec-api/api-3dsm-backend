package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.model.entity.ClienteModel;
import com.example.app.model.entity.ProjetoModel;
import com.example.app.model.entity.UsuarioModel;
import com.example.app.repository.ClienteRepository;
import com.example.app.repository.ProjetoRepository;
import com.example.app.repository.UsuarioRepository;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public ProjetoModel criarProjeto(ProjetoModel projeto) {

        if (projeto.getDataFim().isBefore(projeto.getDataInicio())) {
            throw new IllegalArgumentException("Data fim não pode ser anterior à data de início");
        }

        if (projeto.getGestor() == null || projeto.getGestor().getId() == null) {
            throw new IllegalArgumentException("Gestor é obrigatório");
        }

        UsuarioModel gestor = usuarioRepository.findById(projeto.getGestor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Gestor não encontrado"));

        projeto.setGestor(gestor);

        if (projeto.getCliente() != null && projeto.getCliente().getId() != null) {
            ClienteModel cliente = clienteRepository.findById(projeto.getCliente().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

            projeto.setCliente(cliente);
        }

        projeto.setAtivo(true);

        return projetoRepository.save(projeto);
    }
}
