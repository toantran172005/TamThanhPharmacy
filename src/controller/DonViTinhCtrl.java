package controller;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import dao.DonViTinhDAO;
import entity.DonViTinh;
import gui.DonVi_GUI;

public class DonViTinhCtrl {
	
	private DonVi_GUI dvGUI;
	private ToolCtrl tool = new ToolCtrl();
	private DonViTinhDAO dvDAO = new DonViTinhDAO();
	ArrayList<DonViTinh> listDVT = new ArrayList<DonViTinh>();
	
	public DonViTinhCtrl(DonVi_GUI donVi_GUI) {
		this.dvGUI = donVi_GUI;
	}

	//Đưa dữ liệu lên bảng
	public void setDataChoTable() {
		listDVT = dvDAO.layListDVT();
		DefaultTableModel model = (DefaultTableModel) dvGUI.tblDonVi.getModel();
		int stt = 1;
		
		for(DonViTinh dvt : listDVT) {
			model.addRow(new Object[] {
				stt++,
				dvt.getMaDVT(),
				dvt.getTenDVT()
			});
		}
	}
	
	
	
	
}
