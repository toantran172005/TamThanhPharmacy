package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.ToolCtrl;

import java.awt.*;

public class TimKiemPhieuDoiTra_GUI extends JPanel {
    private JTextField txtKhachHang, txtTenNV;
    private JButton btnTimKiem, btnLamMoi, btnChiTiet;
    private JTable tblPhieuDoiTra;
    private TrangChuQL_GUI mainFrame;
    Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

	public TimKiemPhieuDoiTra_GUI(TrangChuQL_GUI mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    public void initUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1134, 617));
        setBackground(Color.WHITE);

        // ===== TOP PANEL =====
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

        JLabel lblKH = new JLabel("Tên khách hàng:");
        lblKH.setFont(font2);
        txtKhachHang = tool.taoTextField("Tên khách hàng...");

        JLabel lblNV = new JLabel("Tên nhân viên:");
        lblNV.setFont(font2);
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

        // ===== CENTER (TABLE) =====
        String[] colNames = {"", "Mã phiếu", "Tên nhân viên", "Tên khách hàng", "Số điện thoại", "Ngày lập", "Lý do"};
        DefaultTableModel model = new DefaultTableModel(colNames, 0);
        tblPhieuDoiTra = new JTable(model);

        tblPhieuDoiTra.setRowHeight(28);
        tblPhieuDoiTra.getTableHeader().setFont(font2);
        tblPhieuDoiTra.setFont(font2);

        // Thêm checkbox cột đầu
        tblPhieuDoiTra.getColumnModel().getColumn(0).setMaxWidth(40);
        tblPhieuDoiTra.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        tblPhieuDoiTra.getColumnModel().getColumn(0).setCellRenderer(tblPhieuDoiTra.getDefaultRenderer(Boolean.class));

        JScrollPane scrollPane = new JScrollPane(tblPhieuDoiTra);
        add(scrollPane, BorderLayout.CENTER);
        
        btnChiTiet.addActionListener(e -> {
            ChiTietPhieuDoiTra_GUI chiTietPanel = new ChiTietPhieuDoiTra_GUI(mainFrame);
            mainFrame.setUpNoiDung(chiTietPanel);
        });
    }

}
