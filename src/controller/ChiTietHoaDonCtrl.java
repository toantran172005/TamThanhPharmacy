package controller;

import dao.HoaDonDAO;
import entity.HoaDon;
import gui.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChiTietHoaDonCtrl {
	private ChiTietHoaDon_GUI gui;
	private HoaDonDAO hdDAO = new HoaDonDAO();
	private ToolCtrl tool = new ToolCtrl();

	public ChiTietHoaDonCtrl(ChiTietHoaDon_GUI gui) {
		this.gui = gui;
		suKien();
	}

	// ========== GẮN SỰ KIỆN ==========
	private void suKien() {
		gui.getBtnQuayLai().addActionListener(e -> quayLai());
		gui.getBtnInHoaDon().addActionListener(e -> inHoaDon());
		gui.getBtnTaoPhieuDoiTra().addActionListener(e -> taoPhieuDoiTra());
	}

	// ========== HIỂN THỊ THÔNG TIN HOÁ ĐƠN ==========
	public void hienThiThongTinHoaDon(HoaDon hd) {
		if (hd == null)
			return;

		gui.getLblMaHD().setText(hd.getMaHD());
		gui.getLblNgayLap().setText(tool.dinhDangLocalDate(hd.getNgayLap()));
		gui.getLblNhanVien().setText(hd.getNhanVien() != null ? hd.getNhanVien().getTenNV() : "Không xác định");
		gui.getLblKhachHang().setText(hd.getKhachHang() != null ? hd.getKhachHang().getTenKH() : "Khách lẻ");
		gui.getLblGhiChu().setText(hd.getGhiChu() != null ? hd.getGhiChu() : "Không có");
		gui.getLblDiaChi().setText(hd.getDiaChiHT() != null ? hd.getDiaChiHT() : "Chưa có");
		gui.getLblHotline().setText(hd.getHotline() != null ? tool.chuyenSoDienThoai(hd.getHotline()) : "Chưa có");

		double tongTien = hdDAO.tinhTongTienTheoHoaDon(hd.getMaHD());
		double tienNhan = hd.getTienNhan();
		double tienThua = Math.max(0, tienNhan - tongTien);

		gui.getLblTongTien().setText(tool.dinhDangVND(tongTien));
		gui.getLblTienNhan().setText(tool.dinhDangVND(tienNhan));
		gui.getLblTienThua().setText(tool.dinhDangVND(tienThua));

		capNhatBangChiTiet(hd.getMaHD());
	}

	// ========== HIỂN THỊ THUỐC LÊN BẢNG ==========
	private void capNhatBangChiTiet(String maHD) {
		DefaultTableModel model = (DefaultTableModel) gui.getTblThuoc().getModel();
		model.setRowCount(0);

		List<Object[]> chiTietList = hdDAO.layChiTietHoaDon(maHD);
		for (Object[] ct : chiTietList) {
			model.addRow(new Object[] { ct[2], // tenThuoc
					ct[3], // soLuong
					ct[5], // donVi
					tool.dinhDangVND(ct[6] instanceof Number ? ((Number) ct[6]).doubleValue() : 0),
					tool.dinhDangVND(ct[7] instanceof Number ? ((Number) ct[7]).doubleValue() : 0) });
		}
	}

	// ========== QUAY LẠI TRANG TÌM KIẾM HOÁ ĐƠN ==========
	private void quayLai() {
		if (gui.getMainFrameQL() != null) {
			tool.doiPanel(gui, new TimKiemHD_GUI(gui.getMainFrameQL()));
		} else if (gui.getMainFrameNV() != null) {
			tool.doiPanel(gui, new TimKiemHD_GUI(gui.getMainFrameNV()));
		}
	}
	

	// ========== IN HOÁ ĐƠN ==========
	private void inHoaDon() {
		tool.hienThiThongBao("In hóa đơn", "Chức năng in đang được phát triển...", true);
		// TODO: Dùng JasperReports, PDF, hoặc PrinterJob.print()
	}

	// ========== TẠO PHIẾU ĐỔI TRẢ ==========
	private void taoPhieuDoiTra() {
		String maHD = gui.getLblMaHD().getText();
		if (maHD.isEmpty()) {
			tool.hienThiThongBao("Lỗi", "Không có hóa đơn để đổi trả!", false);
			return;
		}

		LapPhieuDoiTra_GUI panel = new LapPhieuDoiTra_GUI();
		panel.setTrangChuQL(gui.getMainFrameQL());
		panel.setTrangChuNV(gui.getMainFrameNV());

		LapPhieuDoiTraCtrl ctrl = new LapPhieuDoiTraCtrl(panel);
		ctrl.setMaHD(maHD); // Truyền mã HD

		if (gui.getMainFrameQL() != null) {
			gui.getMainFrameQL().setUpNoiDung(panel);
		} else if (gui.getMainFrameNV() != null) {
			gui.getMainFrameNV().setUpNoiDung(panel);
		}
	}
}