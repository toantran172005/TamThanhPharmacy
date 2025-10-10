package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ChiTietHoaDonCtrl {
	@FXML public Button btnQuayLai;
	
	private TrangChuNVCtrl trangChuNVCtrl;  // tham chiếu controller cha
	private TrangChuQLCtrl trangChuQLCtrl; 
	
	public void initialize() {
		btnQuayLai.setOnMouseClicked(e -> quayLai());
    	}
	
	public void setTrangChuNVCtrl(TrangChuNVCtrl ctrl) {
		this.trangChuNVCtrl = ctrl;
	}
		    
	public void setTrangChuQLCtrl(TrangChuQLCtrl ctrl) {
		this.trangChuQLCtrl = ctrl;
	}
	
	// ========== QUAY LẠI TRANG TÌM KIẾM THUỐC ==========
	private void quayLai() {
		if (trangChuNVCtrl != null) {
			trangChuNVCtrl.moTrang("/fxml/TimKiemHD.fxml", TimKiemHDCtrl.class);
		} 
		else if (trangChuQLCtrl != null) {
			trangChuQLCtrl.moTrang("/fxml/TimKiemHD.fxml", TimKiemHDCtrl.class);
		} 
		else {
			System.out.println("⚠ Không có tham chiếu TrangChu");
		}
	}
}
