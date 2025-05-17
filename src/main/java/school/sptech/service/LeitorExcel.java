package school.sptech.service;

import java.io.IOException;
import java.io.InputStream;
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

import school.sptech.modulos.Distribuidora;
import school.sptech.modulos.Interrupcao;
import school.sptech.modulos.UnidadeDistribuidora;

public class LeitorExcel {

    public List<Interrupcao> extrairInterrupcoes(String nomeArquivo, InputStream arquivo) {
        try {
            System.out.println("\nIniciando leitura do arquivo %s\n".formatted(nomeArquivo));

            Workbook workbook;
            if (nomeArquivo.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(arquivo);
            } else {
                workbook = new HSSFWorkbook(arquivo);
            }

            Sheet sheet = workbook.getSheetAt(0);

            List<Interrupcao> interrupcoesExtraidas = new ArrayList<>();

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

                System.out.println("Lendo linha " + row.getRowNum());

                Interrupcao interrupcao = new Interrupcao();
                interrupcao.setId((int) row.getCell(0).getNumericCellValue());
                interrupcao.setInicio(row.getCell(9).getLocalDateTimeCellValue());
                interrupcao.setFim(row.getCell(10).getLocalDateTimeCellValue());

                String fatorCompleto = row.getCell(11).getStringCellValue();
                String[] partes = fatorCompleto.split(";");
                String fatorPrincipal = partes[partes.length - 1].trim();
                interrupcao.setFatorGerador(fatorPrincipal);

                // Criando a Distribuidora
                String nomeDistribuidora = row.getCell(16).getStringCellValue();
                String sigla = row.getCell(17).getStringCellValue();
                String cnpj = row.getCell(18).getStringCellValue();
                Distribuidora distribuidora = new Distribuidora(cnpj, nomeDistribuidora, sigla);

                // Criando a UnidadeDistribuidora
                String nomeUnidade = row.getCell(3).getStringCellValue();
                UnidadeDistribuidora unidade = new UnidadeDistribuidora(nomeUnidade, distribuidora);

                // Associando à interrupção
                interrupcao.setUnidadeConsumidora(unidade);

                interrupcoesExtraidas.add(interrupcao);
            }

            workbook.close();
            System.out.println("\nLeitura do arquivo finalizada\n");

            return interrupcoesExtraidas;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private LocalDate converterDate(Date data) {
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
