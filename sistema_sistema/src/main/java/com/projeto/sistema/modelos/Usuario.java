package com.projeto.sistema.modelos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios") // Nome da tabela no banco de dados
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L; // Controle de versão da serialização

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID gerado automaticamente pelo banco
    private Long id_usuario;
    private String nome_usuario;
    private String email;
    private String senha;
    private String tipo;
    private String funcao;
    
    private LocalDateTime data_cadastro = LocalDateTime.now(); // Data e hora do cadastro

    @ElementCollection
    @CollectionTable(name = "usuario_dias_permitidos", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "dia")
    private List<String> diasPermitidos;

    private LocalTime horaInicio;
    private LocalTime horaFim;

    // Construtor com todos os parâmetros necessários
    public Usuario(String nome_usuario, String email, String senha) {
        this.nome_usuario = nome_usuario;
        this.email = email;
        this.senha = senha;
        this.data_cadastro = LocalDateTime.now(); // Data e hora de cadastro
    }

    // Construtor padrão
    public Usuario() {}

    // Getters e Setters
    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNome_usuario() {
        return nome_usuario;
    }

    public void setNome_usuario(String nome_usuario) {
        this.nome_usuario = nome_usuario;
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

    public LocalDateTime getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(LocalDateTime data_cadastro) {
        this.data_cadastro = data_cadastro;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public List<String> getDiasPermitidos() {
        return diasPermitidos;
    }

    public void setDiasPermitidos(List<String> diasPermitidos) {
        this.diasPermitidos = diasPermitidos;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }
}
