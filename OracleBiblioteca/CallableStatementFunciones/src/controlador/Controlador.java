package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import modelo.GestorInterfaz;
import modelo.Historicos;
import modelo.Libro;
import modelo.Prestamo;
import modelo.Usuario;
import vista.Vista;

public class Controlador implements ActionListener, MouseListener {

	private Vista vista;
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

		// declara una acción y añade un escucha al evento producido por el componente
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
			dni=vista.cDNIUsuario.getText().toString().trim();
			nombre=vista.cNombreUsuario.getText().toString().trim();
			apellido=vista.cApellidoUsuario.getText().toString().trim();
			calle=vista.cCalleUsuario.getText().toString().trim();
			ciudad=vista.cCiudadUsuario.getText().toString().trim();
			email=vista.cEmailUsuario.getText().toString().trim();
			zip=vista.cZipUsuario.getText().toString().trim();
			try {
				usuario.insertarUsuario(dni, nombre, apellido, calle, ciudad, zip, email);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.vista.tablaUsuarios.setModel(usuario.listarUsuarios());
			break;
		case __MODIFICAR_USUARIO:
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
			}
			this.vista.tablaUsuarios.setModel(usuario.listarUsuarios());
			break;
		case __ELIMINAR_USUARIO:
			id=Integer.parseInt(vista.cIDEliminarUsuario.getText().trim());
			try {
				usuario.borrarUsuario(id);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.vista.tablaUsuarios.setModel(usuario.listarUsuarios());
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
		case __CREAR_LIBRO:
			titulo=vista.cTituloLibro.getText().toString().trim();
			nombre=vista.cNombreLibro.getText().toString().trim();
			apellido=vista.cApellidoLibro.getText().toString().trim();
			editorial=vista.cEditorialLibro.getText().toString().trim();
			codigo=Integer.parseInt(vista.cTipoLibro.getText().trim());
			try {
				libro.insertarLibro(titulo, nombre, apellido, editorial, codigo);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.vista.tablaLibros.setModel(libro.listarLibros());
			break;
		case __MODIFICAR_LIBRO:
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
			break;
		case __ELIMINAR_LIBRO:
			id=Integer.parseInt(vista.cIDEliminarLibro.getText().toString().trim());
			try {
				libro.borrarLibro(id);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.vista.tablaLibros.setModel(libro.listarLibros());
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
			
			
		case __GOTO_PRESTAMOS:
			gui.cambiarPanel(vista.pPrestamos);
			this.vista.tablaPrestamos.setModel(prestamo.listarPrestamo());
			this.vista.tablaUsuariosPrestamos.setModel(usuario.listarUsuariosPrestamo());
			this.vista.tablaLibrosPrestamos.setModel(libro.listarLibrosPrestamo());
			break;
		case __CREAR_PRESTAMO:
			idUsuario=Integer.parseInt(vista.cUsuarioPrestamo.getText().toString().trim());
			idLibro=Integer.parseInt(vista.cLibroPrestamo.getText().toString().trim());
			finicio=vista.cFechaInicioPrestamo.getText().toString().trim();
			try {
				prestamo.insertarPrestamo(idUsuario, idLibro, finicio);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.vista.tablaPrestamos.setModel(prestamo.listarPrestamo());
			this.vista.tablaUsuariosPrestamos.setModel(usuario.listarUsuariosPrestamo());
			this.vista.tablaLibrosPrestamos.setModel(libro.listarLibrosPrestamo());
			break;
		case __DEVOLVER_PRESTAMO:
			idPrestamo=Integer.parseInt(vista.cIDDevolverLibroPrestamo.getText().toString().trim());
			ffin=vista.cFechaDevolucionPrestamo.getText().toString().trim();
			try {
				prestamo.devolverPrestamo(idPrestamo, ffin);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.vista.tablaPrestamos.setModel(prestamo.listarPrestamo());
			this.vista.tablaUsuariosPrestamos.setModel(usuario.listarUsuariosPrestamo());
			this.vista.tablaLibrosPrestamos.setModel(libro.listarLibrosPrestamo());
			break;
		case __ELIMINAR_PRESTAMO:
			id=Integer.parseInt(vista.cIDEliminarPrestamo.getText().toString().trim());
			try {
				prestamo.borrarPrestamo(id);;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.vista.tablaPrestamos.setModel(prestamo.listarPrestamo());
			this.vista.tablaUsuariosPrestamos.setModel(usuario.listarUsuariosPrestamo());
			this.vista.tablaLibrosPrestamos.setModel(libro.listarLibrosPrestamo());
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
