package codigo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*"Tabla Fija"*/
public class TablaPalabrasReservadas {
    private static final Set<String> PALABRAS_RESERVADAS = new HashSet<>(Arrays.asList(
            "SI","SINO","FIN_SI",
            "MIENTRAS","FIN_MIENTRAS",
            "PARA","FIN_PARA","REPETIR","VECES","FIN_REPETIR",
            "ESPERAR","FUNCION","RETORNAR","IMPRIMIR","LEER","ALEATORIO",
            "EXPERIMENTO","FIN_EXPERIMENTO","BLOQUE","FIN_BLOQUE",
            "IMPORTAR","PAQUETE","USAR","CONSTANTE","VARIABLE",
            "ELEMENTO","COMPUESTO","MEZCLA","CANTIDAD","MOLES","GRAMOS",
            "LITROS","ESTADO","SOLIDO","LIQUIDO","GAS","ACUOSO","PUREZA",
            "CONCENTRACION","MOLARIDAD","CREAR","ELIMINAR","LIMPIAR","COMBINAR","AGREGAR","DISOLVER",
            "PRECIPITAR","EVAPORAR","DESTILAR","FILTRAR","TITULAR",
            "USAR_CATALIZADOR","AJUSTAR_TEMPERATURA","AJUSTAR_PRESION",
            "AGITAR","CALENTAR","ENFRIAR","ANOTAR",
            "GENERAR_REPORTE","EXPORTAR","GUARDAR","CARGAR",
            "EXPLICAR_REACCION","ADVERTIR","RUBRICA","PUNTUACION",
            "INFO","MASA_MOLAR","BALANCEAR","TIPO_REACCION",
            "ENERGIA_REACCION","ENTALPIA","ENTROPIA","GIBBS",
            "PREDICIR_PRODUCTO","EQUILIBRIO","PKA","PH","ESTEQUIOMETRIA",
            "REACTIVO_LIMITANTE",
            "NUMERO","CADENA","BOOLEANO","LISTA","MAPA",
            "VERDADERO","FALSO","NULO"
    ));
    
    /**
     * Revisa si un lexema (convertido a mayúsculas) existe en la tabla fija.
     * @param lexema El texto a revisar.
     * @return true si es una palabra reservada, false en otro caso.
     */
    public static boolean esReservada(String lexema) {
        if (lexema == null) return false;
        return PALABRAS_RESERVADAS.contains(lexema.toUpperCase());
    }
    /**
     * Devuelve una vista de solo lectura del Set de palabras reservadas.
     * @return un Set con las palabras.
     */
    public static java.util.Set<String> getPalabrasReservadas() {
        return java.util.Collections.unmodifiableSet(PALABRAS_RESERVADAS);
    }
}
