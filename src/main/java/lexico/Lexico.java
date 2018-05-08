package lexico;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import utils.PilaErrores;
import utils.TablaSimbolos;

public class Lexico {
	private RandomAccessFile archivo;
	private int posicion = 0, linea = 1,posicionToken = 0, ID = 0,VALOR_STRING = 0, VALOR_INT;
	

	static private LinkedHashMap<Estados, Map<Character, Estados>> automata = new LinkedHashMap<Estados, Map<Character, Estados>>();
	static {
		Map<Character, Estados> transicion = new HashMap<Character, Estados>();
		// De q0
		transicion.put('i', Estados.Q1);
		transicion.put('S', Estados.Q13);
		transicion.put('w', Estados.Q8);
		transicion.put('e', Estados.Q20);
		transicion.put('p', Estados.Q23);
		transicion.put('r', Estados.Q27);

		transicion.put('+', Estados.SUMA);
		transicion.put('-', Estados.RESTA);
		transicion.put('*', Estados.MULTIPLICACION);
		transicion.put('/', Estados.DIVISION);

		transicion.put('>', Estados.MAYOR);
		transicion.put('<', Estados.MENOR);
		transicion.put('=', Estados.IGUAL);
		
		transicion.put('&', Estados.AND);
		transicion.put('|', Estados.OR);
		transicion.put('{', Estados.ABRE_LLAVE);
		transicion.put('}', Estados.CIERRA_LLAVE);
		transicion.put('(', Estados.ABRE_PARENTESIS);
		transicion.put(')', Estados.CIERRA_PARENTESIS);
		transicion.put(';', Estados.PUNTO_COMA);
		transicion.put('"', Estados.COMILLAS);


		automata.put(Estados.Q0, transicion);
		transicion = null;
		// De q1
		transicion = new HashMap<>();
		transicion.put('n', Estados.Q2);
		transicion.put('f', Estados.IF);
		automata.put(Estados.Q1, transicion);
		transicion = null;
		// De Q2
		transicion = new HashMap<>();
		transicion.put('t', Estados.Q3);
		transicion.put('i', Estados.Q6);
		automata.put(Estados.Q2, transicion);
		transicion = null;
		// De Q6
		transicion = new HashMap<>();
		transicion.put('t', Estados.INIT);
		automata.put(Estados.Q6, transicion);
		transicion = null;
		// De Q3
		transicion = new HashMap<>();
		transicion.put(' ', Estados.INT);
		automata.put(Estados.Q3, transicion);
		transicion = null;
		// De q13
		transicion = new HashMap<>();
		transicion.put('t', Estados.Q14);
		automata.put(Estados.Q13, transicion);
		transicion = null;
		// De q14
		transicion = new HashMap<>();
		transicion.put('r', Estados.Q15);
		automata.put(Estados.Q14, transicion);
		transicion = null;

		// De Q15
		transicion = new HashMap<>();
		transicion.put('i', Estados.Q16);
		automata.put(Estados.Q15, transicion);
		transicion = null;
		// De Q16
		transicion = new HashMap<>();
		transicion.put('n', Estados.Q17);
		automata.put(Estados.Q16, transicion);
		transicion = null;
		// De Q17
		transicion = new HashMap<>();
		transicion.put('g', Estados.Q18);
		automata.put(Estados.Q17, transicion);
		transicion = null;
		// De Q18
		transicion = new HashMap<>();
		transicion.put(' ', Estados.STRING);
		automata.put(Estados.Q18, transicion);
		transicion = null;
		// De Q8
		transicion = new HashMap<>();
		transicion.put('h', Estados.Q9);
		automata.put(Estados.Q8, transicion);
		transicion = null;

		// De Q9
		transicion = new HashMap<>();
		transicion.put('i', Estados.Q10);
		automata.put(Estados.Q9, transicion);
		transicion = null;

		// De Q10
		transicion = new HashMap<>();
		transicion.put('l', Estados.Q11);
		automata.put(Estados.Q10, transicion);
		transicion = null;

		// De Q11
		transicion = new HashMap<>();
		transicion.put('e', Estados.WHILE);
		automata.put(Estados.Q11, transicion);
		transicion = null;

		// De Q20
		transicion = new HashMap<>();
		transicion.put('l', Estados.Q21);
		automata.put(Estados.Q20, transicion);
		transicion = null;

		// De Q21
		transicion = new HashMap<>();
		transicion.put('s', Estados.Q22);
		automata.put(Estados.Q21, transicion);
		transicion = null;

		// De Q22
		transicion = new HashMap<>();
		transicion.put('e', Estados.ELSE);
		automata.put(Estados.Q22, transicion);
		transicion = null;
		// De Q23
		transicion = new HashMap<>();
		transicion.put('r', Estados.Q24);
		automata.put(Estados.Q23, transicion);
		transicion = null;
		// De Q24
		transicion = new HashMap<>();
		transicion.put('i', Estados.Q25);
		automata.put(Estados.Q24, transicion);
		transicion = null;
		// De Q25
		transicion = new HashMap<>();
		transicion.put('n', Estados.Q26);
		automata.put(Estados.Q25, transicion);
		transicion = null;
		// De Q26
		transicion = new HashMap<>();
		transicion.put('t', Estados.PRINT);
		automata.put(Estados.Q26, transicion);
		transicion = null;

		// De Q27
		transicion = new HashMap<>();
		transicion.put('e', Estados.Q28);
		automata.put(Estados.Q27, transicion);
		transicion = null;
		// De Q28
		transicion = new HashMap<>();
		transicion.put('a', Estados.Q29);
		automata.put(Estados.Q28, transicion);
		transicion = null;
	
		// De Q29
		transicion = new HashMap<>();
		transicion.put('d', Estados.READ);
		automata.put(Estados.Q29, transicion);
		transicion = null;
	
	}

	public Lexico(RandomAccessFile archivo) {
		this.archivo = archivo;
		try{
		    this.analizar();
        }catch (Exception ex){}
	}

	public static void main(String args[])
	{
		try {
			File archivo = new File("palabras");
			RandomAccessFile raf = new RandomAccessFile(archivo,"r");
			new Lexico(raf);
			System.out.println(PilaErrores.getErrors());
		}catch (Exception ex){
			System.out.println(ex);
		}
	}

	public void analizar() throws IOException {
		char caracter;
		Estados actual = Estados.Q0;
		Map<Character,Estados> tranciciones;
		StringBuilder sb = new StringBuilder();
		do {
		    caracter = obtenerCaracter();
			if (caracter == '#') {
				do {
					caracter = obtenerCaracter();
				} while (caracter != '\n');
				continue;
			}

			if(caracter == '$'){
				do{
					sb.append(caracter);
					caracter = obtenerCaracter();
				}while (Character.isLetter(caracter));
				posicionToken ++;
				Estados identificador = Estados.IDENTIFICADOR;
				identificador.setLinea(linea);
				identificador.setPocision(posicionToken);
				identificador.setLexema(sb.toString());
				TablaSimbolos.insertar(identificador);
				sb = new StringBuilder();
			}
			if (caracter == '"') {
				TablaSimbolos.insertar(Estados.COMILLAS);
				caracter = obtenerCaracter();
				do {
					sb.append(caracter);
					caracter = obtenerCaracter();
				} while (caracter != '"' && caracter != '\u0000');
				Estados valorString = Estados.VALOR_STRING;
				valorString.setLinea(linea);
				posicionToken++;
				valorString.setPocision(posicionToken);
				valorString.setLexema(sb.toString());
				TablaSimbolos.insertar(valorString);
				if(caracter != '\u0000')
					TablaSimbolos.insertar(Estados.COMILLAS);
				else
					System.out.println("Se esperaba cierre");
				sb = new StringBuilder();
				continue;
			}
			if (Character.isDigit(caracter)) {
				do {
					sb.append(caracter);
					caracter = obtenerCaracter();
				} while (Character.isDigit(caracter));
				Estados numero = Estados.VALOR_INT;
				numero.setLexema(sb.toString());
				numero.setLinea(linea);
				posicionToken++;
				numero.setPocision(posicionToken);
				TablaSimbolos.insertar(numero);
				sb = new StringBuilder();
			}


			sb.append(caracter);

			tranciciones = automata.get(actual);
			actual = tranciciones.get(caracter);

			if (actual == null && Character.isWhitespace(caracter)) {
				actual = Estados.Q0;
				continue;
			}

			if(actual != null && actual.isFinal())
			{
				sb = new StringBuilder();
				posicionToken++;
				actual.setPocision(posicionToken);
				actual.setLinea(linea);
				TablaSimbolos.insertar(actual);
				actual = Estados.Q0;
				continue;
			}
			if(actual == null&& caracter != '\u0000')
			{
				PilaErrores.push(100,caracter+"",linea,posicion);
				actual = Estados.Q0;
			}
		} while (caracter != '\u0000');

	}

	public char obtenerCaracter() throws IOException {
		char caracter;
		if (archivo != null && archivo.getFilePointer() < archivo.length()) {
			caracter = (char) archivo.readByte();
			posicion++;
			if (caracter == '\n') {
				linea++;
				posicion = 1;
				posicionToken = 0;
				return obtenerCaracter();
			}
			return caracter;
		} else if (archivo != null && archivo.getFilePointer() == archivo.length()) {
			archivo.close();
			archivo = null;
		}
		return '\u0000';
	}

	
}
