package controller;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import dao.NhanVienDAO;
import entity.NhanVien;
import gui.TimKiemNV_GUI;

public class TimKiemNhanVienCtrl {

	public TimKiemNV_GUI tknvGUI;
	public ArrayList<NhanVien> listNV;
	public NhanVienDAO nvDAO = new NhanVienDAO();
	public ToolCtrl tool = new ToolCtrl();

	public TimKiemNhanVienCtrl(TimKiemNV_GUI tknvGUI) {
		super();
		this.tknvGUI = tknvGUI;
		listNV = layListNhanVien();
	}

	public ArrayList<NhanVien> layListNhanVien() {
		return nvDAO.layListNhanVien();
	}

	public void setDataChoTable(ArrayList<NhanVien> list) {
		DefaultTableModel model = (DefaultTableModel) tknvGUI.tblNhanVien.getModel();
		model.setRowCount(0);

		for (NhanVien nv : list) {

			Object[] row = { nv.getMaNV(), nv.getTenNV(), tool.chuyenSoDienThoai(nv.getSdt()),
					nv.isGioiTinh() ? "Nam" : "Nữ", nv.getChucVu() };
			tknvGUI.model.addRow(row);
		}

	}

}
