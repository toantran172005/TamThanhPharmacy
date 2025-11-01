package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controller.ToolCtrl;

import java.awt.*;

public class ChiTietPhieuDatHang_GUI extends JPanel {

	private JLabel lblDiaChi, lblHotline, lblMaPhieu, lblNgayDat, lblNgayHen, lblNhanVien, lblKhachHang, lblGhiChu;
	private JTable tblThuoc;
	private JComboBox<String> cmbTrangThai;
	private JButton btnInPhieu, btnCapNhat, btnQuayLai;
	private TrangChuQL_GUI mainFrame;
	Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

	public ChiTietPhieuDatHang_GUI(TrangChuQL_GUI mainFrame) {
		this.mainFrame = mainFrame;
		initUI();
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1058, 509));
		setBackground(Color.WHITE);

		// ========== TOP ==========
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
		pnlTop.setBorder(new EmptyBorder(10, 0, 10, 0));
		pnlTop.setBackground(Color.WHITE);

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

		JLabel lblChiTiet = new JLabel("CHI TIẾT PHIẾU ĐẶT THUỐC", SwingConstants.CENTER);
		lblChiTiet.setFont(font1);
		lblChiTiet.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblChiTiet.setBorder(new EmptyBorder(10, 0, 10, 0));
		pnlTop.add(lblChiTiet);

		// Thông tin phiếu
		pnlTop.add(taoDongThongTin("Mã phiếu:", lblMaPhieu = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Ngày đặt:", lblNgayDat = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Ngày hẹn:", lblNgayHen = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Nhân viên:", lblNhanVien = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Khách hàng:", lblKhachHang = tool.taoLabel("")));

		add(pnlTop, BorderLayout.NORTH);

		// ======== CENTER TABLE ========
		JPanel pnlCenter = new JPanel();
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
		pnlCenter.setBorder(new EmptyBorder(10, 20, 10, 20));
		pnlCenter.setBackground(Color.WHITE);

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
		pnlCenter.add(scrollPane);

		// Ghi chú
		pnlCenter.add(Box.createVerticalStrut(15));
		pnlCenter.add(taoDongThongTin("Ghi chú:", lblGhiChu = tool.taoLabel("")));

		// Trạng thái
		pnlCenter.add(Box.createVerticalStrut(10));
		JPanel trangThaiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		trangThaiPanel.setBackground(Color.WHITE);
		JLabel lblTrangThai = tool.taoLabel("Trạng thái:");
		cmbTrangThai = cmbTrangThai = tool.taoComboBox(new String[] { "Chờ hàng", "Đã giao", "Đã hủy" });
		cmbTrangThai.setPreferredSize(new Dimension(150, 30));
		trangThaiPanel.add(lblTrangThai);
		trangThaiPanel.add(cmbTrangThai);
		pnlCenter.add(trangThaiPanel);

		add(pnlCenter, BorderLayout.CENTER);

		// ========== BOTTOM ==========
		JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
		pnlBottom.setBackground(Color.WHITE);

		btnInPhieu = tool.taoButton("In phiếu", "/picture/hoaDon/print.png");
		btnCapNhat = tool.taoButton("Cập nhật", "/picture/hoaDon/edit.png");
		btnQuayLai = tool.taoButton("Quay lại", "/picture/hoaDon/signOut.png");

		pnlBottom.add(btnInPhieu);
		pnlBottom.add(btnCapNhat);
		pnlBottom.add(btnQuayLai);

		add(pnlBottom, BorderLayout.SOUTH);

		btnQuayLai.addActionListener(e -> {
			mainFrame.setUpNoiDung(new TimKiemPhieuDatHang_GUI(mainFrame));
		});
	}

	// Hàm tạo 1 dòng thông tin
	private JPanel taoDongThongTin(String tieuDe, JLabel lblNoiDung) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		panel.setBackground(Color.WHITE);
		JLabel lblTieuDe = tool.taoLabel(tieuDe);
		panel.add(lblTieuDe);
		panel.add(lblNoiDung);
		return panel;
	}

}
