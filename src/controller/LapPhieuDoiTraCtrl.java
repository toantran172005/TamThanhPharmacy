// package controller;
package controller;

import dao.DonViTinhDAO;
import dao.HoaDonDAO;
import dao.PhieuDoiTraDAO;
import dao.ThuocDAO;
import entity.DonViTinh;
import entity.HoaDon;
import entity.PhieuDoiTra;
import gui.ChiTietHoaDon_GUI;
import gui.LapPhieuDoiTra_GUI;
import gui.TimKiemHD_GUI;
import gui.TrangChuQL_GUI;
import gui.TrangChuNV_GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;

public class LapPhieuDoiTraCtrl {
	private final LapPhieuDoiTra_GUI gui;
	private final HoaDonDAO hdDAO = new HoaDonDAO();
	private final PhieuDoiTraDAO pdtDAO = new PhieuDoiTraDAO();
	private final ToolCtrl tool = new ToolCtrl();
	private final DonViTinhDAO dvtDAO = new DonViTinhDAO();
	private final ThuocDAO thuocDAO = new ThuocDAO();

	public LapPhieuDoiTra_GUI getGui() {
		return gui;
	}

	private String maHD;
	private double tongTienHoan = 0;

	public LapPhieuDoiTraCtrl(LapPhieuDoiTra_GUI gui) {
		this.gui = gui;
		suKien();
	}

	// ========== GẮN SỰ KIỆN ==========
	private void suKien() {
		gui.getBtnThem().addActionListener(e -> themThuocVaoPhieu());
		gui.getBtnXoa().addActionListener(e -> xuLyXoaDong());
		gui.getBtnLamMoi().addActionListener(e -> lamMoi());
		gui.getBtnTaoPhieuDT().addActionListener(e -> taoPhieuDoiTra());
		gui.getBtnQuayLai().addActionListener(e -> quayLai());

		gui.getTblHDThuoc().getSelectionModel().addListSelectionListener(event -> {
			if (!event.getValueIsAdjusting()) { // tránh gọi 2 lần
				int selectedRow = gui.getTblHDThuoc().getSelectedRow();
				if (selectedRow != -1) {
					String tenThuoc = gui.getTblHDThuoc().getValueAt(selectedRow, 1).toString();
					gui.getTxtTenThuoc().setText(tenThuoc);
				}
			}
		});
	}

	// ========== GÁN MÃ HÓA ĐƠN VÀ TẢI DỮ LIỆU ==========
	public void setMaHD(String maHD) {
		this.maHD = maHD;
		gui.getLblMaHD().setText(maHD);
		taiDuLieuHoaDon(maHD);
		tinhTongTienHoan();
	}

	// ========== LẤY DỮ LIỆU TỪ HOÁ ĐƠN ==========
	private void taiDuLieuHoaDon(String maHD) {
		HoaDon hd = hdDAO.timHoaDonTheoMa(maHD);
		if (hd != null && hd.getKhachHang() != null) {
			gui.getLblKhachHang().setText(hd.getKhachHang().getTenKH());
		} else {
			gui.getLblKhachHang().setText("Khách lẻ");
		}
		capNhatBangThuocDaMua(maHD);
	}

	// ========== ĐƯA NHỮNG THUỐC ĐÃ MUA LÊN BẢNG ==========
	private void capNhatBangThuocDaMua(String maHD) {
		DefaultTableModel model = (DefaultTableModel) gui.getTblHDThuoc().getModel();
		model.setRowCount(0);

		List<Object[]> chiTietList = hdDAO.layChiTietHoaDon(maHD);
		for (Object[] ct : chiTietList) {
			model.addRow(new Object[] { ct[1], // maThuoc
					ct[2], // tenThuoc
					ct[3], // soLuong
					ct[5], // donVi
					tool.dinhDangVND(ct[6] instanceof Number ? ((Number) ct[6]).doubleValue() : 0),
					tool.dinhDangVND(ct[7] instanceof Number ? ((Number) ct[7]).doubleValue() : 0) });
		}

		gui.getTblHDThuoc().getColumnModel().getColumn(0).setMinWidth(0);
		gui.getTblHDThuoc().getColumnModel().getColumn(0).setMaxWidth(0);
	}

	// ========== THÊM THUỐC VÀO PHIẾU ĐỔI TRẢ ==========
	private void themThuocVaoPhieu() {
		String tenThuoc = gui.getTxtTenThuoc().getText().trim();
		String soLuongStr = gui.getTxtSoLuong().getText().trim();
		String mucHoanStr = (String) gui.getCmbMucHoan().getSelectedItem();
		String ghiChu = gui.getTxaGhiChu().getText().trim();

		if (tenThuoc.isEmpty() || soLuongStr.isEmpty()) {
			tool.hienThiThongBao("Lỗi", "Vui lòng nhập tên thuốc và số lượng!", false);
			return;
		}

		int soLuong;
		try {
			soLuong = Integer.parseInt(soLuongStr);
			if (soLuong <= 0)
				throw new NumberFormatException();
		} catch (NumberFormatException ex) {
			tool.hienThiThongBao("Lỗi", "Số lượng phải là số nguyên dương!", false);
			return;
		}

		// Tìm thuốc trong bảng hóa đơn
		JTable tblHD = gui.getTblHDThuoc();
		int row = -1;
		for (int i = 0; i < tblHD.getRowCount(); i++) {
			if (tblHD.getValueAt(i, 1).toString().equalsIgnoreCase(tenThuoc)) {
				row = i;
				break;
			}
		}

		if (row == -1) {
			tool.hienThiThongBao("Lỗi", "Thuốc không tồn tại trong hóa đơn!", false);
			return;
		}

		int slMua = Integer.parseInt(tblHD.getValueAt(row, 2).toString().replaceAll("[^0-9]", ""));
		if (soLuong > slMua) {
			tool.hienThiThongBao("Lỗi", "Số lượng đổi trả không được vượt quá số lượng đã mua!", false);
			return;
		}

		double donGia = tool.chuyenTienSangSo((String) tblHD.getValueAt(row, 4));
		double thanhTien = donGia * soLuong;
		double mucHoan = Double.parseDouble(mucHoanStr.replace("%", "")) / 100.0;
		double tienHoan = thanhTien * mucHoan;

		tongTienHoan += tienHoan;

		// Thêm vào bảng phiếu đổi trả
		DefaultTableModel modelDT = (DefaultTableModel) gui.getTblPhieuDTThuoc().getModel();
		modelDT.addRow(new Object[] { tenThuoc, soLuong, tblHD.getValueAt(row, 3), // đơn vị
				tblHD.getValueAt(row, 4), // đơn giá
				mucHoanStr, tool.dinhDangVND(tienHoan), ghiChu.isEmpty() ? "Không" : ghiChu, "Xóa" });

		tinhTongTienHoan();
		lamMoiInput();
	}

	// ========== XOÁ 1 DÒNG TTRONG TABLE PHIẾU ĐỔI TRẢ ==========
	private void xuLyXoaDong() {
		JTable table = gui.getTblPhieuDTThuoc();
		int row = table.getSelectedRow();

		if (row == -1) {
			tool.hienThiThongBao("Thông báo", "Vui lòng chọn dòng cần xóa!", false);
			return;
		}

		boolean confirm = tool.hienThiXacNhan("Xác nhận", "Bạn có chắc chắn muốn xoá dòng này?", null);

		if (confirm) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.removeRow(row);

			// Cập nhật lại STT
			for (int i = 0; i < model.getRowCount(); i++) {
				model.setValueAt(i + 1, i, 0);
			}

			tinhTongTienHoan();
		}
	}

	// ========== TÍNH TỔNG TIỀN HOÀN==========
	private void tinhTongTienHoan() {
		JTable tblDT = gui.getTblPhieuDTThuoc();
		DefaultTableModel model = (DefaultTableModel) tblDT.getModel();

		double tong = 0;

		for (int i = 0; i < model.getRowCount(); i++) {
			Object tienHoanObj = model.getValueAt(i, 5); // cột [5] = tiền hoàn
			if (tienHoanObj != null) {
				try {
					double tienHoan = tool.chuyenTienSangSo(tienHoanObj.toString());
					tong += tienHoan;
				} catch (Exception e) {
					System.err.println("Lỗi khi cộng tiền hoàn dòng " + i + ": " + e.getMessage());
				}
			}
		}

		tongTienHoan = tong; // cập nhật biến tổng
		gui.getLblTongTienHoan().setText(tool.dinhDangVND(tongTienHoan));
	}

	// ========== LÀM MỚI CÁC Ô NHẬP ==========
	private void lamMoiInput() {
		gui.getTxtTenThuoc().setText("");
		gui.getTxtSoLuong().setText("");
		gui.getCmbMucHoan().setSelectedIndex(0);
		gui.getTxaGhiChu().setText("");
	}

	// ========== LÀM MỚI ==========
	private void lamMoi() {
		lamMoiInput();
		DefaultTableModel modelDT = (DefaultTableModel) gui.getTblPhieuDTThuoc().getModel();
		modelDT.setRowCount(0);
		tongTienHoan = 0;
		tinhTongTienHoan();
	}

	// ========== TẠO PHIẾU ĐỔI TRẢ ==========
	private void taoPhieuDoiTra() {
		if (gui.getTblPhieuDTThuoc().getRowCount() == 0) {
			tool.hienThiThongBao("Lỗi", "Chưa có thuốc nào để đổi trả!", false);
			return;
		}

		String lyDo = gui.getTxaLyDo().getText().trim();
		if (lyDo.isEmpty()) {
			tool.hienThiThongBao("Lỗi", "Vui lòng nhập lý do đổi trả!", false);
			return;
		}

		// Lấy thông tin hóa đơn
		HoaDon hd = hdDAO.timHoaDonTheoMa(maHD);
		if (hd == null) {
			tool.hienThiThongBao("Lỗi", "Không tìm thấy hóa đơn!", false);
			return;
		}

		// Tạo phiếu đổi trả
		PhieuDoiTra pdt = new PhieuDoiTra();
		pdt.setMaPhieuDT(tool.taoKhoaChinh("PDT"));
		pdt.setHoaDon(hd);
		pdt.setNhanVien(hd.getNhanVien());
		pdt.setNgayDoiTra(LocalDate.now());
		pdt.setLyDo(lyDo);

		String maNV = hd.getNhanVien() != null ? hd.getNhanVien().getMaNV() : null;
		String maHDHienTai = maHD;

		// Thêm phiếu đổi trả
		if (!pdtDAO.themPDT(pdt, maNV, maHDHienTai)) {
			tool.hienThiThongBao("Lỗi", "Không thể tạo phiếu đổi trả!", false);
			return;
		}

		// Thêm chi tiết phiếu đổi trả
		JTable tblDT = gui.getTblPhieuDTThuoc();
		boolean tatCaChiTietThanhCong = true;

		for (int i = 0; i < tblDT.getRowCount(); i++) {
			String tenThuocDT = tblDT.getValueAt(i, 0).toString();
			String maThuoc = null;

			// Tìm mã thuốc tương ứng trong bảng hoá đơn
			for (int j = 0; j < gui.getTblHDThuoc().getRowCount(); j++) {
				if (gui.getTblHDThuoc().getValueAt(j, 1).toString().equalsIgnoreCase(tenThuocDT)) {
					maThuoc = gui.getTblHDThuoc().getValueAt(j, 0).toString();
					break;
				}
			}

			if (maThuoc == null) {
				System.err.println("Không tìm thấy mã thuốc cho: " + tenThuocDT);
				continue;
			}

			String tenDVT = tblDT.getValueAt(i, 2).toString();
			String maDVT = dvtDAO.timMaDVTTheoTen(tenDVT);

			Object[] ct = new Object[] { pdt.getMaPhieuDT(), // [0] maPhieuDT
					maThuoc, // [1] maThuoc (tên thuốc)
					String.valueOf(tblDT.getValueAt(i, 1)), // [2] soLuong (String)
					tblDT.getValueAt(i, 6).toString(), // [3] ghiChu
					String.valueOf(tool.chuyenTienSangSo((String) tblDT.getValueAt(i, 5))), // [4] tienHoan
					String.valueOf(Double.parseDouble(tblDT.getValueAt(i, 4).toString().replace("%", "")) / 100.0), // [5]
																													// mucHoan
					maDVT// [6] maDVT (đơn vị)
			};

			if (!pdtDAO.themChiTietPDTBangObject(ct)) {
				tatCaChiTietThanhCong = false;
				System.err.println("Lỗi thêm chi tiết: " + ct[1]);
			}

			int soLuongTra = Integer.parseInt(tblDT.getValueAt(i, 1).toString());
			if (!thuocDAO.tangSoLuongTon(maThuoc, maDVT, soLuongTra)) {
				tool.hienThiThongBao("Lỗi", "Cập nhật tồn kho thất bại cho thuốc: " + maThuoc, false);
			}
		}

		// KẾT QUẢ
		if (tatCaChiTietThanhCong) {
			tool.hienThiThongBao("Thành công", "Tạo phiếu đổi trả thành công!", true);
			quayLai();
		} else {
			tool.hienThiThongBao("Cảnh báo", "Phiếu đã tạo nhưng một số chi tiết thất bại!", false);
		}
	}

	// ========== QUAY LẠI ==========
	private void quayLai() {
		if (gui.getTrangChuQL() != null) {
			tool.doiPanel(gui, new TimKiemHD_GUI(gui.getTrangChuQL()));
		} else if (gui.getTrangChuNV() != null) {
			tool.doiPanel(gui, new TimKiemHD_GUI(gui.getTrangChuNV()));
		}
	}

}