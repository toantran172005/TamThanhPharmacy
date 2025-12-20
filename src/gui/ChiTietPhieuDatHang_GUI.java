package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controller.ChiTietPDHCtrl;
import controller.ToolCtrl;
import dao.DonViTinhDAO;
import dao.KhuyenMaiDAO;
import dao.PhieuDatHangDAO;
import dao.ThuocDAO;
import entity.DonViTinh;
import entity.KhuyenMai;
import entity.PhieuDatHang;
import entity.Thuoc;

import java.awt.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;

public class ChiTietPhieuDatHang_GUI extends JPanel {

	public JLabel lblDiaChi, lblHotline, lblMaPhieu, lblNgayDat, lblNgayHen, lblNhanVien, lblKhachHang;
	public JTable tblThuoc;
	public JTextArea txaGhiChu;
	public DefaultTableModel model;
	public JComboBox<String> cmbTrangThai;
	public JButton btnTaoHD, btnCapNhat, btnQuayLai;
	public TrangChuQL_GUI mainFrameQL;
	public TrangChuNV_GUI mainFrameNV;
	public PhieuDatHangDAO pdhDAO = new PhieuDatHangDAO();
	public KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
	public DonViTinhDAO dvtDAO = new DonViTinhDAO();
	Font font1 = new Font("Time New Roman", Font.BOLD, 18);
	Font font2 = new Font("Time New Roman", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();
	public PhieuDatHang pdh;
	public ChiTietPDHCtrl ctpdhCtrl = new ChiTietPDHCtrl(this);
	public ThuocDAO thDAO = new ThuocDAO();
	public JComboBox<String> cmbPTThanhToan;
	public JLabel lblTongTien;
	public JTextField txtTienNhan;
	public JLabel lblTienThua;

	public ChiTietPhieuDatHang_GUI(TrangChuQL_GUI mainFrame, PhieuDatHang pdh) {
		this.mainFrameQL = mainFrame;
		this.pdh = pdh;
		initUI();
		hienThiThongTin(pdh);
		choPhepCapNhap();
		setHoatDong();
	}

	public ChiTietPhieuDatHang_GUI(TrangChuNV_GUI mainFrame, PhieuDatHang pdh) {
		this.mainFrameNV = mainFrame;
		this.pdh = pdh;
		initUI();
		hienThiThongTin(pdh);
		choPhepCapNhap();
		setHoatDong();
	}

	public void setHoatDong() {
		btnQuayLai.addActionListener(e -> ctpdhCtrl.quayLaiTrangDanhSach());
		btnCapNhat.addActionListener(e -> ctpdhCtrl.capNhatPDH());
		btnTaoHD.addActionListener(e -> ctpdhCtrl.taoHoaDon());
	}

	public void choPhepCapNhap() {
		if (btnCapNhat.getText().trim().equals("Cập nhật")) {
			cmbTrangThai.setEnabled(false);
			txaGhiChu.setEditable(false);
		} else {
			cmbTrangThai.setEnabled(true);
			txaGhiChu.setEditable(true);
		}
	}

	public void hienThiThongTin(PhieuDatHang pdh) {
		if (pdh == null)
			return;

		lblMaPhieu.setText(pdh.getMaPDH());
		lblNgayDat.setText(tool.dinhDangLocalDate(pdh.getNgayDat()));
		lblNgayHen.setText(tool.dinhDangLocalDate(pdh.getNgayHen()));
		lblNhanVien.setText(pdh.getNhanVien() != null ? pdh.getNhanVien().getTenNV() : "");
		lblKhachHang.setText(pdh.getKhachHang() != null ? pdh.getKhachHang().getTenKH() : "");
		txaGhiChu.setText(pdh.getGhiChu());
		lblDiaChi.setText(pdh.getDiaChiHT());
		lblHotline.setText(tool.chuyenSoDienThoai(pdh.getHotline()));
		cmbTrangThai.setSelectedItem(pdh.getTrangThai());

		DefaultTableModel modelThuoc = (DefaultTableModel) tblThuoc.getModel();
		modelThuoc.setRowCount(0);

		ArrayList<Object[]> dsThuoc = pdhDAO.layDanhSachThuocTheoPDH(pdh.getMaPDH());

		if (dsThuoc == null || dsThuoc.isEmpty()) {
			tool.hienThiThongBao("Thông báo", "Không có chi tiết thuốc cho phiếu này!", false);
			return;
		}

		for (Object[] row : pdhDAO.layDanhSachThuocTheoPDH(pdh.getMaPDH())) {

			String tenThuoc = row[2].toString();
			Thuoc thuoc = thDAO.timThuocTheoMa(thDAO.layMaThuocTheoTen(tenThuoc));

			String noiSanXuat = thDAO.timTenQGTheoMaThuoc(row[1].toString());
			int soLuong = (int) row[3];
			String tenDVT = row[5].toString();
			double donGia = (double) row[6];

			double thanhTien = (double) row[7];

			modelThuoc.addRow(new Object[] { tenThuoc, noiSanXuat, soLuong, tenDVT, tool.dinhDangVND(donGia),
					tool.dinhDangVND(thanhTien) });
		}

	}

	public static String boDau(String s) {
		if (s == null)
			return "";
		return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
				.toLowerCase().trim();
	}

//	public double layThanhTien(Thuoc thuoc, int sl) {
//
//		if (thuoc == null || sl <= 0)
//			return 0;
//
//		double donGia = thuoc.getGiaBan();
//		double giaGoc = donGia * sl;
//
//		String maKM = thDAO.layMaKMTheoMaThuoc(thuoc.getMaThuoc());
//		if (maKM == null || maKM.isEmpty())
//			return giaGoc;
//
//		KhuyenMai km = kmDAO.layKhuyenMaiTheoMa(maKM);
//		if (km == null)
//			return giaGoc;
//
//		if (!km.isTrangThai())
//			return giaGoc;
//
//		LocalDate homNay = LocalDate.now();
//		if (homNay.isBefore(km.getNgayBD()) || homNay.isAfter(km.getNgayKT()))
//			return giaGoc;
//
//		String loaiKM = boDau(km.getLoaiKM());
//		if (loaiKM.contains("giam")) {
//			return giaGoc * (1 - km.getMucKM() / 100.0);
//		}
//
//		return giaGoc;
//	}

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1058, 509));
		setBackground(Color.WHITE);

		// ===== TOP =====
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
		pnlTop.setBorder(new EmptyBorder(10, 0, 10, 0));
		pnlTop.setBackground(Color.WHITE);

		JLabel lblTieuDe = new JLabel("HIỆU THUỐC TAM THANH", SwingConstants.CENTER);
		lblTieuDe.setFont(font1);
		lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblTieuDe);

		pnlTop.add(Box.createVerticalStrut(10));
		JPanel pnlDiaChi = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		pnlDiaChi.setBackground(Color.WHITE);
		pnlDiaChi.add(tool.taoLabel("Địa chỉ: "));
		lblDiaChi = tool.taoLabel("");
		pnlDiaChi.add(lblDiaChi);
		pnlTop.add(pnlDiaChi);

		JPanel pnlHotline = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		pnlHotline.setBackground(Color.WHITE);
		pnlHotline.add(tool.taoLabel("Hotline: "));
		lblHotline = tool.taoLabel("");
		pnlHotline.add(lblHotline);
		pnlTop.add(pnlHotline);

		JLabel lblChiTiet = new JLabel("CHI TIẾT PHIẾU ĐẶT THUỐC", SwingConstants.CENTER);
		lblChiTiet.setFont(font1);
		lblChiTiet.setBorder(new EmptyBorder(10, 0, 10, 0));
		pnlTop.add(lblChiTiet);

		pnlTop.add(taoDongThongTin("Mã phiếu:", lblMaPhieu = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Ngày đặt:", lblNgayDat = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Ngày hẹn:", lblNgayHen = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Nhân viên:", lblNhanVien = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Khách hàng:", lblKhachHang = tool.taoLabel("")));

		add(pnlTop, BorderLayout.NORTH);

		// ===== CENTER =====
		JPanel pnlCenter = new JPanel();
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
		pnlCenter.setBorder(new EmptyBorder(10, 20, 10, 20));
		pnlCenter.setBackground(Color.WHITE);

		String[] columnNames = { "Tên thuốc", "Nơi sản xuất", "Số lượng", "Đơn vị", "Đơn giá", "Thành tiền" };
		model = new DefaultTableModel(columnNames, 0);
		tblThuoc = new JTable(model);
		tblThuoc.setRowHeight(28);
		tblThuoc.getTableHeader().setFont(font2);
		tblThuoc.setFont(font2);

		tblThuoc.setBackground(Color.WHITE);

		tblThuoc.getTableHeader().setBackground(new Color(240, 240, 240));
		tblThuoc.setGridColor(new Color(200, 200, 200));
		tblThuoc.setShowGrid(true);
		tblThuoc.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
		JScrollPane scrollPane = new JScrollPane(tblThuoc);
		scrollPane.getViewport().setBackground(Color.WHITE);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblThuoc.getColumnCount(); i++) {
			tblThuoc.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		JTableHeader header = tblThuoc.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(font2);
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		pnlCenter.add(scrollPane);

		pnlCenter.add(Box.createVerticalStrut(15));

		JPanel pnlThongTin = new JPanel(new BorderLayout(30, 0));
		pnlThongTin.setBackground(Color.WHITE);

		JPanel pnlLeft = new JPanel();
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
		pnlLeft.setBackground(Color.WHITE);

		pnlLeft.add(taoDongThongTin("Ghi chú:", txaGhiChu = tool.taoTextArea(30)));
		pnlLeft.add(Box.createVerticalStrut(10));

		JPanel trangThaiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		trangThaiPanel.setBackground(Color.WHITE);
		trangThaiPanel.add(tool.taoLabel("Trạng thái:"));
		cmbTrangThai = tool.taoComboBox(new String[] { "Đã giao", "Chờ hàng", "Đã hủy" });
		cmbTrangThai.setPreferredSize(new Dimension(150, 30));
		trangThaiPanel.add(cmbTrangThai);
		pnlLeft.add(trangThaiPanel);

		JPanel pnlRight = new JPanel();
		pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
		pnlRight.setBackground(Color.WHITE);

		JPanel row1 = taoHangTrai();
		row1.add(tool.taoLabel("Phương thức thanh toán:"));
		cmbPTThanhToan = tool.taoComboBox(new String[] { "Tiền mặt", "Chuyển khoản" });
		cmbPTThanhToan.setEditable(false);
		row1.add(cmbPTThanhToan);
		pnlRight.add(row1);

		JPanel row2 = taoHangTrai();
		row2.add(tool.taoLabel("Tổng tiền:"));
		lblTongTien = tool.taoLabel("0 VND");
		row2.add(lblTongTien);
		pnlRight.add(row2);

		JPanel row3 = taoHangTrai();
		row3.add(tool.taoLabel("Tiền nhận:"));
		txtTienNhan = tool.taoTextField("Tiền nhận...");
		row3.add(txtTienNhan);
		pnlRight.add(row3);

		JPanel row4 = taoHangTrai();
		row4.add(tool.taoLabel("Tiền thừa:"));
		lblTienThua = tool.taoLabel("0 VND");
		row4.add(lblTienThua);
		pnlRight.add(row4);

		pnlThongTin.add(pnlLeft, BorderLayout.CENTER);
		pnlThongTin.add(pnlRight, BorderLayout.EAST);

		pnlCenter.add(pnlThongTin);
		add(pnlCenter, BorderLayout.CENTER);

		// ===== BOTTOM =====
		JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
		pnlBottom.setBackground(Color.WHITE);

		btnTaoHD = tool.taoButton("Tạo hóa đơn", "/picture/hoaDon/print.png");
		btnCapNhat = tool.taoButton("Cập nhật", "/picture/hoaDon/edit.png");
		btnQuayLai = tool.taoButton("Quay lại", "/picture/hoaDon/signOut.png");

		pnlBottom.add(btnTaoHD);
		pnlBottom.add(btnCapNhat);
		pnlBottom.add(btnQuayLai);

		add(pnlBottom, BorderLayout.SOUTH);
	}

	public JPanel taoHangTrai() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		p.setBackground(Color.WHITE);
		return p;
	}

	public JPanel taoDongThongTin(String tieuDe, JComponent noiDung) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		panel.setBackground(Color.WHITE);

		JLabel lblTieuDe = tool.taoLabel(tieuDe);
		panel.add(lblTieuDe);

		if (noiDung instanceof JTextArea) {
			JTextArea txa = (JTextArea) noiDung;
			txa.setLineWrap(true);
			txa.setWrapStyleWord(true);
			JScrollPane scroll = new JScrollPane(txa);
			scroll.setPreferredSize(new Dimension(250, 60));
			panel.add(scroll);
		} else {
			panel.add(noiDung);
		}

		return panel;
	}
	
	public JLabel getLblMaPhieuDat() {
		return lblMaPhieu;
	}

	public TrangChuQL_GUI getMainFrameQL() {
		return mainFrameQL;
	}

	public TrangChuNV_GUI getMainFrameNV() {
		return mainFrameNV;
	}

}
