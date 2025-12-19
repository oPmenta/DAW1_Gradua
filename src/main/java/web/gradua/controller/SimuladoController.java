package web.gradua.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.gradua.model.*;
import web.gradua.repository.*;
import web.gradua.service.RelatorioService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/simulados")
public class SimuladoController {

    @Autowired
    private QuestaoRepository questaoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RespostaRepository respostaRepository;
    @Autowired
    private ResultadoRepository resultadoRepository;
    @Autowired
    private SimuladoRepository simuladoRepository;
    @Autowired
    private RelatorioService relatorioService;

    // MÉTODO NOVO PARA CORRIGIR O ERRO DO DROPDOWN NO HEADER
    @PostMapping("/selecionar-usuario")
    public String selecionarUsuario(@RequestParam("usuarioId") Long usuarioId, HttpSession session) {
        if (usuarioId == null || usuarioId == 0) {
            session.removeAttribute("usuarioLogado");
        } else {
            // Busca o usuário no banco e salva na sessão
            Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
            session.setAttribute("usuarioLogado", usuario);
        }
        return "redirect:/"; // Redireciona para a home após a troca
    }

    @GetMapping
    public String gerarSimuladoAleatorio(Model model, HttpSession session) {
        List<Questao> todasQuestoes = questaoRepository.findAll();
        if (todasQuestoes.isEmpty())
            return "redirect:/";

        Collections.shuffle(todasQuestoes);
        List<Questao> selecionadas = todasQuestoes.subList(0, Math.min(8, todasQuestoes.size()));

        Simulado novoSimulado = new Simulado();
        novoSimulado.setTitulo(
                "Simulado ENEM - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        novoSimulado.setQuestoes(new ArrayList<>(selecionadas));
        novoSimulado = simuladoRepository.save(novoSimulado);

        session.setAttribute("simuladoAtualId", novoSimulado.getId());
        model.addAttribute("simulado", novoSimulado);
        model.addAttribute("questoesSorteada", selecionadas);
        return "simulado/fazer_prova";
    }

    @PostMapping("/finalizar")
    public String finalizarSimulado(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        Usuario usuarioSessao = (Usuario) session.getAttribute("usuarioLogado");
        Long simuladoId = (Long) session.getAttribute("simuladoAtualId");

        if (usuarioSessao == null || simuladoId == null)
            return "redirect:/login";

        Usuario usuarioReal = usuarioRepository.findById(usuarioSessao.getIdUsuario()).orElse(null);
        Simulado simuladoReal = simuladoRepository.findById(simuladoId).orElse(null);

        if (usuarioReal != null && simuladoReal != null) {
            Resultado res = new Resultado();
            res.setUsuario(usuarioReal);
            res.setSimulado(simuladoReal);
            res.setRealizadoEm(LocalDateTime.now());
            res.setTotalQuestoes(8);
            res = resultadoRepository.save(res);

            int acertos = 0;
            int erros = 0;

            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getKey().startsWith("resposta_")) {
                    Long idQuestao = Long.parseLong(entry.getKey().replace("resposta_", ""));
                    String respAluno = entry.getValue();
                    Questao q = questaoRepository.findById(idQuestao).orElse(null);

                    if (q != null) {
                        if (q.getGabarito().equalsIgnoreCase(respAluno))
                            acertos++;
                        else if (respAluno != null && !respAluno.isEmpty())
                            erros++;

                        Resposta resposta = new Resposta();
                        resposta.setQuestao(q);
                        resposta.setResultado(res);
                        resposta.setAlternativaEscolhida(respAluno);
                        respostaRepository.save(resposta);
                    }
                }
            }

            res.setAcertos(acertos);
            res.setPontuacao(Math.max(0, acertos - erros));
            resultadoRepository.save(res);

            model.addAttribute("resultado", res); 
            model.addAttribute("acertos", acertos); 
            model.addAttribute("erros", erros); 
            model.addAttribute("pontuacao", res.getPontuacao()); 
        }
        return "simulado/resultado";
    }

    @GetMapping("/resultado/{id}/pdf")
    public ResponseEntity<byte[]> baixarResultadoPDF(@PathVariable Long id) {
        try {
            byte[] pdf = relatorioService.gerarRelatorioPDF(id);

            if (pdf == null) {
                System.err.println("ERRO: O PDF retornou nulo.");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "resultado_" + id + ".pdf");

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/historico")
    public String listarHistorico(Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login"; 
        }

        List<Resultado> lista = resultadoRepository.findByUsuarioOrderByRealizadoEmDesc(usuarioLogado);

        model.addAttribute("resultados", lista);
        return "simulado/historico"; 
    }
}