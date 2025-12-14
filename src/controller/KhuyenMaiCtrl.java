package controller;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;
import gui.ChiTietKhuyenMai_GUI;
import gui.DanhSachKhuyenMai_GUI;

public class KhuyenMaiCtrl {

	private DanhSachKhuyenMai_GUI kmGUI;
	private ChiTietKhuyenMai_GUI ctGUI;
	private KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
	private ToolCtrl tool = new ToolCtrl();

	ArrayList<KhuyenMai> listKM = new ArrayList<KhuyenMai>();
	
	public KhuyenMaiCtrl(DanhSachKhuyenMai_GUI danhSachKhuyenMai_GUI) {
		this.kmGUI = danhSachKhuyenMai_GUI;
	}

	public KhuyenMaiCtrl(ChiTietKhuyenMai_GUI chiTietKhuyenMai_GUI) {
		this.ctGUI = chiTietKhuyenMai_GUI;
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
	
	//Xem chi tiết KM
	public void xemChiTietKM() {
		int selectedRow = kmGUI.tblKhuyenMai.getSelectedRow();
		if(selectedRow == -1) {
			tool.hienThiThongBao("Lỗi!", "Vui lòng chọn 1 khuyến mãi để xem chi tiết", false);
			return;
		} 
		
		int modelRow = kmGUI.tblKhuyenMai.convertRowIndexToModel(selectedRow);
		String maKM = kmGUI.tblKhuyenMai.getModel().getValueAt(selectedRow, 0).toString();
		
		KhuyenMai km = kmDAO.layKhuyenMaiTheoMa(maKM);
		ChiTietKhuyenMai_GUI ctGUI = new ChiTietKhuyenMai_GUI();
		
	}
	
	
	
}
