package web.controlevacinacao.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResultado;

    // muitos resultados pertencem a um usuário
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    // muitos resultados pertencem a um simulado
    @ManyToOne
    @JoinColumn(name = "idSimulado")
    private Simulado simulado;

    private Double pontuacao;
    private Integer acertos;
    private Integer totalQuestoes;
    private LocalDateTime realizadoEm;
    private String status;

    public Resultado() {
    }

    public Resultado(Double pontuacao, Integer acertos, Integer totalQuestoes,
            LocalDateTime realizadoEm, String status, Usuario usuario, Simulado simulado) {
        this.pontuacao = pontuacao;
        this.acertos = acertos;
        this.totalQuestoes = totalQuestoes;
        this.realizadoEm = realizadoEm;
        this.status = status;
        this.usuario = usuario;
        this.simulado = simulado;
    }

    // Getters e Setters
    public Long getIdResultado() {
        return idResultado;
    }

    public void setIdResultado(Long idResultado) {
        this.idResultado = idResultado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Simulado getSimulado() {
        return simulado;
    }

    public void setSimulado(Simulado simulado) {
        this.simulado = simulado;
    }

    public Double getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Double pontuacao) {
        this.pontuacao = pontuacao;
    }

    public Integer getAcertos() {
        return acertos;
    }

    public void setAcertos(Integer acertos) {
        this.acertos = acertos;
    }

    public Integer getTotalQuestoes() {
        return totalQuestoes;
    }

    public void setTotalQuestoes(Integer totalQuestoes) {
        this.totalQuestoes = totalQuestoes;
    }

    public LocalDateTime getRealizadoEm() {
        return realizadoEm;
    }

    public void setRealizadoEm(LocalDateTime realizadoEm) {
        this.realizadoEm = realizadoEm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Resultado{" +
                "idResultado=" + idResultado +
                ", pontuacao=" + pontuacao +
                ", acertos=" + acertos +
                ", totalQuestoes=" + totalQuestoes +
                ", realizadoEm=" + realizadoEm +
                ", status='" + status + '\'' +
                '}';
    }
}