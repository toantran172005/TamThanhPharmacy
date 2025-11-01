package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import controller.ToolCtrl;

public class DonVi_GUI extends JPanel {

    private JTextField txtTimDV, txtTenDV;
    private JTable tblDonVi;
    private JButton btnLamMoi, btnLichSuXoa, btnXoaTatCa, btnThemDV;
    private JCheckBox chkChonTatCa;

    private final ToolCtrl tool = new ToolCtrl();

    public DonVi_GUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== TOP =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JLabel lblTitle = new JLabel("ĐƠN VỊ THUỐC", SwingConstants.CENTER);
        lblTitle.setFont(new Font("System", Font.BOLD, 18));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(lblTitle);
        topPanel.add(Box.createVerticalStrut(10));

        // ===== Hàng chính gồm 2 bên =====
        JPanel mainBox = new JPanel();
        mainBox.setLayout(new BoxLayout(mainBox, BoxLayout.X_AXIS));
        mainBox.setBackground(Color.WHITE);
        mainBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== LEFT: Tìm kiếm + nút =====
        JPanel leftVBox = new JPanel();
        leftVBox.setLayout(new BoxLayout(leftVBox, BoxLayout.Y_AXIS));
        leftVBox.setBackground(Color.WHITE);
        leftVBox.setPreferredSize(new Dimension(480, 120));

        txtTimDV = tool.taoTextField("Nhập tên đơn vị...");
        leftVBox.add(taoDong("Tìm đơn vị:", txtTimDV, 120, 200));
        leftVBox.add(Box.createVerticalStrut(15));

        JPanel btnRowLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        btnRowLeft.setBackground(Color.WHITE);

        btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/return.png");
        btnLichSuXoa = tool.taoButton("Lịch sử xoá", "/picture/nhanVien/document.png");
        btnXoaTatCa = tool.taoButton("Xoá tất cả", "/picture/nhanVien/trash.png");

        btnRowLeft.add(btnLamMoi);
        btnRowLeft.add(btnLichSuXoa);

        leftVBox.add(btnRowLeft);

        // ===== RIGHT: Thêm đơn vị =====
        JPanel rightVBox = new JPanel();
        rightVBox.setLayout(new BoxLayout(rightVBox, BoxLayout.Y_AXIS));
        rightVBox.setBackground(Color.WHITE);
        rightVBox.setPreferredSize(new Dimension(420, 120));

        JLabel lblThem = new JLabel("Thêm đơn vị", SwingConstants.CENTER);
        lblThem.setFont(new Font("System", Font.BOLD, 15));
        lblThem.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightVBox.add(lblThem);
        rightVBox.add(Box.createVerticalStrut(10));

        txtTenDV = tool.taoTextField("Nhập tên đơn vị...");
        btnThemDV = tool.taoButton("Thêm", "/picture/hoaDon/plus.png");

        JPanel themRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        themRow.setBackground(Color.WHITE);
        JLabel lblTenDV = tool.taoLabel("Tên đơn vị:");
        lblTenDV.setFont(new Font("System", Font.BOLD, 15));
        themRow.add(lblTenDV);
        themRow.add(txtTenDV);
        themRow.add(btnThemDV);

        rightVBox.add(themRow);

        // ===== Ghép 2 bên =====
        mainBox.add(leftVBox);
        mainBox.add(Box.createHorizontalStrut(50));
        mainBox.add(rightVBox);

        topPanel.add(mainBox);
        topPanel.add(Box.createVerticalStrut(10));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        topPanel.add(sep);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER: Table =====
        String[] cols = {"STT", "Mã Đơn Vị", "Tên Đơn Vị", "Hoạt Động"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        tblDonVi = new JTable(model);
        tblDonVi.setRowHeight(50);
        tblDonVi.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tblDonVi.setSelectionBackground(new Color(0xE3F2FD));
        tblDonVi.setGridColor(new Color(0xDDDDDD));
        tblDonVi.setBackground(Color.WHITE);

        JTableHeader header = tblDonVi.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(0x33, 0x33, 0x33));

        tblDonVi.getColumnModel().getColumn(0).setMaxWidth(40);

        JScrollPane scroll = new JScrollPane(tblDonVi);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        // ===== SỰ KIỆN =====
        btnThemDV.addActionListener(e -> onBtnThemDV());
        btnLamMoi.addActionListener(e -> onBtnLamMoi());
        btnLichSuXoa.addActionListener(e -> onBtnLichSuXoa());
    }

    private JPanel taoDong(String label, JComponent comp, int labelWidth, int fieldWidth) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setBackground(Color.WHITE);

        JLabel lbl = tool.taoLabel(label);
        lbl.setPreferredSize(new Dimension(labelWidth, 25));
        row.add(lbl);

        comp.setPreferredSize(new Dimension(fieldWidth, 30));
        row.add(comp);

        return row;
    }

    // ==== SỰ KIỆN ====
    private void onBtnThemDV() {
        // TODO: Xử lý thêm đơn vị
        JOptionPane.showMessageDialog(this, "Thêm đơn vị thành công!");
    }

    private void onBtnLamMoi() {
        txtTimDV.setText("");
        txtTenDV.setText("");
    }

    private void onBtnXoaTatCa() {
        // TODO
        JOptionPane.showMessageDialog(this, "Đã xoá tất cả!");
    }

    private void onBtnLichSuXoa() {
        // TODO
        JOptionPane.showMessageDialog(this, "Mở lịch sử xoá!");
    }
}
