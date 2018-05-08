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
import java.util.*;

public class Sintactico {
    private static LinkedHashMap<EstadoSintactico, Map<Estados,EstadoSintactico>> automata = new LinkedHashMap<>();
    Estados []tokens;
    Stack<Estados> pila = new Stack<>();
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
        trancicion = new HashMap<>();
        trancicion.put(Estados.INIT,EstadoSintactico.Q15);
        automata.put(EstadoSintactico.Q14,trancicion);

        trancicion = new HashMap<>();
        trancicion.put(Estados.ABRE_PARENTESIS,EstadoSintactico.Q16);
        automata.put(EstadoSintactico.Q15,trancicion);

        trancicion = new HashMap<>();
        trancicion.put(Estados.CIERRA_PARENTESIS,EstadoSintactico.Q17);
        automata.put(EstadoSintactico.Q16,trancicion);


        trancicion = new HashMap<>();
        trancicion.put(Estados.ABRE_LLAVE,EstadoSintactico.Q0);
        automata.put(EstadoSintactico.Q17,trancicion);

        //Estado inicial y sus tranciciones
        trancicion = new HashMap<>();
        trancicion.put(Estados.WHILE,EstadoSintactico.Q8);
        trancicion.put(Estados.IF,EstadoSintactico.Q8);
        trancicion.put(Estados.STRING,EstadoSintactico.Q9);
        trancicion.put(Estados.INT,EstadoSintactico.Q9);
        trancicion.put(Estados.IDENTIFICADOR,EstadoSintactico.Q11);
        trancicion.put(Estados.CIERRA_LLAVE,EstadoSintactico.Q13);
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
        trancicion.put(Estados.COMILLAS,EstadoSintactico.Q10);
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

        trancicion.put(Estados.ABRE_LLAVE,EstadoSintactico.Q17);

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

        trancicion = new HashMap<>();
        trancicion.put(Estados.VALOR_STRING,EstadoSintactico.Q12);
        automata.put(EstadoSintactico.Q10,trancicion);

        trancicion = new HashMap<>();
        trancicion.put(Estados.COMILLAS,EstadoSintactico.Q1);
        automata.put(EstadoSintactico.Q12,trancicion);


    }

    public void analizar()
    {
        EstadoSintactico actual;
        Map<Estados,EstadoSintactico> trancicion;
        Map<Estados,EstadoSintactico> trancicionSiguientes;
        EstadoSintactico anterior;
        Estados token;
        int i = 0;
        boolean b = (tokens[0] == Estados.INIT && tokens[1]== Estados.ABRE_PARENTESIS && tokens[2] == Estados.CIERRA_PARENTESIS && tokens[3] == Estados.ABRE_LLAVE );
        if(b)
        {
            pila.push(tokens[3]);
            actual = EstadoSintactico.Q0;
            trancicion = automata.get(actual);
            for(i = 4; i<tokens.length && !pila.empty();i++)
            {
                token = tokens[i];
                if(token == Estados.CIERRA_LLAVE && !pila.empty())
                {
                    pila.pop();
                    trancicion = automata.get(EstadoSintactico.Q0);
                }
                if(token == Estados.ABRE_LLAVE )
                {
                    pila.push(token);
                    actual = EstadoSintactico.Q0;
                    continue;
                }
                anterior = actual;
                actual = trancicion.get(token);
                if(actual == null)
                {
                    PilaErrores.pushErrorSintactico(anterior,tokens[i-1].getLinea());
                    trancicion = automata.get(EstadoSintactico.Q0);
                    continue;
                }


                trancicion = automata.get(actual);
            }
        }
        while (!pila.empty())
        {
            System.out.println("Falta cerrrar { "+pila.pop().getLinea());
        }
        System.out.println(PilaErrores.getErrors());
    }

}
