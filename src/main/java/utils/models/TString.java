package utils.models;

public class TString extends Token{
    String string;
    public TString(Etiquetas etiqueta,int linea, int id, String string) {
        super(etiqueta,id,linea);
        this.string = string;

    }
    @Override
    public String[] toArray() {
        String []arrya = {id+"",etiqueta.toString(),string};
        return arrya;
    }
}
