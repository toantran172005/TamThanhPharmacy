package controller;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;
import gui.DanhSachKhuyenMai_GUI;

public class KhuyenMaiCtrl {

	private DanhSachKhuyenMai_GUI kmGUI;
	private KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
	ArrayList<KhuyenMai> listKM = new ArrayList<KhuyenMai>();
	
	
	public KhuyenMaiCtrl(DanhSachKhuyenMai_GUI danhSachKhuyenMai_GUI) {
		this.kmGUI = danhSachKhuyenMai_GUI;
	}

	//Đổ dữ liệu lên bảng
	public void setDataChoTable() {
		listKM = kmDAO.layDanhSachKM();
		DefaultTableModel model = (DefaultTableModel) kmGUI.tblKhuyenMai.getModel();
		
		for(KhuyenMai km : listKM) {
			model.addRow(new Object[] {
					km.getMaKM(),
					km.getTenKM(),
					km.getLoaiKM(),
					km.getMucKhuyenMai(),
					km.getNgayBD(),
					km.getNgayKT(),
					km.getTrangThaiHD()
			});
			
		}
	}
	
	
}
