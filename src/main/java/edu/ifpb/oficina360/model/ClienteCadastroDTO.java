package edu.ifpb.oficina360.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ClienteCadastroDTO {

	@Email(message = "E-mail inválido")
	@NotBlank(message = "O e-mail é obrigatório")
	private String email;

	@NotBlank(message = "A senha é obrigatória")
	private String senha;

	@NotBlank(message = "Confirmação de senha é obrigatória")
	private String confirmarSenha;

	// Remover NotBlank aqui
	private String nome;

	// Remover NotBlank aqui
	private String cpf;

	private String cidade;
	private String bairro;
	private String rua;
	private Long oficinaId;
    
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getConfirmarSenha() {
		return confirmarSenha;
	}
	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getRua() {
		return rua;
	}
	public void setRua(String rua) {
		this.rua = rua;
	}
	public Long getOficinaId() {
		return oficinaId;
	}
	public void setOficinaId(Long oficinaId) {
		this.oficinaId = oficinaId;
	}
}
