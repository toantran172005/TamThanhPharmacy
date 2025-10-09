package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TimKiemThuocCtrl {
	@FXML public ComboBox<String> cmbLoaiThuoc;
	@FXML public ComboBox<String> cmbTrangThaiKD;
	@FXML public ImageView imgXemChiTiet;
	@FXML public ImageView imgLamMoi;
	@FXML public Button btnXemChiTiet;
	@FXML public Button btnLamMoi;
	
	public void initialize() {
		setItemComboBoxTrangThai();
		setItemComboBoxLoaiThuoc();
		
		Image imageXemChiTiet = new Image(getClass().getResourceAsStream("/picture/thuoc/find.png"));
        imgXemChiTiet.setImage(imageXemChiTiet);

		Image imageLamMoi = new Image(getClass().getResourceAsStream("/picture/thuoc/refresh.png"));
        imgLamMoi.setImage(imageLamMoi);
        
	}
	
	public void setItemComboBoxTrangThai() {
		cmbTrangThaiKD.getItems().addAll(
		        "Đang kinh doanh",
		        "Ngừng kinh doanh"
		    );
	}
	
	public void setItemComboBoxLoaiThuoc() {
		cmbLoaiThuoc.getItems().addAll(
		        "Đang kinh doanh",
		        "Ngừng kinh doanh"
		    );
	}
}
