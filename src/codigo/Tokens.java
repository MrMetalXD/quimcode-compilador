/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package codigo;

/**
 *
 * @author alanc
 */

public class Tokens {
    public String lexema;
    public String componente;
    public int linea;
    public int columna;
    
    public Tokens(String lexema, String componente, int linea, int columna){
        this.lexema = lexema;
        this.componente = componente;
        this.linea = linea;
        this.columna = columna;
    }
    
    public String getLexema(){
        return lexema;
    }
    
    public String getComponente(){
        return componente;
    }
    
    public int getLinea(){
        return linea;
    }
    
    public int getColumna() {
        return columna;
    }
}
/*
public enum Tokens {
    SI,
    SINO,
    FIN_SI,
    MIENTRAS,
    FIN_MIENTRAS,
    PARA,
    FIN_PARA,
    REPETIR,
    VECES,
    FIN_REPETIR,
    ESPERAR,
    FUNCION,
    RETORNAR,
    IMPRIMIR,
    LEER,
    ALEATORIO,
    EXPERIMENTO,
    BLOQUE,
    FIN_BLOQUE,
    IMPORTAR,
    PAQUETE,
    USAR,
    CONSTANTE,
    VARIABLE,
    ELEMENTO,
    COMPUESTO,
    MEZCLA,
    CANTIDAD,
    MOLES,
    GRAMOS,
    LITROS,
    ESTADO,
    SOLIDO,
    LIQUIDO,
    GAS,
    ACUOSO,
    PUREZA,
    CONCENTRACION,
    MOLARIDAD,
    CREAR,
    ELEMINAR,
    LIMPIAR,
    COMBINAR,
    AGREGAR,
    DISOLVER,
    PRECIPITAR,
    EVAPORAR,
    DESTILAR,
    FILTRAR,
    TITULAR,
    USAR_CATALIZADOR,
    AJUSTAR_TEMPERATURA,
    AJUSTAR_PRESION,
    AGITAR,
    CALENTAR,
    ENFRIAR,
    ANOTAR,
    GENERAR_REPORTE,
    EXPORTAR,
    GUARDAR,
    CARGAR,
    EXPLICAR_REACCION,
    ADVERTIR,
    RUBRICA,
    PUNTUACION,
    INFO,
    MASA_MOLAR,
    BALANCEAR,
    TIPO_REACCION,
    ENERGIA_REACCION,
    ENTALPIA,
    ENTROPIA,
    GIBBS,
    PREDECIR_PRODUCTO,
    EQUILIBRIO,
    PKA,
    PH,
    ESTEQUIOMETRIA,
    REACTIVO_LIMIANTE,
    TIPO_NUMERO,
    TIPO_CADENA,
    TIPO_BOOLEANO, 
    TIPO_LISTA,
    TIPO_MAPA,
    VERDADERO,
    FALSO,
    NULO,
    OP_IGUAL,
    OP_DIF,
    OP_MENOR_IGUAL,
    OP_MAYOR_IGUAL,
    OP_MENOR,
    OP_MAYOR,
    OP_ASING,
    OP_AND,
    OP_OR,
    OP_NOT,
    PAREN_ABRE,
    PAREN_CIERRA,
    LLAVE_ABRE,
    LLAVE_CIERRA,
    CORCHETE_ABRE,
    ORCHETE_CIERRA,
    COMA,
    DOS_PUNTOS,
    PUNTO_COMA,
    LIT_CADENA,
    LIT_NUM_CON_UNIDAD,
    LIT_NUMERO,
    ERROR,
    ERROR_CADENA_NO_CERRADA,
    ERROR_CARACTER_INVALIDO,
    ERROR_IDENTIFICADOR_ES_PALABRA_RESERVADA
}

*/
