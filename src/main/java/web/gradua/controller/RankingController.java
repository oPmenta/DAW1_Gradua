package web.gradua.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web.gradua.model.Usuario;
import web.gradua.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RankingController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/ranking")
    public String exibirRanking(Model model) {
        // Busca todos os usuários e ordena pela soma das pontuações de seus resultados
        List<Usuario> ranking = usuarioRepository.findAll().stream()
            .sorted((u1, u2) -> {
                Double totalU1 = u1.getResultados().stream().mapToDouble(r -> r.getPontuacao()).sum();
                Double totalU2 = u2.getResultados().stream().mapToDouble(r -> r.getPontuacao()).sum();
                return totalU2.compareTo(totalU1); // Ordem decrescente
            })
            .collect(Collectors.toList());

        model.addAttribute("ranking", ranking);
        return "ranking";
    }
}