
package codigo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.*;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;


public class Compilador extends javax.swing.JFrame {
    // Variable global para saber si el archivo actual ya está guardado
    private java.io.File archivoActual = null;
    private boolean cambiosSinGuardar = false;
    private boolean actualizandoTexto = false;
    public String rute="";
    private TablaSimbolos tablaDeSimbolosGlobal;
    private TablaFunciones tablaDeFuncionesGlobal;
    //Variable para la imagen de fondo
   // FondoPanel fondo = new FondoPanel();

    /**
     * Creates new form VentanaPrincipal
     */
    public Compilador() {
      //  this.setContentPane(fondo);
        initComponents();
        inicializar();
        this.setLocationRelativeTo(null);
    }
    
    private void inicializar(){
        //Codigo para implementar el numero de linea
        NumeroLinea numerolinea = new NumeroLinea(jCode);
        jScrollPane1.setRowHeaderView(numerolinea);
        jCode.setEditable(true);
         
        jCode.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e){
                numerolinea.repaint();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                numerolinea.repaint();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               numerolinea.repaint();
            }
            
        });
    }
    
    public void GuardarArchivo(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar archivo .ccode");

        // Filtro para archivos .quim
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Ccode (*.ccode)", "ccode");
        fileChooser.setFileFilter(filter);

        int seleccion = fileChooser.showSaveDialog(this);

        if (seleccion ==  JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
        // Si el nombre no termina con .quim, se lo agregamos automáticamente
            if (!archivo.getName().toLowerCase().endsWith(".ccode")) {
                archivo = new java.io.File(archivo.getAbsolutePath() + ".ccode");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                //Obtener el contenido del jCode y escribirlo
                writer.write(jCode.getText());
                writer.flush();
                
                JOptionPane.showMessageDialog(this,
                    "Archivo guardado correctamente en:\n" + archivo.getAbsolutePath(),
                "Guardar archivo",JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                "Error al guardar el archivo: " + e.getMessage(),
                "Error",JOptionPane.ERROR_MESSAGE);
            }
       } 
    }
    
    public void NuevoArchivo(){
        if (jCode.getText().trim().isEmpty()) {
            jCode.setText("");
            archivoActual = null;
            cambiosSinGuardar = false;
            return;
        }

        //Si hay cambios sin guardar, preguntar al usuario
        if (cambiosSinGuardar) {
            int opcion = JOptionPane.showConfirmDialog(this,
              "Tienes cambios sin guardar.\n¿Deseas guardarlos antes de crear un nuevo archivo?", "Guardar cambios",
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (opcion == JOptionPane.CANCEL_OPTION) {
                return; // cancelar acción
            } else if (opcion == JOptionPane.YES_OPTION) {
                GuardarArchivo(); // Guardamos antes de limpiar
            }
        }
        this.tablaDeSimbolosGlobal = null;
        this.tablaDeFuncionesGlobal = null;
        // Limpiar el área y reiniciar el estado
        jCode.setText("");
        archivoActual = null;
        cambiosSinGuardar = false;
        jErrores.setText("");
    }
    
    public void AbrirArchivo(){
        JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Abrir archivo .ccode");

        // Solo archivos con extensión .ccode
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Ccode (*.ccode)", "ccode");
        fileChooser.setFileFilter(filter);

        int seleccion = fileChooser.showOpenDialog(this);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
                File archivo = fileChooser.getSelectedFile();

            // Validar la extensión manualmente
                if (!archivo.getName().toLowerCase().endsWith(".ccode")) {
                    JOptionPane.showMessageDialog(this,
                        "El archivo seleccionado no es compatible.\nDebe ser un archivo con extensión .ccode",
                        "Archivo no compatible",
                    JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                    StringBuilder contenido = new StringBuilder();
                    String linea;

                    while ((linea = reader.readLine()) != null) {
                        contenido.append(linea).append("\n");
                    }

                    // Mostrar el contenido del archivo .quim en el editor
                    jCode.setText(contenido.toString());

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al abrir el archivo: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void ejecutarAnalisisLexico(){
        this.tablaDeSimbolosGlobal = null;
        this.tablaDeFuncionesGlobal = null;
        try {
            String codigo = jCode.getText();
            Reader lector = new BufferedReader(new StringReader(codigo));
            Lexer lexer = new Lexer(lector);
            
            if (codigo == null || codigo.trim().isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "No hay codigo para analizar. \nPor favor escribe o carga un programa antes de ejecutar el análisis léxico.",
                        "Sin contenido",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            DefaultTableModel tokens = new DefaultTableModel();
            tokens.addColumn("# Linea");
            tokens.addColumn("Lexema");
            tokens.addColumn("Comp. Léxico");

            StringBuilder errores = new StringBuilder();

            Tokens token;
            while((token = lexer.yylex()) != null){
                int linea = token.getLinea();
                String lexema = token.getLexema();
                String compLexico = token.getComponente();
                
                Object[] fila = new Object[3];
                fila[0] = linea;
                fila[1] = lexema;
                fila[2] = compLexico;
                
                tokens.addRow(fila);

                if (compLexico != null && compLexico.startsWith("ERROR")) {
                    String sugerencia = obtenerSugerenciaError(compLexico, lexema);
                    errores.append(String.format("Error en línea %d -> \"%s\" (%s)%n",linea, lexema, compLexico ));

                    if (sugerencia != null && !sugerencia.isEmpty()) {
                        errores.append("   Sugerencia: ")
                                .append(sugerencia)
                                .append("\n\n");
                    } else {
                        errores.append("\n");
                    }
                }
            }
            this.tablaDeSimbolosGlobal = lexer.getTablaSimbolos();
            this.tablaDeFuncionesGlobal = lexer.getTablaFunciones();
            
            if (errores.length() > 0) {
                jErrores.setText("Se encontraron errores léxicos:\n" + errores.toString());
                return; // salimos
            } else {
                jErrores.setText("\n -------- COMPILACION EXITOSA ------- ");
            }

            // Mostrar los resultados en una ventana aparte
            JTable tablaTokens = new JTable(tokens);
            JScrollPane scroll = new JScrollPane(tablaTokens);
            JDialog ventana = new JDialog(this, "Tabla de Tokens", true);
            ventana.setSize(400, 600);
            ventana.setLocationRelativeTo(this);
            ventana.setLayout(new BorderLayout(10, 10));
            ventana.add(scroll, BorderLayout.CENTER);
            ventana.setVisible(true);

        } catch (Exception e) {
             jErrores.setText("️ Error en el análisis léxico: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String obtenerSugerenciaError(String compLex, String lexema) {
        switch (compLex) {
            case "ERROR_LEXICO":
                // Regla: operador pegado a identificador, p.ej. +abc, -x1, =moles
                return "Se detectó un operador pegado a un identificador. " +
                       "Separa el operador del nombre, por ejemplo:\n" +
                       "   \"" + lexema + "\"  ->  \"" + separarOperadorYIdentificador(lexema) + "\"";

            case "ERROR_IDENTIFICADOR_INVALIDO":
                // Casos: 12ba, _8dato, __, etc.
                return "El identificador no cumple con las reglas establecidas. " +
                       "Asegúrate de que:\n" +
                       "  - No comience con un dígito.\n" +
                       "  - Si empieza con '_', lo siga una letra (no dígitos ni más '_').\n" +
                       "Ejemplos válidos: nombre1, _variable, dato_2";

            case "ERROR_IDENTIFICADOR_ES_PALABRA_RESERVADA":
                return "Estás usando una palabra reservada como identificador. " +
                       "Cambia el nombre, por ejemplo:\n" +
                       "   \"" + lexema + "\"  ->  \"" + lexema + "_id\"";

            case "ERROR_CADENA_NO_CERRADA":
                return "La cadena no está cerrada con comillas dobles. " +
                       "Agrega la comilla de cierre al final";

            case "ERROR_CARACTER_INVALIDO":
                return "Hay un carácter que no válido para el lenguaje. " +
                       "Revisa si escribiste un símbolo extraño o un acento que no es válido. " +
                       "Si no lo necesitas, elimínalo o sustitúyelo por un símbolo permitido.";
            
            case "ERROR_NUMERO_MALFORMADO":
                return "El numero esta mal formado o tiene caracteres invalidos. "+
                        "El numero debe estar escrito por uno o mas digitos, seguido de un punto y mas digitos" +
                        "Ejemplo: 2.5, 3, 10.0, 0.1";
            case "ERROR":
            default:
                return "Se encontró un símbolo no reconocido. " +
                       "Verifica que la cadena esté bien escrita o elimínalo.";
        }
    }
    
    private String separarOperadorYIdentificador(String lexema) {
        if (lexema == null || lexema.length() < 2) return lexema;

        char c = lexema.charAt(0);
        if ("+-*/=<>!&".indexOf(c) != -1) {
            String resto = lexema.substring(1);
            // si el resto parece un identificador, agregamos un espacio
            return c + " " + resto;
        }
        return lexema;
    }
    
    private void VerTablaIdentificadores(){
        
        // veremos si el análisis léxico ya se ejecutó
        if (this.tablaDeSimbolosGlobal == null) {
            JOptionPane.showMessageDialog(this, 
                "Debe ejecutar el 'Analizador Léxico' primero.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return; // Detiene el método para evitar el crash
        }
        
        // se hace en un Jtable
        String[] columnas = {"#" , "Nombre", "Tipo", "Línea", "Columna", "Direccion Memoria"}; 
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        //obtenemos el mapa de identificadores
        Map<String, TablaSimbolos.EntradaIdentificador> identificadores = tablaDeSimbolosGlobal.getIdentificadores();
        
        if(identificadores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron identificadores en el codigo fuente",
                    "Tabla de simbolos vacia",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int contador = 1;
        //Llena el modelo con los datos
        for (TablaSimbolos.EntradaIdentificador entrada : identificadores.values()) {
            // Formateamos la dirección para que se vea bien
            String dirMemoriaStr = (entrada.getDireccionMemoria() == -1) 
                                    ? "N/A" // Si es -1, muestra N/A
                                    : String.valueOf(entrada.getDireccionMemoria()); // Si no, muestra el número
            model.addRow(new Object[]{
                contador++,
                entrada.getNombre(),
                entrada.getTipo(), 
                entrada.getLinea(),
                entrada.getColumna(),
                dirMemoriaStr
            });
        }
        //Crea y muestra la ventana (JDialog) con la tabla
        JTable tablaGUI = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tablaGUI);
        JDialog dialogoTabla = new JDialog(this, "Tabla de Variable (Identificadores)", true);
        dialogoTabla.add(scrollPane);
        dialogoTabla.setSize(600, 400);
        dialogoTabla.setLocationRelativeTo(this); // Centra la ventana
        dialogoTabla.setVisible(true); // Muestra la ventana        
    }
    
    private void VerTablaFija() {
        // Preparamos el modelo para la JTable
        String[] columnas = {"Palabra Reservada", "Componente Léxico"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        //Obtiene la lista de palabras de la clase estática
        Set<String> palabras = TablaPalabrasReservadas.getPalabrasReservadas();
        
        //Llena el modelo con los datos
        for (String palabra : palabras) {
            String componente = TablaPalabrasReservadas.getComponente(palabra);
            model.addRow(new Object[]{ palabra, componente });
        }
        //Ordena la tabla alfabéticamente
        JTable tablaGUI = new javax.swing.JTable(model);
        tablaGUI.setAutoCreateRowSorter(true); // Esto la ordena
        
        //Crea y muestra la ventana (JDialog)
        JScrollPane scrollPane = new javax.swing.JScrollPane(tablaGUI);
        JDialog dialogoTabla = new javax.swing.JDialog(this, "Tabla Fija", true);
        dialogoTabla.add(scrollPane);
        dialogoTabla.setSize(400, 600);
        dialogoTabla.setLocationRelativeTo(this);
        dialogoTabla.setVisible(true);
    }
    
    private void VerTablaFunciones() {
        //Verifica si el análisis léxico ya se ejecutó
        if (this.tablaDeFuncionesGlobal == null) {
            JOptionPane.showMessageDialog(this, 
                "Debe ejecutar el 'Analizador Léxico' primero.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return; // Detiene el método
        }
        //preparamos el modelo para la JTable
        String[] columnas = {"Función", "Línea", "Tipo Retorno", "Parámetros"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        //Obtiene el mapa de funciones
        Map<String, TablaFunciones.EntradaFuncion> funciones = 
            tablaDeFuncionesGlobal.getFunciones();
        //Llena el modelo con los datos
        for (TablaFunciones.EntradaFuncion entrada : funciones.values()) {
            // Convertimos la lista de parámetros a un String legible
            String params = entrada.getTiposParametros().toString(); 
            model.addRow(new Object[]{
                entrada.getNombre(),
                entrada.getLinea(),
                entrada.getTipoRetorno(),
                params  // Mostramos la lista de parámetros
            });
        }
        //Crea y muestra la ventana (JDialog)
        JTable tablaGUI = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tablaGUI);
        JDialog dialogoTabla = new JDialog(this, "Tabla de Funciones", true);
        dialogoTabla.add(scrollPane);
        dialogoTabla.setSize(600, 400);
        dialogoTabla.setLocationRelativeTo(this);
        dialogoTabla.setVisible(true);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new FondoPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jCode = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jErrores = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        menuCompilar = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        menuTablaSimbolos = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel1.setBackground(new java.awt.Color(30, 42, 56));

        jCode.setBorder(null);
        jCode.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jScrollPane1.setViewportView(jCode);

        jErrores.setBorder(null);
        jErrores.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jErrores.setRequestFocusEnabled(false);
        jScrollPane2.setViewportView(jErrores);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/logo_quimcode.png"))); // NOI18N
        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 779, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 779, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(375, 375, 375)
                                .addComponent(jLabel1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(98, 98, 98)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 197, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jMenuBar1.setBorder(null);
        jMenuBar1.setToolTipText("");

        menuArchivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/iconArchivo.png"))); // NOI18N
        menuArchivo.setText("Archivo");
        menuArchivo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem7.setText("Nuevo");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        menuArchivo.add(jMenuItem7);

        jMenuItem1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem1.setText("Abrir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        menuArchivo.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem2.setText("Guardar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        menuArchivo.add(jMenuItem2);

        jMenuBar1.add(menuArchivo);

        menuCompilar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/iconCompilar.png"))); // NOI18N
        menuCompilar.setText("Compilar");
        menuCompilar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem3.setText("Analisis léxico");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        menuCompilar.add(jMenuItem3);

        jMenuBar1.add(menuCompilar);

        menuTablaSimbolos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/iconTablaSimbolos.png"))); // NOI18N
        menuTablaSimbolos.setText("Tabla de Simbolos");
        menuTablaSimbolos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem4.setText("Tabla Fija");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        menuTablaSimbolos.add(jMenuItem4);

        jMenuItem5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem5.setText("Tabla de identificadores");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        menuTablaSimbolos.add(jMenuItem5);

        jMenuItem6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem6.setText("Tabla de funciones ");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        menuTablaSimbolos.add(jMenuItem6);

        jMenuBar1.add(menuTablaSimbolos);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        GuardarArchivo();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        AbrirArchivo();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
    VerTablaIdentificadores();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        VerTablaFija();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        NuevoArchivo();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
       ejecutarAnalisisLexico();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        VerTablaFunciones();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Compilador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane jCode;
    private javax.swing.JTextPane jErrores;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenu menuCompilar;
    private javax.swing.JMenu menuTablaSimbolos;
    // End of variables declaration//GEN-END:variables
    
    class FondoPanel extends JPanel {
        private Image imagen;
        public void paint(Graphics g){
            imagen = new ImageIcon(getClass().getResource("/IMAGENES/fondoQuimcode.png")).getImage();
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            //Mostrar imagen de fondo
            setOpaque(false);
            //Dibujar todos los componentes del JFrame
            super.paint(g);
        }
    }

}

