package web.controlevacinacao.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Questao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuestao;
    
    private String enunciado;
    
    // muitas questões pertencem a um simulado
    @ManyToOne
    @JoinColumn(name = "idSimulado")
    private Simulado simulado;
    
    // uma questão tem várias respostas
    @OneToMany(mappedBy = "questao")
    private List<Resposta> respostas = new ArrayList<>();
    
    public Questao() {
    }
    
    public Questao(String enunciado, Simulado simulado) {
        this.enunciado = enunciado;
        this.simulado = simulado;
    }
    
    // Getters e Setters
    public Long getIdQuestao() {
        return idQuestao;
    }
    
    public void setIdQuestao(Long idQuestao) {
        this.idQuestao = idQuestao;
    }
    
    public String getEnunciado() {
        return enunciado;
    }
    
    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }
    
    public Simulado getSimulado() {
        return simulado;
    }
    
    public void setSimulado(Simulado simulado) {
        this.simulado = simulado;
    }
    
    public List<Resposta> getRespostas() {
        return respostas;
    }
    
    public void setRespostas(List<Resposta> respostas) {
        this.respostas = respostas;
    }
    
    @Override
    public String toString() {
        return "Questao{" +
                "idQuestao=" + idQuestao +
                ", enunciado='" + enunciado + '\'' +
                '}';
    }
}