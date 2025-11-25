package edu.ifpb.oficina360.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarClientes(){
        return clienteRepository.findAll();
    }
    
    public Cliente cadastrarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    public Cliente atualizarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // 👉 ADICIONE ESTE MÉTODO
    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    public Long deletarCliente(Cliente clienterequest) {
        clienteRepository.deleteById(clienterequest.getId());
        return 0L;
    }
    
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
    }
}
