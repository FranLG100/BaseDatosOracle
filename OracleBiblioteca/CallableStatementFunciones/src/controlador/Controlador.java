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

import modelo.Audiolibro;
import modelo.GestorInterfaz;
import modelo.Historicos;
import modelo.HistoricosAudiolibros;
import modelo.Libro;
import modelo.Prestamo;
import modelo.PrestamoAudiolibros;
import modelo.Usuario;
import utilesFran.Amadeus;
import vista.Vista;

public class Controlador implements ActionListener, MouseListener {

	private Vista vista;
	private Amadeus amadeus=new Amadeus();
	private GestorInterfaz gui;
	private Usuario usuario=new Usuario();
	private Libro libro= new Libro();
	private Audiolibro audiolibro= new Audiolibro();
	private Prestamo prestamo=new Prestamo();
	private PrestamoAudiolibros prestamo_a=new PrestamoAudiolibros();
	private Historicos historicos=new Historicos();
	private HistoricosAudiolibros historicos_a=new HistoricosAudiolibros();
	private String dni,nombre,apellido,calle,ciudad,email,zip;
	private String titulo,editorial,sello,nlector,alector;
	private int id;
	private int codigo,disponibilidad;
	private int idUsuario, idLibro,idPrestamo;
	private String finicio,ffin;
	private boolean validez;
	private int control = 0;
	private boolean idUsuarioValido = false, idLibroValido = false, finicioValido = false;
	private boolean idPrestamoValido=false,ffinValido=false;
	private boolean idValido=false,dniValido = false,nombreValido = false,apellidoValido = false,calleValido = false,ciudadValido = false,emailValido = false,zipValido = false;
	private boolean tituloValido=false,nombreAutorValido = false,apellidoAutorValido = false,codigoValido = false,editorialValido = false, selloValido=false, nlectorValido=false,alectorValido=false;
	private boolean idPrestamoEliminarValido = false;
	private String[] datosLibro=new String[5];
	private String[] datosAudiolibro=new String[6];
	private String[] datosUsuario=new String[7];
	
	public enum AccionMVC {
		__GOTO_USUARIOS, __GOTO_INICIO, __CREAR_USUARIO, __MODIFICAR_USUARIO, __ELIMINAR_USUARIO,  __CONSULTAR_USUARIO,
		__CASTIGAR_USUARIO,__PERDONAR_USUARIO, __REBAJAR_CASTIGO,
		__GOTO_LIBROS, __CREAR_LIBRO, __MODIFICAR_LIBRO, __ELIMINAR_LIBRO, __DEVOLVER_LIBRO,__PRESTAR_LIBRO, __CONSULTAR_LIBRO,
		__GOTO_PRESTAMOS, __CREAR_PRESTAMO, __DEVOLVER_PRESTAMO, __ELIMINAR_PRESTAMO,
		__GOTO_HISTORICOS,
		__GOTO_AUDIOLIBRO, __CREAR_AUDIOLIBRO, __MODIFICAR_AUDIOLIBRO, __ELIMINAR_AUDIOLIBRO, __DEVOLVER_AUDIOLIBRO,__PRESTAR_AUDIOLIBRO, __CONSULTAR_AUDIOLIBRO,
		__GOTO_PRESTAMOS_AUDIOLIBROS, __CREAR_PRESTAMO_AUDIOLIBRO, __DEVOLVER_PRESTAMO_AUDIOLIBRO, __ELIMINAR_PRESTAMO_AUDIOLIBRO,
		__GOTO_HISTORICOS_AUDIOLIBROS,
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
		
		this.vista.bVolverPIPAud.setActionCommand("__GOTO_INICIO");
		this.vista.bVolverPIPAud.addActionListener(this);
		
		this.vista.bVolverPIPPrestHistAudiolibro.setActionCommand("__GOTO_INICIO");
		this.vista.bVolverPIPPrestHistAudiolibro.addActionListener(this);
		
		this.vista.bVolverPIPPrestAudiolibro.setActionCommand("__GOTO_INICIO");
		this.vista.bVolverPIPPrestAudiolibro.addActionListener(this);
		
		this.vista.bLibrosPI.setActionCommand("__GOTO_LIBROS");
		this.vista.bLibrosPI.addActionListener(this);
		
		this.vista.bAudiolibrosPI.setActionCommand("__GOTO_AUDIOLIBRO");
		this.vista.bAudiolibrosPI.addActionListener(this);
		
		this.vista.bUsuariosPI.setActionCommand("__GOTO_USUARIOS");
		this.vista.bUsuariosPI.addActionListener(this);
		
		this.vista.bHistoricosPI.setActionCommand("__GOTO_HISTORICOS");
		this.vista.bHistoricosPI.addActionListener(this);
		
		this.vista.bHistoricosAudiolibrosPI.setActionCommand("__GOTO_HISTORICOS_AUDIOLIBROS");
		this.vista.bHistoricosAudiolibrosPI.addActionListener(this);
		
		this.vista.btnCrearUsuario.setActionCommand("__CREAR_USUARIO");
		this.vista.btnCrearUsuario.addActionListener(this);
		
		this.vista.btnModificarUsuario.setActionCommand("__MODIFICAR_USUARIO");
		this.vista.btnModificarUsuario.addActionListener(this);
		
		this.vista.btnModificarUsuarioBuscar.setActionCommand("__CONSULTAR_USUARIO");
		this.vista.btnModificarUsuarioBuscar.addActionListener(this);
		
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
		
		this.vista.btnModificarLibroBuscar.setActionCommand("__CONSULTAR_LIBRO");
		this.vista.btnModificarLibroBuscar.addActionListener(this);
		
		this.vista.btnDisponibleLibro.setActionCommand("__DEVOLVER_LIBRO");
		this.vista.btnDisponibleLibro.addActionListener(this);
		
		this.vista.btnCambiarAPrestar.setActionCommand("__PRESTAR_LIBRO");
		this.vista.btnCambiarAPrestar.addActionListener(this);
		
		
		
		
		this.vista.btnCrearAudiolibro.setActionCommand("__CREAR_AUDIOLIBRO");
		this.vista.btnCrearAudiolibro.addActionListener(this);
		
		this.vista.btnModificarAudiolibro.setActionCommand("__MODIFICAR_AUDIOLIBRO");
		this.vista.btnModificarAudiolibro.addActionListener(this);
		
		this.vista.btnEliminarAudiolibro.setActionCommand("__ELIMINAR_AUDIOLIBRO");
		this.vista.btnEliminarAudiolibro.addActionListener(this);
		
		this.vista.btnDisponibleLibro1.setActionCommand("__DEVOLVER_AUDIOLIBRO");
		this.vista.btnDisponibleLibro1.addActionListener(this);
		
		this.vista.btnCambiarAPrestar1.setActionCommand("__PRESTAR_AUDIOLIBRO");
		this.vista.btnCambiarAPrestar1.addActionListener(this);
		
		this.vista.btnModificarAudiolibroBuscar.setActionCommand("__CONSULTAR_AUDIOLIBRO");
		this.vista.btnModificarAudiolibroBuscar.addActionListener(this);
		
		
		
		this.vista.bPrestamosPI.setActionCommand("__GOTO_PRESTAMOS");
		this.vista.bPrestamosPI.addActionListener(this);
		
		this.vista.btnCrearPrestamo.setActionCommand("__CREAR_PRESTAMO");
		this.vista.btnCrearPrestamo.addActionListener(this);
		
		this.vista.btnDevolverLibroPrestamo.setActionCommand("__DEVOLVER_PRESTAMO");
		this.vista.btnDevolverLibroPrestamo.addActionListener(this);
		
		this.vista.btnEliminarPrestamo.setActionCommand("__ELIMINAR_PRESTAMO");
		this.vista.btnEliminarPrestamo.addActionListener(this);
		
		
		
		
		this.vista.bPrestamosAudiolibrosPI.setActionCommand("__GOTO_PRESTAMOS_AUDIOLIBROS");
		this.vista.bPrestamosAudiolibrosPI.addActionListener(this);
		
		this.vista.btnCrearPrestamoAudiolibro.setActionCommand("__CREAR_PRESTAMO_AUDIOLIBRO");
		this.vista.btnCrearPrestamoAudiolibro.addActionListener(this);
		
		this.vista.btnDevolverLibroPrestamoAudiolibro.setActionCommand("__DEVOLVER_PRESTAMO_AUDIOLIBRO");
		this.vista.btnDevolverLibroPrestamoAudiolibro.addActionListener(this);
		
		this.vista.btnEliminarPrestamoAudiolibro.setActionCommand("__ELIMINAR_PRESTAMO_AUDIOLIBRO");
		this.vista.btnEliminarPrestamoAudiolibro.addActionListener(this);
		
		
		this.vista.btnRebajarCastigos.setActionCommand("__REBAJAR_CASTIGO");
		this.vista.btnRebajarCastigos.addActionListener(this);


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
		case __REBAJAR_CASTIGO:
			try {
				usuario.rebajarCastigos();
			} catch (SQLException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
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
		case __CONSULTAR_USUARIO:
			validez=true;
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
				id=Integer.parseInt(vista.cIDModificarUsuario.getText().toString().trim());
				datosUsuario=usuario.listarUsuario(id);
				this.vista.cDNIUsuario.setText(datosUsuario[0]);
				this.vista.cNombreUsuario.setText(datosUsuario[1]);
				this.vista.cApellidoUsuario.setText(datosUsuario[2]);
				this.vista.cCalleUsuario.setText(datosUsuario[3]);
				this.vista.cCiudadUsuario.setText(datosUsuario[4]);
				this.vista.cZipUsuario.setText(datosUsuario[5]);
				this.vista.cEmailUsuario.setText(datosUsuario[6]);
			}
			break;
///////////////////////////////////////////////////////////////////////////////////////////			
////////   *** LIBROS ***   ///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////	
		case __GOTO_LIBROS:
			gui.cambiarPanel(vista.pLibros);
			try {
				this.vista.tablaLibros.setModel(libro.listarLibros());
			} catch (SQLException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			break;
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
				try {
					this.vista.tablaLibros.setModel(libro.listarLibros());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
				try {
					this.vista.tablaLibros.setModel(libro.listarLibros());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
				try {
					this.vista.tablaLibros.setModel(libro.listarLibros());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			break;
		case __CONSULTAR_LIBRO:
			validez=true;
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
				id=Integer.parseInt(vista.cIDModificarLibro.getText().toString().trim());
				datosLibro=libro.listarLibro(id);
				this.vista.cTituloLibro.setText(datosLibro[0]);
				this.vista.cNombreLibro.setText(datosLibro[1]);
				this.vista.cApellidoLibro.setText(datosLibro[2]);
				this.vista.cEditorialLibro.setText(datosLibro[3]);
				this.vista.cTipoLibro.setText(datosLibro[4]);
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
			try {
				this.vista.tablaLibros.setModel(libro.listarLibros());
			} catch (SQLException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			break;
		case __PRESTAR_LIBRO:
			id=Integer.parseInt(vista.cIDPrestarLibro.getText().toString().trim());
			try {
				libro.prestarLibro(id);;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				this.vista.tablaLibros.setModel(libro.listarLibros());
			} catch (SQLException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			break;
///////////////////////////////////////////////////////////////////////////////////////////			
////////*** AUDIOLIBROS ***   ///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////	
		case __GOTO_AUDIOLIBRO:
			gui.cambiarPanel(vista.pAudiolibros);
			this.vista.tablaAudiolibros.setModel(audiolibro.listarAudiolibros());
			break;
		case __CREAR_AUDIOLIBRO:
			validez = true;
			try {
				tituloValido = amadeus.compruebaTextoVacio(vista.cTituloAudiolibro.getText().toString().trim());
				nombreAutorValido = amadeus.compruebaTextoVacio(vista.cNombreAudiolibro.getText().toString().trim());
				apellidoAutorValido = amadeus.compruebaTextoVacio(vista.cApellidoAudiolibro.getText().toString().trim());
				selloValido = amadeus.compruebaTextoVacio(vista.cSelloAudiolibro.getText().toString().trim());
				nlectorValido = amadeus.compruebaTextoVacio(vista.cNLectorAudiolibro.getText().toString().trim());
				alectorValido = amadeus.compruebaTextoVacio(vista.cALectorAudiolibro.getText().toString().trim());
			} catch (IOException e2) {
// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (!tituloValido || !nombreAutorValido || !apellidoAutorValido || !selloValido || !nlectorValido || !alectorValido) {
				validez = false;
				JOptionPane.showMessageDialog(null, "Hay campos en blanco");
			}
			if (validez) {
				titulo = vista.cTituloAudiolibro.getText().toString().trim();
				nombre = vista.cNombreAudiolibro.getText().toString().trim();
				apellido = vista.cApellidoAudiolibro.getText().toString().trim();
				sello = vista.cSelloAudiolibro.getText().toString().trim();
				nlector=vista.cNLectorAudiolibro.getText().toString().trim();
				alector=vista.cALectorAudiolibro.getText().toString().trim();
//codigo=Integer.parseInt(vista.cTipoLibro.getText().trim());
				try {
					audiolibro.insertarAudiolibro(titulo, nombre, apellido, sello,nlector,alector);
				} catch (SQLException e1) {
// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.vista.tablaAudiolibros.setModel(audiolibro.listarAudiolibros());
			}
			break;
		case __MODIFICAR_AUDIOLIBRO:
			validez = true;
			try {
				tituloValido = amadeus.compruebaTextoVacio(vista.cTituloAudiolibro.getText().toString().trim());
				nombreAutorValido = amadeus.compruebaTextoVacio(vista.cNombreAudiolibro.getText().toString().trim());
				apellidoAutorValido = amadeus.compruebaTextoVacio(vista.cApellidoAudiolibro.getText().toString().trim());
				selloValido = amadeus.compruebaTextoVacio(vista.cSelloAudiolibro.getText().toString().trim());
				nlectorValido = amadeus.compruebaTextoVacio(vista.cNLectorAudiolibro.getText().toString().trim());
				alectorValido = amadeus.compruebaTextoVacio(vista.cALectorAudiolibro.getText().toString().trim());
				} catch (IOException e2) {
// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (!tituloValido || !nombreAutorValido || !apellidoAutorValido || !selloValido || !nlectorValido || !alectorValido) {
				validez = false;
				JOptionPane.showMessageDialog(null, "Hay campos en blanco");
			}
			try {
				idValido = amadeus.controlaIntPositivoValido(
						Integer.parseInt(this.vista.cIDModificarAudiolibro.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en ID");
				validez = false;
			}
			if (!idValido) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de ID");
				validez = false;
			}
			if (validez) {
				titulo = vista.cTituloAudiolibro.getText().toString().trim();
				nombre = vista.cNombreAudiolibro.getText().toString().trim();
				apellido = vista.cApellidoAudiolibro.getText().toString().trim();
				sello = vista.cSelloAudiolibro.getText().toString().trim();
				nlector=vista.cNLectorAudiolibro.getText().toString().trim();
				alector=vista.cALectorAudiolibro.getText().toString().trim();
				id = Integer.parseInt(vista.cIDModificarAudiolibro.getText().toString().trim());
				try {
					audiolibro.modificarAudiolibro(titulo, nombre, apellido, sello, nlector, alector, id);
				} catch (SQLException e1) {
// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.vista.tablaAudiolibros.setModel(audiolibro.listarAudiolibros());
			}
			break;
		case __ELIMINAR_AUDIOLIBRO:
			validez = true;
			try {
				idValido = amadeus.controlaIntPositivoValido(
						Integer.parseInt(this.vista.cIDEliminarAudiolibro.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en ID");
				validez = false;
			}
			if (!idValido) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de ID");
				validez = false;
			}
			if (validez) {
				id = Integer.parseInt(vista.cIDEliminarAudiolibro.getText().toString().trim());
				try {
					audiolibro.borrarAudiolibro(id);
				} catch (SQLException e1) {
// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.vista.tablaAudiolibros.setModel(audiolibro.listarAudiolibros());
			}
			break;
		case __CONSULTAR_AUDIOLIBRO:
			validez=true;
			try {
				idValido=amadeus.controlaIntPositivoValido(Integer.parseInt(this.vista.cIDModificarAudiolibro.getText().toString().trim()));
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
				id=Integer.parseInt(vista.cIDModificarAudiolibro.getText().toString().trim());
				datosAudiolibro=audiolibro.listarAudiolibro(id);
				this.vista.cTituloAudiolibro.setText(datosAudiolibro[0]);
				this.vista.cNombreAudiolibro.setText(datosAudiolibro[1]);
				this.vista.cApellidoAudiolibro.setText(datosAudiolibro[2]);
				this.vista.cSelloAudiolibro.setText(datosAudiolibro[3]);
				this.vista.cNLectorAudiolibro.setText(datosAudiolibro[4]);
				this.vista.cALectorAudiolibro.setText(datosAudiolibro[5]);
			}
			break;
		case __DEVOLVER_AUDIOLIBRO:
			id = Integer.parseInt(vista.cIDDisponibleAudiolibro.getText().toString().trim());
			try {
				audiolibro.devolverAudiolibro(id);
			} catch (SQLException e1) {
// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.vista.tablaAudiolibros.setModel(audiolibro.listarAudiolibros());
			break;
		case __PRESTAR_AUDIOLIBRO:
			id = Integer.parseInt(vista.cIDPrestarAudiolibro.getText().toString().trim());
			try {
				audiolibro.prestarAudiolibro(id);
				;
			} catch (SQLException e1) {
// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.vista.tablaAudiolibros.setModel(audiolibro.listarAudiolibros());
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
///////////////////////////////////////////////////////////////////////////////////////////			
////////*** PRESTAMOS AUDIOLIBROS***   ////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////		
		case __GOTO_PRESTAMOS_AUDIOLIBROS:
			gui.cambiarPanel(vista.pPrestamosAudiolibros);
			this.vista.tablaPrestamosAudiolibro.setModel(prestamo_a.listarPrestamo());
			this.vista.tablaUsuariosPrestamosAudiolibro.setModel(usuario.listarUsuariosPrestamo());
			this.vista.tablaLibrosPrestamosAudiolibro.setModel(audiolibro.listarAudiolibrosPrestamo());
			break;
		case __CREAR_PRESTAMO_AUDIOLIBRO:
			validez = true;
			
			try {
				idUsuarioValido = amadeus.compruebaTextoVacio(vista.cUsuarioPrestamoAudiolibro.getText().toString().trim());
				idLibroValido = amadeus.compruebaTextoVacio(vista.cLibroPrestamoAudiolibro.getText().toString().trim());
				finicioValido = amadeus.compruebaTextoVacio(vista.cFechaInicioPrestamoAudiolibro.getText().toString().trim());
			} catch (IOException e2) {
// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				idUsuarioValido = amadeus.controlaIntPositivoValido(
						Integer.parseInt(this.vista.cUsuarioPrestamoAudiolibro.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en ID Usuario");
				validez = false;
			}
			if (!idUsuarioValido) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de ID");
				validez = false;
			}
			try {
				idLibroValido = amadeus.controlaIntPositivoValido(
						Integer.parseInt(this.vista.cLibroPrestamoAudiolibro.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en ID Libro");
				validez = false;
			}
			if (!idLibroValido) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de ID");
				validez = false;
			}
			if (!idUsuarioValido || !idLibroValido || !finicioValido) {
				validez = false;
				JOptionPane.showMessageDialog(null, "Hay campos en blanco");
			}
			if (validez) {
				idUsuario = Integer.parseInt(vista.cUsuarioPrestamoAudiolibro.getText().toString().trim());
				idLibro = Integer.parseInt(vista.cLibroPrestamoAudiolibro.getText().toString().trim());
				finicio = vista.cFechaInicioPrestamoAudiolibro.getText().toString().trim();
				try {
					control = prestamo_a.insertarPrestamo(idUsuario, idLibro, finicio);
				} catch (SQLException e1) {
// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Fecha o id inexistente");
					validez = false;
				}
				this.vista.tablaPrestamosAudiolibro.setModel(prestamo_a.listarPrestamo());
				this.vista.tablaUsuariosPrestamosAudiolibro.setModel(usuario.listarUsuariosPrestamo());
				this.vista.tablaLibrosPrestamosAudiolibro.setModel(audiolibro.listarAudiolibrosPrestamo());
				if (control != 1)
					JOptionPane.showMessageDialog(null, "Prestamo no posible");
			}
			break;
		case __DEVOLVER_PRESTAMO_AUDIOLIBRO:
			validez = true;
			try {
				idPrestamoValido = amadeus
						.compruebaTextoVacio(vista.cIDDevolverLibroPrestamoAudiolibro.getText().toString().trim());
				ffinValido = amadeus.compruebaTextoVacio(vista.cFechaDevolucionPrestamoAudiolibro.getText().toString().trim());
			} catch (IOException e2) {
// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				idPrestamoValido = amadeus.controlaIntPositivoValido(
						Integer.parseInt(this.vista.cIDDevolverLibroPrestamoAudiolibro.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en ID Prestamo");
				validez = false;
			}
			if (!idPrestamoValido) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de ID");
				validez = false;
			}

			if (!idPrestamoValido || !ffinValido) {
				validez = false;
				JOptionPane.showMessageDialog(null, "Hay campos en blanco");
			}
			if (validez) {
				idPrestamo = Integer.parseInt(vista.cIDDevolverLibroPrestamoAudiolibro.getText().toString().trim());
				ffin = vista.cFechaDevolucionPrestamoAudiolibro.getText().toString().trim();
				try {
					prestamo_a.devolverPrestamo(idPrestamo, ffin);
				} catch (SQLException e1) {
// TODO Auto-generated catch block
					e1.printStackTrace();
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Fecha o id inexistente");
				}
				this.vista.tablaPrestamosAudiolibro.setModel(prestamo_a.listarPrestamo());
				this.vista.tablaUsuariosPrestamosAudiolibro.setModel(usuario.listarUsuariosPrestamo());
				this.vista.tablaLibrosPrestamosAudiolibro.setModel(audiolibro.listarAudiolibrosPrestamo());
			}
			break;
		case __ELIMINAR_PRESTAMO_AUDIOLIBRO:
			validez = true;
			
			try {
				idPrestamoEliminarValido = amadeus
						.compruebaTextoVacio(vista.cIDEliminarPrestamoAudiolibro.getText().toString().trim());
			} catch (IOException e2) {
// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				idPrestamoEliminarValido = amadeus.controlaIntPositivoValido(
						Integer.parseInt(this.vista.cIDDevolverLibroPrestamoAudiolibro.getText().toString().trim()));
			} catch (NumberFormatException | IOException e1) {
// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Caracteres Invalidos en ID Eliminar Prestamo");
				validez = false;
			}
			if (!idPrestamoEliminarValido) {
				JOptionPane.showMessageDialog(null, "Introduzca un valor positivo de ID");
				validez = false;
			}

			if (!idPrestamoEliminarValido) {
				validez = false;
				JOptionPane.showMessageDialog(null, "Hay campos en blanco");
			}
			if (validez) {
				id = Integer.parseInt(vista.cIDEliminarPrestamoAudiolibro.getText().toString().trim());
				try {
					prestamo_a.borrarPrestamo(id);
					;
				} catch (SQLException e1) {
// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Id inexistente");
				}
				this.vista.tablaPrestamosAudiolibro.setModel(prestamo_a.listarPrestamo());
				this.vista.tablaUsuariosPrestamosAudiolibro.setModel(usuario.listarUsuariosPrestamo());
				this.vista.tablaLibrosPrestamosAudiolibro.setModel(audiolibro.listarAudiolibrosPrestamo());
			}
			break;
			case __GOTO_HISTORICOS:
				gui.cambiarPanel(vista.pHistoricos);
				this.vista.tablaPrestamosHistoricos.setModel(historicos.listarHistoricos());
				break;
			case __GOTO_HISTORICOS_AUDIOLIBROS:
				gui.cambiarPanel(vista.pHistoricosAudiolibro);
				this.vista.tablaPrestamosHistoricosAudiolibro.setModel(historicos_a.listarHistoricos());
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
