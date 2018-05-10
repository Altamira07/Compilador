package utils.models;

public class Identificador extends Token {

    public Identificador(Etiquetas etiqueta,int linea, int id,String identificador) {
        super(etiqueta,id,linea,identificador);

        this.identificador = identificador;
    }


    @Override
    public String[] toArray() {
        String []arrya = {id+"",etiqueta.toString(),identificador};
        return arrya;
    }


}
