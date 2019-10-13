package modelo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

import datos.Conexion;
import oracle.jdbc.OracleTypes;

public class Libro {
	Connection con;
	CallableStatement cstmt = null;
	int tamanho=0;
	
	
	public void insertarLibro(String titulo, String nombre, String apellido, String editor, int clase) throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call INSERTAR_LIBRO(?,?,?,?,?)}");
		 cstmt.setString(1, titulo);
		 cstmt.setString(2, nombre);
		 cstmt.setString(3, apellido);
		 cstmt.setString(4, editor);
		 cstmt.setInt(5, clase);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public void modificarLibro(String titulo, String nombre, String apellido, String editor, int clase,int id) throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call MODIFICAR_LIBRO(?,?,?,?,?,?)}");
		cstmt.setString(1, titulo);
		 cstmt.setString(2, nombre);
		 cstmt.setString(3, apellido);
		 cstmt.setString(4, editor);
		 cstmt.setInt(5, clase);
		 cstmt.setInt(6, id);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public void borrarLibro(int id) throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call BORRAR_LIBRO(?)}");
		 cstmt.setInt(1, id);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public void devolverLibro(int id) throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call DEVOLVER_LIBRO(?)}");
		 cstmt.setInt(1, id);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public void prestarLibro(int id) throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call PRESTAR_LIBRO(?)}");
		 cstmt.setInt(1, id);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public DefaultTableModel listarLibros() {

		tamanho=0;
		String[] headers = { "ID","Titulo","Nombre Autor","Apellido","Editorial","Codigo","Disponible"};
		DefaultTableModel plantilla = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		try {
			con=Conexion.getConnection();
			cstmt=con.prepareCall("{call CONSULTA_LIBROS.obtener_libros(?)}");
		    cstmt.registerOutParameter(1, OracleTypes.CURSOR);
		    cstmt.executeQuery();
		    ResultSet cursor = (ResultSet)cstmt.getObject(1);
		    
		    while(cursor.next()) {
		    	tamanho++;
		    }
		    cursor.close();
		    cstmt.close();
		    con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[][] filas = new String[tamanho][7];

		try {
			int id,codigo,disponibilidad;
			String titulo,nombre,apellido,editorial;
			con=Conexion.getConnection();
			cstmt=con.prepareCall("{call CONSULTA_LIBROS.obtener_libros(?)}");
		    cstmt.registerOutParameter(1, OracleTypes.CURSOR);
		    cstmt.executeQuery();
		    ResultSet cursor = (ResultSet)cstmt.getObject(1);
			int i = 0;
			while (cursor.next()) {
				id = cursor.getInt(1);
				titulo=cursor.getString(2);
				nombre=cursor.getString(3);
				apellido=cursor.getString(4);
				editorial=cursor.getString(5);
				codigo=cursor.getInt(6);
				disponibilidad=cursor.getInt(7);
				filas[i][0] = Integer.toString(id);
				filas[i][1] = titulo;
				filas[i][2] = nombre;
				filas[i][3] = apellido;
				filas[i][4] = editorial;
				filas[i][5] = Integer.toString(codigo);
				filas[i][6] = Integer.toString(disponibilidad);
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
	
}
