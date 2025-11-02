package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import controller.ToolCtrl;

public class ThemKeThuoc_GUI extends JPanel {

    private JComboBox<String> cmbLoaiKe;
    private JTextField txtSucChua, txtMoTa;
    private JButton btnLamMoi, btnThem;
    private ToolCtrl tool = new ToolCtrl();

    public ThemKeThuoc_GUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 40, 20, 40));

        // ======= TIÊU ĐỀ =======
        JLabel lblTitle = tool.taoLabel("THÊM KỆ THUỐC");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);

        // ======= CENTER =======
        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.setBorder(new EmptyBorder(40, 100, 40, 100));

        pnlCenter.add(createInputRow("Loại kệ:", cmbLoaiKe = new JComboBox<>(new String[]{"Kệ A", "Kệ B", "Kệ C"}), "Chọn kệ hoặc nhập mới..."));
        pnlCenter.add(Box.createVerticalStrut(25));
        pnlCenter.add(createInputRow("Sức chứa:", txtSucChua = tool.taoTextField("Sức chứa dưới 900..."), null));
        pnlCenter.add(Box.createVerticalStrut(25));
        pnlCenter.add(createInputRow("Mô tả:", txtMoTa = tool.taoTextField("Mô tả..."), null));

        add(pnlCenter, BorderLayout.CENTER);

        // ======= BUTTONS =======
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
        pnlBottom.setBackground(Color.WHITE);

        btnLamMoi = tool.taoButton("Làm mới", "/picture/keThuoc/refresh.png");
        btnThem = tool.taoButton("Thêm", "/picture/keThuoc/edit.png");

        pnlBottom.add(btnLamMoi);
        pnlBottom.add(btnThem);
        add(pnlBottom, BorderLayout.SOUTH);

        // ======= SỰ KIỆN =======
        btnLamMoi.addActionListener(e -> lamMoi());
        btnThem.addActionListener(e -> themKeThuoc());
    }

    // ======= Hàm tạo từng hàng Label + Input =======
    private JPanel createInputRow(String label, JComponent input, String comboPrompt) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        row.setBackground(Color.WHITE);

        JLabel lbl = tool.taoLabel(label);
        lbl.setFont(new Font("Times New Roman", Font.BOLD, 15));
        lbl.setPreferredSize(new Dimension(100, 30));

        input.setPreferredSize(new Dimension(250, 35));
        input.setFont(new Font("Times New Roman", Font.PLAIN, 14));

        // Nếu là combo thì cho editable
        if (input instanceof JComboBox) {
            ((JComboBox<?>) input).setEditable(true);
            ((JComboBox<?>) input).setSelectedItem("");
            ((JComboBox<?>) input).setToolTipText(comboPrompt);
        }

        row.add(lbl);
        row.add(input);
        return row;
    }

    private void lamMoi() {
        cmbLoaiKe.setSelectedIndex(-1);
        txtSucChua.setText("");
        txtMoTa.setText("");
    }

    private void themKeThuoc() {
        String loaiKe = (String) cmbLoaiKe.getSelectedItem();
        String sucChua = txtSucChua.getText().trim();
        String moTa = txtMoTa.getText().trim();

        if (loaiKe == null || loaiKe.isEmpty() || sucChua.isEmpty() || moTa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "✅ Thêm kệ thuốc thành công!\n• Loại kệ: " + loaiKe +
                        "\n• Sức chứa: " + sucChua +
                        "\n• Mô tả: " + moTa,
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        lamMoi();
    }

}
