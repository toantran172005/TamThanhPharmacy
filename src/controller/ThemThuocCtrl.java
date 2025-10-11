package controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ThemThuocCtrl {
	
	@FXML VBox imgContainer;
	@FXML ImageView imgThuoc;
	@FXML Button btnChonAnh;
	
	@FXML public void chonAnh() {
		FileChooser fileChon = new FileChooser();
		fileChon.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Ảnh", "*.png", "*.jpg", "*.jpeg", "*.gif")
				);
		
		Stage stage = (Stage)btnChonAnh.getScene().getWindow();
		File file = fileChon.showOpenDialog(stage);
		
		if(file != null) {
			Image img = new Image(file.toURI().toString());
			imgThuoc.setImage(img);
			imgThuoc.setPreserveRatio(true);
			imgThuoc.setFitWidth(207);
			imgThuoc.setFitHeight(283);
			
			imgContainer.setStyle("-fx-background-color: white");
		}
	}
}
