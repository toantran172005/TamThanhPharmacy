package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controller.ToolCtrl;

import java.awt.*;

public class TimKiemNV_GUI extends JPanel {

	private JTable tblNhanVien;
	private JTextField txtSdt, txtTenNV;
	private JButton btnXemChiTiet, btnLamMoi, btnLichSuXoa;
	Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

	private TrangChuQL_GUI mainFrame;

	public TimKiemNV_GUI(TrangChuQL_GUI mainFrame) {
		this.mainFrame = mainFrame;
		initUI();
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1058, 509));
		setBackground(Color.WHITE);

		// ====== TOP PANEL (CẢ TÌM KIẾM VÀ NÚT ĐỀU Ở GIỮA) ======
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setPreferredSize(new Dimension(1058, 140));
		topPanel.setBackground(Color.WHITE);

		// Tiêu đề
		JLabel lblTitle = new JLabel("DANH SÁCH NHÂN VIÊN", SwingConstants.CENTER);
		lblTitle.setFont(font1);
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblTitle);
		topPanel.add(Box.createVerticalStrut(15));

		// --- Panel chứa tìm kiếm + nút (căn giữa) ---
		JPanel centerContent = new JPanel();
		centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.X_AXIS));
		centerContent.setBackground(Color.WHITE);

		// === Bên trái: Tìm kiếm ===
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(2, 1, 0, 8));
		leftPanel.setBackground(Color.WHITE);

		JPanel sdtPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		sdtPanel.setBackground(Color.WHITE);
		JLabel lblSdt = tool.taoLabel("Số điện thoại: ");
		txtSdt = tool.taoTextField("Số điện thoại...");
		sdtPanel.add(lblSdt);
		sdtPanel.add(txtSdt);

		JPanel tenPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		tenPanel.setBackground(Color.WHITE);
		JLabel lblTen = tool.taoLabel("Tên nhân viên: ");
		txtTenNV = tool.taoTextField("Tên nhân viên...");
		tenPanel.add(lblTen);
		tenPanel.add(txtTenNV);

		leftPanel.add(sdtPanel);
		leftPanel.add(tenPanel);

		// === Bên phải: Nút ===
		JPanel rightPanel = new JPanel(new GridBagLayout());
		rightPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 6, 4, 6);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		btnXemChiTiet = tool.taoButton("Xem chi tiết", "/picture/nhanVien/find.png");
		btnLamMoi = tool.taoButton("Làm mới", "/picture/nhanVien/refresh.png");
		btnLichSuXoa = tool.taoButton("Lịch sử xoá", "/picture/nhanVien/document.png");

		Dimension btnSize = new Dimension(145, 38);
		btnXemChiTiet.setPreferredSize(btnSize);
		btnLamMoi.setPreferredSize(btnSize);
		btnLichSuXoa.setPreferredSize(btnSize);

		gbc.gridx = 0;
		gbc.gridy = 0;
		rightPanel.add(btnXemChiTiet, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		rightPanel.add(btnLamMoi, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		rightPanel.add(btnLichSuXoa, gbc);

		// === Gom lại và căn giữa ===
		centerContent.add(Box.createHorizontalGlue()); // Đẩy vào giữa
		centerContent.add(leftPanel);
		centerContent.add(Box.createHorizontalStrut(30)); // Khoảng cách giữa tìm kiếm và nút
		centerContent.add(rightPanel);
		centerContent.add(Box.createHorizontalGlue()); // Đẩy vào giữa

		topPanel.add(centerContent);
		topPanel.add(Box.createVerticalStrut(10));

		add(topPanel, BorderLayout.NORTH);

		// ====== CENTER PANEL (Table) ======
		String[] columnNames = { "STT", "Mã nhân viên", "Tên nhân viên", "Số điện thoại", "Giới tính", "Chức vụ",
				"Hoạt động" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		tblNhanVien = new JTable(model);
		tblNhanVien.setRowHeight(28);
		tblNhanVien.getTableHeader().setFont(font2);
		tblNhanVien.setFont(font2);

		// Đặt nền trắng cho bảng
		tblNhanVien.setBackground(Color.WHITE);

		// Đặt nền trắng cho vùng header và vùng chứa
		tblNhanVien.getTableHeader().setBackground(new Color(240, 240, 240)); // xám rất nhạt
		tblNhanVien.setGridColor(new Color(200, 200, 200)); // Màu đường kẻ ô (nhẹ)
		tblNhanVien.setShowGrid(true); // Bật hiển thị đường kẻ

		// Viền cho bảng
		tblNhanVien.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

		// Nền của JScrollPane (bao quanh bảng)
		JScrollPane scrollPane = new JScrollPane(tblNhanVien);
		scrollPane.getViewport().setBackground(Color.WHITE); // nền vùng chứa bảng

		// Căn giữa nội dung các ô
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblNhanVien.getColumnCount(); i++) {
			tblNhanVien.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		// Căn giữa tiêu đề cột
		JTableHeader header = tblNhanVien.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(font2);
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		add(scrollPane, BorderLayout.CENTER);

		btnXemChiTiet.addActionListener(e -> {
			ChiTietNhanVien_GUI chiTietPanel = new ChiTietNhanVien_GUI(mainFrame);
			mainFrame.setUpNoiDung(chiTietPanel);
		});
	}

}
