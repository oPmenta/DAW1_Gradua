package web.gradua.report;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource; // O DataSource faz parte do Java padrão 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Component // Indica que o Spring deve gerenciar esta classe [cite: 47, 48]
public class JaspersoftUtil {
    private static final Logger logger = LoggerFactory.getLogger(JaspersoftUtil.class); // Inicializa o Logger [cite:
                                                                                        // 49]

    public byte[] gerarRelatorio(String arquivoJasper, Map<String, Object> parametros, DataSource dataSource) {

        // CORREÇÃO DO ERRO: Verifica se o caminho do arquivo não é nulo antes de
        // prosseguir
        if (arquivoJasper == null || arquivoJasper.isEmpty()) {
            logger.error("O caminho do arquivo .jasper não pode ser nulo ou vazio.");
            return null;
        }

        try (Connection conexao = dataSource.getConnection()) { // Obtém conexão com o BD
            try {
                // Carrega o arquivo de dentro da pasta src/main/resources [cite: 53, 54]
                ClassPathResource cpr = new ClassPathResource(arquivoJasper);
                InputStream arquivo = cpr.getInputStream();

                // Lógica para identificar o diretório do relatório (útil para sub-relatórios)
                // [cite: 55, 56]
                String urlRelatorio = cpr.getURL().toString();
                String diretorioRelatorios = urlRelatorio.substring(0, urlRelatorio.lastIndexOf("/") + 1);

                if (parametros == null) {
                    parametros = new HashMap<>(); // Garante que o mapa de parâmetros não seja nulo [cite: 57, 58]
                }

                // Define o parâmetro exigido para o funcionamento de subreports [cite: 7, 60]
                parametros.put("SUBREPORT_DIR", diretorioRelatorios);

                // Preenche o relatório com os dados do banco e parâmetros [cite: 61]
                JasperPrint jasperPrint = JasperFillManager.fillReport(arquivo, parametros, conexao);

                // Exporta o resultado final para o formato PDF [cite: 62]
                return JasperExportManager.exportReportToPdf(jasperPrint);

            } catch (JRException e) {
                logger.error("Problemas no Jasper na geracao do PDF do relatório: " + e); // Erro de motor do Jasper
                                                                                          // [cite: 64]
            } catch (IOException e) {
                logger.error("Problemas nos arquivos de relatórios na geracao do PDF do relatório: " + e); // Erro de
                                                                                                           // arquivo
                                                                                                           // [cite: 66]
            }
        } catch (SQLException e) {
            logger.error("Problemas na obtenção de uma conexão com o BD na geração de relatório: " + e); // Erro de
                                                                                                         // conexão
                                                                                                         // [cite: 68,
                                                                                                         // 69]
        }
        return null;
    }
}