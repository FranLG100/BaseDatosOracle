/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.awt.Component;
import vista.*;

/**
 *
 * @author Alumno
 */
public class GestorInterfaz {

	Vista interfaces = null;

	public GestorInterfaz(Vista interfaces) {
		this.interfaces = interfaces;
	};

	public void cambiarPanel(Component c) {
		interfaces.pContainer.removeAll();
		interfaces.pContainer.add(c);
		interfaces.pContainer.repaint();
		interfaces.pContainer.revalidate();
		interfaces.pack();
	}

}
