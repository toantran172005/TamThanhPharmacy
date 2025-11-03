package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import controller.TimKiemHDCtrl;
import controller.ToolCtrl;
import java.awt.*;

public class TimKiemHD_GUI extends JPanel {
	public JTable tblHoaDon;
	public JTextField txtKhachHang, txtTenNV;
    public JButton btnTimKiem, btnLamMoi, btnChiTiet, btnLichSuXoa, btnXoaHoanTac;
    public TrangChuQL_GUI mainFrame;
    public TrangChuNV_GUI mainFrameNV;

    Font font1 = new Font("Times New Roman", Font.BOLD, 18);
    Font font2 = new Font("Times New Roman", Font.PLAIN, 15);
    public ToolCtrl tool = new ToolCtrl();

    // Constructor
    public TimKiemHD_GUI(TrangChuQL_GUI mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
        new TimKiemHDCtrl(this); // Khởi tạo controller
    }

    public TimKiemHD_GUI(TrangChuNV_GUI mainFrameNV) {
        this.mainFrameNV = mainFrameNV;
        initUI();
        new TimKiemHDCtrl(this); // Khởi tạo controller
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
        lblTitle.setFont(font1);
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        searchPanel.setBackground(Color.WHITE);

        JLabel lblKhachHang = tool.taoLabel("Tên khách hàng: ");
        txtKhachHang = tool.taoTextField("Tên khách hàng...");
        JLabel lblNhanVien = tool.taoLabel("Tên nhân viên: ");
        txtTenNV = tool.taoTextField("Tên nhân viên...");
        btnTimKiem = tool.taoButton("Tìm kiếm", "/picture/hoaDon/search.png");

        searchPanel.add(lblKhachHang);
        searchPanel.add(txtKhachHang);
        searchPanel.add(lblNhanVien);
        searchPanel.add(txtTenNV);
        searchPanel.add(btnTimKiem);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        // các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        buttonPanel.setBackground(Color.WHITE);
        btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/refresh.png");
        btnChiTiet = tool.taoButton("Xem chi tiết", "/picture/hoaDon/xemChiTiet.png");
        btnLichSuXoa = tool.taoButton("Lịch sử xoá", "/picture/hoaDon/document.png");
        btnXoaHoanTac = tool.taoButton("Xóa", "/picture/hoaDon/trash.png");

        buttonPanel.add(btnLamMoi);
        buttonPanel.add(btnChiTiet);
        buttonPanel.add(btnLichSuXoa);
        buttonPanel.add(btnXoaHoanTac);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ========== CENTER TABLE ==========
        String[] columnNames = {"Mã hoá đơn", "Tên nhân viên", "Tên khách hàng", "Thời gian", "Tổng tiền hàng"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Bảng không cho chỉnh sửa
            }
        };
        tblHoaDon = new JTable(model);
        tblHoaDon.setRowHeight(28);
        tblHoaDon.getTableHeader().setFont(font2);
        tblHoaDon.setFont(font2);
        tblHoaDon.setBackground(Color.WHITE);
        tblHoaDon.getTableHeader().setBackground(new Color(240, 240, 240));
        tblHoaDon.setGridColor(new Color(200, 200, 200));
        tblHoaDon.setShowGrid(true);
        tblHoaDon.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        JScrollPane scrollPane = new JScrollPane(tblHoaDon);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Căn giữa nội dung
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tblHoaDon.getColumnCount(); i++) {
            tblHoaDon.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Căn giữa header
        JTableHeader header = tblHoaDon.getTableHeader();
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    }

    // ========== Getters ==========
    public JTable getTblHoaDon() { return tblHoaDon; }
    public JTextField getTxtKhachHang() { return txtKhachHang; }
    public JTextField getTxtTenNV() { return txtTenNV; }
    public JButton getBtnTimKiem() { return btnTimKiem; }
    public JButton getBtnLamMoi() { return btnLamMoi; }
    public JButton getBtnChiTiet() { return btnChiTiet; }
    public JButton getBtnLichSuXoa() { return btnLichSuXoa; }
    public TrangChuQL_GUI getMainFrame() { return mainFrame; }
    public TrangChuNV_GUI getMainFrameNV() { return mainFrameNV; }
    public JButton getBtnXoaHoanTac() { return btnXoaHoanTac;}
}