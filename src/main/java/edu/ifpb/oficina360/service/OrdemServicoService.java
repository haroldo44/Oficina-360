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

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MecanicoRepository mecanicoRepository;


    public List<OrdemServico> listar() {
        return ordemServicoRepository.findAll();
    }

    public OrdemServico buscarPorId(Long id) {
        return ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada! ID: " + id));
    }
    
    public OrdemServico salvar(OrdemServico ordemServico, Long idCliente, Long idMecanico) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado! ID: " + idCliente));

        Mecanico mecanico = mecanicoRepository.findById(idMecanico)
                .orElseThrow(() -> new RuntimeException("Mecânico não encontrado! ID: " + idMecanico));

        ordemServico.setCliente(cliente);
        ordemServico.setMecanico(mecanico);
        
        return ordemServicoRepository.save(ordemServico);
    }
    
    public OrdemServico atualizar(Long id, OrdemServico novosDados) {
        OrdemServico os = buscarPorId(id); 

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

    public void deletar(Long id) {
        buscarPorId(id); 
        ordemServicoRepository.deleteById(id);
    }
}