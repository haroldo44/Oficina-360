package edu.ifpb.oficina360.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "urgencias")
public class Urgencia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "urgencia_seq")
    @SequenceGenerator(name = "urgencia_seq", sequenceName = "urgencia_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "oficina_id")
    private Oficina oficina;

    @NotBlank
    private String descricao;

    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    private boolean resolvido = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDateTime getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public boolean isResolvido() {
		return resolvido;
	}

	public void setResolvido(boolean resolvido) {
		this.resolvido = resolvido;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cliente, dataSolicitacao, descricao, id, oficina, resolvido);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Urgencia other = (Urgencia) obj;
		return Objects.equals(cliente, other.cliente) && Objects.equals(dataSolicitacao, other.dataSolicitacao)
				&& Objects.equals(descricao, other.descricao) && Objects.equals(id, other.id)
				&& Objects.equals(oficina, other.oficina) && resolvido == other.resolvido;
	}
}

