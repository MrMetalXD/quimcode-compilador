package codigo;
import java.util.*;

/**
 * Esta es la "Tabla de Funciones".
 * Almacena información específica de las funciones
 * declaradas por el usuario.
 */
public class TablaFunciones {

    public static class EntradaFuncion {
        private final String nombre;
        private final int linea; // Línea donde se declara
        private String tipoRetorno;
        private final List<String> tiposParametros; // Lista de tipos de sus parámetros
        
        public EntradaFuncion(String nombre, int linea) {
            this.nombre = nombre;
            this.linea = linea;
            this.tipoRetorno = "nulo"; // Por defecto, hasta que el sintáctico diga otra cosa
            this.tiposParametros = new ArrayList<>();
        }
        
        // --- Getters y Setters ---
        public String getNombre() { return nombre; }
        public int getLinea() { return linea; }
        public String getTipoRetorno() { return tipoRetorno; }
        public void setTipoRetorno(String tipoRetorno) { this.tipoRetorno = tipoRetorno; }
        public List<String> getTiposParametros() { return Collections.unmodifiableList(tiposParametros); }
        
        // Método para que el sintáctico añada parámetros
        public void addParametro(String tipoParam) {
            this.tiposParametros.add(tipoParam);
        }
    }
    
    private final Map<String, EntradaFuncion> funciones = new LinkedHashMap<>();
    
    /**
     * Registra una nueva función.
     */
    public void registrarFuncion(String nombre, int linea) {
        if (nombre == null || nombre.isEmpty()) return;
        if (!funciones.containsKey(nombre)) {
            funciones.put(nombre, new EntradaFuncion(nombre, linea));
        }
    }
    
    /**
     * Busca una función por su nombre.
     */
    public EntradaFuncion buscar(String nombre) {
        return funciones.get(nombre);
    }
    
    /**
     * Devuelve el mapa completo de funciones (para la GUI).
     */
    public Map<String, EntradaFuncion> getFunciones() {
        return Collections.unmodifiableMap(funciones);
    }
}