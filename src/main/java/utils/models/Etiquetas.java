package utils.models;

public enum Etiquetas {
    //Palabras reservadas
    INIT(0),
    WHILE(1),
    IF(2),
    ELSE(3),
    STRING(21),
    INT(22),
    READ(25),
    PRINT(26),

    //Identificadores
    IDENTIFICADOR(100),

    //Operadores Aritemeticos
    SUMA(4),
    RESTA(5),
    MULTIPLICACION(6),
    DIVISION(7),
    ASIGNACION(14),

    //Valores
    VALOR_INT(200),
    VALOR_STRING(300),

    //Operadores booleanos
    MENOR(8),
    MENOR_IGUAL(9),
    IGUAL_IGUAL(10),
    MAYOR_IGUAL(11),
    MAYOR(12),
    DIFERENTE(13),
    AND(23),
    OR(24),
    //CaracteresLenguaje
    PUNTO_COMA(15),
    ABRE_PARENTESIS(16),
    CIERRA_PARENTESIS(17),
    ABRE_LLAVE(18),
    CIERRA_LLAVE(19),
    ;

    private final int etiqueta;
    private Etiquetas(int etiqueta)
    {
        this.etiqueta = etiqueta;
    }

    public int getEtiqueta() {
        return etiqueta;
    }
}
