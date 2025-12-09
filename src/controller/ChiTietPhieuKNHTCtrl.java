package controller;

import dao.PhieuKNHTDAO;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuKhieuNaiHoTro;
import gui.ChiTietPhieuKNHT_GUI;
import gui.DanhSachKhieuNaiVaHoTroHK_GUI;

public class ChiTietPhieuKNHTCtrl {
	public ChiTietPhieuKNHT_GUI ctknhtGUI;
	public ToolCtrl tool;
	public PhieuKNHTDAO knhtDAO;

	public ChiTietPhieuKNHTCtrl(ChiTietPhieuKNHT_GUI ctknhtGUI) {
		super();
		this.ctknhtGUI = ctknhtGUI;
		tool = new ToolCtrl();
		knhtDAO = new PhieuKNHTDAO();
	}

	public void capNhatPhieuKNHT() {
		if (ctknhtGUI.btnCapNhat.getText().equals("Cập nhật")) {
			ctknhtGUI.btnCapNhat.setText("Hoàn tất");
			choPhepEdit(true);
		} else {
			if (tool.hienThiXacNhan("Cập nhật khách hàng", "Xác nhận cập nhật khách hàng?", null)) {
				PhieuKhieuNaiHoTro knht = kiemTraThongTin();
				if (knht == null) {
					return;
				}

				if (knhtDAO.capNhatPhieu(knht)) {
					tool.hienThiThongBao("Cập nhập", "Cập nhật phiếu " + knht.getLoaiDon() + " thành công!", true);
					ctknhtGUI.btnCapNhat.setText("Cập nhật");
					choPhepEdit(false);
				} else {
					tool.hienThiThongBao("Cập nhật", "Không thể cập nhật phiếu " + knht.getLoaiDon(), false);
				}
			}
		}
	}

	public PhieuKhieuNaiHoTro kiemTraThongTin() {
		if (ktTenKhachHangHopLe() && ktTenNhanVienHopLe() && ktSoDienThoaiHopLe() && ktNoiDungHopLe()) {

			String tenKH = ctknhtGUI.txtTenKH.getText().trim();
			String tenNV = ctknhtGUI.txtTenNV.getText().trim();
			String sdt = tool.chuyenSoDienThoai(ctknhtGUI.txtSdt.getText().trim());
			String noiDung = ctknhtGUI.txaNoiDung.getText().trim();
			String loaiDon = ctknhtGUI.cmbLoaiDon.getSelectedItem().toString();
			String trangThai = ctknhtGUI.cmbTrangThai.getSelectedItem().toString();

			KhachHang kh = new KhachHang(ctknhtGUI.phieu.getKhachHang().getMaKH(), tenKH, sdt);
			NhanVien nv = new NhanVien(ctknhtGUI.phieu.getNhanVien().getMaNV(), tenNV);

			return new PhieuKhieuNaiHoTro(ctknhtGUI.phieu.getMaPhieu(), nv, kh, ctknhtGUI.phieu.getNgayLap(), noiDung,
					loaiDon, trangThai);
		}

		return null;
	}

	public boolean ktTenKhachHangHopLe() {
		String ten = ctknhtGUI.txtTenKH.getText().trim();
		String regex = "^[\\p{L}\\s]+$";

		if (ten.isEmpty()) {
			tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được để trống", false);
			ctknhtGUI.txtTenKH.requestFocus();
			return false;
		} else if (!ten.matches(regex)) {
			tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được chứa số hoặc ký tự đặc biệt", false);
			ctknhtGUI.txtTenKH.requestFocus();
			ctknhtGUI.txtTenKH.selectAll();
			return false;
		}
		return true;
	}

	public boolean ktTenNhanVienHopLe() {
		String ten = ctknhtGUI.txtTenNV.getText().trim();
		String regex = "^[\\p{L}\\s]+$";

		if (ten.isEmpty()) {
			tool.hienThiThongBao("Tên nhân viên không hợp lệ!", "Tên không được để trống", false);
			ctknhtGUI.txtTenNV.requestFocus();
			return false;
		} else if (!ten.matches(regex)) {
			tool.hienThiThongBao("Tên nhân viên không hợp lệ!", "Tên không được chứa số hoặc ký tự đặc biệt", false);
			ctknhtGUI.txtTenNV.requestFocus();
			ctknhtGUI.txtTenNV.selectAll();
			return false;
		}
		return true;
	}

	public boolean ktNoiDungHopLe() {
		String nd = ctknhtGUI.txaNoiDung.getText().trim();
		if (nd.isEmpty()) {
			tool.hienThiThongBao("Nội dung không hợp lệ!", "Nội dung không được để trống", false);
			ctknhtGUI.txaNoiDung.requestFocus();
			return false;
		}
		return true;
	}

	public boolean ktSoDienThoaiHopLe() {
		String sdt = ctknhtGUI.txtSdt.getText().trim();
		String regex = "^0\\d{9}$";

		if (sdt.isEmpty()) {
			tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Không được để trống", false);
			ctknhtGUI.txtSdt.requestFocus();
			return false;
		} else if (!sdt.matches(regex)) {
			tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Phải gồm 10 chữ số và bắt đầu bằng 0", false);
			ctknhtGUI.txtSdt.requestFocus();
			ctknhtGUI.txtSdt.selectAll();
			return false;
		}
		return true;
	}

	public void choPhepEdit(boolean edit) {
		ctknhtGUI.txtTenKH.setEditable(edit);
		ctknhtGUI.txtTenNV.setEditable(edit);
		ctknhtGUI.txtSdt.setEditable(edit);
		ctknhtGUI.txaNoiDung.setEditable(edit);
		ctknhtGUI.cmbLoaiDon.setEnabled(edit);
		ctknhtGUI.cmbTrangThai.setEnabled(edit);
	}

	public void quayLaiDanhSachKNHT() {
		DanhSachKhieuNaiVaHoTroHK_GUI dsknhtGUI = new DanhSachKhieuNaiVaHoTroHK_GUI();
		tool.doiPanel(ctknhtGUI, dsknhtGUI);
	}

}
