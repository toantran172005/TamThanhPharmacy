package controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class NhapThuocCtrl {
	
	@FXML Button btnThemTep;
	ToolCtrl thongBao = new ToolCtrl();
	
	@FXML public void chonTep() {
		FileChooser fileChon = new FileChooser();
		fileChon.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));	
		
		Stage stage = (Stage)btnThemTep.getScene().getWindow();
		File file = fileChon.showOpenDialog(stage);
	}
	
	
	
	
	//Lưu dữ liệu về database
	@FXML public int luuDuLieu(){
			thongBao.hienThiThongBaoThatBai("Lưu thất bại!","Lỗi gì ai bécc ^.^", "404");
			//hienThiThongBaoThanhCong("Lưu thành công nhé fen ^>^");
			
			return 1;
	}
}

