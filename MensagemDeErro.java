/*
Fabio Silva Campos Melo
Gustavo Lescowicz Kotarsky
*/

public class MensagemDeErro {

    public static String CARACTERE_INVALIDO = "caractere invalido";
    public static String LEXEMA_NAO_IDENTIFICADO = "lexema nao identificado";
    public static String FIM_ARQUIVO_NAO_ESPERADO = "fim de arquivo nao esperado";
    public static String TOKEN_NAO_ESPERADO = "token nao esperado";
    public static String IDENTIFICADOR_NAO_DECLARADO = "identificador nao declarado";
    public static String IDENTIFICADOR_JA_DECLARADO = "identificador ja declarado";
    public static String CLASSE_IDENTIFICADOR_INCOMPATIVEL = "classe de identificador incompatível";
    public static String TIPOS_INCOMPATIVEIS = "tipos incompatíveis";



    public static void Imprimir(long line,String message, String lexeme){

        String menssageLexeme = lexeme == null ? "" : " [" + lexeme + "].";
        System.out.println(line + ":" + message + menssageLexeme);
        System.exit(0);
    }


}


