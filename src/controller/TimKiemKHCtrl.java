package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class TimKiemKHCtrl {

	@FXML
	public ComboBox<String> cmbTrangThai;
	
	public void initialize() {
		setItemCmbTrangThai();
	}
	
	public void setItemCmbTrangThai() {
		cmbTrangThai.getItems().addAll(
		        "Hoạt động",
		        "Ngừng hoạt động"
		    );
	}
	
}
