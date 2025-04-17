package school.sptech;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LeitorExcel {

    public List<Interrupcao> extrairInterrupcoes(String nomeArquivo, InputStream arquivo) {
        try {
            System.out.println("\nIniciando leitura do arquivo %s\n".formatted(nomeArquivo));

            // Criando um objeto Workbook a partir do arquivo recebido
            Workbook workbook;
            if (nomeArquivo.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(arquivo);
            } else {
                workbook = new HSSFWorkbook(arquivo);
            }

            Sheet sheet = workbook.getSheetAt(0);

            List<Interrupcao> interrupcoesExtraidas = new ArrayList<>();

            // Iterando sobre as linhas da planilha
            for (Row row : sheet) {

                if (row.getRowNum() == 0) {
                    System.out.println("\nLendo cabeçalho");

                    for (int i = 0; i < 19; i++) {
                        String coluna = row.getCell(i).getStringCellValue();
                        System.out.println("Coluna " + i + ": " + coluna);
                    }

                    System.out.println("--------------------");
                    continue;
                }

                // Extraindo valor das células e criando objeto Interrupcao
                System.out.println("Lendo linha " + row.getRowNum());

                Interrupcao interrupcao = new Interrupcao();
                interrupcao.setId((int) row.getCell(0).getNumericCellValue());
                interrupcao.setUnidadeConsumidora(row.getCell(3).getStringCellValue());
                interrupcao.setInicio(row.getCell(9).getLocalDateTimeCellValue());
                interrupcao.setFim(row.getCell(10).getLocalDateTimeCellValue());
                interrupcao.setFatorGerador(row.getCell(11).getStringCellValue());
                Duration pegarDuracao = Duration.between(row.getCell(9).getLocalDateTimeCellValue(), row.getCell(10).getLocalDateTimeCellValue());
                interrupcao.setDuracao(pegarDuracao.toMinutes());

                interrupcoesExtraidas.add(interrupcao);
            }

            // Fechando o workbook após a leitura
            workbook.close();

            System.out.println("\nLeitura do arquivo finalizada\n");

            return interrupcoesExtraidas;
        } catch (IOException e) {
            // Caso ocorra algum erro durante a leitura do arquivo uma exceção será lançada
            throw new RuntimeException(e);
        }
    }

    private LocalDate converterDate(Date data) {
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
