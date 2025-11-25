package edu.ifpb.oficina360.model;

import java.util.Objects;

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
@Table(name = "servicos")
public class Servico {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="servico_seq")
    @SequenceGenerator(name="servico_seq", sequenceName="servico_seq",allocationSize=1)
    private Long id;

	@Column
    private String titulo;

	@Column(name = "descricao", columnDefinition = "CLOB") // Força o uso do CLOB no Oracle
	private String descricao;

    @Column
    private String status;  
    // valores: "PENDENTE" ou "FINALIZADO"

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "oficina_id")
    private Oficina oficina;

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

	@Override
	public int hashCode() {
		return Objects.hash(cliente, descricao, id, oficina, status, titulo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Servico other = (Servico) obj;
		return Objects.equals(cliente, other.cliente) && Objects.equals(descricao, other.descricao)
				&& Objects.equals(id, other.id) && Objects.equals(oficina, other.oficina)
				&& Objects.equals(status, other.status) && Objects.equals(titulo, other.titulo);
	}
}

