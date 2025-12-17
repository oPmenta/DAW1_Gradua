package web.gradua.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import web.gradua.model.Questao;
import web.gradua.model.Simulado;
import web.gradua.model.Usuario;
import web.gradua.repository.QuestaoRepository;
import web.gradua.repository.SimuladoRepository;
import web.gradua.repository.UsuarioRepository;

import java.io.InputStream;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(UsuarioRepository usuarioRepository, 
                                      SimuladoRepository simuladoRepository,
                                      QuestaoRepository questaoRepository) {
        return args -> {
            System.out.println("--- INICIANDO DATALOADER ---");

            if (usuarioRepository.count() == 0) {
                Usuario admin = new Usuario();
                admin.setNome("Professor Admin");
                admin.setEmail("admin@gradua.com");
                admin.setSenha("admin");
                admin.setTipo("ADMIN");
                usuarioRepository.save(admin);
                System.out.println("Usuário Admin criado.");
            }

            if (simuladoRepository.count() == 0) {
                try {
                    InputStream inputStream = getClass().getResourceAsStream("/extract.json");
                    
                    if (inputStream == null) {
                        System.out.println("ERRO CRÍTICO: Arquivo 'extract.json' não encontrado na pasta resources!");
                        return;
                    }

                    ObjectMapper mapper = new ObjectMapper();
                    List<QuestaoJson> questoesJson = mapper.readValue(inputStream, new TypeReference<List<QuestaoJson>>(){});

                    Simulado simuladoEnem = new Simulado();
                    simuladoEnem.setTitulo("Simulado ENEM 2025 - Linguagens");
                    simuladoRepository.save(simuladoEnem);

                    System.out.println("Importando " + questoesJson.size() + " questões...");

                    for (QuestaoJson qJson : questoesJson) {
                        Questao q = new Questao();
                        q.setEnunciado(qJson.enunciado);
                        q.setMateria(qJson.materia);
                        q.setGabarito(qJson.gabarito); 
                        q.setSimulado(simuladoEnem);
                        q.setImagemUrl(qJson.imagem_url); 

                        if (qJson.alternativas != null) {
                            for (AlternativaJson alt : qJson.alternativas) {
                                if ("A".equalsIgnoreCase(alt.letra)) q.setAlternativaA(alt.texto);
                                if ("B".equalsIgnoreCase(alt.letra)) q.setAlternativaB(alt.texto);
                                if ("C".equalsIgnoreCase(alt.letra)) q.setAlternativaC(alt.texto);
                                if ("D".equalsIgnoreCase(alt.letra)) q.setAlternativaD(alt.texto);
                                if ("E".equalsIgnoreCase(alt.letra)) q.setAlternativaE(alt.texto);
                            }
                        }
                        questaoRepository.save(q);
                    }
                    System.out.println("--- IMPORTAÇÃO CONCLUÍDA COM SUCESSO ---");

                } catch (Exception e) {
                    System.out.println("ERRO AO IMPORTAR JSON: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class QuestaoJson {
        public String id;
        public String materia;
        public String enunciado;
        public String gabarito; 
        public String imagem_url; 
        
        public List<AlternativaJson> alternativas;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class AlternativaJson {
        public String letra;
        public String texto;
    }
}