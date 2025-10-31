package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ChiTietPhieuDatHang_GUI extends JPanel {

	private JLabel lblDiaChi, lblHotline, lblMaPhieu, lblNgayDat, lblNgayHen, lblNhanVien, lblKhachHang, lblGhiChu;
	private JTable tblThuoc;
	private JComboBox<String> cmbTrangThai;
	private JButton btnInPhieu, btnCapNhat, btnQuayLai;
	private TrangChuQL_GUI mainFrame;

	public ChiTietPhieuDatHang_GUI(TrangChuQL_GUI mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1058, 509));
		setBackground(Color.WHITE);

		// ========== TOP ==========
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

		JLabel lblTieuDe = new JLabel("HIỆU THUỐC TAM THANH", SwingConstants.CENTER);
		lblTieuDe.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 18));
		lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblTieuDe);

		lblDiaChi = new JLabel("Địa chỉ:", SwingConstants.CENTER);
		lblDiaChi.setFont(new Font("SansSerif", Font.PLAIN, 15));
		lblDiaChi.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblDiaChi);

		lblHotline = new JLabel("Hotline:", SwingConstants.CENTER);
		lblHotline.setFont(new Font("SansSerif", Font.PLAIN, 15));
		lblHotline.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblHotline);

		JLabel lblChiTiet = new JLabel("CHI TIẾT PHIẾU ĐẶT THUỐC", SwingConstants.CENTER);
		lblChiTiet.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblChiTiet.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblChiTiet.setBorder(new EmptyBorder(10, 0, 10, 0));
		topPanel.add(lblChiTiet);

		// Thông tin phiếu
		topPanel.add(taoDongThongTin("Mã phiếu:", lblMaPhieu = new JLabel("")));
		topPanel.add(taoDongThongTin("Ngày đặt:", lblNgayDat = new JLabel("")));
		topPanel.add(taoDongThongTin("Ngày hẹn:", lblNgayHen = new JLabel("")));
		topPanel.add(taoDongThongTin("Nhân viên:", lblNhanVien = new JLabel("")));
		topPanel.add(taoDongThongTin("Khách hàng:", lblKhachHang = new JLabel("")));

		add(topPanel, BorderLayout.NORTH);

		// ========== CENTER ==========
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

		// Bảng thuốc
		String[] columns = { "Tên thuốc", "Số lượng", "Đơn vị", "Đơn giá", "Thành tiền" };
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		tblThuoc = new JTable(model);
		tblThuoc.setRowHeight(25);
		tblThuoc.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
		JScrollPane scrollPane = new JScrollPane(tblThuoc);
		centerPanel.add(scrollPane);

		// Ghi chú
		centerPanel.add(Box.createVerticalStrut(15));
		centerPanel.add(taoDongThongTin("Ghi chú:", lblGhiChu = new JLabel("Không có")));

		// Trạng thái
		centerPanel.add(Box.createVerticalStrut(10));
		JPanel trangThaiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		JLabel lblTrangThai = new JLabel("Trạng thái:");
		lblTrangThai.setFont(new Font("SansSerif", Font.BOLD, 15));
		cmbTrangThai = new JComboBox<>(new String[] { "Đang xử lý", "Hoàn thành", "Hủy" });
		cmbTrangThai.setPreferredSize(new Dimension(150, 30));
		trangThaiPanel.add(lblTrangThai);
		trangThaiPanel.add(cmbTrangThai);
		centerPanel.add(trangThaiPanel);

		add(centerPanel, BorderLayout.CENTER);

		// ========== BOTTOM ==========
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));

		btnInPhieu = taoButton("In phiếu", "/img/print.png");
		btnCapNhat = taoButton("Cập nhật", "/img/edit.png");
		btnQuayLai = taoButton("Quay lại", "/img/signOut.png");

		Font btnFont = new Font("SansSerif", Font.BOLD, 15);
		btnInPhieu.setFont(btnFont);
		btnCapNhat.setFont(btnFont);
		btnQuayLai.setFont(btnFont);

		bottomPanel.add(btnInPhieu);
		bottomPanel.add(btnCapNhat);
		bottomPanel.add(btnQuayLai);

		add(bottomPanel, BorderLayout.SOUTH);
		
		btnQuayLai.addActionListener(e -> {
            mainFrame.setUpNoiDung(new TimKiemPhieuDatHang_GUI(mainFrame));
        });
	}

	// Hàm tạo 1 dòng thông tin
	private JPanel taoDongThongTin(String tieuDe, JLabel lblNoiDung) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		JLabel lblTieuDe = new JLabel(tieuDe);
		lblTieuDe.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblNoiDung.setFont(new Font("SansSerif", Font.PLAIN, 15));
		panel.add(lblTieuDe);
		panel.add(lblNoiDung);
		return panel;
	}

	// ====== HÀM TẠO NÚT VỚI ẢNH ======
	private JButton taoButton(String text, String imgPath) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("SansSerif", Font.BOLD, 15));
		btn.setBackground(new Color(245, 245, 245));
		btn.setFocusPainted(false);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		try {
			ImageIcon icon = new ImageIcon(getClass().getResource(imgPath));
			Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
			btn.setIcon(new ImageIcon(scaled));
		} catch (Exception e) {
			System.err.println("Không tìm thấy ảnh: " + imgPath);
		}

		return btn;
	}

}
