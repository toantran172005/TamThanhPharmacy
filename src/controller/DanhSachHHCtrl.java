package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class DanhSachHHCtrl {

	@FXML
	public ComboBox<String> cbbLocHH;
	
	public void initialize() {
		setItemComboBox();
	}
	
	public void setItemComboBox() {
		cbbLocHH.getItems().addAll(
		        "Thuốc kê đơn",
		        "Thuốc không kê đơn",
		        "Thực phẩm chức năng",
		        "Dược mỹ phẩm",
		        "Thần kinh",
		        "Tim mạch",
		        "Kháng sinh",
		        "Tiểu đường"
		    );
	}
}
