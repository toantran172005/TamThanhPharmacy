package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class ThuocCtrl {

	@FXML
	public ComboBox<String> cbLocThuoc;
	
	
	
	public void initialize() {
		
		cbLocThuoc.getItems().addAll(
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
