package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import com.toedter.calendar.JDateChooser;
import controller.ToolCtrl;

public class ThemKhieuNai_GUI extends JPanel {

    private JTextField txtTenKhachHang;
    private JComboBox<String> cmbLoaiDon;
    private JTextArea txaNoiDung;
    private JButton btnQuayLai, btnLamMoi, btnThem;
    private JDateChooser dateNgayLap;

    private final ToolCtrl tool = new ToolCtrl();

    public ThemKhieuNai_GUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== TOP =====
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(20, 10, 30, 10));

        JLabel lblTitle = tool.taoLabel("THÊM KHIẾU NẠI & HỖ TRỢ KHÁCH HÀNG");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 22));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setVerticalAlignment(SwingConstants.CENTER);
        lblTitle.setPreferredSize(new Dimension(600, 40));

        top.add(lblTitle, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        // ===== CENTER =====
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);

        int rowSpacing = 20;
        int labelWidth = 150;
        int fieldWidth = 300;

        center.add(Box.createVerticalStrut(10));

        // --- Tên khách hàng ---
        center.add(taoDong("Tên khách hàng:", txtTenKhachHang = tool.taoTextField(""), labelWidth, fieldWidth));
        center.add(Box.createVerticalStrut(rowSpacing));

        // --- Ngày lập ---
        dateNgayLap = tool.taoDateChooser();
        center.add(taoDong("Ngày lập:", dateNgayLap, labelWidth, fieldWidth));
        center.add(Box.createVerticalStrut(rowSpacing));

        // --- Loại đơn ---
        cmbLoaiDon = tool.taoComboBox(new String[] { "Khiếu nại", "Hỗ trợ", "Khác" });
        center.add(taoDong("Loại đơn:", cmbLoaiDon, labelWidth, fieldWidth));
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

        btnQuayLai = tool.taoButton("Quay lại", "/picture/nhanVien/signOut.png");
        btnLamMoi = tool.taoButton("Làm mới", "/picture/nhanVien/refresh.png");
        btnThem = tool.taoButton("Thêm", "/picture/nhanVien/plus.png");

        bottom.add(btnQuayLai);
        bottom.add(btnLamMoi);
        bottom.add(btnThem);
        add(bottom, BorderLayout.SOUTH);

        // ===== SỰ KIỆN =====
        btnQuayLai.addActionListener(e -> onQuayLai());
        btnLamMoi.addActionListener(e -> onLamMoi());
        btnThem.addActionListener(e -> onThem());
    }

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

    // ===== SỰ KIỆN =====
    public void onQuayLai() {
        JOptionPane.showMessageDialog(this, "Quay lại clicked");
    }

    public void onLamMoi() {
        txtTenKhachHang.setText("");
        cmbLoaiDon.setSelectedIndex(0);
        txaNoiDung.setText("");
        dateNgayLap.setDate(null);
    }

    public void onThem() {
        JOptionPane.showMessageDialog(this, "Thêm khiếu nại thành công (demo)");
    }

}
