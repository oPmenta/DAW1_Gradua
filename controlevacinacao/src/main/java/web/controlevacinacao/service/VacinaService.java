package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.repository.VacinaRepository;

@Service
public class VacinaService {

    private VacinaRepository repositorio;

    public VacinaService(VacinaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional
    public void salvar(Vacina vacina) {
        repositorio.save(vacina);
    }

    @Transactional
    public void alterar(Vacina vacina) {
        repositorio.save(vacina);
    }
}
