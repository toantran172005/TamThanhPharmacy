package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TimKiemHD_GUI extends JPanel {

    private JTable tblHoaDon;
    private JTextField txtKhachHang, txtTenNV;
    private JButton btnTimKiem, btnLamMoi, btnChiTiet, btnLichSuXoa;
    private TrangChuQL_GUI mainFrame; 
    
 // --- Thêm constructor nhận TrangChuQL_GUI ---
    public TimKiemHD_GUI(TrangChuQL_GUI mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }


    public void initUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1058, 509));
        setBackground(Color.WHITE);

        // ======== TOP PANEL ========
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(topPanel, BorderLayout.NORTH);

        // Tiêu đề
        JLabel lblTitle = new JLabel("DANH SÁCH HOÁ ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // HBox tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        searchPanel.setBackground(Color.WHITE);

        JLabel lblKhachHang = new JLabel("Tên khách hàng:");
        lblKhachHang.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtKhachHang = new JTextField(20);
        txtKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel lblNhanVien = new JLabel("Tên nhân viên:");
        lblNhanVien.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtTenNV = new JTextField(20);
        txtTenNV.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        btnTimKiem = taoButton("Tìm kiếm","/img/search.png");

        searchPanel.add(lblKhachHang);
        searchPanel.add(txtKhachHang);
        searchPanel.add(lblNhanVien);
        searchPanel.add(txtTenNV);
        searchPanel.add(btnTimKiem);

        topPanel.add(searchPanel, BorderLayout.CENTER);

        // HBox các nút bên dưới
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        buttonPanel.setBackground(Color.WHITE);

        btnLamMoi = taoButton("Làm mới", "/img/refresh.png");
        btnChiTiet = taoButton("Xem chi tiết", "/img/xemChiTiet.png");
        btnLichSuXoa = taoButton("Lịch sử xoá", "/img/document.png");

        JButton[] buttons = {btnLamMoi, btnChiTiet, btnLichSuXoa};
        for (JButton btn : buttons) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        buttonPanel.add(btnLamMoi);
        buttonPanel.add(btnChiTiet);
        buttonPanel.add(btnLichSuXoa);

        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ======== CENTER TABLE ========
        String[] columnNames = {"", "Mã hoá đơn", "Tên nhân viên", "Tên khách hàng", "Thời gian", "Tổng tiền hàng", "Hoạt động"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tblHoaDon = new JTable(model);
        tblHoaDon.setRowHeight(28);
        tblHoaDon.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(tblHoaDon);
        add(scrollPane, BorderLayout.CENTER);
        
        btnChiTiet.addActionListener(e -> {
            ChiTietHoaDon_GUI chiTietPanel = new ChiTietHoaDon_GUI(mainFrame);
            mainFrame.setUpNoiDung(chiTietPanel);
        });
    }

    // === Getters ===
    public JTable getTblHoaDon() { return tblHoaDon; }
    public JTextField getTxtKhachHang() { return txtKhachHang; }
    public JTextField getTxtTenNV() { return txtTenNV; }
    public JButton getBtnTimKiem() { return btnTimKiem; }
    public JButton getBtnLamMoi() { return btnLamMoi; }
    public JButton getBtnChiTiet() { return btnChiTiet; }
    public JButton getBtnLichSuXoa() { return btnLichSuXoa; }

    private JButton taoButton(String text, String imgPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(245, 245, 245));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imgPath));
            Image scaled = icon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            System.err.println("Không tìm thấy ảnh: " + imgPath);
        }
        return btn;
    }
}
