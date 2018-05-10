package utils.models;

public abstract  class Token {
    Etiquetas etiqueta;
    int linea;
    int id;
    String identificador ="";
    public Token(Etiquetas etiqueta,int linea){
        this.etiqueta = etiqueta;
        this.linea = linea;
        identificador ="";
    }
    public Token(Etiquetas etiqueta,int id, int linea)
    {
        this.etiqueta = etiqueta;
        this.linea = linea;
        this.id = id;
        identificador ="";
    }

    public Token(Etiquetas etiqueta,int id, int linea,String lexema)
    {
        this.etiqueta = etiqueta;
        this.linea = linea;
        this.id = id;
        this.identificador = lexema;
        identificador ="";
    }
    public abstract String[] toArray();

    @Override
    public String toString() {
        return etiqueta+" linea "+linea ;
    }

    public Etiquetas getEtiqueta() {
        return etiqueta;
    }

    public int getId() {
        return id;
    }

    public String getIdentificador() {
        return identificador;
    }

    public int getLinea() {
        return linea;
    }

}
