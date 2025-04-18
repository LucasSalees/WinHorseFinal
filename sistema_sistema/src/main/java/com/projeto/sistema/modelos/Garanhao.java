package com.projeto.sistema.modelos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "garanhoes") // Nome da tabela no banco de dados
public class Garanhao implements Serializable {

    private static final long serialVersionUID = 1L; // Controle de versão da serialização

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_garanhao;

    private String nome_garanhao;
    private String cor_palheta;
    private int caneca;
    private String botijao;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data_contagem_inicial = LocalDate.now(); // Apenas data
    
    private LocalDateTime data_cadastro = LocalDateTime.now(); // Data e hora
    
    private int saldo_atual_palhetas; // Agora apenas esse campo é utilizado
    private BigDecimal valor;
    private String modalidade;
    private String moeda;

    @Column(nullable = false)
    private int quantidade = 0; // Valor padrão para quantidade

    // Construtor sem saldo_inicial_palhetas
    public Garanhao(String nome_garanhao, String cor_palheta, int caneca, int saldo_atual_palhetas, int quantidade) {
        this.nome_garanhao = nome_garanhao;
        this.cor_palheta = cor_palheta;
        this.caneca = caneca;
        this.saldo_atual_palhetas = saldo_atual_palhetas; // Apenas saldo atual
        this.quantidade = quantidade; // Preenchendo o campo quantidade
        this.data_cadastro = LocalDateTime.now(); // Data e hora de cadastro
        this.data_contagem_inicial = LocalDate.now(); // Data da contagem inicial
    }

    // Construtor padrão (caso precise)
    public Garanhao() {
    }

    // Getters e Setters
    public Long getId_garanhao() {
        return id_garanhao;
    }

    public void setId_garanhao(Long id_garanhao) {
        this.id_garanhao = id_garanhao;
    }

    public String getNome_garanhao() {
        return nome_garanhao;
    }

    public void setNome_garanhao(String nome_garanhao) {
        this.nome_garanhao = nome_garanhao;
    }

    public String getCor_palheta() {
        return cor_palheta;
    }

    public void setCor_palheta(String cor_palheta) {
        this.cor_palheta = cor_palheta;
    }

    public int getCaneca() {
        return caneca;
    }

    public void setCaneca(int caneca) {
        this.caneca = caneca;
    }

    public String getBotijao() {
        return botijao;
    }

    public void setBotijao(String botijao) {
        this.botijao = botijao;
    }

    public LocalDate getData_contagem_inicial() {
        return data_contagem_inicial;
    }

    public void setData_contagem_inicial(LocalDate data_contagem_inicial) {
        this.data_contagem_inicial = data_contagem_inicial;
    }

    public LocalDateTime getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(LocalDateTime localDateTime) {
        this.data_cadastro = localDateTime;
    }

    public int getSaldo_atual_palhetas() {
        return saldo_atual_palhetas;
    }

    public void setSaldo_atual_palhetas(int saldo_atual_palhetas) {
        this.saldo_atual_palhetas = saldo_atual_palhetas;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    // Método para ajustar o saldo atual de palhetas
    public void ajustarSaldoAtual(int quantidade) {
        this.saldo_atual_palhetas += quantidade;
    }
}
