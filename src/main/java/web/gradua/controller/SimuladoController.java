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

    @Autowired private QuestaoRepository questaoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RespostaRepository respostaRepository;
    @Autowired private ResultadoRepository resultadoRepository;
    @Autowired private SimuladoRepository simuladoRepository;
    @Autowired private RelatorioService relatorioService;

    // --- MÉTODOS BASICOS (Copiados do seu original) ---

    @PostMapping("/selecionar-usuario")
    public String selecionarUsuario(@RequestParam("usuarioId") Long usuarioId, HttpSession session) {
        if (usuarioId == null || usuarioId == 0) session.removeAttribute("usuarioLogado");
        else session.setAttribute("usuarioLogado", usuarioRepository.findById(usuarioId).orElse(null));
        return "redirect:/";
    }

    @GetMapping
    public String gerarSimuladoAleatorio(Model model, HttpSession session) {
        List<Questao> todas = questaoRepository.findAll();
        if (todas.isEmpty()) return "redirect:/";
        Collections.shuffle(todas);
        
        Simulado s = new Simulado();
        s.setTitulo("Simulado - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")));
        s.setQuestoes(new ArrayList<>(todas.subList(0, Math.min(8, todas.size()))));

        if (session.getAttribute("usuarioLogado") != null) s = simuladoRepository.save(s);
        
        session.setAttribute("simuladoAtual", s);
        model.addAttribute("simulado", s);
        model.addAttribute("questoesSorteada", s.getQuestoes());
        return "simulado/fazer_prova";
    }

    @PostMapping("/finalizar")
    public String finalizar(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogado");
        Simulado s = (Simulado) session.getAttribute("simuladoAtual");
        if (s == null) return "redirect:/simulados";

        Resultado r = new Resultado();
        r.setSimulado(s);
        r.setRealizadoEm(LocalDateTime.now());
        r.setTotalQuestoes(8);
        
        int acertos = 0, erros = 0;
        for (String key : params.keySet()) {
            if (key.startsWith("resposta_")) {
                Questao q = questaoRepository.findById(Long.parseLong(key.replace("resposta_", ""))).orElse(null);
                if (q != null) {
                    if (q.getGabarito().equalsIgnoreCase(params.get(key))) acertos++;
                    else if (!params.get(key).isEmpty()) erros++;
                }
            }
        }
        r.setAcertos(acertos);
        r.setPontuacao(Math.max(0, acertos - erros));

        if (u != null) {
            if (s.getId() == null) s = simuladoRepository.save(s);
            r.setUsuario(u);
            r.setSimulado(s);
            r = resultadoRepository.save(r);
            
            // Salva respostas
            for (String key : params.keySet()) {
                if (key.startsWith("resposta_")) {
                    Questao q = questaoRepository.findById(Long.parseLong(key.replace("resposta_", ""))).orElse(null);
                    if (q != null) {
                        Resposta resp = new Resposta();
                        resp.setQuestao(q);
                        resp.setResultado(r);
                        resp.setAlternativaEscolhida(params.get(key));
                        respostaRepository.save(resp);
                    }
                }
            }
        } else {
            r.setId(null);
        }

        model.addAttribute("resultado", r);
        model.addAttribute("acertos", acertos);
        model.addAttribute("erros", erros);
        model.addAttribute("pontuacao", r.getPontuacao());
        model.addAttribute("isVisitante", u == null);
        return "simulado/resultado";
    }

    @GetMapping("/historico")
    public String listarHistorico(Model model, HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogado");
        if (u == null) return "redirect:/login"; 
        model.addAttribute("resultados", resultadoRepository.findByUsuarioOrderByRealizadoEmDesc(u));
        return "simulado/historico"; 
    }
    
    @GetMapping("/resultado/{id}/pdf")
    public ResponseEntity<byte[]> baixarPDF(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(relatorioService.gerarRelatorioPDF(id), HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    // --- AQUI ESTÃO OS MÉTODOS DE EDITAR E EXCLUIR ---

    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id, Model model) {
        System.out.println(">>> CLICOU EM EDITAR O ID: " + id); // OLHE O CONSOLE
        
        Resultado r = resultadoRepository.findById(id).orElse(null);
        if (r == null) {
            System.out.println(">>> ERRO: ID NÃO ENCONTRADO NO BANCO");
            return "redirect:/simulados/historico";
        }
        
        model.addAttribute("resultado", r);
        return "simulado/editar";
    }

    @PostMapping("/editar/{id}")
    public String salvarEdicao(@PathVariable Long id, @RequestParam("titulo") String novoTitulo) {
        System.out.println(">>> SALVANDO NOVO TITULO: " + novoTitulo);
        Resultado r = resultadoRepository.findById(id).orElse(null);
        if (r != null) {
            Simulado s = r.getSimulado();
            s.setTitulo(novoTitulo);
            simuladoRepository.save(s);
        }
        return "redirect:/simulados/historico";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        System.out.println(">>> CLICOU EM EXCLUIR O ID: " + id); // OLHE O CONSOLE
        
        Resultado r = resultadoRepository.findById(id).orElse(null);
        if (r != null) {
            // Tenta apagar respostas primeiro
            try {
                List<Resposta> resps = respostaRepository.findByResultado(r);
                if (resps != null) respostaRepository.deleteAll(resps);
            } catch (Exception e) {
                System.out.println(">>> Erro ao apagar respostas: " + e.getMessage());
            }

            Simulado s = r.getSimulado();
            resultadoRepository.delete(r);
            if (s != null) simuladoRepository.delete(s);
        }
        return "redirect:/simulados/historico";
    }
}