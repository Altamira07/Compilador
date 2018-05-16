package sintactico;


import javafx.scene.control.Tab;
import lexico.Lexico;
import semantico.Registro;
import semantico.TablaSemantica;
import utils.PilaErrores;
import utils.TablaSimbolos;
import utils.models.Etiquetas;
import utils.models.Identificador;
import utils.models.Token;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

import static sintactico.EstadoSintactico.*;

public class Sintactico {
    Token tokens[];
    Stack<Token> pila = new Stack<>();
    Stack<Token> pilaLlaves = new Stack<>();
    int i = -1;


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
            if(PilaErrores.vacia())
                new Sintactico();
            System.out.println(PilaErrores.getErrors());
        }catch (IOException ex){
            System.out.println(ex);
        }
    }

    public void analizar()
    {
        init();
    }


    public void init()
    {
        EstadoSintactico actual = Q1;
        for( i = 0 ; i<4;i++)
        {
            Token t = tokens[i];

            switch (actual)
            {
                case Q1:
                    if(t.getEtiqueta()== Etiquetas.INIT)
                        actual = Q2;
                    else
                        PilaErrores.pushErrorSintactico(214,t.getLinea(),0);
                    break;
                case Q2:
                    if(t.getEtiqueta()== Etiquetas.ABRE_PARENTESIS)
                        actual = Q3;
                    else
                        PilaErrores.pushErrorSintactico(200,t.getLinea(),0);
                    break;
                case Q3:
                    if(t.getEtiqueta()== Etiquetas.CIERRA_PARENTESIS)
                        actual = Q4;
                    else
                        PilaErrores.pushErrorSintactico(201,t.getLinea(),0);
                    break;
                case Q4:
                    if(t.getEtiqueta() == Etiquetas.ABRE_LLAVE)
                        bloque(t);
                    else {
                        PilaErrores.pushErrorSintactico(202,t.getLinea(),0);
                    }
                    break;
            }
        }
        while (!pilaLlaves.empty())
        {
            while (!pilaLlaves.empty())
                PilaErrores.pushErrorSintactico(203,pilaLlaves.pop().getLinea(),0);
        }
    }

    void bloque(Token t){
        pilaLlaves.push(t);
        //System.out.println("Sub bloque" + pilaLlaves.size());
        boolean pop = false;
        Token token;
        while (!pop && i < tokens.length)
        {
            token = siguiente();
            if(token !=null && token.getEtiqueta() == Etiquetas.CIERRA_LLAVE && !pilaLlaves.empty())
            {
                //System.out.println("Saliendo del subloque " + pilaLlaves.size());
                pilaLlaves.pop();
                pop = true;
                continue;
            }else {
                asignacion();
                print();
                control();
            }
        }
    }

    public void control()
    {
        Token token = actual();
        Registro registro;
        if(token !=null && (token.getEtiqueta() ==Etiquetas.WHILE || token.getEtiqueta() ==Etiquetas.IF))
        {
            registro = new Registro();
            registro.setValores(expBooleana());
            TablaSemantica.insertar(registro);
            //token = actual();
        }
    }


    public void asignacion()
    {
        Token token = actual();
        Registro registro;
        if(token !=null && (token.getEtiqueta() == Etiquetas.STRING || token.getEtiqueta() == Etiquetas.INT))
        {
            registro = new Registro();
            registro.setTipoDato(token);
            token = siguiente();
            if(token !=null && token.getEtiqueta() == Etiquetas.IDENTIFICADOR)
            {
                registro.setIdentificador(token);
                token = siguiente();
                if(token !=null && token.getEtiqueta() == Etiquetas.ASIGNACION)
                {
                    registro.setValores(expAritmetias());
                    TablaSemantica.insertar(registro);
                }else if(token !=null && token.getEtiqueta() != Etiquetas.PUNTO_COMA)
                    PilaErrores.pushErrorSintactico(213,token.getLinea(),i);
                else TablaSemantica.insertar(registro);

            }else PilaErrores.pushErrorSintactico(207,token.getLinea(),i);
        }
        else if(token !=null && token.getEtiqueta() == Etiquetas.IDENTIFICADOR)
        {
            registro = new Registro();
            registro.setIdentificador(token);
            token = siguiente();
            if(token !=null && token.getEtiqueta() == Etiquetas.ASIGNACION)
            {

                registro.setValores(expAritmetias());
                TablaSemantica.insertar(registro);
            }else PilaErrores.pushErrorSintactico(206,token.getLinea(),i);
        }
    }

    public void print()
    {

        Token token = actual();
        Registro registro;
        if(token.getEtiqueta() == Etiquetas.PRINT)
        {
            registro = new Registro();
            registro.setEstructura(token);
            token = siguiente();
            if(token.getEtiqueta() == Etiquetas.ABRE_PARENTESIS)
            {
                i--;
                registro.setValores(expAritmetias());
                TablaSemantica.insertar(registro);
            }else PilaErrores.pushErrorSintactico(200,token.getLinea(),0);
        }
    }

    public ArrayList<Token> expAritmetias()
    {
        Token token;
        Stack<Integer> pos = new Stack<>();
        ArrayList<Token> valores = new ArrayList<>();
        EstadoSintactico actual = Q1;
        do
        {
            token = siguiente();
            if(token !=null && token.getEtiqueta() == Etiquetas.ABRE_PARENTESIS)
            {
                valores.add(token);
                pila.push(token);
                pos.push(i);
                continue;
            }
            if(token !=null && token.getEtiqueta() == Etiquetas.CIERRA_PARENTESIS && !pila.empty())
            {
                valores.add(token);
                pila.pop();
                continue;
            }else if(token !=null && token.getEtiqueta() == Etiquetas.CIERRA_PARENTESIS){

                PilaErrores.pushErrorSintactico(200,token.getLinea(),i);
            }
            switch (actual)
            {
                case Q1:
                    if(token !=null && (token.getEtiqueta() == Etiquetas.IDENTIFICADOR || token.getEtiqueta() == Etiquetas.VALOR_INT || token.getEtiqueta() == Etiquetas.VALOR_STRING  ))
                    {
                        valores.add(token);
                        actual = Q2;
                    }else
                        PilaErrores.pushErrorSintactico(211,token.getLinea(),i);
                    break;
                case Q2:
                    if (token !=null && (token.getEtiqueta() == Etiquetas.SUMA || token.getEtiqueta() == Etiquetas.RESTA || token.getEtiqueta() == Etiquetas.MULTIPLICACION || token.getEtiqueta() == Etiquetas.DIVISION ))
                    {
                        valores.add(token);
                        actual = Q1;
                    }else if(token !=null && token.getEtiqueta() == Etiquetas.PUNTO_COMA) continue;
                    break;
            }
        }while (token !=null && token.getEtiqueta() != Etiquetas.PUNTO_COMA  && i < tokens.length);

        if(!pila.empty())
        {
            while (!pila.empty())
            {
                token  = pila.pop();
                PilaErrores.pushErrorSintactico(201,token.getLinea(),pos.pop());
            }
        }

        if(token !=null && token.getEtiqueta() != Etiquetas.PUNTO_COMA)
        {
            PilaErrores.pushErrorSintactico(204,tokens[i-1].getLinea(),i);
        }
        return valores;
    }

    public ArrayList<Token> expBooleana()
    {
        Token token = siguiente();
        ArrayList<Token> valores = new ArrayList<>();
        Stack<Integer> pos = new Stack<>();
        EstadoSintactico actual = Q1;
        EstadoSintactico anterior = actual;
        if(token !=null && token.getEtiqueta() == Etiquetas.ABRE_PARENTESIS)
        {
            valores.add(token);
            pos.push(i);
            pila.push(token);
            do{
                token = siguiente();
                if(token !=null && token.getEtiqueta() == Etiquetas.ABRE_PARENTESIS)
                {
                    pila.push(token);
                    pos.push(i);
                    continue;
                }
                if(token !=null && token.getEtiqueta() == Etiquetas.CIERRA_PARENTESIS && !pila.empty())
                {
                    valores.add(token);
                    pila.pop();
                    continue;
                }else if(token !=null && token.getEtiqueta() == Etiquetas.CIERRA_PARENTESIS){

                  //  PilaErrores.pushErrorSintactico(200,token.getLinea(),i);
                }
                //anterior = actual;
                switch (actual)
                {
                    case Q1:
                        if(token !=null && (token.getEtiqueta() == Etiquetas.IDENTIFICADOR || token.getEtiqueta() == Etiquetas.VALOR_INT))
                        {
                            valores.add(token);
                            actual = Q2;
                        }
                        else PilaErrores.pushErrorSintactico(211,token.getLinea(),i);
                        break;
                    case Q2:
                        if(token !=null &&(token.getEtiqueta() == Etiquetas.MENOR || token.getEtiqueta() == Etiquetas.MAYOR) )
                        {
                            valores.add(token);
                            actual = Q3;
                        }
                        else if(token !=null &&(token.getEtiqueta() == Etiquetas.ASIGNACION || token.getEtiqueta() == Etiquetas.DIFERENTE))
                        {
                            valores.add(token);
                            actual = Q5;
                        }
                        else if(token !=null &&token.getEtiqueta() == Etiquetas.AND)
                        {
                            valores.add(token);
                            actual = Q6;
                        }
                        else if(token !=null &&token.getEtiqueta() == Etiquetas.OR)
                        {
                            valores.add(token);
                            actual = Q7;
                        }
                        //else  PilaErrores.pushErrorSintactico(208,token.getLinea(),i);
                        break;
                    case Q3:
                        if(token !=null && (token.getEtiqueta() == Etiquetas.IDENTIFICADOR || token.getEtiqueta() == Etiquetas.VALOR_INT))
                        {
                            valores.add(token);
                            actual = Q2;
                        }
                        else if(token !=null &&token.getEtiqueta() == Etiquetas.ASIGNACION) {
                            actual = Q4;
                            valores.add(token);
                        }
                        else {
                            PilaErrores.pushErrorSintactico(211,token.getLinea(),i);
                        }
                        break;
                    case Q4:
                        if(token !=null && (token.getEtiqueta() == Etiquetas.IDENTIFICADOR || token.getEtiqueta() == Etiquetas.VALOR_INT)) {
                            actual = Q2;
                            valores.add(token);
                        }
                        else {
                            PilaErrores.pushErrorSintactico(211,token.getLinea(),i);
                        }
                        break;
                    case Q5:
                        if(token !=null &&token.getEtiqueta() == Etiquetas.ASIGNACION) {
                            actual = Q4;
                            valores.add(token);
                        }
                        else PilaErrores.pushErrorSintactico(206,token.getLinea(),i);
                        break;
                    case Q6:
                        if(token !=null &&token.getEtiqueta() == Etiquetas.AND)
                        {
                            valores.add(token);
                            actual = Q1;
                        }
                        else
                            PilaErrores.pushErrorSintactico(210,token.getLinea(),i);
                        break;
                    case Q7:
                        if(token !=null &&token.getEtiqueta() == Etiquetas.OR)
                        {
                            valores.add(token);
                            actual = Q1;
                        }
                        else
                            PilaErrores.pushErrorSintactico(212,token.getLinea(),i);
                        break;

                }

            }while (token !=null &&token.getEtiqueta() != Etiquetas.ABRE_LLAVE && i < tokens.length);
            if(i == tokens.length)
                anterior();
            else if(token !=null &&token.getEtiqueta() == Etiquetas.ABRE_LLAVE )
            {
                //System.out.println("Bloque de un control");
                bloque(token);
            }
        }else
        {
            PilaErrores.pushErrorSintactico(200,token.getLinea(),i);
        }
        if(!pila.empty())
        {
            while (!pila.empty())
            {
                token  = pila.pop();
                PilaErrores.pushErrorSintactico(201,token.getLinea(),pos.pop());
            }
        }
        return valores;
    }

    Token siguiente()
    {
        i++;
        return (i < tokens.length)?tokens[i]:null;
    }
    Token anterior()
    {
        i--;
        return  tokens[i];
    }
    Token actual()
    {
        return (i < tokens.length)?tokens[i]:null;
    }

    Token consumir(int i)
    {
        return tokens[i];
    }
}
