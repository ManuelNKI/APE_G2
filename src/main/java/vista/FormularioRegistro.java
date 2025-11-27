/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import controlador.Validador;
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
    Validador validador;

    /**
     * Creates new form FormularioRegistro
     */
    public FormularioRegistro() {
        initComponents();
        cliente = new VehiculoControlador();
        validador = new Validador();

        // --- CONFIGURACIÓN DE PLACEHOLDERS (FLATLAF) ---
        jtxtFPlaca.putClientProperty("JTextField.placeholderText", "ABC-1234");
        jtxtFAnio.putClientProperty("JTextField.placeholderText", "Ej: 2024");
        jtxtFChasis.putClientProperty("JTextField.placeholderText", "17 Caracteres");

        // Placeholder del Buscador
        jtxtFiltrar.putClientProperty("JTextField.placeholderText", "Ingrese una placa para buscar");
        // Importante: Limpiamos el texto y color manual que venía del diseño
        jtxtFiltrar.setText("");
        jtxtFiltrar.setForeground(Color.BLACK);

        // Placeholder del ID
        jtxtFID.putClientProperty("JTextField.placeholderText", "Generado Automáticamente");

        // Carga inicial
        obtenerVehiculos();
        seleccionarVehiculo();
        textoInicio();
        btnInicio();
        initValidaciones();

    }

    public void obtenerVehiculos() {
        ArrayList<Vehiculo> vehiculos = cliente.obtenerTodosLosVehiculos();
        cargarTablaVehiculos(vehiculos);
    }

    public void cargarTablaVehiculos(ArrayList<Vehiculo> vehiculos) {
        String[] columnas = {"ID", "Placa", "Chasis", "Marca", "Modelo", "Año", "Color"};
        String[] filas = new String[7];
        modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Retornar false bloquea la edición en todas las celdas
            }
        };

        if (!vehiculos.isEmpty()) {
            for (Vehiculo vehiculo : vehiculos) {
                filas[0] = vehiculo.getID();
                filas[1] = vehiculo.getPLACA();
                filas[2] = vehiculo.getCHASIS();
                filas[3] = vehiculo.getMARCA();
                filas[4] = vehiculo.getMODELO();
                filas[5] = vehiculo.getANIO();
                filas[6] = vehiculo.getCOLOR();
                modelo.addRow(filas);
            }
        }
        jtblVehiculos.setModel(modelo);
        // 1. Configurar columna ID (Índice 0) -> Muy pequeña
        jtblVehiculos.getColumnModel().getColumn(0).setPreferredWidth(30);
        jtblVehiculos.getColumnModel().getColumn(0).setMaxWidth(40);

        // 2. Configurar columna CHASIS (Índice 4) -> Grande
        jtblVehiculos.getColumnModel().getColumn(2).setPreferredWidth(200);

        // 3. Configurar columna AÑO (Índice 5) -> Pequeña
        jtblVehiculos.getColumnModel().getColumn(5).setPreferredWidth(60);
        jtblVehiculos.getColumnModel().getColumn(5).setMaxWidth(80);
    }

    public void buscarVehiculo() {
        String placa = jtxtFiltrar.getText().trim();

        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un numero de Placa para buscar",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        ArrayList<Vehiculo> vehiculos = cliente.buscarVehiculo(placa);
        if (vehiculos != null && !vehiculos.isEmpty()) {
            cargarTablaVehiculos(vehiculos);
            jbtnMostrar.setEnabled(true);
            jbtnFiltrar.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se encontró ningún vehículo con la placa: " + placa,
                    "Sin resultados",
                    JOptionPane.INFORMATION_MESSAGE);
            obtenerVehiculos();
        }
    }

    public void guardarVehiculo() {
        String marca = jtxtFMarca.getText().trim();
        String modelo = jtxtFModelo.getText().trim();
        String chasis = jtxtFChasis.getText().trim();
        String placa = jtxtFPlaca.getText().trim();
        String anio = jtxtFAnio.getText().trim();
        String color = jtxtFColor.getText().trim();

        // Validación Lógica
        String error = validador.validarDatos(marca, modelo, placa, chasis, anio, color);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validación Base de Datos (Duplicados)
        ArrayList<Vehiculo> busqueda = cliente.buscarVehiculo(placa);
        if (busqueda != null && !busqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La placa " + placa + " ya está registrada en el sistema.\nNo se puede duplicar.",
                    "Placa Repetida",
                    JOptionPane.WARNING_MESSAGE);
            jtxtFPlaca.requestFocus();
            return;
        }
        ArrayList<Vehiculo> busquedaChasis = cliente.buscarPorChasis(chasis);
        if (busquedaChasis != null && !busquedaChasis.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El chasis " + chasis + " ya está registrada en el sistema.\nNo se puede duplicar.",
                    "Chasis Repetido",
                    JOptionPane.WARNING_MESSAGE);
            jtxtFChasis.requestFocus();
            return;
        }

        Vehiculo vehiculo = new Vehiculo(marca, modelo, placa, chasis, anio, color);

        if (cliente.insertarVehiculo(vehiculo)) {
            JOptionPane.showMessageDialog(this, "Vehículo insertado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            obtenerVehiculos();
            limpiarTexto();
            btnInicio();
        } else {
            JOptionPane.showMessageDialog(this, "Error al insertar. Posible placa duplicada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editarVehiculo() {
        String id = jtxtFID.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un vehículo para editar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Recolectar datos
        String marca = jtxtFMarca.getText().trim();
        String modelo = jtxtFModelo.getText().trim();
        String placa = jtxtFPlaca.getText().trim();
        String chasis = jtxtFChasis.getText().trim();
        String anio = jtxtFAnio.getText().trim();
        String color = jtxtFColor.getText().trim();

        // 3. Validación de Formato (Lógica)
        String error = validador.validarDatos(marca, modelo, placa, chasis, anio, color);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 4. Validación Base de Datos: PLACA ÚNICA
        ArrayList<Vehiculo> busquedaPlaca = cliente.buscarVehiculo(placa);
        if (busquedaPlaca != null && !busquedaPlaca.isEmpty()) {
            String idEncontrado = busquedaPlaca.get(0).getID();
            // Si el ID encontrado es DIFERENTE al actual, es un duplicado ilegal
            if (!idEncontrado.equals(id)) {
                JOptionPane.showMessageDialog(this,
                        "La placa " + placa + " ya pertenece a otro vehículo.",
                        "Placa Duplicada",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

// 5. Validación Base de Datos: CHASIS ÚNICO
        ArrayList<Vehiculo> busquedaChasis = cliente.buscarPorChasis(chasis);

        if (busquedaChasis != null && !busquedaChasis.isEmpty()) {
            // Recorremos TODOS los resultados (por si la búsqueda devuelve coincidencias parciales)
            for (Vehiculo v : busquedaChasis) {

                // 1. Convertimos ambos IDs a String puro y quitamos espacios
                String idActual = String.valueOf(id).trim();
                String idEncontrado = String.valueOf(v.getID()).trim();

                // 2. Comparamos
                // Si el ID encontrado NO es el mío, significa que ALGUIEN MÁS tiene ese chasis
                if (!idEncontrado.equals(idActual)) {
                    JOptionPane.showMessageDialog(this,
                            "El chasis " + chasis + " ya está registrado en el vehículo ID: " + idEncontrado,
                            "Chasis Duplicado",
                            JOptionPane.WARNING_MESSAGE);
                    return; // Detenemos todo
                }
            }
        }

        // 6. Si pasa todo, actualizar
        Vehiculo vehiculo = new Vehiculo(id, marca, modelo, chasis, placa, anio, color);

        if (cliente.actualizarVehiculo(vehiculo)) {
            JOptionPane.showMessageDialog(this, "Vehículo actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            obtenerVehiculos();
            limpiarTexto();
            btnInicio();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el vehículo", "Error", JOptionPane.ERROR_MESSAGE);
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

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el vehículo con placa " + placa + "?\n"
                + "Esta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (cliente.eliminarVehiculo(id)) {
                JOptionPane.showMessageDialog(this, "Vehículo eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                obtenerVehiculos();
                limpiarTexto();
                btnInicio();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el vehículo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void initValidaciones() {
        // 1. Validar AÑO
        jtxtFAnio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) || jtxtFAnio.getText().length() >= 4) {
                    evt.consume();
                }
            }
        });

        // 2. Validar CHASIS
        jtxtFChasis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (Character.isLowerCase(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                    c = Character.toUpperCase(c);
                }
                if (jtxtFChasis.getText().length() >= 17) {
                    evt.consume();
                }
            }
        });

        // 3. Validar PLACA (Guion automático)
        jtxtFPlaca.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                String text = jtxtFPlaca.getText();

                if (Character.isLowerCase(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                    c = Character.toUpperCase(c);
                }

                boolean isDelete = (c == java.awt.event.KeyEvent.VK_BACK_SPACE) || (c == java.awt.event.KeyEvent.VK_DELETE);

                if (text.length() == 3 && !isDelete && Character.isLetterOrDigit(c)) {
                    evt.consume();
                    jtxtFPlaca.setText(text + "-" + c);
                }

                if (text.length() >= 8) {
                    evt.consume();
                }
            }
        });
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
        jbtnEliminar.setEnabled(false);
        jbtnCancelar.setEnabled(false);
        jbtnMostrar.setEnabled(false);
    }

    public void textoNuevo() {
        jtxtFID.setEnabled(false);
        jtxtFMarca.setEnabled(true);
        jtxtFModelo.setEnabled(true);
        jtxtFPlaca.setEnabled(true);
        jtxtFChasis.setEnabled(true);
        jtxtFAnio.setEnabled(true);
        jtxtFColor.setEnabled(true);

        // Importante: Dejar el ID vacío ("") para que se vea el placeholder
        jtxtFID.setText("");

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

    public void textosSeleccionar() {
        jtxtFMarca.setEnabled(true);
        jtxtFModelo.setEnabled(true);
        jtxtFPlaca.setEnabled(true);
        jtxtFChasis.setEnabled(true);
        jtxtFAnio.setEnabled(true);
        jtxtFColor.setEnabled(true);
    }

    public void seleccionarVehiculo() {
        jtblVehiculos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (jtblVehiculos.getSelectedRow() != -1) {
                    int fila = jtblVehiculos.getSelectedRow();
                    jtxtFID.setText(jtblVehiculos.getValueAt(fila, 0).toString().trim());
                    jtxtFPlaca.setText(jtblVehiculos.getValueAt(fila, 1).toString().trim());
                    jtxtFChasis.setText(jtblVehiculos.getValueAt(fila, 2).toString().trim());
                    jtxtFMarca.setText(jtblVehiculos.getValueAt(fila, 3).toString().trim());
                    jtxtFModelo.setText(jtblVehiculos.getValueAt(fila, 4).toString().trim());
                    jtxtFAnio.setText(jtblVehiculos.getValueAt(fila, 5).toString().trim());
                    jtxtFColor.setText(jtblVehiculos.getValueAt(fila, 6).toString().trim());
                    botonesEditar();
                    textosSeleccionar();
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButton1 = new javax.swing.JRadioButton();
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
        jLabel11 = new javax.swing.JLabel();
        jbtnNuevo = new javax.swing.JButton();
        jbtnGuardar = new javax.swing.JButton();
        jbtnEditar = new javax.swing.JButton();
        jbtnEliminar = new javax.swing.JButton();
        jbtnCancelar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblVehiculos = new javax.swing.JTable();
        jtblVehiculos.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 14));
        jtxtFiltrar = new javax.swing.JTextField();
        jbtnFiltrar = new javax.swing.JButton();
        jbtnMostrar = new javax.swing.JButton();

        jRadioButton1.setText("jRadioButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Roboto Black", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Gestión de Vehículos");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 380, -1));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jtxtFMarca.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jtxtFModelo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jtxtFChasis.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jtxtFColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jtxtFAnio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel2.setFont(new java.awt.Font("Roboto SemiBold", 0, 14)); // NOI18N
        jLabel2.setText("Modelo");

        jLabel3.setFont(new java.awt.Font("Roboto SemiBold", 0, 14)); // NOI18N
        jLabel3.setText("Marca");

        jLabel4.setFont(new java.awt.Font("Roboto SemiBold", 0, 14)); // NOI18N
        jLabel4.setText("Chasis");

        jLabel6.setFont(new java.awt.Font("Roboto SemiBold", 0, 14)); // NOI18N
        jLabel6.setText("Color");

        jLabel7.setFont(new java.awt.Font("Roboto SemiBold", 0, 14)); // NOI18N
        jLabel7.setText("Año");

        jtxtFID.setForeground(new java.awt.Color(204, 204, 204));
        jtxtFID.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel10.setFont(new java.awt.Font("Roboto SemiBold", 0, 14)); // NOI18N
        jLabel10.setText("ID");

        jtxtFPlaca.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jtxtFPlaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtFPlacaActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Roboto SemiBold", 0, 14)); // NOI18N
        jLabel5.setText("Placa");

        jLabel11.setFont(new java.awt.Font("Roboto SemiBold", 1, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Formulario");

        jbtnNuevo.setBackground(new java.awt.Color(0, 86, 179));
        jbtnNuevo.setFont(new java.awt.Font("Roboto SemiBold", 0, 14)); // NOI18N
        jbtnNuevo.setForeground(new java.awt.Color(255, 255, 255));
        jbtnNuevo.setText("Nuevo");
        jbtnNuevo.setBorderPainted(false);
        jbtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNuevoActionPerformed(evt);
            }
        });

        jbtnGuardar.setBackground(new java.awt.Color(0, 86, 179));
        jbtnGuardar.setFont(new java.awt.Font("Roboto SemiBold", 0, 14)); // NOI18N
        jbtnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        jbtnGuardar.setText("Guardar");
        jbtnGuardar.setBorderPainted(false);
        jbtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGuardarActionPerformed(evt);
            }
        });

        jbtnEditar.setBackground(new java.awt.Color(0, 86, 179));
        jbtnEditar.setFont(new java.awt.Font("Roboto SemiBold", 0, 14)); // NOI18N
        jbtnEditar.setForeground(new java.awt.Color(255, 255, 255));
        jbtnEditar.setText("Editar");
        jbtnEditar.setBorderPainted(false);
        jbtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEditarActionPerformed(evt);
            }
        });

        jbtnEliminar.setBackground(new java.awt.Color(0, 86, 179));
        jbtnEliminar.setFont(new java.awt.Font("Roboto SemiBold", 0, 14)); // NOI18N
        jbtnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        jbtnEliminar.setText("Eliminar");
        jbtnEliminar.setBorderPainted(false);
        jbtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEliminarActionPerformed(evt);
            }
        });

        jbtnCancelar.setBackground(new java.awt.Color(0, 86, 179));
        jbtnCancelar.setFont(new java.awt.Font("Roboto SemiBold", 0, 14)); // NOI18N
        jbtnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        jbtnCancelar.setText("Cancelar");
        jbtnCancelar.setBorderPainted(false);
        jbtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCancelarActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Roboto SemiBold", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Opciones");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtxtFColor, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtxtFID, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jtxtFModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jtxtFAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtxtFChasis, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(jtxtFPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtxtFMarca)))
                        .addGap(61, 61, 61))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(216, 216, 216)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(jbtnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jbtnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jbtnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jbtnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jbtnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(93, 93, 93))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(jtxtFID, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jtxtFPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jtxtFChasis, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jtxtFMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jtxtFModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jtxtFAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(jtxtFColor, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(25, 25, 25))))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 50, 780, 350));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jScrollPane1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N

        jtblVehiculos.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
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
        jtblVehiculos.setShowGrid(false);
        jtblVehiculos.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(jtblVehiculos);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 450, 780, 220));

        jtxtFiltrar.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jtxtFiltrar.setForeground(new java.awt.Color(204, 204, 204));
        jtxtFiltrar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
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
        jPanel1.add(jtxtFiltrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 410, 420, 26));

        jbtnFiltrar.setBackground(new java.awt.Color(0, 86, 179));
        jbtnFiltrar.setFont(new java.awt.Font("Roboto SemiBold", 0, 14)); // NOI18N
        jbtnFiltrar.setForeground(new java.awt.Color(255, 255, 255));
        jbtnFiltrar.setText("Buscar");
        jbtnFiltrar.setBorderPainted(false);
        jbtnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnFiltrarActionPerformed(evt);
            }
        });
        jPanel1.add(jbtnFiltrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 410, 80, 26));

        jbtnMostrar.setBackground(new java.awt.Color(0, 86, 179));
        jbtnMostrar.setFont(new java.awt.Font("Roboto SemiBold", 0, 14)); // NOI18N
        jbtnMostrar.setForeground(new java.awt.Color(255, 255, 255));
        jbtnMostrar.setText("Mostrar todo");
        jbtnMostrar.setBorderPainted(false);
        jbtnMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnMostrarActionPerformed(evt);
            }
        });
        jPanel1.add(jbtnMostrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 410, 120, 26));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtFPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtFPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtFPlacaActionPerformed

    private void jbtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGuardarActionPerformed
        guardarVehiculo();
    }//GEN-LAST:event_jbtnGuardarActionPerformed

    private void jbtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNuevoActionPerformed
        btnNuevo();
        textoNuevo();
        jtxtFPlaca.requestFocus();
    }//GEN-LAST:event_jbtnNuevoActionPerformed

    private void jbtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelarActionPerformed
        textoInicio();
        btnInicio();
        limpiarTexto();
    }//GEN-LAST:event_jbtnCancelarActionPerformed

    private void jbtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEditarActionPerformed
        editarVehiculo();
        textosEditar();
        botonesEditar();
    }//GEN-LAST:event_jbtnEditarActionPerformed

    private void jbtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEliminarActionPerformed
        eliminarVehiculo();
    }//GEN-LAST:event_jbtnEliminarActionPerformed

    private void jtxtFiltrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtFiltrarMouseClicked

    }//GEN-LAST:event_jtxtFiltrarMouseClicked

    private void jbtnMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnMostrarActionPerformed
        obtenerVehiculos();
        jbtnFiltrar.setEnabled(true);
        jbtnMostrar.setEnabled(false);
        jtxtFiltrar.setText("");
    }//GEN-LAST:event_jbtnMostrarActionPerformed

    private void jbtnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnFiltrarActionPerformed
        buscarVehiculo();
    }//GEN-LAST:event_jbtnFiltrarActionPerformed

    private void jtxtFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtFiltrarActionPerformed

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

                try {
                    // En lugar de 'new ...', usa el nombre del paquete como texto
                    javax.swing.UIManager.setLookAndFeel("com.formdev.flatlaf.FlatIntelliJLaf");
                } catch (Exception ex) {
                    System.err.println("Error al cargar el tema");
                }

                new FormularioRegistro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
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
