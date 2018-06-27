/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dominio;

import Control.Conexion;
import Modelo.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author marco
 */
//Realiza la validacion del usuario
public class Login 
{
    Conexion con = new Conexion();
    
    public Usuario loginUsuario(String user, String passwd) throws SQLException
    {
        int idUsuario=0;
        String nombreUsuario="";
        String clave="";
        int tipo = 0;  //si el tipo es 1 es un usuario normal
        
        //Leemos los datos del usuario (si existe)
        //Atencion con dejar un espacio despues de abrir las comillas
        //eso impide que la las instrucciones de la query queden pegadas
        
        String consulta = "select us.idusuario" +
                        "	, us.usuario" +
                        "    , da.rut" +
                        "    , concat(da.nombre1, ' ', da.apellido1, ' ', da.apellido2) nombre" +
                        "    , pe.idperfil" +
                        "    , pe.perfil" +
                        "    , us.password" +
                        " from usuario us" +
                        "	, usuario_has_perfil has" +
                        "    , usuario_has_datos_usu dus" +
                        "    , datos_usu da" +
                        "    , perfil pe" +
                        " where upper(trim(us.usuario)) = upper(trim('" + user + "'))" +
                        " and us.password =  sha1('" + passwd + "')" +
                        " and ifnull(us.vigente, 'S') = 'S'" +
                        " and has.usuario_idusuario = us.idusuario" +
                        " and dus.usuario_idusuario = us.idusuario" +
                        " and da.rut = dus.datos_usu_rut" +
                        " and ifnull(da.vigente, 'S') = 'S'" +
                        " and pe.idperfil = has.perfil_idperfil" +
                        " and ifnull(pe.vigente, 'S') = 'S'";

        ResultSet resultado = con.ejecutarSQLSelect(consulta);
        
        if (resultado != null)
        {
            while(resultado.next()) 
            {
                // Obtenemos los datos
                idUsuario = resultado.getInt("idusuario");
                nombreUsuario = resultado.getString("usuario");
                clave = resultado.getString("password");
                tipo = tipo + Integer.parseInt(resultado.getString("idperfil"));
                
            }
        }
        
        Usuario us = new Usuario();
        us.setIdUsuario(idUsuario);
        us.setNombre(nombreUsuario);
        us.setClave(clave);
        us.setTipo(tipo);
        
        return us;
    }
}
