/*
Fabio Silva Campos Melo
Gustavo Lescowicz Kotarsky
*/

public class Caracter {

    public static boolean CaracterEspecial(char simbolo) {
        return simbolo == '=' || simbolo == ')' || simbolo == '(' ||
                simbolo == '<' || simbolo == '>' || simbolo == ',' ||
                simbolo == '+' || simbolo == '-' || simbolo == '*' ||
                simbolo == ';' || simbolo == ']'  || simbolo == '['
                || simbolo == '{'|| simbolo == '}'|| simbolo == '/'
                || simbolo == '"'|| simbolo == '!'|| simbolo == '?'
                || simbolo == ':'|| simbolo == '&' || simbolo == ' ';
    }

    public static char EOF = 65535;

    public static boolean Letra(char letra) {
        return letra >= 'a' && letra <= 'z' || letra >= 'A' && letra <= 'Z';
    }

    public static boolean Digito(char digito) {
        return digito >= '0' && digito <= '9';
    }

    public static boolean Hexadecimal(char hexa) {
        return hexa >= '0' && hexa <= '9' || hexa >= 'A' && hexa <= 'F'||hexa >= 'a' && hexa <= 'f';
    }

    public static boolean CaracterValido(char simbolo) {
        return (Letra(simbolo) || Digito(simbolo) || CaracterEspecial(simbolo) || simbolo == '\'' || simbolo == 10
                || simbolo == 13);
    }

}
