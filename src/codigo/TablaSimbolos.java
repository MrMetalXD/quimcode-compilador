/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codigo;
import java.util.*;
import static java.util.Collections.unmodifiableMap;

/**
 * Esta es la "Tabla de Símbolos" para Identificadores (variables, elementos, etc.).
 * NO incluye funciones ni palabras reservadas.
 */
public class TablaSimbolos {
    public static class EntradaIdentificador {
        private final String nombre;
        private final int linea;
        private final int columna;
        private String tipo; 
        private int direccionMemoria;
        
        public EntradaIdentificador(String nombre, int linea, int columna){
            this.nombre = nombre;
            this.linea = linea;
            this.columna = columna;
            this.tipo = "desconocido";
            this.direccionMemoria = -1;
        }
        
        public String getNombre(){
            return nombre;
        }
        
        public int getLinea(){
            return linea;
        }
        
        public int getColumna(){
            return columna;
        }
        
        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }
        
        public int getDireccionMemoria() {
            return direccionMemoria;
        }
        public void setDireccionMemoria(int direccionMemoria) {
            this.direccionMemoria = direccionMemoria;
        }
        
        @Override
        public String toString(){
            return nombre + "(" + linea + ", " + columna + ")";
        }
    }
    
    private final Map<String, EntradaIdentificador> identificadores = new LinkedHashMap<>();
    
    // Contador para simular direcciones de memoria
    private int siguienteDireccion = 1000;
    
    private int tamanoPorTipo(String tipo) {
        switch(tipo){
            case "elemento":
            case "compuesto":
            case "solucion":
            default:
                return 4; //bytes por defecto
        }
    }
    
    public Map<String, EntradaIdentificador> getIdentificadores() {
        // Devolvemos una copia para que la GUI no pueda modificar la tabla original
        return unmodifiableMap(identificadores);
    }
    
    
    public void registrarIdentificador(String nombre, int linea, int columna) {
        if (nombre == null || nombre.isEmpty()) return;
        
        if (!identificadores.containsKey(nombre)) {
            identificadores.put(nombre, new EntradaIdentificador(nombre, linea, columna));
        }
    }
    
    public void asignarTipoyDireccion(String nombre, String tipo){
        EntradaIdentificador entrada = identificadores.get(nombre);
        if(entrada == null){
            entrada = new EntradaIdentificador(nombre, -1, -1);
            identificadores.put(nombre, entrada);
        }
        entrada.setTipo(tipo);
        
        if(entrada.getDireccionMemoria() == -1){
            entrada.setDireccionMemoria(siguienteDireccion);
            siguienteDireccion += tamanoPorTipo(tipo);
        }
    }
    
    public EntradaIdentificador buscar(String nombre) {
        return identificadores.get(nombre);
    }
    
}