package controller;

import entity.KhuyenMai;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ChiTietKhuyenMaiCtrl {
	
	@FXML
	public Button btnQuayLai;

	public TrangChuQLCtrl trangChuQLCtrl = new TrangChuQLCtrl();
	
	
	public void initialize() {
		ganSuKien();
	}
	
	public void ganSuKien() {
		btnQuayLai.setOnAction(event -> {
				quayLaiDanhSachKhuyenMai();
		});
	}
	
	public void quayLaiDanhSachKhuyenMai() {
			DanhSachKhuyenMaiCtrl dsKMCtrl = trangChuQLCtrl.doiCenterPane("/fxml/DanhSachKhuyenMai.fxml");
			dsKMCtrl.setTrangChuQLCtrl(trangChuQLCtrl);
	}
	
	public void hienThiThongTin(KhuyenMai km) {
		
	}

	public void setTrangChuQLCtrl(TrangChuQLCtrl trangChuQLCtrl) {
		
		this.trangChuQLCtrl = trangChuQLCtrl;
	}
}
