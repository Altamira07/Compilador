package utils;

import java.util.HashMap;
import java.util.Map;

public class PilaErrores {

	private static Nodo principal;
	private static Map<Integer,String> errores = new HashMap<>();
	static {
		errores.put(100,"No se esperaba caracter: ");
		errores.put(200,"Se esperaba operador aritmetico | booleano");
		errores.put(201,"Se esperaba & o |");
		errores.put(202,"Se esperaba INT | ID");
		errores.put(203,"Se esperaba INT | ID | = ");
		errores.put(204,"Se esperaba =");

	}

	public PilaErrores()
	{
		principal = null;
	}

	
	public static String pop() {
		String error = "";
		if(!vacia())
		{
			error = principal.error;
			principal = principal.sig;
		}
		return error;
	}
	
	
	public static void push(int numError,String caracteres,int linea,int posicion) {
		Nodo nuevo;
		String error =  numError +" "+  errores.get(numError)+ caracteres + " posicion: "+posicion + " linea: "+linea;
		if( vacia() )
			principal = new Nodo(error);
		else {
			nuevo = new Nodo(error);
			nuevo.sig = principal;
			principal = nuevo;
		}
	}
	public static void limpiar() {
		principal = null;
	}
	public static String getErrors()
	{
		String errores = "";
		while (!vacia())
			errores+=pop()+"\n";
		return errores;
	}

	public static boolean vacia()
	{
		return (principal == null)? true:false;
	}
	 static class Nodo
	 {
		 private String error;
		 Nodo sig;
		 public Nodo()
		 {
			 sig = null;
			 error = "";
		 }
		 public Nodo(String error)
		 {
			 this.error = error;
			 sig = null;
		 }
	 }
}
