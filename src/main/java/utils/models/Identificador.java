package utils.models;

public class Identificador extends Token {
    String identificador;
    int id;
    public Identificador(Etiquetas etiqueta,int linea, int id,String identificador) {
        super(etiqueta,linea);

        this.identificador = identificador;
        this.id = id;
    }


    @Override
    public String[] toArray() {
        String []arrya = {id+"",etiqueta.toString(),identificador};
        return arrya;
    }


}
