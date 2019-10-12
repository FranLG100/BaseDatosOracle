package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
	
	public enum AccionMVC {
		__GOTO_USUARIOS, __GOTO_INICIO,
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
		
		this.vista.bUsuariosPI.setActionCommand("__GOTO_USUARIOS");
		this.vista.bUsuariosPI.addActionListener(this);

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
