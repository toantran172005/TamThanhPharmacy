package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ChiTietKeThuocCtrl {

	@FXML public Button btnThoat;
	private TrangChuQLCtrl QL;
	
	public void setTrangChuQLCtrl(TrangChuQLCtrl trangChuQLCtrl) {
		this.QL = trangChuQLCtrl;
		
	}
	
	public void veTrangDanhSach() {
		QL.moTrangDanhSachKeThuoc();
	}
	
}
