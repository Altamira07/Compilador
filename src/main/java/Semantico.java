import com.sun.prism.es2.ES2Graphics;
import lexico.Estados;
import lexico.Lexico;
import sintactico.Sintactico;
import utils.PilaErrores;
import utils.TablaSimbolos;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Semantico {
    Estados tokens[];
    ArrayList<TDAtabla> tabla = new ArrayList<>();
    public Semantico()
    {
        //tokens = TablaSimbolos.toArray();
        preAnalisis();

    }

    public static  void main(String[] arg)
    {
        try {
            new Lexico(new RandomAccessFile(new File("palabras"),"r"));
            if(PilaErrores.vacia())
            {
                TablaSimbolos.recorrerTabla();
                new Sintactico();
                if(PilaErrores.vacia())
                {
                    new Semantico();
                }else System.out.println("Error Sintactico");
            }else System.out.println("Error lexico");
        }catch (IOException ex){
            System.out.println(ex);
        }
    }
    public void preAnalisis()
    {
        TDAtabla valor = null;
        for(int i = 0 ; i < tokens.length;i++)
        {
            if(tokens[i] == Estados.STRING || tokens[i] == Estados.INT)
            {
                valor = new TDAtabla();
                valor.setTipo(tokens[i]);
                i++;
                valor.setIdentificador(tokens[i]);
                i++;
                if(tokens[i++] == Estados.IGUAL)
                {
                    if(tokens[i] == Estados.COMILLAS)
                        i++;
                    valor.setValor(tokens[i]);
                }else i--;
                add(valor);
                continue;
            }
            if(tokens[i] == Estados.IDENTIFICADOR)
            {
                valor = new TDAtabla();
                valor.setIdentificador(tokens[i]);
                i++;
                if(tokens[i] == Estados.IGUAL)
                {

                    i++;
                    if(tokens[i] == Estados.COMILLAS || tokens[i] == Estados.VALOR_INT)
                    {
                        if(tokens[i] == Estados.COMILLAS)
                            i++;
                        valor.setValor(tokens[i]);
                    }
                }
                add(valor);
                continue;
            }
        }
        analizar();
    }

    public void analizar()
    {
        Estados identificador,tipo,val;
        TDAtabla valor;
        for(int i = 0 ; i < tabla.size(); i++)
        {
            valor = tabla.get(i);
            identificador = valor.identificador;
            tipo = valor.tipo;
            val =valor.valor;
            if(tipo != null)
            {
                if(val != null) {
                    if ((tipo == Estados.STRING && val == Estados.VALOR_STRING)  || (tipo == Estados.INT && val == Estados.VALOR_INT))
                        continue;
                    else System.out.println("Valor no concuerda"+tipo.getLinea());
                }
            }else{
                TDAtabla declarado = backSearch(i,identificador);
                if(declarado == null)
                {
                    System.out.println("No ha sido declarado el identificador");
                }
            }



        }
    }
    public void add(TDAtabla valor)
    {
       System.out.println("Añadiendo"+ valor.getIdentificador().getLexema());
       tabla.add(valor);
    }

    public TDAtabla backSearch(int i, Estados identificador)
    {
        System.out.println("________________");
        for(int j = (i-1); j > -1; j-- )
        {

            System.out.println(tabla.get(j).identificador.getLexema());
        }
        return null;
    }

    private class TDAtabla
    {
        Estados identificador;
        Estados tipo;
        Estados valor;
        Estados operador;
        public TDAtabla(){}
        public TDAtabla(Estados identificador, Estados tipo, Estados valor) {
            this.identificador = identificador;
            this.tipo = tipo;
            this.valor = valor;
        }

        public Estados getIdentificador() {
            return identificador;
        }

        public void setIdentificador(Estados identificador) {
            this.identificador = identificador;
        }

        public Estados getTipo() {
            return tipo;
        }

        public void setTipo(Estados tipo) {
            this.tipo = tipo;
        }

        public Estados getValor() {
            return valor;
        }

        public void setValor(Estados valor) {
            this.valor = valor;
        }

        public Estados getOperador() {
            return operador;
        }

        public void setOperador(Estados operador) {
            this.operador = operador;
        }

        @Override
        public String toString() {
            return "TDAtabla{" +
                    "identificador=" + identificador +
                    ", tipo=" + tipo +
                    ", valor=" + valor +
                    ", operador=" + operador +
                    '}';
        }
    }

}
