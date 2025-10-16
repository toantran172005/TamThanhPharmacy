package controller;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ToolCtrl {
	// Hiển thị thông báo lưu thành công
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

	// Thông báo lưu thất bại
	public void hienThiThongBaoThatBai(String ghiChu, String tenLoi, String maLoi) {
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

	// hàm đổi từ localdate sang date
	public static java.sql.Date localDateSangSqlDate(LocalDate localDate) {
		return (localDate != null) ? java.sql.Date.valueOf(localDate) : null;
	}

	// hàm đổi từ date thành localdate
	public static LocalDate sqlDateSangLocalDate(java.sql.Date sqlDate) {
		return (sqlDate != null) ? sqlDate.toLocalDate() : null;
	}

	// hàm đổi double sang tiền tệ VND
	public static String dinhDangVND(double amount) {
		Locale localeVN = new Locale("vi", "VN");
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
		return currencyFormatter.format(amount);
	}

	// hàm chuyển số điện thoại từ +84- sang 0 và ngược lại
	public String chuyenSoDienThoai(String sdt) {
		if (sdt == null || sdt.isEmpty())
			return sdt;

		sdt = sdt.trim();

		if (sdt.startsWith("+84-")) {
			return "0" + sdt.substring(4);
		} else if (sdt.startsWith("0")) {
			return "+84-" + sdt.substring(1);
		}

		return sdt;
	}
	
	// ========== CHUYỂN CHUỖI TIỀN VỀ SỐ ==========
	public double chuyenTienSangSo(String text) {
	    if (text == null || text.isEmpty()) return 0;
	    text = text.replaceAll("[^\\d.]", ""); // bỏ ký tự thừa
	    try {
	        return Double.parseDouble(text);
	    } catch (NumberFormatException e) {
	        return 0;
	    }
	}
}
