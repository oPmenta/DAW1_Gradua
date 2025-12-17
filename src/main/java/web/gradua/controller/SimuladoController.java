package web.gradua.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import web.gradua.model.Questao;
import web.gradua.model.Simulado;
import web.gradua.repository.QuestaoRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/simulados")
public class SimuladoController {

    private final QuestaoRepository questaoRepository;

    public SimuladoController(QuestaoRepository questaoRepository) {
        this.questaoRepository = questaoRepository;
    }

    @GetMapping
    public String gerarSimuladoAleatorio(Model model) {
        // Busca TODAS as questões do banco de dados
        List<Questao> todasQuestoes = questaoRepository.findAll();
        
        System.out.println("=================================================");
        System.out.println("DIAGNÓSTICO: Iniciando busca de questões...");
        System.out.println("TOTAL ENCONTRADO NO BANCO: " + todasQuestoes.size());
        System.out.println("=================================================");
        
        if (todasQuestoes.isEmpty()) {
            Questao qErro = new Questao();
            qErro.setEnunciado("ERRO: Banco vazio. Verifique o DataLoader.");
            qErro.setMateria("DEBUG");
            qErro.setAlternativaA("Verifique o terminal");
            todasQuestoes.add(qErro);
        }

        // Embaralha as questões
        Collections.shuffle(todasQuestoes);

        // Seleciona 8 questões, priorizando 1 de cada matéria
        List<Questao> selecionadas = new ArrayList<>();
        Set<String> materiasJaAdicionadas = new HashSet<>();

        for (Questao q : todasQuestoes) {
            if (selecionadas.size() >= 8) break;

            if (!materiasJaAdicionadas.contains(q.getMateria())) {
                selecionadas.add(q);
                materiasJaAdicionadas.add(q.getMateria());
            }
        }

        if (selecionadas.size() < 8) {
            for (Questao q : todasQuestoes) {
                if (selecionadas.size() >= 8) break;
                if (!selecionadas.contains(q)) {
                    selecionadas.add(q);
                }
            }
        }

        Simulado simuladoRapido = new Simulado();
        simuladoRapido.setTitulo("Simulado Rápido - 8 Questões");
        
        model.addAttribute("simulado", simuladoRapido);
        model.addAttribute("questoesSorteada", selecionadas);

        return "simulado/fazer_prova";
    }

    @PostMapping("/finalizar")
    public String finalizarSimulado(@RequestParam Map<String, String> params, Model model) {
        int acertos = 0;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();   // Ex: resposta_15
            String respostaAluno = entry.getValue(); // Ex: A

            if (key.startsWith("resposta_")) {
                
                try {
                    Long questaoId = Long.parseLong(key.split("_")[1]);
                    Questao questao = questaoRepository.findById(questaoId).orElse(null);
                    
                    if (questao != null) {
                        String gabarito = questao.getGabarito(); 

                        if (gabarito != null && gabarito.equalsIgnoreCase(respostaAluno)) {
                            acertos++;
                        } else if (gabarito == null) {
                            System.out.println("AVISO: A questão ID " + questaoId + " não tem gabarito salvo no banco!");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao corrigir questão: " + key + ". Detalhe: " + e.getMessage());
                }
            }
        }

        model.addAttribute("acertos", acertos);
        model.addAttribute("total", 8);
        
        return "simulado/resultado";
    }
}