package controller;

import dao.KhachHangDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ThemKhachHangCtrl {

	@FXML
	public TextField txtTenKH, txtSdt, txtTuoi;

	@FXML
	public HBox hbLamMoi, hbThem;

	public ToolCtrl tool = new ToolCtrl();
	public KhachHangDAO khDAO = new KhachHangDAO();

	public void initialize() {
		lamMoi();
		setUpHBoxVaTxt();
	}

	public void setUpHBoxVaTxt() {
		txtTenKH.setOnAction(event -> ktTenKhachHangHopLe());
		txtSdt.setOnAction(event -> ktSoDienThoaiHopLe());
		txtTuoi.setOnAction(event -> ktTatCaTruocKhiThem());
		hbThem.setOnMouseClicked(event -> ktTatCaTruocKhiThem());
		hbLamMoi.setOnMouseClicked(event -> lamMoi());
	}

	public void lamMoi() {
		txtTenKH.setText("");
		txtSdt.setText("");
		txtTuoi.setText("");
		txtTenKH.requestFocus();
	}

	public void ktTatCaTruocKhiThem() {
		// Nếu các kiểm tra đều đúng -> gọi thêm khách hàng
		if (ktTenKhachHangHopLe() && ktSoDienThoaiHopLe() && ktTuoiHopLe()) {
			themKhachHang();
		}
	}

	public void themKhachHang() {
		if (tool.hienThiXacNhan("Thêm khách hàng", "Xác nhận thêm khách hàng?")) {
			if (khDAO.themKhachHang(tool.taoKhoaChinh("KH"), txtTenKH.getText().trim(),
					tool.chuyenSoDienThoai(txtSdt.getText().trim()), txtTuoi.getText().trim())) {
				lamMoi();
			}
		}

	}

	public boolean ktTenKhachHangHopLe() {
		String ten = txtTenKH.getText().trim();
		String regex = "^[\\p{L}\\s]+$";

		if (ten.isEmpty()) {
			tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được để trống", false);
			txtTenKH.requestFocus();
			return false;
		} else if (!ten.matches(regex)) {
			tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được chứa số hoặc ký tự đặc biệt", false);
			txtTenKH.requestFocus();
			txtTenKH.selectAll();
			return false;
		}
		txtSdt.requestFocus();
		return true;
	}

	public boolean ktSoDienThoaiHopLe() {
		String sdt = txtSdt.getText().trim();
		String regex = "^0\\d{9}$";

		if (sdt.isEmpty()) {
			tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Không được để trống", false);
			txtSdt.requestFocus();
			return false;
		} else if (!sdt.matches(regex)) {
			tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Phải gồm 10 chữ số và bắt đầu bằng 0", false);
			txtSdt.requestFocus();
			txtSdt.selectAll();
			return false;
		}
		txtTuoi.requestFocus();
		return true;
	}

	public boolean ktTuoiHopLe() {
		String tuoiStr = txtTuoi.getText().trim();

		try {
			int tuoi = Integer.parseInt(tuoiStr);

			if (tuoi < 0) {
				tool.hienThiThongBao("Tuổi không hợp lệ!", "Tuổi không được là số âm.", false);
				txtTuoi.requestFocus();
				txtTuoi.selectAll();
				return false;
			}
		} catch (NumberFormatException e) {
			tool.hienThiThongBao("Tuổi không hợp lệ!", "Tuổi phải là số nguyên.", false);
			txtTuoi.requestFocus();
			txtTuoi.selectAll();
			return false;
		}
		return true;
	}

}
