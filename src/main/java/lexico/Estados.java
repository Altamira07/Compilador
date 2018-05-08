package lexico;

public enum Estados 
{
	Q0("Q0",false),
	Q1("Q1",false),
	Q2("Q2",false),
	Q3("Q3",false),
	INT(103,"int",true,"Palabra reservada"),
	IF(107,"if",true,"Palabra reservada"),
	Q6("Q6",false),
	INIT(101,"init",true,"Palabra reservada"),
	Q8("Q8",false),
	Q9("Q9",false),
	Q10("Q10",false),
	Q11("Q11",false),
	WHILE(106,"while",true,"Palabra reservada"),
	Q13("Q13",false),
	Q14("Q14",false),
	Q15("Q15",false),
	Q16("Q16",false),
	Q17("Q17",false),
	Q18("Q18",false),
	STRING(102,"String",true,"Palabra reservada"),
	Q20("Q20",false),
	Q21("Q21",false),
	Q22("Q22",false),
	Q23("Q23",false),
	Q24("Q24",false),
	Q25("Q25",false),
	Q26("Q26",false),
	PRINT(105,"print",true,"Palabra reservada"),
	
	Q27("Q27",false),
	Q28("Q28",false),
	Q29("Q29",false),
	READ(104,"read",true,"Palabra reservada"),
	
	
	ELSE(108,"else",true,"Palabra reservada"),



	DIVISION(133,"/",true,"Operador aritmetico"),
	MULTIPLICACION(132,"*",true,"Operador aritmetico"),
	RESTA(131,"-",true,"Operador aritmetico"),
	SUMA(130,"+",true,"Operador aritmetico"),
	
	IGUAL(122,"=",true,"igual"),
	MENOR(120,"<",true,"menor que"),
	MAYOR(121,">",true,"mayor que"),
	
	AND(122,"&",true,"and"),
	OR(123,"|",true,"or"),
	
	ABRE_LLAVE(115,"{",true,"delimitador_abre"),
	ABRE_PARENTESIS(117,"(",true,"delimitador_abre"),
	CIERRA_LLAVE(116,"}",true,"delimitador_cierre"),
	CIERRA_PARENTESIS(118,")",true,"delimitador_cierre"),
	PUNTO_COMA(110,";",true,"delimitador"),
	COMILLAS(119,"\"",true,"Comillas"),
	IDENTIFICADOR(400),
	VALOR_INT(1000),
	VALOR_STRING(2000),
    VACIO(),
	;
	
	
	private  String estado;
	private final boolean _final;
	private final String descripcion;
	private final int etiqueta;
	private int linea,pocision;
	Estados (int etiqueta, String estado, boolean _final,String desc)
	{
		this.etiqueta = etiqueta;
		this.estado = estado;
		this._final = _final;
		this.descripcion = desc;
	}
	Estados(){
		estado = "";
		_final = true;
		descripcion = "";
		etiqueta = 0;

	}
	Estados (String estado, boolean _final,String desc)
	{
		etiqueta = 0;
		this.estado = estado;
		this._final = _final;
		this.descripcion = desc;
	}

	Estados(int etiqueta)
	{
		this.etiqueta = etiqueta;
		_final = false;
		descripcion = "";
		estado = "";
	}
	Estados (String estado,boolean _final){
		etiqueta = 0;
		this.estado = estado;
		this.descripcion = "";
		this._final = _final;
	}
	
	public String getEstado() {
		return estado;
	}
	public boolean isFinal() {
		return _final;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public int getLinea() {
		return linea;
	}

	public void setLinea(int linea) {
		this.linea = linea;
	}

	public int getPocision() {
		return pocision;
	}

	public void setLexema(String valor)
	{
		this.estado = valor;
	}

	public void setPocision(int pocision) {
		this.pocision = pocision;
	}

	public int getEtiqueta()
	{
		return etiqueta;
	}
}

