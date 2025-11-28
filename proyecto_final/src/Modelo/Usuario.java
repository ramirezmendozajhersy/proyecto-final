/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author USER
 */
public class Usuario {
    private String nombreUsuario;
    private String contraseña;
  

    public Usuario(String nombreUsuario, String contraseña, int nivelAcceso) {
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
     
    }
    public String getNombreUsuario() { 
        return nombreUsuario; 
    }
    public String getContraseña() { 
        return contraseña; 
    }
    
       
    
}
