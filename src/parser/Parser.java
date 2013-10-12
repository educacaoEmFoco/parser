package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

public class Parser {

	private static final String SPACE_SYMBOL = " ";
	private static final String UNDERLINE_SYMBOL = "_";
	private static final String STD_CSV_EXTENSION = ".csv";
	private static final String STD_CSV_DELIMITATOR = ";";
	final static private String dirPath = "resources/";
	final static private String outPath = "informacoes/";
	final static private String stdExtension = ".txt";

	final static private int NOME = 0;
	final static private int NOME_SEM_ACENTO = 1;
	final static private int SIGLA = 2;
	final static private String estados[][] = { { "Acre", "Acre", "AC" },
			{ "Alagoas", "Alagoas", "AL" }, { "Amapá", "Amapa", "AP" },
			{ "Amazonas", "Amazonas", "AM" }, { "Bahia", "Bahia", "BA" },
			{ "Ceará", "Ceara", "CE" },
			{ "Distrito Federal", "Distrito Federal", "DF" },
			{ "Espírito Santo", "Espirito Santo", "ES" },
			{ "Goiás", "Goias", "GO" }, { "Maranhão", "Maranhao", "MA" },
			{ "Mato Grosso", "Mato Grosso", "MT" },
			{ "Mato Grosso do Sul", "Mato Grosso do Sul", "MS" },
			{ "Minas Gerais", "Minas Gerais", "MG" }, { "Pará", "Para", "PA" },
			{ "Paraíba", "Paraiba", "PB" }, { "Paraná", "Parana", "PR" },
			{ "Pernambuco", "Pernambuco", "PE" }, { "Piauí", "Piaui", "PI" },
			{ "Rio de Janeiro", "Rio de Janeiro", "RJ" },
			{ "Rio Grande do Norte", "Rio Grande do Norte", "RN" },
			{ "Rio Grande do Sul", "Rio Grande do Sul", "RS" },
			{ "Rondônia", "Rondonia", "RO" }, { "Roraima", "Roraima", "RR" },
			{ "Santa Catarina", "Santa Catarina", "SC" },
			{ "São Paulo", "Sao Paulo", "SP" }, { "Sergipe", "Sergipe", "SE" },
			{ "Tocantins", "Tocantins", "TO" } };

	private String headers[];
	private HashMap<String, String[]> mapaDeInformacoes = new HashMap<String, String[]>();
	private String indicador;

	void parseAll() throws IOException {
		criarArquivosParaEstados();

		File parentDirList[] = listarDiretorios(dirPath);

		for (int i = 0; i < parentDirList.length; i++) {
			if (!parentDirList[i].isDirectory())
				continue;

			parseAllCSVFiles(listarDiretorios(parentDirList[i].toString()));
		}
	}

	private void parseAllCSVFiles(File[] arquivos) throws IOException {
		for (int i = 0; i < arquivos.length; i++) {
			indicador = formatIndicador(arquivos[i].getName());
			inicializarMapaDeInformacoes();
			parseCSVFile(arquivos[i]);
			ataulizarEstados();
		}

	}

	private void parseCSVFile(File file) throws IOException {
		FileReader reader = new FileReader(file);
		BufferedReader buff = new BufferedReader(reader);

		buff.readLine();
		headers = buff.readLine().split(STD_CSV_DELIMITATOR);

		int ufPos = 0;

		for (int i = 0; i < estados.length && buff.ready();) {
			String line[] = buff.readLine().split(STD_CSV_DELIMITATOR);
			line[ufPos] = estadoValido(line[ufPos]);
			if (line[ufPos] != "") {
				mapaDeInformacoes.put(line[ufPos], line);
				i++;
			}
		}
		buff.close();
		reader.close();
	}

	private void ataulizarEstados() throws IOException {
		Iterator<String> it = mapaDeInformacoes.keySet().iterator();

		while (it.hasNext()) {
			String key = it.next();
			File estado = new File(outPath + key + stdExtension);
			FileWriter writer = new FileWriter(estado, true);
			PrintWriter print = new PrintWriter(writer);

			print.println(indicador);

			String valores[] = mapaDeInformacoes.get(key);
			for (int i = 1; i < headers.length; i++) {
				if(valores[i].equalsIgnoreCase("-"))
					print.print(headers[i] + ": " + 0);
				else
					print.print(headers[i] + ": " + valores[i]);
				
				print.println();
			}

			print.println();
			print.println();
			print.flush();
			print.close();
			writer.close();
		}
	}

	private boolean criarArquivosParaEstados() throws IOException {
		for (int i = 0; i < estados.length; i++) {
			File estado = new File(outPath + estados[i][NOME] + stdExtension);

			estado.createNewFile();
			if (!estado.exists())
				return false;

			FileWriter writer = new FileWriter(estado, false);
			PrintWriter print = new PrintWriter(writer);

			print.println(estados[i][NOME]);
			print.println(estados[i][SIGLA]);

			print.println();

			print.flush();
			print.close();
			writer.close();
		}
		return true;
	}

	private String estadoValido(String estado) {
		for (int i = 0; i < estados.length; i++) {
			for (int j = 0; j < estados[i].length; j++) {
				if (estado.equalsIgnoreCase(estados[i][j])) {
					return estados[i][NOME];
				}
			}
		}
		return "";
	}

	private String formatIndicador(String name) {
		String formated = name.split(STD_CSV_EXTENSION)[0];
		formated.replaceAll(UNDERLINE_SYMBOL, SPACE_SYMBOL);
		return formated;
	}

	private void inicializarMapaDeInformacoes() {
		mapaDeInformacoes.clear();
	}

	private File[] listarDiretorios(String path) {
		File dir = new File(path);
		File dirList[] = dir.listFiles();

		return dirList;
	}
}
