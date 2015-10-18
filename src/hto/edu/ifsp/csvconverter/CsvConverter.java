package hto.edu.ifsp.csvconverter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
A p r e n d e r e i m p l e m e n t a r u m 

mecanismo arquitetural em JAVA

• Preparar uma demonstração
• Apresentar para turma como funciona o mecanismo e seu respectivo código-fonte
• Criar uma documentação que contemple a visão do programador que usará o mecanismo
 (usará as interfaces) e a visão de que manterá o mecanismo futuramente 
 (estrutura interna do mecanismo)
• Faça a documentação da forma que
 achar mais adequado (desenho, texto,
 diagrama de classes, etc)
 
 Desafio
 
*/
public class CsvConverter {
	
	/**
	 * Mecanismo Genérico de Geração de arquivo CSV baseado em um SELECT em uma tabela em Banco de Dados 
	 * (cada coluna da tabela será uma coluna no CSV)
	 */
	public boolean resultSetToCsv(ResultSet toConvert, String saveFilePath) throws SQLException, IOException{
		//criando o beffer para escrita no arquivo, 
		//abrindo para append, pois não quero interferir em um possive arquivo já existente.
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(saveFilePath, true)));
		//criando uma variavel para receber temporariamente o nossos dados do resultset
		StringBuilder tempResConvet = new StringBuilder();
		//verificamos quantas colunas temos no resultset
		ResultSetMetaData rsmd = toConvert.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		//Começamos a andar pelas linhas, não volto para a primeira pois estou considerando 
		//que o caller pode querer as linhas a partir de determinado ponto.		
		while(toConvert.next()){
			for (int i = 1; i <= columnsNumber; i++) {
				//Começamos a transferência dos dados no result set para a string			
				tempResConvet.append(toConvert.getString(i));
				tempResConvet.append(";");
			}			
			//salvo a cada linha, pois não sei quantas linhas receberei no result set.
			out.println(tempResConvet.toString());
			//limpo a minha string para a proxima linha
			tempResConvet.setLength(0);
		}
		out.close();
		return false;
	}
}
