package controlador;

import java.sql.SQLException;

import modelo.Usuario;
import vista.Vista;

public class Main {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Usuario user=new Usuario();
		user.consultarUsuarios();
		new Controlador(new Vista()).iniciar();
		 
	}

}
