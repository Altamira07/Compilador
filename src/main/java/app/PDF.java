package app;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PDF {
   public static void ver(String file)
    {
        File archivo = new File(file);
        try {
            Desktop.getDesktop().open(archivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
