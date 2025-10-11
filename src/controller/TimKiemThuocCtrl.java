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
	
	private TrangChuNVCtrl trangChuNVCtrl;  // tham chiếu controller cha
    private TrangChuQLCtrl trangChuQLCtrl; 
	
	public void initialize() {
		setItemComboBoxTrangThai();
		setItemComboBoxLoaiThuoc();
		
		Image imageXemChiTiet = new Image(getClass().getResourceAsStream("/picture/thuoc/find.png"));
        imgXemChiTiet.setImage(imageXemChiTiet);

		Image imageLamMoi = new Image(getClass().getResourceAsStream("/picture/thuoc/refresh.png"));
        imgLamMoi.setImage(imageLamMoi);
        
        btnXemChiTiet.setOnMouseClicked(e -> moTrangChiTietThuoc());
        
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
	
	public void setTrangChuNVCtrl(TrangChuNVCtrl ctrl) {
		this.trangChuNVCtrl = ctrl;
	}
		    
	public void setTrangChuQLCtrl(TrangChuQLCtrl ctrl) {
		this.trangChuQLCtrl = ctrl;
	}
		
	// ========== MỞ TRANG CHI TIẾT THUỐC ==========
	private void moTrangChiTietThuoc() {
		if (trangChuNVCtrl != null) {
			trangChuNVCtrl.moTrang("/fxml/ChiTietThuoc.fxml", ChiTietThuocCtrl.class);
	    } 
	    else if (trangChuQLCtrl != null) {
	    	trangChuQLCtrl.moTrang("/fxml/ChiTietThuoc.fxml", ChiTietThuocCtrl.class);
	    } 
	    else {
	        System.out.println("⚠ Không có tham chiếu TrangChu");
	    }
	}
}
