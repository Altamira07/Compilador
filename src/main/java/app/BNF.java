package app;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

public class BNF extends JFrame {
    final String bnf = "<programa> ::= 'main', '(', ')', <sentencia>\n" +
            "<sentencia> ::= <bloque> | <if> | <while> | <read> | <while> | <print> \n" +
            "                   | <asignacion> | <declaracion> | <expresion>\n" +
            "\n" +
            "<read> ::= 'read', '(', ( var | <numero> ), ')', ';' \n" +
            "<print> ::= 'print', '(', ( var | <cadena> ), ')', ';' \n" +
            "<while> ::= 'while','(', <expresion>, ')', <sentencia> \n" +
            "<if> ::= 'if', '(', <expresion>, ')', <sentencia>, ['else',<sentencia>] \n" +
            "<asignacion> ::= <id>, '=', <expresion>, ';' \n" +
            "<declaracion> ::= <tipo>, <id>, '=', <expresion>, ';' \n" +
            "<expresion> ::= <exprMat>, {( '>',<exprMat> ) | ('<',<exprMat> ) | ('=',<exprMat> ) | ('!',<exprMat> ) | ('<=',<exprMat> ) | ('>=',<exprMat> ) | ('==',<exprMat> ) | ('!=',<exprMat> ) | ('&&',<exprMat> ) | ('||',<exprMat> )}\n" +
            "\n" +
            "<exprMat> ::= <termino>, {( '+',<termino>) | ('-',<termino>)} \n" +
            "<termino> ::= <factor>, {( '*',<factor> ) | ('/',<factor>)} \n" +
            "<factor> ::= '(', <expresion>, ')'\n" +
            "         | '-', <factor>\n" +
            "         | '!', <factor>\n" +
            "         | <numero>\n" +
            "         | <cadena>\n" +
            "         | <id> \n" +
            "\n" +
            "<id> ::= letra, { letra | <digito> } \n" +
            "<tipo> ::= 'int'|'String' \n" +
            "\n" +
            "<cadena> ::= '\"', ? all characters ? , \"'\" \n" +
            "letra  ::= \"A\" | \"B\" | \"C\" | \"D\" | \"E\" | \"F\" | \"G\"\n" +
            "       | \"H\" | \"I\" | \"J\" | \"K\" | \"L\" | \"M\" | \"N\"\n" +
            "       | \"O\" | \"P\" | \"Q\" | \"R\" | \"S\" | \"T\" | \"U\"\n" +
            "       | \"V\" | \"W\" | \"X\" | \"Y\" | \"Z\" | \"a\" | \"b\"\n" +
            "       | \"c\" | \"d\" | \"e\" | \"f\" | \"g\" | \"h\" | \"i\"\n" +
            "       | \"j\" | \"k\" | \"l\" | \"m\" | \"n\" | \"o\" | \"p\"\n" +
            "       | \"q\" | \"r\" | \"s\" | \"t\" | \"u\" | \"v\" | \"w\"\n" +
            "       | \"x\" | \"y\" | \"z\" \n" +
            "\n" +
            "<numero> ::= <digito>, { <digito> } \n" +
            "<digito> ::= \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\"  ";
    public BNF()
    {
        super("Backus-Naur form del Lenguaje");
        setVisible(false);
        init();
    }
    void  init()
    {
        JPanel cp = new JPanel(new BorderLayout());

        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 100);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
        textArea.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(textArea);
        cp.add(sp);
        textArea.setEditable(false);
        textArea.setText(bnf);
        setContentPane(cp);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }
}
