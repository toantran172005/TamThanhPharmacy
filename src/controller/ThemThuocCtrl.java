package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import dao.DonViTinhDAO;
import dao.KeThuocDAO;
import dao.ThueDAO;
import dao.ThuocDAO;
import entity.DonViTinh;
import entity.KeThuoc;
import entity.QuocGia;
import entity.Thue;
import entity.Thuoc;
import gui.ThemThuoc_GUI;

public class ThemThuocCtrl {

	private ThuocDAO thuocDAO = new ThuocDAO();
	private KeThuocDAO keThuocDao = new KeThuocDAO();
	private DonViTinhDAO dvtDao = new DonViTinhDAO();
	private ThueDAO thueDao = new ThueDAO();
	private ToolCtrl tool = new ToolCtrl();
	private ThemThuoc_GUI themThuoc;
	
	public ThemThuocCtrl(ThemThuoc_GUI themThuoc) {
		// TODO Auto-generated constructor stub
		this.themThuoc = themThuoc;
	}

	public ThemThuocCtrl() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	//set cmb quốc gia
	public void setCmbQuocGia() {
		ArrayList<QuocGia> listQG = thuocDAO.layListQG();
		themThuoc.cmbQuocGia.addItem("Tất cả");
		for(QuocGia qg : listQG) {
			themThuoc.cmbQuocGia.addItem(qg.getTenQG());
		}
	}
	
	//set cmb kệ thuốc
	public void setCmbKeThuoc() {
		ArrayList<KeThuoc> listKT = keThuocDao.layListKeThuoc();
		themThuoc.cmbKeThuoc.addItem("Tất cả");
		for(KeThuoc kt : listKT) {
			themThuoc.cmbKeThuoc.addItem(kt.getLoaiKe());
		}
	}
	
	//set cmb đơn vị
	public void setCmbDonVi() {
		ArrayList<DonViTinh> listDV = dvtDao.layListDVT();
		themThuoc.cmbDonVi.addItem("Tất cả");
		for(DonViTinh dvt : listDV) {
			themThuoc.cmbDonVi.addItem(dvt.getTenDVT());
		}
	}
	
	//set cmb thuế
	public void setCmbThue() {
		ArrayList<Thue> listThue = thueDao.layListThue();
		themThuoc.cmbThue.addItem("Tất cả");
		for(Thue th : listThue) {
			if(!th.getLoaiThue().equalsIgnoreCase("Thuế TNCN")) {
				Double tyLe = th.getTiLeThue() * 100;
				String thue = th.getLoaiThue()+ " " + tyLe + "%";
				themThuoc.cmbThue.addItem(thue);
			}
		}
	}
	
	//thêm thuốc
	public void themThuoc() {
	    String tenThuoc = themThuoc.txtTenThuoc.getText().trim();
	    String dangThuoc = themThuoc.txtDangThuoc.getText().trim();
	    String donVi = themThuoc.cmbDonVi.getSelectedItem().toString();
	    String quocGia = themThuoc.cmbQuocGia.getSelectedItem().toString();
	    String keThuoc = themThuoc.cmbKeThuoc.getSelectedItem().toString();
	    String thueStr = themThuoc.cmbThue.getSelectedItem().toString();

	    int soLuong = (int) themThuoc.spSoLuongTon.getValue();
	    String giaBanStr = themThuoc.txtGiaBan.getText().trim();
	    LocalDate hanSuDung = tool.utilDateSangLocalDate(themThuoc.dpHanSuDung.getDate());
	    String anh = themThuoc.urlAnh;

	    if (tenThuoc.isEmpty() || dangThuoc.isEmpty()) {
	        tool.hienThiThongBao("Lỗi", "Tên thuốc và dạng thuốc không được để trống!", false);
	        return;
	    }

	    if (donVi.equals("Tất cả") || keThuoc.equals("Tất cả") || quocGia.equals("Tất cả")) {
	        tool.hienThiThongBao("Lỗi", "Vui lòng chọn đầy đủ Đơn vị / Quốc gia / Kệ thuốc!", false);
	        return;
	    }

	    if (soLuong < 0) {
	        tool.hienThiThongBao("Lỗi", "Số lượng tồn phải >= 0!", false);
	        return;
	    }

	    if (giaBanStr.isEmpty()) {
	        tool.hienThiThongBao("Lỗi", "Giá bán không được để trống!", false);
	        return;
	    }

	    if (hanSuDung == null || hanSuDung.isBefore(LocalDate.now())) {
	        tool.hienThiThongBao("Lỗi", "Hạn sử dụng không hợp lệ!", false);
	        return;
	    }

	    if (anh == null) {
	        tool.hienThiThongBao("Lỗi", "Vui lòng chọn ảnh thuốc!", false);
	        return;
	    }

	    double giaBan;
	    try {
	        giaBan = Double.parseDouble(giaBanStr);
	        if (giaBan <= 0) throw new NumberFormatException();
	    } catch (Exception e) {
	        tool.hienThiThongBao("Lỗi", "Giá bán không hợp lệ!", false);
	        return;
	    }

	    Thue thue = null;
	    if (!thueStr.equals("Tất cả")) {
	        String so = thueStr.replaceAll("[^0-9]", "");
	        double tyLe = Double.parseDouble(so) / 100;
	        thue = new Thue(thueStr.split(" ")[0], tyLe);
	    }
	    
	    Thuoc thuoc = new Thuoc();
	    thuoc.setTenThuoc(tenThuoc);
	    thuoc.setDangThuoc(dangThuoc);
	    thuoc.setSoLuong(soLuong);
	    thuoc.setGiaBan(giaBan);
	    thuoc.setHanSuDung(hanSuDung);
	    thuoc.setAnh(anh);

	    thuoc.setDvt(new DonViTinh(donVi));
	    thuoc.setKeThuoc(new KeThuoc(keThuoc));
	    //thuoc.setQuocGia(new QuocGia(quocGia));
	    thuoc.setThue(thue);

	    if (thuocDAO.themThuoc(thuoc)) {
	        tool.hienThiThongBao("Thành công", "Thêm thuốc thành công!", true);
	        resetForm();
	    } else {
	        tool.hienThiThongBao("Thất bại", "Thêm thuốc thất bại!", false);
	    }
	}

	
	private void resetForm() {
	    themThuoc.txtTenThuoc.setText("");
	    themThuoc.txtDangThuoc.setText("");
	    themThuoc.txtGiaBan.setText("");
	    themThuoc.spSoLuongTon.setValue(0);
	    themThuoc.dpHanSuDung.setDate(null);

	    themThuoc.cmbDonVi.setSelectedIndex(0);
	    themThuoc.cmbQuocGia.setSelectedIndex(0);
	    themThuoc.cmbKeThuoc.setSelectedIndex(0);
	    themThuoc.cmbThue.setSelectedIndex(0);
	}


}
