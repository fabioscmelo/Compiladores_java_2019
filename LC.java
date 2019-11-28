/*
Fabio Silva Campos Melo
Gustavo Lescowicz Kotarsky
Lucas Dutra Ponce de Leon
*/

import java.io.BufferedReader;
import java.io.FileReader;

public class LC {

    public static void main(String[] args) {

        BufferedReader bufferedReader;
        AnalisadorLexico lexical;
        String nomeArquivo = args[0];
        //String nomeArquivo = "teste.l";


        if (nomeArquivo.contains(".l")) {
            new Parser(nomeArquivo).S();
        }

    }
}
