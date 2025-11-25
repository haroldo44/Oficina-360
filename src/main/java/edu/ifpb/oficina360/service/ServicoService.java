package edu.ifpb.oficina360.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.model.Servico;
import edu.ifpb.oficina360.repository.ServicoRepository;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public List<Servico> listarPendentes(Cliente cliente) {
        return servicoRepository.findByClienteAndStatus(cliente, "PENDENTE");
    }

    public List<Servico> listarFinalizados(Cliente cliente) {
        return servicoRepository.findByClienteAndStatus(cliente, "FINALIZADO");
    }

    public Servico salvar(Servico servico) {
        return servicoRepository.save(servico);
    }
}

