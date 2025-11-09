package web.controlevacinacao.repository.queries.vacina;


import java.util.List;

import web.controlevacinacao.filter.VacinaFilter;
import web.controlevacinacao.model.Vacina;

public interface VacinaQueries {

	// Page<Vacina> pesquisar(VacinaFilter filtro, Pageable pageable);
	
	List<Vacina> pesquisar(VacinaFilter filtro);
	
}
