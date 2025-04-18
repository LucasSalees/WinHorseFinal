
package com.projeto.sistema.modelos;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "movimentacoes") // Nome da tabela no banco de dados
public class Movimentacao implements Serializable {

    private static final long serialVersionUID = 1L; // Controle de versão da serialização

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_movimentacao;
    private String botijao;
    private int caneca;
    private String cor_palheta;
    private String endereco;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data_movimentacao;
    private String destino; // Ex: "entrada" ou "saida"
    private int quantidade;
    private String profissional;
    private String nome_profissional;
    private String identificador_profissional;
    private String nome_usuario;
    private String embriao;
    private int quantidade_embriao;

	@ManyToOne
    @JoinColumn(name = "garanhao_id_garanhao", referencedColumnName = "id_garanhao")
    private Garanhao garanhao;

    private String nome_garanhao; // Nome do garanhão associado à movimentação

    // Getters e Setters
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Long getId_movimentacao() {
        return id_movimentacao;
    }

    public void setId_movimentacao(Long id_movimentacao) {
        this.id_movimentacao = id_movimentacao;
    }

    public String getBotijao() {
        return botijao;
    }

    public void setBotijao(String botijao) {
        this.botijao = botijao;
    }

    public int getCaneca() {
        return caneca;
    }

    public void setCaneca(int caneca) {
        this.caneca = caneca;
    }

    public String getCor_palheta() {
        return cor_palheta;
    }

    public void setCor_palheta(String cor_palheta) {
        this.cor_palheta = cor_palheta;
    }

    public LocalDate getData_movimentacao() {
        return data_movimentacao;
    }

    public void setData_movimentacao(LocalDate data_movimentacao) {
        this.data_movimentacao = data_movimentacao;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.quantidade = quantidade;
    }

    public Garanhao getGaranhao() {
        return garanhao;
    }

    public void setGaranhao(Garanhao garanhao) {
        this.garanhao = garanhao;
    }

    public String getNome_garanhao() {
        return nome_garanhao;
    }

    public void setNome_garanhao(String nome_garanhao) {
        this.nome_garanhao = nome_garanhao;
    }

    public String getNome_profissional() {
        return nome_profissional;
    }

    public void setNome_profissional(String nome_profissional) {
        this.nome_profissional = nome_profissional;
    }

    public String getIdentificador_profissional() {
        return identificador_profissional;
    }

    public void setIdentificador_profissional(String identificador_profissional) {
        this.identificador_profissional = identificador_profissional;
    }

    public String getProfissional() {
        return profissional;
    }

    public void setProfissional(String profissional) {
        this.profissional = profissional;
    }

    public String getNome_usuario() {
        return nome_usuario;
    }

    public void setNome_usuario(String nome_usuario) {
        this.nome_usuario = nome_usuario;
    }

    public String getEmbriao() {
        return embriao;
    }

    public void setEmbriao(String embriao) {
        this.embriao = embriao;
    }
    
    public int getQuantidade_embriao() {
		return quantidade_embriao;
	}

	public void setQuantidade_embriao(int quantidade_embriao) {
		this.quantidade_embriao = quantidade_embriao;
	}
}
