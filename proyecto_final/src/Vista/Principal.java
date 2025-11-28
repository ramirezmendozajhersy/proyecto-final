/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Controlador.ControladorOrdenes;
import Controlador.ControladorOrdenes.ExcepcionProcesamientoDatos;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 *
 * @author USER
 */
public class Principal {

    
    
    private static final String NOMBRE_ARCHIVO_DATOS = "Data/Datos.txt";
    private static final String NOMBRE_ARCHIVO_USUARIO = "Data/Usuario.txt";

    public static void main(String[] args) {
        
       
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("\ndd/MM/yyyy HH:mm:ss a");
        String fechaHoraActual = ahora.format(formato);
     

        ControladorOrdenes controlador = new ControladorOrdenes();
      
        Scanner lector = new Scanner(System.in); 

        System.out.println("--- EJECUCION REPORTES ---");
        System.out.println("Fecha y Hora : " + fechaHoraActual); 
        
        
        String usuarioIngresado = "";
        String contraseñaIngresada = "";

        try {
            System.out.print("\nIngrese su nombre de usuario: ");
            usuarioIngresado = lector.nextLine();
    
            System.out.print("Ingrese su contraseña: ");
            contraseñaIngresada = lector.nextLine();
        
            

           
            boolean autenticado = controlador.validarCredenciales(usuarioIngresado, contraseñaIngresada, NOMBRE_ARCHIVO_USUARIO);

            if (autenticado) {
                System.out.println("\nLogin exitoso. Acceso concedido.");
                mostrarMenuReportes(controlador, lector); 
                
            } else {
                System.out.println("\n ERROR DE AUTENTICACION: Usuario o contraseña incorrectos.");
            }

        } catch (ExcepcionProcesamientoDatos e) {
         
            System.out.println("\n\n ERROR CRITICO DEL SISTEMA:");
            System.out.println("Mensaje: " + e.getMessage());
            if (e.esCritico()) {
                System.out.println("Tipo: ERROR FATAL. La ejecucion ha sido detenida.");
            }
        } catch (Exception e) {
            System.out.println("\nError inesperado durante el login: " + e.getMessage());
        } finally {
           
      
            System.out.println("\n--- Proceso finalizado. ---");
        }
    }


   
    private static void mostrarMenuReportes(ControladorOrdenes controlador, Scanner scanner) {
        int opcion = -1;
        
       
        while (opcion != 0) {
            
            System.out.println("------------------------------------------------");
            System.out.println("         MEN DE REPORTES ");
            System.out.println("------------------------------------------------");
            System.out.println("1. Reporte anual (Monto total y productos)");
            System.out.println("2. Reporte productos comprados por categoria");
            System.out.println("3. Reporte monto total por metodo de pago");
            System.out.println("4. Reporte conteo de ordenes por estado");
            System.out.println("5. Reporte monto total por ciudad de entrega");
            System.out.println("6. Reporte conteo de ordenes por proveedor");
            System.out.println("7. Reporte anual por año fiscal "); 
            System.out.println("0. Salir del sistema");
            System.out.println("------------------------------------------------");
            System.out.print("Seleccione una opcion: ");
            
          
            String entrada = scanner.nextLine();
            
            try {
               
                opcion = Integer.parseInt(entrada);

               
                switch (opcion) {
                    case 1:
                        controlador.generarReporteAnual(NOMBRE_ARCHIVO_DATOS);
                        break;
                    case 2:
                        controlador.generarReportePorCategoria(NOMBRE_ARCHIVO_DATOS);
                        break;
                    case 3:
                        controlador.generarReportePorMetodoPago(NOMBRE_ARCHIVO_DATOS);
                        break;
                    case 4:
                        controlador.generarReportePorEstado(NOMBRE_ARCHIVO_DATOS);
                        break;
                    case 5:
                        controlador.generarReportePorCiudad(NOMBRE_ARCHIVO_DATOS);
                        break;
                    case 6:
                        controlador.generarReportePorProveedor(NOMBRE_ARCHIVO_DATOS);
                        break;
                    case 7: 
                        System.out.print("Ingrese el Año Fiscal a consultar (ej: 2025): ");
                        String añoStr = scanner.nextLine();
                        int añoFiltro = Integer.parseInt(añoStr.trim());
                        controlador.generarReportePorAñoFiscal(NOMBRE_ARCHIVO_DATOS, añoFiltro);
                        break;
                    case 0:
                        System.out.println("\nCerrando sesion y saliendo del sistema...");
                        break;
                    default:
                     
                        System.out.println("Opcion invalida. Por favor, ingrese un numero del 0 al 7"); 
                        break;
                }
            
          
            } catch (ExcepcionProcesamientoDatos e) {
                 System.out.println("\n\n ERROR CRITICO DURANTE EL REPORTE:");
                 System.out.println("Mensaje: " + e.getMessage());
                 if (e.esCritico()) {
                     System.out.println("Tipo: ERROR FATAL. La ejecucion del programa se detiene.");
                     opcion = 0; 
                 }
          
            } catch (NumberFormatException e) {
              
                System.out.println("Entrada invalida. Por favor, ingrese un numero");
                opcion = -1;
            }
        }
    }
}