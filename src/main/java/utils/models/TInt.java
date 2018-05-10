package utils.models;

public class TInt extends Token {
    int numero;
    int id;
    public TInt(Etiquetas etiqueta,int linea, int id, int numero) {
        super(etiqueta, linea);
        this.numero = numero;
        this.id = id;
    }

    @Override
    public String[] toArray() {
        String []arrya = {id+"",etiqueta.toString(),numero+""};
        return arrya;
    }
}
