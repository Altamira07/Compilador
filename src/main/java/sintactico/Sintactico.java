package sintactico;

import lexico.Lexico;
import semantico.Registro;
import semantico.TablaSemantica;
import utils.PilaErrores;
import utils.TablaSimbolos;
import utils.models.Etiquetas;
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
        init();;
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
                control();
            }
        }
    }

    public void control()
    {
        Token token = actual();
        if(token !=null && (token.getEtiqueta() ==Etiquetas.WHILE || token.getEtiqueta() ==Etiquetas.IF))
        {
            expBooleana();
            //token = actual();
        }
    }


    public void asignacion()
    {
        Token token = actual();
        Registro r;
        if(token !=null && (token.getEtiqueta() == Etiquetas.STRING || token.getEtiqueta() == Etiquetas.INT))
        {
            r = new Registro();
            r.setTipo(token);
            token = siguiente();
            if(token !=null && token.getEtiqueta() == Etiquetas.IDENTIFICADOR)
            {
                r.setIdentificador(token);
                TablaSemantica.insertar(r);
                token = siguiente();
                if(token !=null && token.getEtiqueta() == Etiquetas.ASIGNACION)
                {
                    expAritmetias();
                }else if(token !=null && token.getEtiqueta() != Etiquetas.PUNTO_COMA)
                    PilaErrores.pushErrorSintactico(213,token.getLinea(),i);
            }else PilaErrores.pushErrorSintactico(207,token.getLinea(),i);
        } else if(token !=null && token.getEtiqueta() == Etiquetas.IDENTIFICADOR)
        {
            r = new Registro();
            r.setIdentificador(token);
            TablaSemantica.insertar(r);
            token = siguiente();
            if(token !=null && token.getEtiqueta() == Etiquetas.ASIGNACION){
                expAritmetias();
            }else PilaErrores.pushErrorSintactico(206,token.getLinea(),i);
        }
    }




    public void expAritmetias()
    {
        Token token;
        Stack<Integer> pos = new Stack<>();
        EstadoSintactico actual = Q1;
        do
        {
            token = siguiente();
            if(token !=null && token.getEtiqueta() == Etiquetas.ABRE_PARENTESIS)
            {
                pila.push(token);
                pos.push(i);
                continue;
            }
            if(token !=null && token.getEtiqueta() == Etiquetas.CIERRA_PARENTESIS && !pila.empty())
            {
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
                        actual = Q2;
                    }else
                        PilaErrores.pushErrorSintactico(211,token.getLinea(),i);
                    break;
                case Q2:
                    if (token !=null && (token.getEtiqueta() == Etiquetas.SUMA || token.getEtiqueta() == Etiquetas.RESTA || token.getEtiqueta() == Etiquetas.MULTIPLICACION || token.getEtiqueta() == Etiquetas.DIVISION ))
                    {
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

    }


    public void expBooleana()
    {
        Token token = siguiente();
        Stack<Integer> pos = new Stack<>();
        EstadoSintactico actual = Q1;
        EstadoSintactico anterior = actual;
        if(token !=null && token.getEtiqueta() == Etiquetas.ABRE_PARENTESIS)
        {
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
                            if(token.getEtiqueta() == Etiquetas.IDENTIFICADOR)
                                TablaSemantica.insertar(new Registro(null,null,token));
                            actual = Q2;
                        }
                        else PilaErrores.pushErrorSintactico(211,token.getLinea(),i);
                        break;
                    case Q2:
                        if(token !=null &&(token.getEtiqueta() == Etiquetas.MENOR || token.getEtiqueta() == Etiquetas.MAYOR) )
                            actual = Q3;
                        else if(token !=null &&(token.getEtiqueta() == Etiquetas.ASIGNACION || token.getEtiqueta() == Etiquetas.DIFERENTE))
                            actual = Q5;
                        else if(token !=null &&token.getEtiqueta() == Etiquetas.AND)
                            actual = Q6;
                        else if(token !=null &&token.getEtiqueta() == Etiquetas.OR)
                            actual = Q7;
                        //else  PilaErrores.pushErrorSintactico(208,token.getLinea(),i);

                        break;
                    case Q3:
                        if(token !=null && (token.getEtiqueta() == Etiquetas.IDENTIFICADOR || token.getEtiqueta() == Etiquetas.VALOR_INT))
                            actual = Q2;
                        else if(token !=null &&token.getEtiqueta() == Etiquetas.ASIGNACION)
                            actual = Q4;
                        else
                            PilaErrores.pushErrorSintactico(211,token.getLinea(),i);
                        break;
                    case Q4:
                        if(token !=null && (token.getEtiqueta() == Etiquetas.IDENTIFICADOR || token.getEtiqueta() == Etiquetas.VALOR_INT))
                            actual = Q2;
                        else
                            PilaErrores.pushErrorSintactico(211,token.getLinea(),i);
                        break;
                    case Q5:
                        if(token !=null &&token.getEtiqueta() == Etiquetas.ASIGNACION)
                            actual = Q4;
                        else PilaErrores.pushErrorSintactico(206,token.getLinea(),i);
                        break;
                    case Q6:
                        if(token !=null &&token.getEtiqueta() == Etiquetas.AND)
                            actual = Q1;
                        else
                            PilaErrores.pushErrorSintactico(210,token.getLinea(),i);
                        break;
                    case Q7:
                        if(token !=null &&token.getEtiqueta() == Etiquetas.OR)
                            actual = Q1;
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
