package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import controller.ToolCtrl;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LapPhieuDatHang_GUI extends JPanel {
	private JTextField txtSdt, txtTenKH, txtTuoi, txtSoLuong;
	private JComboBox<String> cmbSanPham, cmbDonVi;
	private JTable tblThuoc;
	private JTextArea txaGhiChu;
	private JButton btnThem, btnLamMoi, btnTaoPhieuDat;
	private JDateChooser ngayHen;
	Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

	public LapPhieuDatHang_GUI() {
		setLayout(new BorderLayout(0, 15));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(1027, 900));
		setBorder(new EmptyBorder(10, 20, 10, 20));

		// =================== TOP ===================
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
		pnlTop.setBackground(Color.WHITE);
		pnlTop.setBorder(new EmptyBorder(20, 20, 10, 20));

		JLabel lblTieuDe = new JLabel("LẬP PHIẾU ĐẶT THUỐC", SwingConstants.CENTER);
		lblTieuDe.setFont(font1);
		lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblTieuDe);

		pnlTop.add(Box.createVerticalStrut(10));
		pnlTop.add(new JSeparator());
		pnlTop.add(Box.createVerticalStrut(15));

		// --- Hàng 1: SĐT, Tên KH, Tuổi ---
		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
		row1.setBackground(Color.WHITE);

		row1.add(tool.taoLabel("Số điện thoại:"));
		txtSdt = tool.taoTextField("Số điện thoại...");
		row1.add(txtSdt);

		row1.add(tool.taoLabel("Tên khách hàng:"));
		txtTenKH = tool.taoTextField("Tên khách hàng...");
		row1.add(txtTenKH);

		row1.add(tool.taoLabel("Tuổi:"));
		txtTuoi = tool.taoTextField("Tuổi...");
		row1.add(txtTuoi);
		pnlTop.add(row1);

		// --- Hàng 2: Sản phẩm, Số lượng ---
		JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
		row2.setBackground(Color.WHITE);

		row2.add(tool.taoLabel("Sản phẩm:"));
		cmbSanPham = tool.taoComboBox(new String[] {});
		cmbSanPham.setEditable(true);
		cmbSanPham.setPreferredSize(new Dimension(200, 30));
		row2.add(cmbSanPham);

		row2.add(tool.taoLabel("Số lượng:"));
		txtSoLuong = tool.taoTextField("Số lượng...");
		row2.add(txtSoLuong);
		pnlTop.add(row2);

		// --- Hàng 3: Ngày hẹn, Đơn vị, Thêm ---
		JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
		row3.setBackground(Color.WHITE);

		// --- JDateChooser ---
		row3.add(tool.taoLabel("Ngày hẹn:"));
		ngayHen = tool.taoDateChooser();
		ngayHen.setDate(new Date()); // ngày hiện tại mặc định
		row3.add(ngayHen);

		row3.add(tool.taoLabel("Đơn vị:"));
		cmbDonVi = tool.taoComboBox(new String[] {});
		row3.add(cmbDonVi);

		btnThem = tool.taoButton("Thêm", "/picture/hoaDon/plus.png");
		row3.add(btnThem);
		pnlTop.add(row3);

		add(pnlTop, BorderLayout.NORTH);

		// =================== CENTER ===================
		String[] cols = { "STT", "Tên thuốc", "Số lượng", "Đơn vị", "Hoạt động" };
		DefaultTableModel model = new DefaultTableModel(cols, 0);
		tblThuoc = new JTable(model);
		tblThuoc.setRowHeight(25);
		tblThuoc.setFont(font2);
		tblThuoc.getTableHeader().setFont(font2);

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

		add(scrollPane, BorderLayout.CENTER);

		// =================== BOTTOM ===================
		JPanel pnlBottom = new JPanel();
		pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
		pnlBottom.setBorder(new EmptyBorder(10, 20, 20, 20));
		pnlBottom.setBackground(Color.WHITE);

		// --- Ghi chú ---
		JPanel rowNote = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		rowNote.setBackground(Color.WHITE);
		txaGhiChu = tool.taoTextArea(50);
		rowNote.add(tool.taoLabel("Ghi chú:"));
		rowNote.add(new JScrollPane(txaGhiChu));
		pnlBottom.add(rowNote);

		// --- Nút chức năng ---
		JPanel rowBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
		rowBtns.setBackground(Color.WHITE);
		btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/refresh.png");
		btnTaoPhieuDat = tool.taoButton("Tạo phiếu đặt", "/picture/hoaDon/plus.png");
		rowBtns.add(btnLamMoi);
		rowBtns.add(btnTaoPhieuDat);

		pnlBottom.add(rowBtns);
		add(pnlBottom, BorderLayout.SOUTH);
	}

}
