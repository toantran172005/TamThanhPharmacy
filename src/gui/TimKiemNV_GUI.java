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
    private JButton btnXemChiTiet, btnLamMoi, btnLichSuXoa, btnXoa;
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

        // ====== TOP PANEL ======
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(810, 120));

        // --- Bên trái (tìm kiếm) ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(2, 1));

        // Dòng 1: SĐT
        JPanel sdtPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        JLabel lblSdt = tool.taoLabel("Số điện thoại: ");
        txtSdt = tool.taoTextField("Số điện thoại...");
        sdtPanel.add(lblSdt);
        sdtPanel.add(txtSdt);

        // Dòng 2: Tên nhân viên
        JPanel tenPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        JLabel lblTen = tool.taoLabel("Tên nhân viên: ");
        txtTenNV = tool.taoTextField("Tên nhân viên...");
        tenPanel.add(lblTen);
        tenPanel.add(txtTenNV);

        leftPanel.add(sdtPanel);
        leftPanel.add(tenPanel);

        // --- Bên phải (nút chức năng) ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(2, 2, 10, 5));

        btnXemChiTiet = tool.taoButton("Xem chi tiết", "/picture/nhanVien/find.png");
        btnLamMoi = tool.taoButton("Làm mới", "/picture/nhanVien/refresh.png");
        btnLichSuXoa = tool.taoButton("Lịch sử xoá", "/picture/nhanVien/document.png");
        btnXoa = tool.taoButton("Xoá tất cả", "/picture/nhanVien/trash.png");

        rightPanel.add(btnXemChiTiet);
        rightPanel.add(btnLamMoi);
        rightPanel.add(btnLichSuXoa);
        rightPanel.add(btnXoa);

        topPanel.add(leftPanel, BorderLayout.CENTER);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ====== CENTER PANEL (Table) ======
        String[] columnNames = {"STT", "Mã nhân viên", "Tên nhân viên", "Số điện thoại", "Giới tính", "Chức vụ", "Hoạt động"};
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
		scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách hoá đơn")); // viền ngoài

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
		((DefaultTableCellRenderer) header.getDefaultRenderer())
		        .setHorizontalAlignment(SwingConstants.CENTER);

		add(scrollPane, BorderLayout.CENTER);
        
        btnXemChiTiet.addActionListener(e -> {
			ChiTietNhanVien_GUI chiTietPanel = new ChiTietNhanVien_GUI(mainFrame);
			mainFrame.setUpNoiDung(chiTietPanel);
		});
    }

}
