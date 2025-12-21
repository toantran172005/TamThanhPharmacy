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
