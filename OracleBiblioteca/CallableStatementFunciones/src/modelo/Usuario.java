package modelo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

import datos.Conexion;
import oracle.jdbc.OracleTypes;

public class Usuario {

	Connection con;
	CallableStatement cstmt = null;
	double salarioMensual;
	int empleadoId = 101;
	int tamanho=0;
	
	public void consultarUsuarios() throws SQLException {
		//EJEMPLO CURSOR
		con=Conexion.getConnection();
		cstmt=con.prepareCall("{call CONSULTA_USUARIOS.obtener_usuarios(?)}");
	    cstmt.registerOutParameter(1, OracleTypes.CURSOR);
	    cstmt.executeQuery();
	    ResultSet cursor = (ResultSet)cstmt.getObject(1);
	    int tamanho=0;
	    while(cursor.next()) {
	    	tamanho++;
	        System.out.println("TAMANHO:"+tamanho+" ID = " + cursor.getInt(1)+" DNI:"+cursor.getString(2)+" Nombre:"+cursor.getString(3)+" Apellido:"+cursor.getString(4)+" Calle:"+cursor.getString(5)+" Ciudad:"+cursor.getString(6)+" Zip:"+cursor.getString(7)+" Email:"+cursor.getString(8));
	    
	    }
	    cursor.close();
	    cstmt.close();
	    con.close();
	}
	
	public void multiplicarSalario() throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call set_employee_salary(?,?)}");
		 cstmt.setInt(1, 101);
         cstmt.setInt(2, 2);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public void consultarSalario() throws SQLException {
		//EJEMPLO FUNCION
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{ ? = callget_employee_salary(?) }");
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setInt(2, empleadoId);
        cstmt.execute();
        salarioMensual = cstmt.getDouble(1);
        cstmt.close();
        System.out.println("Empleado con id:" + empleadoId);
        System.out.println("Salario $" + salarioMensual);
		con.close();
	}
	
	public void insertarUsuario(String dni, String nombre, String apellido, String calle, String ciudad,String zip, String email) throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call INSERTAR_USUARIO(?,?,?,?,?,?,?)}");
		 cstmt.setString(1, dni);
		 cstmt.setString(2, nombre);
		 cstmt.setString(3, apellido);
		 cstmt.setString(4, calle);
		 cstmt.setString(5, ciudad);
		 cstmt.setString(6, zip);
		 cstmt.setString(7, email);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public void modificarUsuario(String dni, String nombre, String apellido, String calle, String ciudad,String zip, String email,int id) throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call MODIFICAR_USUARIO(?,?,?,?,?,?,?,?)}");
		 cstmt.setString(1, dni);
		 cstmt.setString(2, nombre);
		 cstmt.setString(3, apellido);
		 cstmt.setString(4, calle);
		 cstmt.setString(5, ciudad);
		 cstmt.setString(6, zip);
		 cstmt.setString(7, email);
		 cstmt.setInt(8, id);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public void borrarUsuario(int id) throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call BORRAR_USUARIO(?)}");
		 cstmt.setInt(1, id);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public void castigarUsuario(int id) throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call CASTIGAR_USUARIO(?)}");
		 cstmt.setInt(1, id);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public void perdonarUsuario(int id) throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call PERDONAR_USUARIO(?)}");
		 cstmt.setInt(1, id);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public DefaultTableModel listarUsuarios() {

		ResultSetMetaData rsmetadatos;
		int columnas=0;
		tamanho=0;
		String[] headers = null;
		
		DefaultTableModel plantilla = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		try {
			con=Conexion.getConnection();
			cstmt=con.prepareCall("{call CONSULTA_USUARIOS.obtener_usuarios(?)}");
		    cstmt.registerOutParameter(1, OracleTypes.CURSOR);
		    cstmt.executeQuery();
		    ResultSet cursor = (ResultSet)cstmt.getObject(1);
		    rsmetadatos=cursor.getMetaData();
		    columnas=rsmetadatos.getColumnCount();
		    headers=new String[columnas];
		    for (int i = 0; i < headers.length; i++) {
				headers[i]=rsmetadatos.getColumnName(i+1);
			}
		    while(cursor.next()) {
		    	tamanho++;
		        System.out.println("ID = " + cursor.getInt(1)+" DNI:"+cursor.getString(2)+" Nombre:"+cursor.getString(3)+" Apellido:"+cursor.getString(4)+" Calle:"+cursor.getString(5)+" Ciudad:"+cursor.getString(6)+" Zip:"+cursor.getInt(7)+"  Email:"+cursor.getString(8));
		    }
		    cursor.close();
		    cstmt.close();
		    con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[][] filas = new String[tamanho][9];

		try {
			int id,zip,penalizado;
			String dni,nombre,apellido,calle,ciudad,email;
			con=Conexion.getConnection();
			cstmt=con.prepareCall("{call CONSULTA_USUARIOS.obtener_usuarios(?)}");
		    cstmt.registerOutParameter(1, OracleTypes.CURSOR);
		    cstmt.executeQuery();
		    ResultSet cursor = (ResultSet)cstmt.getObject(1);
			int i = 0;
			while (cursor.next()) {
				id = cursor.getInt(1);
				dni=cursor.getString(2);
				nombre=cursor.getString(3);
				apellido=cursor.getString(4);
				calle=cursor.getString(5);
				ciudad=cursor.getString(6);
				zip=cursor.getInt(7);
				email=cursor.getString(8);
				penalizado=cursor.getInt(9);
				filas[i][0] = Integer.toString(id);
				filas[i][1] = dni;
				filas[i][2] = nombre;
				filas[i][3] = apellido;
				filas[i][4] = calle;
				filas[i][5] = ciudad;
				filas[i][6] = Integer.toString(zip);
				filas[i][7] = email;
				filas[i][8] = Integer.toString(penalizado);
				
				i++;
				System.out.println(filas.toString());
			}
			con.close();
			plantilla.setDataVector(filas, headers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plantilla;
	}
	
	public DefaultTableModel listarUsuariosPrestamo() {

		tamanho=0;
		String[] headers = { "ID","DNI","NOMBRE","PENALIZADO" };
		DefaultTableModel plantilla = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		try {
			con=Conexion.getConnection();
			cstmt=con.prepareCall("{call CONSULTA_USUARIOS.obtener_usuarios(?)}");
		    cstmt.registerOutParameter(1, OracleTypes.CURSOR);
		    cstmt.executeQuery();
		    ResultSet cursor = (ResultSet)cstmt.getObject(1);
		    
		    while(cursor.next()) {
		    	tamanho++;
		        System.out.println("ID = " + cursor.getInt(1)+" DNI:"+cursor.getString(2)+" Nombre:"+cursor.getString(3)+" Apellido:"+cursor.getString(4)+" Calle:"+cursor.getString(5)+" Ciudad:"+cursor.getString(6)+" Zip:"+cursor.getInt(7)+"  Email:"+cursor.getString(8));
		    }
		    cursor.close();
		    cstmt.close();
		    con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[][] filas = new String[tamanho][4];

		try {
			int id,penalizado;
			String dni,nombre;
			con=Conexion.getConnection();
			cstmt=con.prepareCall("{call CONSULTA_USUARIOS.obtener_usuarios(?)}");
		    cstmt.registerOutParameter(1, OracleTypes.CURSOR);
		    cstmt.executeQuery();
		    ResultSet cursor = (ResultSet)cstmt.getObject(1);
			int i = 0;
			while (cursor.next()) {
				id = cursor.getInt(1);
				dni=cursor.getString(2);
				nombre=cursor.getString(3)+" "+cursor.getString(4);
				penalizado=cursor.getInt(9);
				filas[i][0] = Integer.toString(id);
				filas[i][1] = dni;
				filas[i][2] = nombre;
				filas[i][3] = Integer.toString(penalizado);
				i++;
				System.out.println(filas.toString());
			}
			con.close();
			plantilla.setDataVector(filas, headers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plantilla;
	}
	
	public void rebajarCastigos() throws SQLException {
			con=Conexion.getConnection();
			cstmt = con.prepareCall("{call REBAJAR_CASTIGO()}");
	         cstmt.execute();
	         cstmt.close();
			con.close();
	}
	
	
	public String[] listarUsuario(int n) {
		String dni,nombre,apellido,calle,ciudad,zip,email;
		String[] datos=new String[7];
		try {
			con=Conexion.getConnection();
			cstmt=con.prepareCall("{call CONSULTA_USUARIO.obtener_usuario(?,?)}");
		    cstmt.registerOutParameter(1, OracleTypes.CURSOR);
		    cstmt.setInt(2, n);
		    cstmt.executeQuery();
		    ResultSet cursor = (ResultSet)cstmt.getObject(1);

			while (cursor.next()) {
				dni=cursor.getString(1);
				nombre=cursor.getString(2);
				apellido=cursor.getString(3);
				calle=cursor.getString(4);
				ciudad=cursor.getString(5);
				zip=cursor.getString(6);
				email=cursor.getString(7);
				datos[0] = dni;
				datos[1] = nombre;
				datos[2] = apellido;
				datos[3] = calle;
				datos[4] = ciudad;
				datos[5] = zip;
				datos[6] = email;
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datos;
	}
	
	
}
