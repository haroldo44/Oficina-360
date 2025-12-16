package edu.ifpb.oficina360.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class ServicoAgendamentoDTO {

    private Long idCliente;
    private String titulo;
    private String descricao;
    
    private Long idMecanico;
    
    private LocalDate data;
    private LocalTime hora;
    
    
	public Long getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
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
	public Long getIdMecanico() {
		return idMecanico;
	}
	public void setIdMecanico(Long idMecanico) {
		this.idMecanico = idMecanico;
	}
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	public LocalTime getHora() {
		return hora;
	}
	public void setHora(LocalTime hora) {
		this.hora = hora;
	}
}