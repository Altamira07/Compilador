package utils;

import sintactico.EstadoSintactico;

import java.util.HashMap;
import java.util.Map;

public class PilaErrores {

	private static Nodo principal;
	private static Map<Integer,String> errores = new HashMap<>();
	private static Map<EstadoSintactico,String> erroresSintacticos = new HashMap<>();
	static {
		errores.put(100,"No se esperaba caracter: ");
		errores.put(200,"Se esperaba operador aritmetico | booleano");
		errores.put(201,"Se esperaba & o |");
		errores.put(202,"Se esperaba INT | ID");
		errores.put(203,"Se esperaba INT | ID | = ");
		errores.put(204,"Se esperaba =");

		erroresSintacticos.put(EstadoSintactico.Q14,"INIT");
		erroresSintacticos.put(EstadoSintactico.Q15,"(");
		erroresSintacticos.put(EstadoSintactico.Q16,")");
		erroresSintacticos.put(EstadoSintactico.Q17,"{");
		erroresSintacticos.put(EstadoSintactico.Q0,"}");
		erroresSintacticos.put(EstadoSintactico.Q9,"IDENTIFICADOR");
		erroresSintacticos.put(EstadoSintactico.Q11," = | ; ");
		erroresSintacticos.put(EstadoSintactico.Q8,"(");
		erroresSintacticos.put(EstadoSintactico.EXP,"IDENTIFICADOR | INT | \"");
		erroresSintacticos.put(EstadoSintactico.Q10,"VALOR_STRING");
		erroresSintacticos.put(EstadoSintactico.Q12,"\"");
		erroresSintacticos.put(EstadoSintactico.Q2,"INT | IDENTIFICADOR");
		erroresSintacticos.put(EstadoSintactico.Q3,"INT | IDENTIFICADOR");
		erroresSintacticos.put(EstadoSintactico.Q5,"=");
		erroresSintacticos.put(EstadoSintactico.Q4,"INT | IDENTIFICADOR");
		erroresSintacticos.put(EstadoSintactico.Q1,")");




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
	public static void pushErrorSintactico(EstadoSintactico estado,int linea)
	{
		Nodo nuevo;
		String error = "NUMERO" + "Se esperaba "+ erroresSintacticos.get(estado) + " linea" + linea;
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
