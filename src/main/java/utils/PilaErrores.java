package utils;

import sintactico.EstadoSintactico;
import utils.models.Token;

import java.util.HashMap;
import java.util.Map;

public class PilaErrores {

	private static Nodo principal;
	private static Map<Integer,String> errores = new HashMap<>();
	static {
		errores.put(100,"No se esperaba caracter: ");
		errores.put(200,"(");
		errores.put(201,")");
		errores.put(202,"{ de }");
		errores.put(203,"} de {");
		errores.put(204,";");

		errores.put(213,"; | =");
		errores.put(205,"VALOR");

		errores.put(206,"=");
		errores.put(207,"IDENTIFICADOR");

		errores.put(208,"<= | >= | == | < | > | != ");
		errores.put(209,"+ | - | * | /");
		errores.put(210,"&");
		errores.put(212,"|");

		errores.put(214,"INIT");
		errores.put(211,"IDENTIFICADOR | VALOR ");

		errores.put(300,"Ya habia sido declarado");
		errores.put(301,"No ha sido declarado");


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

	public static  void pushErrrorSemantico(int id,Token i,int linea)
	{
		Nodo nuevo;
		String error = "id: " + id + " Se esperaba: "+errores.get(id)+ " "+ i.getIdentificador()+" linea: "+linea;
		if( vacia() )
			principal = new Nodo(error);
		else {
			nuevo = new Nodo(error);
			nuevo.sig = principal;
			principal = nuevo;
		}
	}

	public static void pushErrorSintactico(int id,int linea,int posicion)
	{
		Nodo nuevo;
		String error = "id: " + id + " Se esperaba: "+errores.get(id)+ " linea: "+linea;
		if( vacia() )
			principal = new Nodo(error);
		else {
			nuevo = new Nodo(error);
			nuevo.sig = principal;
			principal = nuevo;
		}
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
