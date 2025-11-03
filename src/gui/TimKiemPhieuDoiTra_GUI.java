package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controller.ChiTietHoaDonCtrl;
import controller.ChiTietPhieuDoiTraCtrl;
import controller.TimKiemHDCtrl;
import controller.TimKiemPhieuDoiTraCtrl;
import controller.ToolCtrl;
import java.awt.*;

public class TimKiemPhieuDoiTra_GUI extends JPanel {
    private JTextField txtKhachHang, txtTenNV;
    private JButton btnTimKiem, btnLamMoi, btnChiTiet;
    private JTable tblPhieuDoiTra;
    private TrangChuQL_GUI mainFrame;
    private TrangChuNV_GUI mainFrameNV;

    Font font1 = new Font("Times New Roman", Font.BOLD, 18);
    Font font2 = new Font("Times New Roman", Font.PLAIN, 15);
    public ToolCtrl tool = new ToolCtrl();

    // Constructor cho Quản lý
    public TimKiemPhieuDoiTra_GUI(TrangChuQL_GUI mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
        new TimKiemPhieuDoiTraCtrl(this);
    }

    // Constructor cho Nhân viên
    public TimKiemPhieuDoiTra_GUI(TrangChuNV_GUI mainFrameNV) {
        this.mainFrameNV = mainFrameNV;
        initUI();
        new TimKiemPhieuDoiTraCtrl(this);
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1134, 617));
        setBackground(Color.WHITE);

        // ========== TOP PANEL ==========
        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tiêu đề
        JLabel lblTitle = new JLabel("DANH SÁCH PHIẾU ĐỔI TRẢ", SwingConstants.CENTER);
        lblTitle.setFont(font1);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlTop.add(lblTitle);
        pnlTop.add(Box.createVerticalStrut(10));

        // Hàng tìm kiếm
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
        row1.setBackground(Color.WHITE);

        JLabel lblKH = tool.taoLabel("Tên khách hàng:");
        txtKhachHang = tool.taoTextField("Tên khách hàng...");
        JLabel lblNV = tool.taoLabel("Tên nhân viên:");
        txtTenNV = tool.taoTextField("Tên nhân viên...");
        btnTimKiem = tool.taoButton("Tìm kiếm", "/picture/hoaDon/search.png");

        row1.add(lblKH);
        row1.add(txtKhachHang);
        row1.add(lblNV);
        row1.add(txtTenNV);
        row1.add(btnTimKiem);
        pnlTop.add(row1);

        // Hàng chức năng
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 5));
        row2.setBackground(Color.WHITE);
        btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/refresh.png");
        btnChiTiet = tool.taoButton("Xem chi tiết", "/picture/hoaDon/xemChiTiet.png");
        row2.add(btnLamMoi);
        row2.add(btnChiTiet);
        pnlTop.add(row2);

        add(pnlTop, BorderLayout.NORTH);

        // ========== CENTER (TABLE) ==========
        String[] colNames = {"Mã phiếu", "Tên nhân viên", "Tên khách hàng", "Ngày lập", "Lý do"};
        DefaultTableModel model = new DefaultTableModel(colNames, 0);
        tblPhieuDoiTra = new JTable(model);
        tblPhieuDoiTra.setRowHeight(28);
        tblPhieuDoiTra.getTableHeader().setFont(font2);
        tblPhieuDoiTra.setFont(font2);

        // Thiết kế bảng
        tblPhieuDoiTra.setBackground(Color.WHITE);
        tblPhieuDoiTra.getTableHeader().setBackground(new Color(240, 240, 240));
        tblPhieuDoiTra.setGridColor(new Color(200, 200, 200));
        tblPhieuDoiTra.setShowGrid(true);
        tblPhieuDoiTra.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        JScrollPane scrollPane = new JScrollPane(tblPhieuDoiTra);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Căn giữa nội dung
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tblPhieuDoiTra.getColumnCount(); i++) {
            tblPhieuDoiTra.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Căn giữa tiêu đề cột
        JTableHeader header = tblPhieuDoiTra.getTableHeader();
        header.setBackground(new Color(240, 240, 240));
        header.setFont(font2);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        add(scrollPane, BorderLayout.CENTER);

        // Focus mặc định
        txtKhachHang.requestFocusInWindow();
    }

    // ========== GETTER ==========
    public JTextField getTxtKhachHang() { return txtKhachHang; }
    public JTextField getTxtTenNV() { return txtTenNV; }
    public JButton getBtnTimKiem() { return btnTimKiem; }
    public JButton getBtnLamMoi() { return btnLamMoi; }
    public JButton getBtnChiTiet() { return btnChiTiet; }
    public JTable getTblPhieuDoiTra() { return tblPhieuDoiTra; }
    public TrangChuQL_GUI getMainFrame() { return mainFrame; }
    public TrangChuNV_GUI getMainFrameNV() { return mainFrameNV; }

    // ========== SETTER ==========
    public void setMainFrame(TrangChuQL_GUI mainFrame) { this.mainFrame = mainFrame; }
    public void setMainFrameNV(TrangChuNV_GUI mainFrameNV) { this.mainFrameNV = mainFrameNV; }
}