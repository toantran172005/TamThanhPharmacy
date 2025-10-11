package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ThongBaoCtrl {
	//Hiển thị thông báo lưu thành công
		public void hienThiThongBaoThanhCong(String text) {
			Alert thongBao = new Alert(Alert.AlertType.INFORMATION);
			thongBao.setTitle("Thông báo");
			thongBao.setHeaderText(null);
			thongBao.setContentText(text);
			
			Stage stageAlert = (Stage) thongBao.getDialogPane().getScene().getWindow();
			stageAlert.getIcons().add(new Image("/picture/trangChu/logo.jpg"));
			ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/picture/thuoc/check.png")));
			
			icon.setFitHeight(50);
		    icon.setFitWidth(50);
		    thongBao.setGraphic(icon);

		    thongBao.showAndWait();
		}
		
		//Thông báo lưu thất bại
		public void hienThiThongBaoThatBai( String ghiChu, String tenLoi, String maLoi) {
			Alert thongBao = new Alert(AlertType.INFORMATION);
			thongBao.setTitle("Thông báo lỗi");
			thongBao.setHeaderText(ghiChu);
			thongBao.setContentText("Lỗi: " + tenLoi + "\nMã lỗi: " + maLoi);
			
			Stage stageAlert = (Stage) thongBao.getDialogPane().getScene().getWindow();
			stageAlert.getIcons().add(new Image("/picture/trangChu/logo.jpg"));
			ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/picture/thuoc/cross.png")));
		
			icon.setFitHeight(50);
		    icon.setFitWidth(50);
		    thongBao.setGraphic(icon);

		    thongBao.showAndWait();
		}
}
