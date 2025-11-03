package controller;

import java.util.ArrayList;

import dao.PhieuKNHTDAO;
import entity.PhieuKhieuNaiHoTro;
import gui.DanhSachKhieuNaiVaHoTroHK_GUI;

public class DanhSachKNHTCtrl {

	public DanhSachKhieuNaiVaHoTroHK_GUI knhtGUI;
	public PhieuKNHTDAO knhtDAO = new PhieuKNHTDAO();
	public ToolCtrl tool = new ToolCtrl();

	public ArrayList<PhieuKhieuNaiHoTro> listKNHT;

	public DanhSachKNHTCtrl(DanhSachKhieuNaiVaHoTroHK_GUI knhtGUI) {
		super();
		this.knhtGUI = knhtGUI;
		listKNHT = layListPhieuKNHT();
	}
	
	public void lamMoi() {
	    knhtGUI.txtTenKH.setText("");
	    knhtGUI.txtTenNV.setText("");
	    knhtGUI.cmbLoaiDon.setSelectedItem("Tất cả");
	    knhtGUI.cmbTrangThai.setSelectedItem("Chờ xử lý");
	    locTatCa();
	}


	public void locTatCa() {
		ArrayList<PhieuKhieuNaiHoTro> ketQua = new ArrayList<>();

		String tenKH = knhtGUI.txtTenKH.getText().trim().toLowerCase();
		String tenNV = knhtGUI.txtTenNV.getText().trim().toLowerCase();
		String loaiDon = knhtGUI.cmbLoaiDon.getSelectedItem().toString();
		String trangThai = knhtGUI.cmbTrangThai.getSelectedItem().toString();

		for (PhieuKhieuNaiHoTro pk : listKNHT) {
			boolean trungTenKH = tenKH.isEmpty() || pk.getKhachHang().getTenKH().toLowerCase().contains(tenKH);
			boolean trungTenNV = tenNV.isEmpty() || pk.getNhanVien().getTenNV().toLowerCase().contains(tenNV);
			boolean trungLoaiDon = loaiDon.equals("Tất cả") || pk.getLoaiDon().equalsIgnoreCase(loaiDon);
			boolean trungTrangThai = trangThai.equals(pk.getTrangThai());

			if (trungTenKH && trungTenNV && trungLoaiDon && trungTrangThai) {
				ketQua.add(pk);
			}
		}

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
