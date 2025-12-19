package web.gradua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.gradua.model.Resposta;
import web.gradua.model.Resultado;
import java.util.List;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {
    
    // O Spring entende "findByResultado" e cria o SQL automaticamente:
    // "SELECT * FROM resposta WHERE id_resultado = ?"
    List<Resposta> findByResultado(Resultado resultado);
    
}