package utils;

import lexico.Estados;
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
