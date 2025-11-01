package gui;

import javax.swing.*;
import java.awt.*;
import controller.ToolCtrl;

public class ThemKhachHang_GUI extends JPanel {

    private JTextField txtTenKH;
    private JTextField txtSdt;
    private JTextField txtTuoi;
    private JButton btnLamMoi;
    private JButton btnThem;

    private final ToolCtrl tool = new ToolCtrl();

    public ThemKhachHang_GUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== TOP =====
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(20, 10, 30, 10));

        JLabel lblTitle = tool.taoLabel("THÊM KHÁCH HÀNG");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 22));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
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
        center.add(taoDong("Tên khách hàng:", txtTenKH = tool.taoTextField("Nhập tên..."), labelWidth, fieldWidth));
        center.add(Box.createVerticalStrut(rowSpacing));

        // --- Số điện thoại ---
        center.add(taoDong("Số điện thoại:", txtSdt = tool.taoTextField("Nhập số điện thoại..."), labelWidth, fieldWidth));
        center.add(Box.createVerticalStrut(rowSpacing));

        // --- Tuổi ---
        center.add(taoDong("Tuổi:", txtTuoi = tool.taoTextField("Nhập tuổi..."), labelWidth, fieldWidth));
        center.add(Box.createVerticalStrut(rowSpacing));

        add(center, BorderLayout.CENTER);

        // ===== BOTTOM =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        bottom.setBackground(Color.WHITE);

        btnLamMoi = tool.taoButton("Làm mới", "/picture/khachHang/return.png");
        btnThem = tool.taoButton("Thêm", "/picture/khachHang/addUser.png");

        bottom.add(btnLamMoi);
        bottom.add(btnThem);

        add(bottom, BorderLayout.SOUTH);

        // ===== EVENTS =====
        btnLamMoi.addActionListener(e -> onBtnLamMoi());
        btnThem.addActionListener(e -> onBtnThem());
    }

    // === Tạo 1 hàng label + field ===
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

    // ===== EVENT HANDLERS =====
    public void onBtnLamMoi() {
        txtTenKH.setText("");
        txtSdt.setText("");
        txtTuoi.setText("");
    }

    public void onBtnThem() {
        JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công (demo)");
    }

}
