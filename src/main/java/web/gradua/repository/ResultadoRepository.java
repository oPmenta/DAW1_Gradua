package web.gradua.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import web.gradua.model.Resultado;
public interface ResultadoRepository extends JpaRepository<Resultado, Long> {}