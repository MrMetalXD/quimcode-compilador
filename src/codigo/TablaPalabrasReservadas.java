package codigo;

import java.util.Arrays;
import static java.util.Collections.*;
import java.util.HashSet;
import java.util.Set;

/*"Tabla Fija"*/
public class TablaPalabrasReservadas {
    private static final Set<String> PALABRAS_RESERVADAS = new HashSet<>(Arrays.asList(
           "si", "sino", "sino_si","fin_si","mientras","hacer",
            "principal", "fin_principal", "elemento", "compuesto","moles","peso",
            "estado", "solido", "liquido", "litro", "gaseoso", "acuoso", "reaccion",
            "agregar", "eliminar", "limpiar", "mezclar", "disolver", "precipitar","evaporar",
            "destilar", "medir_ph", "medir_temp", "medir_presion", "agitar", "calentar",
            "enfriar", "neutralizar", "balancear_reaccion", "ejecturar_reaccion", "mostrar","mostrar_info",
            "enviar_alerta","imprimir"
    ));
    
    /*
        Revisa si un lexema existe en la tabla fija
        Se le pasa un parametro lexema que sera el lexema a revisar
        retorna true si la palabra es reservada
    */
    public static boolean esReservada(String lexema) {
        if(lexema == null) return false;
        return PALABRAS_RESERVADAS.contains(lexema);
    }
   
    // Devuelve el conjunto de palabras reservadas para solo lectura
    public static Set<String> getPalabrasReservadas() {
        return unmodifiableSet(PALABRAS_RESERVADAS);
    }
}
