package web.gradua.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario {

    // Constantes para facilitar a verificação de tipo
    public static final String PADRAO = "PADRAO";
    public static final String ADMIN = "ADMIN";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    private String nome;
    private String email;
    private String senha;
    private String tipo;

    @OneToMany(mappedBy = "usuario")
    private List<Resultado> resultados = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha, String tipo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }

    // Getters e Setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public List<Resultado> getResultados() { return resultados; }
    public void setResultados(List<Resultado> resultados) { this.resultados = resultados; }

    @Override
    public String toString() {
        return "Usuario{id=" + idUsuario + ", nome='" + nome + "'}";
    }
}