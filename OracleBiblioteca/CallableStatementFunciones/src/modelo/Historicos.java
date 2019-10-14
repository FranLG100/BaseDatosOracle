package modelo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.swing.table.DefaultTableModel;

import datos.Conexion;
import oracle.jdbc.OracleTypes;

public class Historicos {
	
	Connection con;
	CallableStatement cstmt = null;
	String nombre,dni,apellido,titulo,autor,finicio,ffin,finplazo;
	int tamanho, id;
	
	
	public DefaultTableModel listarHistoricos() {

		tamanho=0;
		String[] headers = { "ID","DNI","Usuario","Titulo","Autor","F.Inicio","F.Fin","Fin Plazo"};
		DefaultTableModel plantilla = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		try {
			con=Conexion.getConnection();
			cstmt=con.prepareCall("{call CONSULTA_HISTORICOS.obtener_historicos(?)}");
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

		String[][] filas = new String[tamanho][8];

		try {
			con=Conexion.getConnection();
			cstmt=con.prepareCall("{call CONSULTA_HISTORICOS.obtener_historicos(?)}");
		    cstmt.registerOutParameter(1, OracleTypes.CURSOR);
		    cstmt.executeQuery();
		    ResultSet cursor = (ResultSet)cstmt.getObject(1);
			int i = 0;
			while (cursor.next()) {
				id = cursor.getInt(1);
				dni=cursor.getString(2);
				nombre=cursor.getString(3)+" "+cursor.getString(4);
				titulo=cursor.getString(5);
				autor=cursor.getString(6)+" "+cursor.getString(7);
				finicio=cursor.getString(8).substring(0,10);
				if(cursor.getString(9) != null)
					ffin=cursor.getString(9).substring(0,10);
				else
					ffin=cursor.getString(9);
				finplazo=cursor.getString(10).substring(0,10);
				
				filas[i][0] = Integer.toString(id);
				filas[i][1] = dni;
				filas[i][2] = nombre;
				filas[i][3] = titulo;
				filas[i][4] = autor;
				filas[i][5] = finicio;
				filas[i][6] = ffin;
				filas[i][7] = finplazo;
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
