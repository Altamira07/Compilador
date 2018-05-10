package utils.models;

public class TInt extends Token {
    int numero;

    public TInt(Etiquetas etiqueta,int linea, int id, int numero) {
        super(etiqueta,id, linea);
        this.numero = numero;
    }

    @Override
    public String[] toArray() {
        String []arrya = {id+"",etiqueta.toString(),numero+""};
        return arrya;
    }
}
