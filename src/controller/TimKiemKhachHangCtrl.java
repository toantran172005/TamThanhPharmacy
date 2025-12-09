package controller;

import java.util.ArrayList;
import dao.KhachHangDAO;
import entity.KeThuoc;
import entity.KhachHang;
import gui.ChiTietKeThuoc_GUI;
import gui.ChiTietKhachHang_GUI;
import gui.ThongKeKhachHang_GUI;
import gui.TimKiemKH_GUI;

public class TimKiemKhachHangCtrl {

	public ThongKeKhachHang_GUI thongKekhGUI;
	public ArrayList<KhachHang> listKHTK;
	public KhachHangDAO khDAO = new KhachHangDAO();
	public ToolCtrl tool = new ToolCtrl();

	public boolean hienThiHoatDong = true;
	public ArrayList<KhachHang> listKH = new ArrayList<>();
	public TimKiemKH_GUI tkkhGUI;

	public TimKiemKhachHangCtrl(TimKiemKH_GUI tkkhGUI) {
		super();
		this.tkkhGUI = tkkhGUI;
	}

	public void xemChiTietKH() {
		int selectedRow = tkkhGUI.tblKhachHang.getSelectedRow();
		if (selectedRow == -1) {
			tool.hienThiThongBao("Lỗi", "Vui lòng chọn một phiếu để xem chi tiết!", false);
			return;
		}
		int modelRow = tkkhGUI.tblKhachHang.convertRowIndexToModel(selectedRow);
		Object maObj = tkkhGUI.tblKhachHang.getModel().getValueAt(modelRow, 0);
		String maKe = maObj == null ? "" : maObj.toString();

		KhachHang ke = null;
		for (KhachHang k : listKH) {
			if (k.getMaKH().equals(maKe)) {
				ke = k;
				break;
			}
		}

		if (ke != null) {
			ChiTietKhachHang_GUI ctkhGUI = new ChiTietKhachHang_GUI(ke);
			tool.doiPanel(tkkhGUI, ctkhGUI);
		}
	}

	public void xoaKhachHang() {
		listKH = layListKhachHang();
		if (tkkhGUI.btnXoa.getText().equals("Xóa")) {
			if (tool.hienThiXacNhan("Xóa khách hàng", "Xác nhận xóa khách hàng?", null)) {
				int viewRow = tkkhGUI.tblKhachHang.getSelectedRow();
				if (viewRow != -1) {
					int modelRow = tkkhGUI.tblKhachHang.convertRowIndexToModel(viewRow);
					Object maObj = tkkhGUI.tblKhachHang.getModel().getValueAt(modelRow, 0);
					String maKh = maObj == null ? "" : maObj.toString();

					KhachHang kh = null;
					for (KhachHang k : listKH) {
						if (k.getMaKH().equals(maKh)) {
							kh = k;
							break;
						}
					}
					if (khDAO.xoaKhachHang(maKh)) {
						tool.hienThiThongBao("Xóa khách hàng", "Đã xóa khách hàng thành công!", true);
					}

				}
				locTatCa(hienThiHoatDong);
			}
		} else {
			if (tool.hienThiXacNhan("Khôi phục khách hàng", "Xác nhận khôi phục khách hàng?", null)) {
				int viewRow = tkkhGUI.tblKhachHang.getSelectedRow();
				if (viewRow != -1) {
					int modelRow = tkkhGUI.tblKhachHang.convertRowIndexToModel(viewRow);
					Object maObj = tkkhGUI.tblKhachHang.getModel().getValueAt(modelRow, 0);
					String maKh = maObj == null ? "" : maObj.toString();

					KhachHang kh = null;
					for (KhachHang k : listKH) {
						if (k.getMaKH().equals(maKh)) {
							kh = k;
							break;
						}
					}
					if (khDAO.khoiPhucKhachHang(maKh)) {
						tool.hienThiThongBao("Khôi phục khách hàng", "Đã khôi phục khách hàng thành công!", true);
					}
				}
				locTatCa(hienThiHoatDong);
			}
		}
	}

	public void lamMoi() {
		tkkhGUI.txtTenKH.setText("");
		tkkhGUI.txtSdt.setText("");
		hienThiHoatDong = true;
		tkkhGUI.btnLichSuXoa.setText("Lịch sử xóa");
		locTatCa(hienThiHoatDong);
	}

	public ArrayList<KhachHang> layListKhachHang() {
		return khDAO.layListKhachHang();
	}

	public void xuLyBtnLichSuXoa() {
		hienThiHoatDong = !hienThiHoatDong;
		tkkhGUI.btnLichSuXoa.setText(!hienThiHoatDong ? "Danh sách hiện tại" : "Lịch sử xóa");
		tkkhGUI.btnXoa.setText(!hienThiHoatDong ? "Khôi phục" : "Xóa");
		locTatCa(hienThiHoatDong);
	}

	public void locTatCa(boolean hienThiHoatDong) {
		listKH = layListKhachHang();
		ArrayList<KhachHang> ketQua = new ArrayList<>();
		String tenKH = tkkhGUI.txtTenKH.getText().trim();
		String sdt = tool.chuyenSoDienThoai(tkkhGUI.txtSdt.getText().trim());

		for (KhachHang kh : listKH) {
			boolean trungTen = tenKH == null || tenKH.isBlank()
					|| kh.getTenKH().toLowerCase().contains(tenKH.toLowerCase());
			boolean trungSdt = sdt == null || sdt.isBlank() || kh.getSdt().contains(sdt);

			if (trungTen && trungSdt && kh.isTrangThai() == hienThiHoatDong) {
				ketQua.add(kh);
			}
		}

		setDataChoTable(ketQua);
	}

	public void setDataChoTable(ArrayList<KhachHang> list) {
		tkkhGUI.model.setRowCount(0);

		for (KhachHang kh : list) {

			Object[] row = { kh.getMaKH(), kh.getTenKH(), tool.chuyenSoDienThoai(kh.getSdt()), kh.getTuoi(),
					kh.isTrangThai() ? "Hoạt động" : "Đã xóa" };
			tkkhGUI.model.addRow(row);
		}
	}

}
