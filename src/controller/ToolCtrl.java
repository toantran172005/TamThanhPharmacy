package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import database.KetNoiDatabase;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ToolCtrl {

	// định dạng date picker
	public void dinhDangDatePicker(DatePicker datePicker) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	    datePicker.setConverter(new StringConverter<LocalDate>() {
	        @Override
	        public String toString(LocalDate date) {
	            return (date != null) ? formatter.format(date) : "";
	        }

	        @Override
	        public LocalDate fromString(String string) {
	            if (string != null && !string.isEmpty()) {
	                return LocalDate.parse(string, formatter);
	            } else {
	                return null;
	            }
	        }
	    });
	}

	// Hiển thị thông báo
	public void hienThiThongBao(String tieuDe, String noiDung, Boolean trangThai) {
		if (trangThai) {
			try {
				Alert thongBao = new Alert(Alert.AlertType.INFORMATION);
				thongBao.setTitle("Thông báo");
				thongBao.setHeaderText(tieuDe);
				thongBao.setContentText(noiDung);

				Stage stageAlert = (Stage) thongBao.getDialogPane().getScene().getWindow();
				stageAlert.getIcons().add(new Image("/picture/trangChu/logo.jpg"));
				ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/picture/thuoc/check.png")));

				icon.setFitHeight(50);
				icon.setFitWidth(50);
				thongBao.setGraphic(icon);

				thongBao.showAndWait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				Alert thongBao = new Alert(AlertType.INFORMATION);
				thongBao.setTitle("Thông báo lỗi");
				thongBao.setHeaderText(tieuDe);
				thongBao.setContentText(noiDung);

				Stage stageAlert = (Stage) thongBao.getDialogPane().getScene().getWindow();
				stageAlert.getIcons().add(new Image("/picture/trangChu/logo.jpg"));
				ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/picture/thuoc/cross.png")));

				icon.setFitHeight(50);
				icon.setFitWidth(50);
				thongBao.setGraphic(icon);

				thongBao.showAndWait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// Hiển thị thông báo xác nhận
	public boolean hienThiXacNhan(String tieuDe, String noiDung) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(tieuDe);
		alert.setHeaderText(noiDung);
		alert.setContentText("Nhấn OK để xác nhận hoặc Cancel để hủy.");

		try {
			Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
			stageAlert.getIcons().add(new Image("/picture/trangChu/logo.jpg"));
		} catch (Exception e) {
			System.out.println("⚠ Không tìm thấy logo cửa sổ.");
		}

		Optional<ButtonType> ketQua = alert.showAndWait();
		return ketQua.isPresent() && ketQua.get() == ButtonType.OK;
	}

	public String dinhDangLocalDate(LocalDate date) {
		if (date == null)
			return "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return date.format(formatter);
	}

	// hàm đổi từ localdate sang date
	public java.sql.Date localDateSangSqlDate(LocalDate localDate) {
		return (localDate != null) ? java.sql.Date.valueOf(localDate) : null;
	}

	// hàm đổi từ date thành localdate
	public LocalDate sqlDateSangLocalDate(java.sql.Date sqlDate) {
		return (sqlDate != null) ? sqlDate.toLocalDate() : null;
	}

	// hàm đổi double sang tiền tệ VND
	public String dinhDangVND(double amount) {
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

	public String taoKhoaChinh(String tenBangVietTat) {
		// Bảng ánh xạ viết tắt -> tên bảng
		Map<String, String> mapBang = new HashMap<>();
		mapBang.put("PNT", "PhieuNhapThuoc");
		mapBang.put("K", "Kho");
		mapBang.put("TK", "TaiKhoan");
		mapBang.put("PKN", "Phieu_KhieuNai_HoTroKH");
		mapBang.put("T", "Thue");
		mapBang.put("KH", "KhachHang");
		mapBang.put("NV", "NhanVien");
		mapBang.put("KT", "KeThuoc");
		mapBang.put("NCC", "NhaCungCap");
		mapBang.put("TH", "Thuoc");
		mapBang.put("HD", "HoaDon");
		mapBang.put("KM", "KhuyenMai");
		mapBang.put("PDT", "PhieuDoiTra");
		mapBang.put("PDTH", "PhieuDatThuoc");

		if (!mapBang.containsKey(tenBangVietTat)) {
			throw new IllegalArgumentException("Không tìm thấy bảng tương ứng với mã: " + tenBangVietTat);
		}

		String tenBang = mapBang.get(tenBangVietTat);

		// Xử lý đặc biệt cho một số bảng
		String cotKhoa;
		switch (tenBangVietTat) {
		case "KT":
			cotKhoa = "maKe"; // KeThuoc
			break;
		case "K":
			cotKhoa = "maKho"; // Kho
			break;
		case "PDT":
			cotKhoa = "maPhieuDT"; // PhieuDoiTra
			break;
		case "PDTH":
			cotKhoa = "maPDT"; // PhieuDatThuoc
			break;
		case "PKN":
			cotKhoa = "maPhieu"; // Phieu_KhieuNai_HoTroKH
			break;
		case "T":
			cotKhoa = "maThue";
			break;
		default:
			// Mặc định: nếu tên bảng có nhiều từ viết hoa → ma + viết tắt, ngược lại ma +
			// tên bảng
			if (tenBang.contains("_") || tenBang.matches(".*[A-Z].*[A-Z].*")) {
				cotKhoa = "ma" + tenBangVietTat;
			} else {
				cotKhoa = "ma" + tenBang;
			}
			break;
		}

		String prefix = "TT" + tenBangVietTat; // Ví dụ: TTTH, TTKH...

		String sql = String.format(
				"SELECT TOP 1 %s FROM %s WHERE %s LIKE ? " + "ORDER BY TRY_CAST(REPLACE(%s, '%s', '') AS INT) DESC",
				cotKhoa, tenBang, cotKhoa, cotKhoa, prefix);

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, prefix + "%");
			ResultSet rs = ps.executeQuery();

			int so = 0;
			if (rs.next()) {
				String maMax = rs.getString(1); // VD: TTHD9
				String soStr = maMax.replace(prefix, ""); // -> "9"
				so = Integer.parseInt(soStr);
			}

			so++; // tăng lên 1
			String maMoi = prefix + so; // VD: TTHD10
			return maMoi;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// ========== CHUYỂN CHUỖI TIỀN VỀ SỐ ==========
	public double chuyenTienSangSo(String text) {
		if (text == null || text.isEmpty())
			return 0;
		text = text.replaceAll("[^\\d.]", ""); // bỏ ký tự thừa
		try {
			return Double.parseDouble(text);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

}
