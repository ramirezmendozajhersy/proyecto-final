/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author USER
 */
public class DetalleFinanciero {
    
    private double importeOrden;
    private double costoEnvio;
    private String metodoPago;
    public DetalleFinanciero(double importeOrden, double costoEnvio, String metodoPago) {
        
        this.importeOrden = importeOrden; 
        this.costoEnvio = costoEnvio; 
        this.metodoPago = metodoPago;
    }
    public double getImporteOrden() { 
        return importeOrden; 
    }
    public double getCostoEnvio() { 
        return costoEnvio; 
    }
    public String getMetodoPago() { 
        return metodoPago; 
    }
}  

