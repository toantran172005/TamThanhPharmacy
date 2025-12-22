package controller;

import dao.KeThuocDAO;
import gui.ThemKeThuoc_GUI;

public class ThemKeThuocCtrl {

	public ThemKeThuoc_GUI tktGUI;
	public ToolCtrl tool = new ToolCtrl();
	public KeThuocDAO ktDAO = new KeThuocDAO();

	public ThemKeThuocCtrl(ThemKeThuoc_GUI tktGUI) {
		super();
		this.tktGUI = tktGUI;
	}

	public void lamMoi() {
		tktGUI.txtLoaiKe.setText("");
		tktGUI.txtLoaiKe.requestFocus();
		tktGUI.txtSucChua.setText("");
		tktGUI.txtMoTa.setText("");
	}

	public void kiemTraTatCa() {

		String tenLoai = tktGUI.txtLoaiKe.getText().trim();
		if (tenLoai.isEmpty()) {
			tool.hienThiThongBao("Lỗi dữ liệu", "Loại kệ không được để trống!", false);
			tktGUI.txtLoaiKe.requestFocus();
			return;
		}

		if (!kiemTraSucChua())
			return;

		if (ktDAO.timTheoTen(tenLoai) != null) {
			tool.hienThiThongBao("Lỗi dữ liệu", "Loại kệ này đã tồn tại!", false);
			return;
		}

		themKeThuoc();
	}

	public boolean kiemTraSucChua() {
		String input = tktGUI.txtSucChua.getText().trim();
		if (input.isEmpty()) {
			tool.hienThiThongBao("Lỗi sức chứa", "Sức chứa không được để trống!", false);
			tktGUI.txtSucChua.requestFocus();
			return false;
		}

		try {
			int sucChua = Integer.parseInt(input);
			if (sucChua <= 0 || sucChua > 900) {
				tool.hienThiThongBao("Lỗi sức chứa", "Sức chứa phải là số dương và dưới 900!", false);
				tktGUI.txtSucChua.selectAll();
				tktGUI.txtSucChua.requestFocus();
				return false;
			}
			return true;
		} catch (NumberFormatException e) {
			tool.hienThiThongBao("Lỗi sức chứa", "Vui lòng nhập số nguyên hợp lệ!", false);
			tktGUI.txtSucChua.selectAll();
			tktGUI.txtSucChua.requestFocus();
			return false;
		}
	}

	public void themKeThuoc() {
		String loaiKe = tktGUI.txtLoaiKe.getText().trim();
		String sucChua = tktGUI.txtSucChua.getText().trim();
		String moTa = tktGUI.txtMoTa.getText().trim();

		if (tool.hienThiXacNhan("Thêm kệ thuốc", "Xác nhận thêm mới kệ thuốc: " + loaiKe + "?", null)) {
			String maMoi = tool.taoKhoaChinh("KT");

			if (ktDAO.themKeThuoc(maMoi, loaiKe, sucChua, moTa)) {
				tool.hienThiThongBao("Thêm kệ thuốc", "Thêm mới kệ thuốc thành công!", true);
				lamMoi();
			} else {
				tool.hienThiThongBao("Thêm kệ thuốc", "Thêm mới kệ thuốc thất bại do lỗi hệ thống!", false);
			}
		}
	}

}
