package web.gradua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.gradua.model.Resultado;
import web.gradua.model.Usuario; 
import java.util.List;

public interface ResultadoRepository extends JpaRepository<Resultado, Long> {

    List<Resultado> findByUsuarioOrderByRealizadoEmDesc(Usuario usuario);
}