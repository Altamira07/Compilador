package app;


import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import utils.PilaErrores;
import utils.TablaSimbolos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Editor extends Action
{
    JFrame ventana;
    RSyntaxTextArea editor;
    Color colorDefault;
    JTextArea consola;
    DefaultTableModel modelo;
    JButton lexico,sintactico,semantico;
    public Editor()
    {
        ventana = new JFrame("Compilador");
        init();
    }
    private void init()
    {
        ventana.setLayout(new BorderLayout());
        ventana.setJMenuBar(getMenuBar());
        ventana.add(getToolBar(),BorderLayout.NORTH);


        ventana.add(getEditor(),BorderLayout.CENTER);
        ventana.add(getConsola(),BorderLayout.SOUTH);

        ventana.add(getTable(),BorderLayout.EAST);

        ventana.setDefaultCloseOperation(ventana.EXIT_ON_CLOSE);
        ventana.pack();
        ventana.setLocationRelativeTo(null);

    }

    private JMenuBar getMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu mArchivo = new JMenu("Archivo");
        JMenuItem abrir,nuevo,salir,guardar;

        abrir = new JMenuItem("Abrir");
        abrir.addActionListener(new Oyente(ActionForButtons.ABRIR));
        nuevo = new JMenuItem("Nuevo");
        nuevo.addActionListener(new Oyente(ActionForButtons.NUEVO));

        salir = new JMenuItem("Salir");
        salir.addActionListener(new Oyente(ActionForButtons.SALIR));
        guardar = new JMenuItem("Guardar");
        guardar.addActionListener(new Oyente(ActionForButtons.GUARDAR));
        mArchivo.add(nuevo);
        mArchivo.add(abrir);
        mArchivo.add(guardar);
        mArchivo.addSeparator();
        mArchivo.add(salir);

        menuBar.add(mArchivo);
        return menuBar;
    }

    private JToolBar getToolBar()
    {

        JToolBar toolBar = new JToolBar();

        lexico = new JButton("Lexico");
        lexico.addActionListener(new Oyente(ActionForButtons.LEXICO));
        colorDefault = lexico.getBackground();
        sintactico = new JButton("Sintactico");
        sintactico.addActionListener(new Oyente(ActionForButtons.SINTACTICO));
        semantico = new JButton("Semantico");
        semantico.addActionListener(new Oyente(ActionForButtons.SEMANTICO));
        toolBar.add(lexico);
        toolBar.add(sintactico);
        toolBar.add(semantico);

        return toolBar;
    }

    private RTextScrollPane getEditor()
    {
        editor = new RSyntaxTextArea(   30,60);
        editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        editor.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(editor);
        return sp;
    }

    private JPanel getConsola()
    {
        consola = new JTextArea(5, 5);
        consola.setEditable(false);
        JLabel lblConsola = new JLabel("Consola");
        JPanel pnlConsola = new JPanel(new BorderLayout());
        pnlConsola.add(lblConsola, BorderLayout.NORTH);
        pnlConsola.add(new JScrollPane(consola), BorderLayout.CENTER);
        return pnlConsola;
    }

    private JScrollPane getTable()
    {
        JTable table = new JTable();
        modelo = (DefaultTableModel) table.getModel();
        String[]campos ={"ID","Descripcion","Lexema"};
        for(String s:campos)
            modelo.addColumn(s);
        return new JScrollPane(table);
    }

    public void setVisible(boolean visible)
    {
        ventana.setVisible(visible);
    }

    @Override
    void habilitar(ActionForButtons action,boolean b) {
        switch (action)
        {
            case LEXICO:
                lexico.setEnabled(b);
                break;
            case SEMANTICO:
                semantico.setEnabled(b);
                break;
            case SINTACTICO:
                sintactico.setEnabled(b);
        }
    }

    @Override
    void guardar() {
        reiniciar();
        if(archivo == null)
        {
            JFileChooser chooser = new JFileChooser();
            chooser.showSaveDialog(ventana);
            archivo = chooser.getSelectedFile();
        }
        if(archivo != null)
        {
            FileWriter fw;
            try {
                fw = new FileWriter(archivo);
                fw.write(editor.getText());
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else
            JOptionPane.showMessageDialog(null,"No se ha guardado el Archivo","Archivo no guardado",JOptionPane.WARNING_MESSAGE);
        reiniciar();
    }

    @Override
    void abrir() {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(ventana);
        archivo = chooser.getSelectedFile();
        if(archivo != null)
        {
            Scanner lector;
            try {
                lector= new Scanner(archivo);
                String cadena = "";
                while (lector.hasNextLine())
                    cadena+=lector.nextLine()+"\n";
                reiniciar();
                editor.setText(cadena);
                lector.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else JOptionPane.showMessageDialog(null,"No se ha abierto ningun archivo","Error",JOptionPane.ERROR_MESSAGE);
    }

    @Override
    void nuevo() {
        consola.setText("");
        editor.setText("");
        modelo.setNumRows(0);
        reiniciar();
    }

    @Override
    void reiniciar()
    {
        System.out.println("No entro :v");
        consola.setText("");
        modelo.setNumRows(0);
        lexico.setEnabled(true);
        cambiarColores(ActionForButtons.LEXICO, Color.YELLOW);
        sintactico.setEnabled(false);
        cambiarColores(ActionForButtons.SINTACTICO, colorDefault);
        semantico.setEnabled(false);
        cambiarColores(ActionForButtons.SEMANTICO, colorDefault);
        TablaSimbolos.limpiar();
        PilaErrores.limpiar();

    }
    @Override
    void salir() {
        System.exit(0);
    }

    @Override
    void verErrores() {
        consola.setText(PilaErrores.getErrors());
    }

    @Over   ride
    void cambiarColores(ActionForButtons action, Color color) {
        switch (action)
        {
            case LEXICO:
                lexico.setBackground(color);
                break;
            case SINTACTICO:
                sintactico.setBackground(color);
                break;
            case SEMANTICO:
                semantico.setBackground(color);
        }
    }
}
