package modelo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.swing.table.DefaultTableModel;

import datos.Conexion;
import oracle.jdbc.OracleTypes;

public class HistoricosAudiolibros {
	
	Connection con;
	CallableStatement cstmt = null;
	String nombre,dni,apellido,titulo,autor,aautor,finicio,ffin,finplazo;
	int tamanho, id;
	
	
	public DefaultTableModel listarHistoricos() {

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
			cstmt=con.prepareCall("{call CONSULTA_HISTORICOS_A.obtener_historicosa(?)}");
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
			con=Conexion.getConnection();
			cstmt=con.prepareCall("{call CONSULTA_HISTORICOS_A.obtener_historicosa(?)}");
		    cstmt.registerOutParameter(1, OracleTypes.CURSOR);
		    cstmt.executeQuery();
		    ResultSet cursor = (ResultSet)cstmt.getObject(1);
			int i = 0;
			while (cursor.next()) {
				id = cursor.getInt(1);
				dni=cursor.getString(2);
				nombre=cursor.getString(3);
				apellido=cursor.getString(4);
				titulo=cursor.getString(5);
				autor=cursor.getString(6);
				aautor=cursor.getString(7);
				finicio=cursor.getString(8).substring(0,10);
				if(cursor.getString(9) != null)
					ffin=cursor.getString(9).substring(0,10);
				else
					ffin=cursor.getString(9);
				finplazo=cursor.getString(10).substring(0,10);
				
				filas[i][0] = Integer.toString(id);
				filas[i][1] = dni;
				filas[i][2] = nombre;
				filas[i][3] = apellido;
				filas[i][4] = titulo;
				filas[i][5] = autor;
				filas[i][6] = aautor;
				filas[i][7] = finicio;
				filas[i][8] = ffin;
				filas[i][9] = finplazo;
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
