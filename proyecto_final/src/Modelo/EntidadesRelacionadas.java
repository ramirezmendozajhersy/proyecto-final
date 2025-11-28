/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author USER
 */
public class EntidadesRelacionadas {
      
    private String nombreProveedor;
    private String nombreRepartidor;
    private int añoFiscal;
    public EntidadesRelacionadas(String nombreProveedor, String nombreRepartidor, int añoFiscal) {
        
        this.nombreProveedor = nombreProveedor; 
        this.nombreRepartidor = nombreRepartidor; 
        this.añoFiscal = añoFiscal;
    }
    public int getAñoFiscal() { 
        return añoFiscal; 
    }
    public String getNombreProveedor() { 
        return nombreProveedor; 
    }
    public String getNombreRepartidor() { 
        return nombreRepartidor; 
    }
}
