/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accesoHibernate;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Santiago Marin Velasquez
 */
public class MetodosParaEmpleados{ 
   // Session sesion = HibernateUtil.getSessionFactory().openSession();
    //Transaction tx = null;    
    Scanner sc = new Scanner(System.in);
    
    //----------------METODO PARA MOSTRAR EMPLEADOS--------------------
    
    public void mostrarEmpleados() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Query query = sesion.createQuery("select e from Empleados e"); // Consulta para seleccionar todos los empleados
    List<Empleados> listaEmpleados = query.list();
    
        if (listaEmpleados.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {
            for (Empleados empleado : listaEmpleados) {
                System.out.println("ID: " + empleado.getIdEmpleado()
                        + " | Usuario: " + empleado.getNombreUsuario() + " | Contraseña: " + empleado.getContrasena() +
                        " | Nombre: " + empleado.getNombreCompleto() + " | Telefono: " + empleado.getTelefonoContacto());          
                System.out.println("-----------------------------------------------------------------------------------");
            }
        }
    }

    //------ METODO PARA AÑADIR EMPLEADOS-----------------------  
    
    public void anadirEmpleado(){
    Session sesion = null;
    Transaction tx = null;
        try {
            sesion = HibernateUtil.getSessionFactory().openSession();
            tx = sesion.beginTransaction();
            System.out.println("Ingrese el nombre de usuario:");
            String usuario = sc.nextLine();
            System.out.println("Ingrese la contraseña");
            String contra = sc.nextLine();
            System.out.println("Ingrese el nombre del empleado");
            String nombre = sc.nextLine();
            System.out.println("Ingrese el teléfono");
            String telefono = sc.nextLine();

            Empleados e = new Empleados(usuario, contra, nombre, telefono);
            sesion.save(e);
            System.out.println("El empleado " + e.getNombreCompleto() + " ha sido creado");
            tx.commit();
            sesion.refresh(e);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Deshacer la transacción si está activa
            }
            e.printStackTrace();
        } finally {
            if (sesion != null && sesion.isOpen()) {
                sesion.close(); // Cerrar la sesión al finalizar si está abierta
            }
        }
    }

//----------- METODO PARA VALIDAR EMPLEADO-----------------------------
    
    public boolean validarEmpleado(){
        boolean acceso = false;
        int intentos = 3;
        do {
            System.out.println("Introduce el usuario: ");
            String usuario = sc.nextLine();
            System.out.println("Introduce la contraseña:");
            String contrasena = sc.nextLine();
            Empleados em = obtenerDatosEmpleado(usuario);
            if (em != null && em.getContrasena().equals(contrasena)) {
                System.out.println("Acceso concedido. Bienvenido/a " + em.getNombreCompleto()+"!");
                acceso = true;
                break;
            } else {
                intentos--;
                System.out.println("Usuario o contraseña incorrectos. Intentos restantes: " + intentos);
            }
        } while (intentos > 0);
        if (!acceso) {
            System.out.println("Se agotaron los intentos. Volviendo al menú principal.");
        }
        return acceso;
    }
    
    // ESTE METODO PERMITE OBETENER LOS DATOS PARA PODER VALIDARLO EN EL METODO DE validarEmpleado()
    
    public Empleados obtenerDatosEmpleado(String usuarioIngresado) {
        Session sesion = null;
        Transaction tx = null;
        Empleados empleado = null;
        try {
            sesion = HibernateUtil.getSessionFactory().openSession();
            tx = sesion.beginTransaction();
            // Realizo la consulta para obtener los datos del empleado basado en el usuario ingresado
            Query query = sesion.createQuery("FROM Empleados WHERE nombre_usuario = :usuario");
            query.setParameter("usuario", usuarioIngresado);
            empleado = (Empleados) query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (sesion != null) {
                tx.rollback();// Deshacer la transacción si está activa
            }
            e.printStackTrace();
        } finally {
            if (sesion != null && sesion.isOpen()) {
                sesion.close();// Cerrar la sesión al finalizar si está abierta
            }
        }
        return empleado;
    }

    // --------- METODO PARA MODIFICAR EMPLEADO------------------------
    public void modificarEmpleado() {
    Session sesion = null;
    Transaction tx = null; 
        try {
            sesion = HibernateUtil.getSessionFactory().openSession();
            tx = sesion.beginTransaction();
            System.out.println("Ingrese id del empleado a modificar: ");
            int id = sc.nextInt();
            sc.nextLine();
            Empleados empleado = (Empleados) sesion.get(Empleados.class, id); // Recuperar el empleado por su ID

            if (empleado != null) {
                System.out.println("Ingrese el nuevo nombre de usuario:");
                String usuario = sc.nextLine();
                System.out.println("Ingrese la nueva contraseña");
                String contra = sc.nextLine();
                System.out.println("Ingrese el nuevo nombre del empleado");
                String nombre = sc.nextLine();
                System.out.println("Ingrese el nuevo teléfono");
                String telefono = sc.nextLine();

                // Actualizar los datos del empleado
                empleado.setNombreUsuario(usuario);
                empleado.setContrasena(contra);
                empleado.setNombreCompleto(nombre);
                empleado.setTelefonoContacto(telefono);

                sesion.update(empleado); // Actualizar el empleado en la base de datos
                System.out.println("Los datos del empleado con id " + empleado.getIdEmpleado() + " han sido modificados correctamente.");
                tx.commit();
                sesion.refresh(empleado);
            } else {
                System.out.println("No se encontró ningún empleado con ese ID.");
            }
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Deshacer la transacción si está activa
            }
            ex.printStackTrace();
        } finally {
            if (sesion != null && sesion.isOpen()) {
                sesion.close(); // Cerrar la sesión al finalizar si está abierta
            }
        }
    }

    // --------- METODO PARA MODIFICAR CONTRASEÑA EMPLEADO ---------------
    
    public void modificarContrasenaEmpleado() {
        Session sesion = null;
        Transaction tx = null;
        try {
            sesion = HibernateUtil.getSessionFactory().openSession();
            tx = sesion.beginTransaction();
            System.out.println("Ingrese el ID del empleado para modificar su contraseña: ");
            int id = sc.nextInt();
            sc.nextLine();
            Empleados empleado = (Empleados) sesion.get(Empleados.class, id); // Recuperar el empleado por su ID

            if (empleado != null) {
                System.out.println("Ingrese la nueva contraseña:");
                String nuevaContrasena = sc.nextLine();

                // Actualizar solo la contraseña del empleado
                empleado.setContrasena(nuevaContrasena);

                sesion.update(empleado); // Actualizar la contraseña del empleado en la base de datos
                System.out.println("La contraseña del empleado con ID " + empleado.getIdEmpleado() + " ha sido modificada correctamente.");
                tx.commit();
                sesion.refresh(empleado);
            } else {
                System.out.println("No se encontró ningún empleado con ese ID.");
            }
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) {
            tx.rollback(); // Deshacer la transacción si está activa
        }
            ex.printStackTrace();
        } finally {
            if (sesion != null && sesion.isOpen()) {
                sesion.close(); // Cerrar la sesión al finalizar si está abierta
            }
        }
    }
    
    // ---------- METODO PARA BORRAR EMPLEADO -------------------------
    
    public void borrarEmpleado(){
        Session sesion = null;
        Transaction tx = null;
        try{
            sesion = HibernateUtil.getSessionFactory().openSession();
            tx = sesion.beginTransaction();
            System.out.println("Ingrese id del empleado a eliminar: ");
            int id = sc.nextInt();
            sc.nextLine();
            Empleados empleado = (Empleados) sesion.get(Empleados.class, id); // Recuperar el empleado por su ID        
            if (empleado != null) {
                Query query = sesion.createQuery("DELETE FROM Incidencias WHERE id_empleado_destino = :id");
                query.setParameter("id", id);
                int numIncidenciasEliminadas = query.executeUpdate();
                System.out.println("Se han eliminado " + numIncidenciasEliminadas + " incidencias asociadas al empleado con ID " + id);

                
                sesion.delete(empleado); // Eliminar el empleado recuperado
                System.out.println("Empleado " + empleado.getNombreCompleto() + " con id " + empleado.getIdEmpleado() + " ha sido borrado");
                tx.commit();
            } else {
                System.out.println("No se encontró ningún empleado con ese ID.");
            }
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Deshacer la transacción si está activa
            }
            ex.printStackTrace();
        } finally {
            if (sesion != null && sesion.isOpen()) {
                sesion.close(); // Cerrar la sesión al finalizar si está abierta
            }
        }       
    }
       
}
