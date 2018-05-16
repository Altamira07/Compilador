package semantico;


import com.sun.corba.se.impl.interceptors.PICurrent;
import lexico.Lexico;
import sintactico.Sintactico;
import utils.PilaErrores;
import utils.models.Etiquetas;
import utils.models.Identificador;
import utils.models.TInt;
import utils.models.Token;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.Stack;

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
        System.out.println("_________");
        registros = TablaSemantica.toArray();
        analizar();
    }

    public void analizar()
    {

    }

    void validarString(){

    }

    void validarInt() {

    }
    public boolean isDeclarado(Token t)
    {
        for(Token token:declarados)
            if(t.getId() == token.getId())
                return true;
        return false;
    }

}
