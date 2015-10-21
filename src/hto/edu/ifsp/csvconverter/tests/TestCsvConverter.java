package hto.edu.ifsp.csvconverter.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.mockrunner.mock.jdbc.MockResultSet;

import hto.edu.ifsp.csvconverter.CsvConverter;

public class TestCsvConverter {

	// private final static String FOLDER_PATH = "D://";
	private final static String FOLDER_PATH = "/Users/phimac/Documents/";

	private static void deleteFile(String fileName) {
		File file = new File(fileName);
		file.delete();
	}

	/**
	 * Compara os dados de um dado result set e um arquivo csv, coluna por
	 * coluna.
	 * 
	 * O metodo não funciona se os dados contiverem virgula.
	 * 
	 * @param rsToTest
	 * @param fileToTest
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	private static boolean compareResultSetAndFile(ResultSet rsToTest,
			String fileToTest) throws IOException, SQLException {
		BufferedReader br = null;
		try {
			// preparando para abrir o arquivo.
			FileInputStream stream = new FileInputStream(fileToTest);
			InputStreamReader reader = new InputStreamReader(stream);
			br = new BufferedReader(reader);
			String linha = br.readLine();
			String[] linhaSplited;
			// pegando o numero de colunas no resultSet
			ResultSetMetaData rsmd = rsToTest.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			// voltando o resultset para antes da primeira linha, para que o
			// next gere o resultado esperado.
			rsToTest.beforeFirst();
			while (linha != null) {
				// separando as colunas pela virgula
				linhaSplited = linha.split(",");
				if (linhaSplited.length != columnsNumber) {
					return false;
				} else {
					rsToTest.next();
					for (int i = 1; i <= columnsNumber; i++) {
						// pegando os dados do result set e cruzando com os do
						// arquivo
						String content;
						// tratando dados entre aspas.
						if (rsToTest.getString(i) != null
								&& rsToTest.getString(i).contains(",")) {
							content = "\"" + rsToTest.getString(i) + "\"";
						} else {
							content = rsToTest.getString(i);
						}
						if (!content.equals(linhaSplited[i - 1])) {
							return false;
						}
					}
					linha = br.readLine();
				}
			}
		} finally {
			br.close();
		}
		return true;
	}

	/**
	 * Teste com duas linhas e uma coluna.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static boolean test1() throws Exception {
		// Criando um resultset de teste
		MockResultSet rs = new MockResultSet(null);
		rs.addRow(new Object[] { "Test row 1" });
		rs.addRow(new Object[] { "Test row 2" });
		deleteFile(FOLDER_PATH + "teste.csv");
		// criando o csv a partir do result set.
		CsvConverter test = new CsvConverter();
		test.resultSetToCsv(rs, FOLDER_PATH + "teste.csv");
		// verificando o conteúdo
		return compareResultSetAndFile(rs, FOLDER_PATH + "teste.csv");
	}

	/**
	 * Teste com duas linhas e três colunas.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static boolean test2() throws Exception {
		// Criando um resultset de teste
		MockResultSet rs = new MockResultSet(null);
		rs.addRow(new Object[] { "Test", "row", Integer.valueOf(1) });
		rs.addRow(new Object[] { "Test", "row", Integer.valueOf(2) });
		deleteFile(FOLDER_PATH + "teste2.csv");
		// criando o csv a partir do result set.
		CsvConverter test = new CsvConverter();
		test.resultSetToCsv(rs, FOLDER_PATH + "teste2.csv");
		// verificando o conteúdo
		return compareResultSetAndFile(rs, FOLDER_PATH + "teste2.csv");
	}

	/**
	 * Teste com duas linhas e três colunas + virgula.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static boolean test3() throws Exception {
		// Criando um resultset de teste
		MockResultSet rs = new MockResultSet(null);
		rs.addRow(new Object[] { "Test, row 1" });
		rs.addRow(new Object[] { "Test, row 2" });
		// criando o csv a partir do result set.
		CsvConverter test = new CsvConverter();
		deleteFile(FOLDER_PATH + "teste3.csv");
		test.resultSetToCsv(rs, FOLDER_PATH + "teste3.csv");
		// verificando o conteúdo
		return compareResultSetAndFile(rs, FOLDER_PATH + "teste3.csv");
	}

	private static boolean test4() throws Exception {
		CsvConverter test = new CsvConverter();
		if (test.resultSetToCsv(null, FOLDER_PATH + "teste4.csv")) {
			return false;
		} else {
			return true;
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.print("teste 1 = ");
		if (TestCsvConverter.test1()) {
			System.out.println("sucesso!");
		} else {
			System.out.println("Erro!");
		}

		System.out.print("teste 2 = ");
		if (TestCsvConverter.test2()) {
			System.out.println("sucesso!");
		} else {
			System.out.println("Erro!");
		}

		System.out.print("teste 3 = ");
		if (TestCsvConverter.test3()) {
			System.out.println("sucesso!");
		} else {
			System.out.println("Erro!");
		}

		System.out.print("teste 4 = ");
		if (TestCsvConverter.test4()) {
			System.out.println("sucesso!");
		} else {
			System.out.println("Erro!");
		}

	}
}
