package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ThemKhieuNaiCtrl {
	@FXML public ComboBox<String> cmbLoaiDon;
	@FXML public ImageView imgThem;
	@FXML public ImageView imgLamMoi;
	@FXML public Button btnThem;
	@FXML public Button btnLamMoi;
	
	public void initialize() {
		setItemComboBox();
		
		Image imageThem = new Image(getClass().getResourceAsStream("/picture/khieuNaiVaHoTroKH/plus.png"));
        imgThem.setImage(imageThem);

		Image imageLamMoi = new Image(getClass().getResourceAsStream("/picture/khieuNaiVaHoTroKH/refresh.png"));
        imgLamMoi.setImage(imageLamMoi);
        
	}
	
	public void setItemComboBox() {
		cmbLoaiDon.getItems().addAll(
		        "Đơn khiếu nại",
		        "Đơn hỗ trợ"
		    );
	}
}
