package semantico;


import utils.models.Token;

import java.util.ArrayList;

public class Registro {
    Token identificador;
    Token tipoDato;
    ArrayList<Token> valores;
    public Token getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Token identificador) {
        this.identificador = identificador;
    }

    public Token getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(Token tipoDato) {
        this.tipoDato = tipoDato;
    }

    public ArrayList<Token> getValores() {
        return valores;
    }

    public void setValores(ArrayList<Token> valores) {
        this.valores = valores;
    }
}
