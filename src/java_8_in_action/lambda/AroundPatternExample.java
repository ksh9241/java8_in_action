package java_8_in_action.lambda;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AroundPatternExample {
	
	public static String processFile() throws IOException {
		
		// Around Pattern을 이용하면 finally에서 close를 따로 처리하지 않아도 된다.
		try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
			return br.readLine();
		}
	}
	
	public static String processFile(BufferedReaderProcesser p) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
			return p.process(br);
		}
	}
	
	public static void main (String [] args) throws IOException {
		String oneLine = processFile( (BufferedReader br) -> br.readLine());
		
		String twoLine = processFile( (BufferedReader br) -> br.readLine() + br.readLine());
		
		
	}
}
