package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ChiTietKeThuocCtrl {

	@FXML public Button btnThoat;
	private TrangChuQLCtrl QL;
	private TrangChuNVCtrl NV;
	
	public void setTrangChuQLCtrl(TrangChuQLCtrl trangChuQLCtrl) {
		this.QL = trangChuQLCtrl;
		
	}

	public void setTrangChuNVCtrl(TrangChuNVCtrl trangChuNVCtrl) {
		this.NV = trangChuNVCtrl;
		
	}
	
	public void veTrangDanhSach() {
		if(QL != null) {
			QL.moTrangDanhSachKeThuoc();
		} else if(NV != null) {
			NV.moTrangDanhSachKeThuoc();
		}
	}
	
}
