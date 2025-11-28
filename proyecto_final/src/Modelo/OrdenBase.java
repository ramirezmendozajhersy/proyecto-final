/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author USER
 */
public class OrdenBase {
        private int idOrden;
    private String numeroOrden;
    private String fechaOrden; 
    public OrdenBase(int idOrden, String numeroOrden, String fechaOrden) {
        
        this.idOrden = idOrden; 
        this.numeroOrden = numeroOrden; 
        this.fechaOrden = fechaOrden;
    }
    public int getIdOrden() 
    { 
        return idOrden; 
    }
    public String getNumeroOrden() 
    { 
        return numeroOrden; 
    }
    public String getFechaOrden() { 
        return fechaOrden; 
    }
}
