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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author USER
 */
public class ControladorReportePorCategoria {

    
    public ControladorReportePorCategoria(ArrayList<RegistroMaestro> registros) {
    }

    public void generarReportePorCategoria(String rutaCSV, String rutaSalidaTXT) {
        Map<String, Integer> unidadesPorCategoria = new TreeMap<>();
        List<String> errores = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaCSV))) {
            String linea = br.readLine(); // cabecera
            int ln = 1;
            if (linea == null) errores.add("Archivo vacio");
            else {
                while ((linea = br.readLine()) != null) {
                    ln++;
                    try {
                        String[] c = linea.split(",", -1);
                        if (c.length < 15) { 
                            errores.add("Linea " + ln + ": columnas insuficientes. Se requieren 15 y solo se encontraron " + c.length + "."); 
                            continue; 
                        }

                        OrdenBase orden = new OrdenBase(Integer.parseInt(c[0].trim()), c[1].trim(), c[2].trim());
                        DetalleFinanciero det = new DetalleFinanciero(Double.parseDouble(c[6].trim()),
                                                                      Double.parseDouble(c[7].trim()),
                                                                      c[8].trim());
                        ProductoVendido prod = new ProductoVendido(c[3].trim(), Integer.parseInt(c[4].trim()), c[5].trim());

                    
                        InformacionLogistica log = new InformacionLogistica(
                            c[9].trim(), 
                            Integer.parseInt(c[10].trim()), 
                            c[11].trim()
                        );
                        
                        
                        EntidadesRelacionadas ent = new EntidadesRelacionadas(
                            c[12].trim(), 
                            c[13].trim(),
                            Integer.parseInt(c[14].trim()) 
                        );

                        
                        RegistroMaestro rm = new RegistroMaestro(orden, det, prod, log, ent); 

                        // Lógica del reporte
                        String categoria = rm.getProducto().getCategoria();
                        int cantidad = rm.getProducto().getCantidad();
                        
                        unidadesPorCategoria.put(categoria, unidadesPorCategoria.getOrDefault(categoria, 0) + cantidad);

                    } catch (NumberFormatException nfe) {
                        errores.add("Linea " + ln + ": numero invalido -> " + nfe.getMessage());
                    } catch (Exception ex) {
                        errores.add("Linea " + ln + ": error -> " + ex.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            errores.add("Error I/O: " + e.getMessage());
        }

        // escribir reporte
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaSalidaTXT))) {
            pw.println("REPORTE POR CATEGORÍA");
            pw.println("=====================");
            for (Map.Entry<String, Integer> e : unidadesPorCategoria.entrySet()) {
                pw.printf("%s : %d unidades%n", e.getKey(), e.getValue());
            }
        } catch (IOException e) {
            errores.add("Error escribiendo reporte: " + e.getMessage());
        }

        if (!errores.isEmpty()) {
            try (PrintWriter pe = new PrintWriter(new FileWriter(rutaSalidaTXT + ".errores.txt"))) {
                pe.println("ERRORES - Reporte Por Categoría");
                for (String err : errores) pe.println(err);
            } catch (IOException e) {
                System.out.println("No se pudo escribir archivo de errores: " + e.getMessage());
            }
        }
    }

    public void mostrarMenuReportes() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}