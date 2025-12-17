package web.gradua.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "simulado")
public class Simulado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_simulado")
    private Long id;

    private String titulo;

    @OneToMany(mappedBy = "simulado", cascade = CascadeType.ALL)
    private List<Questao> questoes;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public List<Questao> getQuestoes() { return questoes; }
    public void setQuestoes(List<Questao> questoes) { this.questoes = questoes; }
}