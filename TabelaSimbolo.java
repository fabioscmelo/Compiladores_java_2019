/*
Fabio Silva Campos Melo
Gustavo Lescowicz Kotarsky
*/

import java.util.HashMap;


public class TabelaSimbolo {
    public final byte CONST = 0;
    public final byte INTEGER = 1;
    public final byte BYTE = 2;
    public final byte STRING = 3;
    public final byte WHILE = 4;
    public final byte IF = 5;
    public final byte ELSE = 6;
    public final byte AND = 7;
    public final byte OR = 8;
    public final byte NOT = 9;
    public final byte IGUAL = 10;
    public final byte IGUAL_IGUAL = 11;
    public final byte A_PARENTESES = 12;
    public final byte F_PARENTESES = 13;
    public final byte MENOR = 14;
    public final byte MAIOR = 15;
    public final byte DIFERENTE = 16;
    public final byte MAIORIGUAL = 17;
    public final byte MENORIGUAL = 18;
    public final byte VIRGULA = 19;
    public final byte SOMA = 20;
    public final byte SUBTRACAO = 21;
    public final byte MULTIPLICACAO = 22;
    public final byte DIVISAO = 23;
    public final byte PONTO_VIRGULA = 24;
    public final byte BEGIN = 25;
    public final byte END = 26;
    public final byte THEN = 27;
    public final byte READ_LINE = 28;
    public final byte MAIN = 29;
    public final byte WRITE = 30;
    public final byte WRITE_LINE = 31;
    public final byte BOOLEAN = 32;
    public final byte ID = 33;
    public final byte VALORCONST = 34;

    public HashMap<String, Simbolo> tabela = new HashMap<String, Simbolo>();

    TabelaSimbolo() {

        tabela.put("const", new Simbolo("const", CONST));
        tabela.put("integer", new Simbolo("integer", INTEGER));
        tabela.put("byte", new Simbolo("byte", BYTE));
        tabela.put("string", new Simbolo("string", STRING));
        tabela.put("while", new Simbolo("while", WHILE));
        tabela.put("if", new Simbolo("if", IF));
        tabela.put("else", new Simbolo("else", ELSE));
        tabela.put("and", new Simbolo("and", AND));
        tabela.put("or", new Simbolo("or", OR));
        tabela.put("not", new Simbolo("not", NOT));
        tabela.put("=", new Simbolo("=", IGUAL));
        tabela.put("==", new Simbolo("==", IGUAL_IGUAL));
        tabela.put("(", new Simbolo("(", A_PARENTESES));
        tabela.put(")", new Simbolo(")", F_PARENTESES));
        tabela.put("<", new Simbolo("<", MENOR));
        tabela.put(">", new Simbolo(">", MAIOR));
        tabela.put("!=", new Simbolo("!=", DIFERENTE));
        tabela.put(">=", new Simbolo(">=", MAIORIGUAL));
        tabela.put("<=", new Simbolo("<=", MENORIGUAL));
        tabela.put(",", new Simbolo(",", VIRGULA));
        tabela.put("+", new Simbolo("+", SOMA));
        tabela.put("-", new Simbolo("-", SUBTRACAO));
        tabela.put("*", new Simbolo("*", MULTIPLICACAO));
        tabela.put("/", new Simbolo("/", DIVISAO));
        tabela.put(";", new Simbolo(";", PONTO_VIRGULA));
        tabela.put("begin", new Simbolo("begin", BEGIN));
        tabela.put("end", new Simbolo("end", END));
        tabela.put("then", new Simbolo("then", THEN));
        tabela.put("readln", new Simbolo("readln", READ_LINE));
        tabela.put("main", new Simbolo("main", MAIN));
        tabela.put("write", new Simbolo("write", WRITE));
        tabela.put("writeln", new Simbolo("writeln", WRITE_LINE));
        tabela.put("boolean", new Simbolo("boolean", BOOLEAN));

    }

    public void mostrarTabelaDeSimbolo() {
        for (Simbolo simbolo : tabela.values()) {
            System.out.println("Lexema: " + simbolo.getLexema() + " | Token: " + simbolo.getToken() + " | Tipo: "+ simbolo.getTipo() +
                    " | Classe: "+     simbolo.getClasse()) ;

        }
    }

/*
    public String pesquisa(String lexema){
        lexema = lexema.toLowerCase();
        Simbolo aux = tabela.get(lexema);
        return ((aux == null) ? "NULL" : ""+aux.getEnd());
    }
    */


    public Simbolo buscarSimbolo(String lexema) {
        return tabela.get(lexema);
    }

    public Simbolo inserirID(String lexema) {
        tabela.put(lexema, new Simbolo(lexema, ID));
        return tabela.get(lexema);
    }

    public Simbolo inserirConst(String lexema, String tipo) {
        tabela.put(lexema, new Simbolo(lexema, VALORCONST, tipo));
        return new Simbolo(lexema, VALORCONST, tipo);
    }

/*
     public static void main(String[] args) {
         TabelaSimbolo tabelaSimbolo = new TabelaSimbolo();
       // tabelaSimbolo.mostrarTabelaDeSimbolo();
         tabelaSimbolo.inserirConst("rolar","Integer");
         Simbolo simbolo = tabelaSimbolo.buscarSimbolo("rolar");
         System.out.println("Pesquisa\nLexema: " + simbolo.getLexema() + " | Token: " + simbolo.getToken() + " " +simbolo.getTipo());
     }
    */

}
