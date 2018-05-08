package utils;

import lexico.Estados;

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
	
	public static void insertar(Estados estado) {
		Nodo nuevo = new Nodo(estado);
		contador++;
		if (vacia()) {
			raiz = fin = nuevo;
		} else {
			fin.sig = nuevo;
			fin = nuevo;
		}
	}


	public static Estados []toArray()
    {
        Estados []array = new Estados[contador];
        int i = 0 ;
        Nodo rec = raiz;
        while (rec != null)
        {
            array[i] = rec.estado;
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


	

	public void setModelo(DefaultTableModel modelo) {
		if(this.modelo == null)
			this.modelo = modelo;
	}
	private static class  Nodo {
		Estados estado;
		Nodo sig;

		public Nodo(Estados estado) {
			sig = null;
			this.estado = estado;
		}

	}
}
