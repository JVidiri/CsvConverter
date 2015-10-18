package hto.edu.ifsp.csvconverter.tests;

import java.io.IOException;
import java.sql.SQLException;

import com.mockrunner.mock.jdbc.MockResultSet;

import hto.edu.ifsp.csvconverter.CsvConverter;

public class TestCsvConverter {

	private static boolean test1() throws Exception {
		MockResultSet rs = new MockResultSet(null);		
		rs.addRow(new Object[]{Integer.valueOf(4),"Sous Theme de Test","Vos publications peuvent se retrouver ici","2008/05/10","7","/0/3/",Integer.valueOf(3),Integer.valueOf(3),"","Visible","","default",Integer.valueOf(1),null,Integer.valueOf(-1)});
		rs.addRow(new Object[]{Integer.valueOf(5),"Sous Theme de Test","Vos publications peuvent se retrouver ici2","2012/05/10","7","/0/3/",Integer.valueOf(5),Integer.valueOf(12),"","Invisible","","default",Integer.valueOf(5),null,Integer.valueOf(-10)});		
		CsvConverter test = new CsvConverter();
		test.resultSetToCsv(rs, "/home/jvidiri/teste.txt");
		//TODO adicionar leitura do arquivo.
		return true;
	}
	
	public static void main(String[] args) throws Exception {	
		if (TestCsvConverter.test1()){
			System.out.println("sucesso!");
			return;
		}		
		System.out.println("Erro!");
	}	
}
