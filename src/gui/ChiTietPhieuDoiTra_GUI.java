package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controller.ToolCtrl;

import java.awt.*;

public class ChiTietPhieuDoiTra_GUI extends JPanel {

	private JLabel lblDiaChi, lblHotline, lblMaPhieuDT, lblMaHD, lblNgayLap, lblNhanVien, lblKhachHang, lblLyDo,
			lblTongTienHoan;
	private JTable tblThuoc;
	private JButton btnInPhieu, btnQuayLai;
	private TrangChuQL_GUI mainFrame;
	Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

	public ChiTietPhieuDoiTra_GUI(TrangChuQL_GUI mainFrame) {
		this.mainFrame = mainFrame;
		initUI();
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1058, 509));
		setBackground(Color.WHITE);

		// ======= TOP (Thông tin hiệu thuốc + phiếu) =======
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
		pnlTop.setBackground(Color.WHITE);
		pnlTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel lblTieuDe = new JLabel("HIỆU THUỐC TAM THANH", SwingConstants.CENTER);
		lblTieuDe.setFont(font1);
		lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblTieuDe);

		JLabel lblDiaChiTitle = tool.taoLabel("Địa chỉ: ");
		lblDiaChi = tool.taoLabel("");

		JLabel lblHotlineTitle = tool.taoLabel("Hotline: ");
		lblHotline = tool.taoLabel("");

		JPanel pnlDiaChi = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pnlDiaChi.setBackground(Color.WHITE);
		pnlDiaChi.add(lblDiaChiTitle);
		pnlDiaChi.add(lblDiaChi);

		JPanel pnlHotline = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pnlHotline.setBackground(Color.WHITE);
		pnlHotline.add(lblHotlineTitle);
		pnlHotline.add(lblHotline);

		pnlTop.add(pnlDiaChi);
		pnlTop.add(pnlHotline);

		JLabel lblChiTietTieuDe = new JLabel("CHI TIẾT PHIẾU ĐỔI TRẢ", SwingConstants.CENTER);
		lblChiTietTieuDe.setFont(font1);
		lblChiTietTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblChiTietTieuDe);

		// Thông tin phiếu
		pnlTop.add(taoDongThongTin("Mã phiếu đổi trả:", lblMaPhieuDT = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Mã hoá đơn:", lblMaHD = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Ngày lập:", lblNgayLap = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Nhân viên:", lblNhanVien = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Khách hàng:", lblKhachHang = tool.taoLabel("")));

		add(pnlTop, BorderLayout.NORTH);

		// ======= CENTER (Bảng thuốc + lý do) =======
		JPanel pnlCenter = new JPanel();
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
		pnlCenter.setBackground(Color.WHITE);

		String[] columns = { "Tên thuốc", "Số lượng", "Đơn vị", "Mức hoàn", "Tiền hoàn", "Ghi chú" };
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		tblThuoc = new JTable(model);

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
		scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách thuốc")); // viền ngoài

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

		JPanel pnlLyDo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlLyDo.setBackground(Color.WHITE);
		JLabel lblLyDoTitle = tool.taoLabel("Lý do:");
		lblLyDo = tool.taoLabel("");
		pnlLyDo.add(lblLyDoTitle);
		pnlLyDo.add(lblLyDo);
		pnlCenter.add(pnlLyDo);

		add(pnlCenter, BorderLayout.CENTER);

		// ======= BOTTOM (Tổng tiền + nút) =======
		JPanel pnlBottom = new JPanel();
		pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
		pnlBottom.setBackground(Color.WHITE);

		JPanel pnlTongTien = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlTongTien.setBackground(Color.WHITE);
		JLabel lblTongTienTitle = tool.taoLabel("Tổng tiền hoàn:");
		lblTongTienHoan = tool.taoLabel("0 VND");
		pnlTongTien.add(lblTongTienTitle);
		pnlTongTien.add(lblTongTienHoan);
		pnlBottom.add(pnlTongTien);

		JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
		pnlNut.setBackground(Color.WHITE);
		btnInPhieu = tool.taoButton("In phiếu", "/picture/hoaDon/print.png");
		btnQuayLai = tool.taoButton("Quay lại", "/picture/hoaDon//signOut.png");
		pnlNut.add(btnInPhieu);
		pnlNut.add(btnQuayLai);
		pnlBottom.add(pnlNut);

		add(pnlBottom, BorderLayout.SOUTH);

		btnQuayLai.addActionListener(e -> {
			mainFrame.setUpNoiDung(new TimKiemPhieuDoiTra_GUI(mainFrame));
		});
	}

	private JPanel taoDongThongTin(String tieuDe, JLabel lblNoiDung) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 5));
		panel.setBackground(Color.WHITE);
		JLabel lblTieuDe = tool.taoLabel(tieuDe);
		panel.add(lblTieuDe);
		panel.add(lblNoiDung);
		return panel;
	}

}
