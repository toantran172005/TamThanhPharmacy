package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DangNhapCtrl {
	
	@FXML public Button btnDangNhap;
	
	public void chuyenDenTrangChuNhanVien() {
		Stage stage = (Stage)btnDangNhap.getScene().getWindow();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/TrangChuQL.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        StackPane container = new StackPane(root);
        Scene scene = new Scene(container);		
		stage.setScene(scene);	
		stage.setMaximized(true);
        stage.setResizable(true);
	}
	
}
