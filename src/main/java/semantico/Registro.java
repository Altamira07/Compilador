package semantico;

import utils.models.Token;

public class Registro {
    Token valor;
    Token tipo;
    Token identificador;

    public Registro(Token valor, Token tipo, Token identificador) {
        this.valor = valor;
        this.tipo = tipo;
        this.identificador = identificador;
    }
    public Registro(){
        this.valor = null;
        this.tipo = null;
        this.identificador = null;

    }

    public Token getValor() {
        return valor;
    }

    public void setValor(Token valor) {
        this.valor = valor;
    }

    public Token getTipo() {
        return tipo;
    }

    public void setTipo(Token tipo) {
        this.tipo = tipo;
    }

    public Token getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Token identificador) {
        this.identificador = identificador;
    }
}
