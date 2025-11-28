/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author USER
 */
public class InformacionLogistica {
     private String ciudadEntrega;
    private int tiempoEntrega;
    private String estadoOrden;
    public InformacionLogistica(String ciudadEntrega, int tiempoEntrega, String estadoOrden) {
        
        this.ciudadEntrega = ciudadEntrega; 
        this.tiempoEntrega = tiempoEntrega; 
        this.estadoOrden = estadoOrden;
    }
    public String getCiudadEntrega() { 
        return ciudadEntrega; 
    }
    public String getEstadoOrden() { 
        return estadoOrden; 
    }
    public int getTiempoEntrega() { 
        return tiempoEntrega; 
    }
}
