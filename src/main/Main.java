package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import controller.DangNhapCtrl;
import gui.DangNhap_GUI;

public class Main {
	public static void main(String[] args) {
	    try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    SwingUtilities.invokeLater(() -> {
	    	DangNhap_GUI gui = new DangNhap_GUI();
	        new DangNhapCtrl(gui);  
	        gui.setVisible(true);
	        });
	}

}
