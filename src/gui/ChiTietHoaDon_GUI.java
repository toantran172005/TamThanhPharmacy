package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controller.ToolCtrl;

import java.awt.*;
import java.util.Vector;

public class ChiTietHoaDon_GUI extends JPanel {
	private JLabel lblDiaChi, lblHotline, lblMaHD, lblNgayLap, lblNhanVien, lblKhachHang;
	private JLabel lblGhiChu, lblTongTien, lblTienNhan, lblTienThua; 
	private JTable tblThuoc;
	private JButton btnInHoaDon, btnQuayLai, btnTaoPhieuDoiTra;
	private TrangChuQL_GUI mainFrame;
	Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

	public ChiTietHoaDon_GUI(TrangChuQL_GUI mainFrame) {
		this.mainFrame = mainFrame;
		initUI();
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1134, 617));
		setBackground(Color.WHITE);

		// --- TOP PANEL ---
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
		pnlTop.setBackground(Color.WHITE);

		// Tên hiệu thuốc
		JLabel lblTieuDe = new JLabel("HIỆU THUỐC TAM THANH", SwingConstants.CENTER);
		lblTieuDe.setFont(font1);
		lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblTieuDe);

		// Địa chỉ (center)
		pnlTop.add(Box.createVerticalStrut(10));
		JPanel pnlDiaChi = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		pnlDiaChi.setBackground(Color.WHITE);
		JLabel lblDC = tool.taoLabel("Địa chỉ: ");
		lblDiaChi = tool.taoLabel("");
		pnlDiaChi.add(lblDC);
		pnlDiaChi.add(lblDiaChi);
		pnlTop.add(pnlDiaChi);

		// Hotline (center)
		JPanel pnlHotline = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		pnlHotline.setBackground(Color.WHITE);
		JLabel lblHL = tool.taoLabel("Hotline: ");
		lblHotline = tool.taoLabel("");
		pnlHotline.add(lblHL);
		pnlHotline.add(lblHotline);
		pnlTop.add(pnlHotline);

		// Tiêu đề chi tiết hoá đơn
		pnlTop.add(Box.createVerticalStrut(10));
		JLabel lblChiTiet = new JLabel("CHI TIẾT HOÁ ĐƠN", SwingConstants.CENTER);
		lblChiTiet.setFont(font1);
		lblChiTiet.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblChiTiet);

		pnlTop.add(taoDongThongTin("Mã hoá đơn: ", lblMaHD = tool.taoLabel("Đỗ Minh Tuấn")));
		pnlTop.add(taoDongThongTin("Ngày lập: ", lblNgayLap = tool.taoLabel("11/11/1111")));
		pnlTop.add(taoDongThongTin("Nhân viên: ", lblNhanVien = tool.taoLabel("Đỗ Minh Tuấn")));
		pnlTop.add(taoDongThongTin("Khách hàng: ", lblKhachHang = tool.taoLabel("Đỗ Minh Tuấn")));

		add(pnlTop, BorderLayout.NORTH);

		// ======== CENTER TABLE ========
		String[] columnNames = { "Tên thuốc", "Số lượng", "Đơn vị", "Đơn giá", "Thành tiền" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		tblThuoc = new JTable(model);
		tblThuoc.setRowHeight(28);
		tblThuoc.getTableHeader().setFont(font2);
		tblThuoc.setFont(font2);

		// Đặt nền trắng cho bảng
		tblThuoc.setBackground(Color.WHITE);

		// Đặt nền trắng cho vùng header và vùng chứa
		tblThuoc.getTableHeader().setBackground(new Color(240, 240, 240)); // xám rất nhạt
		tblThuoc.setGridColor(new Color(200, 200, 200)); // Màu đường kẻ ô (nhẹ)
		tblThuoc.setShowGrid(true); // Bật hiển thị đường kẻ

		// Viền cho bảng
		tblThuoc.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

		// Nền của JScrollPane (bao quanh bảng)
		JScrollPane scrollPane = new JScrollPane(tblThuoc);
		scrollPane.getViewport().setBackground(Color.WHITE); // nền vùng chứa bảng

		// Căn giữa nội dung các ô
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblThuoc.getColumnCount(); i++) {
			tblThuoc.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		// Căn giữa tiêu đề cột
		JTableHeader header = tblThuoc.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(font2);
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		add(scrollPane, BorderLayout.CENTER);

		// --- BOTTOM PANEL ---
		JPanel pnlBottom = new JPanel();
		pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
		pnlBottom.setBorder(new EmptyBorder(10, 0, 10, 0));
		pnlBottom.setBackground(Color.WHITE);

		pnlBottom.add(taoDongThongTin("Ghi chú:", lblGhiChu = tool.taoLabel("Không có")));
		pnlBottom.add(taoDongThongTin("Tổng tiền:", lblTongTien = tool.taoLabel("1.000.000 VNĐ")));
		pnlBottom.add(taoDongThongTin("Tiền nhận:", lblTienNhan = tool.taoLabel("1.000.000 VNĐ")));
		pnlBottom.add(taoDongThongTin("Tiền thừa:", lblTienThua = tool.taoLabel("1.000.000 VNĐ")));

		JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 10));
		pnlButtons.setBackground(Color.WHITE);

		btnInHoaDon = tool.taoButton("In hoá đơn", "/picture/hoaDon/print.png");
		btnQuayLai = tool.taoButton("Quay lại", "/picture/hoaDon/signOut.png");
		btnTaoPhieuDoiTra = tool.taoButton("Tạo phiếu đổi trả", "/picture/hoaDon/plus.png");

		pnlButtons.add(btnInHoaDon);
		pnlButtons.add(btnQuayLai);
		pnlButtons.add(btnTaoPhieuDoiTra);

		pnlBottom.add(pnlButtons);
		add(pnlBottom, BorderLayout.SOUTH);

		btnQuayLai.addActionListener(e -> {
			mainFrame.setUpNoiDung(new TimKiemHD_GUI(mainFrame));
		});
	}

	private JPanel taoDongThongTin(String ten, JLabel lbl) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		panel.setBackground(Color.WHITE);
		JLabel lblTen = tool.taoLabel(ten);
		panel.add(lblTen);
		panel.add(lbl);
		return panel;
	}



}
