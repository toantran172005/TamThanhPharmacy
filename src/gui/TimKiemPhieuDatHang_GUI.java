package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controller.ToolCtrl;

import java.awt.*;

public class TimKiemPhieuDatHang_GUI extends JPanel {
	private JTable tblPhieuDatThuoc;
	private JTextField txtKhachHang, txtTenNV;
	private JComboBox<String> cmbTrangThai;
	private JButton btnTimKiem, btnChiTiet, btnLamMoi;
	private TrangChuQL_GUI mainFrame;
	private TrangChuNV_GUI mainFrameNV;
	Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

	public TimKiemPhieuDatHang_GUI(TrangChuQL_GUI mainFrame) {
		this.mainFrame = mainFrame;
		initUI();
	}

	public TimKiemPhieuDatHang_GUI(TrangChuNV_GUI mainFrameNV) {
		this.mainFrameNV = mainFrameNV;
		initUI();
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1058, 509));
		setBackground(Color.WHITE);

		// ======== TOP PANEL ========
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(Color.WHITE);
		topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(topPanel, BorderLayout.NORTH);

		// Tiêu đề
		JLabel lblTitle = new JLabel("DANH SÁCH PHIẾU ĐẶT THUỐC", SwingConstants.CENTER);
		lblTitle.setFont(font1);
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // 🔥 Giúp căn giữa khi dùng BoxLayout
		topPanel.add(lblTitle);

		// ====== Hàng tìm kiếm ======
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
		searchPanel.setBackground(Color.WHITE);

		JLabel lblKH = tool.taoLabel("Tên khách hàng: ");
		txtKhachHang = tool.taoTextField("Tên khách hàng...");

		JLabel lblNV = tool.taoLabel("Tên nhân viên:");
		txtTenNV = tool.taoTextField("Tên nhân viên...");

		btnTimKiem = tool.taoButton("Tìm kiếm", "/picture/hoaDon/search.png");

		searchPanel.add(lblKH);
		searchPanel.add(txtKhachHang);
		searchPanel.add(lblNV);
		searchPanel.add(txtTenNV);
		searchPanel.add(btnTimKiem);

		// ====== Hàng trạng thái & nút chức năng ======
		JPanel functionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 10));
		functionPanel.setBackground(Color.WHITE);

		JLabel lblTrangThai = tool.taoLabel("Trạng thái:");
		cmbTrangThai = cmbTrangThai = cmbTrangThai = tool
				.taoComboBox(new String[] { "Tất cả", "Chờ hàng", "Đã giao", "Đã hủy" });
		cmbTrangThai.setPreferredSize(new Dimension(140, 26));

		btnChiTiet = tool.taoButton("Xem chi tiết", "/picture/hoaDon/xemChiTiet.png");
		btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/refresh.png");

		functionPanel.add(lblTrangThai);
		functionPanel.add(cmbTrangThai);
		functionPanel.add(btnChiTiet);
		functionPanel.add(btnLamMoi);

		// ====== Panel trên cùng (Top) ======
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(Color.WHITE);
		topPanel.add(lblTitle);
		topPanel.add(searchPanel);
		topPanel.add(functionPanel);

		add(topPanel, BorderLayout.NORTH);

		// ======== CENTER TABLE ========
		String[] columnNames = { "STT", "Mã phiếu", "Tên nhân viên", "Tên khách hàng", "Ngày đặt", "Ngày hẹn",
				"Trạng thái" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		tblPhieuDatThuoc = new JTable(model);
		tblPhieuDatThuoc.setRowHeight(28);
		tblPhieuDatThuoc.getTableHeader().setFont(font2);
		tblPhieuDatThuoc.setFont(font2);

		// Đặt nền trắng cho bảng
		tblPhieuDatThuoc.setBackground(Color.WHITE);

		// Đặt nền trắng cho vùng header và vùng chứa
		tblPhieuDatThuoc.getTableHeader().setBackground(new Color(240, 240, 240)); // xám rất nhạt
		tblPhieuDatThuoc.setGridColor(new Color(200, 200, 200)); // Màu đường kẻ ô (nhẹ)
		tblPhieuDatThuoc.setShowGrid(true); // Bật hiển thị đường kẻ

		// Viền cho bảng
		tblPhieuDatThuoc.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

		// Nền của JScrollPane (bao quanh bảng)
		JScrollPane scrollPane = new JScrollPane(tblPhieuDatThuoc);
		scrollPane.getViewport().setBackground(Color.WHITE); // nền vùng chứa bảng

		// Căn giữa nội dung các ô
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblPhieuDatThuoc.getColumnCount(); i++) {
			tblPhieuDatThuoc.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		// Căn giữa tiêu đề cột
		JTableHeader header = tblPhieuDatThuoc.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(font2);
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		add(scrollPane, BorderLayout.CENTER);

		btnChiTiet.addActionListener(e -> {
			ChiTietPhieuDatHang_GUI chiTietPanel = new ChiTietPhieuDatHang_GUI(mainFrame);
			mainFrame.setUpNoiDung(chiTietPanel);
		});
	}
}
