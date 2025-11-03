package controller;

import java.util.ArrayList;
import dao.KhachHangDAO;
import entity.KhachHang;
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
		listKH = layListKhachHang();
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

	public void chuyenSangLSX() {
		hienThiHoatDong = !hienThiHoatDong;
		tkkhGUI.btnLichSuXoa.setText(!hienThiHoatDong ? "Danh sách hiện tại" : "Lịch sử xóa");
		locTatCa(hienThiHoatDong);
	}

	public void locTatCa(boolean hienThiHoatDong) {
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
