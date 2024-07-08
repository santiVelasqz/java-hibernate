/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accesoHibernate;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author capitan
 */
public class MetodosParaIncidencias {
    
    Transaction tx = null;    
    Scanner sc = new Scanner(System.in);
    
    //----------ESTE METODO OBTENEMOS LA INCIDENCIA POR ID----------------------
    
    public Incidencias obtenerIncidenciaPorId() {
    Session sesion = null;
    Incidencias incidencia = null;
        try {
            sesion = HibernateUtil.getSessionFactory().openSession();
            System.out.println("Introduce el id de la incidencia: ");
            int id = sc.nextInt();
            sc.nextLine();
            incidencia = (Incidencias) sesion.get(Incidencias.class, id);
            if (incidencia != null) {
                System.out.println("Incidencia encontrada:");
                System.out.println("ID: " + incidencia.getIdIncidencia());
                System.out.println("Fecha y Hora de Generación: " + incidencia.getFechaHoraGeneracion());
                System.out.println("Empleado Origen: " + incidencia.getEmpleadoOrigen());
                System.out.println("Empleado Destino: " + incidencia.getEmpleadoDestino());
                System.out.println("Detalle de la Incidencia: " + incidencia.getDetalleIncidencia());
                System.out.println("Tipo de Incidencia: " + (incidencia.getTipoIncidencia() == 'U' ? "Urgente" : "Normal"));
            } else {
                System.out.println("No se encontró ninguna incidencia con el ID proporcionado.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sesion != null && sesion.isOpen()) {
                sesion.close(); // Cerrar la sesión al finalizar si está abierta
            }
        }
        return incidencia;
    }
    
    //------ METODO DE MOSTRAR INCIDENCIAS-----------
    public void mostrarIncidencias() {
    Session sesion = null;
        try {
            sesion = HibernateUtil.getSessionFactory().openSession();   
            Query query = sesion.createQuery("select i from Incidencias i");
            List<Incidencias> listaIncidencias = query.list();

            if (listaIncidencias.isEmpty()) {
                System.out.println("No hay incidencias registradas.");
            } else {
                for (Incidencias incidencia : listaIncidencias) {
                    System.out.println("ID: " + incidencia.getIdIncidencia());
                    System.out.println("Fecha y Hora de Generación: " + incidencia.getFechaHoraGeneracion());
                    Empleados empleadoOrigen = incidencia.getEmpleadosByIdEmpleadoOrigen();
                    Empleados empleadoDestino = incidencia.getEmpleadosByIdEmpleadoDestino();
                    System.out.print("Empleado Origen ID: ");
                    if (empleadoOrigen != null) {
                        System.out.println(empleadoOrigen.getNombreCompleto());
                    } else {
                        System.out.println("Empleado Origen no asignado");
                    }
                    System.out.print("Empleado Destino ID: ");
                    if (empleadoDestino != null) {
                        System.out.println(empleadoDestino.getNombreCompleto());
                    } else {
                        System.out.println("Empleado Destino no asignado");
                    }
                    System.out.println("Detalle de la Incidencia: " + incidencia.getDetalleIncidencia());
                    System.out.println("Tipo de Incidencia: " + incidencia.getTipoIncidencia());
                    System.out.println("--------------------------------------");
                    sesion.refresh(incidencia);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sesion != null && sesion.isOpen()) {
                sesion.close(); // Cerrar la sesión al finalizar
            }
        }
    }
    
    //-----ESTE METODO SE UTILIZAN PARA OBTENER LOS EMPLEADOS Y LUEGO USARLO EN anadirIncidencias() -------

    public Empleados obtenerEmpleado(Session sesion, String usuarioIngresado) {
        Empleados empleado = null;
        try {
            Query query = sesion.createQuery("FROM Empleados WHERE nombre_completo = :nombreEmpleado");
            query.setParameter("nombreEmpleado", usuarioIngresado);
            empleado = (Empleados) query.uniqueResult();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return empleado;
    }
    
    public void anadirIncidencia() {
        Session sesion = null;
        Transaction tx = null;
        Scanner sc = new Scanner(System.in);
        try {
            sesion = HibernateUtil.getSessionFactory().openSession();
            tx = sesion.beginTransaction();
            System.out.println("Ingrese el empleado de origen:");
            String nombreOrigen = sc.nextLine();
            System.out.println("Ingrese el empleado de destino:");
            String nombreDestino = sc.nextLine();
            System.out.println("Ingrese el detalle de la incidencia");
            String detalle = sc.nextLine();
            System.out.println("Ingrese el tipo de incidencia: U- Urgente / N- Normal:");
            char tipo = sc.next().charAt(0);
            Empleados emOrigen = obtenerEmpleado(sesion, nombreOrigen);
            Empleados emDestino = obtenerEmpleado(sesion, nombreDestino);

            if (emOrigen != null && emDestino != null) {
                Incidencias e = new Incidencias();
                e.setEmpleadosByIdEmpleadoOrigen(emOrigen);
                e.setEmpleadosByIdEmpleadoDestino(emDestino);
                e.setFechaHoraGeneracion(Date.from(Instant.now()));
                e.setEmpleadoOrigen(nombreOrigen);
                e.setEmpleadoDestino(nombreDestino);
                e.setDetalleIncidencia(detalle);
                e.setTipoIncidencia(tipo);
                sesion.save(e);
                System.out.println("La incidencia ha sido creada");
                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback(); // En caso de error, se deshace la transacción
            }
            e.printStackTrace();
        } finally {
            if (sesion != null && sesion.isOpen()) {
                sesion.close(); // Se cierra la sesión al finalizar
            }
        }
    }

//-------- METODO PARA OBTENER INCIDENCIA EMPLEADO ORIGEN --------------
    
    public void incidenciasPorEmpleadoOrigen() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Ingrese el nombre del empleado de origen: ");
    String empleadoOrigen = scanner.nextLine();     
    Session sesion = null;
        try {
            sesion = HibernateUtil.getSessionFactory().openSession();   
            Query query = sesion.createQuery("select i from Incidencias i where i.empleadoOrigen = :empleadoOrigen");
            query.setParameter("empleadoOrigen", empleadoOrigen);
            List<Incidencias> listaIncidencias = query.list();
            if (listaIncidencias.isEmpty()) {
                System.out.println("No hay incidencias registradas para el empleado de origen " + empleadoOrigen + ".");
            } else {
                for (Incidencias incidencia : listaIncidencias) {
                    System.out.println("ID: " + incidencia.getIdIncidencia());
                    System.out.println("Fecha y Hora de Generación: " + incidencia.getFechaHoraGeneracion());
                    System.out.println("Empleado Origen: " + incidencia.getEmpleadoOrigen());
                    System.out.println("Empleado Destino: " + incidencia.getEmpleadoDestino());
                    System.out.println("Detalle de la Incidencia: " + incidencia.getDetalleIncidencia());
                    System.out.println("Tipo de Incidencia: " + (incidencia.getTipoIncidencia() == 'U' ? "Urgente" : "Normal"));
                    System.out.println("--------------------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sesion != null && sesion.isOpen()) {
                sesion.close(); // Cerrar la sesión al finalizar
            }
        }
    }
    
//-------- METODO PARA OBTENER INCIDENCIA EMPLEADO DESTINO --------------
    
    public void incidenciasPorEmpleadoDestino() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Ingrese el nombre del empleado de destino: ");
    String empleadoDestino = scanner.nextLine();    
    Session sesion = null;
        try {
            sesion = HibernateUtil.getSessionFactory().openSession();   
            Query query = sesion.createQuery("select i from Incidencias i where i.empleadoDestino = :empleadoDestino");
            query.setParameter("empleadoDestino", empleadoDestino);
            List<Incidencias> listaIncidencias = query.list();
            if (listaIncidencias.isEmpty()) {
                System.out.println("No hay incidencias registradas para el empleado de origen " + empleadoDestino + ".");
            } else {
                for (Incidencias incidencia : listaIncidencias) {
                    System.out.println("ID: " + incidencia.getIdIncidencia());
                    System.out.println("Fecha y Hora de Generación: " + incidencia.getFechaHoraGeneracion());
                    System.out.println("Empleado Origen: " + incidencia.getEmpleadoOrigen());
                    System.out.println("Empleado Destino: " + incidencia.getEmpleadoDestino());
                    System.out.println("Detalle de la Incidencia: " + incidencia.getDetalleIncidencia());
                    System.out.println("Tipo de Incidencia: " + (incidencia.getTipoIncidencia() == 'U' ? "Urgente" : "Normal"));
                    System.out.println("--------------------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sesion != null && sesion.isOpen()) {
                sesion.close(); // Cerrar la sesión al finalizar
            }
        }
    }
}
