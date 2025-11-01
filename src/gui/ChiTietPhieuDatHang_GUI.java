package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.ToolCtrl;

import java.awt.*;

public class ChiTietPhieuDatHang_GUI extends JPanel {

	private JLabel lblDiaChi, lblHotline, lblMaPhieu, lblNgayDat, lblNgayHen, lblNhanVien, lblKhachHang, lblGhiChu;
	private JTable tblThuoc;
	private JComboBox<String> cmbTrangThai;
	private JButton btnInPhieu, btnCapNhat, btnQuayLai;
	private TrangChuQL_GUI mainFrame;
	Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

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
		lblTieuDe.setFont(font1);
		lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblTieuDe);

		lblDiaChi = new JLabel("Địa chỉ:", SwingConstants.CENTER);
		lblDiaChi.setFont(font2);
		lblDiaChi.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblDiaChi);

		lblHotline = new JLabel("Hotline:", SwingConstants.CENTER);
		lblHotline.setFont(font2);
		lblHotline.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblHotline);

		JLabel lblChiTiet = new JLabel("CHI TIẾT PHIẾU ĐẶT THUỐC", SwingConstants.CENTER);
		lblChiTiet.setFont(font1);
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
		tblThuoc.getTableHeader().setFont(font2);
		JScrollPane scrollPane = new JScrollPane(tblThuoc);
		centerPanel.add(scrollPane);

		// Ghi chú
		centerPanel.add(Box.createVerticalStrut(15));
		centerPanel.add(taoDongThongTin("Ghi chú:", lblGhiChu = new JLabel("Không có")));

		// Trạng thái
		centerPanel.add(Box.createVerticalStrut(10));
		JPanel trangThaiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		JLabel lblTrangThai = new JLabel("Trạng thái:");
		lblTrangThai.setFont(font2);
		cmbTrangThai = cmbTrangThai = tool.taoComboBox(new String[] {"Chờ hàng", "Đã giao", "Đã hủy"});
		cmbTrangThai.setPreferredSize(new Dimension(150, 30));
		trangThaiPanel.add(lblTrangThai);
		trangThaiPanel.add(cmbTrangThai);
		centerPanel.add(trangThaiPanel);

		add(centerPanel, BorderLayout.CENTER);

		// ========== BOTTOM ==========
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));

		btnInPhieu = tool.taoButton("In phiếu", "/picture/hoaDon/print.png");
		btnCapNhat = tool.taoButton("Cập nhật", "/picture/hoaDon/edit.png");
		btnQuayLai = tool.taoButton("Quay lại", "/picture/hoaDon/signOut.png");

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
		lblTieuDe.setFont(font2);
		lblNoiDung.setFont(font2);
		panel.add(lblTieuDe);
		panel.add(lblNoiDung);
		return panel;
	}

}
