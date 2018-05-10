package utils.models;

public abstract  class Token {
    Etiquetas etiqueta;
    int linea;
    public Token(Etiquetas etiqueta,int linea){
        this.etiqueta = etiqueta;
        this.linea = linea;
    }

    public abstract String[] toArray();

    @Override
    public String toString() {
        return etiqueta+" linea "+linea ;
    }

    public Etiquetas getEtiqueta() {
        return etiqueta;
    }



    public int getLinea() {
        return linea;
    }

}
