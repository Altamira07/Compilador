package app;


import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;
import semantico.TablaSemantica;
import utils.PilaErrores;
import utils.TablaSimbolos;
import utils.models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static org.fife.ui.rsyntaxtextarea.TokenTypes.LITERAL_BACKQUOTE;

public class Editor extends Action
{
    JFrame ventana;
    RSyntaxTextArea editor;
    Color colorDefault;
    RSyntaxTextArea consola;
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

        JMenu ayuda = new JMenu("Ayuda");

        JMenuItem verBNF = new JMenuItem("ver BNF");
        JMenuItem docSintacito = new JMenuItem("Acerca de Sintactico");
        docSintacito.addActionListener(new Oyente(ActionForButtons.DOC_SINTACTICO));

        JMenuItem docSemantico = new JMenuItem("Acerca de Semantico");
        docSemantico.addActionListener(new Oyente(ActionForButtons.DOC_SEMANTICO));

        JMenuItem docLexico = new JMenuItem("Acerca de Lexico");
        docLexico.addActionListener(new Oyente(ActionForButtons.DOC_LEXICO));
        JMenuItem acerca = new JMenuItem("Acerca de");
        acerca.addActionListener(new Oyente(ActionForButtons.ACERCA_DE));
        verBNF.addActionListener(new Oyente(ActionForButtons.VERBNF));
        ayuda.add(docLexico);
        ayuda.add(docSintacito);
        ayuda.add(docSemantico);
        ayuda.addSeparator();
        ayuda.add(verBNF);
        ayuda.addSeparator();
        ayuda.add(acerca);

        menuBar.add(mArchivo);

        menuBar.add(ayuda);
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
        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/custom", "app.custom.CustomEditor");
        editor.setSyntaxEditingStyle("text/custom");

        editor.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(editor);
        return sp;
    }

    private JPanel getConsola()
    {
        consola = new RSyntaxTextArea(5,5);
        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        //atmf.putMapping("text/custom","app.custom.CustomConsole");
        SyntaxScheme scheme = consola.getSyntaxScheme();
        scheme.getStyle(LITERAL_BACKQUOTE).foreground = Color.RED;
        consola.setSyntaxEditingStyle("text/custom");
        consola.setCodeFoldingEnabled(false);
        consola.setEditable(false);
        consola.setEOLMarkersVisible(false);
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
        String[]campos ={"ID","Tipo","Lexema"};
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
        TablaSemantica.limpiar();

    }
    @Override
    void salir() {
        System.exit(0);
    }

    @Override
    void verErrores() {
        consola.setCaretColor(Color.RED);
        consola.setText(PilaErrores.getErrors());
    }

    @Override
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
    @Override
    void toTable()
    {
        Token[] tokens =  TablaSimbolos.toArray();
        for(int i = 0 ; i< tokens.length;i++)
        {
            String datos[] = new String[3];
            switch (tokens[i].getEtiqueta())
            {
                case IDENTIFICADOR:
                    Identificador id = (Identificador)tokens[i];
                    modelo.addRow(id.toArray());
                    break;
                case VALOR_STRING:
                    TString tString = (TString)tokens[i];
                    modelo.addRow(tString.toArray());
                    break;
                case VALOR_INT:
                    TInt tInt =  (TInt) tokens[i];
                    modelo.addRow(tInt.toArray());
                    break;
                case WHILE:
                case IF:
                case ELSE:
                case INIT:
                case STRING:
                case INT:
                case PRINT:
                case READ:
                    Palabra palabra = (Palabra)tokens[i];
                    modelo.addRow(palabra.toArray());
                    break;
                case AND:
                case OR:
                case MAYOR:
                case MENOR:
                case DIFERENTE:
                    OpBooleano opBooleano = (OpBooleano) tokens[i];
                    modelo.addRow(opBooleano.toArray());
                    break;
                case SUMA:
                case RESTA:
                case MULTIPLICACION:
                case DIVISION:
                case ASIGNACION:
                    OpAritemetico opAritemetico = (OpAritemetico) tokens[i];
                    modelo.addRow(opAritemetico.toArray());
                    break;
                case ABRE_LLAVE:
                case CIERRA_LLAVE:
                case ABRE_PARENTESIS:
                case CIERRA_PARENTESIS:
                case PUNTO_COMA:
                    CaracterLenguaje caracterLenguaje = (CaracterLenguaje) tokens[i];
                    modelo.addRow(caracterLenguaje.toArray());
                    break;
            }
        }
    }
}
