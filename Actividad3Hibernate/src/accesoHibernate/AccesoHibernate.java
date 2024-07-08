/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accesoHibernate;


import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Santiago Marin Velasquez
 */
public class AccesoHibernate {
    
    public static void main(String[] args) {
        Scanner sc =  new Scanner(System.in);
        MetodosParaEmpleados metod = new MetodosParaEmpleados();   
        boolean salida = false; 
        int opciones;       
        do{
            try{               
                menu();
                System.out.println("----------");
                // el método pideEntero con el que se elije la opción que quiero.
                opciones = pideEntero("Escribe un número entero: ");
                switch (opciones){  
                //En el case 1 invoco el método para añadir un empleado
                    case 1:                        
                        System.out.println();
                        metod.anadirEmpleado();
                        System.out.println("----------");
                        break;
                //En el case 2 invoco el método para validar el usuario y contraseña                  
                    case 2:
                        
                        System.out.println();
                        metod.mostrarEmpleados();
                        System.out.println();
                        if (metod.validarEmpleado()) {
                        menuSecundario();
                        }                      
                        System.out.println("----------");
                        break;
                //En el case 3 invoco el método para modificar los datos de un empleado
                    case 3:                        
                        System.out.println();
                        metod.mostrarEmpleados();
                        System.out.println();
                        metod.modificarEmpleado();
                        System.out.println("----------");
                        break;
                // En el case 4 invoco el método para cambiar la contraseña de un empleado
                    case 4:                       
                        System.out.println();
                        metod.mostrarEmpleados();
                        System.out.println();
                        metod.modificarContrasenaEmpleado();
                        System.out.println("----------");
                        break;
                // En el case 5 invoco el método para eliminar un empleado                  
                    case 5:                       
                        System.out.println();
                        metod.mostrarEmpleados();
                        metod.borrarEmpleado();
                        System.out.println("----------");
                        break;
                //En el case 6 se sale del programa
                    case 6:
                        System.out.println("Ha seleccionado salir.");
                        System.out.println("Ha salido del programa. Hasta la próxima");
                        salida = true;
                        break;
                //Por si se escribe un número que se sale del rango de opciones                    
                    default:                        
                        System.out.println("El número introducido no se encuentra dentro de las opciones. Inténtalo de nuevo");
                        System.out.println("----------");
                        break;            
                }
            }catch(InputMismatchException e){
                System.out.println("El valor introducido no es numérico. Por favor, introduzca un número entero");
                System.out.println("");
            }
        } while (!salida); 
        System.exit(0);
    }
    
    //Método para mostrar el menú en pantalla
    public static void menu(){
        System.out.println("\n===== Menú ====");
        System.out.println("Elija una de las siguientes opciones: ");
        System.out.println("");
        System.out.println("1- Insertar empleado nuevo.");
        System.out.println("2- Acceder al sistema de incidencias");
        System.out.println("3- Modificar perfil de un empleado.");
        System.out.println("4- Cambiar la contraseña de un empleado.");
        System.out.println("5- Eliminar un empleado.");
        System.out.println("6- salir.");
    }
    // MENU QUE VA DENTRO DE LA VALIDACION
        public static void subMenu(){
        System.out.println("\n===== Submenú ====");
        System.out.println("Elija una de las siguientes opciones: ");
        System.out.println("");
        System.out.println("1- Obtener un objeto Incidencia a partir de su Id.");
        System.out.println("2- Obtener la lista de todas las incidencias");
        System.out.println("3- Insertar una incidencia.");
        System.out.println("4- Obtener las incidencias creadas por un empleado concreto.");
        System.out.println("5- Obtener las incidencias destinadas para un empleado .");
        System.out.println("6- salir.");
    }
    
    //Método para pedir que ingrese un número entero
    public static int pideEntero(String pregunta){
        int entero = 0;    
        do{
            try{
                System.out.println(pregunta);
                Scanner input = new Scanner (System.in);
                entero = input.nextInt();
            }catch(InputMismatchException e){
                    System.out.println("El valor introducido no es numérico");
                    System.out.println("Por favor, introduzca un número");
                    System.out.println("");
            }
        }while (entero <=0);
        return entero;
    }

    // METODO DEL MENU SECUNDARIO
    public static void menuSecundario() {
        MetodosParaEmpleados metod = new MetodosParaEmpleados();
        MetodosParaIncidencias in = new MetodosParaIncidencias();
        boolean regresar = false;
        do {
            try {
                subMenu();
                int opcionSubmenu = pideEntero("Escribe un número entero: ");

                switch (opcionSubmenu) {
                    //En el case 1 invoco el método para obtener incidencia por id
                    case 1:                       
                        System.out.println();
                        in.obtenerIncidenciaPorId();
                        System.out.println("----------");
                        break;
                //En el case 2 invoco el método para mostrar incidencias                
                    case 2:                      
                        System.out.println();
                        in.mostrarIncidencias();
                        System.out.println("----------");
                        break;
                //En el case 3 invoco el método para añadir una incidencia
                    case 3:                        
                        System.out.println();
                        metod.mostrarEmpleados();
                        System.out.println();
                        in.anadirIncidencia();
                        System.out.println();
                        
                        System.out.println("----------");
                        break;
                // En el case 4 invoco el método para mostrar incidencias dependiendo del empleado de origen
                    case 4:                       
                        System.out.println();                        
                        in.incidenciasPorEmpleadoOrigen();                       
                        System.out.println("----------");
                        break;
                // En el case 5 invoco el método mostrar incidencias dependiendo del empleado de destino                  
                    case 5:                      
                        System.out.println();
                        in.incidenciasPorEmpleadoDestino();
                        System.out.println("----------");
                        break;
                // En el case 6 vuelve al menu principal
                    case 6:
                        System.out.println("Ha seleccionado regresar.");
                        System.out.println("Ha vuelto al menú principal");
                        regresar = true; // Salir del submenú y regresar al menú principal
                        break;
                    default:
                        System.out.println("El número introducido no se encuentra dentro de las opciones. Inténtalo de nuevo");
                }
            } catch (InputMismatchException e) {
                System.out.println("El valor introducido no es numérico. Por favor, introduzca un número entero.");
                System.out.println("");
            }
        } while (!regresar);
    }

    
        
}

