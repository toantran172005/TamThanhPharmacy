package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TimKiemHDCtrl {
	@FXML public Button btnChiTiet;
	
	private TrangChuNVCtrl trangChuNVCtrl;  // tham chiếu controller cha
    private TrangChuQLCtrl trangChuQLCtrl; 
	
	public void initialize() {
        btnChiTiet.setOnMouseClicked(e -> moTrangChiTiet());
	}
	
	public void setTrangChuNVCtrl(TrangChuNVCtrl ctrl) {
	   this.trangChuNVCtrl = ctrl;
	}
	    
	public void setTrangChuQLCtrl(TrangChuQLCtrl ctrl) {
	    this.trangChuQLCtrl = ctrl;
	}
	
	// ========== MỞ TRANG CHI TIẾT HOÁ ĐƠN==========
    private void moTrangChiTiet() {
    	if (trangChuNVCtrl != null) {
            trangChuNVCtrl.moTrang("/fxml/ChiTietHoaDon.fxml", ChiTietHoaDonCtrl.class);
        } 
        else if (trangChuQLCtrl != null) {
        	trangChuQLCtrl.moTrang("/fxml/ChiTietHoaDon.fxml", ChiTietHoaDonCtrl.class);
        } 
        else {
            System.out.println("⚠ Không có tham chiếu TrangChu");
        }
    }
}
