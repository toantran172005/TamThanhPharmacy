package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class ChiTietHoaDon_GUI extends JPanel {
    private JLabel lblDiaChi, lblHotline, lblMaHD, lblNgayLap, lblNhanVien, lblKhachHang;
    private JLabel lblGhiChu, lblTongTien, lblTienNhan, lblTienThua;
    private JTable tblThuoc;
    private JButton btnInHoaDon, btnQuayLai, btnTaoPhieuDoiTra;
    private TrangChuQL_GUI mainFrame;
    
    public ChiTietHoaDon_GUI(TrangChuQL_GUI mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    public void initUI() {
    	setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1134, 617));
        setBackground(Color.WHITE);

     // --- TOP PANEL ---
        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setBackground(Color.WHITE);

        // Tên hiệu thuốc
        JLabel lblTieuDe = new JLabel("HIỆU THUỐC TAM THANH", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 16));
        lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlTop.add(lblTieuDe);

        // Địa chỉ (center)
        pnlTop.add(Box.createVerticalStrut(10));
        JPanel pnlDiaChi = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlDiaChi.setBackground(Color.WHITE);
        JLabel lblDC = new JLabel("Địa chỉ:");
        lblDC.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 15));
        lblDiaChi = new JLabel("");
        lblDiaChi.setFont(new Font("SansSerif", Font.PLAIN, 15));
        pnlDiaChi.add(lblDC);
        pnlDiaChi.add(lblDiaChi);
        pnlTop.add(pnlDiaChi);

        // Hotline (center)
        JPanel pnlHotline = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlHotline.setBackground(Color.WHITE);
        JLabel lblHL = new JLabel("Hotline:");
        lblHL.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 15));
        lblHotline = new JLabel("");
        lblHotline.setFont(new Font("SansSerif", Font.PLAIN, 15));
        pnlHotline.add(lblHL);
        pnlHotline.add(lblHotline);
        pnlTop.add(pnlHotline);

        // Tiêu đề chi tiết hoá đơn
        pnlTop.add(Box.createVerticalStrut(10));
        JLabel lblChiTiet = new JLabel("CHI TIẾT HOÁ ĐƠN", SwingConstants.CENTER);
        lblChiTiet.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblChiTiet.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlTop.add(lblChiTiet);


        pnlTop.add(Box.createVerticalStrut(10));
        pnlTop.add(taoDongThongTin("Mã hoá đơn:", lblMaHD = new JLabel("")));
        pnlTop.add(taoDongThongTin("Ngày lập:", lblNgayLap = new JLabel("")));
        pnlTop.add(taoDongThongTin("Nhân viên:", lblNhanVien = new JLabel("")));
        pnlTop.add(taoDongThongTin("Khách hàng:", lblKhachHang = new JLabel("")));

        add(pnlTop, BorderLayout.NORTH);

        // --- CENTER TABLE ---
        String[] columns = {"Tên thuốc", "Số lượng", "Đơn vị", "Đơn giá", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        tblThuoc = new JTable(model);
        tblThuoc.setRowHeight(25);
        tblThuoc.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(tblThuoc);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách thuốc"));

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.add(scroll, BorderLayout.CENTER);

        JPanel pnlGhiChu = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        pnlGhiChu.add(new JLabel("Ghi chú:"));
        lblGhiChu = new JLabel("");
        pnlGhiChu.add(lblGhiChu);
        pnlCenter.add(pnlGhiChu, BorderLayout.SOUTH);

        add(pnlCenter, BorderLayout.CENTER);

        // --- BOTTOM PANEL ---
        JPanel pnlBottom = new JPanel();
        pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
        pnlBottom.setBorder(new EmptyBorder(10, 0, 10, 0));
        pnlBottom.setBackground(Color.WHITE);

        pnlBottom.add(taoDongThongTin("Tổng tiền:", lblTongTien = new JLabel("")));
        pnlBottom.add(taoDongThongTin("Tiền nhận:", lblTienNhan = new JLabel("")));
        pnlBottom.add(taoDongThongTin("Tiền thừa:", lblTienThua = new JLabel("")));

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 10));
        pnlButtons.setBackground(Color.WHITE);

        btnInHoaDon = taoButton("In hoá đơn", "/img/print.png");
        btnQuayLai = taoButton("Quay lại", "/img/signOut.png");
        btnTaoPhieuDoiTra = taoButton("Tạo phiếu đổi trả", "/img/plus.png");

        pnlButtons.add(btnInHoaDon);
        pnlButtons.add(btnQuayLai);
        pnlButtons.add(btnTaoPhieuDoiTra);

        pnlBottom.add(pnlButtons);
        add(pnlBottom, BorderLayout.SOUTH);
        
        btnQuayLai.addActionListener(e -> {
            mainFrame.setUpNoiDung(new TimKiemHD_GUI(mainFrame));
        });
    }

    private JPanel taoDongThongTin(String ten, JLabel lbl) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        panel.setBackground(Color.WHITE);
        JLabel lblTen = new JLabel(ten);
        lblTen.setFont(new Font("SansSerif", Font.BOLD, 15));
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblTen);
        panel.add(lbl);
        return panel;
    }

    // ====== HÀM TẠO NÚT VỚI ẢNH ======
    private JButton taoButton(String text, String imgPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setBackground(new Color(245, 245, 245));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imgPath));
            Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            System.err.println("Không tìm thấy ảnh: " + imgPath);
        }

        return btn;
    }
}
