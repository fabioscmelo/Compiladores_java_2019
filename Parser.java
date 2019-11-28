/*
Fabio Silva Campos Melo
Gustavo Lescowicz Kotarsky
*/

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.BufferedReader;
import java.io.FileReader;

public class Parser {

    private BufferedReader bufferedReader, bufferedReader2;
    private AnalisadorLexico lexico;
    private TabelaSimbolo tabelaSimbolo;
    private Simbolo simbolo;


    public Parser(String fileName) {

        try {
            /*
            bufferedReader = new BufferedReader(new FileReader(fileName));
            bufferedReader2 = new BufferedReader(new FileReader(fileName));

            lexico = new AnalisadorLexico(bufferedReader);
            tabelaSimbolo = new TabelaSimbolo();

            simbolo = lexico.automatoFinito();
            if (simbolo.getToken() != -1)
                System.out.println(simbolo.getToken() + " " + simbolo.getLexema());
            while (simbolo.getToken() != -1) {
                simbolo = lexico.automatoFinito();
                if (simbolo.getToken() != -1)
                    System.out.println(simbolo.getToken() + " " + simbolo.getLexema() + " "+ simbolo.getTipo());
            }
             */

            bufferedReader = new BufferedReader(new FileReader(fileName));
            tabelaSimbolo = new TabelaSimbolo();
            lexico = new AnalisadorLexico(bufferedReader, tabelaSimbolo);

            simbolo = lexico.automatoFinito();


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    void casaToken(byte tokenEsperado) {

        if (simbolo.getToken() == tokenEsperado) {
           // System.out.println(simbolo.getToken() + " " + simbolo.getLexema() + " " + simbolo.getTipo());
            simbolo = lexico.automatoFinito();

        } else {

            if (lexico.EOF) {
                MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.FIM_ARQUIVO_NAO_ESPERADO, lexico.lexema);
                // System.out.println(lexico.linha + " :fim de arquivo não esperado");
                System.exit(0);
            } else {
                MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TOKEN_NAO_ESPERADO, lexico.lexema);
                // System.out.println(lexico.linha + " :token nao esperado" + "[" + lexico.lexema + "]");
                System.exit(0);
            }
        }
    }

    //S -> {D}+ main {C}+end
    void S() {

        while (simbolo.getToken() == tabelaSimbolo.INTEGER ||
                simbolo.getToken() == tabelaSimbolo.BOOLEAN ||
                simbolo.getToken() == tabelaSimbolo.BYTE ||
                simbolo.getToken() == tabelaSimbolo.STRING ||
                simbolo.getToken() == tabelaSimbolo.CONST) {
            D();
        }
        casaToken(tabelaSimbolo.MAIN);
        while (simbolo.getToken() == tabelaSimbolo.ID ||
                simbolo.getToken() == tabelaSimbolo.WHILE ||
                simbolo.getToken() == tabelaSimbolo.IF ||
                simbolo.getToken() == tabelaSimbolo.PONTO_VIRGULA ||
                simbolo.getToken() == tabelaSimbolo.READ_LINE ||
                simbolo.getToken() == tabelaSimbolo.WRITE ||
                simbolo.getToken() == tabelaSimbolo.WRITE_LINE) {
            C();
        }
        casaToken(tabelaSimbolo.END);

    }

    // D -> (integer | boolean | byte | string) id [= [-]v_const] {,id [= [-]v_const]}; |
    //         const id = [-]v_const;

    void D() {

        Simbolo auxD = new Simbolo(), auxID = new Simbolo(), auxCONST = new Simbolo(), auxIDConst = new Simbolo();
        Boolean sinal = false;


        if (simbolo.getToken() == tabelaSimbolo.INTEGER) {


            auxD.classe = "var";      //semantica (5)
            auxD.tipo = "integer";          //semantica (1)
            casaToken(tabelaSimbolo.INTEGER);

        } else if (simbolo.getToken() == tabelaSimbolo.BOOLEAN) {


            auxD.classe = "var";  //semantica (5)
            auxD.setTipo("boolean");       //semantica (2)
            casaToken(tabelaSimbolo.BOOLEAN);

        } else if (simbolo.getToken() == tabelaSimbolo.BYTE) {


            auxD.setClasse("var");  //semantica (5)
            auxD.setTipo("byte"); //semantica (3)
            casaToken(tabelaSimbolo.BYTE);

        } else if (simbolo.getToken() == tabelaSimbolo.STRING) {

            auxD.setClasse("var");  //semantica (5)
            auxD.setTipo("string"); //semantica (4)
            casaToken(tabelaSimbolo.STRING);

        } else {
            auxD.classe = "const";
            casaToken(tabelaSimbolo.CONST);
            auxCONST = simbolo;

            if (auxCONST.classe == "")//semantica (7)
            {
                tabelaSimbolo.buscarSimbolo(auxCONST.lexema).classe = auxD.classe;
                if (auxCONST.classe != "const") {
                    tabelaSimbolo.buscarSimbolo(auxID.lexema).tipo = auxD.tipo;
                }

            } else {
                MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.IDENTIFICADOR_JA_DECLARADO, simbolo.getLexema());
            }

            casaToken(tabelaSimbolo.ID);
            casaToken(tabelaSimbolo.IGUAL);
            if (simbolo.getToken() == tabelaSimbolo.SUBTRACAO) {
                casaToken(tabelaSimbolo.SUBTRACAO);
            }
            tabelaSimbolo.buscarSimbolo(auxCONST.lexema).tipo = simbolo.tipo;
            casaToken(tabelaSimbolo.VALORCONST);
            casaToken(tabelaSimbolo.PONTO_VIRGULA);
        }


        auxID = simbolo;

        if (simbolo.getToken() == tabelaSimbolo.ID) {


            if (auxID.classe == "")//semantica (7)
            {
                tabelaSimbolo.buscarSimbolo(auxID.lexema).classe = auxD.classe;
                if (auxID.classe != "const") {
                    tabelaSimbolo.buscarSimbolo(auxID.lexema).tipo = auxD.tipo;

                }

            } else {
                MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.IDENTIFICADOR_JA_DECLARADO, simbolo.getLexema());
            }


            casaToken(tabelaSimbolo.ID);


            if (simbolo.getToken() == tabelaSimbolo.IGUAL) {
                casaToken(tabelaSimbolo.IGUAL);
                if (simbolo.getToken() == tabelaSimbolo.SUBTRACAO) {
                    casaToken(tabelaSimbolo.SUBTRACAO);
                }
                auxCONST = simbolo;
                if (auxCONST.tipo == auxD.tipo) {//semantica (9)
                    casaToken(tabelaSimbolo.VALORCONST);

                } else {
                    MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, null);
                }


            }
            while (simbolo.getToken() == tabelaSimbolo.VIRGULA) {

                casaToken(tabelaSimbolo.VIRGULA);
                auxID = simbolo;
                if (auxID.classe == "")//semantica (7)
                {
                    tabelaSimbolo.buscarSimbolo(auxID.lexema).classe = auxD.classe;
                    if (auxID.classe != "const") {
                        tabelaSimbolo.buscarSimbolo(auxID.lexema).tipo = auxD.tipo;
                    }

                } else {
                    MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.IDENTIFICADOR_JA_DECLARADO, simbolo.getLexema());
                }

                tabelaSimbolo.mostrarTabelaDeSimbolo();
                casaToken(tabelaSimbolo.ID);

                if (simbolo.getToken() == tabelaSimbolo.IGUAL) {
                    casaToken(tabelaSimbolo.IGUAL);
                    if (simbolo.getToken() == tabelaSimbolo.SUBTRACAO) {
                        casaToken(tabelaSimbolo.SUBTRACAO);
                    }
                    auxCONST = simbolo;
                    if (auxCONST.tipo == auxD.tipo) {//semantica (9)
                        casaToken(tabelaSimbolo.VALORCONST);
                    } else {
                        MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, null);
                    }


                }
            }
            casaToken(tabelaSimbolo.PONTO_VIRGULA);
        }
    }


    //  C -> id = EXP;| while ‘(‘ EXP ’)’ W | if ‘(‘EXP’)’ then ( C [else (W) ] | begin {C} end [else (W) ] )  | ; | readln’(‘  id  ’)’; | (write | writeln)’(‘ EXP{,EXP} ’)’;

    void C() {

        Simbolo auxC = new Simbolo(), auxID = new Simbolo(), auxCONST = new Simbolo(), auxEXP = new Simbolo(),
        auxReadLN =  new Simbolo();

        auxID = simbolo;

        if (simbolo.getToken() == tabelaSimbolo.ID) {
            if (auxID.classe == "") {
                MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.IDENTIFICADOR_NAO_DECLARADO, simbolo.getLexema());
            } else if (auxID.classe == "const") {
                MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.CLASSE_IDENTIFICADOR_INCOMPATIVEL, simbolo.getLexema());
            }
            casaToken(tabelaSimbolo.ID);
            casaToken(tabelaSimbolo.IGUAL);


            auxEXP.tipo = EXP().tipo;

            if (auxID.tipo != auxEXP.tipo) {
                MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, null);
            }
            casaToken(tabelaSimbolo.PONTO_VIRGULA);
        } else if (simbolo.getToken() == tabelaSimbolo.WHILE) {
            casaToken(tabelaSimbolo.WHILE);
            casaToken(tabelaSimbolo.A_PARENTESES);
            auxEXP = EXP();
            if (auxEXP.tipo != "boolean") {
                MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, null);
            }
            casaToken(tabelaSimbolo.F_PARENTESES);
            W();
        } else if (simbolo.getToken() == tabelaSimbolo.IF) {
            casaToken(tabelaSimbolo.IF);
            casaToken(tabelaSimbolo.A_PARENTESES);
            auxEXP = EXP();
            if (auxEXP.tipo != "boolean") {
                MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, null);
            }
            casaToken(tabelaSimbolo.F_PARENTESES);
            casaToken(tabelaSimbolo.THEN);
            //VERIFICA SE CHAMA C();
            if (simbolo.getToken() == tabelaSimbolo.ID || simbolo.getToken() == tabelaSimbolo.WHILE || simbolo.getToken() == tabelaSimbolo.IF || simbolo.getToken() == tabelaSimbolo.PONTO_VIRGULA ||
                    simbolo.getToken() == tabelaSimbolo.READ_LINE || simbolo.getToken() == tabelaSimbolo.WRITE || simbolo.getToken() == tabelaSimbolo.WRITE_LINE) {
                C();
                if (simbolo.getToken() == tabelaSimbolo.ELSE) {
                    casaToken(tabelaSimbolo.ELSE);
                    W();
                }
            } else if (simbolo.getToken() == tabelaSimbolo.BEGIN) {
                casaToken(tabelaSimbolo.BEGIN);
                while (simbolo.getToken() == tabelaSimbolo.ID || simbolo.getToken() == tabelaSimbolo.WHILE
                        || simbolo.getToken() == tabelaSimbolo.IF || simbolo.getToken() == tabelaSimbolo.PONTO_VIRGULA
                        || simbolo.getToken() == tabelaSimbolo.READ_LINE || simbolo.getToken() == tabelaSimbolo.WRITE
                        || simbolo.getToken() == tabelaSimbolo.WRITE_LINE) {
                    C();
                }
                casaToken(tabelaSimbolo.END);
                if (simbolo.getToken() == tabelaSimbolo.ELSE) {
                    casaToken(tabelaSimbolo.ELSE);
                    W();
                }
            }
        } else if (simbolo.getToken() == tabelaSimbolo.PONTO_VIRGULA) {
            casaToken(tabelaSimbolo.PONTO_VIRGULA);
        } else if (simbolo.getToken() == tabelaSimbolo.READ_LINE) {
            casaToken(tabelaSimbolo.READ_LINE);
            casaToken(tabelaSimbolo.A_PARENTESES);

            auxReadLN = simbolo;

            if (auxReadLN.classe == "") {
                MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.IDENTIFICADOR_NAO_DECLARADO, simbolo.getLexema());
            }else if(auxReadLN.classe == "const"){
                MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.CLASSE_IDENTIFICADOR_INCOMPATIVEL, simbolo.getLexema());
            }else if(auxReadLN.tipo == "boolean") {
                MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, null);
            }
            casaToken(tabelaSimbolo.ID);
            casaToken(tabelaSimbolo.F_PARENTESES);
            casaToken(tabelaSimbolo.PONTO_VIRGULA);
        } else if (simbolo.getToken() == tabelaSimbolo.WRITE || simbolo.getToken() == tabelaSimbolo.WRITE_LINE) {
            if (simbolo.getToken() == tabelaSimbolo.WRITE) {
                casaToken(tabelaSimbolo.WRITE);
            } else {
                casaToken(tabelaSimbolo.WRITE_LINE);
            }
            casaToken(tabelaSimbolo.A_PARENTESES);
            EXP();
            while (simbolo.getToken() == tabelaSimbolo.VIRGULA) {
                casaToken(tabelaSimbolo.VIRGULA);
                EXP();
            }
            casaToken(tabelaSimbolo.F_PARENTESES);
            casaToken(tabelaSimbolo.PONTO_VIRGULA);
        }

    }

    void W() {
        if (simbolo.getToken() == tabelaSimbolo.BEGIN) {
            casaToken(tabelaSimbolo.BEGIN);
            while (simbolo.getToken() == tabelaSimbolo.ID ||
                    simbolo.getToken() == tabelaSimbolo.WHILE ||
                    simbolo.getToken() == tabelaSimbolo.IF ||
                    simbolo.getToken() == tabelaSimbolo.PONTO_VIRGULA ||
                    simbolo.getToken() == tabelaSimbolo.READ_LINE ||
                    simbolo.getToken() == tabelaSimbolo.WRITE ||
                    simbolo.getToken() == tabelaSimbolo.WRITE_LINE) {
                C();
            }
            casaToken(tabelaSimbolo.END);
        } else {
            C();
        }
    }

    Simbolo EXP() {
        // == != < > <= >=
        Simbolo auxExps = new Simbolo(), auxExp = new Simbolo();// semantica(24)
        int op = 0;
        auxExps.tipo = EXPS().tipo;
        auxExp.tipo = auxExps.tipo;

        if (simbolo.getToken() == tabelaSimbolo.IGUAL_IGUAL || simbolo.getToken() == tabelaSimbolo.DIFERENTE || simbolo.getToken() == tabelaSimbolo.MENOR ||
                simbolo.getToken() == tabelaSimbolo.MAIOR || simbolo.getToken() == tabelaSimbolo.MENORIGUAL || simbolo.getToken() == tabelaSimbolo.MAIORIGUAL) {
                op = 0;
            if (simbolo.getToken() == tabelaSimbolo.IGUAL_IGUAL) {
                op = 1;
                casaToken(tabelaSimbolo.IGUAL_IGUAL);
            } else if (simbolo.getToken() == tabelaSimbolo.DIFERENTE) {
                op = 2;
                casaToken(tabelaSimbolo.DIFERENTE);
            } else if (simbolo.getToken() == tabelaSimbolo.MENOR) {
                op = 3;
                casaToken(tabelaSimbolo.MENOR);
            } else if (simbolo.getToken() == tabelaSimbolo.MAIOR) {
                op = 4;
                casaToken(tabelaSimbolo.MAIOR);
            } else if (simbolo.getToken() == tabelaSimbolo.MENORIGUAL) {
                op = 5;
                casaToken(tabelaSimbolo.MENORIGUAL);
            } else {
                op = 6;
                casaToken(tabelaSimbolo.MAIORIGUAL);
            }
            Simbolo auxExps1 = new Simbolo();
            auxExps1.tipo = EXPS().tipo;

            //  System.out.println(auxExps.tipo+"tipo1" + auxExps1.tipo+"tipo2");
            // semantica (25)

             if (op == 1) {
                 if (auxExps.tipo == "string" && auxExps1.tipo == "string"
                         || auxExps.tipo == "integer" && auxExps1.tipo == "byte"
                         || auxExps.tipo == "byte" && auxExps1.tipo == "integer"
                         || auxExps.tipo == "integer" && auxExps1.tipo == "integer"
                         || auxExps.tipo == "byte" && auxExps1.tipo == "byte"
                         || auxExps.tipo == "boolean" && auxExps1.tipo == "boolean") {
                     auxExp.tipo = "boolean";
                 } else {
                     MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, null);
                 }
             }else{
                 if ( auxExps.tipo == "integer" && auxExps1.tipo == "byte"
                         || auxExps.tipo == "byte" && auxExps1.tipo == "integer"
                         || auxExps.tipo == "integer" && auxExps1.tipo == "integer"
                         || auxExps.tipo == "byte" && auxExps1.tipo == "byte"
                         || auxExps.tipo == "boolean" && auxExps1.tipo == "boolean") {
                     auxExp.tipo = "boolean";
                 } else {
                     MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, null);
                 }
             }
        }
        return auxExp;
    }

    Simbolo EXPS() {
        boolean sinalMenos = false, operadormenos =  false;
        Simbolo auxEXPS = new Simbolo(), auxT = new Simbolo(), auxT1 = new Simbolo();
        int op = 0;
        // + - or
        if (simbolo.getToken() == tabelaSimbolo.SOMA) {
            sinalMenos = false;
            casaToken(tabelaSimbolo.SOMA);
        } else if (simbolo.getToken() == tabelaSimbolo.SUBTRACAO) {
            sinalMenos = true;
            casaToken(tabelaSimbolo.SUBTRACAO);
        }
        auxT.tipo = T().tipo;


        if (sinalMenos && (auxT.tipo == "integer" || auxT.tipo == "byte")) {
            auxT.tipo = "integer";
        }// else {
        //     MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, "");
        // }


        auxEXPS.tipo = auxT.tipo;

        while (simbolo.getToken() == tabelaSimbolo.SOMA || simbolo.getToken() == tabelaSimbolo.SUBTRACAO || simbolo.getToken() == tabelaSimbolo.OR) {
            if (simbolo.getToken() == tabelaSimbolo.OR) {
                op = 0;
                casaToken(tabelaSimbolo.OR);
            } else if (simbolo.getToken() == tabelaSimbolo.SOMA) {
                op = 1;
                casaToken(tabelaSimbolo.SOMA);
            } else {
                op = 2;
                casaToken(tabelaSimbolo.SUBTRACAO);
                operadormenos = true;
            }

            auxT1.tipo = T().tipo;
                if (operadormenos && (auxT1.tipo == "integer" || auxT1.tipo == "byte")) {
                   auxT1.tipo = "integer";
               }
             //System.out.println(sinalMenos + auxT.tipo + "1"+operadormenos+ auxT1.tipo + "2");

            if (op == 0) {
                if (auxT.tipo != "boolean" && auxT1.tipo != "boolean") {
                    MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, null);
                } else {
                    auxEXPS.tipo = "boolean";
                }
            } else if (op == 1) {

                if (auxT.tipo.equals("string") && auxT1.tipo.equals("string")) {
                    auxEXPS.tipo = "string";
                } else if ((auxT.tipo.equals("integer") && auxT1.tipo.equals("integer"))
                        || (auxT.tipo.equals("integer") && auxT1.tipo.equals("byte"))
                        || (auxT.tipo.equals("byte") && auxT1.tipo.equals("integer"))) {
                    auxEXPS.tipo = "integer";
                } else if (auxT.tipo.equals("byte") && auxT1.tipo.equals("byte")) {
                    auxEXPS.tipo = "byte";
                } else {
                    MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, null);
                }
            } else if (op == 2) {
                if (auxT.tipo.equals("integer") && auxT1.tipo.equals("integer")
                        || auxT.tipo.equals("integer") && auxT1.tipo.equals("byte")
                        || auxT.tipo.equals("byte") && auxT1.tipo.equals("integer")) {
                    auxEXPS.tipo = "integer";

                } else if (auxT.tipo.equals("byte") && auxT1.tipo.equals("byte")) {
                    auxEXPS.tipo = "byte";
                } else {
                    MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, null);
                }
            }
        }
        return auxEXPS;
    }

    Simbolo T() {

        Simbolo auxF = new Simbolo(), auxF1 = new Simbolo(), auxT = new Simbolo();

        auxF.tipo = F().tipo;
        auxT.tipo = auxF.tipo;
        int op = 0;
        // * / and


        while (simbolo.getToken() == tabelaSimbolo.MULTIPLICACAO || simbolo.getToken() == tabelaSimbolo.DIVISAO || simbolo.getToken() == tabelaSimbolo.AND) {
            if (simbolo.getToken() == tabelaSimbolo.MULTIPLICACAO) {
                op = 0;
                casaToken(tabelaSimbolo.MULTIPLICACAO);
            } else if (simbolo.getToken() == tabelaSimbolo.DIVISAO) {
                op = 1;
                casaToken(tabelaSimbolo.DIVISAO);
            } else {
                op = 2;
                casaToken(tabelaSimbolo.AND);
            }
            auxF1.tipo = F().tipo;
            if (op == 0) {
                if (auxF.tipo == "integer" && auxF1.tipo == "integer"
                        || auxF.tipo == "byte" && auxF1.tipo == "integer"
                        || auxF.tipo == "integer" && auxF1.tipo == "byte"
                        || auxF.tipo == "byte" && auxF1.tipo == "byte") {
                    auxT.tipo = "integer";
                } else {
                    MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, null);
                }
            } else if (op == 1) {
                if (auxF.tipo == "integer" && auxF1.tipo == "integer"
                        || auxF.tipo == "byte" && auxF1.tipo == "integer"
                        || auxF.tipo == "integer" && auxF1.tipo == "byte"
                        || auxF.tipo == "byte" && auxF1.tipo == "byte") {
                    auxT.tipo = "integer";
                } else {
                    MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, null);
                }
            } else if (op == 2) {
                if (auxF.tipo == "boolean" && auxF1.tipo == "boolean") {
                    auxT.tipo = "boolean";
                } else {
                    MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, null);
                }
            }
        }
        return auxT;
    }

    Simbolo F() {
        Simbolo auxF = new Simbolo(), auxID = new Simbolo(), auxConst = new Simbolo(), auxF1 = new Simbolo();
        // exp id const notF
        if (simbolo.getToken() == tabelaSimbolo.A_PARENTESES) {
            casaToken(tabelaSimbolo.A_PARENTESES);
            auxF.tipo = EXP().tipo;
            casaToken(tabelaSimbolo.F_PARENTESES);
        } else if (simbolo.getToken() == tabelaSimbolo.ID) {

            auxID = simbolo;
            if (auxID.classe == "") {
                MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.IDENTIFICADOR_NAO_DECLARADO, simbolo.getLexema());

            } else {
                auxF.tipo = auxID.tipo;
            }
            casaToken(tabelaSimbolo.ID);
        } else if (simbolo.getToken() == tabelaSimbolo.VALORCONST) {
            auxConst = simbolo;
            auxF.tipo = auxConst.tipo;
            casaToken(tabelaSimbolo.VALORCONST);
        } else {
            casaToken(tabelaSimbolo.NOT);
            auxF1.tipo = F().tipo;
            if (auxF1.tipo != "boolean") {
                MensagemDeErro.Imprimir(lexico.linha, MensagemDeErro.TIPOS_INCOMPATIVEIS, null);
            }

        }
        return auxF;

    }

}
