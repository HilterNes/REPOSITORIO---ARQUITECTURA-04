/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.modelo;
import Vista.Registro;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class controlador implements ActionListener {
 
    Registro Vista;
    modelo Modelo;
    
    
    public controlador(Registro Vista, modelo Modelo) {
         this.Vista = Vista;
        this.Modelo = Modelo;
        this.Vista.btn_agregar.addActionListener(this);
        this.Vista.btn_eliminartodo.addActionListener(this);
        this.Vista.btn_eliminar.addActionListener(this);
        this.Vista.btn_actualizar.addActionListener(this);
        this.Vista.btn_guardar.addActionListener(this);
        this.Vista.btn_leer.addActionListener(this);
        this.FechaNacimiento().addPropertyChangeListener("date", evt -> calcularEdad());
    }
    
    public void iniciar(){
        Vista.setTitle("REGISTRO DE ALUMNOS");
        Vista.setSize(650, 590);
        Vista.setLocationRelativeTo(null);
}

    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource()== Vista.btn_agregar){
            agregar(Vista.tblregistro);   
        }
        
        if(e.getSource()== Vista.btn_eliminartodo){
            eliminarTodo(Vista.tblregistro);
       }
        
        if(e.getSource()== Vista.btn_eliminar){
            elimninar(Vista.tblregistro);
        }
        
        
        if(e.getSource()== Vista.btn_actualizar){
            actualizar(Vista.tblregistro);
        }
        
        if(e.getSource()== Vista.btn_guardar){
            guardarDatos(Vista.tblregistro);
        }
        
        if(e.getSource()== Vista.btn_leer){
            leer(Vista.tblregistro);
        }
        
    }

     public void agregar(JTable tblregistro) {
        try {     
        modelo alumno = new modelo();
        alumno.setNombre(Vista.txtnombre.getText());
        alumno.setCodigo(Vista.txtcodigo.getText());
        alumno.setDireccion(Vista.txtdireccion.getText());
        alumno.setEdad(Integer.parseInt(Vista.txtedad.getText()));
        alumno.setFecha(Vista.JDC_Fecha.getDate());
        Vista.listaAlumno.add(alumno);
        Vista.refrescarTabla();
      } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(null, "ERROR AL AGREGAR ALUMNO");
      }
        Vista.txtnombre.setText("");
        Vista.txtcodigo.setText("");
        Vista.txtdireccion.setText("");
        Vista.txtedad.setText("");
        Vista.JDC_Fecha.setDate(null);
    }

   public void eliminarTodo(JTable tblregistro) {
        DefaultTableModel modelo = (DefaultTableModel) tblregistro.getModel();
        int eliminartbl = tblregistro.getRowCount();
        for (int i = eliminartbl - 1; i >= 0; i--) {
            modelo.removeRow(i);
        }
    }
    
    public void elimninar(JTable tblregistro) {
        int filaselect = tblregistro.getSelectedRow();
        if (filaselect != -1) {
            DefaultTableModel modelo = (DefaultTableModel) tblregistro.getModel();
            modelo.removeRow(filaselect);
        } else {
            JOptionPane.showMessageDialog(null, "ERROR FILA NO SELECCIONADA");
        }
      }
    
    public void actualizar(JTable tblregistro) {
        int filaselect = tblregistro.getSelectedRow();
        if (filaselect >= 0) {
            Vista.txtnombre.setText(tblregistro.getValueAt(filaselect, 0).toString());
            Vista.txtcodigo.setText(tblregistro.getValueAt(filaselect, 1).toString());
            Vista.txtdireccion.setText(tblregistro.getValueAt(filaselect, 2).toString());
            Vista.txtedad.setText(tblregistro.getValueAt(filaselect, 3).toString());
            Vista.JDC_Fecha.setDate((Date) tblregistro.getValueAt(filaselect, 4));
            DefaultTableModel modelo = (DefaultTableModel) tblregistro.getModel();
            modelo.removeRow(filaselect);
        } else {
            JOptionPane.showMessageDialog(null, "SELECCIONAR CAMPO");
        }
    }
    
    public JDateChooser FechaNacimiento() {
        return Vista.JDC_Fecha;
    }
    
    public JTextField getTxtEdad() {
        return Vista.txtedad;
    }
    
  private void calcularEdad() {
        Date fechaNacimiento = FechaNacimiento().getDate();
        if (fechaNacimiento != null) {
            // Obtener la fecha actual
            Date fechaActual = new Date();

            // Calcular la edad
            int edad = calcularEdad(fechaNacimiento, fechaActual);

            // Mostrar la edad en el campo txtEdad
            getTxtEdad().setText(String.valueOf(edad));
        }
    }

    private int calcularEdad(Date fechaNacimiento, Date fechaActual) {
        java.util.Calendar fechaNac = java.util.Calendar.getInstance();
        fechaNac.setTime(fechaNacimiento);

        java.util.Calendar fechaAct = java.util.Calendar.getInstance();
        fechaAct.setTime(fechaActual);

        int edad = fechaAct.get(java.util.Calendar.YEAR) - fechaNac.get(java.util.Calendar.YEAR);

        // Ajustar si aún no ha cumplido años este año
        if (fechaAct.get(java.util.Calendar.DAY_OF_YEAR) < fechaNac.get(java.util.Calendar.DAY_OF_YEAR)) {
            edad--;
        }

        return edad;
    }
    
    public void guardarDatos(JTable tblregistro) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("personas.txt"))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Formato de fecha
            for (modelo persona : Vista.listaAlumno) {
                writer.write(persona.getNombre()+ "," + persona.getCodigo()+ "," + persona.getDireccion() + "," + persona.getEdad()+ "," + sdf.format(persona.getFecha()));
                writer.newLine();
            }
            JOptionPane.showMessageDialog(Vista, "Datos guardados correctamente.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(Vista, "Error al guardar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void leer(JTable tblregistro) {
    DefaultTableModel model = (DefaultTableModel) tblregistro.getModel();
    model.setRowCount(0); // Limpiar la tabla antes de agregar los datos

    try (BufferedReader reader = new BufferedReader(new FileReader("personas.txt"))) {
        String line;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Formato de fecha
        while ((line = reader.readLine()) != null) {
            String[] datos = line.split(","); // Separar los datos por coma
            // Convertir la fecha a Date (si es necesario)
            Date fechaNacimiento = sdf.parse(datos[4]);

            // Crear una nueva fila con los datos leídos
            Object[] fila = {
                datos[0], // Nombre
                datos[1], // Código
                datos[2], // Dirección
                Integer.parseInt(datos[3]), // Edad
                fechaNacimiento // Fecha de nacimiento
            };

            model.addRow(fila); // Añadir la fila a la tabla
        }
    } catch (IOException | ParseException e) {
        JOptionPane.showMessageDialog(null, "Error al leer los datos.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
    
}
