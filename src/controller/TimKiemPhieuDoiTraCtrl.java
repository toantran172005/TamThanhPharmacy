package controller;

import dao.PhieuDoiTraDAO;
import entity.HoaDon;
import entity.PhieuDoiTra;
import gui.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.List;

public class TimKiemPhieuDoiTraCtrl {
	public TimKiemPhieuDoiTra_GUI gui;
	public PhieuDoiTraDAO pdtDAO = new PhieuDoiTraDAO();
	public ToolCtrl tool = new ToolCtrl();
	public TrangChuQL_GUI trangChuQL;
	public TrangChuNV_GUI trangChuNV;
	public List<PhieuDoiTra> listPDT;

	public TimKiemPhieuDoiTraCtrl(TimKiemPhieuDoiTra_GUI gui) {
		this.gui = gui;
		this.trangChuQL = gui.getMainFrame();
		this.trangChuNV = gui.getMainFrameNV();
		listPDT = pdtDAO.layListPDT();
		capNhatBang(listPDT);
		suKien();

		// Tự động reload khi panel được hiển thị
		gui.addAncestorListener(new javax.swing.event.AncestorListener() {
			@Override
			public void ancestorAdded(javax.swing.event.AncestorEvent event) {
				listPDT = pdtDAO.layListPDT();
				capNhatBang(listPDT);
			}

			@Override
			public void ancestorRemoved(javax.swing.event.AncestorEvent event) {
			}

			@Override
			public void ancestorMoved(javax.swing.event.AncestorEvent event) {
			}
		});
	}

	public void setTrangChuQL(TrangChuQL_GUI trangChuQL) {
		this.trangChuQL = trangChuQL;
	}

	public void setTrangChuNV(TrangChuNV_GUI trangChuNV) {
		this.trangChuNV = trangChuNV;
	}

	// ================== SỰ KIỆN ==================
	public void suKien() {
		ActionListener setAction = e -> locPhieuDoiTra();
		gui.getTxtKhachHang().addActionListener(setAction);
		gui.getTxtTenNV().addActionListener(setAction);
		gui.getBtnTimKiem().addActionListener(setAction);

		gui.getBtnLamMoi().addActionListener(e -> lamMoiBang());
		gui.getBtnChiTiet().addActionListener(e -> xemChiTiet());
	}

	// ================== LỌC PHIẾU ĐỔI TRẢ ==================
	public void locPhieuDoiTra() {
		String tenKH = gui.getTxtKhachHang().getText().trim().toLowerCase();
		String tenNV = gui.getTxtTenNV().getText().trim().toLowerCase();
		List<PhieuDoiTra> danhSachHienTai = pdtDAO.layListPDT();
		List<PhieuDoiTra> listLoc = danhSachHienTai;

		if (!tenKH.isEmpty()) {
			listLoc = danhSachHienTai.stream().filter(pdt -> {
				String ten = pdt.getHoaDon().getKhachHang().getTenKH();
				return ten != null && ten.toLowerCase().contains(tenKH);
			}).toList();
		}

		if (!tenNV.isEmpty()) {
			listLoc = listLoc.stream().filter(pdt -> {
				String ten = pdt.getHoaDon().getNhanVien().getTenNV();
				return ten != null && ten.toLowerCase().contains(tenNV);
			}).toList();
		}

		if (listLoc.isEmpty() && (!tenKH.isEmpty() || !tenNV.isEmpty())) {
			tool.hienThiThongBao("Kết quả", "Không tìm thấy phiếu đổi trả phù hợp.", false);
		}

		capNhatBang(listLoc);
	}

	// ================== LÀM MỚI BẢNG ==================
	public void lamMoiBang() {
		gui.getTxtKhachHang().setText("");
		gui.getTxtTenNV().setText("");
		listPDT = pdtDAO.layListPDT();
		capNhatBang(listPDT);
	}

	// ========== XEM CHI TIẾT ==========
	public void xemChiTiet() {
		int row = gui.getTblPhieuDoiTra().getSelectedRow();
		if (row == -1) {
			tool.hienThiThongBao("Thông báo", "Vui lòng chọn một phiếu đổi trả!", false);
			return;
		}
		String maPDT = (String) gui.getTblPhieuDoiTra().getValueAt(row, 0);
		PhieuDoiTra pdt = pdtDAO.timPhieuDoiTraTheoMa(maPDT);

		ChiTietPhieuDoiTra_GUI chiTietPanel;
		if (trangChuQL != null) {
			chiTietPanel = new ChiTietPhieuDoiTra_GUI(trangChuQL);
			trangChuQL.setUpNoiDung(chiTietPanel);
		} else if (trangChuNV != null) {
			chiTietPanel = new ChiTietPhieuDoiTra_GUI(trangChuNV);
			trangChuNV.setUpNoiDung(chiTietPanel);
		} else
			return;

		chiTietPanel.getCtrl().hienThiThongTinPhieuDT(pdt);
		;
	}

	// ================== CẬP NHẬT BẢNG ==================
	public void capNhatBang(List<PhieuDoiTra> list) {
		this.listPDT = list;
		DefaultTableModel model = (DefaultTableModel) gui.getTblPhieuDoiTra().getModel();
		model.setRowCount(0);

		for (PhieuDoiTra pdt : list) {
			model.addRow(new Object[] { pdt.getMaPhieuDT(), pdt.getNhanVien().getTenNV(),
					pdt.getHoaDon().getKhachHang().getTenKH(), tool.dinhDangLocalDate(pdt.getNgayDoiTra()),
					pdt.getLyDo() });
		}
		gui.getTblPhieuDoiTra().repaint();
	}
}