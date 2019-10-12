
package callablestatementfunciones;

import datos.Conexion;
import modelo.Usuario;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import java.sql.*;

public class CallableStatementFunciones {

    public static void main(String[] args) {
        int empleadoId = 60; // indentificadora recuperar salario
        Usuario prueba=new Usuario();
        try {
            Connection con = Conexion.getConnection();
            CallableStatement cstmt = null;
            double salarioMensual;
            
            //prueba.insertarUsuario("28823344C", "Francisco", "Lorente", "Salud de los Enfermos", "Sevilla", "41013", "fralg100@gmail.com");
            //prueba.modificarUsuario("28823344C", "Adrian", "Lorente", "Salud de los Enfermos", "Sevilla", 41013, "fralorgir@gmail.com",3);
            prueba.borrarUsuario(3);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
