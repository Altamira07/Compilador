package utils.models;

public class TString extends Token{
    String string;
    int id;
    public TString(Etiquetas etiqueta,int linea, int id, String string) {
        super(etiqueta,linea);
        this.string = string;
        this.id = id;
    }
    @Override
    public String[] toArray() {
        String []arrya = {id+"",etiqueta.toString(),string};
        return arrya;
    }
}
