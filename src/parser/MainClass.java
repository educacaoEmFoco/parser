package parser;

import java.io.IOException;

public class MainClass {

	public static void main(String[] args) {
		Parser p = new Parser();
		try {
			p.parseAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
