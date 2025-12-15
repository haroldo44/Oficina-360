package edu.ifpb.oficina360.service;

import com.lowagie.text.DocumentException;
import edu.ifpb.oficina360.model.Servico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Service
public class PdfComprovanteService {

    private static final String DIRETORIO = "comprovantes";

    @Autowired
    private EmailService emailService;

    public String gerarESalvarPdf(
            Servico servico,
            String modelo,
            String placa,
            String marca,
            String chassi,
            String diagnostico,
            String solucao,
            String pecas,
            Double valorPecas,
            Double valorMaoDeObra,
            Double total
    ) {

        try {
            File pasta = new File(DIRETORIO);
            if (!pasta.exists()) pasta.mkdirs();

            String nomeArquivo = "OS_" + servico.getId() + ".pdf";
            File arquivo = new File(pasta, nomeArquivo);

            ByteArrayOutputStream pdf =
                    emailService.gerarPdfVisual(
                            servico,
                            modelo,
                            placa,
                            marca,
                            chassi,
                            diagnostico,
                            solucao,
                            pecas,
                            valorPecas,
                            valorMaoDeObra,
                            total
                    );

            try (FileOutputStream fos = new FileOutputStream(arquivo)) {
                fos.write(pdf.toByteArray());
            }

            return arquivo.getAbsolutePath();

        } catch (DocumentException | java.io.IOException e) {
            throw new RuntimeException("Erro ao gerar comprovante PDF", e);
        }
    }
}

