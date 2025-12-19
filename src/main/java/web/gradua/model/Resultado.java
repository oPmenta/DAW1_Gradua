package web.gradua.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resultado")
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resultado")
    private Long id;

    private Integer pontuacao;
    private Integer acertos;

    @Column(name = "total_questoes")
    private Integer totalQuestoes;

    @Column(name = "realizado_em")
    private LocalDateTime realizadoEm;
    
    private String status; // Pode manter como rastro, mas não será o principal para o PDF

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_simulado")
    private Simulado simulado;

    // --- NOVA RELAÇÃO ---
    @OneToMany(mappedBy = "resultado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas = new ArrayList<>();

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getPontuacao() { return pontuacao; }
    public void setPontuacao(Integer pontuacao) { this.pontuacao = pontuacao; }
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

    public List<Resposta> getRespostas() { return respostas; }
    public void setRespostas(List<Resposta> respostas) { this.respostas = respostas; }
}