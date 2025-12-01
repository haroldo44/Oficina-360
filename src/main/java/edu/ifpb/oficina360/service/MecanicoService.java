package edu.ifpb.oficina360.service;

import edu.ifpb.oficina360.model.Mecanico;
import edu.ifpb.oficina360.model.MecanicoCadastroDTO;
import edu.ifpb.oficina360.model.Oficina;
import edu.ifpb.oficina360.repository.MecanicoRepository;
import edu.ifpb.oficina360.repository.OficinaRepository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class MecanicoService {

    private final String UPLOAD_DIR = "uploads/";

    @Autowired
    private MecanicoRepository mecanicoRepository;

    @Autowired
    private OficinaRepository oficinaRepository;

    // Método auxiliar: Salva a foto no disco e retorna o nome do arquivo
    private String salvarFoto(MultipartFile foto) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);

        // Garante que o diretório 'uploads/' existe
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String nomeArquivo = UUID.randomUUID() + "_" + foto.getOriginalFilename();
        Files.copy(foto.getInputStream(),
                uploadPath.resolve(nomeArquivo),
                StandardCopyOption.REPLACE_EXISTING);

        return nomeArquivo;
    }

    /**
     * Converte o DTO para Entidade, lida com horários e foto, e salva o Mecânico.
     */
    @Transactional
    public Mecanico salvarMecanico(MecanicoCadastroDTO dto, Long oficinaId) throws IOException {

        Oficina oficina = oficinaRepository.findById(oficinaId)
                .orElseThrow(() -> new RuntimeException("Oficina não encontrada"));

        Mecanico mecanico = new Mecanico();

        // 1. Processar e salvar foto (usa o arquivo que foi anexado no Controller)
        if (dto.getFotoArquivo() != null && !dto.getFotoArquivo().isEmpty()) {
            String nomeFoto = salvarFoto(dto.getFotoArquivo());
            mecanico.setNomeArquivoFoto(nomeFoto);
        } else {
            mecanico.setNomeArquivoFoto(null); // Define como NULL se não houver foto
        }

        // 2. Converter String de Horário para LocalTime (Pode lançar DateTimeParseException)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        mecanico.setTurnoManhaInicio(LocalTime.parse(dto.getTurnoManhaInicioString(), formatter));
        mecanico.setTurnoManhaFim(LocalTime.parse(dto.getTurnoManhaFimString(), formatter));
        mecanico.setTurnoTardeInicio(LocalTime.parse(dto.getTurnoTardeInicioString(), formatter));
        mecanico.setTurnoTardeFim(LocalTime.parse(dto.getTurnoTardeFimString(), formatter));

        // 3. Setar dados e relacionamento
        mecanico.setNomeCompleto(dto.getNomeCompleto());
        mecanico.setEmail(dto.getEmail());
        mecanico.setSenha(dto.getSenha());
        mecanico.setNumeroTelefone(dto.getNumeroTelefone());
        mecanico.setOficina(oficina);

        // O campo 'horaInicio' da Entidade Mecanico parece ser redundante e deve ser removido ou setado.
        // Se você não o removeu, defina ele para evitar NOT NULL (usando o inicio da manhã como placeholder)
        // Se a Entidade Mecanico for corrigida para remover este campo, esta linha é desnecessária.
        if (mecanico.getHoraInicio() == null) {
            mecanico.setHoraInicio(mecanico.getTurnoManhaInicio());
        }

        return mecanicoRepository.save(mecanico);
    }

    public List<Mecanico> buscarMecanicosPorOficina(Long oficinaId) {
        return mecanicoRepository.findByOficinaId(oficinaId);
    }

    public void removerMecanico(Long mecanicoId) {
        mecanicoRepository.deleteById(mecanicoId);
    }
    
    public void editarMecanico(Long id, MecanicoCadastroDTO dto) {
        Mecanico mecanico = mecanicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mecânico não encontrado com id: " + id));

        mecanico.setNomeCompleto(dto.getNomeCompleto());
        mecanico.setEmail(dto.getEmail());
        mecanico.setSenha(dto.getSenha());
        mecanico.setNumeroTelefone(dto.getNumeroTelefone());
        mecanico.setTurnoManhaInicio(LocalTime.parse(dto.getTurnoManhaInicioString()));
        mecanico.setTurnoManhaFim(LocalTime.parse(dto.getTurnoManhaFimString()));
        mecanico.setTurnoTardeInicio(LocalTime.parse(dto.getTurnoTardeInicioString()));
        mecanico.setTurnoTardeFim(LocalTime.parse(dto.getTurnoTardeFimString()));

        // Atualiza a foto se foi enviada
        if (dto.getFotoArquivo() != null && !dto.getFotoArquivo().isEmpty()) {
            try {
                String nomeArquivo = salvarFoto(dto.getFotoArquivo()); // método que grava no disco
                mecanico.setNomeArquivoFoto(nomeArquivo);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao salvar foto do mecânico: " + e.getMessage(), e);
            }
        }

        mecanicoRepository.save(mecanico);
    }

    
    


}