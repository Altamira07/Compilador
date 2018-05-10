package utils.models;

public class OpBooleano extends Token {
    String operador;
    public OpBooleano(Etiquetas etiqueta,int linea,String operador) {
        super(etiqueta,linea);
        this.operador = operador;
    }

    @Override
    public String[] toArray() {
        String []arrya = {etiqueta.getEtiqueta()+"",etiqueta.toString(),operador};
        return arrya;
    }
}
