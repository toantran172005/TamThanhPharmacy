package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class ThongKeKhachHangCtrl {
	@FXML public ComboBox<String> cmbThoiGianTK;
	@FXML public ComboBox<String> cmbLoaiTK;
	@FXML public ComboBox<String> cmbTopTK;
	@FXML public Button btnLamMoi;
	
	
	public void initialize() {
		setItemComboBoxThoiGianTK(); 
		setItemComboBoxLoaiTK();   
		setItemComboBoxTopTK();   
	}
	
	public void setItemComboBoxThoiGianTK() {
		cmbThoiGianTK.getItems().addAll(
		        "Ngày",
		        "Tháng",
		        "Năm"
		    );
	}
	
	public void setItemComboBoxLoaiTK() {
		cmbLoaiTK.getItems().addAll(
		        "Mua nhiều lần nhất",
		        "Tổng tiền mua nhiều nhất"
		    );
	}
	
	public void setItemComboBoxTopTK() {
		cmbTopTK.getItems().addAll(
		        "10",
		        "20",
		        "30", 
		        "50", 
		        "100"
		    );
	}
	
}
