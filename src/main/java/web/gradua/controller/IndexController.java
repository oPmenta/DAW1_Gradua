package web.gradua.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.gradua.model.Usuario;
import web.gradua.repository.UsuarioRepository;

@Controller
public class IndexController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping(value = {"/", "/index.html"})
    public String index(Model model, HttpSession session) {
        // 1. Busca todos os usuários do banco (criados pelo DataLoader) e envia para o dropdown
        model.addAttribute("todosUsuarios", usuarioRepository.findAll());
        
        // 2. Pega o usuário que está na sessão e coloca no model para o HTML enxergar
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));
        
        return "index";
    }

    @PostMapping("/simulacao/selecionar-usuario")
    public String selecionarUsuario(@RequestParam("usuarioId") Long id, HttpSession session) {
        if (id == null || id == 0) {
            session.removeAttribute("usuarioLogado");
        } else {
            // Busca o usuário completo no banco pelo ID e guarda na Sessão do navegador
            Usuario user = usuarioRepository.findById(id).orElse(null);
            session.setAttribute("usuarioLogado", user);
        }
        return "redirect:/"; // Recarrega a página para aplicar as mudanças
    }
}