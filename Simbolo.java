/*
Fabio Silva Campos Melo
Gustavo Lescowicz Kotarsky
*/

public class Simbolo {


    public String lexema = "";
    private byte token;
    public String classe = "";
    public String tipo = "";
    private int tamanho = 0;
    private int endereco;


    public Simbolo(){
        this.token = -1;
        this.tipo = "";
        this.classe = "";

    }

    public Simbolo(String lexema, byte token) {
        this.lexema = lexema;
        this.token = token;
        this.tipo = tipo;
        this.classe = "";

    }

    public Simbolo(String lexema,byte token, String tipo){
        this.token = token;
        this.lexema = lexema;
        this.tipo = tipo;
        this.classe = "";


    }


    public byte getToken(){
        return token;
    }

    public int getEnd(){
        return endereco;
    }

    public String getLexema(){
        return lexema;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setToken(byte token) {
        this.token = token;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public int getEndereco() {
        return endereco;
    }

    public void setEndereco(int endereco) {
        this.endereco = endereco;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }
}
