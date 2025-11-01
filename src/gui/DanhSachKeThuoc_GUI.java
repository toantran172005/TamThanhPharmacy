package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controller.ToolCtrl;
import java.awt.*;

public class DanhSachKeThuoc_GUI extends JPanel {
	
	private JComboBox<String> cmbSucChua;
	private JComboBox<String> cmbLoaiKe;
	private JButton btnXemCT;
	private JButton btnLamMoi;
	private JButton btnLichSuXoa;
	private JTable tblKeThuoc;

	public ToolCtrl tool = new ToolCtrl();

	public DanhSachKeThuoc_GUI() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE); // toàn bộ panel chính màu trắng

		// ====================== TOP PANEL ======================
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));
		topPanel.setBackground(Color.WHITE);

		// ---- LEFT: Filters ----
		JPanel leftVBox = new JPanel();
		leftVBox.setLayout(new BoxLayout(leftVBox, BoxLayout.Y_AXIS));
		leftVBox.setBackground(Color.WHITE);

		// Row 1: Sức chứa
		JPanel rowSucChua = new JPanel();
		rowSucChua.setLayout(new BoxLayout(rowSucChua, BoxLayout.X_AXIS));
		rowSucChua.setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
		rowSucChua.setBackground(Color.WHITE);

		JLabel lblSucChua = tool.taoLabel("Sức chứa:");
		lblSucChua.setPreferredSize(new Dimension(120, 30));

		cmbSucChua = tool.taoComboBox(new String[] { "Tất cả" });
		cmbSucChua.setBackground(Color.WHITE);

		rowSucChua.add(Box.createHorizontalStrut(15));
		rowSucChua.add(lblSucChua);
		rowSucChua.add(Box.createHorizontalStrut(12));
		rowSucChua.add(cmbSucChua);
		rowSucChua.add(Box.createHorizontalGlue());

		// Row 2: Loại kệ
		JPanel rowLoaiKe = new JPanel();
		rowLoaiKe.setLayout(new BoxLayout(rowLoaiKe, BoxLayout.X_AXIS));
		rowLoaiKe.setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
		rowLoaiKe.setBackground(Color.WHITE);

		JLabel lblLoaiKe = tool.taoLabel("Loại kệ:");
		lblLoaiKe.setPreferredSize(new Dimension(120, 30));

		cmbLoaiKe = tool.taoComboBox(new String[] { "Tất cả" });
		cmbLoaiKe.setBackground(Color.WHITE);

		rowLoaiKe.add(Box.createHorizontalStrut(15));
		rowLoaiKe.add(lblLoaiKe);
		rowLoaiKe.add(Box.createHorizontalStrut(12));
		rowLoaiKe.add(cmbLoaiKe);
		rowLoaiKe.add(Box.createHorizontalGlue());

		leftVBox.add(rowSucChua);
		leftVBox.add(Box.createVerticalStrut(12));
		leftVBox.add(rowLoaiKe);

		// ---- RIGHT: Buttons ----
		JPanel rightVBox = new JPanel();
		rightVBox.setLayout(new BoxLayout(rightVBox, BoxLayout.Y_AXIS));
		rightVBox.setBackground(Color.WHITE);

		JPanel btnRow1 = new JPanel();
		btnRow1.setLayout(new BoxLayout(btnRow1, BoxLayout.X_AXIS));
		btnRow1.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
		btnRow1.setBackground(Color.WHITE);

		btnXemCT = tool.taoButton("Xem chi tiết", "/picture/keThuoc/find.png");
		btnLamMoi = tool.taoButton("Làm mới", "/picture/keThuoc/refresh.png");

		btnRow1.add(Box.createHorizontalStrut(30));
		btnRow1.add(btnXemCT);
		btnRow1.add(Box.createHorizontalStrut(25));
		btnRow1.add(btnLamMoi);
		btnRow1.add(Box.createHorizontalGlue());

		JPanel btnRow2 = new JPanel();
		btnRow2.setLayout(new BoxLayout(btnRow2, BoxLayout.X_AXIS));
		btnRow2.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
		btnRow2.setBackground(Color.WHITE);

		btnLichSuXoa = tool.taoButton("Lịch sử xóa", "/picture/khachHang/document.png");

		btnRow2.add(Box.createHorizontalStrut(30));
		btnRow2.add(btnLichSuXoa);
		btnRow2.add(Box.createHorizontalStrut(25));

		rightVBox.add(btnRow1);
		rightVBox.add(Box.createVerticalStrut(10));
		rightVBox.add(btnRow2);

		topPanel.add(leftVBox, BorderLayout.WEST);
		topPanel.add(rightVBox, BorderLayout.EAST);

		// ====================== CENTER: TALL TABLE ======================
		String[] cols = { "Mã kệ", "Loại kệ", "Sức chứa", "Mô tả", "Trạng thái", "Hoạt động" };
		DefaultTableModel model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		tblKeThuoc = new JTable(model);
		tblKeThuoc.setRowHeight(40);
		tblKeThuoc.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tblKeThuoc.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));
		tblKeThuoc.setSelectionBackground(new Color(0xE3F2FD));
		tblKeThuoc.setGridColor(new Color(0xDDDDDD));
		tblKeThuoc.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		// 🌿 Làm trắng toàn bộ table
		tblKeThuoc.setBackground(Color.WHITE);
		tblKeThuoc.setForeground(new Color(0x33, 0x33, 0x33));

		// 🌿 Header trắng đồng bộ
		JTableHeader header = tblKeThuoc.getTableHeader();
		header.setBackground(Color.WHITE);
		header.setForeground(new Color(0x33, 0x33, 0x33));
		header.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));

		// Column widths
		tblKeThuoc.getColumnModel().getColumn(0).setPreferredWidth(127);
		tblKeThuoc.getColumnModel().getColumn(1).setPreferredWidth(180);
		tblKeThuoc.getColumnModel().getColumn(2).setPreferredWidth(92);
		tblKeThuoc.getColumnModel().getColumn(3).setPreferredWidth(185);
		tblKeThuoc.getColumnModel().getColumn(4).setPreferredWidth(138);
		tblKeThuoc.getColumnModel().getColumn(5).setPreferredWidth(104);

		// 🌿 Scroll trắng hoàn toàn
		JScrollPane scrollPane = new JScrollPane(tblKeThuoc);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setBackground(Color.WHITE);

		// ====================== FINAL LAYOUT ======================
		add(topPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

		// ====================== EVENTS ======================
		btnXemCT.addActionListener(this::onBtnXemCT);
		btnLamMoi.addActionListener(this::onBtnLamMoi);
		btnLichSuXoa.addActionListener(this::onBtnLichSuXoa);
	}

	// === Event stubs ===
	public void onBtnXemCT(java.awt.event.ActionEvent e) {
		/* TODO */ }

	public void onBtnLamMoi(java.awt.event.ActionEvent e) {
		/* TODO */ }

	public void onBtnLichSuXoa(java.awt.event.ActionEvent e) {
		/* TODO */ }
}
