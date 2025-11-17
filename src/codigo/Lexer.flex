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

LETRAS = [a-zA-Z_]+
ESPACIO = [ \t\r\n]+
DIGITO = [0-9]
ENTERO = {DIGITO}+
DECIMAL = "." {DIGITO}+
EXPRESION = [eE][+-]?{ENTERO}
NUMERO = {ENTERO} ( {DECIMAL} )?

IDENTIFICADOR = {LETRAS}({LETRAS}|{DIGITO})*
CADENA = \"([^\"\\]|\\.)*\"
COMENTARIO = \#.*                          

/* Simbolos comunes en quimica */

UNIDAD_MOL = "mol" /* mol cantidad de sustancia */
UNIDAD_GRAMOS = "g" /* gramos */
UNIDAD_KG = "kg" /* kilogramos */
UNIDAD_L = "L" /* litros */
UNIDAD_ML = "ml" /*mililitros */
UNIDAD_ATM = "atm" /* atmosferas de presion */
UNIDAD_KPA = "kPa" /* kilopascales (presion) */
UNIDAD_BAR = "bar" /* otra unidad de presion */
UNIDAD_K = "K" /* Kelvin */
UNIDAD_C = "C" /* grados Celsius */
UNIDAD_PCTJ = "%" /* porcentaje */
UNIDAD_MOLAR = "M" /* molaridad = mol/L */

LIT_NUMERICA = {NUMERO}({UNIDAD_MOL}|{UNIDAD_GRAMOS}|{UNIDAD_KG}|{UNIDAD_L}|{UNIDAD_ML}|{UNIDAD_ATM}|{UNIDAD_KPA}|{UNIDAD_BAR}|{UNIDAD_K}|{UNIDAD_C}|{UNIDAD_PCTJ}|{UNIDAD_MOLAR})
%%
/* espacios y comentarios */
{ESPACIO}                    { /* ignorar */ }
{COMENTARIO}                 { /* ignorar */ }

"si" { return token(yytext(),"TKN_SI",yyline,yycolumn); }
"sino" { return token(yytext(),"TKN_SINO",yyline,yycolumn); }
"sino_si" { return token(yytext(),"TKN_SINO_SI",yyline,yycolumn); }
"fin_si" { return token(yytext(),"TKN_FIN_SI",yyline,yycolumn); }
"mientras" { return token(yytext(),"TKN_MIENTRAS",yyline,yycolumn); }
"fin_mientras" { return token(yytext(),"TKN_FIN_MIENTRAS",yyline,yycolumn); }
"hacer" { return token(yytext(),"TKN_HACER",yyline,yycolumn); }

"principal" { return token(yytext(),"TKN_PRINCIPAL",yyline,yycolumn); }
"fin_principal" { return token(yytext(),"TKN_FIN_PRINCIPIAL",yyline,yycolumn); }

"elemento" { return token(yytext(),"TKN_ELEMENTO",yyline,yycolumn); }
"compuesto" { return token(yytext(),"TKN_COMPUESTO",yyline,yycolumn); }
"solucion" { return token(yytext(),"TKN_SOLUCION",yyline,yycolumn); }
"moles" { return token(yytext(),"TKN_MOLES",yyline,yycolumn); }
"peso" { return token(yytext(),"TKN_PESO",yyline,yycolumn); }
"estado" { return token(yytext(),"TKN_ESTADO",yyline,yycolumn); }
"solido" { return token(yytext(),"TKN_SOLIDO",yyline,yycolumn); }
"liquido" { return token(yytext(),"TKN_LIQUIDO",yyline,yycolumn); }
"litro" { return token(yytext(),"TKN_LITRO",yyline,yycolumn); }
"gaseoso" { return token(yytext(),"TKN_GASEOSO",yyline,yycolumn); }
"acuoso" { return token(yytext(),"TKN_ACUOSO",yyline,yycolumn); }
"reaccion" { return token(yytext(),"TKN_reaccion",yyline,yycolumn); }

"agregar" { return token(yytext(),"TKN_AGREGAR",yyline,yycolumn); }
"eliminar" { return token(yytext(),"TKN_ELIMINAR",yyline,yycolumn); }
"limpiar" { return token(yytext(),"TKN_LIMPIAR",yyline,yycolumn); }
"mezclar" { return token(yytext(),"TKN_MEZCLAR",yyline,yycolumn); }
"disolver" { return token(yytext(),"TKN_DISOLVER",yyline,yycolumn); }
"precipitar" { return token(yytext(),"TKN_PRECIPITAR",yyline,yycolumn); }
"evaporar" { return token(yytext(),"TKN_EVAPORAR",yyline,yycolumn); }
"destilar" { return token(yytext(),"TKN_DESTILAR",yyline,yycolumn); }
"medir_ph" { return token(yytext(),"TKN_MEDIR_PH",yyline,yycolumn); }
"medir_temp" { return token(yytext(),"TKN_MEDIR_TEMPERATURA",yyline,yycolumn); }
"medir_presion" { return token(yytext(),"TKN_MEDIR_PRESION",yyline,yycolumn); }
"agitar" { return token(yytext(),"TKN_AGITAR",yyline,yycolumn); }
"calentar" { return token(yytext(),"TKN_CALENTAR",yyline,yycolumn); }
"enfriar" { return token(yytext(),"TKN_ENFRIAR",yyline,yycolumn); }
"neutralizar" { return token(yytext(),"TKN_NEUTRALIZAR",yyline,yycolumn); }
"balancear_reaccion" { return token(yytext(),"TKN_BALANCEAR_REACCION",yyline,yycolumn); }
"ejecutar_reaccion" { return token(yytext(),"TKN_EJECUTAR_REACCION",yyline,yycolumn); }
"duracion" {return token(yytext(),"TKN_DURACION",yyline,yycolumn);}
"mostrar" { return token(yytext(),"TKNK_MOSTRAR",yyline,yycolumn); }
"mostrar_info" { return token(yytext(),"TKN_MOSTRAR_INFO",yyline,yycolumn); }
"enviar_alerta" { return token(yytext(),"TKN_ENVIAR_ALERTA",yyline,yycolumn); }
"imprimir" { return token(yytext(),"TKN_IMPRIMIR",yyline,yycolumn); }
 
"+" { return token(yytext(),"OP_MAS",yyline,yycolumn); }
"-" { return token(yytext(),"OP_MENOS",yyline,yycolumn); }
"*" { return token(yytext(),"OP_POR",yyline,yycolumn); }
"/"  { return token(yytext(),"OP_DIV",yyline,yycolumn); }
"==" { return token(yytext(),"OP_IGUAL",yyline,yycolumn); }
"!=" { return token(yytext(),"OP_DIF",yyline,yycolumn); }
"<=" { return token(yytext(),"OP_MENOR_IGUAL",yyline,yycolumn); }
">=" { return token(yytext(),"OP_MAYOR_IGUAL",yyline,yycolumn); }
"<" { return token(yytext(),"OP_MENOR",yyline,yycolumn); }
">" { return token(yytext(),"OP_MAYOR",yyline,yycolumn); }
"=" { return token(yytext(),"OP_ASIGN",yyline,yycolumn); }
"&&" { return token(yytext(),"OP_AND",yyline,yycolumn); }
"|" { return token(yytext(),"OP_OR",yyline,yycolumn); }
"!" { return token(yytext(),"OP_NOT",yyline,yycolumn); }
"(" { return token(yytext(),"PAREN_ABRE",yyline,yycolumn); }
")" { return token(yytext(),"PAREN_CIERRA",yyline,yycolumn); }
"{" { return token(yytext(),"LLAVE_ABRE",yyline,yycolumn); }
"}" { return token(yytext(),"LLAVE_CIERRA",yyline,yycolumn); }
"[" { return token(yytext(),"CORCHETE_ABRE",yyline,yycolumn); }
"]" { return token(yytext(),"CORCHETE_CIERRA",yyline,yycolumn); }
"," { return token(yytext(),"COMA",yyline,yycolumn); }
":" { return token(yytext(),"DOS_PUNTOS",yyline,yycolumn); }
";" { return token(yytext(),"PUNTO_COMA",yyline,yycolumn); }

{CADENA} { return token(yytext(),"CADENA",yyline,yycolumn); }
{LIT_NUMERICA} { return token(yytext(),"LIT_NUMERICA",yyline,yycolumn); }
{NUMERO} { return token(yytext(),"NUMERO",yyline,yycolumn); }

 /* -------- Identificadores -------- */
  {IDENTIFICADOR} {
        // 1. Revisa la "Tabla Fija"
        if (TablaPalabrasReservadas.esReservada(yytext())){
            return token(yytext(),"ERROR_IDENTIFICADOR_ES_PALABRA_RESERVADA",yyline,yycolumn);
        } else {
            // 2. Si no es reservada, la registra en la "Tabla de identificadores"
            tablaSimbolos.registrarIdentificador(yytext(), yyline+1, yycolumn+1);
            return token(yytext(),"IDENTIFICADOR",yyline,yycolumn);
        }             
  }

  /* -------- Errores léxicos comunes -------- */
  \"[^\"\n]* { return token(yytext(),"ERROR_CADENA_NO_CERRADA",yyline,yycolumn); }
  [^\s\w\[\]{}();:,.=<>+\-*/\"ÁÉÍÓÚÜÑáéíóúüñ%] { return token(yytext(),"ERROR_CARACTER_INVALIDO",yyline,yycolumn); }

/* -------- Error de análisis -------- */
. {return token(yytext(), "ERROR", yyline, yycolumn);}
