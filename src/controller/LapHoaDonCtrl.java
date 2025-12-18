package controller;

import dao.*;
import entity.*;
import gui.LapHoaDon_GUI;
import gui.TrangChuNV_GUI;
import gui.TrangChuQL_GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.tools.Tool;

import java.awt.Color;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LapHoaDonCtrl {
	public LapHoaDon_GUI gui;
	public ToolCtrl tool = new ToolCtrl();
	public TrangChuQL_GUI trangChuQL;
	public TrangChuNV_GUI trangChuNV;

	public ThuocDAO thuocDAO = new ThuocDAO();
	public KhachHangDAO khDAO = new KhachHangDAO();
	public NhanVienDAO nvDAO = new NhanVienDAO();
	public DonViTinhDAO dvtDAO = new DonViTinhDAO();
	public KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
	public HoaDonDAO hdDAO = new HoaDonDAO();

	public List<KhachHang> dsKhachHang;
	public List<Thuoc> dsThuoc;
	public DefaultTableModel tableModel;

	public boolean dangSetTenKH = false;
	public boolean dangSetSdtKH = false;

	public LapHoaDonCtrl() {
		this(null);
	}

	public LapHoaDonCtrl(LapHoaDon_GUI gui) {
		this.gui = gui;
		if (gui == null)
			return;
		this.trangChuQL = gui.getMainFrame();
		this.trangChuNV = gui.getMainFrameNV();
		this.tableModel = (DefaultTableModel) gui.getTblThuoc().getModel();
		loadData();
	}
	
	public void setComboxQuocGia() {
	    Object selected = gui.getCmbSanPham().getSelectedItem();
	    if (selected == null) return;

	    String tenThuoc = selected.toString();

//	    String maThuoc = thuocDAO.layMaThuocTheoTen(tenThuoc);
//	    if (maThuoc == null) return;

	    gui.getCmbQuocGia().removeAllItems();

	    ArrayList<QuocGia> listQG = thuocDAO.layListQuocGiaTheoThuoc(tenThuoc);
	    if (listQG != null) {
	        for (QuocGia qg : listQG) {
	            gui.getCmbQuocGia().addItem(qg.getTenQG());
	        }
	    }
	}

	// === TẢI DỮ LIỆU ===
	public void loadData() {
		taiDuLieu();
		suKien();
		goiYKhachHang();
		setComboxQuocGia();
	}

	// ========== TẢI DỮ LIỆU ==========
	public void taiDuLieu() {
		dsKhachHang = khDAO.layListKhachHang();
		dsThuoc = thuocDAO.layListThuoc();

		gui.getCmbSanPham().removeAllItems();
		for (Thuoc t : dsThuoc)
			gui.getCmbSanPham().addItem(t.getTenThuoc());

		List<DonViTinh> dsDVT = dvtDAO.layListDVT();
		gui.getCmbDonVi().removeAllItems();
		for (DonViTinh dvt : dsDVT)
			gui.getCmbDonVi().addItem(dvt.getTenDVT());
		if (!dsDVT.isEmpty())
			gui.getCmbDonVi().setSelectedIndex(0);

		gui.getCmbHTThanhToan().setSelectedItem("Tiền mặt");
	}


	// ========== SỰ KIỆN ==========
	public void suKien() {
		gui.getBtnThem().addActionListener(e -> xuLyThemThuocVaoBang());
		gui.getBtnXoa().addActionListener(e -> xuLyXoaDong());
		gui.getBtnLamMoi().addActionListener(e -> lamMoi());
		gui.getBtnTaoHD().addActionListener(e -> xuLyXuatHoaDon());
		gui.getCmbHTThanhToan().addActionListener(e -> tinhTienThua());
		gui.getTxtTienNhan().addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tinhTienThua();
			}
		});
		
		gui.getCmbSanPham().addActionListener(e -> setComboxQuocGia());
	}

	// ========== TÌM KHÁCH HÀNG ==========
	public void goiYKhachHang() {
		gui.getTxtTenKH().addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (dangSetTenKH || gui.getTxtTenKH().getText().trim().isEmpty())
					return;
				String input = gui.getTxtTenKH().getText().trim().toLowerCase();
				List<KhachHang> ketQua = dsKhachHang.stream().filter(kh -> kh.getTenKH().toLowerCase().contains(input))
						.limit(5).toList();
				if (!ketQua.isEmpty())
					hienThiListKhachHang(gui.getTxtTenKH(), ketQua, true);
			}
		});

		gui.getTxtSdt().addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (dangSetSdtKH || gui.getTxtSdt().getText().trim().isEmpty())
					return;
				String input = gui.getTxtSdt().getText().trim();
				List<KhachHang> ketQua = dsKhachHang.stream()
						.filter(kh -> tool.chuyenSoDienThoai(kh.getSdt()).contains(input)).limit(5).toList();
				if (!ketQua.isEmpty())
					hienThiListKhachHang(gui.getTxtSdt(), ketQua, false);
			}
		});
	}

	// ========== HIỂN THỊ KHÁCH HÀNG ĐÃ CÓ ==========
	public void hienThiListKhachHang(JTextField tf, List<KhachHang> list, boolean isTen) {
		JPopupMenu pop = new JPopupMenu();
		for (KhachHang kh : list) {
			String text = isTen ? kh.getTenKH() + " - " + tool.chuyenSoDienThoai(kh.getSdt())
					: tool.chuyenSoDienThoai(kh.getSdt()) + " - " + kh.getTenKH();
			JMenuItem item = new JMenuItem(text);
			item.addActionListener(e -> {
				if (isTen) {
					dangSetSdtKH = true;
					gui.getTxtTenKH().setText(kh.getTenKH());
					gui.getTxtSdt().setText(tool.chuyenSoDienThoai(kh.getSdt()));
					gui.getTxtTuoi().setText(String.valueOf(kh.getTuoi()));
					gui.getTxtTuoi().setEditable(false);
					dangSetSdtKH = false;
				} else {
					dangSetTenKH = true;
					gui.getTxtSdt().setText(tool.chuyenSoDienThoai(kh.getSdt()));
					gui.getTxtTenKH().setText(kh.getTenKH());
					gui.getTxtTuoi().setText(String.valueOf(kh.getTuoi()));
					gui.getTxtTuoi().setEditable(false);
					dangSetTenKH = false;
				}
				pop.setVisible(false);
			});
			pop.add(item);
		}
		SwingUtilities.invokeLater(() -> pop.show(tf, 0, tf.getHeight()));
	}

	// ========== THÊM THUỐC XUỐNG BẢNG ==========
	public void xuLyThemThuocVaoBang() {
		String tenThuoc = gui.getCmbSanPham().getEditor().getItem().toString().trim();
		if (tenThuoc.isEmpty()) {
			tool.hienThiThongBao("Lỗi", "Vui lòng chọn thuốc!", false);
			return;
		}

		// ===== Lấy thông tin thuốc =====
		Thuoc thuoc = dsThuoc.stream().filter(t -> t.getTenThuoc().equalsIgnoreCase(tenThuoc)).findFirst().orElse(null);
		if (thuoc == null) {
			tool.hienThiThongBao("Lỗi", "Không tìm thấy thuốc \"" + tenThuoc + "\" trong danh sách!", false);
			return;
		}

		// ===== Kiểm tra số lượng =====
		int tonKho = thuocDAO.laySoLuongTon(thuoc.getMaThuoc());
		int sl;
		try {
			sl = Integer.parseInt(gui.getTxtSoLuong().getText().trim());
			if (sl <= 0 || sl > tonKho)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			tool.hienThiThongBao("Lỗi", "Số lượng phải lớn hơn 0 và bé hơn số lượng tồn (" + tonKho + ")!", false);
			return;
		}

		// ===== Lấy đơn vị tính & giá gốc =====
		String tenQG = (String) gui.getCmbQuocGia().getSelectedItem();
		String tenDVT = (String) gui.getCmbDonVi().getSelectedItem();
		DonViTinh dvt = dvtDAO.timTheoTen(tenDVT);
		double donGiaGoc = (dvt != null) ? thuocDAO.layGiaBanTheoDVT(thuoc.getMaThuoc(), dvt.getMaDVT())
				: thuoc.getGiaBan();

		// ===== Tính khuyến mãi =====
		double donGiaSauKM = donGiaGoc;
		double thanhTien = donGiaSauKM * sl;
		double mucGiam;
		String moTaKM = "Không có";
		String maKM = thuocDAO.layMaKMTheoMaThuoc(thuoc.getMaThuoc());
		LocalDate homNay = LocalDate.now();

		if (maKM != null && !maKM.isEmpty()) {
			KhuyenMai km = kmDAO.layKhuyenMaiTheoMa(maKM);
			if (km != null && !homNay.isBefore(km.getNgayBD()) && !homNay.isAfter(km.getNgayKT())) {
				switch (km.getLoaiKM().toLowerCase()) {
				case "giảm giá":
					mucGiam = km.getMucKM();
					donGiaSauKM *= (1 - mucGiam / 100.0);
					thanhTien = donGiaSauKM * sl;
					moTaKM = "Giảm " + mucGiam + "%";
					break;

				case "mua tặng":
					int soLuongTang = (sl / km.getSoLuongMua()) * km.getSoLuongTang();
					if (soLuongTang > 0) {
						moTaKM = String.format("Mua %d tặng %d (Tổng: %d)", km.getSoLuongMua(), km.getSoLuongTang(),
								sl + soLuongTang);
						thanhTien = donGiaGoc * sl; // chỉ tính phần mua
						sl += soLuongTang;
					}
					break;
				}
			}
		}

		// ===== Kiểm tra trùng thuốc trong bảng =====
		DefaultTableModel model = (DefaultTableModel) gui.getTblThuoc().getModel();
		for (int i = 0; i < model.getRowCount(); i++) {
			if (model.getValueAt(i, 1).toString().equalsIgnoreCase(thuoc.getTenThuoc())) {
				int soLuongCu = Integer.parseInt(model.getValueAt(i, 2).toString());
				int soLuongMoi = soLuongCu + sl;
				model.setValueAt(soLuongMoi, i, 2);
				model.setValueAt(tool.dinhDangVND(donGiaSauKM), i, 4);
				model.setValueAt(tool.dinhDangVND(donGiaSauKM * soLuongMoi), i, 5);
				model.setValueAt(moTaKM, i, 6);
				tinhTongTien();
				resetFormNhap();
				return;
			}
		}

		// ===== Thêm dòng mới =====
		model.addRow(
				new Object[] { model.getRowCount() + 1, thuoc.getTenThuoc(), tenQG, sl, dvt != null ? dvt.getTenDVT() : "",
						tool.dinhDangVND(donGiaGoc), tool.dinhDangVND(thanhTien), moTaKM, "Xóa" });

		tinhTongTien();
		resetFormNhap();
	}

	// ========== Reset form nhập ==========
	public void resetFormNhap() {
		gui.getTxtSoLuong().setText("");
		gui.getCmbSanPham().setSelectedIndex(0);
		gui.getCmbDonVi().setSelectedIndex(0);
	}

	// ========== XOÁ DÒNG TRÊN BẢNG ==========
	public void xuLyXoaDong() {
		JTable table = gui.getTblThuoc();
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

			tinhTongTien();
		}
	}

	// ========== SETUP STT CỦA CÁC DÒNG ==========
	public void capNhatSTT() {
		for (int i = 0; i < tableModel.getRowCount(); i++)
			tableModel.setValueAt(i + 1, i, 0);
	}

	// ========== TÍNH TỔNG TIỀN CỦA HOÁ ĐƠN ==========
	public void tinhTongTien() {
		double tong = 0;

		for (int i = 0; i < tableModel.getRowCount(); i++) {
			Object giaTri = tableModel.getValueAt(i, 6); // Cột chứa giá tiền
			if (giaTri != null) {
				String text = giaTri.toString().trim();
				double gia = tool.chuyenTienSangSo(text);
				tong += gia;
			}
		}

		gui.getLblTongTien().setText(tool.dinhDangVND(tong));
	}

	// ========== TÍNH TIỀN THỪA CỦA HOÁ ĐƠN ==========
	public void tinhTienThua() {
		// Nếu không chọn "Tiền mặt" → không có tiền thừa
		if (!"Tiền mặt".equals(gui.getCmbHTThanhToan().getSelectedItem())) {
			gui.getLblTienThua().setText("0 VNĐ");
			return;
		}

		try {
			double tong = tool.chuyenTienSangSo(gui.getLblTongTien().getText());
			double nhan = tool.chuyenTienSangSo(gui.getTxtTienNhan().getText());

			double tienThua = nhan - tong;
			if (tienThua < 0)
				tienThua = 0; // tránh âm

			// Hiển thị lại bằng định dạng VND
			gui.getLblTienThua().setText(tool.dinhDangVND(tienThua));

		} catch (Exception e) {
			gui.getLblTienThua().setText("0 VNĐ");
		}
	}

	// ========== LÀM MỚI ==========
	public void lamMoi() {
		gui.getTxtSdt().setText("");
		gui.getTxtTenKH().setText("");
		gui.getTxtTuoi().setText("");
		gui.getTxtTuoi().setEditable(true);
		gui.getTxtSoLuong().setText("");
		gui.getTxtTienNhan().setText("");
		tableModel.setRowCount(0);
		tinhTongTien();
	}

	// ========== XUẤT/LƯU HOÁ ĐƠN ==========
	public void xuLyXuatHoaDon() {
		try {
			// ==== 1. Kiểm tra dữ liệu ====
			if (tableModel.getRowCount() == 0) {
				tool.hienThiThongBao("Thông báo", "Chưa có thuốc trong hóa đơn!", false);
				return;
			}

			String tenKH = gui.getTxtTenKH().getText().trim();
			String sdt = gui.getTxtSdt().getText().trim();
			String tuoiText = gui.getTxtTuoi().getText().trim();
			String hinhThucTT = (String) gui.getCmbHTThanhToan().getSelectedItem();

			if (tenKH.isEmpty() || sdt.isEmpty()) {
				tool.hienThiThongBao("Thông báo", "Vui lòng nhập đầy đủ thông tin khách hàng!", false);
				return;
			}

			int tuoi;
			try {
				tuoi = Integer.parseInt(tuoiText);
				if (tuoi <= 0)
					throw new NumberFormatException();
			} catch (NumberFormatException e) {
				tool.hienThiThongBao("Lỗi nhập liệu", "Tuổi không hợp lệ!", false);
				return;
			}

			// ==== 2. Xử lý khách hàng ====
			KhachHangDAO khDAO = new KhachHangDAO();
			String sdtChuan = tool.chuyenSoDienThoai(sdt);
			KhachHang kh = khDAO.timMaKhachHangTheoSDT(sdtChuan);
			String maKH;

			if (kh == null) {
				maKH = tool.taoKhoaChinh("KH");
				boolean themKH = khDAO.themKhachHangMoi(maKH, tenKH, sdtChuan, tuoi);
				if (!themKH) {
					tool.hienThiThongBao("Lỗi", "Không thể thêm khách hàng mới!", false);
					return;
				}
			} else {
				maKH = kh.getMaKH();
			}

			// ==== 3. Tạo hóa đơn ====
			String maHD = tool.taoKhoaChinh("HD");
			String maNV;
			if(trangChuNV != null) {
				NhanVien nv = trangChuNV.layNhanVien();
				maNV = nv.getMaNV();
			}
			else {
				NhanVien nv = trangChuQL.layNhanVien();
				maNV = nv.getMaNV();
			}
			String diaChiHT = "456 Nguyễn Huệ, TP.HCM";
			String tenHT = "Hiệu Thuốc Tâm Thanh";
			String hotline = "+84-912345689";

			double tienNhan = 0;
			try {
				if (!gui.getTxtTienNhan().getText().trim().isEmpty()) {
					tienNhan = Double.parseDouble(gui.getTxtTienNhan().getText().trim().replace(",", ""));
				}
			} catch (NumberFormatException e) {
				tool.hienThiThongBao("Lỗi nhập liệu", "Số tiền nhận không hợp lệ!", false);
				return;
			}
			KhachHang khHD = khDAO.timKhachHangTheoMa(maKH);
			NhanVien nv = nvDAO.timNhanVienTheoMa(maNV);

			HoaDon hd = new HoaDon(maHD, khHD, nv, hinhThucTT, LocalDate.now(), diaChiHT, tenHT, "", hotline, tienNhan,
					true);

			if (!hdDAO.themHoaDon(hd)) {
				tool.hienThiThongBao("Lỗi", "Không thể tạo hóa đơn!", false);
				return;
			}

			// ==== 4. Thêm chi tiết hóa đơn ====
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				String tenThuoc = tableModel.getValueAt(i, 1).toString();
				Thuoc t = dsThuoc.stream().filter(x -> x.getTenThuoc().equals(tenThuoc)).findFirst().orElse(null);

				if (t == null)
					continue;

				int soLuong = Integer.parseInt(tableModel.getValueAt(i, 3).toString());
				double donGia = tool.chuyenTienSangSo(tableModel.getValueAt(i, 5).toString());

				String tenDVT = (String) gui.getCmbDonVi().getSelectedItem();
				String maDVT = dvtDAO.timMaDVTTheoTen(tenDVT); // hoặc lấy từ DAO nếu bạn có danh sách DVT

				hdDAO.themChiTietHoaDon(maHD, t.getMaThuoc(), soLuong, maDVT, donGia);
				thuocDAO.giamSoLuongTon(t.getMaThuoc(), maDVT, soLuong);
			}

			// ==== 5. Thông báo thành công & làm mới giao diện ====
			tool.hienThiThongBao("Thành công", "Xuất hóa đơn thành công!", true);
			tableModel.setRowCount(0);
			lamMoi();

		} catch (Exception e) {
			e.printStackTrace();
			tool.hienThiThongBao("Lỗi", "Đã xảy ra lỗi khi xuất hóa đơn!", false);
		}
	}

}