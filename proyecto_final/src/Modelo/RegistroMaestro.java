/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author USER
 */
public class RegistroMaestro {
    
    private OrdenBase base; 
    private DetalleFinanciero financiero;
    private ProductoVendido producto;
    private InformacionLogistica logistica;
    private EntidadesRelacionadas entidades;
    
    
    public RegistroMaestro(OrdenBase base, DetalleFinanciero financiero, ProductoVendido producto, 
            InformacionLogistica logistica, EntidadesRelacionadas entidades) {
        
        this.base = base; 
        this.financiero = financiero; 
        this.producto = producto; 
        this.logistica = logistica; 
        this.entidades = entidades;
        
    }
    
    public OrdenBase getBase() { 
        return base;
    }
    
    public DetalleFinanciero getFinanciero() { 
        return financiero; 
    }
    public ProductoVendido getProducto() { 
        return producto; 
    }
    public EntidadesRelacionadas getEntidades() { 
        return entidades; 
    }
    public InformacionLogistica getLogistica() { 
        return logistica; 
    }
}
