/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import controlador.VehiculoControlador;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import modelo.Vehiculo;

/**
 *
 * @author Lenovo LOQ
 */
public class FormularioRegistro extends javax.swing.JFrame {

    DefaultTableModel modelo;
    VehiculoControlador cliente;

    /**
     * Creates new form FormularioRegistro
     */
    public FormularioRegistro() {
        initComponents();
        cliente = new VehiculoControlador();
        cargarTablaVehiculos();
        seleccionarVehiculo();
        textoInicio();
        btnInicio();
    }

    public void cargarTablaVehiculos() {
        String[] columnas = {"Id", "Marca", "Modelo", "Placa", "Chasis", "Anio", "Color"};
        String[] filas = new String[7];
        modelo = new DefaultTableModel(null, columnas);

        ArrayList<Vehiculo> vehiculos = cliente.obtenerTodosLosVehiculos();
        if (!vehiculos.isEmpty()) {
            for (Vehiculo vehiculo : vehiculos) {
                filas[0] = vehiculo.getID();
                filas[1] = vehiculo.getMARCA();
                filas[2] = vehiculo.getMODELO();
                filas[3] = vehiculo.getPLACA();
                filas[4] = vehiculo.getCHASIS();
                filas[5] = vehiculo.getANIO();
                filas[6] = vehiculo.getCOLOR();
                modelo.addRow(filas);
            }
        }
        jtblVehiculos.setModel(modelo);
    }

    public void guardarVehiculo() {
        // Validar campos vacíos
        String marca = jtxtFMarca.getText().trim();
        String modelo = jtxtFModelo.getText().trim();
        String chasis = jtxtFChasis.getText().trim();
        String placa = jtxtFPlaca.getText().trim();
        String anio = jtxtFAnio.getText().trim();
        String color = jtxtFColor.getText().trim();
        
        if (marca.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El campo MARCA es obligatorio",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            jtxtFMarca.requestFocus();
            return;
        }
        
        if (modelo.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El campo MODELO es obligatorio",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            jtxtFModelo.requestFocus();
            return;
        }
        
        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El campo PLACA es obligatorio",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            jtxtFPlaca.requestFocus();
            return;
        }
        
        if (chasis.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El campo CHASIS es obligatorio",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            jtxtFChasis.requestFocus();
            return;
        }
        
        if (anio.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El campo AÑO es obligatorio",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            jtxtFAnio.requestFocus();
            return;
        }
        
        // Validar que el año sea numérico
        try {
            int anioNumerico = Integer.parseInt(anio);
            if (anioNumerico < 1900 || anioNumerico > 2100) {
                JOptionPane.showMessageDialog(this, 
                    "El año debe estar entre 1900 y 2100",
                    "Año inválido",
                    JOptionPane.WARNING_MESSAGE);
                jtxtFAnio.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "El año debe ser un número válido",
                "Formato incorrecto",
                JOptionPane.WARNING_MESSAGE);
            jtxtFAnio.requestFocus();
            return;
        }

        Vehiculo vehiculo = new Vehiculo(marca, modelo, placa, chasis, anio, color);

        if (cliente.insertarVehiculo(vehiculo)) {
            JOptionPane.showMessageDialog(this, 
                "Vehículo insertado correctamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            cargarTablaVehiculos();
            limpiarTexto();
            btnInicio();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Error al insertar el vehículo. Verifique que la placa no esté duplicada.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarVehiculo() {
        String id = jtxtFID.getText().trim();
        String placa = jtxtFPlaca.getText().trim();
        
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor seleccione un vehículo de la tabla para eliminar",
                "Ningún vehículo seleccionado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Confirmar eliminación
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea eliminar el vehículo con placa " + placa + "?\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (cliente.eliminarVehiculo(id)) {
                JOptionPane.showMessageDialog(this, 
                    "Vehículo eliminado correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTablaVehiculos();
                limpiarTexto();
                btnInicio();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al eliminar el vehículo",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void editarVehiculo() {
        // Validar que hay un vehículo seleccionado
        String id = jtxtFID.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor seleccione un vehículo de la tabla para editar",
                "Ningún vehículo seleccionado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validar campos vacíos
        String marca = jtxtFMarca.getText().trim();
        String modelo = jtxtFModelo.getText().trim();
        String placa = jtxtFPlaca.getText().trim();
        String chasis = jtxtFChasis.getText().trim();
        String anio = jtxtFAnio.getText().trim();
        String color = jtxtFColor.getText().trim();
        
        if (marca.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El campo MARCA es obligatorio",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            jtxtFMarca.requestFocus();
            return;
        }
        
        if (modelo.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El campo MODELO es obligatorio",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            jtxtFModelo.requestFocus();
            return;
        }
        
        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El campo PLACA es obligatorio",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            jtxtFPlaca.requestFocus();
            return;
        }
        
        if (chasis.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El campo CHASIS es obligatorio",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            jtxtFChasis.requestFocus();
            return;
        }
        
        if (anio.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El campo AÑO es obligatorio",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            jtxtFAnio.requestFocus();
            return;
        }
        
        // Validar que el año sea numérico
        try {
            int anioNumerico = Integer.parseInt(anio);
            if (anioNumerico < 1900 || anioNumerico > 2100) {
                JOptionPane.showMessageDialog(this, 
                    "El año debe estar entre 1900 y 2100",
                    "Año inválido",
                    JOptionPane.WARNING_MESSAGE);
                jtxtFAnio.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "El año debe ser un número válido",
                "Formato incorrecto",
                JOptionPane.WARNING_MESSAGE);
            jtxtFAnio.requestFocus();
            return;
        }

        Vehiculo vehiculo = new Vehiculo(id, marca, modelo, chasis, placa, anio, color);

        if (cliente.actualizarVehiculo(vehiculo)) {
            JOptionPane.showMessageDialog(this, 
                "Vehículo actualizado correctamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            cargarTablaVehiculos();
            limpiarTexto();
            btnInicio();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Error al actualizar el vehículo",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void limpiarTexto() {
        jtxtFID.setText("");
        jtxtFMarca.setText("");
        jtxtFModelo.setText("");
        jtxtFPlaca.setText("");
        jtxtFChasis.setText("");
        jtxtFAnio.setText("");
        jtxtFColor.setText("");
    }

    public void moverFocoCampoVacio() {

        if (jtxtFPlaca.getText().isEmpty()) {
            jtxtFPlaca.requestFocus();

        } else if (jtxtFChasis.getText().isEmpty()) {
            jtxtFChasis.requestFocus();
        }
    }

    public void textoInicio() {
        jtxtFID.setEnabled(false);
        jtxtFMarca.setEnabled(false);
        jtxtFModelo.setEnabled(false);
        jtxtFPlaca.setEnabled(false);
        jtxtFChasis.setEnabled(false);
        jtxtFAnio.setEnabled(false);
        jtxtFColor.setEnabled(false);
    }

    public void btnInicio() {
        jbtnNuevo.setEnabled(true);
        jbtnGuardar.setEnabled(false);
        jbtnEditar.setEnabled(false);
        jbtnEliminar.setEnabled(true);
        jbtnCancelar.setEnabled(false);
    }

    public void textoNuevo() {
        jtxtFID.setEnabled(false);
        jtxtFMarca.setEnabled(true);
        jtxtFModelo.setEnabled(true);
        jtxtFPlaca.setEnabled(true);
        jtxtFChasis.setEnabled(true);
        jtxtFAnio.setEnabled(true);
        jtxtFColor.setEnabled(true);
        jtxtFID.setText("    Id generado Automaticamente");
        jtxtFMarca.setText("");
        jtxtFModelo.setText("");
        jtxtFPlaca.setText("");
        jtxtFChasis.setText("");
        jtxtFAnio.setText("");
        jtxtFColor.setText("");
    }

    public void btnNuevo() {
        jbtnNuevo.setEnabled(false);
        jbtnGuardar.setEnabled(true);
        jbtnEditar.setEnabled(false);
        jbtnEliminar.setEnabled(false);
        jbtnCancelar.setEnabled(true);
    }

    public void textosEditar() {
        jtxtFID.setEnabled(false);
        jtxtFMarca.setEnabled(true);
        jtxtFModelo.setEnabled(true);
        jtxtFPlaca.setEnabled(true);
        jtxtFChasis.setEnabled(true);
        jtxtFAnio.setEnabled(true);
        jtxtFColor.setEnabled(true);
    }

    public void botonesEditar() {
        jbtnNuevo.setEnabled(false);
        jbtnGuardar.setEnabled(false);
        jbtnEditar.setEnabled(true);
        jbtnEliminar.setEnabled(true);
        jbtnCancelar.setEnabled(true);
    }

    public void seleccionarVehiculo() {
        jtblVehiculos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (jtblVehiculos.getSelectedRow() != -1) {
                    int fila = jtblVehiculos.getSelectedRow();
                    jtxtFID.setText(jtblVehiculos.getValueAt(fila, 0).toString().trim());
                    jtxtFMarca.setText(jtblVehiculos.getValueAt(fila, 1).toString().trim());
                    jtxtFModelo.setText(jtblVehiculos.getValueAt(fila, 2).toString().trim());
                    jtxtFPlaca.setText(jtblVehiculos.getValueAt(fila, 3).toString().trim());
                    jtxtFChasis.setText(jtblVehiculos.getValueAt(fila, 4).toString().trim());
                    jtxtFAnio.setText(jtblVehiculos.getValueAt(fila, 5).toString().trim());
                    jtxtFColor.setText(jtblVehiculos.getValueAt(fila, 6).toString().trim());
                    jbtnEditar.setEnabled(true);
                    jtxtFMarca.setEnabled(true);
                    jtxtFModelo.setEnabled(true);
                    jtxtFPlaca.setEnabled(true);
                    jtxtFChasis.setEnabled(true);
                    jtxtFAnio.setEnabled(true);
                    jtxtFColor.setEnabled(true);
                }
            }
        });
    }

    public void buscarVehiculo() {
        String placa = jtxtFiltrar.getText().trim();

        if (placa.isEmpty() || placa.contains("Ingrese un numero de placa")) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un numero de Placa para buscar",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Vehiculo vehiculo = cliente.buscarVehiculo(placa);
        if (vehiculo != null) {
            String[] columnas = {"Id", "Marca", "Modelo", "Placa", "Chasis", "Anio", "Color"};
            String[] filas = new String[7];
            modelo = new DefaultTableModel(null, columnas);
                filas[0] = vehiculo.getID();
                filas[1] = vehiculo.getMARCA();
                filas[2] = vehiculo.getMODELO();
                filas[3] = vehiculo.getPLACA();
                filas[4] = vehiculo.getCHASIS();
                filas[5] = vehiculo.getANIO();
                filas[6] = vehiculo.getCOLOR();
                modelo.addRow(filas);
            jtblVehiculos.setModel(modelo);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se encontró ningún vehículo con la placa: " + placa,
                    "Vehículo no encontrado",
                    JOptionPane.INFORMATION_MESSAGE);
            cargarTablaVehiculos();
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jtxtFMarca = new javax.swing.JTextField();
        jtxtFModelo = new javax.swing.JTextField();
        jtxtFChasis = new javax.swing.JTextField();
        jtxtFColor = new javax.swing.JTextField();
        jtxtFAnio = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jtxtFID = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jtxtFPlaca = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jbtnCancelar = new javax.swing.JButton();
        jbtnNuevo = new javax.swing.JButton();
        jbtnEditar = new javax.swing.JButton();
        jbtnEliminar = new javax.swing.JButton();
        jbtnGuardar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblVehiculos = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jtxtFiltrar = new javax.swing.JTextField();
        jbtnFiltrar = new javax.swing.JButton();
        jbtnMostrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        jLabel1.setText("Formulario Registro Vehiculos");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jtxtFMarca.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jtxtFModelo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jtxtFChasis.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jtxtFColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jtxtFAnio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setText("MODELO");

        jLabel3.setText("MARCA");

        jLabel4.setText("CHASIS");

        jLabel6.setText("COLOR");

        jLabel7.setText("ANIO");

        jtxtFID.setForeground(new java.awt.Color(204, 204, 204));
        jtxtFID.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel10.setText("ID");

        jtxtFPlaca.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtxtFPlaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtFPlacaActionPerformed(evt);
            }
        });

        jLabel5.setText("PLACA");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtxtFMarca)
                    .addComponent(jtxtFModelo)
                    .addComponent(jtxtFChasis)
                    .addComponent(jtxtFColor)
                    .addComponent(jtxtFAnio)
                    .addComponent(jtxtFID, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .addComponent(jtxtFPlaca))
                .addGap(64, 64, 64))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtFID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtFMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtFModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtFPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtFChasis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtFAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jtxtFColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jbtnCancelar.setText("Cancelar");
        jbtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCancelarActionPerformed(evt);
            }
        });

        jbtnNuevo.setText("Nuevo");
        jbtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNuevoActionPerformed(evt);
            }
        });

        jbtnEditar.setText("Editar");
        jbtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEditarActionPerformed(evt);
            }
        });

        jbtnEliminar.setText("Eliminar");
        jbtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEliminarActionPerformed(evt);
            }
        });

        jbtnGuardar.setText("Guardar");
        jbtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGuardarActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Opciones");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jbtnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)))
                .addGap(41, 41, 41))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(jbtnNuevo)
                .addGap(12, 12, 12)
                .addComponent(jbtnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtnEditar)
                .addGap(12, 12, 12)
                .addComponent(jbtnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtnCancelar)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jtblVehiculos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jtblVehiculos);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Buscar Vehiculo");

        jtxtFiltrar.setForeground(new java.awt.Color(204, 204, 204));
        jtxtFiltrar.setText("      Ingrese un numero de placa");
        jtxtFiltrar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtxtFiltrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtxtFiltrarMouseClicked(evt);
            }
        });
        jtxtFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtFiltrarActionPerformed(evt);
            }
        });

        jbtnFiltrar.setText("Filtrar");
        jbtnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnFiltrarActionPerformed(evt);
            }
        });

        jbtnMostrar.setText("Mostrar todo");
        jbtnMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnMostrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(254, 254, 254)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jtxtFiltrar))
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jbtnFiltrar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jbtnMostrar))
                                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 23, Short.MAX_VALUE)))))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jbtnMostrar)
                    .addComponent(jbtnFiltrar))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtFPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtFPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtFPlacaActionPerformed

    private void jbtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGuardarActionPerformed
        // TODO add your handling code here:
        guardarVehiculo();
    }//GEN-LAST:event_jbtnGuardarActionPerformed

    private void jbtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNuevoActionPerformed
        // TODO add your handling code here:
        btnNuevo();
        textoNuevo();
    }//GEN-LAST:event_jbtnNuevoActionPerformed

    private void jbtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelarActionPerformed
        textoInicio();
        btnInicio();
        limpiarTexto();        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnCancelarActionPerformed

    private void jbtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEditarActionPerformed
        editarVehiculo();
        textosEditar();
        botonesEditar();        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnEditarActionPerformed

    private void jbtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEliminarActionPerformed
        eliminarVehiculo();
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnEliminarActionPerformed

    private void jtxtFiltrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtFiltrarMouseClicked
        jtxtFiltrar.setText("    ");
        jtxtFiltrar.setForeground(Color.black);
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtFiltrarMouseClicked

    private void jbtnMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnMostrarActionPerformed
        cargarTablaVehiculos();
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnMostrarActionPerformed

    private void jbtnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnFiltrarActionPerformed

        buscarVehiculo();
        jtxtFiltrar.setText("      Ingrese un numero de placa");
        jtxtFiltrar.setForeground(Color.gray);

        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnFiltrarActionPerformed

    private void jtxtFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtFiltrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtFiltrarActionPerformed

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
            java.util.logging.Logger.getLogger(FormularioRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormularioRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormularioRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormularioRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormularioRegistro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnCancelar;
    private javax.swing.JButton jbtnEditar;
    private javax.swing.JButton jbtnEliminar;
    private javax.swing.JButton jbtnFiltrar;
    private javax.swing.JButton jbtnGuardar;
    private javax.swing.JButton jbtnMostrar;
    private javax.swing.JButton jbtnNuevo;
    private javax.swing.JTable jtblVehiculos;
    private javax.swing.JTextField jtxtFAnio;
    private javax.swing.JTextField jtxtFChasis;
    private javax.swing.JTextField jtxtFColor;
    private javax.swing.JTextField jtxtFID;
    private javax.swing.JTextField jtxtFMarca;
    private javax.swing.JTextField jtxtFModelo;
    private javax.swing.JTextField jtxtFPlaca;
    private javax.swing.JTextField jtxtFiltrar;
    // End of variables declaration//GEN-END:variables
}
