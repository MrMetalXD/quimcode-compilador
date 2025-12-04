/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codigo;

import java.io.File;

/**
 *
 * @author alanc
 */
public class EjecutarLexer {
    public static void main(String[] args) {
        // Cambiar la ruta de acuerdo a su equipo
        String ruta = "C:/Users/alanc/OneDrive/Documents/Tec/11vo Semestre/Lenguajes y Automatas 1/U4/quimcode-compilador/src/codigo/Lexer.flex";
        generarLexer(ruta);
    }
    
    public static void generarLexer(String ruta){
        File archivo = new File(ruta);
        JFlex.Main.generate(archivo);
    }
    
}
