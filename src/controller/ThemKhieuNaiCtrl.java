package controller;

import java.time.ZoneId;

import dao.KhachHangDAO;
import dao.NhanVienDAO;
import dao.PhieuKNHTDAO;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuKhieuNaiHoTro;
import gui.DanhSachKhieuNaiVaHoTroHK_GUI;
import gui.ThemKhieuNai_GUI;

public class ThemKhieuNaiCtrl {
	public ThemKhieuNai_GUI tknGUI;
	public ToolCtrl tool;
	public PhieuKNHTDAO knhtDAO;
	public KhachHangDAO khDAO;
	public NhanVienDAO nvDAO;

	public ThemKhieuNaiCtrl(ThemKhieuNai_GUI tknGUI) {
		super();
		this.tknGUI = tknGUI;
		tool = new ToolCtrl();
		knhtDAO = new PhieuKNHTDAO();
		khDAO = new KhachHangDAO();
		nvDAO = new NhanVienDAO();
	}

	public void themPhieu() {
		if (tool.hienThiXacNhan("Thêm phiếu",
				"Xác nhận thêm phiếu " + tknGUI.cmbLoaiDon.getSelectedItem().toString() + "?", null)) {
			PhieuKhieuNaiHoTro knht = kiemTraThongTin();
			if (knhtDAO.themPhieu(knht)) {
				tool.hienThiThongBao("Thêm phiếu",
						"Thêm phiếu " + tknGUI.cmbLoaiDon.getSelectedItem().toString() + " thành công!", true);
				lamMoi();
			} else {
				tool.hienThiThongBao("Thêm phiếu", "Không thể thêm phiếu!", false);
			}
		}
	}

	public PhieuKhieuNaiHoTro kiemTraThongTin() {
		if (ktTenKhachHangHopLe() && ktNoiDungHopLe() && ktSoDienThoaiHopLe()) {

			String tenKH = tknGUI.txtTenKhachHang.getText().trim();
			String sdt = tool.chuyenSoDienThoai(tknGUI.txtSdt.getText().trim());
			String noiDung = tknGUI.txaNoiDung.getText().trim();
			String loaiDon = tknGUI.cmbLoaiDon.getSelectedItem().toString();

			KhachHang kh = khDAO.timMaKhachHangTheoSDT(sdt);
			NhanVien nv = nvDAO.timNhanVienTheoMa("TTNV1");

			if (kh == null) {
				khDAO.themKhachHang(tool.taoKhoaChinh("KH"), tenKH, String.valueOf(sdt), String.valueOf(30));
			}
			kh = khDAO.timMaKhachHangTheoSDT(sdt);

			return new PhieuKhieuNaiHoTro(tool.taoKhoaChinh("PKN"), nv, kh,
					tknGUI.dateNgayLap.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), noiDung,
					loaiDon, "Chờ xử lý");
		}

		return null;
	}

	public boolean ktTenKhachHangHopLe() {
		String ten = tknGUI.txtTenKhachHang.getText().trim();
		String regex = "^[\\p{L}\\s]+$";

		if (ten.isEmpty()) {
			tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được để trống", false);
			tknGUI.txtTenKhachHang.requestFocus();
			return false;
		} else if (!ten.matches(regex)) {
			tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được chứa số hoặc ký tự đặc biệt", false);
			tknGUI.txtTenKhachHang.requestFocus();
			tknGUI.txtTenKhachHang.selectAll();
			return false;
		}
		return true;
	}

	public boolean ktNoiDungHopLe() {
		String nd = tknGUI.txaNoiDung.getText().trim();
		if (nd.isEmpty()) {
			tool.hienThiThongBao("Nội dung không hợp lệ!", "Nội dung không được để trống", false);
			tknGUI.txaNoiDung.requestFocus();
			return false;
		}
		return true;
	}

	public boolean ktSoDienThoaiHopLe() {
		String sdt = tknGUI.txtSdt.getText().trim();
		String regex = "^0\\d{9}$";

		if (sdt.isEmpty()) {
			tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Không được để trống", false);
			tknGUI.txtSdt.requestFocus();
			return false;
		} else if (!sdt.matches(regex)) {
			tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Phải gồm 10 chữ số và bắt đầu bằng 0", false);
			tknGUI.txtSdt.requestFocus();
			tknGUI.txtSdt.selectAll();
			return false;
		}
		return true;
	}

	public void quayLaiDanhSach() {
		DanhSachKhieuNaiVaHoTroHK_GUI dsGUI = new DanhSachKhieuNaiVaHoTroHK_GUI();
		tool.doiPanel(tknGUI, dsGUI);
	}

	public void lamMoi() {
		tknGUI.txtTenKhachHang.setText("");
		tknGUI.txtSdt.setText("");
		tknGUI.txaNoiDung.setText("");
		tknGUI.cmbLoaiDon.setSelectedItem("Khiếu nại");
		tknGUI.dateNgayLap.setDate(null);
	}

}
