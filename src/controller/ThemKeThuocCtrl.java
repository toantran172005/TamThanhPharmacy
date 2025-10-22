package controller;

import java.util.ArrayList;
import javafx.application.Platform;

import dao.KeThuocDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ThemKeThuocCtrl {

	@FXML
	public ComboBox<String> cmbLoaiKe;
	@FXML
	public TextField txtSucChua, txtMoTa;
	@FXML
	public Button btnLamMoi, btnThem;

	public ToolCtrl tool = new ToolCtrl();
	public KeThuocDAO ktDAO = new KeThuocDAO();

	public void initialize() {
		setDataChoComboBox();
		lamMoi();
		setHoatDong();
	}

	public void setHoatDong() {
		cmbLoaiKe.setOnAction(event -> kiemTraCmb());
		cmbLoaiKe.getEditor().setOnKeyPressed(event -> {
			Platform.runLater(() -> kiemTraCmb());
		});
		txtSucChua.setOnAction(event -> kiemTraSucChua());
		txtMoTa.setOnAction(event -> kiemTraTatCa());
		btnLamMoi.setOnAction(event -> lamMoi());
		btnThem.setOnAction(event -> kiemTraTatCa());
	}

	public void lamMoi() {
		cmbLoaiKe.getEditor().clear();
		cmbLoaiKe.requestFocus();
		txtSucChua.clear();
		txtMoTa.clear();
	}

	public void kiemTraTatCa() {
		boolean hopLeCmb = kiemTraCmb();
		boolean hopLeSucChua = false;

		if (hopLeCmb) {
			hopLeSucChua = kiemTraSucChua();
		}

		if (hopLeCmb && hopLeSucChua) {
			themKeThuoc();
		}
	}

	public void themKeThuoc() {
		if (tool.hienThiXacNhan("Thêm kệ thuốc", "Xác nhận thêm mới kệ thuốc: " + cmbLoaiKe.getValue() + "?")) {
			if (ktDAO.themKeThuoc(tool.taoKhoaChinh("KT"), cmbLoaiKe.getValue(), txtSucChua.getText(),
					txtMoTa.getText())) {
				tool.hienThiThongBao("Thêm kệ thuốc", "Thêm mới kệ thuốc thành công!", true);
				lamMoi();
			} else {
				tool.hienThiThongBao("Thêm kệ thuốc", "Thêm mới kệ thuốc thất bại!", false);
			}

		}

	}

	public boolean kiemTraCmb() {
		String text = cmbLoaiKe.getEditor().getText();

		if (text == null || text.trim().isEmpty()) {
			tool.hienThiThongBao("Lỗi ComboBox", "Vui lòng chọn hoặc nhập loại kệ!", false);
			Platform.runLater(() -> cmbLoaiKe.getEditor().requestFocus());
			return false;
		}

		cmbLoaiKe.setValue(text.trim());
		Platform.runLater(() -> txtSucChua.requestFocus());
		return true;
	}

	public boolean kiemTraSucChua() {
		try {
			int sucChua = Integer.parseInt(txtSucChua.getText().trim());

			if (sucChua > 900) {
				tool.hienThiThongBao("Lỗi sức chứa", "Sức chứa phải dưới 900!", false);
				txtSucChua.selectAll();
				return false;
			}

			Platform.runLater(() -> txtMoTa.requestFocus());
			return true;

		} catch (NumberFormatException e) {
			tool.hienThiThongBao("Lỗi sức chứa", "Vui lòng nhập số cho sức chứa!", false);
			txtSucChua.selectAll();
			return false;
		}
	}

	public void setDataChoComboBox() {
		ArrayList<String> listKT = ktDAO.layTatCaKeThuoc();
		for (String kt : listKT) {
			cmbLoaiKe.getItems().add(kt);
		}
	}

}
