package utils.models;

public class CaracterLenguaje extends Token {
    char caracter;

    public CaracterLenguaje(Etiquetas etiqueta, int linea,char caracter) {
        super(etiqueta,linea);
        this.caracter = caracter;
    }

    @Override
    public String[] toArray() {
        String[]array = {etiqueta.getEtiqueta()+"",etiqueta.toString(),caracter+""};
        return array;
    }

    @Override
    public String toString() {
        return "CaracterLenguaje{" +
                "caracter=" + caracter +
                ", etiqueta=" + etiqueta +
                ", linea=" + linea +
                '}';
    }

}
