package web.gradua.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.gradua.model.*;
import web.gradua.repository.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/simulados")
public class SimuladoController {

    private final QuestaoRepository questaoRepository;
    private final UsuarioRepository usuarioRepository;
    
    @Autowired
    private ResultadoRepository resultadoRepository;
    
    @Autowired
    private SimuladoRepository simuladoRepository;

    public SimuladoController(QuestaoRepository questaoRepository, UsuarioRepository usuarioRepository) {
        this.questaoRepository = questaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String gerarSimuladoAleatorio(Model model, HttpSession session) {
        List<Questao> todasQuestoes = questaoRepository.findAll();
        if (todasQuestoes.isEmpty()) return "redirect:/";

        Collections.shuffle(todasQuestoes);
        int quantidade = Math.min(8, todasQuestoes.size());
        List<Questao> selecionadas = todasQuestoes.subList(0, quantidade);

        // 1. Criamos um Simulado ÚNICO para esta tentativa
        Simulado novoSimulado = new Simulado();
        novoSimulado.setTitulo("Simulado ENEM - " + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        novoSimulado.setQuestoes(new ArrayList<>(selecionadas)); // Salva a lista de questões sorteadas
        
        // 2. Persistimos o simulado no banco ANTES de começar a prova
        novoSimulado = simuladoRepository.save(novoSimulado);

        // 3. Guardamos o ID na sessão para recuperar no PostMapping
        session.setAttribute("simuladoAtualId", novoSimulado.getId());

        model.addAttribute("simulado", novoSimulado);
        model.addAttribute("questoesSorteada", selecionadas);
        return "simulado/fazer_prova";
    }

    @PostMapping("/finalizar")
    public String finalizarSimulado(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        int acertos = 0;
        int erros = 0;
        
        // StringBuilder para montar a String de respostas: "ID:MARCOU;ID:MARCOU"
        StringJoiner respostasJoiner = new StringJoiner(";");

        Usuario usuarioSessao = (Usuario) session.getAttribute("usuarioLogado");
        Long simuladoId = (Long) session.getAttribute("simuladoAtualId");

        // Se o simuladoId sumiu da sessão, redireciona (evita erros)
        if (simuladoId == null) return "redirect:/simulados";

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().startsWith("resposta_")) {
                String idStr = entry.getKey().replace("resposta_", "");
                String respAluno = entry.getValue();

                // Guarda a escolha: "12:A"
                respostasJoiner.add(idStr + ":" + respAluno);

                Questao q = questaoRepository.findById(Long.parseLong(idStr)).orElse(null);
                if (q != null) {
                    if (q.getGabarito() != null && q.getGabarito().equalsIgnoreCase(respAluno)) {
                        acertos++;
                    } else if (respAluno != null && !respAluno.isEmpty()) {
                        erros++;
                    }
                }
            }
        }

        double pontuacaoFinal = Math.max(0, acertos - erros);

        if (usuarioSessao != null) {
            Usuario usuarioReal = usuarioRepository.findById(usuarioSessao.getIdUsuario()).orElse(null);
            Simulado simuladoReal = simuladoRepository.findById(simuladoId).orElse(null);

            if (usuarioReal != null && simuladoReal != null) {
                Resultado res = new Resultado();
                res.setUsuario(usuarioReal);
                res.setSimulado(simuladoReal); // Aqui salvamos qual foi o conjunto de questões
                res.setAcertos(acertos);
                res.setTotalQuestoes(8);
                res.setPontuacao(pontuacaoFinal);
                res.setRealizadoEm(LocalDateTime.now());
                
                // Salvamos o rastro das respostas para o PDF comparar com o gabarito depois
                res.setStatus(respostasJoiner.toString()); 
                
                resultadoRepository.save(res);
            }
        }

        model.addAttribute("acertos", acertos);
        model.addAttribute("erros", erros);
        model.addAttribute("pontuacao", (int) pontuacaoFinal);
        return "simulado/resultado";
    }
}