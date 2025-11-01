package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import controller.ToolCtrl;

public class TimKiemKH_GUI extends JPanel {

	private JTextField txtTenKH;
	private JTextField txtSdt;
	private JTable tblKhachHang;
	private JButton btnXemChiTiet;
	private JButton btnLamMoi;
	private JButton btnLichSuXoa;

	private final ToolCtrl tool = new ToolCtrl();

	public TimKiemKH_GUI() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// ===== TOP PANEL =====
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(Color.WHITE);
		topPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 10, 30));

		JPanel mainBox = new JPanel();
		mainBox.setLayout(new BoxLayout(mainBox, BoxLayout.X_AXIS));
		mainBox.setBackground(Color.WHITE);

		// LEFT: Filters
		JPanel leftVBox = new JPanel();
		leftVBox.setLayout(new BoxLayout(leftVBox, BoxLayout.Y_AXIS));
		leftVBox.setBackground(Color.WHITE);
		leftVBox.setPreferredSize(new Dimension(480, 120));

		txtTenKH = tool.taoTextField("Nhập tên...");
		txtSdt = tool.taoTextField("Số điện thoại...");

		leftVBox.add(taoDong("Tên khách hàng:", txtTenKH, 150, 255));
		leftVBox.add(Box.createVerticalStrut(15));
		leftVBox.add(taoDong("Số điện thoại:", txtSdt, 150, 255));

		// ===== RIGHT: Buttons =====
		JPanel rightVBox = new JPanel();
		rightVBox.setLayout(new BoxLayout(rightVBox, BoxLayout.Y_AXIS));
		rightVBox.setBackground(Color.WHITE);
		rightVBox.setPreferredSize(new Dimension(500, 120));

		// Row 1: Xem chi tiết + Làm mới
		JPanel btnRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		btnRow1.setBackground(Color.WHITE);

		btnXemChiTiet = tool.taoButton("Xem chi tiết", "/picture/khachHang/find.png");
		btnLamMoi = tool.taoButton("Làm mới", "/picture/khachHang/refresh.png");

		btnRow1.add(btnXemChiTiet);
		btnRow1.add(btnLamMoi);

		// Row 2: Lịch sử xóa (căn trái giống btnRow1)
		JPanel btnRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		btnRow2.setBackground(Color.WHITE);

		btnLichSuXoa = tool.taoButton("Lịch sử xóa", "/picture/khachHang/document.png");
		btnRow2.add(btnLichSuXoa);

		rightVBox.add(btnRow1);
		rightVBox.add(Box.createVerticalStrut(10));
		rightVBox.add(btnRow2);

		mainBox.add(leftVBox);
		mainBox.add(Box.createHorizontalStrut(100));
		mainBox.add(rightVBox);

		topPanel.add(mainBox, BorderLayout.CENTER);
		add(topPanel, BorderLayout.NORTH);

		// ====================== CENTER: Table ======================
		String[] cols = { "Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Tuổi", "Hoạt động" };
		DefaultTableModel model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		tblKhachHang = new JTable(model);
		tblKhachHang.setRowHeight(38);
		tblKhachHang.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tblKhachHang.setSelectionBackground(new Color(0xE3F2FD));
		tblKhachHang.setGridColor(new Color(0xDDDDDD));
		tblKhachHang.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		// 🌿 Nền trắng cell + chữ đen
		tblKhachHang.setBackground(Color.WHITE);
		tblKhachHang.setForeground(new Color(0x33, 0x33, 0x33));

		// 🌿 Header trắng đồng bộ
		JTableHeader header = tblKhachHang.getTableHeader();
		header.setFont(new Font("Times New Roman", Font.BOLD, 14));
		header.setBackground(Color.WHITE);
		header.setForeground(new Color(0x33, 0x33, 0x33));
		header.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));

		// 🌿 Column widths (có thể chỉnh theo nhu cầu)
		tblKhachHang.getColumnModel().getColumn(0).setPreferredWidth(130);
		tblKhachHang.getColumnModel().getColumn(1).setPreferredWidth(152);
		tblKhachHang.getColumnModel().getColumn(2).setPreferredWidth(171);
		tblKhachHang.getColumnModel().getColumn(3).setPreferredWidth(84);
		tblKhachHang.getColumnModel().getColumn(4).setPreferredWidth(125);

		// 🌿 Scroll trắng hoàn toàn
		JScrollPane scrollTable = new JScrollPane(tblKhachHang);
		scrollTable.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
		scrollTable.getViewport().setBackground(Color.WHITE);
		scrollTable.setBackground(Color.WHITE);

		add(scrollTable, BorderLayout.CENTER);

		// ===== EVENTS =====
		btnXemChiTiet.addActionListener(e -> onBtnXemChiTiet());
		btnLamMoi.addActionListener(e -> onBtnLamMoi());
		btnLichSuXoa.addActionListener(e -> onBtnLichSuXoa());
	}

	// ===== Hàm taoDong label + component đẹp =====
	private JPanel taoDong(String text, JComponent comp, int labelWidth, int fieldWidth) {
		JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		row.setBackground(Color.WHITE);

		if (!text.isEmpty()) {
			JLabel lbl = tool.taoLabel(text);
			lbl.setPreferredSize(new Dimension(labelWidth, 25));
			row.add(lbl);
		}

		if (comp != null) {
			comp.setPreferredSize(new Dimension(fieldWidth, comp.getPreferredSize().height));
			row.add(comp);
		}

		return row;
	}

	// ===== EVENT HANDLERS =====
	public void onBtnXemChiTiet() {
		/* TODO */ }

	public void onBtnLamMoi() {
		txtTenKH.setText("");
		txtSdt.setText("");
	}

	public void onBtnLichSuXoa() {
		/* TODO */ }

}
