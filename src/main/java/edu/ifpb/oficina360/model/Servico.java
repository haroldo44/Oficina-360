package edu.ifpb.oficina360.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="servico_seq")
    @SequenceGenerator(name="servico_seq", sequenceName="servico_seq", allocationSize=1)
    private Long id;

    @Column
    private String titulo;

    // CORREÇÃO AQUI: Mudado de "TEXT" para "CLOB" para funcionar no Oracle
    @Column(name = "descricao", columnDefinition = "CLOB") 
    private String descricao;

    @Column
    private String status; // PENDENTE, FINALIZADO

    @Column(name = "data_agendamento")
    private LocalDate dataAgendamento;
    
    @Column(name = "data_finalizacao")
    private LocalDate dataFinalizacao;

    @Column(name = "hora_agendamento")
    private LocalTime horaAgendamento;
    
    @Column
    private Double preco;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "oficina_id")
    private Oficina oficina;

    @ManyToOne
    @JoinColumn(name = "mecanico_id")
    private Mecanico mecanico;
    
    @Column(name = "caminho_pdf", length = 500)
    private String caminhoPdf;


    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getDataAgendamento() {
		return dataAgendamento;
	}

	public void setDataAgendamento(LocalDate dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}

	public LocalDate getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(LocalDate dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public LocalTime getHoraAgendamento() {
		return horaAgendamento;
	}

	public void setHoraAgendamento(LocalTime horaAgendamento) {
		this.horaAgendamento = horaAgendamento;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public Mecanico getMecanico() {
		return mecanico;
	}

	public void setMecanico(Mecanico mecanico) {
		this.mecanico = mecanico;
	}

	public String getCaminhoPdf() {
		return caminhoPdf;
	}

	public void setCaminhoPdf(String caminhoPdf) {
		this.caminhoPdf = caminhoPdf;
	}

	@Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Servico other = (Servico) obj;
        return Objects.equals(id, other.id);
    }
}