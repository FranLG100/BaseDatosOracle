package modelo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

import datos.Conexion;
import oracle.jdbc.OracleTypes;

public class Audiolibro {
	Connection con;
	CallableStatement cstmt = null;
	int tamanho=0;
	
	
	public void insertarAudiolibro(String titulo, String nombre, String apellido, String sello, String nlector,String alector) throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call INSERTAR_AUDIOLIBRO(?,?,?,?,?,?)}");
		 cstmt.setString(1, titulo);
		 cstmt.setString(2, nombre);
		 cstmt.setString(3, apellido);
		 cstmt.setString(4, sello);
		 cstmt.setString(5, nlector);
		 cstmt.setString(6, alector);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public void modificarAudiolibro(String titulo, String nombre, String apellido, String sello, String nlector,String alector,int id) throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call MODIFICAR_AUDIOLIBRO(?,?,?,?,?,?,?)}");
		cstmt.setString(1, titulo);
		 cstmt.setString(2, nombre);
		 cstmt.setString(3, apellido);
		 cstmt.setString(4, sello);
		 cstmt.setString(5, nlector);
		 cstmt.setString(6, alector);
		 cstmt.setInt(7, id);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public void borrarAudiolibro(int id) throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call BORRAR_AUDIOLIBRO(?)}");
		 cstmt.setInt(1, id);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public void devolverAudiolibro(int id) throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call DEVOLVER_AUDIOLIBRO(?)}");
		 cstmt.setInt(1, id);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public void prestarAudiolibro(int id) throws SQLException {
		//EJEMPLO PROCEDIMIENTO
		con=Conexion.getConnection();
		cstmt = con.prepareCall("{call PRESTAR_AUDIOLIBRO(?)}");
		 cstmt.setInt(1, id);
         cstmt.execute();
         cstmt.close();
		con.close();
	}
	
	public DefaultTableModel listarAudiolibros() {

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
			cstmt=con.prepareCall("{call CONSULTA_AUDIOLIBROS.obtener_audiolibros(?)}");
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
		    }
		    cursor.close();
		    cstmt.close();
		    con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[][] filas = new String[tamanho][columnas];

		try {
			int id,codigo,disponibilidad;
			String titulo,nombre,apellido,editorial;
			con=Conexion.getConnection();
			cstmt=con.prepareCall("{call CONSULTA_AUDIOLIBROS.obtener_audiolibros(?)}");
		    cstmt.registerOutParameter(1, OracleTypes.CURSOR);
		    cstmt.executeQuery();
		    ResultSet cursor = (ResultSet)cstmt.getObject(1);
			int i = 0;
			while (cursor.next()) {
				id = cursor.getInt(1);
				titulo=cursor.getString(2);
				nombre=cursor.getString(3);
				apellido=cursor.getString(4);
				String sello = cursor.getString(5);
				String lector = cursor.getString(6);
				disponibilidad=cursor.getInt(7);
				filas[i][0] = Integer.toString(id);
				filas[i][1] = titulo;
				filas[i][2] = nombre;
				filas[i][3] = apellido;
				filas[i][4] = sello;
				filas[i][5] = lector;
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
	
	public String[] listarAudiolibro(int n) {
		String titulo,nombre,apellido,sello, lnombre,lapellido;
		String[] datos=new String[6];
		try {
			con=Conexion.getConnection();
			cstmt=con.prepareCall("{call CONSULTA_AUDIOLIBRO.obtener_audiolibro(?,?)}");
		    cstmt.registerOutParameter(1, OracleTypes.CURSOR);
		    cstmt.setInt(2, n);
		    cstmt.executeQuery();
		    ResultSet cursor = (ResultSet)cstmt.getObject(1);

			while (cursor.next()) {
				titulo=cursor.getString(1);
				nombre=cursor.getString(2);
				apellido=cursor.getString(3);
				sello=cursor.getString(4);
				lnombre=cursor.getString(5);
				lapellido=cursor.getString(6);
				datos[0] = titulo;
				datos[1] = nombre;
				datos[2] = apellido;
				datos[3] = sello;
				datos[4] = lnombre;
				datos[5] = lapellido;
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datos;
	}
	
	
	public DefaultTableModel listarAudiolibrosPrestamo() {

		tamanho=0;
		String[] headers = { "ID","TITULO","AUTOR","DISPONIBLE"};
		DefaultTableModel plantilla = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		try {
			con=Conexion.getConnection();
			cstmt=con.prepareCall("{call CONSULTA_AUDIOLIBROS.obtener_audiolibros(?)}");
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

		String[][] filas = new String[tamanho][4];

		try {
			int id,disponibilidad;
			String titulo,nombre;
			con=Conexion.getConnection();
			cstmt=con.prepareCall("{call CONSULTA_AUDIOLIBROS.obtener_audiolibros(?)}");
		    cstmt.registerOutParameter(1, OracleTypes.CURSOR);
		    cstmt.executeQuery();
		    ResultSet cursor = (ResultSet)cstmt.getObject(1);
			int i = 0;
			//
			while (cursor.next()) {
				id = cursor.getInt(1);
				titulo=cursor.getString(2);
				nombre=cursor.getString(3)+" "+cursor.getString(4);
				disponibilidad=cursor.getInt(7);
				filas[i][0] = Integer.toString(id);
				filas[i][1] = titulo;
				filas[i][2] = nombre;
				filas[i][3] = Integer.toString(disponibilidad);
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
