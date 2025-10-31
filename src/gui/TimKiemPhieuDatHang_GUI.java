package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TimKiemPhieuDatHang_GUI extends JPanel {
	private JTable tblPhieuDatThuoc;
	private JTextField txtKhachHang, txtTenNV;
	private JComboBox<String> cmbTrangThai;
	private JButton btnTimKiem, btnChiTiet, btnLamMoi;
	private TrangChuQL_GUI mainFrame;

	public TimKiemPhieuDatHang_GUI(TrangChuQL_GUI mainFrame) {
        this.mainFrame = mainFrame;
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
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // 🔥 Giúp căn giữa khi dùng BoxLayout
		topPanel.add(lblTitle);

		// ====== Hàng tìm kiếm ======
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
		searchPanel.setBackground(Color.WHITE);

		JLabel lblKH = new JLabel("Tên khách hàng:");
		lblKH.setFont(new Font("SansSerif", Font.BOLD, 15));
		txtKhachHang = new JTextField(15);
		txtKhachHang.setFont(new Font("SansSerif", Font.PLAIN, 15));

		JLabel lblNV = new JLabel("Tên nhân viên:");
		lblNV.setFont(new Font("SansSerif", Font.BOLD, 15));
		txtTenNV = new JTextField(15);
		txtTenNV.setFont(new Font("SansSerif", Font.PLAIN, 15));

		btnTimKiem = taoButton("Tìm kiếm", "/img/search.png");
		
		searchPanel.add(lblKH);
		searchPanel.add(txtKhachHang);
		searchPanel.add(lblNV);
		searchPanel.add(txtTenNV);
		searchPanel.add(btnTimKiem);

		// ====== Hàng trạng thái & nút chức năng ======
		JPanel functionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 10));
		functionPanel.setBackground(Color.WHITE);

		JLabel lblTrangThai = new JLabel("Trạng thái:");
		lblTrangThai.setFont(new Font("SansSerif", Font.BOLD, 15));
		cmbTrangThai = new JComboBox<>();
		cmbTrangThai.setPreferredSize(new Dimension(140, 26));
		cmbTrangThai.setFont(new Font("SansSerif", Font.PLAIN, 14));

		btnChiTiet = taoButton("Xem chi tiết", "/img/xemChiTiet.png");
		btnLamMoi = taoButton("Làm mới", "/img/refresh.png");

		JButton[] buttons = { btnLamMoi, btnChiTiet };
		for (JButton btn : buttons) {
			btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
			btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

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

		// ====== Bảng danh sách phiếu ======
		String[] columnNames = { "", "Mã phiếu", "Tên nhân viên", "Tên khách hàng", "Ngày đặt", "Ngày hẹn",
				"Trạng thái" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		tblPhieuDatThuoc = new JTable(model);
		tblPhieuDatThuoc.setRowHeight(28);
		tblPhieuDatThuoc.setFont(new Font("SansSerif", Font.PLAIN, 14));
		tblPhieuDatThuoc.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

		JScrollPane scrollPane = new JScrollPane(tblPhieuDatThuoc);
		add(scrollPane, BorderLayout.CENTER);
		
		btnChiTiet.addActionListener(e -> {
		    ChiTietPhieuDatHang_GUI chiTietPanel = new ChiTietPhieuDatHang_GUI(mainFrame);
		    mainFrame.setUpNoiDung(chiTietPanel);
		});
	}

	private JButton taoButton(String text, String imgPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(245, 245, 245));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imgPath));
            Image scaled = icon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            System.err.println("Không tìm thấy ảnh: " + imgPath);
        }
        return btn;
    }
}
