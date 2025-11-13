
package codigo;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.*;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;


public class Compilador extends javax.swing.JFrame {
    // Variable global para saber si el archivo actual ya está guardado
    private java.io.File archivoActual = null;
    private boolean cambiosSinGuardar = false;
    private boolean actualizandoTexto = false;
    public String rute="";
    
    //Variable para la imagen de fondo
    FondoPanel fondo = new FondoPanel();


    /**
     * Creates new form VentanaPrincipal
     */
    public Compilador() {
        this.setContentPane(fondo);
        initComponents();
        inicializar();
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
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Guardar archivo .quim");

        // Filtro para archivos .quim
        javax.swing.filechooser.FileNameExtensionFilter filter =
        new javax.swing.filechooser.FileNameExtensionFilter("Archivos Quim (*.quim)", "quim");
        fileChooser.setFileFilter(filter);

        int seleccion = fileChooser.showSaveDialog(this);

        if (seleccion == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File archivo = fileChooser.getSelectedFile();
        // Si el nombre no termina con .quim, se lo agregamos automáticamente
            if (!archivo.getName().toLowerCase().endsWith(".quim")) {
                archivo = new java.io.File(archivo.getAbsolutePath() + ".quim");
            }

            try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(archivo))) {
                //Obtener el contenido del jCode y escribirlo
                writer.write(jCode.getText());
                writer.flush();
                
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Archivo guardado correctamente en:\n" + archivo.getAbsolutePath(),
                "Guardar archivo",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this,
                "Error al guardar el archivo: " + e.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
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
            int opcion = javax.swing.JOptionPane.showConfirmDialog(this,
              "Tienes cambios sin guardar.\n¿Deseas guardarlos antes de crear un nuevo archivo?", "Guardar cambios",
            javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);
            
            if (opcion == javax.swing.JOptionPane.CANCEL_OPTION) {
                return; // cancelar acción
            } else if (opcion == javax.swing.JOptionPane.YES_OPTION) {
                GuardarArchivo(); // Guardamos antes de limpiar
            }
        }
        // Limpiar el área y reiniciar el estado
        jCode.setText("");
        archivoActual = null;
        cambiosSinGuardar = false;   
    }
    
    public void AbrirArchivo(){
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Abrir archivo .quim");

        // 🔹 Solo archivos con extensión .quim
        javax.swing.filechooser.FileNameExtensionFilter filter = 
                new javax.swing.filechooser.FileNameExtensionFilter("Archivos Quim (*.quim)", "quim");
        fileChooser.setFileFilter(filter);

        int seleccion = fileChooser.showOpenDialog(this);

        if (seleccion == javax.swing.JFileChooser.APPROVE_OPTION) {
                java.io.File archivo = fileChooser.getSelectedFile();

            // Validar la extensión manualmente
                if (!archivo.getName().toLowerCase().endsWith(".quim")) {
                    javax.swing.JOptionPane.showMessageDialog(this,

                        "El archivo seleccionado no es compatible.\nDebe ser un archivo con extensión .quim",
                        "Archivo no compatible",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(archivo))) {
                    StringBuilder contenido = new StringBuilder();
                    String linea;

                    while ((linea = reader.readLine()) != null) {
                        contenido.append(linea).append("\n");
                    }

                    // Mostrar el contenido del archivo .quim en el editor
                    jCode.setText(contenido.toString());

                } catch (Exception e) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Error al abrir el archivo: " + e.getMessage(),
                            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void ejecutarAnalisisLexico(){
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
            tokens.addColumn("#");
            tokens.addColumn("Lexema");
            tokens.addColumn("Comp. Léxico");

            StringBuilder errores = new StringBuilder();

            Tokens token;
            while((token = lexer.yylex()) != null){
                int linea = token.getLinea();
                String lexema = token.getLexema();
                String compLexico = token.getComponente();

                //Si el token pertenece a algún tipo de error, lo mandamos a jErrores
                if(token.toString().startsWith("ERROR")){
                    errores.append(String.format("Linea %d -> %s (%s)&%n",linea, lexema, token));
                } else {
                    tokens.addRow(new Object[]{ linea, lexema, compLexico});
                }
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

            if (errores.length() == 0) {
                jErrores.setText("ANALISIS LÉXICO COMPLETADO SIN ERRORES.");
            } else {
                jErrores.setText(" Se encontraron errores léxicos:\n\n" + errores.toString());
            }
            
        } catch (Exception e) {
             jErrores.setText("️ Error en el análisis léxico: " + e.getMessage());
            e.printStackTrace();
        }
        
        
        
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addGap(67, 67, 67)
                .addComponent(jLabel1)
                .addContainerGap(353, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(171, 171, 171)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
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
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
      
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        NuevoArchivo();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
       ejecutarAnalisisLexico();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

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

