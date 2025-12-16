package edu.ifpb.oficina360.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import edu.ifpb.oficina360.model.Servico;
import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    // Cores
    private static final Color BG_HEADER = new Color(75, 85, 99); 
    private static final Color BG_TOTAL = new Color(245, 245, 245); 
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    
    private static final Locale PT_BR = new Locale("pt", "BR");
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(PT_BR);

    @PostConstruct
    public void init() {
        System.out.println(">>> [EmailService] Serviço de PDF iniciado.");
    }

    public void enviarComprovantePdf(Servico servico, String modelo, String placa, String marca, String chassi,
                                     String diagnostico, String solucao,
                                     String pecas, Double valorPecas, Double valorMaoDeObra, Double total) {
        try {
            ByteArrayOutputStream pdfStream = gerarPdfVisual(servico, modelo, placa, marca, chassi, diagnostico, solucao, pecas, valorPecas, valorMaoDeObra, total);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(remetente != null ? remetente : "oficina@email.com");
            helper.setTo(servico.getCliente().getEmail());
            helper.setSubject("Comprovante de Serviço - " + servico.getCliente().getNome());
            helper.setText("Olá! Segue em anexo o comprovante do serviço realizado.", false);

            helper.addAttachment("OS_" + servico.getId() + ".pdf", new ByteArrayResource(pdfStream.toByteArray()));

            mailSender.send(message);
            System.out.println(">>> [EmailService] PDF enviado para: " + servico.getCliente().getEmail());

        } catch (Exception e) {
            System.err.println(">>> Erro ao gerar PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ByteArrayOutputStream gerarPdfVisual(Servico servico, String modelo, String placa, String marca, String chassi,
                                                 String diagnostico, String solucao,
                                                 String pecas, Double valorPecas, Double valorMaoDeObra, Double total) throws DocumentException {
        
        Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLACK);
        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE); 
        Font fontLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.BLACK);  
        Font fontValue = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.DARK_GRAY);   

        // --- CABEÇALHO ---
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        PdfPCell cellTitle = new PdfPCell(new Phrase("ORDEM DE SERVIÇO REALIZADO", fontTitulo));
        cellTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitle.setBorder(Rectangle.BOTTOM);
        cellTitle.setPaddingBottom(10); 
        cellTitle.setBorderColor(BORDER_COLOR);
        headerTable.addCell(cellTitle);
        document.add(headerTable);
        document.add(new Paragraph(" "));

        // --- 1. CLIENTE ---
        document.add(criarBarraCinza("Cliente", fontHeader));
        PdfPTable tblCliente = new PdfPTable(4); 
        tblCliente.setWidthPercentage(100);
        tblCliente.setWidths(new float[]{1, 1, 1, 1}); 
        
        adicionarCelulaCampo(tblCliente, "Nome", servico.getCliente().getNome(), fontLabel, fontValue);
        
        String endereco = servico.getCliente().getRua() + ", " + servico.getCliente().getBairro() + " - " + servico.getCliente().getCidade();
        PdfPCell cellEnd = criarCelulaBase();
        cellEnd.setColspan(3);
        cellEnd.addElement(new Phrase("Endereço", fontLabel));
        cellEnd.addElement(new Phrase(endereco, fontValue));
        tblCliente.addCell(cellEnd);

        adicionarCelulaCampo(tblCliente, "Telefone", servico.getCliente().getContato() != null ? servico.getCliente().getContato() : "Não informado", fontLabel, fontValue);
        
        PdfPCell cellCpf = criarCelulaBase();
        cellCpf.setColspan(3);
        cellCpf.addElement(new Phrase("CPF", fontLabel));
        cellCpf.addElement(new Phrase(servico.getCliente().getCpf(), fontValue));
        tblCliente.addCell(cellCpf);

        document.add(tblCliente);
        document.add(new Paragraph(" "));

        // --- 2. VEÍCULO ---
        document.add(criarBarraCinza("Veículo", fontHeader));
        PdfPTable tblVeiculo = new PdfPTable(2);
        tblVeiculo.setWidthPercentage(100);
        
        adicionarCelulaCampo(tblVeiculo, "Veículo (Modelo/Ano)", modelo, fontLabel, fontValue);
        adicionarCelulaCampo(tblVeiculo, "Placa", placa, fontLabel, fontValue);
        adicionarCelulaCampo(tblVeiculo, "Marca", marca != null ? marca : "-", fontLabel, fontValue);
        adicionarCelulaCampo(tblVeiculo, "Nº Chassi", chassi != null && !chassi.isEmpty() ? chassi : "-", fontLabel, fontValue);
        
        document.add(tblVeiculo);
        document.add(new Paragraph(" "));

        // --- 3. INFORMAÇÃO CLIENTE (Título + Descrição) ---
        document.add(criarBarraCinza("Informação descrita por cliente", fontHeader));
        PdfPTable tblDesc = new PdfPTable(1);
        tblDesc.setWidthPercentage(100);
        PdfPCell cellDesc = criarCelulaBase();
        cellDesc.setPadding(8); 
        
        // CORREÇÃO: Incluindo o Título se existir
        String tituloProblema = servico.getTitulo() != null && !servico.getTitulo().isEmpty() ? servico.getTitulo() : "Sem Título";
        String descTexto = servico.getDescricao() != null ? servico.getDescricao() : "Não informado.";
        
        // Formatação: Título em Negrito + Quebra + Descrição
        Paragraph pInfoCliente = new Paragraph();
        pInfoCliente.add(new Chunk("Título: " + tituloProblema + "\n", fontLabel)); // Título em negrito
        pInfoCliente.add(new Chunk("\n" + descTexto, fontValue)); // Descrição normal
        
        cellDesc.addElement(pInfoCliente); 
        tblDesc.addCell(cellDesc);
        document.add(tblDesc);
        document.add(new Paragraph(" "));

        // --- 4. DIAGNÓSTICO DO MECÂNICO ---
        document.add(criarBarraCinza("Diagnóstico do mecânico", fontHeader));
        adicionarBlocoTexto(document, diagnostico, fontValue);
        document.add(new Paragraph(" "));

        // --- 5. SOLUÇÃO APLICADA ---
        document.add(criarBarraCinza("Solução", fontHeader));
        adicionarBlocoTexto(document, solucao, fontValue);
        document.add(new Paragraph(" "));

        // --- 6. PEÇAS ---
        document.add(criarBarraCinza("Itens e Peças", fontHeader));
        PdfPTable tblPecas = new PdfPTable(1);
        tblPecas.setWidthPercentage(100);
        
        PdfPCell cellPecasDesc = criarCelulaBase();
        cellPecasDesc.addElement(new Phrase("Descrição das Peças / Material", fontLabel));
        cellPecasDesc.addElement(new Paragraph(pecas != null && !pecas.isEmpty() ? pecas : "Nenhuma peça utilizada", fontValue));
        cellPecasDesc.setPadding(8);
        tblPecas.addCell(cellPecasDesc);
        
        PdfPCell cellPecasValor = criarCelulaBase();
        cellPecasValor.addElement(new Phrase("Valor Total das Peças: " + formatarValor(valorPecas), fontLabel));
        cellPecasValor.setPadding(5);
        cellPecasValor.setBackgroundColor(BG_TOTAL);
        tblPecas.addCell(cellPecasValor);
        
        document.add(tblPecas);
        document.add(new Paragraph(" "));

        // --- 7. MÃO DE OBRA ---
        document.add(criarBarraCinza("Mão de obra", fontHeader));
        PdfPTable tblMaoObra = new PdfPTable(2);
        tblMaoObra.setWidthPercentage(100);
        adicionarCelulaCampo(tblMaoObra, "Mecânico", servico.getMecanico() != null ? servico.getMecanico().getNomeCompleto() : "-", fontLabel, fontValue);
        adicionarCelulaCampo(tblMaoObra, "Valor Mão de Obra", formatarValor(valorMaoDeObra), fontLabel, fontValue);
        document.add(tblMaoObra);
        document.add(new Paragraph(" "));

        // --- 8. INFORMAÇÕES FINAIS ---
        document.add(criarBarraCinza("Informações finais do serviço", fontHeader));
        PdfPTable tblFinal = new PdfPTable(2);
        tblFinal.setWidthPercentage(100);
        
        // Coluna Esquerda
        PdfPCell cellLeft = criarCelulaBase();
        cellLeft.addElement(new Phrase("Oficina: " + (servico.getOficina() != null ? servico.getOficina().getNomeDaOficina() : "-"), fontLabel));
        cellLeft.addElement(new Phrase("Contato: " + (servico.getOficina() != null ? servico.getOficina().getTelefoneDoProprietario() : "-"), fontLabel));
        cellLeft.addElement(new Phrase("CNPJ: " + (servico.getOficina() != null ? servico.getOficina().getCnpj() : "-"), fontLabel));
        String endOficina = servico.getOficina() != null ? servico.getOficina().getRua() + ", " + servico.getOficina().getBairro() + " - " + servico.getOficina().getCidade() : "-";
        cellLeft.addElement(new Phrase("Endereço: " + endOficina, fontLabel));
        cellLeft.setBackgroundColor(BG_TOTAL);
        cellLeft.setPadding(8);
        tblFinal.addCell(cellLeft);

        // Coluna Direita
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataAg = servico.getDataAgendamento() != null ? servico.getDataAgendamento().format(dtf) : "-";
        String dataFin = servico.getDataFinalizacao() != null ? servico.getDataFinalizacao().format(dtf) : LocalDate.now().format(dtf);
        
        PdfPCell cellRight = criarCelulaBase();
        cellRight.addElement(new Phrase("Data agendamento: " + dataAg, fontLabel));
        cellRight.addElement(new Phrase("Data finalização: " + dataFin, fontLabel));
        cellRight.addElement(new Phrase("Protocolo: " + servico.getId(), fontLabel));
        
        Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.BLACK); 
        Paragraph pTotal = new Paragraph("\nTotal do serviço: " + formatarValor(total), fontTotal);
        pTotal.setAlignment(Element.ALIGN_RIGHT);
        cellRight.addElement(pTotal);
        
        cellRight.setBackgroundColor(BG_TOTAL);
        cellRight.setPadding(8);
        tblFinal.addCell(cellRight);

        document.add(tblFinal);

        document.close();
        return out;
    }

    // --- MÉTODOS AUXILIARES ---

    private String formatarValor(Double valor) {
        if (valor == null) return "R$ 0,00";
        return MOEDA.format(valor);
    }

    private PdfPTable criarBarraCinza(String titulo, Font font) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell(new Phrase(titulo, font));
        cell.setBackgroundColor(BG_HEADER);
        cell.setPadding(3); 
        cell.setBorderColor(BORDER_COLOR);
        table.addCell(cell);
        return table;
    }

    private void adicionarCelulaCampo(PdfPTable table, String label, String valor, Font fLabel, Font fValue) {
        PdfPCell cell = criarCelulaBase();
        cell.addElement(new Phrase(label, fLabel));
        cell.addElement(new Phrase(valor, fValue));
        cell.setPadding(4); 
        table.addCell(cell);
    }

    private void adicionarBlocoTexto(Document doc, String texto, Font font) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell = criarCelulaBase();
        cell.setPadding(10);
        cell.setMinimumHeight(40);
        cell.addElement(new Paragraph(texto != null ? texto : "", font));
        table.addCell(cell);
        doc.add(table);
    }

    private PdfPCell criarCelulaBase() {
        PdfPCell cell = new PdfPCell();
        cell.setBorderColor(BORDER_COLOR);
        cell.setPadding(4); 
        return cell;
    }
}