package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import controller.ToolCtrl;

public class TimKiemThuoc_GUI extends JPanel {

	private JTextField txtTenThuoc;
	private JComboBox<String> cmbLoaiThuoc;
	private JTable tblThuoc;
	private JCheckBox chkChonTatCa;
	private JButton btnXemChiTiet, btnLamMoi, btnLichSuXoa, btnXoaTatCa;

	private final ToolCtrl tool = new ToolCtrl();

	public TimKiemThuoc_GUI() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// ====== TOP PANEL ======
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(Color.WHITE);
		topPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 10, 30));

		JPanel vboxTop = new JPanel();
		vboxTop.setLayout(new BoxLayout(vboxTop, BoxLayout.Y_AXIS));
		vboxTop.setBackground(Color.WHITE);

		// ===== Label tiêu đề =====
		JLabel lblTitle = new JLabel("TÌM KIẾM THUỐC", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		vboxTop.add(lblTitle);

		// ===== Hàng 1: Bộ lọc =====
		JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
		filterRow.setBackground(Color.WHITE);

		JPanel nameBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		nameBox.setBackground(Color.WHITE);
		JLabel lblTenThuoc = tool.taoLabel("Tên thuốc:");
		txtTenThuoc = tool.taoTextField("Tên thuốc...");
		txtTenThuoc.setPreferredSize(new Dimension(190, 34));
		lblTenThuoc.setPreferredSize(new Dimension(100, 25));
		nameBox.add(lblTenThuoc);
		nameBox.add(txtTenThuoc);

		JPanel loaiBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		loaiBox.setBackground(Color.WHITE);
		JLabel lblLoaiThuoc = tool.taoLabel("Loại thuốc:");
		cmbLoaiThuoc = new JComboBox<>();
		cmbLoaiThuoc.setPreferredSize(new Dimension(190, 34));
		lblLoaiThuoc.setPreferredSize(new Dimension(100, 25));
		loaiBox.add(lblLoaiThuoc);
		loaiBox.add(cmbLoaiThuoc);

		filterRow.add(nameBox);
		filterRow.add(loaiBox);
		vboxTop.add(filterRow);

		// ===== Hàng 2: Nút chức năng =====
		JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		btnRow.setBackground(Color.WHITE);

		btnXemChiTiet = tool.taoButton("Xem chi tiết", "/picture/keThuoc/find.png");
		btnLamMoi = tool.taoButton("Làm mới", "/picture/keThuoc/refresh.png");
		btnLichSuXoa = tool.taoButton("Lịch sử xoá", "/picture/nhanVien/document.png");

		btnRow.add(btnXemChiTiet);
		btnRow.add(btnLamMoi);
		btnRow.add(btnLichSuXoa);
		vboxTop.add(btnRow);

		topPanel.add(vboxTop, BorderLayout.CENTER);
		add(topPanel, BorderLayout.NORTH);

		// ====== CENTER: Bảng thuốc ======
		String[] cols = {"Mã thuốc", "Tên thuốc", "Phân loại", "Giá bán", "Số lượng", "Dạng thuốc", "Hạn sử dụng", "Hoạt động" };
		DefaultTableModel model = new DefaultTableModel(cols, 0);

		tblThuoc = new JTable(model);
		tblThuoc.setRowHeight(38);
		tblThuoc.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tblThuoc.setSelectionBackground(new Color(0xE3F2FD));
		tblThuoc.setGridColor(new Color(0xDDDDDD));
		tblThuoc.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		tblThuoc.setBackground(Color.WHITE);
		tblThuoc.setForeground(new Color(0x33, 0x33, 0x33));

		JTableHeader header = tblThuoc.getTableHeader();
		header.setFont(new Font("Times New Roman", Font.BOLD, 14));
		header.setBackground(Color.WHITE);
		header.setForeground(new Color(0x33, 0x33, 0x33));
		header.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
		
		JScrollPane scroll = new JScrollPane(tblThuoc);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
		scroll.getViewport().setBackground(Color.WHITE);
		scroll.setBackground(Color.WHITE);

		add(scroll, BorderLayout.CENTER);

		// ===== Sự kiện =====
		btnLamMoi.addActionListener(e -> onBtnLamMoi());
		btnXemChiTiet.addActionListener(e -> onBtnXemChiTiet());
		btnLichSuXoa.addActionListener(e -> onBtnLichSuXoa());
	}

	// ================== EVENT HANDLERS ==================
	private void onBtnLamMoi() {
		txtTenThuoc.setText("");
		cmbLoaiThuoc.setSelectedIndex(-1);
	}

	private void onBtnXemChiTiet() {
		/* TODO: xử lý xem chi tiết thuốc */
	}

	private void onBtnLichSuXoa() {
		/* TODO: hiển thị lịch sử xóa thuốc */
	}

	private void onBtnXoaTatCa() {
		/* TODO: xử lý xoá tất cả thuốc được chọn */
	}
}
