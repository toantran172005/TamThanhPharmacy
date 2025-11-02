package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controller.ToolCtrl;

import java.awt.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class ThongKeKhachHang_GUI extends JPanel {

	public ToolCtrl tool = new ToolCtrl();

	// TOP
	private JTextField txtNgayBD, txtNgayKT;
	private JButton btnThongKe;

	// TOTAL OVERVIEW
	private JLabel lblTongKH, lblTongSLM, lblTongCT, lblChiTieuTB;

	// CHART
	private JFreeChart barChart;
	private ChartPanel chartPanel;

	// TABLE
	private JTable tblThongKeKH;
	private JComboBox<String> cmbTop;

	// BOTTOM BUTTONS
	private JButton btnXuatExcel, btnLamMoi;

	public ThongKeKhachHang_GUI() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// ===== TOP PANEL =====
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(Color.WHITE);
		topPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

		JLabel lblTitle = tool.taoLabel("THỐNG KÊ KHÁCH HÀNG");
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblTitle);

		// Separator dưới tiêu đề
		topPanel.add(Box.createVerticalStrut(10));
		JSeparator sep1 = new JSeparator();
		sep1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
		topPanel.add(sep1);
		topPanel.add(Box.createVerticalStrut(10));

		// Date choosers + button
		JPanel datePanel = new JPanel();
		datePanel.setBackground(Color.WHITE);
		datePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));

		txtNgayBD = tool.taoTextField("Ngày bắt đầu...");
		txtNgayBD.setPreferredSize(new Dimension(160, 35));
		txtNgayKT = tool.taoTextField("Ngày kết thúc...");
		txtNgayKT.setPreferredSize(new Dimension(160, 35));

		btnThongKe = tool.taoButton("Thống kê", "/picture/khachHang/statistic.png");

		datePanel.add(tool.taoLabel("Từ ngày:"));
		datePanel.add(txtNgayBD);
		datePanel.add(tool.taoLabel("Đến ngày:"));
		datePanel.add(txtNgayKT);
		datePanel.add(btnThongKe);

		topPanel.add(datePanel);

		// Separator dưới date chooser
		topPanel.add(Box.createVerticalStrut(10));
		JSeparator sep2 = new JSeparator();
		sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
		topPanel.add(sep2);
		topPanel.add(Box.createVerticalStrut(10));

		add(topPanel, BorderLayout.NORTH);

		// ===== CENTER PANEL =====
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

		// --- Tổng quan ---
		JPanel pnlTongQuan = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
		pnlTongQuan.setBackground(Color.WHITE);

		lblTongKH = tool.taoLabel("0");
		lblTongSLM = tool.taoLabel("0");
		lblTongCT = tool.taoLabel("0 VND");
		lblChiTieuTB = tool.taoLabel("0 VND");

		pnlTongQuan.add(createInfoBox("Tổng khách hàng", lblTongKH));
		pnlTongQuan.add(createInfoBox("Tổng số lần mua", lblTongSLM));
		pnlTongQuan.add(createInfoBox("Tổng chi tiêu", lblTongCT));
		pnlTongQuan.add(createInfoBox("Chi tiêu TB/Khách", lblChiTieuTB));

		centerPanel.add(pnlTongQuan);

		// Separator giữa tổng quan và chart
		centerPanel.add(Box.createVerticalStrut(10));
		JSeparator sep3 = new JSeparator();
		sep3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
		centerPanel.add(sep3);
		centerPanel.add(Box.createVerticalStrut(10));

		// --- BarChart Top khách ---
		JPanel chartContainer = new JPanel(new BorderLayout());
		chartContainer.setBackground(Color.WHITE);
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		barChart = ChartFactory.createBarChart("Top Khách Hàng", "Tên khách hàng", "Tổng chi tiêu (VND)", dataset);
		chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new Dimension(1000, 300));
		chartContainer.add(chartPanel, BorderLayout.CENTER);
		centerPanel.add(chartContainer);

		// Separator giữa chart và bảng
		centerPanel.add(Box.createVerticalStrut(10));
		JSeparator sep4 = new JSeparator();
		sep4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
		centerPanel.add(sep4);
		centerPanel.add(Box.createVerticalStrut(10));

		// --- Bảng thống kê chi tiết ---
		JPanel tblPanel = new JPanel();
		tblPanel.setLayout(new BoxLayout(tblPanel, BoxLayout.Y_AXIS));
		tblPanel.setBackground(Color.WHITE);

		JPanel topFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		topFilter.setBackground(Color.WHITE);
		topFilter.add(tool.taoLabel("Thống kê theo top:"));
		cmbTop = tool.taoComboBox(new String[] { "5", "10", "20" });
		topFilter.add(cmbTop);

		tblPanel.add(topFilter);

		String[] cols = { "STT", "Mã KH", "Tên KH", "SĐT", "Số lần mua", "Tổng chi tiêu" };
		DefaultTableModel model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		tblThongKeKH = new JTable(model);
		tblThongKeKH.setRowHeight(38);
		tblThongKeKH.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tblThongKeKH.setGridColor(new Color(0xDDDDDD));
		tblThongKeKH.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblThongKeKH.setSelectionBackground(new Color(0xE3F2FD));

		// 🌿 Table trắng
		tblThongKeKH.setBackground(Color.WHITE);
		tblThongKeKH.setForeground(new Color(0x33, 0x33, 0x33));

		JTableHeader header = tblThongKeKH.getTableHeader();
		header.setBackground(Color.WHITE);
		header.setForeground(new Color(0x33, 0x33, 0x33));
		header.setFont(new Font("Times New Roman", Font.BOLD, 14));
		header.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));

		JScrollPane scrollTable = new JScrollPane(tblThongKeKH);
		scrollTable.setBackground(Color.WHITE);
		scrollTable.getViewport().setBackground(Color.WHITE);
		scrollTable.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));

		tblPanel.add(scrollTable);

		centerPanel.add(tblPanel);

		add(centerPanel, BorderLayout.CENTER);

		// ===== BOTTOM BUTTONS =====
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		bottomPanel.setBackground(Color.WHITE);

		btnXuatExcel = tool.taoButton("Xuất file", "/picture/khachHang/export.png");
		btnLamMoi = tool.taoButton("Làm mới", "/picture/khachHang/refresh.png");

		bottomPanel.add(btnXuatExcel);
		bottomPanel.add(btnLamMoi);

		add(bottomPanel, BorderLayout.SOUTH);
	}

	// === Hỗ trợ tạo info box cho tổng quan ===
	private JPanel createInfoBox(String title, JLabel value) {
		JPanel pnl = new JPanel();
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
		pnl.setBackground(Color.WHITE);
		JLabel lblTitle = tool.taoLabel(title);
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 15));
		value.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		value.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnl.add(lblTitle);
		pnl.add(value);
		return pnl;
	}

}
