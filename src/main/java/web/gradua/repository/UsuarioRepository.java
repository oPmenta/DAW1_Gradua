package web.gradua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.gradua.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}