package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DanhSachKhuyenMaiCtrl {
	
	@FXML
	public Button btnTaoKM;

	public void chuyenDenChiTietKM() {
		
	}
	
	public void hienThiTrangTaoKM() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ThemKhuyenMai.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Thêm khuyến mãi");
			stage.setScene(new Scene(root));
			stage.sizeToScene();
			stage.centerOnScreen();
			stage.setResizable(false);
			
			//stage.initModality(Modality.APPLICATION_MODAL); //Chặn thao tác trên màn hình chính khi pop up đang mở
			
			Stage currentStage = (Stage) btnTaoKM.getScene().getWindow();
			stage.initOwner(currentStage);
			
			stage.showAndWait();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
