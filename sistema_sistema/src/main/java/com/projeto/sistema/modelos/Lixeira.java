package com.projeto.sistema.modelos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Lixeira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_movimentacao")
    private Long idMovimentacao;
    
    @Column(name = "nome_garanhao")
    private String nomeGaranhao;

    @Column(name = "motivo_exclusao")
    private String motivoExclusao;

    @Column(name = "data_exclusao")
    private LocalDateTime dataExclusao;
    
    @Column(name = "nome_usuario_responsavel") // Adicionando o nome do usuário responsável
    private String nomeUsuarioResponsavel;
    
    @Column(name = "botijao") // Adicionando o nome do usuário responsável
    private String botijao;

    public Lixeira() {}

    public Lixeira(Long idMovimentacao, String motivoExclusao, String nomeGaranhao, String nomeUsuarioResponsavel, String botijao) {
        this.idMovimentacao = idMovimentacao;
        this.motivoExclusao = motivoExclusao;
        this.nomeGaranhao = nomeGaranhao;
        this.nomeUsuarioResponsavel = nomeUsuarioResponsavel; // Atribuindo o nome do usuário responsável
        this.dataExclusao = LocalDateTime.now();
        this.botijao = botijao;
    }

    public String getBotijao() {
		return botijao;
	}

	public void setBotijao(String botijao) {
		this.botijao = botijao;
	}

	// Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(Long idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    public String getMotivoExclusao() {
        return motivoExclusao;
    }

    public void setMotivoExclusao(String motivoExclusao) {
        this.motivoExclusao = motivoExclusao;
    }
    
    public String getNomeGaranhao() {
        return nomeGaranhao;
    }

    public void setNomeGaranhao(String nomeGaranhao) {
        this.nomeGaranhao = nomeGaranhao;
    }

    public String getNomeUsuarioResponsavel() {
        return nomeUsuarioResponsavel;
    }

    public void setNomeUsuarioResponsavel(String nomeUsuarioResponsavel) {
        this.nomeUsuarioResponsavel = nomeUsuarioResponsavel;
    }

    public LocalDateTime getDataExclusao() {
        return dataExclusao;
    }

    public void setDataExclusao(LocalDateTime dataExclusao) {
        this.dataExclusao = dataExclusao;
    }
}
