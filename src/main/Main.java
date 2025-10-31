package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import gui.DangNhap_GUI;

public class Main {
	public static void main(String[] args) {
	    try {
	        // Just use the system look and feel (matches Windows/macOS/Linux)
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    SwingUtilities.invokeLater(() -> {
	        new DangNhap_GUI().setVisible(true);
	    });
	}

}
