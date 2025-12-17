package web.gradua.model;

import jakarta.persistence.*;

@Entity
@Table(name = "questao")
public class Questao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_questao")
    private Long id;

    @Column(columnDefinition = "TEXT") // Permite textos longos
    private String enunciado;
    
    private String materia;   // Ex: Inglês, Matemática
    private String conteudo;  // Ex: Interpretação de Texto
    private String imagemUrl; // O nome do arquivo da imagem
    private String gabarito;  // A, B, C, D ou E

    // Para simplificar, vamos salvar os textos das alternativas na própria questão
    @Column(columnDefinition = "TEXT") private String alternativaA;
    @Column(columnDefinition = "TEXT") private String alternativaB;
    @Column(columnDefinition = "TEXT") private String alternativaC;
    @Column(columnDefinition = "TEXT") private String alternativaD;
    @Column(columnDefinition = "TEXT") private String alternativaE;

    @ManyToOne
    @JoinColumn(name = "id_simulado")
    private Simulado simulado;

    // --- GETTERS E SETTERS OBRIGATÓRIOS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }
    public String getGabarito() { return gabarito; }
    public void setGabarito(String gabarito) { this.gabarito = gabarito; }
    public String getAlternativaA() { return alternativaA; }
    public void setAlternativaA(String alternativaA) { this.alternativaA = alternativaA; }
    public String getAlternativaB() { return alternativaB; }
    public void setAlternativaB(String alternativaB) { this.alternativaB = alternativaB; }
    public String getAlternativaC() { return alternativaC; }
    public void setAlternativaC(String alternativaC) { this.alternativaC = alternativaC; }
    public String getAlternativaD() { return alternativaD; }
    public void setAlternativaD(String alternativaD) { this.alternativaD = alternativaD; }
    public String getAlternativaE() { return alternativaE; }
    public void setAlternativaE(String alternativaE) { this.alternativaE = alternativaE; }
    public Simulado getSimulado() { return simulado; }
    public void setSimulado(Simulado simulado) { this.simulado = simulado; }
}