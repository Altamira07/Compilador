package sintactico;

import com.sun.deploy.uitoolkit.impl.awt.AWTPluginUIToolkit;
import com.sun.xml.internal.bind.v2.model.core.ID;
import lexico.Estados;
import lexico.Lexico;
import utils.PilaErrores;
import utils.TablaSimbolos;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

public class Sintactico {
    private static LinkedHashMap<EstadoSintactico, Map<Estados,EstadoSintactico>> automata = new LinkedHashMap<>();
    Estados []tokens;
    Stack<Map<Estados,EstadoSintactico>> pila = new Stack<>();
    public Sintactico ()
    {
        tokens = TablaSimbolos.toArray();
        analizar();
    }

    public static void main(String[] arg)
    {
        try {
            File archivo = new File("palabras");
            RandomAccessFile raf = new RandomAccessFile(archivo,"r");
            new Lexico(raf);
            new Sintactico();
            System.out.println(PilaErrores.getErrors());
        }catch (IOException ex){
            System.out.println(ex);
        }
    }
    static {




        Map<Estados,EstadoSintactico> trancicion;
//Estado inicial y sus tranciciones
        trancicion = new HashMap<>();
        trancicion.put(Estados.WHILE,EstadoSintactico.Q8);
        trancicion.put(Estados.IF,EstadoSintactico.Q8);
        trancicion.put(Estados.STRING,EstadoSintactico.Q9);
        trancicion.put(Estados.INT,EstadoSintactico.Q9);
        trancicion.put(Estados.IDENTIFICADOR,EstadoSintactico.Q11);
        automata.put(EstadoSintactico.Q0,trancicion);


        trancicion = new HashMap<>();
        trancicion.put(Estados.IDENTIFICADOR,EstadoSintactico.Q11);
        automata.put(EstadoSintactico.Q9,trancicion);


        trancicion = new HashMap<>();
        trancicion.put(Estados.PUNTO_COMA,EstadoSintactico.Q0);
        trancicion.put(Estados.IGUAL,EstadoSintactico.EXP);
        automata.put(EstadoSintactico.Q11,trancicion);

        trancicion = new HashMap<>();
        trancicion.put(Estados.ABRE_PARENTESIS,EstadoSintactico.EXP);
        automata.put(EstadoSintactico.Q8,trancicion);

        trancicion =new HashMap<>();
        trancicion.put(Estados.IDENTIFICADOR,EstadoSintactico.Q1);
        trancicion.put(Estados.VALOR_INT,EstadoSintactico.Q1);
        automata.put(EstadoSintactico.EXP,trancicion);

        trancicion = new HashMap<>();
        trancicion.put(Estados.SUMA,EstadoSintactico.Q2);
        trancicion.put(Estados.RESTA,EstadoSintactico.Q2);
        trancicion.put(Estados.MULTIPLICACION,EstadoSintactico.Q2);
        trancicion.put(Estados.DIVISION,EstadoSintactico.Q2);

        trancicion.put(Estados.AND,EstadoSintactico.Q6);
        trancicion.put(Estados.OR,EstadoSintactico.Q6);

        trancicion.put(Estados.IGUAL,EstadoSintactico.Q5);

        trancicion.put(Estados.MENOR,EstadoSintactico.Q3);
        trancicion.put(Estados.MAYOR,EstadoSintactico.Q3);


        //Agregamos el estado final de Q1
        trancicion.put(Estados.CIERRA_PARENTESIS,EstadoSintactico.Q0);
        trancicion.put(Estados.PUNTO_COMA,EstadoSintactico.Q0);

        automata.put(EstadoSintactico.Q1,trancicion);

        trancicion = new HashMap<>();
        trancicion.put(Estados.IDENTIFICADOR,EstadoSintactico.Q1);
        trancicion.put(Estados.VALOR_INT,EstadoSintactico.Q1);
        automata.put(EstadoSintactico.Q2,trancicion);

        trancicion = new HashMap<>();
        trancicion.put(Estados.AND,EstadoSintactico.EXP);
        trancicion.put(Estados.OR,EstadoSintactico.EXP);
        automata.put(EstadoSintactico.Q6,trancicion);

        trancicion = new HashMap<>();
        trancicion.put(Estados.VALOR_INT,EstadoSintactico.Q1);
        trancicion.put(Estados.IDENTIFICADOR,EstadoSintactico.Q1);
        trancicion.put(Estados.IGUAL,EstadoSintactico.Q4);
        automata.put(EstadoSintactico.Q3,trancicion);

        trancicion = new HashMap<>();
        trancicion.put(Estados.IGUAL,EstadoSintactico.Q4);
        automata.put(EstadoSintactico.Q5,trancicion);

        trancicion = new HashMap<>();
        trancicion.put(Estados.IDENTIFICADOR,EstadoSintactico.Q1);
        trancicion.put(Estados.VALOR_INT,EstadoSintactico.Q1);
        automata.put(EstadoSintactico.Q4,trancicion);



    }

    public void analizar()
    {
        EstadoSintactico actual = EstadoSintactico.Q0;
        System.out.println("Analizare");
        Map<Estados,EstadoSintactico> trancicion = automata.get(actual);
        Estados tokenActual;
        for(int i = 0 ; i < tokens.length; i++)
        {
            tokenActual = tokens[i];
            System.out.println(tokenActual);
            actual  = trancicion.get(tokenActual);
            //System.out.println("Trancicion actual "+actual.isFinal());
            trancicion = automata.get(actual);

        }
    }





}
