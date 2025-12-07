package controller;

import java.util.ArrayList;

import dao.PhieuKNHTDAO;
import entity.PhieuKhieuNaiHoTro;
import gui.ChiTietPhieuKNHT_GUI;
import gui.DanhSachKhieuNaiVaHoTroHK_GUI;
import gui.ThemKhieuNai_GUI;

public class DanhSachKNHTCtrl {

	public DanhSachKhieuNaiVaHoTroHK_GUI knhtGUI;
	public PhieuKNHTDAO knhtDAO = new PhieuKNHTDAO();
	public ToolCtrl tool = new ToolCtrl();
	public ArrayList<PhieuKhieuNaiHoTro> listDangHienThi;
	public ArrayList<PhieuKhieuNaiHoTro> listKNHT;

	public DanhSachKNHTCtrl(DanhSachKhieuNaiVaHoTroHK_GUI knhtGUI) {
		super();
		this.knhtGUI = knhtGUI;
	}

	public void chuyenSangThem() {
		ThemKhieuNai_GUI tknGUI = new ThemKhieuNai_GUI();
		tool.doiPanel(knhtGUI, tknGUI);
	}

	public void chuyenSangChiTiet() {
		int row = knhtGUI.tblKNHT.getSelectedRow();
		if (row == -1) {
			tool.hienThiThongBao("Thông báo", "Bạn chưa chọn dòng nào!", false);
			return;
		}

		int modelRow = knhtGUI.tblKNHT.convertRowIndexToModel(row);

		PhieuKhieuNaiHoTro phieu = listDangHienThi.get(modelRow);

		ChiTietPhieuKNHT_GUI ctknhtGUI = new ChiTietPhieuKNHT_GUI(phieu);
		tool.doiPanel(knhtGUI, ctknhtGUI);
	}

	public void lamMoi() {
		knhtGUI.txtTenKH.setText("");
		knhtGUI.txtTenNV.setText("");
		knhtGUI.cmbLoaiDon.setSelectedItem("Tất cả");
		knhtGUI.cmbTrangThai.setSelectedItem("Chờ xử lý");
		locTatCa();
	}

	public void locTatCa() {

		listKNHT = layListPhieuKNHT();

		ArrayList<PhieuKhieuNaiHoTro> ketQua = new ArrayList<>();

		String tenKH = knhtGUI.txtTenKH.getText().trim().toLowerCase();
		String tenNV = knhtGUI.txtTenNV.getText().trim().toLowerCase();
		String loaiDon = knhtGUI.cmbLoaiDon.getSelectedItem().toString();
		String trangThai = knhtGUI.cmbTrangThai.getSelectedItem().toString();

		for (PhieuKhieuNaiHoTro pk : listKNHT) {
			boolean trungTenKH = tenKH.isEmpty() || pk.getKhachHang().getTenKH().toLowerCase().contains(tenKH);
			boolean trungTenNV = tenNV.isEmpty() || pk.getNhanVien().getTenNV().toLowerCase().contains(tenNV);
			boolean trungLoaiDon = loaiDon.equals("Tất cả") || pk.getLoaiDon().equalsIgnoreCase(loaiDon);
			boolean trungTrangThai = trangThai.equals("Tất cả") || trangThai.equals(pk.getTrangThai());

			if (trungTenKH && trungTenNV && trungLoaiDon && trungTrangThai) {
				ketQua.add(pk);
			}
		}

		listDangHienThi = ketQua;
		setDataChoTable(ketQua);
	}

	public ArrayList<PhieuKhieuNaiHoTro> layListPhieuKNHT() {
		return knhtDAO.layListPhieuKNHT();
	}

	public void setDataChoTable(ArrayList<PhieuKhieuNaiHoTro> list) {
		knhtGUI.model.setRowCount(0);

		for (PhieuKhieuNaiHoTro pk : list) {
			Object[] row = { pk.getMaPhieu(), pk.getKhachHang().getTenKH(), pk.getNhanVien().getTenNV(),
					pk.getLoaiDon(), tool.dinhDangLocalDate(pk.getNgayLap()), pk.getTrangThai() };
			knhtGUI.model.addRow(row);
		}
	}

}
