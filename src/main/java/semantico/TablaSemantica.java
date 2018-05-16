package semantico;


import utils.models.Token;

import java.util.ArrayList;

public class TablaSemantica {

    public static ArrayList<Registro> valores = new ArrayList<>();
    public static  void  limpiar()
    {
        valores = new ArrayList<>();
    }

    public static void insertar(Registro registro)
    {
        valores.add(registro);
    }
    public static Registro []toArray()
    {
        Registro registros[] = new Registro[valores.size()];
        for(int i = 0; i < valores.size(); i++)
            registros[i] = valores.get(i);

        return registros;
    }
}
