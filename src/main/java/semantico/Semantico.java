package semantico;


import com.sun.corba.se.impl.interceptors.PICurrent;
import lexico.Lexico;
import sintactico.Sintactico;
import utils.PilaErrores;
import utils.models.*;

import javax.jws.soap.SOAPBinding;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.*;

public class Semantico {

    Registro []registros;
    ArrayList<Registro> declarados = new ArrayList<>();
    Map<Token,Token> declaradoComo = new HashMap<>();
    public static void main(String arg[])
    {
        try
        {
            new Lexico(new RandomAccessFile(new File("palabras"),"r"));
            if(PilaErrores.vacia())
            {
                new Sintactico();
                if(PilaErrores.vacia())
                {
                    System.out.println("Hare el semantico");
                    System.out.println("________________________");
                    new Semantico();
                    if(!PilaErrores.vacia())
                        System.out.println(PilaErrores.getErrors());
                }else System.out.println(PilaErrores.getErrors());

            }else System.out.println(PilaErrores.getErrors());

        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public Semantico()
    {
        System.out.println("_________");
        registros = TablaSemantica.toArray();
        analizar();
    }

    public void analizar()
    {
        Registro registro;
        for(int i = 0 ; i<registros.length; i++)
        {
            registro = registros[i];
            if(registro.getTipoDato() != null)
            {
                if(!isDeclarado(registro.getIdentificador()))
                {
                    declarado(registro.getIdentificador(),registro.getTipoDato());
                    Token tipoDato;
                    if(registro.getValores() != null && registro.getValores().size() > 0)
                    {
                        tipoDato = validarAsignacion(registro.getValores());
                        if(registro.getTipoDato().getEtiqueta() == Etiquetas.STRING && tipoDato.getEtiqueta() != Etiquetas.VALOR_STRING)
                        {
                            PilaErrores.pushErrrorSemantico(302,registro.getIdentificador(),registro.getIdentificador().getLinea());
                            continue;
                        }
                        if(registro.getTipoDato().getEtiqueta() == Etiquetas.INT && tipoDato.getEtiqueta() != Etiquetas.VALOR_INT)
                        {
                            PilaErrores.pushErrrorSemantico(303,registro.getIdentificador(),registro.getIdentificador().getLinea());
                            continue;
                        }
                    }
                }
                else
                    PilaErrores.pushErrrorSemantico(300,registro.getIdentificador(),registro.getIdentificador().getLinea());
            }else if(registro.getIdentificador() !=null && !isDeclarado(registro.getIdentificador()))
                PilaErrores.pushErrrorSemantico(301,registro.getIdentificador(),registro.getIdentificador().getLinea());
            else if(registro.getIdentificador() !=null && isDeclarado(registro.getIdentificador())){
                Token tipoDato;
                Token declarado = declaradoComo(registro.getIdentificador());
                if(registro.getValores() != null && registro.getValores().size() > 0)
                {
                    tipoDato = validarAsignacion(registro.getValores());
                    if(declarado.getEtiqueta() == Etiquetas.VALOR_STRING && tipoDato.getEtiqueta() != Etiquetas.VALOR_STRING)
                    {
                        PilaErrores.pushErrrorSemantico(302,registro.getIdentificador(),registro.getIdentificador().getLinea());
                        continue;
                    }
                    if(declarado.getEtiqueta() == Etiquetas.VALOR_INT && tipoDato.getEtiqueta() != Etiquetas.VALOR_INT)
                    {
                        PilaErrores.pushErrrorSemantico(303,registro.getIdentificador(),registro.getIdentificador().getLinea());
                        continue;
                    }
                }
            }else if(registro.getEstructura() != null){
                if(registro.getValores().size()>0) {
                    Token tipoDato = validarAsignacion(registro.getValores());
                    if (tipoDato.getEtiqueta() != Etiquetas.VALOR_STRING)
                    {
                        PilaErrores.pushErrrorSemantico(307,registro.getEstructura(),registro.getEstructura().getLinea());
                    }
                }

            }else

                if(registro.getValores().size()>0)
                    validarExpresionBooleana(registro.getValores());

        }
    }

    Token validarAsignacion(ArrayList<Token> valores)
    {
        Stack<Token> pila = new Stack<>();
        Token valor;
        for(int i = 0 ; i<valores.size(); i++)
        {
            valor = valores.get(i);
            if(valor.getEtiqueta() == Etiquetas.IDENTIFICADOR || valor.getEtiqueta() == Etiquetas.VALOR_INT || valor.getEtiqueta() == Etiquetas.VALOR_STRING)
            {
                if(valor.getEtiqueta() == Etiquetas.IDENTIFICADOR)
                {
                    Token declarado = declaradoComo(valor);
                    if(declarado == null){
                        PilaErrores.pushErrrorSemantico(304,valor,valor.getLinea());
                    }
                    pila.push(declarado);
                    continue;
                }
                pila.push(valor);
            }
            else {
                if(valor.getEtiqueta() == Etiquetas.ABRE_PARENTESIS || valor.getEtiqueta() ==Etiquetas.CIERRA_PARENTESIS)
                    continue;
                valor = valores.get(i+1);
                if(valor.getEtiqueta()==Etiquetas.IDENTIFICADOR)
                {
                    if(!isDeclarado(valor))
                        PilaErrores.pushErrrorSemantico(301,valor,valor.getLinea());
                    else
                        valor = declaradoComo(valor);
                }
                Token temp = pila.pop();
                i++;
                if((temp.getEtiqueta() == Etiquetas.VALOR_STRING && valor.getEtiqueta() == Etiquetas.VALOR_INT)
                        || (temp.getEtiqueta() == Etiquetas.VALOR_INT && valor.getEtiqueta() == Etiquetas.VALOR_STRING)
                        || (temp.getEtiqueta() == Etiquetas.VALOR_STRING && valor.getEtiqueta() == Etiquetas.VALOR_STRING)  )
                    pila.push(new TString(Etiquetas.VALOR_STRING,temp.getLinea(),0,""));
                else if(valor.getEtiqueta() == Etiquetas.VALOR_INT && temp.getEtiqueta() == Etiquetas.VALOR_INT)
                    pila.push(new TInt(Etiquetas.VALOR_INT,valor.getLinea(),0,0));
                else pila.push(temp);
            }
        }

        return pila.pop();
    }

    void validarExpresionBooleana(ArrayList<Token> valores)
    {
        Stack<Token> pila = new Stack<>();
        System.out.println(valores.size());
        for(int i = 0 ; i<valores.size(); i++)
        {
            Token valor = valores.get(i);
            if(valor.getEtiqueta() == Etiquetas.VALOR_INT || valor.getEtiqueta() == Etiquetas.IDENTIFICADOR)
                pila.push(valor);
        }
        while (!pila.empty())
        {
            Token valor = pila.pop();
            if(valor.getEtiqueta() == Etiquetas.IDENTIFICADOR)
            {
                if (!isDeclarado(valor))
                    PilaErrores.pushErrrorSemantico(301,valor,valor.getLinea());
                else if(declaradoComo(valor).getEtiqueta() != Etiquetas.VALOR_INT)
                    PilaErrores.pushErrrorSemantico(305,valor,valor.getLinea());
            }else if(valor.getEtiqueta() != Etiquetas.VALOR_INT)
                    PilaErrores.pushErrrorSemantico(306,valor,valor.getLinea());
        }

    }

    Token declaradoComo(Token identificador)
    {
        for(Registro r:declarados)
            if(r.getIdentificador().getId() == identificador.getId() )
            {
                if(r.getTipoDato().getEtiqueta() == Etiquetas.STRING)
                    return  new TString(Etiquetas.VALOR_STRING,0,0,"");
                else
                    return  new TInt(Etiquetas.VALOR_INT,0,0,0);
            }
        return null;
    }

    public void declarado(Token identificador, Token tipoDato)
    {
        Registro r = new Registro();
        r.setTipoDato(tipoDato);
        r.setIdentificador(identificador);
        declarados.add(r);
    }

    /**
     *
     * @param t
     * @return true if token is declared, false if not
     */
    public boolean isDeclarado(Token t)
    {
        for(Registro r:declarados)
            if(r.getIdentificador().getId() == t.getId() )
                return true;
        return false;
    }

}
