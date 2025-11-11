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
        return clienteRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + idUsuario));
    }

    @Transactional
    public Cliente salvar(Cliente cliente, Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setTipo("CLIENTE");
        usuarioRepository.save(usuario);

        cliente.setUsuario(usuario);
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(Long idUsuario, Cliente clienteAtualizado) {

        Cliente cliente = clienteRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + idUsuario));
                
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setCpf(clienteAtualizado.getCpf());
        cliente.setCidade(clienteAtualizado.getCidade());
        cliente.setBairro(clienteAtualizado.getBairro());
        cliente.setRua(clienteAtualizado.getRua());
        cliente.setTelefone(clienteAtualizado.getTelefone());
        

        return clienteRepository.save(cliente);
    }

    @Transactional
    public void deletar(Long idUsuario) {
        
        Cliente cliente = clienteRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado para o ID: " + idUsuario));
        
        clienteRepository.delete(cliente);
        usuarioRepository.deleteById(idUsuario);
    }
}