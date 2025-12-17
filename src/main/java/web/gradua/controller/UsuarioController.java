package web.gradua.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import web.gradua.repository.UsuarioRepository;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", repository.findAll());
        return "usuario/lista";
    }
}