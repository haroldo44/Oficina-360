package edu.ifpb.oficina360.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    
    // Listar todos os clientes
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }
    
    // Salvar cliente (serve para cadastro e atualização)
    @Transactional
    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    // Deletar cliente pelo ID
    @Transactional
    public void deletarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente não encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }
    
    // Buscar cliente pelo ID
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));
    }

    // Buscar cliente pelo email (para login)
    public Cliente buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email).orElse(null);
    }
}
