package edu.ifpb.oficina360.model;

import java.util.List;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "oficinas")
public class Oficina {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oficina_seq")
    @SequenceGenerator(name = "oficina_seq", sequenceName = "oficina_seq", allocationSize = 1)
    private Long id;

    @Column
    private String nomeDaOficina;

    @Column(unique = true)
    private String cnpj;

    @Column
    private String cidade;

    @Column
    private String bairro;

    @Column
    private String rua;

    @Column
    private String fotoProprietario;

    @Column
    private String imagem;

    @Transient
    private MultipartFile fotoProprietarioArquivo;

    @Transient
    private MultipartFile imagemOficinaArquivo;

    @Column
    private String nomeDoProprietario;

    @Column
    private String telefoneDoProprietario;

    @Column
    private String cidadeDoProprietario;

    @Column
    private String bairroDoProprietario;

    @Column
    private String ruaDoProprietario;

    @Column
    private String email;

    @Column
    private String senha;

    @Transient
    private String confirmarSenha;

    @Column(name = "descricao", columnDefinition = "CLOB") 
    private String descricao;

    @OneToMany(mappedBy = "oficina")
    private List<Servico> servicos;
    
    @OneToMany(mappedBy = "oficina")
    private List<Mecanico> mecanicos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeDaOficina() {
		return nomeDaOficina;
	}

	public void setNomeDaOficina(String nomeDaOficina) {
		this.nomeDaOficina = nomeDaOficina;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
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

	public String getFotoProprietario() {
		return fotoProprietario;
	}

	public void setFotoProprietario(String fotoProprietario) {
		this.fotoProprietario = fotoProprietario;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public MultipartFile getFotoProprietarioArquivo() {
		return fotoProprietarioArquivo;
	}

	public void setFotoProprietarioArquivo(MultipartFile fotoProprietarioArquivo) {
		this.fotoProprietarioArquivo = fotoProprietarioArquivo;
	}

	public MultipartFile getImagemOficinaArquivo() {
		return imagemOficinaArquivo;
	}

	public void setImagemOficinaArquivo(MultipartFile imagemOficinaArquivo) {
		this.imagemOficinaArquivo = imagemOficinaArquivo;
	}

	public String getNomeDoProprietario() {
		return nomeDoProprietario;
	}

	public void setNomeDoProprietario(String nomeDoProprietario) {
		this.nomeDoProprietario = nomeDoProprietario;
	}

	public String getTelefoneDoProprietario() {
		return telefoneDoProprietario;
	}

	public void setTelefoneDoProprietario(String telefoneDoProprietario) {
		this.telefoneDoProprietario = telefoneDoProprietario;
	}

	public String getCidadeDoProprietario() {
		return cidadeDoProprietario;
	}

	public void setCidadeDoProprietario(String cidadeDoProprietario) {
		this.cidadeDoProprietario = cidadeDoProprietario;
	}

	public String getBairroDoProprietario() {
		return bairroDoProprietario;
	}

	public void setBairroDoProprietario(String bairroDoProprietario) {
		this.bairroDoProprietario = bairroDoProprietario;
	}

	public String getRuaDoProprietario() {
		return ruaDoProprietario;
	}

	public void setRuaDoProprietario(String ruaDoProprietario) {
		this.ruaDoProprietario = ruaDoProprietario;
	}

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Servico> getServicos() {
		return servicos;
	}

	public void setServicos(List<Servico> servicos) {
		this.servicos = servicos;
	}

	public List<Mecanico> getMecanicos() {
		return mecanicos;
	}

	public void setMecanicos(List<Mecanico> mecanicos) {
		this.mecanicos = mecanicos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bairro, bairroDoProprietario, cidade, cidadeDoProprietario, cnpj, confirmarSenha, descricao,
				email, fotoProprietario, fotoProprietarioArquivo, id, imagem, imagemOficinaArquivo, mecanicos,
				nomeDaOficina, nomeDoProprietario, rua, ruaDoProprietario, senha, servicos, telefoneDoProprietario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Oficina other = (Oficina) obj;
		return Objects.equals(bairro, other.bairro) && Objects.equals(bairroDoProprietario, other.bairroDoProprietario)
				&& Objects.equals(cidade, other.cidade)
				&& Objects.equals(cidadeDoProprietario, other.cidadeDoProprietario) && Objects.equals(cnpj, other.cnpj)
				&& Objects.equals(confirmarSenha, other.confirmarSenha) && Objects.equals(descricao, other.descricao)
				&& Objects.equals(email, other.email) && Objects.equals(fotoProprietario, other.fotoProprietario)
				&& Objects.equals(fotoProprietarioArquivo, other.fotoProprietarioArquivo)
				&& Objects.equals(id, other.id) && Objects.equals(imagem, other.imagem)
				&& Objects.equals(imagemOficinaArquivo, other.imagemOficinaArquivo)
				&& Objects.equals(mecanicos, other.mecanicos) && Objects.equals(nomeDaOficina, other.nomeDaOficina)
				&& Objects.equals(nomeDoProprietario, other.nomeDoProprietario) && Objects.equals(rua, other.rua)
				&& Objects.equals(ruaDoProprietario, other.ruaDoProprietario) && Objects.equals(senha, other.senha)
				&& Objects.equals(servicos, other.servicos)
				&& Objects.equals(telefoneDoProprietario, other.telefoneDoProprietario);
	}
}
