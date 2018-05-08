package sintactico;

import lexico.Estados;

public enum EstadoSintactico {
    //Transiciones para expresiones
    EXP(false),
    Q0(false),
    Q1(true),
    Q2(false),
    Q3(false),
    Q4(false),
    Q5(false),
    Q6(false),
    CONTROL(false),
    Q7(false),
    Q8(true),
    Q9(false),
    Q10(true),
    DECLARACIONES(false),
    Q11(false),
    Q12(false),
    Q13(false),
    Q14(false),
    PROGRAMA(false),
    INSTRUCCIONES(false)
    ;



    private final boolean _final;

    private EstadoSintactico(boolean _final)
    {
        this._final = _final;
    }
    public boolean isFinal()
    {
        return  _final;
    }
}
