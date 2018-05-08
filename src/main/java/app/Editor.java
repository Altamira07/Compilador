package app;


import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Editor
{
    JFrame ventana;
    RSyntaxTextArea editor;
    JTextArea consola;
    DefaultTableModel modelo;
    JButton lexico,sintactico,semantico;
    Color color;
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

        nuevo = new JMenuItem("Nuevo");

        salir = new JMenuItem("Salir");
        guardar = new JMenuItem("Guardar");

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
         sintactico = new JButton("Sintactico");
        semantico = new JButton("Semantico");
        color = lexico.getBackground();
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

}
