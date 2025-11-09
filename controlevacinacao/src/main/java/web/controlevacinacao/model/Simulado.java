package web.controlevacinacao.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Simulado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSimulado;

    private String titulo;

    // um simulado pode ter várias questões
    @OneToMany(mappedBy = "simulado")
    private List<Questao> questoes = new ArrayList<>();

    // um simulado pode ter vários resultados
    @OneToMany(mappedBy = "simulado")
    private List<Resultado> resultados = new ArrayList<>();

    public Simulado() {
    }

    public Simulado(String titulo) {
        this.titulo = titulo;
    }

    // Getters e Setters
    public Long getIdSimulado() {
        return idSimulado;
    }

    public void setIdSimulado(Long idSimulado) {
        this.idSimulado = idSimulado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }

    public List<Resultado> getResultados() {
        return resultados;
    }

    public void setResultados(List<Resultado> resultados) {
        this.resultados = resultados;
    }

    @Override
    public String toString() {
        return "Simulado{" +
                "idSimulado=" + idSimulado +
                ", titulo='" + titulo + '\'' +
                '}';
    }
}