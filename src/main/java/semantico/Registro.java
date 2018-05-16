package semantico;


import utils.models.Token;

import java.util.ArrayList;

public class Registro {
    Token identificador;
    Token tipoDato;
    ArrayList<Token> valores;
    Token estructura;
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

    public Token getEstructura() {
        return estructura;
    }

    public void setEstructura(Token estructura) {
        this.estructura = estructura;
    }
}
