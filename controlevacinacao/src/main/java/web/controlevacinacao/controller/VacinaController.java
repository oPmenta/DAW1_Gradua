package web.controlevacinacao.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import web.controlevacinacao.filter.VacinaFilter;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.repository.VacinaRepository;
import web.controlevacinacao.service.VacinaService;

@Controller
public class VacinaController {

    private static final Logger logger = LoggerFactory.getLogger(VacinaController.class);
    private VacinaRepository repositorio;
    private VacinaService servico;

    public VacinaController(VacinaRepository repositorio,
                            VacinaService servico) {
        this.repositorio = repositorio;
        this.servico = servico;
    }

    @GetMapping("/vacina/abrirpesquisar")
    public String abrirPesquisar() {
        return "vacina/pesquisar :: formulario";
    }

    @GetMapping("/vacina/pesquisar")
    public String pesquisar(VacinaFilter filtro, Model model) {
        List<Vacina> vacinas = repositorio.pesquisar(filtro);
        logger.debug("Vacinas pesquisadas: {}", vacinas);
        model.addAttribute("vacinas", vacinas);
        return "vacina/mostrar :: tabela";
    }

    @GetMapping("/vacina/cadastrar")
    public String abrirCadastro(Vacina vacina) {
        return "vacina/cadastrar :: formulario";
    }

    @PostMapping("/vacina/cadastrar")
    public String cadastrar(Vacina vacina, RedirectAttributes atributos) {
        servico.salvar(vacina);
        atributos.addFlashAttribute("mensagem", "Vacina cadastrada com sucesso!");
        return "redirect:/mostrarmensagem";
    }

    @GetMapping("/mostrarmensagem")
    public String mostrarMensagem() {
        return "mensagem :: texto";
    }

    @GetMapping("/vacina/alterar/{codigo}")
    public String abrirAlterar(@PathVariable("codigo") Long codigo, Model model) {
        Optional<Vacina> optVacina = repositorio.findById(codigo);
        if (optVacina.isPresent()) {
            Vacina vacina = optVacina.get();
            model.addAttribute("vacina", vacina);
            return "vacina/alterar :: formulario";
        } else {
            model.addAttribute("mensagem", "Não foi encontrada uma vacina com esse código");
            return "mensagem :: texto";
        }
    }

    @PostMapping("/vacina/alterar")
    public String alterar(Vacina vacina, RedirectAttributes atributos) {
        servico.alterar(vacina);
        atributos.addFlashAttribute("mensagem", "Vacina alterada com sucesso!");
        return "redirect:/mostrarmensagem";
    }


}
