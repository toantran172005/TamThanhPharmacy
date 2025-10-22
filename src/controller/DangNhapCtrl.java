package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DangNhapCtrl {
	
	@FXML public TextField txtPassOpen;
	@FXML public PasswordField txpPassClose;
	@FXML public ImageView imgEye;
	@FXML public Button btnDangNhap;
	private Image matMo;
	private Image matDong;
	private boolean doiMat = false;
	
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
	
	@FXML public void initialize() {
//		load icon từ resource
		matMo = new Image(getClass().getResourceAsStream("/picture/dangNhap/eye.png"));
		matDong = new Image(getClass().getResourceAsStream("/picture/dangNhap/closed-eyes.png"));
		
//		đồng bộ 2 field
		txtPassOpen.textProperty().bindBidirectional(txpPassClose.textProperty());

//		icon mặc định
		imgEye.setImage(matDong);
	}
	
	@FXML private void hienMatKhau() {
		int conTro = doiMat ? txtPassOpen.getCaretPosition() : txpPassClose.getCaretPosition();
		
		doiMat = !doiMat;
		
		if(doiMat) {
			txtPassOpen.setVisible(true);
			txtPassOpen.setManaged(true);
			
			txpPassClose.setVisible(false);
			txpPassClose.setManaged(false);
			
			imgEye.setImage(matMo);
			
			txtPassOpen.requestFocus();
			txtPassOpen.positionCaret(conTro);
			
		} else {
			txpPassClose.setVisible(true);
			txpPassClose.setManaged(true);
			
			txtPassOpen.setVisible(false);
			txtPassOpen.setManaged(false);
			
			imgEye.setImage(matDong);
			
			txpPassClose.requestFocus();
			txpPassClose.positionCaret(conTro);
		}
	}
	
}
