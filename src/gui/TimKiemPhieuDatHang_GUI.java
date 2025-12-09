package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controller.TimKiemPhieuDatHangCtrl;
import controller.ToolCtrl;
import dao.PhieuDatHangDAO;
import entity.PhieuDatHang;

import java.awt.*;
import java.util.ArrayList;

public class TimKiemPhieuDatHang_GUI extends JPanel {

	public JTable tblPhieuDatHang;
	public JTextField txtTenKH, txtTenNV;
	public JComboBox<String> cmbTrangThai;
	public JButton btnTimKiem, btnChiTiet, btnLamMoi;
	public TrangChuQL_GUI mainFrameQL;
	public TrangChuNV_GUI mainFrameNV;
	Font font1 = new Font("Time New Roman", Font.BOLD, 18);
	Font font2 = new Font("Time New Roman", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();
	public TimKiemPhieuDatHangCtrl dspdhCtrl = new TimKiemPhieuDatHangCtrl(this);

	public TimKiemPhieuDatHang_GUI(TrangChuNV_GUI mainFrameNV) {
		this.mainFrameNV = mainFrameNV;
		initUI();
		setHoatDong();
	}

	public TimKiemPhieuDatHang_GUI(TrangChuQL_GUI mainFrame) {
		this.mainFrameQL = mainFrame;
		initUI();
		setHoatDong();
	}

	public void setHoatDong() {
		btnLamMoi.addActionListener(e -> dspdhCtrl.lamMoi());
		btnTimKiem.addActionListener(e -> dspdhCtrl.locTatCa());
		btnChiTiet.addActionListener(e -> dspdhCtrl.moTrangChiTiet());
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1058, 509));
		setBackground(Color.WHITE);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(Color.WHITE);
		topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(topPanel, BorderLayout.NORTH);

		JLabel lblTitle = new JLabel("DANH SÁCH PHIẾU ĐẶT THUỐC", SwingConstants.CENTER);
		lblTitle.setFont(font1);
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblTitle);

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
		searchPanel.setBackground(Color.WHITE);

		JLabel lblKH = tool.taoLabel("Tên khách hàng: ");
		txtTenKH = tool.taoTextField("Tên khách hàng...");

		JLabel lblNV = tool.taoLabel("Tên nhân viên:");
		txtTenNV = tool.taoTextField("Tên nhân viên...");

		btnTimKiem = tool.taoButton("Tìm kiếm", "/picture/hoaDon/search.png");

		searchPanel.add(lblKH);
		searchPanel.add(txtTenKH);
		searchPanel.add(lblNV);
		searchPanel.add(txtTenNV);
		searchPanel.add(btnTimKiem);

		JPanel functionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 10));
		functionPanel.setBackground(Color.WHITE);

		JLabel lblTrangThai = tool.taoLabel("Trạng thái:");
		cmbTrangThai = tool.taoComboBox(new String[] { "Tất cả", "Chờ hàng", "Đã giao", "Đã hủy" });
		cmbTrangThai.setEditable(false);

		btnChiTiet = tool.taoButton("Xem chi tiết", "/picture/hoaDon/xemChiTiet.png");
		btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/refresh.png");

		functionPanel.add(lblTrangThai);
		functionPanel.add(cmbTrangThai);
		functionPanel.add(btnChiTiet);
		functionPanel.add(btnLamMoi);

		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(Color.WHITE);
		topPanel.add(lblTitle);
		topPanel.add(searchPanel);
		topPanel.add(functionPanel);

		add(topPanel, BorderLayout.NORTH);

		String[] columnNames = { "Mã phiếu", "Tên nhân viên", "Tên khách hàng", "Ngày đặt", "Ngày hẹn", "Trạng thái" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		
		tblPhieuDatHang = new JTable(model);
		tblPhieuDatHang.setRowHeight(38);
		tblPhieuDatHang.setFont(font2);

		tblPhieuDatHang.setBackground(Color.WHITE);
		tblPhieuDatHang.getTableHeader().setBackground(new Color(240, 240, 240));
		tblPhieuDatHang.setGridColor(new Color(200, 200, 200));
		tblPhieuDatHang.setShowGrid(true);
		tblPhieuDatHang.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

		JScrollPane scrollPane = new JScrollPane(tblPhieuDatHang);
		scrollPane.getViewport().setBackground(Color.WHITE);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblPhieuDatHang.getColumnCount(); i++) {
			tblPhieuDatHang.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		JTableHeader header = tblPhieuDatHang.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(font1);
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		add(scrollPane, BorderLayout.CENTER);
		dspdhCtrl.setDataChoTable(dspdhCtrl.layTatCaPhieuDatHang());
	}

	public JTable getTblPhieuDatHang() {
		return tblPhieuDatHang;
	}

	public TrangChuQL_GUI getMainFrameQL() {
		return mainFrameQL;
	}

	public void setMainFrameQL(TrangChuQL_GUI mainFrame) {
		this.mainFrameQL = mainFrame;
	}

	public TrangChuNV_GUI getMainFrameNV() {
		return mainFrameNV;
	}

	public void setMainFrameNV(TrangChuNV_GUI mainFrameNV) {
		this.mainFrameNV = mainFrameNV;
	}

}
