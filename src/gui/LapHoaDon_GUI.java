package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.ToolCtrl;

import java.awt.*;

public class LapHoaDon_GUI extends JPanel {
	private JTextField txtSdt, txtTenKH, txtTuoi, txtSoLuong, txtTienNhan;
	private JComboBox<String> cmbSanPham, cmbDonVi, cmbHTThanhToan;
	private JLabel lblTongTien, lblTienThua;
	private JTable tblThuoc;
	private JButton btnThem, btnLamMoi, btnTaoHD;
	Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

	public LapHoaDon_GUI() {
		setLayout(new BorderLayout(0, 15));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(1027, 900));
		setBorder(new EmptyBorder(10, 20, 10, 20));

		// ===================== PHẦN TRÊN =====================
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
		pnlTop.setBackground(Color.WHITE);
		pnlTop.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblTitle = new JLabel("LẬP HÓA ĐƠN", SwingConstants.CENTER);
		lblTitle.setFont(font1);
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblTitle);
		pnlTop.add(Box.createVerticalStrut(10));
		pnlTop.add(new JSeparator());

		// --- Dòng 1: SDT & Tên KH ---
		JPanel row1 = taoHang();
		row1.add(taoLabel("Số điện thoại:"));
		txtSdt = tool.taoTextField("Số điện thoại...");
		row1.add(txtSdt);
		row1.add(taoLabel("Tên khách hàng:"));
		txtTenKH = tool.taoTextField("Tên khách hàng...");
		row1.add(txtTenKH);
		pnlTop.add(row1);

		// --- Dòng 2: Tuổi & Sản phẩm ---
		JPanel row2 = taoHang();
		row2.add(taoLabel("Tuổi:"));
		txtTuoi = tool.taoTextField("Tuổi...");
		row2.add(txtTuoi);
		row2.add(taoLabel("Sản phẩm:"));
		cmbSanPham = tool.taoComboBox(new String[] {});
		cmbSanPham.setEditable(true);
		cmbSanPham.setPreferredSize(new Dimension(180, 28));
		row2.add(cmbSanPham);
		pnlTop.add(row2);

		// --- Dòng 3: Số lượng, Đơn vị, nút Thêm ---
		JPanel row3 = taoHang();
		row3.add(taoLabel("Số lượng:"));
		txtSoLuong = tool.taoTextField("Nhập số lượng...");
		row3.add(txtSoLuong);

		row3.add(taoLabel("Đơn vị tính:"));
		cmbDonVi = tool.taoComboBox(new String[] {});
		cmbDonVi.setPreferredSize(new Dimension(150, 28));
		row3.add(cmbDonVi);

		btnThem = tool.taoButton("Thêm", "/picture/hoaDon/plus.png");
		row3.add(btnThem);
		pnlTop.add(row3);

		add(pnlTop, BorderLayout.NORTH);

		// ===================== PHẦN GIỮA: BẢNG =====================
		String[] cols = { "STT", "Tên thuốc", "Số lượng", "Đơn vị", "Đơn giá", "Thành tiền", "Ghi chú", "Hoạt động" };
		DefaultTableModel model = new DefaultTableModel(cols, 0);
		tblThuoc = new JTable(model);
		tblThuoc.setRowHeight(25);
		tblThuoc.setFont(font2);
		tblThuoc.getTableHeader().setFont(font2);

		JScrollPane scroll = new JScrollPane(tblThuoc);
		add(scroll, BorderLayout.CENTER);

		// ===================== PHẦN DƯỚI =====================
		JPanel pnlBottom = new JPanel();
		pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
		pnlBottom.setBackground(Color.WHITE);
		pnlBottom.setBorder(new EmptyBorder(10, 0, 10, 0));

		// --- Thanh toán ---
		JPanel row4 = taoHangTrai();
		row4.add(taoLabel("Phương thức thanh toán:"));
		cmbHTThanhToan = tool.taoComboBox(new String[] {"Tiền mặt", "Chuyển khoản"});
		cmbHTThanhToan.setPreferredSize(new Dimension(150, 28));
		row4.add(cmbHTThanhToan);
		pnlBottom.add(row4);

		// --- Tổng tiền ---
		JPanel row5 = taoHangTrai();
		row5.add(taoLabel("Tổng tiền:"));
		lblTongTien = new JLabel("0 VND");
		lblTongTien.setFont(font2);
		row5.add(lblTongTien);
		pnlBottom.add(row5);

		// --- Tiền nhận ---
		JPanel row6 = taoHangTrai();
		row6.add(taoLabel("Tiền nhận:"));
		txtTienNhan = tool.taoTextField("Tiền nhận...");
		row6.add(txtTienNhan);
		pnlBottom.add(row6);

		// --- Tiền thừa ---
		JPanel row7 = taoHangTrai();
		row7.add(taoLabel("Tiền thừa:"));
		lblTienThua = new JLabel("0 VND");
		lblTienThua.setFont(font2);
		row7.add(lblTienThua);
		pnlBottom.add(row7);

		// --- Nút thao tác ---
		JPanel row8 = taoHang();
		row8.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/refresh.png");
		btnTaoHD = tool.taoButton("Tạo hoá đơn", "/picture/hoaDon/plus.png");
		row8.add(btnLamMoi);
		row8.add(Box.createHorizontalStrut(50));
		row8.add(btnTaoHD);
		pnlBottom.add(row8);

		add(pnlBottom, BorderLayout.SOUTH);
	}

	// ====== HÀM TẠO CÁC THÀNH PHẦN CHUNG ======
	private JPanel taoHang() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
		p.setBackground(Color.WHITE);
		return p;
	}

	private JPanel taoHangTrai() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		p.setBackground(Color.WHITE);
		return p;
	}

	private JLabel taoLabel(String text) {
		JLabel lbl = new JLabel(text);
		lbl.setFont(font2);
		return lbl;
	}

	private JTextField taoTextField(String prompt, int width) {
		JTextField txt = new JTextField(prompt);
		txt.setPreferredSize(new Dimension(width, 28));
		txt.setFont(font2);
		txt.setForeground(Color.GRAY);
		return txt;
	}

}
