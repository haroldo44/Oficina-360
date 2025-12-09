package edu.ifpb.oficina360.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.model.Mecanico;
import edu.ifpb.oficina360.model.Servico;
import edu.ifpb.oficina360.repository.ServicoRepository;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;
    
    public List<Servico> listarCancelados(Cliente cliente) {
        return servicoRepository.findByClienteAndStatus(cliente, "CANCELADO");
    }
    
    public List<Servico> listarPendentes(Cliente cliente) {
        return servicoRepository.findByClienteAndStatus(cliente, "PENDENTE");
    }

    public List<Servico> listarFinalizados(Cliente cliente) {
        return servicoRepository.findByClienteAndStatus(cliente, "FINALIZADO");
    }

    public Servico salvar(Servico servico) {
        return servicoRepository.save(servico);
    }

    public List<Servico> buscarPorOficinaEStatus(Long oficinaId, String status) {
        return servicoRepository.findByOficinaIdAndStatus(oficinaId, status);
    }

    public List<Servico> buscarPorClienteEStatus(Cliente cliente, String status) {
        return servicoRepository.findByClienteAndStatus(cliente, status);
    }

    public List<Servico> buscarPorMecanicoEStatus(Mecanico mecanico, String status) {
        return servicoRepository.findByMecanicoAndStatus(mecanico, status);
    }

    // --- NOVO MÉTODO PARA O HISTÓRICO COMPLETO DO MECÂNICO ---
    // Este método junta os FINALIZADOS e CANCELADOS numa única lista
    public List<Servico> listarHistoricoMecanico(Mecanico mecanico) {
        List<Servico> historico = new ArrayList<>();
        
        // Busca finalizados
        List<Servico> finalizados = servicoRepository.findByMecanicoAndStatus(mecanico, "FINALIZADO");
        if (finalizados != null) {
            historico.addAll(finalizados);
        }

        // Busca cancelados
        List<Servico> cancelados = servicoRepository.findByMecanicoAndStatus(mecanico, "CANCELADO");
        if (cancelados != null) {
            historico.addAll(cancelados);
        }
        
        return historico;
    }

}