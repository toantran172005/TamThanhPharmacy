package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controller.ToolCtrl;

import java.awt.*;

public class TimKiemHD_GUI extends JPanel {

	private JTable tblHoaDon;
	private JTextField txtKhachHang, txtTenNV;
	private JButton btnTimKiem, btnLamMoi, btnChiTiet, btnLichSuXoa;
	private TrangChuQL_GUI mainFrame;
	private TrangChuNV_GUI mainFrameNV;
	Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

	// --- Thêm constructor nhận TrangChuQL_GUI ---
	public TimKiemHD_GUI(TrangChuQL_GUI mainFrame) {
		this.mainFrame = mainFrame;
		initUI();
	}
	
	public TimKiemHD_GUI(TrangChuNV_GUI mainFrameNV) {
		this.mainFrameNV = mainFrameNV;
		initUI();
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1058, 509));
		setBackground(Color.WHITE);

		// ======== TOP PANEL ========
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(Color.WHITE);
		topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(topPanel, BorderLayout.NORTH);

		// Tiêu đề
		JLabel lblTitle = new JLabel("DANH SÁCH HOÁ ĐƠN", SwingConstants.CENTER);
		lblTitle.setFont(font1);
		topPanel.add(lblTitle, BorderLayout.NORTH);

		// HBox tìm kiếm
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		searchPanel.setBackground(Color.WHITE);

		JLabel lblKhachHang = tool.taoLabel("Tên khách hàng: ");
		txtKhachHang = tool.taoTextField("Tên khách hàng...");

		JLabel lblNhanVien = tool.taoLabel("Tên nhân viên: ");
		txtTenNV = tool.taoTextField("Tên nhân viên...");

		btnTimKiem = tool.taoButton("Tìm kiếm", "/picture/hoaDon/search.png");

		searchPanel.add(lblKhachHang);
		searchPanel.add(txtKhachHang);
		searchPanel.add(lblNhanVien);
		searchPanel.add(txtTenNV);
		searchPanel.add(btnTimKiem);

		topPanel.add(searchPanel, BorderLayout.CENTER);

		// HBox các nút bên dưới
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
		buttonPanel.setBackground(Color.WHITE);

		btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/refresh.png");
		btnChiTiet = tool.taoButton("Xem chi tiết", "/picture/hoaDon/xemChiTiet.png");
		btnLichSuXoa = tool.taoButton("Lịch sử xoá", "/picture/hoaDon/document.png");

		buttonPanel.add(btnLamMoi);
		buttonPanel.add(btnChiTiet);
		buttonPanel.add(btnLichSuXoa);

		topPanel.add(buttonPanel, BorderLayout.SOUTH);

		// ======== CENTER TABLE ========
		String[] columnNames = { "STT", "Mã hoá đơn", "Tên nhân viên", "Tên khách hàng", "Thời gian", "Tổng tiền hàng",
				"Hoạt động" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		tblHoaDon = new JTable(model);
		tblHoaDon.setRowHeight(28);
		tblHoaDon.getTableHeader().setFont(font2);
		tblHoaDon.setFont(font2);

		// Đặt nền trắng cho bảng
		tblHoaDon.setBackground(Color.WHITE);

		// Đặt nền trắng cho vùng header và vùng chứa
		tblHoaDon.getTableHeader().setBackground(new Color(240, 240, 240)); // xám rất nhạt
		tblHoaDon.setGridColor(new Color(200, 200, 200)); // Màu đường kẻ ô (nhẹ)
		tblHoaDon.setShowGrid(true); // Bật hiển thị đường kẻ

		// Viền cho bảng
		tblHoaDon.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

		// Nền của JScrollPane (bao quanh bảng)
		JScrollPane scrollPane = new JScrollPane(tblHoaDon);
		scrollPane.getViewport().setBackground(Color.WHITE); // nền vùng chứa bảng

		// Căn giữa nội dung các ô
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblHoaDon.getColumnCount(); i++) {
		    tblHoaDon.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		// Căn giữa tiêu đề cột
		JTableHeader header = tblHoaDon.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(font2);
		((DefaultTableCellRenderer) header.getDefaultRenderer())
		        .setHorizontalAlignment(SwingConstants.CENTER);

		add(scrollPane, BorderLayout.CENTER);

		btnChiTiet.addActionListener(e -> {
			ChiTietHoaDon_GUI chiTietPanel = new ChiTietHoaDon_GUI(mainFrame);
			mainFrame.setUpNoiDung(chiTietPanel);
		});
	}

	// === Getters ===
	public JTable getTblHoaDon() {
		return tblHoaDon;
	}

	public JTextField getTxtKhachHang() {
		return txtKhachHang;
	}

	public JTextField getTxtTenNV() {
		return txtTenNV;
	}

	public JButton getBtnTimKiem() {
		return btnTimKiem;
	}

	public JButton getBtnLamMoi() {
		return btnLamMoi;
	}

	public JButton getBtnChiTiet() {
		return btnChiTiet;
	}

	public JButton getBtnLichSuXoa() {
		return btnLichSuXoa;
	}
}
