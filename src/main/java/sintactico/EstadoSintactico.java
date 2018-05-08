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
    Q7(false),
    Q8(false),
    Q9(false),
    Q10(false),
    Q11(false),
    Q12(false),
    Q13(true),
    Q14(false),
    Q15(false),
    Q16(false),
    Q17(false),

    Q18(false),

    Q19(false),

    Q20(false),
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
