package web.gradua.model;

import jakarta.persistence.*;

@Entity
@Table(name = "resposta")
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resposta")
    private Long id;

    private String alternativaEscolhida;

    @ManyToOne
    @JoinColumn(name = "id_questao")
    private Questao questao;

    @ManyToOne
    @JoinColumn(name = "id_resultado")
    private Resultado resultado;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAlternativaEscolhida() { return alternativaEscolhida; }
    public void setAlternativaEscolhida(String alternativaEscolhida) { this.alternativaEscolhida = alternativaEscolhida; }
    public Questao getQuestao() { return questao; }
    public void setQuestao(Questao questao) { this.questao = questao; }
    public Resultado getResultado() { return resultado; }
    public void setResultado(Resultado resultado) { this.resultado = resultado; }
}