package semantico;


import com.sun.corba.se.impl.interceptors.PICurrent;
import lexico.Lexico;
import sintactico.Sintactico;
import utils.PilaErrores;
import utils.models.Token;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.SortedMap;

public class Semantico {

    Registro []registros;
    ArrayList<Token> declarados = new ArrayList<>();

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
        registros = TablaSemantica.toArray();
        System.out.println("_________");
        preAnalisis();
    }


    public void preAnalisis()
    {

        for(int i = 0 ; i< registros.length; i++)
        {
            if(registros[i].getTipo() != null)
                if(!isDeclarado(registros[i].getIdentificador()))
                {
                    declarados.add(registros[i].getIdentificador());
                }else
                    PilaErrores.pushErrrorSemantico(300,registros[i].identificador,registros[i].identificador.getLinea());
            else
                if(!isDeclarado(registros[i].getIdentificador()))
                    PilaErrores.pushErrrorSemantico(301,registros[i].identificador,registros[i].identificador.getLinea());

        }
    }

    public boolean isDeclarado(Token t)
    {
        for(Token token:declarados)
            if(t.getId() == token.getId())
                return true;
        return false;
    }

}
