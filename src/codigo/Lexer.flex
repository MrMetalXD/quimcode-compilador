package codigo;
import static codigo.Tokens.*;

%%
%class Lexer
%type Tokens
%line
%column

%{
    private final TablaSimbolos tablaSimbolos = new TablaSimbolos();
    private final TablaFunciones tablaFunciones = new TablaFunciones();

    public TablaSimbolos getTablaSimbolos() {
        return this.tablaSimbolos;
    }

    public TablaFunciones getTablaFunciones() { 
        return this.tablaFunciones;
    }

    private Tokens token(String lex, String comp, int li, int c){
        return new Tokens(lex, comp, li+1, c+1); 
    }
%}

GUION_B = _   
LETRA = [a-zA-Z_]+
ESPACIO = [ \t\r\n]+
DIGITO = [0-9]
ENTERO = {DIGITO}+
DECIMAL = "." {DIGITO}+
NUMERO = {ENTERO} ( {DECIMAL} )?

IDENTIFICADOR = ({LETRA} | {GUION_B}{LETRA}) ({LETRA} | {DIGITO})*
CADENA = \"([^\"\\]|\\.)*\"
COMENTARIO = \#.*
                       

%%
/* espacios y comentarios */
{ESPACIO}                    { /* ignorar */ }
{COMENTARIO}                 { /* ignorar */ }

"si"|
"sino"|
"fin_si"|
"mientras"|
"fin_mientras"|
"analisis_lexico"|
"analisis_sintactico"|
"principal"| 
"ignorar"|
"token"|
"patron"|
"inicio"|
"regla"|
"vacio"|
"terminales"|
"no_terminales"|
"entrada"|
"imprimir"|
"arbol"|
"tokens"|
"dibujar"|
"cargar_archivo"|
"linea"|
"columna"|
"verificar_errores"|
"ver_errores"|
"limpiar_errores"|
"compilar"|
"seccion" | 
"definir_alfabeto"|
"conjunto"|
"definir_token"
"palabras_reservadas"|
"ejecutar_automata"|
"estados"|
"estado_inicial"|
"estados_finales"|
"transiciones"
"otro"|
"campo"|
"estructura_tabla"|
"tabla"|
"tabla_simbolos"|
"construir_tabla"|
"generar_TablaSimbolos"|
"ejecutarProg" {return token(yytext(),"PALABRA_RESERVADA",yyline,yycolumn); }

"+"|
"*" { return token(yytext(),"OPE_ARITMETICO",yyline,yycolumn); }

"=="|
"!="|
"<="|
">="|
"<"|
">" { return token(yytext(),"OPE_RELACIONAL",yyline,yycolumn); }

"=" { return token(yytext(),"OPE_ASIGNACION",yyline,yycolumn); }
"=>" { return token(yytext(),"OPE_TRANSICION",yyline,yycolumn); }

"&&"|
"||"|
"!" { return token(yytext(),"OPE_LOGICO",yyline,yycolumn); }

"("|
")"|
"{"|
"}"|
"["|
"]"|
"|"| 
";"|
","| 
":" { return token(yytext(),"DELIMITADOR",yyline,yycolumn); }



{CADENA} { return token(yytext(),"CADENA",yyline,yycolumn); }

{NUMERO} { return token(yytext(),"NUMERO",yyline,yycolumn); }

/*   ERRORES DE IDENTIFICADOR    */
({DIGITO}+{LETRA}({LETRA}|{DIGITO})*) | ({GUION_B}({DIGITO}|{GUION_B})({LETRA}|{DIGITO}|{GUION_B})*) | ({GUION_B})  { return token(yytext(), "ERROR_IDENTIFICADOR_INVALIDO", yyline, yycolumn);}
([-+*/=<>!&]){LETRA}({LETRA}|{DIGITO})* { return token(yytext(), "ERROR_LEXICO", yyline, yycolumn);}
/* -------- Identificadores -------- */

  {IDENTIFICADOR} {
        // Revisa la "Tabla Fija"
        if (TablaPalabrasReservadas.esReservada(yytext())){
            return token(yytext(),"ERROR_IDENTIFICADOR_ES_PALABRA_RESERVADA",yyline,yycolumn);
        } else {
            // Si no es reservada, la registra en la "Tabla de identificadores"
            tablaSimbolos.registrarIdentificador(yytext(), yyline+1, yycolumn+1);
            return token(yytext(),"IDENTIFICADOR",yyline,yycolumn);
        }             
  }
 
  /* -------- Errores léxicos comunes -------- */
  \"[^\"\n]* { return token(yytext(),"ERROR_CADENA_NO_CERRADA",yyline,yycolumn); }
  
[^\s\w\[\]{}();:,.=<>+\-*/\"ÁÉÍÓÚÜÑáéíóúüñ%] { return token(yytext(),"ERROR_CARACTER_INVALIDO",yyline,yycolumn); }
({ENTERO}"." ) | ("."{ENTERO}) { return token(yytext(),"ERROR_NUMERO_MALFORMADO",yyline,yycolumn); }

/* -------- Error de análisis -------- */
. {return token(yytext(), "ERROR", yyline, yycolumn);}



