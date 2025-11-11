package edu.ifpb.oficina360.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
@Table(name = "\"EMPRESA\"") // Aspas duplas obrigatórias
public class Empresa {

    // CHAVE PRIMÁRIA COMPARTILHADA (PK/FK)
    @Id
    @Column(name = "ID_USUARIO")
    private Long idUsuario; 

    @OneToOne
    @MapsId // CRÍTICO: Mapeia o campo idUsuario como sendo a PK/FK para Usuario
    @JoinColumn(name = "ID_USUARIO") // Não precisa de referencedColumnName
    private Usuario usuario;
    
    // CAMPOS DA OFICINA
    @Column(name = "CNPJ") private String cnpj;
    @Column(name = "NOME_OFICINA") private String nomeOficina;
    @Column(name = "CIDADE_OFICINA") private String cidadeOficina;
    @Column(name = "BAIRRO_OFICINA") private String bairroOficina;
    @Column(name = "RUA_OFICINA") private String ruaOficina;
    @Column(name = "TELEFONE") private String telefone;
    @Column(name = "IMAGEM_OFICINA") private String imagemOficina;
    
    // CAMPOS DO PROPRIETÁRIO
    @Column(name = "CIDADE_PROPRIETARIO") private String cidadeProprietario;
    @Column(name = "BAIRRO_PROPRIETARIO") private String bairroProprietario;
    @Column(name = "RUA_PROPRIETARIO") private String ruaProprietario;

    public Empresa() {}
    
    // GETTERS E SETTERS
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getNomeOficina() { return nomeOficina; }
    public void setNomeOficina(String nomeOficina) { this.nomeOficina = nomeOficina; }

    public String getCidadeOficina() { return cidadeOficina; }
    public void setCidadeOficina(String cidadeOficina) { this.cidadeOficina = cidadeOficina; }

    public String getBairroOficina() { return bairroOficina; }
    public void setBairroOficina(String bairroOficina) { this.bairroOficina = bairroOficina; }

    public String getRuaOficina() { return ruaOficina; }
    public void setRuaOficina(String ruaOficina) { this.ruaOficina = ruaOficina; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getImagemOficina() { return imagemOficina; }
    public void setImagemOficina(String imagemOficina) { this.imagemOficina = imagemOficina; }

    public String getCidadeProprietario() { return cidadeProprietario; }
    public void setCidadeProprietario(String cidadeProprietario) { this.cidadeProprietario = cidadeProprietario; }

    public String getBairroProprietario() { return bairroProprietario; }
    public void setBairroProprietario(String bairroProprietario) { this.bairroProprietario = bairroProprietario; }

    public String getRuaProprietario() { return ruaProprietario; }
    public void setRuaProprietario(String ruaProprietario) { this.ruaProprietario = ruaProprietario; }
}