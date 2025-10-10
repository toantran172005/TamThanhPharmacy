package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TimKiemNhanVienCtrl {
	@FXML public ComboBox<String> cmbTrangThai;
	@FXML public Button btnXemChiTiet;
	
	private TrangChuNVCtrl trangChuNVCtrl;  // tham chiếu controller cha
    private TrangChuQLCtrl trangChuQLCtrl; 
	
	public void initialize() {
		setItemComboBoxTrangThai();    
        btnXemChiTiet.setOnMouseClicked(e -> moTrangChiTietThuoc());
	}
	
	public void setItemComboBoxTrangThai() {
		cmbTrangThai.getItems().addAll(
		        "Còn làm",
		        "Đã nghỉ"
		    );
	}
	
	public void setTrangChuNVCtrl(TrangChuNVCtrl ctrl) {
		this.trangChuNVCtrl = ctrl;
	}
		    
	public void setTrangChuQLCtrl(TrangChuQLCtrl ctrl) {
		this.trangChuQLCtrl = ctrl;
	}
		
	// ========== MỞ TRANG CHI TIẾT NHÂN VIÊN ==========
	private void moTrangChiTietThuoc() {
		if (trangChuNVCtrl != null) {
			trangChuNVCtrl.moTrang("/fxml/ChiTietNhanVien.fxml", ChiTietNhanVienCtrl.class);
	    } 
	    else if (trangChuQLCtrl != null) {
	    	trangChuQLCtrl.moTrang("/fxml/ChiTietNhanVien.fxml", ChiTietNhanVienCtrl.class);
	    } 
	    else {
	        System.out.println("⚠ Không có tham chiếu TrangChu");
	    }
	}
}
