package web.gradua.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import web.gradua.model.Usuario;
import web.gradua.repository.UsuarioRepository;
import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Injeta a lista do dropdown em TODAS as páginas
    @ModelAttribute("todosUsuarios")
    public List<Usuario> addTodosUsuariosToModel() {
        return usuarioRepository.findAll();
    }

    // Injeta o usuário da sessão em TODAS as páginas
    @ModelAttribute("usuarioLogado")
    public Usuario addUsuarioToModel(HttpSession session) {
        return (Usuario) session.getAttribute("usuarioLogado");
    }
}