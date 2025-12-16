package edu.ifpb.oficina360.service;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.model.ClienteCadastroDTO;
import edu.ifpb.oficina360.model.Oficina;
import edu.ifpb.oficina360.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private OficinaService oficinaService;

    private final String uploadDir = "uploads/";

    public ClienteService() {
        File pasta = new File(uploadDir);
        if (!pasta.exists()) pasta.mkdirs();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }


    public String salvarArquivoTemporario(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) return null;
        String nomeArquivo = UUID.randomUUID() + "_" + StringUtils.cleanPath(arquivo.getOriginalFilename());
        try {
            Files.createDirectories(Paths.get(uploadDir));
            try (InputStream in = arquivo.getInputStream()) {
                Files.copy(in, Paths.get(uploadDir + nomeArquivo), StandardCopyOption.REPLACE_EXISTING);
            }
            return nomeArquivo;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo temporário: " + e.getMessage(), e);
        }
    }


    public Cliente finalizarCadastro(ClienteCadastroDTO dto, String fotoNome) {
        Cliente cliente = new Cliente();

        cliente.setEmail(dto.getEmail());
        cliente.setSenha(dto.getSenha()); 
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setContato(dto.getContato());
        cliente.setCidade(dto.getCidade());
        cliente.setBairro(dto.getBairro());
        cliente.setRua(dto.getRua());

        Oficina oficina = oficinaService.buscarPorId(dto.getOficinaId());
        cliente.setOficina(oficina);

        if (fotoNome != null) {
            cliente.setFoto(fotoNome);
        }

        return clienteRepository.save(cliente);
    }

    // Atualiza foto do cliente.
    public void atualizarFotoCliente(Long id, MultipartFile novaFoto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (novaFoto != null && !novaFoto.isEmpty()) {
            try {
                String nomeArquivo = UUID.randomUUID() + "_" + novaFoto.getOriginalFilename();
                Files.copy(novaFoto.getInputStream(),
                        Paths.get("uploads/" + nomeArquivo),
                        StandardCopyOption.REPLACE_EXISTING);
                cliente.setFoto(nomeArquivo);
                clienteRepository.save(cliente);

            } catch (IOException e) {
                throw new RuntimeException("Erro ao atualizar foto do cliente: " + e.getMessage());
            }
        }
    }


    public void salvar(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    public void excluir(Long id) {
        clienteRepository.deleteById(id);
    }

    public boolean autenticar(String email, String senhaDigitada) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return cliente.getSenha().equals(senhaDigitada);
    }

    public Cliente buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email).orElse(null);
    }
}
