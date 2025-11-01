package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import controller.ToolCtrl;
import java.awt.*;

/**
 * Giao diện: Danh sách Khiếu nại & Hỗ trợ khách hàng
 * Bố cục: 2 hàng filter + hàng nút nằm dưới 2 comboBox
 */
public class DanhSachKhieuNaiVaHoTroHK_GUI extends JPanel {

	private JTextField txtTenKH, txtTenNV;
	private JComboBox<String> cmbLoaiDon, cmbTrangThai;
	private JButton btnXemCT, btnLamMoi;
	private JTable tblKNHT;
	public ToolCtrl tool = new ToolCtrl();

	public DanhSachKhieuNaiVaHoTroHK_GUI() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// ====================== TOP ======================
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
		topPanel.setBackground(Color.WHITE);

		// === TITLE ===
		JLabel lblTitle = new JLabel("DANH SÁCH KHIẾU NẠI VÀ HỖ TRỢ KHÁCH HÀNG", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblTitle);
		topPanel.add(Box.createVerticalStrut(15));

		// === FILTERS ===
		JPanel filtersPanel = new JPanel();
		filtersPanel.setLayout(new GridLayout(2, 2, 40, 15)); // 2 hàng 2 cột
		filtersPanel.setBackground(Color.WHITE);

		// Tên KH
		JLabel lblTenKH = tool.taoLabel("Tên khách hàng:");
		txtTenKH = tool.taoTextField("Nhập tên khách hàng...");
		JPanel p1 = taoDong(lblTenKH, txtTenKH);

		// Loại đơn
		JLabel lblLoaiDon = tool.taoLabel("Loại đơn:");
		cmbLoaiDon = tool.taoComboBox(new String[] { "Tất cả" });
		JPanel p2 = taoDong(lblLoaiDon, cmbLoaiDon);

		// Tên NV
		JLabel lblTenNV = tool.taoLabel("Tên nhân viên:");
		txtTenNV = tool.taoTextField("Nhập tên nhân viên...");
		JPanel p3 = taoDong(lblTenNV, txtTenNV);

		// Trạng thái
		JLabel lblTrangThai = tool.taoLabel("Trạng thái:");
		cmbTrangThai = tool.taoComboBox(new String[] { "Tất cả", "Chờ xử lý", "Đã xử lý" });
		JPanel p4 = taoDong(lblTrangThai, cmbTrangThai);

		filtersPanel.add(p1);
		filtersPanel.add(p2);
		filtersPanel.add(p3);
		filtersPanel.add(p4);

		topPanel.add(filtersPanel);
		topPanel.add(Box.createVerticalStrut(10));

		// === BUTTONS (nằm dưới 2 comboBox, căn giữa) ===
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
		btnPanel.setBackground(Color.WHITE);

		btnXemCT = tool.taoButton("Xem chi tiết", "/picture/khachHang/find.png");
		btnLamMoi = tool.taoButton("Làm mới", "/picture/khachHang/return.png");

		btnPanel.add(btnXemCT);
		btnPanel.add(btnLamMoi);

		topPanel.add(btnPanel);

		// ====================== TABLE ======================
		String[] cols = { "Mã phiếu", "Tên khách hàng", "Tên nhân viên", "Loại đơn", "Ngày tạo", "Trạng thái" };
		DefaultTableModel model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		tblKNHT = new JTable(model);
		tblKNHT.setRowHeight(38);
		tblKNHT.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tblKNHT.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));
		tblKNHT.setSelectionBackground(new Color(0xE3F2FD));
		tblKNHT.setGridColor(new Color(0xDDDDDD));
		tblKNHT.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		// Nền trắng
		tblKNHT.setBackground(Color.WHITE);
		tblKNHT.setForeground(new Color(0x33, 0x33, 0x33));

		JTableHeader header = tblKNHT.getTableHeader();
		header.setBackground(Color.WHITE);
		header.setForeground(new Color(0x33, 0x33, 0x33));
		header.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));

		JScrollPane scrollTable = new JScrollPane(tblKNHT);
		scrollTable.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
		scrollTable.getViewport().setBackground(Color.WHITE);
		scrollTable.setBackground(Color.WHITE);

		// ====================== LAYOUT ======================
		add(topPanel, BorderLayout.NORTH);
		add(scrollTable, BorderLayout.CENTER);

		// Events
		btnXemCT.addActionListener(e -> onBtnXemCT());
		btnLamMoi.addActionListener(e -> onBtnLamMoi());
	}

	// === HÀM TẠO DÒNG LABEL + COMPONENT ===
	private JPanel taoDong(JLabel lbl, JComponent comp) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
		p.setBackground(Color.WHITE);
		lbl.setPreferredSize(new Dimension(120, 30));
		comp.setPreferredSize(new Dimension(220, 32));
		p.add(lbl);
		p.add(comp);
		return p;
	}

	public void onBtnXemCT() { /* TODO */ }

	public void onBtnLamMoi() { /* TODO */ }
}
