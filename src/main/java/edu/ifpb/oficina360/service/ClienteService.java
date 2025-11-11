package edu.ifpb.oficina360.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.model.Usuario;
import edu.ifpb.oficina360.repository.ClienteRepository;
import edu.ifpb.oficina360.repository.UsuarioRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long idUsuario) {
        // Busca direta no repositório de Cliente usando o ID
        return clienteRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + idUsuario));
    }

    @Transactional
    public Cliente salvar(Cliente cliente, Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // GARANTE QUE O TIPO DO USUÁRIO NÃO FIQUE NULL
        usuario.setTipo("CLIENTE");
        usuarioRepository.save(usuario);

        cliente.setUsuario(usuario);
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(Long idUsuario, Cliente clienteAtualizado) {

        // 1. Busca o Cliente existente
        Cliente cliente = clienteRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + idUsuario));
                
        // 2. Atualiza campos específicos do Cliente
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setCpf(clienteAtualizado.getCpf());
        cliente.setCidade(clienteAtualizado.getCidade());
        cliente.setBairro(clienteAtualizado.getBairro());
        cliente.setRua(clienteAtualizado.getRua());
        cliente.setTelefone(clienteAtualizado.getTelefone());
        
        // 3. Atualiza campos de Usuário herdados (opcional, se necessário)
        // cliente.setEmail(clienteAtualizado.getEmail());

        return clienteRepository.save(cliente);
    }

    @Transactional
    public void deletar(Long idUsuario) {
        
        // 1. Busca o Cliente para garantir que ele existe
        Cliente cliente = clienteRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado para o ID: " + idUsuario));
        
        // 2. Deleta o registro da tabela CLIENTE
        clienteRepository.delete(cliente);

        // 3. Deleta o registro da tabela USUARIO (o registro pai)
        usuarioRepository.deleteById(idUsuario);
    }
}