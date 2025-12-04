package codigo;
import java_cup.runtime.Symbol;
%%
%class LexerCup
%cup
%line
%column

%{
    private Symbol symbol(int type){
        return new Symbol(type, yyline, yycolumn);
    }
    
    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline, yycolumn, value);
    }
    
%}

GUION_B = _  
LETRA = [a-zA-Z_]+
DIGITO = [0-9]
NUMERO = {DIGITO}+
ID = ({LETRA}|{GUION_B}{LETRA})({LETRA}|{DIGITO})*
CADENA = \"([^\"\\]|\\.)*\"

%%

"compilar" {return symbol(sym.COMPILAR);}
"seccion" {return symbol(sym.SECCION);}
"analisis_lexico" {return symbol(sym.ANALISIS_LEXICO);}
"analisis_sintactico" { return symbol(sym.ANALISIS_SINTACTICO); }
"principal" { return symbol(sym.PRINCIPAL); }
"si" { return symbol(sym.SI); }
"sino" { return symbol(sym.SINO); }
"fin_si" { return symbol(sym.FIN_SI); }
"mientras" { return symbol(sym.MIENTRAS); }
"fin_mientras" { return symbol(sym.FIN_MIENTRAS); }
"ignorar" { return symbol(sym.IGNORAR); }
"patron" { return symbol(sym.PATRON); }
"token" { return symbol(sym.TOKEN); }
"terminales" { return symbol(sym.TERMINALES); }
"no_terminales" { return symbol(sym.NO_TERMINALES); }
"inicio" { return symbol(sym.INICIO); }
"regla" { return symbol(sym.REGLA); }
"vacio" { return symbol(sym.VACIO); }
"entrada" { return symbol(sym.ENTRADA); }
"tokens" { return symbol(sym.TOKENS); }
"arbol" { return symbol(sym.ARBOL); }
"imprimir" { return symbol(sym.IMPRIMIR); }
"dibujar" { return symbol(sym.DIBUJAR); }
"cargar_archivo" { return symbol(sym.CARGAR_ARCHIVO); }
"linea" { return symbol(sym.LINEA); }
"columna" { return symbol(sym.COLUMNA); }
"verificar_errores" { return symbol(sym.VERIFICAR_ERRORES); }
"ver_errores" { return symbol(sym.VER_ERRORES); }
"limpiar_errores" { return symbol(sym.LIMPIAR_ERRORES); }
"definir_alfabeto" { return symbol(sym.DEFINIR_ALFABETO); }
"conjunto" { return symbol(sym.CONJUNTO); }
"definir_tokens" { return symbol(sym.DEFINIR_TOKENS); }
"palabras_reservadas" { return symbol(sym.PALABRAS_RESERVADAS); }
"ejecutar_automata" { return symbol(sym.EJECUTAR_AUTOMATA); }
"estados" { return symbol(sym.ESTADOS); }
"estado_inicial" { return symbol(sym.ESTADO_INICIAL); }
"estados_finales" { return symbol(sym.ESTADOS_FINALES); }
"transiciones" { return symbol(sym.TRANSICIONES); }
"otro" { return symbol(sym.OTRO); }
"tabla_simbolos" { return symbol(sym.TABLA_SIMBOLOS); }
"campo" {return symbol(sym.CAMPO);}
"estructura_tabla" {return symbol(sym.ESTRUCTURA_TABLA);}
"tabla" {return symbol(sym.TABLA);}
"generar_TablaSimbolos" {return symbol(sym.GENERAR_TS);}
"construir_tabla" {return symbol(sym.CONSTRUIR_TABLA);}
"ejecutarProg" {return symbol(sym.EJECUTARPROG);}

"=>" {{ return symbol(sym.OPE_TRANSICION); }}
"=" { return symbol(sym.OPE_ASIGNACION); }
"+"|"-"|"*"|"/" { return symbol(sym.OPE_ARITMETICO); }
"=="|"!="|"<"|">"|"<="|">=" { return symbol(sym.OPE_RELACIONAL); }
"&&"|"||"|"!" { return symbol(sym.OPE_LOGICO); }

"{" { return symbol(sym.LLAVE_ABRE); }
"}" { return symbol(sym.LLAVE_CIERRA); }
"(" { return symbol(sym.PAREN_ABRE); }
")" { return symbol(sym.PAREN_CIERRA); }
"[" { return symbol(sym.CORCHETE_ABRE); }
"]" { return symbol(sym.CORCHETE_CIERRA); }
"," { return symbol(sym.COMA); }
":" { return symbol(sym.DOS_PUNTOS); }
";" { return symbol(sym.PUNTO_COMA); }

{CADENA} { return symbol(sym.CADENA, yytext()); }
{NUMERO} { return symbol(sym.NUMERO, Integer.parseInt(yytext())); }
/* Identificadores */
{ID} { return symbol(sym.ID, yytext()); }
/* Ignorar espacios y comentarios */
[ \t\r\n]+ { /* ignorar */ }
"#".* { /* comentario */ }
