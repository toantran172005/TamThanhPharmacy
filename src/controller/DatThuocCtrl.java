package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DatThuocCtrl {
	
	@FXML Button btnTaoPhieuDat;
	
	@FXML public void hienThiPhieuDat() {
	
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PhieuDatThuoc.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Phiếu đặt thuốc");
			stage.setScene(new Scene(root));
			stage.sizeToScene();
			stage.centerOnScreen();
			stage.setResizable(false);
			
			//stage.initModality(Modality.APPLICATION_MODAL); //Chặn thao tác trên màn hình chính khi pop up đang mở
			
			Stage currentStage = (Stage) btnTaoPhieuDat.getScene().getWindow();
			stage.initOwner(currentStage);
			
			stage.showAndWait();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
