/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codigo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author alanc
 */
public class EjecutarLexer {
    public static void main(String[] args) throws Exception {
        // Cambiar la ruta de acuerdo a su equipo
        String ruta = "C:/Users/alanc/OneDrive/Documents/Tec/11vo Semestre/Lenguajes y Automatas 1/Unidad 4/Proyecto Compilador/compilercode-compilador/src/codigo/Lexer.flex";
        String rutaLexerCup = "C:/Users/alanc/OneDrive/Documents/Tec/11vo Semestre/Lenguajes y Automatas 1/Unidad 4/Proyecto Compilador/compilercode-compilador/src/codigo/LexerCup.flex";
        String[] rutaCup = {"-parser","Sintaxis","C:/Users/alanc/OneDrive/Documents/Tec/11vo Semestre/Lenguajes y Automatas 1/Unidad 4/Proyecto Compilador/compilercode-compilador/src/codigo/Sintaxis.cup"};
        
        generar(ruta,rutaLexerCup,rutaCup);
    }
    
    public static void generar(String ruta, String rutaLexerCup, String[] rutaCup) throws IOException, Exception{
        File archivo = new File(ruta);
        JFlex.Main.generate(archivo);
        
        archivo = new File(rutaLexerCup);
        JFlex.Main.generate(archivo);
        
        java_cup.Main.main(rutaCup);
        
        Path rutaSym = Paths.get("C:/Users/alanc/OneDrive/Documents/Tec/11vo Semestre/Lenguajes y Automatas 1/Unidad 4/Proyecto Compilador/compilercode-compilador/src/codigo/Sym.java");
        
        if(Files.exists(rutaSym)){
            Files.delete(rutaSym);
        }
        
        Files.move(
                Paths.get("C:/Users/alanc/OneDrive/Documents/Tec/11vo Semestre/Lenguajes y Automatas 1/Unidad 4/Proyecto Compilador/compilercode-compilador/sym.java"), 
                Paths.get("C:/Users/alanc/OneDrive/Documents/Tec/11vo Semestre/Lenguajes y Automatas 1/Unidad 4/Proyecto Compilador/compilercode-compilador/src/codigo/sym.java")
        );
        
        Path rutaSintaxis = Paths.get("C:/Users/alanc/OneDrive/Documents/Tec/11vo Semestre/Lenguajes y Automatas 1/Unidad 4/Proyecto Compilador/compilercode-compilador/src/codigo/Sintaxis.java");
        
        if(Files.exists(rutaSintaxis)) {
            Files.delete(rutaSintaxis);
        }
        
        Files.move(Paths.get("C:/Users/alanc/OneDrive/Documents/Tec/11vo Semestre/Lenguajes y Automatas 1/Unidad 4/Proyecto Compilador/compilercode-compilador/Sintaxis.java"), 
                Paths.get("C:/Users/alanc/OneDrive/Documents/Tec/11vo Semestre/Lenguajes y Automatas 1/Unidad 4/Proyecto Compilador/compilercode-compilador/src/codigo/Sintaxis.java"));
    }
    
    
    
}
