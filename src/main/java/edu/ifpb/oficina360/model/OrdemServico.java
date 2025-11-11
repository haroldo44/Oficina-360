package edu.ifpb.oficina360.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "ORDEMSERVICO") // nome da tabela conforme seu banco
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ordem_seq")
    @SequenceGenerator(name = "ordem_seq", sequenceName = "SEQ_ORDEM_SERVICO", allocationSize = 1)
    @Column(name = "ID_ORDEM")
    private Long idOrdem;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "ID_MECANICO")
    private Mecanico mecanico;

    @Column(name = "DESCRICAO_PROBLEMA")
    private String descricaoProblemaCliente;

    @Column(name = "DIAGNOSTICO_MECANICO")
    private String diagnosticoMecanico;

    @Column(name = "SOLUCAO_APLICADA")
    private String solucaoAplicada;

    @Column(name = "PECAS_TROCADAS")
    private String pecasTrocadas;

    @Column(name = "ITENS_ADICIONAIS")
    private String itensAdicionais;

    @Column(name = "MAO_DE_OBRA")
    private Double maoDeObra;

    @Column(name = "VALOR_TOTAL")
    private Double valorTotal;

    @Column(name = "DATA_ABERTURA")
    private LocalDate dataAgendamento;

    @Column(name = "DATA_FINALIZACAO")
    private LocalDate dataFinalizacao;

    // Getters e Setters
    public Long getIdOrdem() { return idOrdem; }
    public void setIdOrdem(Long idOrdem) { this.idOrdem = idOrdem; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Mecanico getMecanico() { return mecanico; }
    public void setMecanico(Mecanico mecanico) { this.mecanico = mecanico; }

    public String getDescricaoProblemaCliente() { return descricaoProblemaCliente; }
    public void setDescricaoProblemaCliente(String descricaoProblemaCliente) { this.descricaoProblemaCliente = descricaoProblemaCliente; }

    public String getDiagnosticoMecanico() { return diagnosticoMecanico; }
    public void setDiagnosticoMecanico(String diagnosticoMecanico) { this.diagnosticoMecanico = diagnosticoMecanico; }

    public String getSolucaoAplicada() { return solucaoAplicada; }
    public void setSolucaoAplicada(String solucaoAplicada) { this.solucaoAplicada = solucaoAplicada; }

    public String getPecasTrocadas() { return pecasTrocadas; }
    public void setPecasTrocadas(String pecasTrocadas) { this.pecasTrocadas = pecasTrocadas; }

    public String getItensAdicionais() { return itensAdicionais; }
    public void setItensAdicionais(String itensAdicionais) { this.itensAdicionais = itensAdicionais; }

    public Double getMaoDeObra() { return maoDeObra; }
    public void setMaoDeObra(Double maoDeObra) { this.maoDeObra = maoDeObra; }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }

    public LocalDate getDataAgendamento() { return dataAgendamento; }
    public void setDataAgendamento(LocalDate dataAgendamento) { this.dataAgendamento = dataAgendamento; }

    public LocalDate getDataFinalizacao() { return dataFinalizacao; }
    public void setDataFinalizacao(LocalDate dataFinalizacao) { this.dataFinalizacao = dataFinalizacao; }
}
