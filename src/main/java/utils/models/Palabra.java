package utils.models;

public class Palabra extends Token {
    String palabra;
    public Palabra(Etiquetas etiqueta,int linea,String palabra) {
        super(etiqueta,linea);
        this.palabra = palabra;
    }

    @Override
    public String[] toArray() {
        String va[] = {etiqueta.getEtiqueta()+"",etiqueta.toString(),palabra};
        return  va;
    }
}
