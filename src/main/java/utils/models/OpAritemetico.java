package utils.models;

public class OpAritemetico  extends Token{
    char operador;
    public OpAritemetico(Etiquetas etiqueta,int linea,char operador) {
        super(etiqueta,linea);
        this.operador = operador;
    }
    @Override
    public String[] toArray() {
        String []arrya = {etiqueta.getEtiqueta()+"",etiqueta.toString(),operador+""};
        return arrya;
    }

    @Override
    public String toString() {
        return "OpAritemetico{" +
                "operador=" + operador +
                ", etiqueta=" + etiqueta +
                ", linea=" + linea +
                '}';
    }
}
