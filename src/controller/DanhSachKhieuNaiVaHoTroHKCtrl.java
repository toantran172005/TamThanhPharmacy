package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DanhSachKhieuNaiVaHoTroHKCtrl {
	
	@FXML public ComboBox<String> cmbTrangThai;
	@FXML public ImageView imgThem;
	@FXML public Button btnThem;
	
	private TrangChuNVCtrl trangChuNVCtrl;  // tham chiếu controller cha
    private TrangChuQLCtrl trangChuQLCtrl; 
	
	public void initialize() {
		setItemComboBox();
		
		Image image = new Image(getClass().getResourceAsStream("/picture/khieuNaiVaHoTroKH/plus.png"));
        imgThem.setImage(image);
        
        btnThem.setOnMouseClicked(e -> moTrangThem());
	}
	
	public void setItemComboBox() {
		cmbTrangThai.getItems().addAll(
		        "Đang xử lý",
		        "Hoàn thành"
		    );
	}
	
	public void setTrangChuNVCtrl(TrangChuNVCtrl ctrl) {
	   this.trangChuNVCtrl = ctrl;
	}
	    
	public void setTrangChuQLCtrl(TrangChuQLCtrl ctrl) {
	    this.trangChuQLCtrl = ctrl;
	}
	
	// ========== MỞ TRANG THÊM KHIẾU NẠI HỖ TRỢ ==========
    private void moTrangThem() {
    	if (trangChuNVCtrl != null) {
            trangChuNVCtrl.doiCenterPane("/fxml/ThemKhieuNai.fxml");
        } 
        else if (trangChuQLCtrl != null) {
            trangChuQLCtrl.doiCenterPane("/fxml/ThemKhieuNai.fxml");
        } 
        else {
            System.out.println("⚠ Không có tham chiếu TrangChu");
        }
    }
}
