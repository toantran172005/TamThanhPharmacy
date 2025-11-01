package gui;

import java.awt.*;
import javax.swing.*;

import controller.ToolCtrl;

public class ChiTietPhieuKNHT_GUI extends JPanel {

	private ToolCtrl tool = new ToolCtrl();

	private JTextField txtTenKH, txtSdt, txtTenNV;
	private JComboBox<String> cmbLoaiDon, cmbTrangThai;
	private JTextArea txaNoiDung;
	private JButton btnCapNhat, btnQuayLai;

	public ChiTietPhieuKNHT_GUI() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// ===== TOP =====
		JPanel top = new JPanel(new BorderLayout());
		top.setBackground(Color.WHITE);
		top.setBorder(BorderFactory.createEmptyBorder(20, 10, 30, 10));

		JLabel lblTitle = tool.taoLabel("CHI TIẾT PHIẾU KHIẾU NẠI & HỖ TRỢ");
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa theo chiều ngang
		lblTitle.setVerticalAlignment(SwingConstants.CENTER);
		lblTitle.setPreferredSize(new Dimension(600, 40)); // giúp tránh text bị cắt

		top.add(lblTitle, BorderLayout.CENTER);
		add(top, BorderLayout.NORTH);

		// ===== CENTER =====
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		center.setBackground(Color.WHITE);

		// spacing giữa các hàng
		int rowSpacing = 20;
		int labelWidth = 150;
		int fieldWidth = 300;

		center.add(Box.createVerticalStrut(10));

		// --- Tên khách hàng ---
		center.add(taoDong("Tên khách hàng:", txtTenKH = tool.taoTextField(""), labelWidth, fieldWidth));
		center.add(Box.createVerticalStrut(rowSpacing));

		// --- Số điện thoại ---
		center.add(taoDong("Số điện thoại:", txtSdt = tool.taoTextField(""), labelWidth, fieldWidth));
		center.add(Box.createVerticalStrut(rowSpacing));

		// --- Tên nhân viên ---
		center.add(taoDong("Tên nhân viên:", txtTenNV = tool.taoTextField(""), labelWidth, fieldWidth));
		center.add(Box.createVerticalStrut(rowSpacing));

		// --- Loại đơn ---
		cmbLoaiDon = tool.taoComboBox(new String[] { "Khiếu nại", "Hỗ trợ", "Khác" });
		center.add(taoDong("Loại đơn:", cmbLoaiDon, labelWidth, fieldWidth));
		center.add(Box.createVerticalStrut(rowSpacing));

		// --- Trạng thái ---
		cmbTrangThai = tool.taoComboBox(new String[] { "Hoạt động", "Ngừng hoạt động" });
		center.add(taoDong("Trạng thái:", cmbTrangThai, labelWidth, fieldWidth));
		center.add(Box.createVerticalStrut(rowSpacing));

		// --- Nội dung ---
		txaNoiDung = tool.taoTextArea(115);
		JScrollPane scroll = new JScrollPane(txaNoiDung);
		scroll.setPreferredSize(new Dimension(fieldWidth, 115));
		center.add(taoDong("Nội dung:", scroll, labelWidth, fieldWidth));

		add(center, BorderLayout.CENTER);

		// ===== BOTTOM =====
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
		bottom.setBackground(Color.WHITE);

		btnCapNhat = tool.taoButton("Cập nhật", "/picture/khachHang/edit.png");
		btnQuayLai = tool.taoButton("Quay lại", "/picture/khachHang/refresh.png");

		bottom.add(btnCapNhat);
		bottom.add(btnQuayLai);
		add(bottom, BorderLayout.SOUTH);
	}

	// === Hàm tạo 1 hàng label + control ===
	private JPanel taoDong(String text, JComponent comp, int labelWidth, int fieldWidth) {
		JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
		row.setBackground(Color.WHITE);

		JLabel lbl = tool.taoLabel(text);
		lbl.setPreferredSize(new Dimension(labelWidth, 25));
		comp.setPreferredSize(new Dimension(fieldWidth, comp.getPreferredSize().height));

		row.add(lbl);
		row.add(comp);
		return row;
	}
	
	public static void main(String[] args) {
	    JFrame f = new JFrame("Chi tiết phiếu KN & HT");
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setContentPane(new ChiTietPhieuKNHT_GUI());
	    f.pack();
	    f.setLocationRelativeTo(null);
	    f.setVisible(true);
	}

}
