package edu.ifpb.oficina360.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.model.Mecanico;
import edu.ifpb.oficina360.model.Servico;
import edu.ifpb.oficina360.repository.ServicoRepository;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PdfComprovanteService pdfComprovanteService;


    // --- MÉTODOS DE LISTAGEM ---
    public List<Servico> listarCancelados(Cliente cliente) {
        List<Servico> lista = new ArrayList<>(servicoRepository.findByClienteAndStatus(cliente, "CANCELADO"));
        filtrarApenasDoCliente(lista, cliente); 
        return lista;
    }
    
    public List<Servico> listarPendentes(Cliente cliente) {
        List<Servico> lista = new ArrayList<>(servicoRepository.findByClienteAndStatus(cliente, "PENDENTE"));
        filtrarApenasDoCliente(lista, cliente);
        return lista;
    }

    public List<Servico> listarFinalizados(Cliente cliente) {
        List<Servico> lista = new ArrayList<>(servicoRepository.findByClienteAndStatus(cliente, "FINALIZADO"));
        filtrarApenasDoCliente(lista, cliente);
        return lista;
    }

    public List<Servico> buscarPorClienteEStatus(Cliente cliente, String status) {
        List<Servico> lista = new ArrayList<>(servicoRepository.findByClienteAndStatus(cliente, status));
        filtrarApenasDoCliente(lista, cliente);
        return lista;
    }

    public List<Servico> buscarPorOficinaEStatus(Long oficinaId, String status) {
        List<Servico> lista = servicoRepository.findByOficinaIdAndStatus(oficinaId, status);
        tratarClienteNulo(lista);
        return lista;
    }

    public List<Servico> buscarPorMecanicoEStatus(Mecanico mecanico, String status) {
        List<Servico> servicos = servicoRepository.findByMecanicoAndStatus(mecanico, status);
        tratarClienteNulo(servicos);
        return servicos;
    }

    public List<Servico> listarHistoricoMecanico(Mecanico mecanico) {
        List<Servico> historico = new ArrayList<>();
        List<Servico> finalizados = servicoRepository.findByMecanicoAndStatus(mecanico, "FINALIZADO");
        if (finalizados != null) historico.addAll(finalizados);
        List<Servico> cancelados = servicoRepository.findByMecanicoAndStatus(mecanico, "CANCELADO");
        if (cancelados != null) historico.addAll(cancelados);
        tratarClienteNulo(historico);
        return historico;
    }

    public Servico salvar(Servico servico) {
        return servicoRepository.save(servico);
    }
    
    // --- MÉTODO PRINCIPAL DE FINALIZAÇÃO ---
    // Este método recebe tudo direto do Controller, então não depende dos getters da entidade para funcionar.
    public void finalizarServicoComDados(
            Long idServico,
            String modelo,
            String placa,
            String marca,
            String chassi,
            String diagnostico,
            String solucao,
            String pecas,
            Double valorPecas,
            Double valorMaoDeObra
    ) {

        Servico servico = servicoRepository.findById(idServico)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        Double total = (valorPecas != null ? valorPecas : 0.0)
                     + (valorMaoDeObra != null ? valorMaoDeObra : 0.0);

        servico.setStatus("FINALIZADO");
        servico.setDataFinalizacao(LocalDate.now());
        servico.setPreco(total);

        if (servico.getDescricao() == null || servico.getDescricao().isEmpty()) {
            servico.setDescricao("Sem descrição informada.");
        }

        // 🔥 GERA E SALVA PDF
        String caminhoPdf = pdfComprovanteService.gerarESalvarPdf(
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

        servico.setCaminhoPdf(caminhoPdf);
        servicoRepository.save(servico);

        // 📧 ENVIA EMAIL (IGUAL AO QUE JÁ FUNCIONAVA)
        if (servico.getCliente() != null && servico.getCliente().getEmail() != null) {
            emailService.enviarComprovantePdf(
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
        }
    }


    // Método legado (CORRIGIDO PARA COMPILAR)
    // Removemos as chamadas para getDiagnosticoMecanico() que não existem na entidade atual.
    public void enviarComprovantePorId(Long idServico) {
        Servico servico = servicoRepository.findById(idServico)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        
        if (servico.getDescricao() == null) servico.setDescricao("Sem descrição informada.");

        if (servico.getCliente() != null && servico.getCliente().getEmail() != null) {
            // Passa valores padrão/vazios para os campos novos, já que não temos de onde tirar do banco
            emailService.enviarComprovantePdf(
                servico, 
                "Ver Sistema", // Modelo
                "-",           // Placa
                "-",           // Marca
                "-",           // Chassi
                "-",           // Diagnóstico (vazio pois não temos getter)
                "-",           // Solução (vazio pois não temos getter)
                "",            // Peças
                0.0,           // Valor Peças
                0.0,           // Valor Mão de Obra
                servico.getPreco() // Total
            );
        }
    }

    // --- Filtros Auxiliares ---
    private void filtrarApenasDoCliente(List<Servico> servicos, Cliente clienteLogado) {
        if (servicos == null) return;
        if (clienteLogado == null || clienteLogado.getId() == null) {
            servicos.clear();
            return;
        }
        servicos.removeIf(s -> {
            try {
                if (s.getCliente() == null) return true;
                return !s.getCliente().getId().equals(clienteLogado.getId());
            } catch (Exception e) {
                return true; 
            }
        });
    }
    
    private void tratarClienteNulo(List<Servico> servicos) {
        if (servicos != null) {
            for (Servico s : servicos) {
                try {
                    if (s.getCliente() == null) {
                        setDummyCliente(s);
                    }
                } catch (Exception e) {
                    setDummyCliente(s);
                }
            }
        }
    }

    private void setDummyCliente(Servico s) {
        Cliente clienteRemovido = new Cliente();
        clienteRemovido.setNome("[Cliente Removido]");
        clienteRemovido.setEmail("-");
        clienteRemovido.setContato("-");
        clienteRemovido.setCpf("-");
        s.setCliente(clienteRemovido);
    }
}