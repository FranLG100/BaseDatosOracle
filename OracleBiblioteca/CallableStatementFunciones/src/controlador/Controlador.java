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
import modelo.Usuario;
import vista.Vista;

public class Controlador implements ActionListener, MouseListener {

	private Vista vista;
	private GestorInterfaz gui;
	private Usuario usuario=new Usuario();
	private String dni,nombre,apellido,calle,ciudad,email,zip;
	private int id;
	
	public enum AccionMVC {
		__GOTO_USUARIOS, __GOTO_INICIO, __CREAR_USUARIO, __MODIFICAR_USUARIO, __ELIMINAR_USUARIO,
		__CASTIGAR_USUARIO,__PERDONAR_USUARIO,
		__GOTO_LIBROS
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

		// declara una acción y añade un escucha al evento producido por el componente
		this.vista.bVolverPIPUs.setActionCommand("__GOTO_INICIO");
		this.vista.bVolverPIPUs.addActionListener(this);
		
		this.vista.bVolverPIPLib.setActionCommand("__GOTO_INICIO");
		this.vista.bVolverPIPLib.addActionListener(this);
		
		this.vista.bLibrosPI.setActionCommand("__GOTO_LIBROS");
		this.vista.bLibrosPI.addActionListener(this);
		
		this.vista.bUsuariosPI.setActionCommand("__GOTO_USUARIOS");
		this.vista.bUsuariosPI.addActionListener(this);
		
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
			//this.vista.tablaUsuarios.setModel(usuario.listarUsuarios());
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
