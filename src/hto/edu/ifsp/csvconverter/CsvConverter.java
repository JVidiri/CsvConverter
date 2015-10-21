package hto.edu.ifsp.csvconverter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Mecanismo Genérico de Geração de arquivo CSV baseado em um SELECT em uma
 * tabela em Banco de Dados. Cada coluna da tabela será uma coluna no CSV.
 */
public class CsvConverter {

	/**
	 * Transforma um result set em um arquivo Csv e salva no arquivo
	 * especificado, acrescenta se o arquivo existir.
	 * 
	 * @param toConvert
	 *            - Resultado do Select
	 * @param saveFilePath
	 *            - Caminho absoluto do arquivo a ser salvo.
	 * @return true - Se a operação foi bem sucedida - false caso o contrário
	 * @throws SQLException
	 *             - Se o ResultSet informado for inválido
	 * @throws IOException
	 *             - Se houver algum problema de IO.
	 */
	public boolean resultSetToCsv(ResultSet toConvert, String saveFilePath)
			throws SQLException, IOException {

		if (toConvert == null)
			return false;
		// criando o buffer para escrita no arquivo,
		// abrindo para append, pois não quero interferir em um possive
		// arquivo
		// já existente
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				saveFilePath, true)));
		try {

			// criando uma variavel para receber temporariamente o nossos dados
			// do
			// resultset
			StringBuilder tempResConvet = new StringBuilder();
			// verificamos quantas colunas temos no resultset
			ResultSetMetaData rsmd = toConvert.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			// Começamos a andar pelas linhas, não volto para a primeira pois
			// estou considerando
			// que o caller pode querer as linhas a partir de determinado ponto.
			while (toConvert.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					// Começamos a transferência dos dados no result set para a
					// string
					String content = toConvert.getString(i);
					// Checo para ver se o conteudo possui virgulas, pois
					// isso quebraria a formtação correta do CSV
					if (content != null && content.contains(",")) {
						// Se possui, adiciono aspas no começo
						// e no fim do conteudo.
						tempResConvet.append("\"");
						tempResConvet.append(content);
						tempResConvet.append("\"");
					} else {
						tempResConvet.append(content);
					}
					if (i != columnsNumber) {
						tempResConvet.append(",");
					}
				}
				// salvo a cada linha, pois não sei quantas linhas receberei no
				// result set.
				out.println(tempResConvet.toString());
				// limpo a minha string para a proxima linha
				tempResConvet.setLength(0);
			}
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return true;
	}
}
