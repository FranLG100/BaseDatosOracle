package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import modelo.GestorInterfaz;
import modelo.Historicos;
import modelo.Libro;
import modelo.Prestamo;
import modelo.Usuario;
import utilesFran.Amadeus;
import vista.Vista;

public class Controlador implements ActionListener, MouseListener {

	private Vista vista;
	private Amadeus amadeus=new Amadeus();
	private GestorInterfaz gui;
	private Usuario usuario=new Usuario();
	private Libro libro= new Libro();
	private Prestamo prestamo=new Prestamo();
	private Historicos historicos=new Historicos();
	private String dni,nombre,apellido,calle,ciudad,email,zip;
	private String titulo,editorial;
	private int id;
	private int codigo,disponibilidad;
	private int idUsuario, idLibro,idPrestamo;
	private String finicio,ffin;
	private boolean validez;
	
	boolean idValido=false,dniValido = false,nombreValido = false,apellidoValido = false,calleValido = false,ciudadValido = false,emailValido = false,zipValido = false;
	boolean tituloValido=false,nombreAutorValido = false,apellidoAutorValido = false,codigoValido = false,editorialValido = false;
	
	
	public enum AccionMVC {
		__GOTO_USUARIOS, __GOTO_INICIO, __CREAR_USUARIO, __MODIFICAR_USUARIO, __ELIMINAR_USUARIO,
		__CASTIGAR_USUARIO,__PERDONAR_USUARIO,
		__GOTO_LIBROS, __CREAR_LIBRO, __MODIFICAR_LIBRO, __ELIMINAR_LIBRO, __DEVOLVER_LIBRO,__PRESTAR_LIBRO,
		__GOTO_PRESTAMOS, __CREAR_PRESTAMO, __DEVOLVER_PRESTAMO, __ELIMINAR_PRESTAMO,
		__GOTO_HISTORICOS,
	}
	
	public Controlador(Vista vista) {
		this.vista = vista;
		this.gui = new GestorInterfaz(vista);
	}
	
	public void iniciar() {
		// Skin tipo WINDOWS
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(vista);
			vista.setVisible(true);
		} catch (UnsupportedLookAndFeelException ex) {
		} catch (ClassNotFoundException ex) {
		} catch (InstantiationException ex) {
		} catch (IllegalAccessException ex) {
		}
		
		gui.cambiarPanel(vista.pRelleno);
		gui.cambiarPanel(vista.pInicio);

		// declara una acciﾃｳn y aﾃｱade un escucha al evento producido por el componente
		this.vista.bVolverPIPUs.setActionCommand("__GOTO_INICIO");
		this.vista.bVolverPIPUs.addActionListener(this);
		
		this.vista.bVolverPIPLib.setActionCommand("__GOTO_INICIO");
		this.vista.bVolverPIPLib.addActionListener(this);
		
		this.vista.bVolverPIPPrest.setActionCommand("__GOTO_INICIO");
		this.vista.bVolverPIPPrest.addActionListener(this);
		
		this.vista.bVolverPIPPrestHist.setActionCommand("__GOTO_INICIO");
		this.vista.bVolverPIPPrestHist.addActionListener(this);
		
		this.vista.bLibrosPI.setActionCommand("__GOTO_LIBROS");
		this.vista.bLibrosPI.addActionListener(this);
		
		this.vista.bUsuariosPI.setActionCommand("__GOTO_USUARIOS");
		this.vista.bUsuariosPI.addActionListener(this);
		
		this.vista.bHistoricosPI.setActionCommand("__GOTO_HISTORICOS");
		this.vista.bHistoricosPI.addActionListener(this);
		
		this.vista.btnCrearUsuario.setActionCommand("__CREAR_USUARIO");
		this.vista.btnCrearUsuario.addActionListener(this);
		
		this.vista.btnModificarUsuario.setActionCommand("__MODIFICAR_USUARIO");
		this.vista.btnModificarUsuario.addActionListener(this);
		
		this.vista.btnEliminarUsuario.setActionCommand("__ELIMINAR_USUARIO");
		this.vista.btnEliminarUsuario.addActionListener(this);
		
		this.vista.btnCastigarUsuario.setActionCommand("__CASTIGAR_USUARIO");
		this.vista.btnCastigarUsuario.addActionListener(this);
		
		this.vista.btnPerdonarUsuario.setActionCommand("__PERDONAR_USUARIO");
		this.vista.btnPerdonarUsuario.addActionListener(this);
		
		this.vista.btnCrearLibro.setActionCommand("__CREAR_LIBRO");
		this.vista.btnCrearLibro.addActionListener(this);
		
		this.vista.btnModificarLibro.setActionCommand("__MODIFICAR_LIBRO");
		this.vista.btnModificarLibro.addActionListener(this);
		
		this.vista.btnEliminarLibro.setActionCommand("__ELIMINAR_LIBRO");
		this.vista.btnEliminarLibro.addActionListener(this);
		
		this.vista.btnDisponibleLibro.setActionCommand("__DEVOLVER_LIBRO");
		this.vista.btnDisponibleLibro.addActionListener(this);
		
		this.vista.btnCambiarAPrestar.setActionCommand("__PRESTAR_LIBRO");
		this.vista.btnCambiarAPrestar.addActionListener(this);
		
		
		
		this.vista.bPrestamosPI.setActionCommand("__GOTO_PRESTAMOS");
		this.vista.bPrestamosPI.addActionListener(this);
		
		this.vista.btnCrearPrestamo.setActionCommand("__CREAR_PRESTAMO");
		this.vista.btnCrearPrestamo.addActionListener(this);
		
		this.vista.btnDevolverLibroPrestamo.setActionCommand("__DEVOLVER_PRESTAMO");
		this.vista.btnDevolverLibroPrestamo.addActionListener(this);
		
		this.vista.btnEliminarPrestamo.setActionCommand("__ELIMINAR_PRESTAMO");
		this.vista.btnEliminarPrestamo.addActionListener(this);
		


	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (AccionMVC.valueOf(e.getActionCommand())) {
		case __GOTO_INICIO:
			gui.cambiarPanel(vista.pInicio);
			break;
		case __GOTO_USUARIOS:
			gui.cambiarPanel(vista.pUsuarios);
			this.vista.tablaUsuarios.setModel(usuario.listarUsuarios());
			break;
		case __CREAR_USUARIO:
			validez=true;
			try {
				dniValido=amadeus.compruebaTextoVacio(vista.cDNIUsuario.getText().toString().trim());
				nombreValido=amadeus.compruebaTextoVacio(vista.cNombreUsuario.getText().toString().trim());
				apellidoValido=amadeus.compruebaTextoVacio(vista.cApellidoUsuario.getText().toString().trim());
				calleValido=amadeus.compruebaTextoVacio(vista.cCalleUsuario.getText().toString().trim());
				ciudadValido=amadeus.compruebaTextoVacio(vista.cCiudadUsuario.getText().toString().trim());
				emailValido=amadeus.compruebaTextoVacio(vista.cEmailUsuario.getText().toString().trim());
				zipValido=amadeus.compruebaTextoVacio(vista.cZipUsuario.getText().toString().trim());
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if(!dniValido || !nombreValido || !apellidoValido || !calleValido || !ciudadValido || !emailValido || !zipValido) {
				validez=false;
				JOptionPane.showMessageDialog(null, "Hay campos en blanco");
			}
			try {
				
				dniValido=amadeus.compruebaNIF(vista.cDNIUsuario.getText().toString().trim());
				
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if(!dniValido) {
				validez=false;
				JOptionPane.showMessageDialog(null, "DNI invalido");
			}
			try {
				zipValido=amadeus.controlaIntPositivoValido(Integer.parseInt(this.vista.cZipUsuario.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en Zip");
				validez=false;
			}
			if(!zipValido ) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de Zip");
				validez=false;
			}
			zip=vista.cZipUsuario.getText().toString().trim();
			if(zip.length()!=5) {
				JOptionPane.showMessageDialog(null, "El zip debe tener 5 digitos");
				validez=false;
			}
			if(validez) {
			dni=vista.cDNIUsuario.getText().toString().trim();
			nombre=vista.cNombreUsuario.getText().toString().trim();
			apellido=vista.cApellidoUsuario.getText().toString().trim();
			calle=vista.cCalleUsuario.getText().toString().trim();
			ciudad=vista.cCiudadUsuario.getText().toString().trim();
			email=vista.cEmailUsuario.getText().toString().trim();
			//zip=vista.cZipUsuario.getText().toString().trim();
			try {
				usuario.insertarUsuario(dni, nombre, apellido, calle, ciudad, zip, email);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.vista.tablaUsuarios.setModel(usuario.listarUsuarios());
			}
			break;
		case __MODIFICAR_USUARIO:
			validez=true;
			try {
				dniValido=amadeus.compruebaTextoVacio(vista.cDNIUsuario.getText().toString().trim());
				nombreValido=amadeus.compruebaTextoVacio(vista.cNombreUsuario.getText().toString().trim());
				apellidoValido=amadeus.compruebaTextoVacio(vista.cApellidoUsuario.getText().toString().trim());
				calleValido=amadeus.compruebaTextoVacio(vista.cCalleUsuario.getText().toString().trim());
				ciudadValido=amadeus.compruebaTextoVacio(vista.cCiudadUsuario.getText().toString().trim());
				emailValido=amadeus.compruebaTextoVacio(vista.cEmailUsuario.getText().toString().trim());
				zipValido=amadeus.compruebaTextoVacio(vista.cZipUsuario.getText().toString().trim());
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if(!dniValido || !nombreValido || !apellidoValido || !calleValido || !ciudadValido || !emailValido || !zipValido) {
				validez=false;
				JOptionPane.showMessageDialog(null, "Hay campos en blanco");
			}
			try {
				
				dniValido=amadeus.compruebaNIF(vista.cDNIUsuario.getText().toString().trim());
				
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if(!dniValido) {
				validez=false;
				JOptionPane.showMessageDialog(null, "DNI invalido");
			}
			try {
				zipValido=amadeus.controlaIntPositivoValido(Integer.parseInt(this.vista.cZipUsuario.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en Zip");
				validez=false;
			}
			if(!zipValido ) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de Zip");
				validez=false;
			}
			zip=vista.cZipUsuario.getText().toString().trim();
			if(zip.length()!=5) {
				JOptionPane.showMessageDialog(null, "El zip debe tener 5 digitos");
				validez=false;
			}
			try {
				idValido=amadeus.controlaIntPositivoValido(Integer.parseInt(this.vista.cIDModificarUsuario.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en ID");
				validez=false;
			}
			if(!idValido ) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de ID");
				validez=false;
			}
			if(validez) {
				dni=vista.cDNIUsuario.getText().toString().trim();
				nombre=vista.cNombreUsuario.getText().toString().trim();
				apellido=vista.cApellidoUsuario.getText().toString().trim();
				calle=vista.cCalleUsuario.getText().toString().trim();
				ciudad=vista.cCiudadUsuario.getText().toString().trim();
				email=vista.cEmailUsuario.getText().toString().trim();
				zip=vista.cZipUsuario.getText().toString().trim();
				id=Integer.parseInt(vista.cIDModificarUsuario.getText().toString().trim());
				try {
					usuario.modificarUsuario(dni, nombre, apellido, calle, ciudad, zip, email,id);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "ID incorrecto");
				} catch(NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "ID no numerico");
				}
				this.vista.tablaUsuarios.setModel(usuario.listarUsuarios());
			}
			break;
		case __ELIMINAR_USUARIO:
			try {
				idValido=amadeus.controlaIntPositivoValido(Integer.parseInt(this.vista.cIDEliminarUsuario.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en ID");
				validez=false;
			}
			if(!idValido ) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de ID");
				validez=false;
			}
			if(idValido) {
				id=Integer.parseInt(vista.cIDEliminarUsuario.getText().trim());
				try {
					usuario.borrarUsuario(id);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.vista.tablaUsuarios.setModel(usuario.listarUsuarios());
				}
			break;
		case __CASTIGAR_USUARIO:
			id=Integer.parseInt(vista.cIDPenalizarUsuario.getText().trim());
			try {
				usuario.castigarUsuario(id);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.vista.tablaUsuarios.setModel(usuario.listarUsuarios());
			break;
		case __PERDONAR_USUARIO:
			id=Integer.parseInt(vista.cIDPerdonarUsuario.getText().trim());
			try {
				usuario.perdonarUsuario(id);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.vista.tablaUsuarios.setModel(usuario.listarUsuarios());
			break;
		case __GOTO_LIBROS:
			gui.cambiarPanel(vista.pLibros);
			this.vista.tablaLibros.setModel(libro.listarLibros());
			break;
///////////////////////////////////////////////////////////////////////////////////////////			
////////   *** LIBROS ***   ///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////	
		case __CREAR_LIBRO:
			validez=true;
			try {
				tituloValido=amadeus.compruebaTextoVacio(vista.cTituloLibro.getText().toString().trim());
				nombreAutorValido=amadeus.compruebaTextoVacio(vista.cNombreLibro.getText().toString().trim());
				apellidoAutorValido=amadeus.compruebaTextoVacio(vista.cApellidoLibro.getText().toString().trim());
				editorialValido=amadeus.compruebaTextoVacio(vista.cEditorialLibro.getText().toString().trim());
				codigoValido=amadeus.compruebaTextoVacio(vista.cTipoLibro.getText().toString().trim());
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if(!tituloValido || !nombreAutorValido || !apellidoAutorValido || !editorialValido || !codigoValido) {
				validez=false;
				JOptionPane.showMessageDialog(null, "Hay campos en blanco");
			}
			codigo=Integer.parseInt(vista.cTipoLibro.getText().trim());
			if(codigo>3||codigo<1) {
				JOptionPane.showMessageDialog(null, "El código de libro debe ser 1,2 o 3\n1:7 dias\n2:14 dias\n3:21 dias");
				validez=false;
			}
			if(validez) {
				titulo=vista.cTituloLibro.getText().toString().trim();
				nombre=vista.cNombreLibro.getText().toString().trim();
				apellido=vista.cApellidoLibro.getText().toString().trim();
				editorial=vista.cEditorialLibro.getText().toString().trim();
				//codigo=Integer.parseInt(vista.cTipoLibro.getText().trim());
				try {
					libro.insertarLibro(titulo, nombre, apellido, editorial, codigo);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.vista.tablaLibros.setModel(libro.listarLibros());
				}
			break;
		case __MODIFICAR_LIBRO:
			validez=true;
			try {
				tituloValido=amadeus.compruebaTextoVacio(vista.cTituloLibro.getText().toString().trim());
				nombreAutorValido=amadeus.compruebaTextoVacio(vista.cNombreLibro.getText().toString().trim());
				apellidoAutorValido=amadeus.compruebaTextoVacio(vista.cApellidoLibro.getText().toString().trim());
				editorialValido=amadeus.compruebaTextoVacio(vista.cEditorialLibro.getText().toString().trim());
				codigoValido=amadeus.compruebaTextoVacio(vista.cTipoLibro.getText().toString().trim());
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if(!tituloValido || !nombreAutorValido || !apellidoAutorValido || !editorialValido || !codigoValido) {
				validez=false;
				JOptionPane.showMessageDialog(null, "Hay campos en blanco");
			}
			codigo=Integer.parseInt(vista.cTipoLibro.getText().trim());
			if(codigo>3||codigo<1) {
				JOptionPane.showMessageDialog(null, "El código de libro debe ser 1,2 o 3\n1:7 dias\n2:14 dias\n3:21 dias");
				validez=false;
			}
			try {
				idValido=amadeus.controlaIntPositivoValido(Integer.parseInt(this.vista.cIDModificarLibro.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en ID");
				validez=false;
			}
			if(!idValido ) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de ID");
				validez=false;
			}
			if(validez) {
				titulo=vista.cTituloLibro.getText().toString().trim();
				nombre=vista.cNombreLibro.getText().toString().trim();
				apellido=vista.cApellidoLibro.getText().toString().trim();
				editorial=vista.cEditorialLibro.getText().toString().trim();
				codigo=Integer.parseInt(vista.cTipoLibro.getText().trim());
				id=Integer.parseInt(vista.cIDModificarLibro.getText().toString().trim());
				try {
					libro.modificarLibro(titulo, nombre, apellido, editorial, codigo,id);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.vista.tablaLibros.setModel(libro.listarLibros());
			}
			break;
		case __ELIMINAR_LIBRO:
			validez=true;
			try {
				idValido=amadeus.controlaIntPositivoValido(Integer.parseInt(this.vista.cIDEliminarLibro.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en ID");
				validez=false;
			}
			if(!idValido ) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de ID");
				validez=false;
			}
			if(validez) {
				id=Integer.parseInt(vista.cIDEliminarLibro.getText().toString().trim());
				try {
					libro.borrarLibro(id);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.vista.tablaLibros.setModel(libro.listarLibros());
			}
			break;
		case __DEVOLVER_LIBRO:
			id=Integer.parseInt(vista.cIDDisponibleLibro.getText().toString().trim());
			try {
				libro.devolverLibro(id);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.vista.tablaLibros.setModel(libro.listarLibros());
			break;
		case __PRESTAR_LIBRO:
			id=Integer.parseInt(vista.cIDPrestarLibro.getText().toString().trim());
			try {
				libro.prestarLibro(id);;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.vista.tablaLibros.setModel(libro.listarLibros());
			break;
///////////////////////////////////////////////////////////////////////////////////////////			
////////   *** PRESTAMOS ***   ////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////		
		case __GOTO_PRESTAMOS:
			gui.cambiarPanel(vista.pPrestamos);
			this.vista.tablaPrestamos.setModel(prestamo.listarPrestamo());
			this.vista.tablaUsuariosPrestamos.setModel(usuario.listarUsuariosPrestamo());
			this.vista.tablaLibrosPrestamos.setModel(libro.listarLibrosPrestamo());
			break;
		case __CREAR_PRESTAMO:
			validez=true;
			int control=0;
			boolean idUsuarioValido=false,idLibroValido=false,finicioValido=false;
			try {
				idUsuarioValido=amadeus.compruebaTextoVacio(vista.cUsuarioPrestamo.getText().toString().trim());
				idLibroValido=amadeus.compruebaTextoVacio(vista.cLibroPrestamo.getText().toString().trim());
				finicioValido=amadeus.compruebaTextoVacio(vista.cFechaInicioPrestamo.getText().toString().trim());
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				idUsuarioValido=amadeus.controlaIntPositivoValido(Integer.parseInt(this.vista.cUsuarioPrestamo.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en ID Usuario");
				validez=false;
			}
			if(!idUsuarioValido ) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de ID");
				validez=false;
			}
			try {
				idLibroValido=amadeus.controlaIntPositivoValido(Integer.parseInt(this.vista.cLibroPrestamo.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en ID Libro");
				validez=false;
			}
			if(!idLibroValido ) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de ID");
				validez=false;
			}
			if( !idUsuarioValido || !idLibroValido || !finicioValido ) {
				validez=false;
				JOptionPane.showMessageDialog(null, "Hay campos en blanco");
			}
			if(validez) {
				idUsuario=Integer.parseInt(vista.cUsuarioPrestamo.getText().toString().trim());
				idLibro=Integer.parseInt(vista.cLibroPrestamo.getText().toString().trim());
				finicio=vista.cFechaInicioPrestamo.getText().toString().trim();
				try {
					control=prestamo.insertarPrestamo(idUsuario, idLibro, finicio);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Fecha o id inexistente");
					validez=false;
				}
				this.vista.tablaPrestamos.setModel(prestamo.listarPrestamo());
				this.vista.tablaUsuariosPrestamos.setModel(usuario.listarUsuariosPrestamo());
				this.vista.tablaLibrosPrestamos.setModel(libro.listarLibrosPrestamo());
				if(control!=1)
					JOptionPane.showMessageDialog(null, "Prestamo no posible");
			}
			break;
		case __DEVOLVER_PRESTAMO:
			validez=true;
			boolean idPrestamoValido=false,ffinValido=false;
			try {
				idPrestamoValido=amadeus.compruebaTextoVacio(vista.cIDDevolverLibroPrestamo.getText().toString().trim());
				ffinValido=amadeus.compruebaTextoVacio(vista.cFechaDevolucionPrestamo.getText().toString().trim());
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				idPrestamoValido=amadeus.controlaIntPositivoValido(Integer.parseInt(this.vista.cIDDevolverLibroPrestamo.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en ID Prestamo");
				validez=false;
			}
			if(!idPrestamoValido ) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de ID");
				validez=false;
			}
			
			if( !idPrestamoValido || !ffinValido ) {
				validez=false;
				JOptionPane.showMessageDialog(null, "Hay campos en blanco");
			}
			if(validez) {
				idPrestamo=Integer.parseInt(vista.cIDDevolverLibroPrestamo.getText().toString().trim());
				ffin=vista.cFechaDevolucionPrestamo.getText().toString().trim();
				try {
					prestamo.devolverPrestamo(idPrestamo, ffin);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Fecha o id inexistente");
				}
				this.vista.tablaPrestamos.setModel(prestamo.listarPrestamo());
				this.vista.tablaUsuariosPrestamos.setModel(usuario.listarUsuariosPrestamo());
				this.vista.tablaLibrosPrestamos.setModel(libro.listarLibrosPrestamo());
			}
			break;
		case __ELIMINAR_PRESTAMO:
			validez=true;
			boolean idPrestamoEliminarValido=false;
			try {
				idPrestamoEliminarValido=amadeus.compruebaTextoVacio(vista.cIDEliminarPrestamo.getText().toString().trim());
				} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				idPrestamoEliminarValido=amadeus.controlaIntPositivoValido(Integer.parseInt(this.vista.cIDDevolverLibroPrestamo.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en ID Eliminar Prestamo");
				validez=false;
			}
			if(!idPrestamoEliminarValido ) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de ID");
				validez=false;
			}
			
			if( !idPrestamoEliminarValido ) {
				validez=false;
				JOptionPane.showMessageDialog(null, "Hay campos en blanco");
			}
			if(validez) {
				id=Integer.parseInt(vista.cIDEliminarPrestamo.getText().toString().trim());
				try {
					prestamo.borrarPrestamo(id);;
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Id inexistente");
				}
				this.vista.tablaPrestamos.setModel(prestamo.listarPrestamo());
				this.vista.tablaUsuariosPrestamos.setModel(usuario.listarUsuariosPrestamo());
				this.vista.tablaLibrosPrestamos.setModel(libro.listarLibrosPrestamo());
			}
				break;
			case __GOTO_HISTORICOS:
				gui.cambiarPanel(vista.pHistoricos);
				this.vista.tablaPrestamosHistoricos.setModel(historicos.listarHistoricos());
				break;
		}
		}
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	
}
