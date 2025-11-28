/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author USER
 */
public class ProductoVendido {
    
    private String nombreProducto;
    private int cantidad;
    private String categoria;
    public ProductoVendido(String nombreProducto, int cantidad, String categoria) {
        
        this.nombreProducto = nombreProducto; 
        this.cantidad = cantidad; 
        this.categoria = categoria;
    }
    public int getCantidad() { return cantidad; }
    public String getCategoria() { return categoria; }
    public String getNombreProducto() { return nombreProducto; }
}
