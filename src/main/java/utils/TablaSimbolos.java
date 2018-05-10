package utils;

import lexico.Estados;
import utils.models.Etiquetas;
import utils.models.Identificador;
import utils.models.Token;

import javax.swing.table.DefaultTableModel;

public class TablaSimbolos {

	private static Nodo raiz;
	private static Nodo fin;
	private static Nodo rec;
	private DefaultTableModel modelo;
	private static int contador = 0;
	public TablaSimbolos() {
		raiz = fin = null;
	}
	
	public static void insertar(Token token) {
		Nodo nuevo = new Nodo(token);
		contador++;
		if (vacia()) {
			raiz = fin = nuevo;
		} else {
			fin.sig = nuevo;
			fin = nuevo;
		}
	}

	public static int search(String lexema)
	{
		Token tokens[] = toArray();
		System.out.println(lexema);
		for(Token t: tokens)
		{
			if(t.getIdentificador().equals(lexema))
				return  t.getId();
		}
		return -1;
	}



	public static void main(String args[])
	{
		insertar(new Identificador(Etiquetas.IDENTIFICADOR,10,10,"$Hola"));
		insertar(new Identificador(Etiquetas.IDENTIFICADOR,10,11,"$Hola2"));
		insertar(new Identificador(Etiquetas.IDENTIFICADOR,10,12,"$Hola3"));
		insertar(new Identificador(Etiquetas.IDENTIFICADOR,10,12,"$Hola4"));

		insertar(new Identificador(Etiquetas.VALOR_STRING,10,13,"\"Hola mundo\""));
		insertar(new Identificador(Etiquetas.IDENTIFICADOR,10,14,"\"Hola mundo\""));


		int id = search("$Hola4");
		System.out.println("Econtre: "+id);
		insertar(new Identificador(Etiquetas.IDENTIFICADOR,10,12,"$Hola4"));

	}


	public static Token []toArray()
    {
        Token []array = new Token[contador];
        int i = 0 ;
        Nodo rec = raiz;
		while (rec != null)
        {
		    array[i] = rec.token;
			i++;
            rec = rec.sig;
        }
        return  array;
    }

	public static void limpiar() {
		contador = 0;
		raiz = fin = rec = null;
	}

	public static boolean vacia() {
		return (raiz == null) ? true : false;
	}


	public static void recorrerTabla()
	{
		Nodo rec = raiz;
		while (rec != null)
		{

			rec = rec.sig;
		}

	}
	

	public void setModelo(DefaultTableModel modelo) {
		if(this.modelo == null)
			this.modelo = modelo;
	}
	private static class  Nodo {
		Token token;
		Nodo sig;
		public Nodo(Token token) {
			sig = null;
			this.token = token;
		}

	}
}
