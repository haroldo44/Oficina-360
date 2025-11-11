package edu.ifpb.oficina360.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.model.Mecanico;
import edu.ifpb.oficina360.model.OrdemServico;
import edu.ifpb.oficina360.repository.OrdemServicoRepository;
import edu.ifpb.oficina360.repository.ClienteRepository;
import edu.ifpb.oficina360.repository.MecanicoRepository;

@Service
public class OrdemServicoService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    // INJEÇÃO NECESSÁRIA para buscar as entidades Cliente e Mecanico
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MecanicoRepository mecanicoRepository;


    // 1. MÉTODO: listar()
    public List<OrdemServico> listar() {
        return ordemServicoRepository.findAll();
    }

    // 2. MÉTODO: buscarPorId(Long id) - Retornando a entidade (não Optional)
    public OrdemServico buscarPorId(Long id) {
        return ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada! ID: " + id));
    }
    
    // 3. MÉTODO: salvar(OrdemServico ordemServico, Long idCliente, Long idMecanico)
    public OrdemServico salvar(OrdemServico ordemServico, Long idCliente, Long idMecanico) {
        // Busca e associa o Cliente
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado! ID: " + idCliente));

        // Busca e associa o Mecanico
        Mecanico mecanico = mecanicoRepository.findById(idMecanico)
                .orElseThrow(() -> new RuntimeException("Mecânico não encontrado! ID: " + idMecanico));

        ordemServico.setCliente(cliente);
        ordemServico.setMecanico(mecanico);
        
        return ordemServicoRepository.save(ordemServico);
    }
    
    // 4. MÉTODO: atualizar(Long id, OrdemServico novosDados)
    public OrdemServico atualizar(Long id, OrdemServico novosDados) {
        OrdemServico os = buscarPorId(id); // Reusa o método para buscar a OS

        // Atualiza os campos relevantes (mantendo Cliente e Mecânico originais)
        os.setDescricaoProblemaCliente(novosDados.getDescricaoProblemaCliente());
        os.setDiagnosticoMecanico(novosDados.getDiagnosticoMecanico());
        os.setSolucaoAplicada(novosDados.getSolucaoAplicada());
        os.setPecasTrocadas(novosDados.getPecasTrocadas());
        os.setItensAdicionais(novosDados.getItensAdicionais());
        os.setMaoDeObra(novosDados.getMaoDeObra());
        os.setValorTotal(novosDados.getValorTotal());
        os.setDataAgendamento(novosDados.getDataAgendamento());
        os.setDataFinalizacao(novosDados.getDataFinalizacao());

        return ordemServicoRepository.save(os);
    }

    // Método deletar
    public void deletar(Long id) {
        // Verifica se existe antes de deletar
        buscarPorId(id); 
        ordemServicoRepository.deleteById(id);
    }
}