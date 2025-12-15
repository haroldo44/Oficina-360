package edu.ifpb.oficina360.controller;

import edu.ifpb.oficina360.model.Servico;
import edu.ifpb.oficina360.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@Controller
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoRepository servicoRepository;

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> baixarComprovante(@PathVariable Long id) {

        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (servico.getCaminhoPdf() == null) {
            return ResponseEntity.notFound().build();
        }

        File arquivo = new File(servico.getCaminhoPdf());

        if (!arquivo.exists()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Resource resource = new UrlResource(arquivo.toURI());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + arquivo.getName() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
