package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.model.entity.ProjetoModel;
import com.example.app.model.entity.UsuarioModel;
import com.example.app.repository.ProjetoRepository;
import com.example.app.repository.UsuarioRepository;

@Service
public class ProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ProjetoModel criarProjeto(ProjetoModel projeto){

        if (projeto.getDataFim().isBefore(projeto.getDataInicio())){
            throw new  IllegalArgumentException("Data fim não pode ser anterior à data de início");
        }

        if(projeto.getGestor() == null || projeto.getGestor().getId() == null){
            throw new  IllegalArgumentException("Gestor não encontrado");
        }

        // verifica se já existe o gestor
        UsuarioModel gestor = usuarioRepository.findById(projeto.getGestor().getId())
            .orElseThrow(() -> new IllegalArgumentException("Gestor não encontrado"));

    
        projeto.setGestor(gestor);

        //profissional opcional

        
    
    }

}
