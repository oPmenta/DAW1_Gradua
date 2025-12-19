package web.gradua.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(value = { "/", "/index.html" })
    public String index() {
        // Não precisa mais de model.addAttribute, o GlobalControllerAdvice já faz isso!
        return "index";
    }
}