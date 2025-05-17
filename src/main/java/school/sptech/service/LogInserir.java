package school.sptech.service;

import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.modulos.Log;

import java.util.List;

public class LogInserir {

    private JdbcTemplate template;

    public LogInserir(JdbcTemplate template) {
        this.template = template;
    }

    public void registrarLog(Log log) {
        try {
            template.update(
                    "INSERT INTO log (nivel, mensagem, mensagem_log) VALUES (?, ?, ?)",
                    log.getNivel(),
                    log.getMensagem(),
                    log.getMensagemLog()
            );
            System.out.println("Log registrado: " + log.getMensagemLog());
        } catch (Exception e) {
            System.err.println("Erro ao registrar log: " + e.getMessage());
        }
    }


    public List<Log> obterTodosLogs() {
        String sql = "SELECT id_log, data_hora, nivel, mensagem, mensagem_log FROM log";
        return template.query(sql, (rs, rowNum) -> {
            Log log = new Log(
                    rs.getString("nivel"),
                    rs.getString("mensagem"),
                    rs.getString("mensagem_log")
            );
            log.setIdLog(rs.getInt("id_log"));
            log.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime()); // Mapeando data e hora
            return log;
        });
    }


}