package controller;

import entity.KhachHang;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ChiTietKhachHangCtrl {
	
	public ToolCtrl tool = new ToolCtrl();
	
	@FXML
	public TextField txtMaKH, txtTenKH, txtSdt, txtTuoi;
	
	@FXML
	public ComboBox<String> cmbTrangThai;
	
	@FXML
	public Button btnCapNhat, btnQuayLai;
	
	public TrangChuQLCtrl trangChuQLCtrl;
	public TrangChuNVCtrl trangChuNVCtrl;
	
	public void initialize() {
		setItemCmbTrangThai();
		choPhepCapNhat(false);
		setHoatDongChoBTN();
	}
	
	public void setHoatDongChoBTN() {
		btnQuayLai.setOnAction(event -> quayLaiTimKiem());
		btnCapNhat.setOnAction(event -> capNhatThongTin());
	}
	
	public void capNhatThongTin() {
		if(btnCapNhat.getText().trim().equals("Cập nhật")) {
			btnCapNhat.setText("Hoàn tất");
			choPhepCapNhat(true);
		} else { // cập nhật thông tin
			
			btnCapNhat.setText("Cập nhật");
			choPhepCapNhat(false);
		}
	}
	
	public void quayLaiTimKiem() {
		if(trangChuQLCtrl != null) {
			TimKiemKHCtrl tkKHCtrl = trangChuQLCtrl.doiCenterPane("/fxml/TimKiemKH.fxml");
			tkKHCtrl.setTrangChuQLCtrl(trangChuQLCtrl);
		} else {
			TimKiemKHCtrl tkKHCtrl = trangChuNVCtrl.doiCenterPane("/fxml/TimKiemKH.fxml");
			tkKHCtrl.setTrangChuNVCtrl(trangChuNVCtrl);
		}
	}
	
	public void choPhepCapNhat(boolean choPhep) {
		if(choPhep) {
			txtTenKH.setEditable(true);
			txtSdt.setEditable(true);
			txtTuoi.setEditable(true);
			cmbTrangThai.setEditable(true);
		} else {
			txtMaKH.setEditable(false);
			txtTenKH.setEditable(false);
			txtSdt.setEditable(false);
			txtTuoi.setEditable(false);
			cmbTrangThai.setEditable(false);
		}
	}

	public void hienThiThongTin(KhachHang kh) {
		txtMaKH.setText(kh.getMaKH());
		txtTenKH.setText(kh.getTenKH());
		txtSdt.setText(tool.chuyenSoDienThoai(kh.getSdt()));
		txtTuoi.setText(String.valueOf(kh.getTuoi()));
		if (kh.isTrangThai()) {
		    cmbTrangThai.setValue("Hoạt động");
		} else {
		    cmbTrangThai.setValue("Ngừng hoạt động");
		}
	}
	
	public void setItemCmbTrangThai() {
		cmbTrangThai.getItems().addAll("Hoạt động", "Ngừng hoạt động");
	}
	
	public void setTrangChuQLCtrl(TrangChuQLCtrl trangChuQLCtrl) {
		this.trangChuQLCtrl = trangChuQLCtrl;
	}

	public void setTrangChuNVCtrl(TrangChuNVCtrl trangChuNVCtrl) {
		this.trangChuNVCtrl = trangChuNVCtrl;
	}
	
}
