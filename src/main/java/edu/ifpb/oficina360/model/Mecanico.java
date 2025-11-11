package edu.ifpb.oficina360.model;

import jakarta.persistence.*;

@Entity
@Table(name = "MECANICO")
public class Mecanico {

    @Id
    @Column(name = "ID_USUARIO")
    private Long idUsuario; // PK compartilhada

    @OneToOne
    @MapsId
    @JoinColumn(name = "ID_USUARIO") 
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "ID_EMPRESA", nullable = false)
    private Empresa empresa;

    @Column(name = "NOME", nullable = false)
    private String nome;

    public Mecanico() {}

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
