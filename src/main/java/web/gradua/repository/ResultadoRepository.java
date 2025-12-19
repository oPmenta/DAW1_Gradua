package web.gradua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.gradua.model.Resultado;
import web.gradua.model.Usuario; // Importe o seu Model Usuario
import java.util.List;

public interface ResultadoRepository extends JpaRepository<Resultado, Long> {

    // O Spring Data vai gerar o SQL: 
    // SELECT * FROM resultado WHERE id_usuario = ? ORDER BY realizado_em DESC
    List<Resultado> findByUsuarioOrderByRealizadoEmDesc(Usuario usuario);
}