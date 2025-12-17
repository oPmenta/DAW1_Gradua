package web.gradua.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import web.gradua.model.Questao;
public interface QuestaoRepository extends JpaRepository<Questao, Long> {}