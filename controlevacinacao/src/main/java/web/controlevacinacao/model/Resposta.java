package web.controlevacinacao.model;

import jakarta.persistence.*;

@Entity
public class Resposta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResposta;
    
    private String alternativaEscolhida;
    
    // muitas respostas pertencem a um usuário
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
    
    // muitas respostas pertencem a uma questão
    @ManyToOne
    @JoinColumn(name = "idQuestao")
    private Questao questao;
    
    public Resposta() {
    }
    
    public Resposta(String alternativaEscolhida, Usuario usuario, Questao questao) {
        this.alternativaEscolhida = alternativaEscolhida;
        this.usuario = usuario;
        this.questao = questao;
    }
    
    // Getters e Setters
    public Long getIdResposta() {
        return idResposta;
    }
    
    public void setIdResposta(Long idResposta) {
        this.idResposta = idResposta;
    }
    
    public String getAlternativaEscolhida() {
        return alternativaEscolhida;
    }
    
    public void setAlternativaEscolhida(String alternativaEscolhida) {
        this.alternativaEscolhida = alternativaEscolhida;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Questao getQuestao() {
        return questao;
    }
    
    public void setQuestao(Questao questao) {
        this.questao = questao;
    }
    
    @Override
    public String toString() {
        return "Resposta{" +
                "idResposta=" + idResposta +
                ", alternativaEscolhida='" + alternativaEscolhida + '\'' +
                '}';
    }
}