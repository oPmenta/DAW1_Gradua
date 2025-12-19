package web.gradua.service;

import org.springframework.stereotype.Service;
import web.gradua.report.JaspersoftUtil;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Service
public class RelatorioService {

    private final DataSource dataSource;
    private final JaspersoftUtil jaspersoftUtil;

    public RelatorioService(DataSource dataSource, JaspersoftUtil jaspersoftUtil) {
        this.dataSource = dataSource;
        this.jaspersoftUtil = jaspersoftUtil;
    }

    public byte[] gerarRelatorioPDF(Long idResultado) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("ID_RESULTADO", idResultado);
        return jaspersoftUtil.gerarRelatorio("/relatorios/simulado.jasper", parametros, dataSource);
    }
}