package controller;

import dao.PhieuDoiTraDAO;
import dao.ThuocDAO;
import entity.PhieuDoiTra;
import gui.ChiTietPhieuDoiTra_GUI;
import gui.TimKiemHD_GUI;
import gui.TimKiemPhieuDoiTra_GUI;
import gui.TrangChuNV_GUI;
import gui.TrangChuQL_GUI;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;

public class ChiTietPhieuDoiTraCtrl {
	public ChiTietPhieuDoiTra_GUI gui;
	public PhieuDoiTraDAO phieuDTDAO = new PhieuDoiTraDAO();
	public ToolCtrl tool = new ToolCtrl();
	public TrangChuNV_GUI trangChuNV;
	public TrangChuQL_GUI trangChuQL;
	public ThuocDAO thuocDao = new ThuocDAO();

	public ChiTietPhieuDoiTraCtrl(ChiTietPhieuDoiTra_GUI gui) {
		this.gui = gui;
		suKien();
	}

	// ========== GẮN SỰ KIỆN ==========
	public void suKien() {
		gui.getBtnQuayLai().addActionListener(e -> quayLai());
	}

	// ========== HIỂN THỊ THÔNG TIN PHIẾU ĐỔI TRẢ ==========
	public void hienThiThongTinPhieuDT(PhieuDoiTra phieuDT) {
		if (phieuDT == null)
			return;

		String maPhieuDT = phieuDT.getMaPhieuDT();
		String maHD = phieuDT.getHoaDon().getMaHD();
		String tenKH = phieuDT.getHoaDon().getKhachHang().getTenKH();
		String tenNV = phieuDT.getNhanVien().getTenNV();
		String diaChi = phieuDT.getHoaDon().getDiaChiHT();
		String hotline = phieuDT.getHoaDon().getHotline();
		String lyDo = phieuDT.getLyDo();

		gui.getLblMaPhieuDT().setText(maPhieuDT);
		gui.getLblMaHD().setText(maHD);
		gui.getLblKhachHang().setText(tenKH);
		gui.getLblNhanVien().setText(tenNV);
		gui.getLblDiaChi().setText(diaChi);
		gui.getLblHotline().setText(tool.chuyenSoDienThoai(hotline));
		gui.getLblLyDo().setText(lyDo);

		if (phieuDT.getNgayDoiTra() instanceof LocalDate ngayDoiTra) {
			gui.getLblNgayLap().setText(tool.dinhDangLocalDate(ngayDoiTra));
		} else {
			gui.getLblNgayLap().setText("");
		}

		setDataChoTable(maPhieuDT);

		double tongTienHoan = phieuDTDAO.tinhTongTienHoanTheoPhieuDT(maPhieuDT);
		gui.getLblTongTienHoan().setText(tool.dinhDangVND(tongTienHoan));
	}

	// ========== ĐƯA DỮ LIỆU VÀO BẢNG THUỐC ==========
	public void setDataChoTable(String maPhieuDT) {
		List<Object[]> listCT = phieuDTDAO.layDanhSachThuocTheoPhieuDT(maPhieuDT);
		DefaultTableModel model = (DefaultTableModel) gui.getTblThuoc().getModel();
		model.setRowCount(0);
		String noiSanXuat;

		for (Object[] row : listCT) {
			double tienHoan = row[7] instanceof Number ? ((Number) row[7]).doubleValue() : 0;
			System.out.print(row[1]);
			noiSanXuat = thuocDao.timTenQGTheoMaThuoc(row[1].toString());
			model.addRow(new Object[] { row[2], // tên thuốc
					noiSanXuat,
					row[3], // số lượng
					row[5], // đơn vị
					row[6], // mức hoàn
					tool.dinhDangVND(tienHoan), 
					row[8] // ghi chú
			});
		}
	}

	// ========== QUAY LẠI TRANG TÌM KIẾM PHIẾU ĐỔI TRẢ ==========
	public void quayLai() {
		if (gui.getMainFrameQL() != null) {
			tool.doiPanel(gui, new TimKiemPhieuDoiTra_GUI(gui.getMainFrameQL()));
		} else if (gui.getMainFrameNV() != null) {
			tool.doiPanel(gui, new TimKiemPhieuDoiTra_GUI(gui.getMainFrameNV()));
		}
	}

}