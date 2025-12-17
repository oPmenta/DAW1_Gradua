package web.gradua.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultado")
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resultado")
    private Long id;

    private Double pontuacao;
    private Integer acertos;
    private Integer totalQuestoes;
    private String status; 
    private LocalDateTime realizadoEm;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_simulado")
    private Simulado simulado;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getPontuacao() { return pontuacao; }
    public void setPontuacao(Double pontuacao) { this.pontuacao = pontuacao; }
    public Integer getAcertos() { return acertos; }
    public void setAcertos(Integer acertos) { this.acertos = acertos; }
    public Integer getTotalQuestoes() { return totalQuestoes; }
    public void setTotalQuestoes(Integer totalQuestoes) { this.totalQuestoes = totalQuestoes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getRealizadoEm() { return realizadoEm; }
    public void setRealizadoEm(LocalDateTime realizadoEm) { this.realizadoEm = realizadoEm; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Simulado getSimulado() { return simulado; }
    public void setSimulado(Simulado simulado) { this.simulado = simulado; }
}