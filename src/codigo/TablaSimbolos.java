/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codigo;
import java.util.*;

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
    
    // <-- ¡YA BORRAMOS LAS PALABRAS RESERVADAS DE AQUÍ!

    public Map<String, EntradaIdentificador> getIdentificadores() {
        // Devolvemos una copia para que la GUI no pueda modificar la tabla original
        return java.util.Collections.unmodifiableMap(identificadores);
    }
    
    // <-- ¡YA BORRAMOS EL MÉTODO esReservada() DE AQUÍ!
    
    public void registrarIdentificador(String nombre, int linea, int columna) {
        if (nombre == null || nombre.isEmpty()) return;
        
        // ¡BORRAMOS ESTA LÍNEA! -> if (esReservada(nombre)) return;
        // Ya no es necesaria, porque el Lexer hace esta revisión ANTES de llamar este método.

        if (!identificadores.containsKey(nombre)) {
            identificadores.put(nombre, new EntradaIdentificador(nombre, linea, columna));
        }
    }
    
    public EntradaIdentificador buscar(String nombre) {
        return identificadores.get(nombre);
    }
    
}