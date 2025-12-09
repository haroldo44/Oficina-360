package edu.ifpb.oficina360.service;

import edu.ifpb.oficina360.model.Oficina;
import edu.ifpb.oficina360.model.OficinaCadastroDTO;
import edu.ifpb.oficina360.repository.OficinaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class OficinaService {

    @Autowired
    private OficinaRepository oficinaRepository;
    
    
    // Agora a pasta é EXTERNA ao projeto
    private final String uploadDir = "uploads/";

    public OficinaService() {
        File pasta = new File(uploadDir);
        if (!pasta.exists()) pasta.mkdirs();
    }

    
    public Oficina buscarPorId(Long id) {
        return oficinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oficina não encontrada"));
    }

    public Oficina finalizarCadastro(
            OficinaCadastroDTO dto,
            MultipartFile imagemOficina,
            MultipartFile fotoProprietario) {

        Oficina oficina = new Oficina();

        oficina.setEmail(dto.getEmail());
        oficina.setSenha(dto.getSenha());
        oficina.setNomeDaOficina(dto.getNomeDaOficina());
        oficina.setCidade(dto.getCidade());
        oficina.setBairro(dto.getBairro());
        oficina.setRua(dto.getRua());
        oficina.setCnpj(dto.getCnpj());

        oficina.setNomeDoProprietario(dto.getNomeDoProprietario());
        oficina.setTelefoneDoProprietario(dto.getTelefoneDoProprietario());
        oficina.setCidadeDoProprietario(dto.getCidadeDoProprietario());
        oficina.setBairroDoProprietario(dto.getBairroDoProprietario());
        oficina.setRuaDoProprietario(dto.getRuaDoProprietario());

        try {
            if (imagemOficina != null && !imagemOficina.isEmpty()) {
                String nomeArquivo = gerarNomeArquivo(imagemOficina);
                salvarArquivo(imagemOficina, nomeArquivo);
                oficina.setImagem(nomeArquivo);
            }

            if (fotoProprietario != null && !fotoProprietario.isEmpty()) {
                String nomeArquivo = gerarNomeArquivo(fotoProprietario);
                salvarArquivo(fotoProprietario, nomeArquivo);
                oficina.setFotoProprietario(nomeArquivo);
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagens: " + e.getMessage());
        }

        return oficinaRepository.save(oficina);
    }

    public void atualizarInformacoes(Long id, Oficina dados) {
        Oficina oficina = oficinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oficina não encontrada"));

        oficina.setNomeDaOficina(dados.getNomeDaOficina());
        oficina.setCnpj(dados.getCnpj());
        oficina.setCidade(dados.getCidade());
        oficina.setBairro(dados.getBairro());
        oficina.setRua(dados.getRua());
        oficina.setTelefoneDoProprietario(dados.getTelefoneDoProprietario());
        oficina.setDescricao(dados.getDescricao());

        oficinaRepository.save(oficina);
    }

    // FOTO OFICINA
    public void atualizarFoto(Long id, MultipartFile novaFoto) {
        Oficina oficina = oficinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oficina não encontrada"));

        if (novaFoto != null && !novaFoto.isEmpty()) {
            try {
                String nomeArquivo = gerarNomeArquivo(novaFoto);
                salvarArquivo(novaFoto, nomeArquivo);
                oficina.setImagem(nomeArquivo);
                oficinaRepository.save(oficina);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao atualizar foto da oficina: " + e.getMessage());
            }
        }
    }

    // FOTO PROPRIETÁRIO
    public void atualizarFotoProprietario(Long id, MultipartFile novaFoto) {
        Oficina oficina = oficinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oficina não encontrada"));

        if (novaFoto != null && !novaFoto.isEmpty()) {
            try {
                String nomeArquivo = gerarNomeArquivo(novaFoto);
                salvarArquivo(novaFoto, nomeArquivo);
                oficina.setFotoProprietario(nomeArquivo);
                oficinaRepository.save(oficina);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao atualizar foto do proprietário: " + e.getMessage());
            }
        }
    }

    // AUXILIAR
    private String gerarNomeArquivo(MultipartFile arquivo) {
        return UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
    }

    private void salvarArquivo(MultipartFile arquivo, String nomeArquivo) throws IOException {
        Files.copy(arquivo.getInputStream(),
                Paths.get(uploadDir + nomeArquivo),
                StandardCopyOption.REPLACE_EXISTING);
    }
    
    public void excluir(Long id) {
        oficinaRepository.deleteById(id);
    }
    
    // Autenticar login
    public boolean autenticar(String email, String senhaDigitada) {
        Oficina oficina = oficinaRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Oficina não encontrada"));

        // Comparação direta (sem hash)
        return oficina.getSenha().equals(senhaDigitada);
    }
    
    public Oficina buscarPorEmail(String email) {
        return oficinaRepository.findByEmail(email).orElse(null);
    }

	 // dentro da classe OficinaService
	 public List<Oficina> buscarTodas() {
	     return oficinaRepository.findAll();
	 }

}
