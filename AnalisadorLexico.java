/*
Fabio Silva Campos Melo
Gustavo Lescowicz Kotarsky
Lucas Dutra Ponce de Leon
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AnalisadorLexico {

    //leitura
    private BufferedReader _bufferedReader;
    public long linha;
    public boolean EOF;


    //automato
    private boolean erroCompila;
    public String lexema;
    private char charDevolvido;
    private boolean devolver;


    //tabela de simbolo
    private TabelaSimbolo tabelaSimbolo;
    private Simbolo simbolo;
    private String tipo;


    //construtor
    public AnalisadorLexico(BufferedReader bufferedReader, TabelaSimbolo tbSimbolo) {
        tabelaSimbolo = tbSimbolo;
        _bufferedReader = bufferedReader;
        linha = 1;
        EOF = false;
        erroCompila = false;


    }


    public Simbolo automatoFinito() {
        lexema = "";
        int estado = 0;
        int estadoFinal = 9;
        final  int a = 3;
        int b = a+3;

        while (estado != estadoFinal) {
            switch (estado) {


                case 0:
                    estado = estado0();
                    break;
                case 1:
                    estado = estado01();
                    break;
                case 2:
                    estado = estado02();
                    break;
                case 3:
                    estado = estado03();
                    break;
                case 5:
                    estado = estado05();
                    break;
                case 7:
                    estado = estado07();
                    break;
                case 16:
                    estado = estado16();
                    break;
                case 11:
                    estado = estado11();
                    break;
                case 12:
                    estado = estado12();
                    break;
                case 13:
                    estado = estado13();
                    break;
                case 14:
                    estado = estado14();
                    break;
                case 15:
                    estado = estado15();
                    break;
                case 17:
                    estado = estado17();
                    break;
                case 18:
                    estado = estado18();
                    break;
                case 19:
                    estado = estado19();
                    break;
                case 20:
                    estado = estado20();
                    break;
                case 9:
                    estado = 9;
                    break;
            }
        }

        if (erroCompila)
            System.exit(0);

        if (!EOF) {
            //Busca simbolo na tabela
            if (tabelaSimbolo.buscarSimbolo(lexema) != null) {
                simbolo = tabelaSimbolo.buscarSimbolo(lexema);

                //Se nao tiver tenta inserir
                // tenta inserir constante integer
            } else if (Caracter.Digito((lexema.charAt(0)))) {
                simbolo = tabelaSimbolo.inserirConst(lexema, tipo);

                // tenta inserir constante string
            } else if (lexema.charAt(0) == '\'' && lexema.charAt(lexema.length() - 1) == '\'') {
                tipo = "string";
                String replace = lexema.replaceAll("\'\'", "\'");
                simbolo = tabelaSimbolo.inserirConst(replace, tipo);

                // tenta inserir constante boolean
            } else if ("true".equals(lexema) || "false".equals(lexema)) {
                tipo = "boolean";
                simbolo = tabelaSimbolo.inserirConst(lexema, tipo);

                // senao insere novo indetificador
            } else {
                simbolo = tabelaSimbolo.inserirID(lexema);
            }
            // comentario
        } else {
            simbolo = new Simbolo("", (byte) -1);
        }
        return simbolo;
    }


    private char lerChar() {

        try {
            if (devolver == false) {
                charDevolvido = (char) _bufferedReader.read();
                //System.out.println(((byte) charDevolvido));
                if (charDevolvido == 10) {
                    linha++;
                }
            } else {
                devolver = false;

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return charDevolvido;
    }


    private int estado0() {
        lexema = "";
        char simbolo = lerChar();

        if (simbolo == 10) {
            return 0;

        } else if (simbolo == 32 || simbolo == 11 || simbolo == 8 || simbolo == 13 || simbolo == 9) {
            return 0;

        } else if (simbolo == '(' || simbolo == ')' || simbolo == ',' || simbolo == '+' || simbolo == '-' || simbolo == '*'
                || simbolo == ';' || simbolo == '*' || simbolo == ',') {
            lexema += simbolo;
            return 9;

        } else if (Caracter.Letra(simbolo)) {
            lexema += simbolo;
            return 16;

        } else if (simbolo == '_') {
            lexema += simbolo;
            return 20;

        } else if (simbolo == '0') {
            tipo = "integer";
            lexema += simbolo;
            return 1;

        } else if (Caracter.Digito(simbolo) && simbolo != 0) {
            lexema += simbolo;
            return 7;

        } else if (simbolo == '\'') {
            lexema += simbolo;
            return 11;

        } else if (simbolo == '>') {
            lexema += simbolo;
            return 12;

        } else if (simbolo == '<') {
            lexema += simbolo;
            return 13;

        } else if (simbolo == '!') {
            lexema += simbolo;
            return 14;

        } else if (simbolo == '=') {
            lexema += simbolo;
            return 15;

        } else if (simbolo == '/') {
            lexema += simbolo;
            return 17;

        } else if (simbolo == Caracter.EOF) {
            EOF = true;
            return 9;
        }
        return error(simbolo);

    }

    private int estado01() {
        char simbolo = lerChar();
        if (simbolo == 'h') {
            lexema += simbolo;
            return 2;
        } else if (Caracter.Digito(simbolo)) {
            lexema += simbolo;
            return 7;
        } else if (!(Caracter.Digito(simbolo) && simbolo == 'h')) {
            devolver = true;
            return 9;
        }
        return error(simbolo);
    }

    private int estado02() {
        char simbolo = lerChar();
        if (Caracter.Hexadecimal(simbolo)) {
            lexema += simbolo;
            return 3;
        }
        return error(simbolo);
    }

    private int estado03() {
        char simbolo = lerChar();
        if (Caracter.Hexadecimal(simbolo)) {
            lexema += simbolo;
            tipo = "byte";
            return 9;
        }
        return error(simbolo);
    }

    private int estado05() {
        char simbolo = lerChar();
        if (simbolo == '\'') {
            lexema += simbolo;
            return 11;
        } else if (!(simbolo == '\'')) {
            devolver = true;
            return 9;
        }
        return error(simbolo);
    }


    private int estado07() {
        char simbolo = lerChar();
        if (Caracter.Digito(simbolo)) {
            lexema += simbolo;
            tipo = "integer";
            return 7;
        } else if (!(Caracter.Digito(simbolo))) {
            tipo = "integer";
            devolver = true;
            return 9;
        }
        return error(simbolo);
    }

    private int estado11() {
        char simbolo = lerChar();
        if (Caracter.Digito(simbolo) || Caracter.Letra(simbolo) || Caracter.CaracterEspecial(simbolo)
                || simbolo == ' ') {
            lexema += simbolo;
            return 11;
        } else if (simbolo == '\'') {
            lexema += simbolo;
            return 5;
        }
        return error(simbolo);
    }

    private int estado12() {
        char simbolo = lerChar();
        if (simbolo == '=') {
            lexema += simbolo;
            return 9;
        } else if (simbolo != '=') {
            devolver = true;
            return 9;
        }
        return error(simbolo);
    }

    private int estado13() {
        char simbolo = lerChar();
        if (simbolo == '=') {
            lexema += simbolo;
            return 9;
        } else if (simbolo != '=') {
            devolver = true;
            return 9;
        }
        return error(simbolo);
    }

    private int estado14() {
        char simbolo = lerChar();
        if (simbolo == '=') {
            lexema += simbolo;
            return 9;
        } else if (simbolo != '=') {
            devolver = true;
            return 9;
        }
        return error(simbolo);
    }


    private int estado15() {
        char simbolo = lerChar();
        if (simbolo == '=') {
            lexema += simbolo;
            return 9;
        } else if (simbolo != '=') {
            devolver = true;
            return 9;
        }
        return error(simbolo);
    }

    private int estado16() {
        char simbolo = lerChar();
        if (Caracter.Letra(simbolo) || Caracter.Digito(simbolo) || simbolo == '_') {
            lexema += simbolo;
            return 16;
        } else if (!(Caracter.Letra(simbolo) || Caracter.Digito(simbolo) || simbolo == '_')) {
            devolver = true;
            return 9;
        }
        return error(simbolo);

    }

    private int estado17() {
        char simbolo = lerChar();
        if (simbolo == '*') {
            lexema += simbolo;
            return 18;
        } else if (!(simbolo == '*')) {
            devolver = true;
            return 9;
        }
        return error(simbolo);

    }

    private int estado18() {
        char simbolo = lerChar();

        if (!(Caracter.CaracterValido(simbolo))) {
            return error(simbolo);
        }
        if (simbolo == '*') {
            lexema += simbolo;
            return 19;
        } else if (!(simbolo == '*' && EOF)) {
            lexema += simbolo;
            return 18;
        }
        return error(simbolo);
    }

    private int estado19() {
        char simbolo = lerChar();
        if (simbolo == '/') {
            lexema = "";
            return 0;
        } else if (!(simbolo == '/')) {
            lexema += simbolo;
            return 18;
        }
        return error(simbolo);
    }

    private int estado20() {
        char simbolo = lerChar();
        if (Caracter.Letra(simbolo)) {
            lexema += simbolo;
            return 16;
        }
        return error(simbolo);
    }


    public int error(char simbolo) {
        erroCompila = true;
        if (simbolo == Caracter.EOF) {
            MensagemDeErro.Imprimir(linha,MensagemDeErro.FIM_ARQUIVO_NAO_ESPERADO,null);
            //System.out.println(linha + ":fim de arquivo nao esperado.");
            EOF = true;
            return 9;
        }
        if (Caracter.CaracterValido(simbolo)) {
            if (simbolo == 13 || simbolo == 10) {
               MensagemDeErro.Imprimir(linha,MensagemDeErro.LEXEMA_NAO_IDENTIFICADO,lexema);

                //System.out.println(linha + ":lexema nao identificado" + "[" + lexema + "/n].");
            } else {
                MensagemDeErro.Imprimir(linha,MensagemDeErro.LEXEMA_NAO_IDENTIFICADO,lexema);
            }
        } else
            MensagemDeErro.Imprimir(linha,MensagemDeErro.CARACTERE_INVALIDO,null);
          //  System.out.println(linha + ":caractere invalido.");

        return 9;
    }



/*
   public static void main(String[] args) throws Exception {
      try (FileReader reader = new FileReader("teste.l"); BufferedReader br = new BufferedReader(reader)) {
          AnalisadorLexico aL = new AnalisadorLexico(br);
         //FileReader reader2 = new FileReader("teste.l");
        // BufferedReader br2 = new BufferedReader(reader2);


         Simbolo simbolo = new Simbolo();
         while (br.read() != 65535) {
            simbolo = aL.automatoFinito();
           // simbol = aL.analisarLexema(aL.devolve, br, br2);
            if (simbolo != null) {
               System.out.println(simbolo.getToken()+" "+simbolo.getLexema());
            }
         }
      } catch (IOException e) {
         System.err.format("IOException: %s%n", e);
      }
   }
*/
}
