package edu.ifpb.oficina360.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ifpb.oficina360.model.Agendamento;
import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.model.Empresa;
import edu.ifpb.oficina360.model.Mecanico;
import edu.ifpb.oficina360.repository.AgendamentoRepository;
import edu.ifpb.oficina360.repository.ClienteRepository;
import edu.ifpb.oficina360.repository.MecanicoRepository;
import edu.ifpb.oficina360.repository.EmpresaRepository;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MecanicoRepository mecanicoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;


    public List<Agendamento> listar() {
        return agendamentoRepository.findAll();
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado!"));
    }

    public Agendamento salvar(Agendamento agendamento, Long idCliente, Long idMecanico, Long idEmpresa) {

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        Mecanico mecanico = mecanicoRepository.findById(idMecanico)
                .orElseThrow(() -> new RuntimeException("Mecânico não encontrado!"));

        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada!"));

        agendamento.setCliente(cliente);
        agendamento.setMecanico(mecanico);
        agendamento.setEmpresa(empresa);

        return agendamentoRepository.save(agendamento);
    }

    public Agendamento atualizar(Long id, Agendamento dados) {
        Agendamento agendamento = buscarPorId(id);

        agendamento.setDataAgendada(dados.getDataAgendada());
        agendamento.setHoraAgendada(dados.getHoraAgendada());
        agendamento.setStatus(dados.getStatus());
        agendamento.setDescricaoProblemaCliente(dados.getDescricaoProblemaCliente());
        
        return agendamentoRepository.save(agendamento);
    }

    public void deletar(Long id) {
        agendamentoRepository.deleteById(id);
    }
}