package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import dao.DonViTinhDAO;
import dao.PhieuDatHangDAO;
import dao.ThuocDAO;
import gui.ChiTietPhieuDatHang_GUI;
import gui.LapHoaDon_GUI;
import gui.TimKiemPhieuDatHang_GUI;

public class ChiTietPDHCtrl {

	public ChiTietPhieuDatHang_GUI ctpdhGUI;
	public ToolCtrl tool = new ToolCtrl();
	public PhieuDatHangDAO pdhDAO = new PhieuDatHangDAO();
	public DonViTinhDAO dvtDAO = new DonViTinhDAO();
	public ThuocDAO thDAO = new ThuocDAO();

	public ChiTietPDHCtrl(ChiTietPhieuDatHang_GUI ctpdhGUI) {
		super();
		this.ctpdhGUI = ctpdhGUI;
	}

//	public void taoHoaDon() {
//		String maHD = tool.taoKhoaChinh("HD");
//		String maKH = ctpdhGUI.pdh.getKhachHang().getMaKH();
//		String maNV = ctpdhGUI.pdh.getNhanVien().getMaNV();
//		LocalDate ngayLap = LocalDate.now();
//		String diaChiHT = "456 Nguyễn Huệ, TP.HCM";
//		String tenHT = "Hiệu Thuốc Tam Thanh";
//		String hotline = "+84-912345689";
//		String loaiTT = "Chuyển khoản";
//		String ghiChu = ctpdhGUI.txaGhiChu.getText().trim();
//		double tienNhan = 0;
//		boolean trangThai = true;
//
//		List<Object[]> dsChiTiet = new ArrayList<>();
//		DefaultTableModel model = ctpdhGUI.model;
//		for (int i = 0; i < model.getRowCount(); i++) {
//			String maThuoc = thDAO.layMaThuocTheoTen(model.getValueAt(i, 0).toString());
//			int soLuong = Integer.parseInt(model.getValueAt(i, 2).toString());
//			double donGia = thDAO.layDonGiaTheoMaThuoc(maThuoc);
//			String maDVT = dvtDAO.timMaDVTTheoTen(model.getValueAt(i, 3).toString());
//
//			dsChiTiet.add(new Object[] { maThuoc, soLuong, donGia, maDVT });
//		}
//
//		if (dsChiTiet.isEmpty()) {
//			tool.hienThiThongBao("Tạo hóa đơn", "Danh sách chi tiết hóa đơn trống!", false);
//			return;
//		}
//
//		int ketQua = pdhDAO.taoHoaDonVaChiTiet(maHD, maKH, maNV, loaiTT, ngayLap, diaChiHT, tenHT, ghiChu, hotline,
//				tienNhan, trangThai, dsChiTiet);
//
//		if (ketQua == 1 && pdhDAO.capNhatMaHDChoPhieuDatHang(ctpdhGUI.pdh.getMaPDH(), maHD)
//				&& pdhDAO.capNhatTrangThaiPhieu(ctpdhGUI.pdh.getMaPDH(), "Đã giao")) {
//			tool.hienThiThongBao("Tạo hóa đơn", "Tạo hóa đơn thành công!", true);
//			ctpdhGUI.cmbTrangThai.setSelectedItem(trangThai);
//		} else {
//			tool.hienThiThongBao("Tạo hóa đơn", "Không thể tạo hóa đơn! Vui lòng thử lại.", false);
//		}
//	}
	
	public void taoHoaDon() {
	    String maPDH = ctpdhGUI.getLblMaPhieuDat().getText();

	    LapHoaDon_GUI lapHD = new LapHoaDon_GUI(ctpdhGUI.getMainFrameQL());
	    LapHoaDonCtrl ctrl = new LapHoaDonCtrl(lapHD);

	    ctrl.loadTuPhieuDatHang(maPDH);

	    tool.doiPanel(ctpdhGUI, lapHD);
	}


	public void capNhatPDH() {

		if (ctpdhGUI.btnCapNhat.getText().equals("Cập nhật")) {
			ctpdhGUI.btnCapNhat.setText("Lưu");
			ctpdhGUI.choPhepCapNhap();
		} else {
			if (tool.hienThiXacNhan("Cập nhật", "Xác nhận cập nhật trạng thái?", null)) {
				if (ctpdhGUI.pdh.getTrangThai().equals("Đã giao")) {
					tool.hienThiThongBao("Cập nhật", "Không thể cập nhật vì phiếu này đã chuyển thành hóa đơn!", false);
				} else {
					ctpdhGUI.btnCapNhat.setText("Cập nhật");
					ctpdhGUI.choPhepCapNhap();
					if (capNhatTrangThai()) {
						tool.hienThiThongBao("Cập nhật", "Cập nhật trạng thái thành công!", true);
					}
				}
			}
		}

	}

	public boolean capNhatTrangThai() {
		String trangThai = String.valueOf(ctpdhGUI.cmbTrangThai.getSelectedItem());
		if (pdhDAO.capNhatTrangThaiPhieu(ctpdhGUI.pdh.getMaPDH(), trangThai)) {
			return true;
		}
		return false;
	}

	// CODE CŨ
//	public void quayLaiTrangDanhSach() {
//		if (ctpdhGUI.mainFrameQL != null) {
//			tool.doiPanel(ctpdhGUI, new TimKiemPhieuDatHang_GUI(ctpdhGUI.mainFrameQL));
//		} else {
//			tool.doiPanel(ctpdhGUI, new TimKiemPhieuDatHang_GUI(ctpdhGUI.mainFrameNV));
//		}
//	}
	
	// CODE MỚI
	public void quayLaiTrangDanhSach() {
	    if (ctpdhGUI.mainFrameQL != null) {
	        ctpdhGUI.mainFrameQL.setUpNoiDung(
	            new TimKiemPhieuDatHang_GUI(ctpdhGUI.mainFrameQL)
	        );
	    } else {
	        ctpdhGUI.mainFrameNV.setUpNoiDung(
	            new TimKiemPhieuDatHang_GUI(ctpdhGUI.mainFrameNV)
	        );
	    }
	}

}
