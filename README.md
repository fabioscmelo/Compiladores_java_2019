# Compilador
Trabalho de compiladores PUC-MG desenvolvido em Java.

Grupo:

[Fabio Melo](https://github.com/fabioscmelo)

[Gustavo Lescowicz Kotarsky](https://github.com/gustavokotarsky)

|   Parte|   Valor|  Nota|
|-|-|-|
|   Analise Léxica/Sintática| 10| 10|
|   Analise Semântica|  20| 20|


Automato:

<img src="https://github.com/gustavokotarsky/compiladores/blob/master/Automato.jpg?raw=true">

Gramática:

 - __S__-> {D}+ main {C}+end
 - __D__-> (integer | boolean | byte | string) id [= [-]v_const] {,id [= [-]v_const]}; |
 const id = [-]v_const; 
 - __C__-> id = EXP;|
while ‘(‘ EXP ’)’ W |
if ‘(‘EXP’)’ then ( C [else (W) ] | begin {C} end [else (W) ] ) |
; |
readln’(‘ id ’)’; |
(write | writeln)’(‘ EXP{,EXP} ’)’;
 - __W__-> begin {C} end | C


 - __EXP__-> EXPS [(==|!=|<|>|<=|>=) EXPS] 
 - __EXPS__-> [+|-] T {(+|-|or) T}
 - __T__-> F {(*|and|/) F}
 - __F__-> ‘(‘ EXP ’)’| id | v_const | not F
