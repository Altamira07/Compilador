package app;

import com.sun.org.apache.regexp.internal.RE;
import lexico.Lexico;
import sintactico.Sintactico;
import utils.PilaErrores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class Action  {

    File archivo;

    public void hacerLexico()
    {
        reiniciar();
        guardar();
        if(archivo != null)
        {
            try {
                new Lexico(new RandomAccessFile(archivo,"r"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(PilaErrores.vacia())
            {
                toTable();
                cambiarColores(ActionForButtons.LEXICO,Color.GREEN);
                habilitar(ActionForButtons.SINTACTICO,true);
                habilitar(ActionForButtons.LEXICO,false);
                cambiarColores(ActionForButtons.SINTACTICO,Color.YELLOW);
            }
            else{
                cambiarColores(ActionForButtons.LEXICO,Color.red);

                verErrores();
            }

        }
    }
    public void hacerSintactico()
    {
        hacerLexico();
        if(PilaErrores.vacia())
        {

            new Sintactico();
            if(PilaErrores.vacia())
            {

                cambiarColores(ActionForButtons.SINTACTICO,Color.GREEN);
                habilitar(ActionForButtons.SEMANTICO,true);
                habilitar(ActionForButtons.SINTACTICO,false);
                cambiarColores(ActionForButtons.SEMANTICO,Color.YELLOW);
            }else{
                cambiarColores(ActionForButtons.SINTACTICO,Color.RED);
                verErrores();
            }
        }
    }
    public void hacerSemantico()
    {

    }
    abstract void habilitar(ActionForButtons action,boolean b);
    abstract void toTable();
    abstract void guardar();
    abstract void abrir();
    abstract void nuevo();
    abstract void salir();
    abstract void verErrores();
    abstract  void cambiarColores(ActionForButtons action,Color color);
    abstract void reiniciar();
    class Oyente implements ActionListener
    {
        ActionForButtons action =null;
        public Oyente(ActionForButtons action)
        {
            this.action = action;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (action)
            {
                case LEXICO:
                    hacerLexico();
                    break;
                case SINTACTICO:
                    hacerSintactico();
                    break;
                case SEMANTICO:
                    hacerSemantico();
                    break;
                case ABRIR:
                    abrir();
                    break;
                case NUEVO:
                    nuevo();
                    break;
                case GUARDAR:
                    guardar();
                    break;
                case SALIR:
                    salir();
                    break;

            }
        }
    }
}
