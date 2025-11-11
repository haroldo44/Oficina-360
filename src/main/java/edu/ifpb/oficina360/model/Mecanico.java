package edu.ifpb.oficina360.model;

import jakarta.persistence.*;

@Entity
@Table(name = "MECANICO")
public class Mecanico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mecanico_seq")
    @SequenceGenerator(name = "mecanico_seq", sequenceName = "SEQ_MECANICO", allocationSize = 1)
    @Column(name = "ID_MECANICO")
    private Long idMecanico;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "ID_EMPRESA", nullable = false)
    private Empresa empresa;

    @OneToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false, unique = true)
    private Usuario usuario;

    public Mecanico() {}

    // Getters e Setters
    public Long getIdMecanico() { return idMecanico; }
    public void setIdMecanico(Long idMecanico) { this.idMecanico = idMecanico; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
