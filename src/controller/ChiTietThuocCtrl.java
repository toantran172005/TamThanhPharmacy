package controller;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import dao.ThuocDAO;
import entity.Thuoc;
import gui.ChiTietThuoc_GUI;
import gui.TimKiemThuoc_GUI;

public class ChiTietThuocCtrl {

	private ToolCtrl tool = new ToolCtrl();
	private ThuocDAO thDAO = new ThuocDAO();
	private ChiTietThuoc_GUI ctThuoc;
	
	public ChiTietThuocCtrl(ChiTietThuoc_GUI ctThuoc) {
		this.ctThuoc = ctThuoc;
	}
	
	//Hiển thị chi tiết thuốc
	public void xemChiTietThuoc(String maThuoc) {
		if(maThuoc == null) {
			tool.hienThiThongBao("Lỗi!", "Mã thuốc không hợp lệ", false);
			return;
		}
		
		try {
			ArrayList<Thuoc> list = thDAO.layListThuocHoanChinh();
			for(Thuoc t : list) {
				if(maThuoc.equalsIgnoreCase(t.getMaThuoc())) {
					//lấy đường dẫn ảnh
					String anhPath = t.getAnh();
	                ImageIcon icon = null;
	                if (anhPath != null && anhPath.startsWith("/picture/")) {
	                    java.net.URL imgURL = getClass().getResource(anhPath);
	                    if (imgURL != null) {
	                        icon = new ImageIcon(imgURL);
	                    } else {
	                        tool.hienThiThongBao("Lỗi", "Không tìm thấy ảnh", false);
	                    }
	                }
	                
	                //Đổ dữ liệu
	                Image img = icon.getImage().getScaledInstance(240, 240, Image.SCALE_SMOOTH);
	                ctThuoc.lblAnh.setIcon(new ImageIcon(img));
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//quay lại trang tìm kiếm
	public void quayLaiTrangTimKiem() {
		tool.doiPanel(ctThuoc, new TimKiemThuoc_GUI());
	}
	
}
