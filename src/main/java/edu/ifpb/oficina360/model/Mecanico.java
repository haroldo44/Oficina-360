package edu.ifpb.oficina360.model;

import java.time.LocalTime;
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
@Table(name = "mecanicos")
public class Mecanico {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mecanico_seq")
    @SequenceGenerator(name = "mecanico_seq", sequenceName = "mecanico_seq", allocationSize = 1)
    private Long id;
	
	@Column
	private String nomeCompleto;
	
	@Column
	private String email;
	
	@Column
	private String senha;
	
	@Column
	private String numeroTelefone;
    
    // NOVO CAMPO PARA ARMAZENAR O NOME DO ARQUIVO DA FOTO NO DISCO
    @Column(name = "foto_arquivo")
    private String nomeArquivoFoto;
	
	@Column(name = "hora_inicio_servico", nullable = false)
    private LocalTime horaInicio;
	
	@ManyToOne
	@JoinColumn(name = "oficina_id")
	private Oficina oficina;
	
	@Column(name = "turno_manha_inicio", nullable = false)
	private LocalTime turnoManhaInicio;

	@Column(name = "turno_manha_fim", nullable = false)
	private LocalTime turnoManhaFim;

	@Column(name = "turno_tarde_inicio", nullable = false)
	private LocalTime turnoTardeInicio;

	@Column(name = "turno_tarde_fim", nullable = false)
	private LocalTime turnoTardeFim;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
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

	public String getNumeroTelefone() {
		return numeroTelefone;
	}

	public void setNumeroTelefone(String numeroTelefone) {
		this.numeroTelefone = numeroTelefone;
	}
    
    // NOVO GETTER E SETTER
    public String getNomeArquivoFoto() {
		return nomeArquivoFoto;
	}

	public void setNomeArquivoFoto(String nomeArquivoFoto) {
		this.nomeArquivoFoto = nomeArquivoFoto;
	}
    // FIM NOVO GETTER E SETTER

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}
    
    public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public LocalTime getTurnoManhaInicio() {
		return turnoManhaInicio;
	}

	public void setTurnoManhaInicio(LocalTime turnoManhaInicio) {
		this.turnoManhaInicio = turnoManhaInicio;
	}

	public LocalTime getTurnoManhaFim() {
		return turnoManhaFim;
	}

	public void setTurnoManhaFim(LocalTime turnoManhaFim) {
		this.turnoManhaFim = turnoManhaFim;
	}

	public LocalTime getTurnoTardeInicio() {
		return turnoTardeInicio;
	}

	public void setTurnoTardeInicio(LocalTime turnoTardeInicio) {
		this.turnoTardeInicio = turnoTardeInicio;
	}

	public LocalTime getTurnoTardeFim() {
		return turnoTardeFim;
	}

	public void setTurnoTardeFim(LocalTime turnoTardeFim) {
		this.turnoTardeFim = turnoTardeFim;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, horaInicio, id, nomeArquivoFoto, nomeCompleto, numeroTelefone, oficina, senha,
				turnoManhaFim, turnoManhaInicio, turnoTardeFim, turnoTardeInicio);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mecanico other = (Mecanico) obj;
		return Objects.equals(email, other.email) && Objects.equals(horaInicio, other.horaInicio)
				&& Objects.equals(id, other.id) && Objects.equals(nomeArquivoFoto, other.nomeArquivoFoto)
				&& Objects.equals(nomeCompleto, other.nomeCompleto)
				&& Objects.equals(numeroTelefone, other.numeroTelefone) && Objects.equals(oficina, other.oficina)
				&& Objects.equals(senha, other.senha) && Objects.equals(turnoManhaFim, other.turnoManhaFim)
				&& Objects.equals(turnoManhaInicio, other.turnoManhaInicio)
				&& Objects.equals(turnoTardeFim, other.turnoTardeFim)
				&& Objects.equals(turnoTardeInicio, other.turnoTardeInicio);
	}
}