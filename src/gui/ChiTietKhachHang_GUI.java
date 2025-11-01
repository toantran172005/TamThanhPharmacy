package gui;

import javax.swing.*;
import controller.ToolCtrl;
import java.awt.*;

/**
 * Giao diện ChiTietKhachHang trắng đồng bộ
 * - Giữ nguyên bố cục
 * - Toàn bộ nền trắng như form ChiTietPhieuKNHT
 */
public class ChiTietKhachHang_GUI extends JPanel {

    private JTextField txtMaKH;
    private JTextField txtTenKH;
    private JTextField txtSdt;
    private JTextField txtTuoi;
    private JComboBox<String> cmbTrangThai;
    private JButton btnCapNhat;
    private JButton btnQuayLai;

    public ToolCtrl tool = new ToolCtrl();

    public ChiTietKhachHang_GUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE); // toàn form trắng

        // ====================== TOP ======================
        JLabel lblTitle = new JLabel("CHI TIẾT KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(lblTitle, BorderLayout.CENTER);

        // ====================== FORM ======================
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        formPanel.setBackground(Color.WHITE);

        // === ROW 1: Mã khách hàng ===
        formPanel.add(createFormRow("Mã khách hàng:", txtMaKH = tool.taoTextField("")));
        formPanel.add(Box.createVerticalStrut(30));

        // === ROW 2: Tên khách hàng ===
        formPanel.add(createFormRow("Tên khách hàng:", txtTenKH = tool.taoTextField("")));
        formPanel.add(Box.createVerticalStrut(30));

        // === ROW 3: Số điện thoại ===
        formPanel.add(createFormRow("Số điện thoại:", txtSdt = tool.taoTextField("")));
        formPanel.add(Box.createVerticalStrut(30));

        // === ROW 4: Tuổi ===
        formPanel.add(createFormRow("Tuổi:", txtTuoi = tool.taoTextField("")));
        formPanel.add(Box.createVerticalStrut(30));

        // === ROW 5: Trạng thái ===
        JPanel rowTrangThai = new JPanel(new GridBagLayout());
        rowTrangThai.setBackground(Color.WHITE);
        GridBagConstraints g5 = new GridBagConstraints();
        g5.fill = GridBagConstraints.HORIZONTAL;
        g5.weightx = 1.0;

        JPanel inner5 = new JPanel();
        inner5.setLayout(new BoxLayout(inner5, BoxLayout.X_AXIS));
        inner5.setMaximumSize(new Dimension(400, 50));
        inner5.setBackground(Color.WHITE);

        JLabel lblTrangThai = tool.taoLabel("Trạng thái:");
        lblTrangThai.setPreferredSize(new Dimension(130, 30));
        cmbTrangThai = tool.taoComboBox(new String[] { "Hoạt động", "Ngừng hoạt động" });
        cmbTrangThai.setMaximumSize(new Dimension(240, 35));

        inner5.add(Box.createHorizontalGlue());
        inner5.add(lblTrangThai);
        inner5.add(Box.createHorizontalStrut(50));
        inner5.add(cmbTrangThai);
        inner5.add(Box.createHorizontalGlue());

        rowTrangThai.add(inner5, g5);
        formPanel.add(rowTrangThai);
        formPanel.add(Box.createVerticalStrut(50));

        // === ROW 6: Buttons ===
        JPanel rowButtons = new JPanel(new GridBagLayout());
        rowButtons.setBackground(Color.WHITE);
        GridBagConstraints g6 = new GridBagConstraints();
        g6.fill = GridBagConstraints.HORIZONTAL;
        g6.weightx = 1.0;

        JPanel inner6 = new JPanel();
        inner6.setLayout(new BoxLayout(inner6, BoxLayout.X_AXIS));
        inner6.setMaximumSize(new Dimension(400, 80));
        inner6.setBackground(Color.WHITE);

        btnCapNhat = tool.taoButton("Cập nhật", "/picture/khachHang/edit.png");
        btnQuayLai = tool.taoButton("Quay lại", "/picture/khachHang/signOut.png");

        inner6.add(Box.createHorizontalGlue());
        inner6.add(btnCapNhat);
        inner6.add(Box.createHorizontalStrut(50));
        inner6.add(btnQuayLai);
        inner6.add(Box.createHorizontalGlue());

        rowButtons.add(inner6, g6);
        formPanel.add(rowButtons);

        // ====================== LAYOUT ======================
        add(topPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        // Sự kiện
        btnCapNhat.addActionListener(e -> onBtnCapNhat());
        btnQuayLai.addActionListener(e -> onBtnQuayLai());
    }

    // === Hàm tạo 1 dòng có label + textfield ===
    private JPanel createFormRow(String labelText, JTextField field) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.X_AXIS));
        inner.setMaximumSize(new Dimension(400, 50));
        inner.setBackground(Color.WHITE);

        JLabel label = tool.taoLabel(labelText);
        label.setPreferredSize(new Dimension(130, 30));
        field.setMaximumSize(new Dimension(240, 35));

        inner.add(Box.createHorizontalGlue());
        inner.add(label);
        inner.add(Box.createHorizontalStrut(50));
        inner.add(field);
        inner.add(Box.createHorizontalGlue());

        row.add(inner, g);
        return row;
    }

    public void onBtnCapNhat() {
        // TODO: xử lý cập nhật
    }

    public void onBtnQuayLai() {
        // TODO: xử lý quay lại
    }
}
