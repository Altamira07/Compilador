package semantico;


import java.util.ArrayList;

public class TablaSemantica {
    public static ArrayList<Registro> tabla = new ArrayList<>();
    public static  void insertar(Registro r)
    {
        System.out.println("Insertando");
        tabla.add(r);
    }
    public static void limipar()
    {
        tabla = new ArrayList<>();
    }
    public static Registro[] toArray()
    {
        Registro r[] = new Registro[tabla.size()];

        for( int i = 0; i < tabla.size(); i++)
            r[i] = tabla.get(i);


        return r;
    }

}
