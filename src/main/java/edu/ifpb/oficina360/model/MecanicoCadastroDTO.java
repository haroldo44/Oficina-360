package edu.ifpb.oficina360.model; // Crie um novo pacote 'dto' se for o caso

import java.time.LocalTime;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*; // Use a importação correta (jakarta ou javax)

public class MecanicoCadastroDTO {
    
    // Usado para receber o arquivo da foto
    private MultipartFile fotoArquivo;

    @NotBlank(message = "O nome é obrigatório")
    private String nomeCompleto;

    @Email(message = "Email inválido")
    @NotBlank(message = "O email é obrigatório")
    private String email;

    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    @NotBlank(message = "O telefone é obrigatório")
    private String numeroTelefone;

    // O nome do arquivo será salvo no DB (para exibição)
    private String nomeArquivoFoto;
    
    @NotBlank(message = "O início da manhã é obrigatório")
    private String turnoManhaInicioString;

    @NotBlank(message = "O fim da manhã é obrigatório")
    private String turnoManhaFimString; 

    @NotBlank(message = "O início da tarde é obrigatório")
    private String turnoTardeInicioString; 

    @NotBlank(message = "O fim da tarde é obrigatório")
    private String turnoTardeFimString;

	public MultipartFile getFotoArquivo() {
		return fotoArquivo;
	}

	public void setFotoArquivo(MultipartFile fotoArquivo) {
		this.fotoArquivo = fotoArquivo;
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

	public String getNomeArquivoFoto() {
		return nomeArquivoFoto;
	}

	public void setNomeArquivoFoto(String nomeArquivoFoto) {
		this.nomeArquivoFoto = nomeArquivoFoto;
	}

	public String getTurnoManhaInicioString() {
		return turnoManhaInicioString;
	}

	public void setTurnoManhaInicioString(String turnoManhaInicioString) {
		this.turnoManhaInicioString = turnoManhaInicioString;
	}

	public String getTurnoManhaFimString() {
		return turnoManhaFimString;
	}

	public void setTurnoManhaFimString(String turnoManhaFimString) {
		this.turnoManhaFimString = turnoManhaFimString;
	}

	public String getTurnoTardeInicioString() {
		return turnoTardeInicioString;
	}

	public void setTurnoTardeInicioString(String turnoTardeInicioString) {
		this.turnoTardeInicioString = turnoTardeInicioString;
	}

	public String getTurnoTardeFimString() {
		return turnoTardeFimString;
	}

	public void setTurnoTardeFimString(String turnoTardeFimString) {
		this.turnoTardeFimString = turnoTardeFimString;
	}
}