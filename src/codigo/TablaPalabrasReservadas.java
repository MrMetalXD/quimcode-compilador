package codigo;

import java.util.Arrays;
import static java.util.Collections.*;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/*"Tabla Fija"*/
public class TablaPalabrasReservadas {
   private static final Map<String, String> PALABRAS_RESERVADAS = new LinkedHashMap<>();

    static {
        registrar("si", "PALABRA_RESERVADA");
        registrar("sino", "PALABRA_RESERVADA");
        registrar("fin_si", "PALABRA_RESERVADA");
        registrar("mientras", "PALABRA_RESERVADA");
        registrar("fin_mientras", "PALABRA_RESERVADA");
        
        registrar("principal", "PALABRA_RESERVADA");
        registrar("analisis_lexico", "PALABRA_RESERVADA");
        registrar("analisis_sintactico", "PALABRA_RESERVADA");
        
        registrar("ignorar", "PALABRA_RESERVADA");
        registrar("patron", "PALABRA_RESERVADA");
        registrar("token", "PALABRA_RESERVADA");
        
        registrar("regla", "PALABRA_RESERVADA");
        registrar("inicio", "PALABRA_RESERVADA");
        registrar("vacio", "PALABRA_RESERVADA");
        registrar("terminales", "PALABRA_RESERVADA");
        registrar("no_terminales", "PALABRA_RESERVADA");
        
        registrar("entrada", "PALABRA_RESERVADA");
        registrar("imprimir", "PALABRA_RESERVADA");
        registrar("arbol", "PALABRA_RESERVADA");
        registrar("tokens", "PALABRA_RESERVADA");
        registrar("dibujar", "PALABRA_RESERVADA");
        registrar("cargar_archivo", "PALABRA_RESERVADA");
        registrar("verificar_errores", "PALABRA_RESERVADA");
        registrar("ver_errores", "PALABRA_RESERVADA");
        registrar("limpiar_errores", "PALABRA_RESERVADA");
        registrar("linea", "PALABRA_RESERVADA");
        registrar("columna", "PALABRA_RESERVADA");
        registrar("compilar", "PALABRA_RESERVADA");
        registrar("seccion", "PALABRA_RESERVADA");
        
        registrar("+", "OPE_ARITMETICO");
        registrar("-", "OPE_ARITMETICO");
        registrar("*", "OPE_ARITMETICO");
        registrar("/", "OPE_ARITMETICO");
        
        registrar("=", "OPE_ASIGNACION");
        
        registrar("==", "OPE_RELACIONAL");
        registrar("!=", "OPE_RELACIONAL");
        registrar("<", "OPE_RELACIONAL");
        registrar(">", "OPE_RELACIONAL");
        registrar("<=", "OPE_RELACIONAL");
        registrar(">=", "OPE_RELACIONAL");
        
        registrar("&&", "OPE_LOGICO");
        registrar("||", "OPE_LOGICO");
        registrar("!", "OPE_LOGICO");
        
        registrar("(", "DELIMITADOR");
        registrar(")", "DELIMITADOR");
        registrar("{", "DELIMITADOR");
        registrar("}", "DELIMITADOR");
        registrar(";", "DELIMITADOR");
        registrar(",", "DELIMITADOR");
        registrar(":", "DELIMITADOR");
        registrar("|", "DELIMITADOR");
    }

    private static void registrar(String lexema, String componente) {
        PALABRAS_RESERVADAS.put(lexema, componente);
    }

    /*
        Revisa si un lexema existe en la tabla fija.
    */
    public static boolean esReservada(String lexema) {
        if (lexema == null) return false;
        return PALABRAS_RESERVADAS.containsKey(lexema);
    }

    // Devuelve solo los lexemas (como antes), para no romper el resto del código
    public static Set<String> getPalabrasReservadas() {
        return unmodifiableSet(PALABRAS_RESERVADAS.keySet());
    }

    //Devuelve el componente léxico de una palabra reservada
    public static String getComponente(String lexema) {
        return PALABRAS_RESERVADAS.get(lexema);
    }
}
