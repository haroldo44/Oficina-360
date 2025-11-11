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


    // LISTAR TODOS
    public List<Agendamento> listar() {
        return agendamentoRepository.findAll();
    }

    // BUSCAR POR ID
    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado!"));
    }

    // SALVAR (com cliente, mecânico e empresa)
    public Agendamento salvar(Agendamento agendamento, Long idCliente, Long idMecanico, Long idEmpresa) {

        // 1. Busca as entidades relacionadas ou lança exceção (RuntimeException)
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        Mecanico mecanico = mecanicoRepository.findById(idMecanico)
                .orElseThrow(() -> new RuntimeException("Mecânico não encontrado!"));

        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada!"));

        // 2. Associa as entidades ao agendamento
        agendamento.setCliente(cliente);
        agendamento.setMecanico(mecanico);
        agendamento.setEmpresa(empresa);

        // 3. Salva e retorna o agendamento
        return agendamentoRepository.save(agendamento);
    }

    // ATUALIZAR
    public Agendamento atualizar(Long id, Agendamento dados) {
        Agendamento agendamento = buscarPorId(id);

        // Atualiza apenas os campos mutáveis
        agendamento.setDataAgendada(dados.getDataAgendada());
        // CORREÇÃO: Uso do método 'getHoraAgendada()' do modelo Agendamento.java
        agendamento.setHoraAgendada(dados.getHoraAgendada());
        agendamento.setStatus(dados.getStatus());
        agendamento.setDescricaoProblemaCliente(dados.getDescricaoProblemaCliente());
        
        return agendamentoRepository.save(agendamento);
    }

    // DELETAR
    public void deletar(Long id) {
        agendamentoRepository.deleteById(id);
    }
}