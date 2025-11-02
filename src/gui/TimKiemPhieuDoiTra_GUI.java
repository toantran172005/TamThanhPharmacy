package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controller.ToolCtrl;

import java.awt.*;

public class TimKiemPhieuDoiTra_GUI extends JPanel {
	private JTextField txtKhachHang, txtTenNV;
	private JButton btnTimKiem, btnLamMoi, btnChiTiet;
	private JTable tblPhieuDoiTra;
	private TrangChuQL_GUI mainFrame;
	private TrangChuNV_GUI mainFrameNV;
	Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

	public TimKiemPhieuDoiTra_GUI(TrangChuQL_GUI mainFrame) {
		this.mainFrame = mainFrame;
		initUI();
	}
	
	public TimKiemPhieuDoiTra_GUI(TrangChuNV_GUI mainFrameNV) {
		this.mainFrameNV = mainFrameNV;
		initUI();
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1134, 617));
		setBackground(Color.WHITE);

		// ===== TOP PANEL =====
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
		pnlTop.setBackground(Color.WHITE);
		pnlTop.setBorder(new EmptyBorder(10, 10, 10, 10));

		// Tiêu đề
		JLabel lblTitle = new JLabel("DANH SÁCH PHIẾU ĐỔI TRẢ", SwingConstants.CENTER);
		lblTitle.setFont(font1);
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblTitle);
		pnlTop.add(Box.createVerticalStrut(10));

		// Hàng tìm kiếm
		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
		row1.setBackground(Color.WHITE);

		JLabel lblKH = tool.taoLabel("Tên khách hàng:");
		txtKhachHang = tool.taoTextField("Tên khách hàng...");

		JLabel lblNV = tool.taoLabel("Tên nhân viên:");
		txtTenNV = tool.taoTextField("Tên nhân viên...");

		btnTimKiem = tool.taoButton("Tìm kiếm", "/picture/hoaDon/search.png");

		row1.add(lblKH);
		row1.add(txtKhachHang);
		row1.add(lblNV);
		row1.add(txtTenNV);
		row1.add(btnTimKiem);
		pnlTop.add(row1);

		// Hàng chức năng
		JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 5));
		row2.setBackground(Color.WHITE);

		btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/refresh.png");
		btnChiTiet = tool.taoButton("Xem chi tiết", "/picture/hoaDon/xemChiTiet.png");

		row2.add(btnLamMoi);
		row2.add(btnChiTiet);
		pnlTop.add(row2);

		add(pnlTop, BorderLayout.NORTH);

		// ===== CENTER (TABLE) =====
		String[] colNames = { "STT", "Mã phiếu", "Tên nhân viên", "Tên khách hàng", "Số điện thoại", "Ngày lập", "Lý do" };
		DefaultTableModel model = new DefaultTableModel(colNames, 0);
		tblPhieuDoiTra = new JTable(model);

		tblPhieuDoiTra.setRowHeight(28);
		tblPhieuDoiTra.getTableHeader().setFont(font2);
		tblPhieuDoiTra.setFont(font2);

		// Đặt nền trắng cho bảng
		tblPhieuDoiTra.setBackground(Color.WHITE);

		// Đặt nền trắng cho vùng header và vùng chứa
		tblPhieuDoiTra.getTableHeader().setBackground(new Color(240, 240, 240)); // xám rất nhạt
		tblPhieuDoiTra.setGridColor(new Color(200, 200, 200)); // Màu đường kẻ ô (nhẹ)
		tblPhieuDoiTra.setShowGrid(true); // Bật hiển thị đường kẻ

		// Viền cho bảng
		tblPhieuDoiTra.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

		// Nền của JScrollPane (bao quanh bảng)
		JScrollPane scrollPane = new JScrollPane(tblPhieuDoiTra);
		scrollPane.getViewport().setBackground(Color.WHITE); // nền vùng chứa bảng

		// Căn giữa nội dung các ô
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblPhieuDoiTra.getColumnCount(); i++) {
			tblPhieuDoiTra.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		// Căn giữa tiêu đề cột
		JTableHeader header = tblPhieuDoiTra.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(font2);
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		add(scrollPane, BorderLayout.CENTER);
		
		
		btnChiTiet.addActionListener(e -> {
		    ChiTietPhieuDoiTra_GUI chiTietPanel = new ChiTietPhieuDoiTra_GUI(mainFrame);
		    mainFrame.setUpNoiDung(chiTietPanel);
		});
	}

}
