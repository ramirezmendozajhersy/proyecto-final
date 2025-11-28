/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Modelo.DetalleFinanciero;
import Modelo.EntidadesRelacionadas;
import Modelo.InformacionLogistica;
import Modelo.OrdenBase;
import Modelo.ProductoVendido;
import Modelo.RegistroMaestro;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author USER
 */
public class ControladorOrdenes {




    private static final int MAX_GRUPOS = 50; 

   
    public static class ExcepcionProcesamientoDatos extends Exception {
        private final boolean esCritico;
        public ExcepcionProcesamientoDatos(String mensaje, boolean esCritico) {
            super(mensaje); this.esCritico = esCritico;
        }
        public boolean esCritico() { 
            return esCritico; }
    }

    public boolean validarCredenciales(String usuario, String contraseña, String nombreArchivoUsuario) throws ExcepcionProcesamientoDatos {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivoUsuario))) {
            String linea = br.readLine();
            if (linea == null || linea.trim().isEmpty()) {
                throw new ExcepcionProcesamientoDatos("El archivo de usuarios esta vacio o es inaccesible.", true);
            }
            String[] campos = linea.split(",");
            if (campos.length < 2) {
                 throw new ExcepcionProcesamientoDatos("Formato invalido en el archivo de usuarios. Se espera 'usuario,contraseña'.", true);
            }
            String usuarioArchivo = campos[0].trim();
            String contraseñaArchivo = campos[1].trim();
            return usuario.equals(usuarioArchivo) && contraseña.equals(contraseñaArchivo);
        } catch (IOException e) {
            throw new ExcepcionProcesamientoDatos("Error critico de I/O al leer el archivo de usuarios: " + e.getMessage(), true);
        }
    }

    private int contarLineas(String nombreArchivo) throws ExcepcionProcesamientoDatos {
        int conteo = 0;
        try (BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo))) {
            lector.readLine();
            while (lector.readLine() != null) {
                conteo++;
            }
        } catch (IOException e) {
            throw new ExcepcionProcesamientoDatos("Error critico de I/O al contar lineas: El archivo no se encuentra o no se puede leer.", true);
        }
        return conteo;
    }

    private RegistroMaestro[] cargarDatos(String nombreArchivo) throws ExcepcionProcesamientoDatos {
        int numRegistros = contarLineas(nombreArchivo);
        if (numRegistros == 0) {
            throw new ExcepcionProcesamientoDatos("El archivo no contiene registros validos para procesar.", true);
        }
        
        RegistroMaestro[] registros = new RegistroMaestro[numRegistros];
        int indiceRegistro = 0;
        int numeroLinea = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            
            br.readLine(); 
            numeroLinea++; 

            String linea;
            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                String[] campos = linea.split(",");
                
                if (campos.length < 15) {
                    System.err.println("Advertencia de datos en linea " + numeroLinea + ": Faltan campos");
                    continue; 
                }
                
                try {
                    OrdenBase base = new OrdenBase(Integer.parseInt(campos[0].trim()), campos[1].trim(), campos[3].trim());
                    DetalleFinanciero financiero = new DetalleFinanciero(Double.parseDouble(campos[4].trim()), Double.parseDouble(campos[5].trim()), campos[12].trim());
                    ProductoVendido producto = new ProductoVendido(campos[8].trim(), Integer.parseInt(campos[9].trim()), campos[10].trim());
                    InformacionLogistica logistica = new InformacionLogistica(campos[13].trim(), Integer.parseInt(campos[14].trim()), campos[11].trim());
                    EntidadesRelacionadas entidades = new EntidadesRelacionadas(campos[6].trim(), campos[7].trim(), Integer.parseInt(campos[2].trim()));
                    
                    registros[indiceRegistro++] = new RegistroMaestro(base, financiero, producto, logistica, entidades);

                } catch (NumberFormatException e) {
                    throw new ExcepcionProcesamientoDatos("Error critico de formato numerico en la linea " + numeroLinea + " Detalle: " + e.getMessage(), true);
                }
            } 
        } catch (IOException e) {
            throw new ExcepcionProcesamientoDatos("Error critico de I/O al leer el archivo. Detalle: " + e.getMessage(), true);
        }
        
        return registros;
    }
    

    

    public void generarReporteAnual(String nombreArchivo) throws ExcepcionProcesamientoDatos {
        RegistroMaestro[] registros = cargarDatos(nombreArchivo); 
        System.out.println("\n 1. Reporte anual (Monto total de productos comprados)");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-15s | %-15s | %-25s |%n", "ID ORDEN", "IMPORTE ORDEN", "CANTIDAD PROD.", "PRODUCTO");
        System.out.println("-----------------------------------------------------------------------------------");
        
        double totalImporte = 0.0;
        int totalProductos = 0;
        
        for (RegistroMaestro r : registros) {
            double importe = r.getFinanciero().getImporteOrden();
            int cantidad = r.getProducto().getCantidad();
            
            System.out.printf("| %-10d | $%-14.2f | %-15d | %-25s |%n", 
                r.getBase().getIdOrden(), 
                importe, 
                cantidad,
                r.getProducto().getNombreProducto()
            );
            
            totalImporte += importe;
            totalProductos += cantidad;
        }

        System.out.println("-----------------------------------------------------------------------------------");
        System.out.printf("Total de importe: $%.2f%n", totalImporte);
        System.out.printf("Total de productos: %d unidades%n", totalProductos);
    }
    
   
    public void generarReportePorCategoria(String nombreArchivo) throws ExcepcionProcesamientoDatos {
        RegistroMaestro[] registros = cargarDatos(nombreArchivo); 
        System.out.println("\n 2. Reporte total de productos comprados por categoria");

        String[] categoriasUnicas = new String[MAX_GRUPOS];
        int[] productosPorCategoria = new int[MAX_GRUPOS];
        int indiceUnico = 0;
        int totalGlobalProductos = 0;

        for (RegistroMaestro r : registros) {
            String categoria = r.getProducto().getCategoria();
            int cantidad = r.getProducto().getCantidad();
            totalGlobalProductos += cantidad;
            
            boolean encontrado = false;
            for (int i = 0; i < indiceUnico; i++) {
                if (categoriasUnicas[i] != null && categoriasUnicas[i].equals(categoria)) {
                    productosPorCategoria[i] += cantidad;
                    encontrado = true;
                    break;
                }
            }
            
            if (!encontrado && indiceUnico < MAX_GRUPOS) {
                categoriasUnicas[indiceUnico] = categoria;
                productosPorCategoria[indiceUnico] = cantidad;
                indiceUnico++;
            }
        }
        
       
        System.out.println("-------------------------------------------------------------");
       
        System.out.printf("| %-20s | %-15s | %-14s |%n", "CATEGORIA", "TOTAL PRODUCTOS", "PORCENTAJE"); 
        System.out.println("-------------------------------------------------------------");
        
        double sumaPorcentajes = 0.0;
        for (int i = 0; i < indiceUnico; i++) {
            if (categoriasUnicas[i] != null) {
               double porcentaje = (double) productosPorCategoria[i] / totalGlobalProductos * 100;
               sumaPorcentajes += porcentaje;
               
               
               System.out.printf("| %-20s | %-15d | %13.2f%% |%n", categoriasUnicas[i], productosPorCategoria[i], porcentaje);
            }
        }
        System.out.println("-------------------------------------------------------------");
        
        System.out.printf("| %-20s | %-15d | %13.2f%% |%n", "TOTAL", totalGlobalProductos, sumaPorcentajes);
    
        
        System.out.println("-------------------------------------------------------------");
        System.out.printf("Total de productos vendidos: %d unidades%n", totalGlobalProductos);
    }
    
   
    public void generarReportePorMetodoPago(String nombreArchivo) throws ExcepcionProcesamientoDatos {
        RegistroMaestro[] registros = cargarDatos(nombreArchivo);
        System.out.println("\n 3. Reporte monto total por metodo de pago");
        
        String[] metodosUnicos = new String[MAX_GRUPOS];
        double[] montosPorMetodo = new double[MAX_GRUPOS];
        int indiceUnico = 0;
        double totalGlobalImporte = 0.0;

        for (RegistroMaestro r : registros) {
            String metodo = r.getFinanciero().getMetodoPago();
            double importe = r.getFinanciero().getImporteOrden();
            totalGlobalImporte += importe;

            boolean encontrado = false;
            for (int i = 0; i < indiceUnico; i++) {
                if (metodosUnicos[i] != null && metodosUnicos[i].equals(metodo)) {
                    montosPorMetodo[i] += importe;
                    encontrado = true;
                    break;
                }
            }
            
            if (!encontrado && indiceUnico < MAX_GRUPOS) {
                metodosUnicos[indiceUnico] = metodo;
                montosPorMetodo[indiceUnico] = importe;
                indiceUnico++;
            }
        }


        System.out.println("-------------------------------------------------------------");
     
        System.out.printf("| %-20s | %-12s | %-14s |%n", "METODO DE PAGO", "MONTO TOTAL", "PORCENTAJE"); 
        System.out.println("-------------------------------------------------------------");
        
        double sumaPorcentajes = 0.0;
        for (int i = 0; i < indiceUnico; i++) {
            if (metodosUnicos[i] != null) {
               double porcentaje = (montosPorMetodo[i] / totalGlobalImporte) * 100;
               sumaPorcentajes += porcentaje;
               
              
               System.out.printf("| %-20s | $%-11.2f | %13.2f%% |%n", metodosUnicos[i], montosPorMetodo[i], porcentaje);
            }
        }
        System.out.println("-------------------------------------------------------------");
        
        System.out.printf("| %-20s | $%-11.2f | %13.2f%% |%n", "TOTAL", totalGlobalImporte, sumaPorcentajes);
       
        
        System.out.println("-------------------------------------------------------------");
        System.out.printf("Total de importe pagado: $%.2f%n", totalGlobalImporte);
    }
    
 
    public void generarReportePorEstado(String nombreArchivo) throws ExcepcionProcesamientoDatos {
        RegistroMaestro[] registros = cargarDatos(nombreArchivo);
        System.out.println("\n 4. Reporte conteo de ordenes por estado");

        String[] estadosUnicos = new String[MAX_GRUPOS];
        int[] conteoPorEstado = new int[MAX_GRUPOS];
        int indiceUnico = 0;
        int totalGlobalOrdenes = 0;

        for (RegistroMaestro r : registros) {
            String estado = r.getLogistica().getEstadoOrden();
            totalGlobalOrdenes++;

            boolean encontrado = false;
            for (int i = 0; i < indiceUnico; i++) {
                if (estadosUnicos[i] != null && estadosUnicos[i].equals(estado)) {
                    conteoPorEstado[i]++;
                    encontrado = true;
                    break;
                }
            }
            
            if (!encontrado && indiceUnico < MAX_GRUPOS) {
                estadosUnicos[indiceUnico] = estado;
                conteoPorEstado[indiceUnico] = 1;
                indiceUnico++;
            }
        }

      
        System.out.println("-------------------------------------------------------------");
       
        System.out.printf("| %-20s | %-12s | %-14s |%n", "ESTADO DE ORDEN", "CONTEO", "PORCENTAJE"); 
        System.out.println("-------------------------------------------------------------");
        
        double sumaPorcentajes = 0.0;
        for (int i = 0; i < indiceUnico; i++) {
             if (estadosUnicos[i] != null) {
                double porcentaje = (double) conteoPorEstado[i] / totalGlobalOrdenes * 100;
                sumaPorcentajes += porcentaje;
                
            
                System.out.printf("| %-20s | %-12d | %13.2f%% |%n", estadosUnicos[i], conteoPorEstado[i], porcentaje);
             }
        }
        System.out.println("-------------------------------------------------------------");
      
        System.out.printf("| %-20s | %-12d | %13.2f%% |%n", "TOTAL", totalGlobalOrdenes, sumaPorcentajes);
       
        System.out.println("-------------------------------------------------------------");
        System.out.printf("Total de ordenes procesadas: %d%n", totalGlobalOrdenes);
    }

   
    public void generarReportePorCiudad(String nombreArchivo) throws ExcepcionProcesamientoDatos {
        RegistroMaestro[] registros = cargarDatos(nombreArchivo);
        System.out.println("\n 5. Reporte Monto Total por Ciudad de Entrega");
        
        String[] ciudadesUnicas = new String[MAX_GRUPOS];
        double[] montosPorCiudad = new double[MAX_GRUPOS];
        int indiceUnico = 0;
        double totalGlobalImporte = 0.0;

        for (RegistroMaestro r : registros) {
            String ciudad = r.getLogistica().getCiudadEntrega();
            double importe = r.getFinanciero().getImporteOrden();
            totalGlobalImporte += importe;

            boolean encontrado = false;
            for (int i = 0; i < indiceUnico; i++) {
                if (ciudadesUnicas[i] != null && ciudadesUnicas[i].equals(ciudad)) {
                    montosPorCiudad[i] += importe;
                    encontrado = true;
                    break;
                }
            }
            
            if (!encontrado && indiceUnico < MAX_GRUPOS) {
                ciudadesUnicas[indiceUnico] = ciudad;
                montosPorCiudad[indiceUnico] = importe;
                indiceUnico++;
            }
        }

       
        System.out.println("-------------------------------------------------------------");
       
        System.out.printf("| %-20s | %-12s | %-14s |%n", "CIUDAD DE ENTREGA", "MONTO TOTAL", "PORCENTAJE"); 
        System.out.println("-------------------------------------------------------------");
        
        double sumaPorcentajes = 0.0;
        for (int i = 0; i < indiceUnico; i++) {
             if (ciudadesUnicas[i] != null) {
                double porcentaje = (montosPorCiudad[i] / totalGlobalImporte) * 100;
                sumaPorcentajes += porcentaje;
                
                
                System.out.printf("| %-20s | $%-11.2f | %13.2f%% |%n", ciudadesUnicas[i], montosPorCiudad[i], porcentaje);
             }
        }
        System.out.println("-------------------------------------------------------------");
      
        System.out.printf("| %-20s | $%-11.2f | %13.2f%% |%n", "TOTAL", totalGlobalImporte, sumaPorcentajes);
        
        
        System.out.println("-------------------------------------------------------------");
        System.out.printf("Total global de importe a entregar: $%.2f%n", totalGlobalImporte);
    }

    
    public void generarReportePorProveedor(String nombreArchivo) throws ExcepcionProcesamientoDatos {
        RegistroMaestro[] registros = cargarDatos(nombreArchivo);
        System.out.println("\n 6. Reporte conteo de ordenes por proveedor");

        String[] proveedoresUnicos = new String[MAX_GRUPOS];
        int[] conteoPorProveedor = new int[MAX_GRUPOS];
        int indiceUnico = 0;
        int totalGlobalOrdenes = 0;

        for (RegistroMaestro r : registros) {
            String proveedor = r.getEntidades().getNombreProveedor();
            totalGlobalOrdenes++;

            boolean encontrado = false;
            for (int i = 0; i < indiceUnico; i++) {
                if (proveedoresUnicos[i] != null && proveedoresUnicos[i].equals(proveedor)) {
                    conteoPorProveedor[i]++;
                    encontrado = true;
                    break;
                }
            }
            
            if (!encontrado && indiceUnico < MAX_GRUPOS) {
                proveedoresUnicos[indiceUnico] = proveedor;
                conteoPorProveedor[indiceUnico] = 1;
                indiceUnico++;
            }
        }

       
        System.out.println("-------------------------------------------------------------");
        
        System.out.printf("| %-20s | %-12s | %-14s |%n", "PROVEEDOR", "CONTEO", "PORCENTAJE"); 
        System.out.println("-------------------------------------------------------------");
        
        double sumaPorcentajes = 0.0;
        for (int i = 0; i < indiceUnico; i++) {
             if (proveedoresUnicos[i] != null) {
                double porcentaje = (double) conteoPorProveedor[i] / totalGlobalOrdenes * 100;
                sumaPorcentajes += porcentaje;
                
                
                System.out.printf("| %-20s | %-12d | %13.2f%% |%n", proveedoresUnicos[i], conteoPorProveedor[i], porcentaje);
             }
        }
        System.out.println("-------------------------------------------------------------");
       
        System.out.printf("| %-20s | %-12d | %13.2f%% |%n", "TOTAL", totalGlobalOrdenes, sumaPorcentajes);
     
        
        System.out.println("-------------------------------------------------------------");
        System.out.printf("Total global de ordenes con proveedor registrado: %d%n", totalGlobalOrdenes);
    }

  
    public void generarReportePorAñoFiscal(String nombreArchivoDatos, int añoFiltro) throws ExcepcionProcesamientoDatos {
        RegistroMaestro[] registros = cargarDatos(nombreArchivoDatos);
        System.out.println("\n 7. Reporte detallado de ordenes para el año fiscal: " + añoFiltro);

        double totalMontoAño = 0.0;
        int totalProductosAño = 0;
        
       
        int[] idsOrdenesUnicas = new int[registros.length];
        int indiceOrdenUnica = 0; 
        


        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-12s | %-10s | %-30s | %-10s | %-12s | %-20s |%n", 
            "AÑO FISCAL", "ID ORDEN", "PRODUCTO", "CANTIDAD", "IMPORTE ORDEN", "PROVEEDOR");
        System.out.println("------------------------------------------------------------------------------------------------------------------");


        for (RegistroMaestro r : registros) {
            
            int añoFiscal = r.getEntidades().getAñoFiscal(); 
            
           
            if (añoFiscal == añoFiltro) {
               
                int idOrden = r.getBase().getIdOrden();
                String nombreProducto = r.getProducto().getNombreProducto();
                int cantidad = r.getProducto().getCantidad();
                double importeOrden = r.getFinanciero().getImporteOrden();
                String nombreProveedor = r.getEntidades().getNombreProveedor();

               
                String importeFormateado = String.format("S/%,.2f", importeOrden);
                
               
                System.out.printf("| %-12d | %-10d | %-30s | %-10d | %12s | %-20s |%n", 
                    añoFiscal, idOrden, nombreProducto, cantidad, importeFormateado, nombreProveedor);
                
               
                totalMontoAño += importeOrden;
                totalProductosAño += cantidad;
                
               
                boolean idYaContado = false;
                for (int i = 0; i < indiceOrdenUnica; i++) {
                    if (idsOrdenesUnicas[i] == idOrden) {
                        idYaContado = true;
                        break;
                    }
                }

                if (!idYaContado) {
                    idsOrdenesUnicas[indiceOrdenUnica] = idOrden;
                    indiceOrdenUnica++;
                }
                
            }
        }
        
     
        System.out.println("------------------------------------------------------------------------------------------------------------------");

       
        if (indiceOrdenUnica > 0) {
            System.out.printf("Total de ordenes encontradas: %d%n", indiceOrdenUnica);
            System.out.printf("Total de productos vendidos: %d unidades%n", totalProductosAño);
            System.out.printf("Monto total acumulado para %d: S/%,.2f%n", añoFiltro, totalMontoAño);
        } else {
            System.out.printf("No se encontraron ordenes para el año fiscal: %d%n", añoFiltro);
        }
    }
}
       