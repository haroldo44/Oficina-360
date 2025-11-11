package edu.ifpb.oficina360.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "AGENDAMENTO")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AGENDAMENTO")
    private Long idAgendamento;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "ID_MECANICO")
    private Mecanico mecanico;

    @ManyToOne
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID_USUARIO")
    private Empresa empresa;

    @Column(name = "DESCRICAO_PROBLEMA_CLIENTE") 
    private String descricaoProblemaCliente;

    @Column(name = "DATA_AGENDADA")
    private LocalDate dataAgendada;

    @Column(name = "HORA_AGENDADA")
    private LocalTime horaAgendada;

    @Column(name = "STATUS")
    private String status;

    // Getters e setters
    public Long getIdAgendamento() {
        return idAgendamento;
    }
    public void setIdAgendamento(Long idAgendamento) {
        this.idAgendamento = idAgendamento;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public Mecanico getMecanico() {
        return mecanico;
    }
    public void setMecanico(Mecanico mecanico) {
        this.mecanico = mecanico;
    }
    public Empresa getEmpresa() {
        return empresa;
    }
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    public String getDescricaoProblemaCliente() {
        return descricaoProblemaCliente;
    }
    public void setDescricaoProblemaCliente(String descricaoProblemaCliente) {
        this.descricaoProblemaCliente = descricaoProblemaCliente;
    }
    public LocalDate getDataAgendada() {
        return dataAgendada;
    }
    public void setDataAgendada(LocalDate dataAgendada) {
        this.dataAgendada = dataAgendada;
    }
    public LocalTime getHoraAgendada() {
        return horaAgendada;
    }
    public void setHoraAgendada(LocalTime horaAgendada) {
        this.horaAgendada = horaAgendada;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
