package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class DanhSachKeThuocCtrl {
	
	@FXML public ComboBox<String> cbbTrangThai;
	@FXML public ComboBox<String> cbbLoaiKe;
	@FXML public Button btnChiTiet;
	public TrangChuQLCtrl Ql;
	public TrangChuNVCtrl NV;
	
	@FXML public void initialize() {
		cbbTrangThai.getItems().addAll("Hoạt động","Ngừng hoạt động");
		cbbTrangThai.setValue("Hoạt động");
		cbbLoaiKe.getItems().addAll("Thuốc kê đơn", "Thuốc không kê đơn","Tim mạch","Thần kinh");
		cbbLoaiKe.setValue("Thuốc kê đơn");
		
	}

	public void setTrangChuQLCtrl(TrangChuQLCtrl trangChuQLCtrl) {
		this.Ql = trangChuQLCtrl;
	}
	
	public void setTrangChuNVCtrl(TrangChuNVCtrl trangChuNVCtrl) {
		this.NV = trangChuNVCtrl;
		
	}
	
	@FXML public void moTrangChiTiet() {
		if(Ql != null) {
			Ql.setTrangChiTietKeThuoc();
		} else if(NV != null) {
			NV.setTrangChiTietKeThuoc();
		}
	}

}
