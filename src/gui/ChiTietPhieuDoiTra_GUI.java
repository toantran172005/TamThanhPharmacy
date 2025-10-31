package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ChiTietPhieuDoiTra_GUI extends JPanel{

    private JLabel lblDiaChi, lblHotline, lblMaPhieuDT, lblMaHD, lblNgayLap,
                   lblNhanVien, lblKhachHang, lblLyDo, lblTongTienHoan;
    private JTable tblThuoc;
    private JButton btnInPhieu, btnQuayLai;
    private TrangChuQL_GUI mainFrame;

	public ChiTietPhieuDoiTra_GUI(TrangChuQL_GUI mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    public void initUI() {
    	setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1058, 509));
		setBackground(Color.WHITE);

        // ======= TOP (Thông tin hiệu thuốc + phiếu) =======
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTieuDe = new JLabel("HIỆU THUỐC TAM THANH", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 18));
        lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(lblTieuDe);

        JLabel lblDiaChiTitle = new JLabel("Địa chỉ: ");
        lblDiaChiTitle.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 15));
        lblDiaChi = new JLabel("");
        lblDiaChi.setFont(new Font("SansSerif", Font.PLAIN, 15));

        JLabel lblHotlineTitle = new JLabel("Hotline: ");
        lblHotlineTitle.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 15));
        lblHotline = new JLabel("");
        lblHotline.setFont(new Font("SansSerif", Font.PLAIN, 15));

        JPanel diaChiPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        diaChiPanel.add(lblDiaChiTitle);
        diaChiPanel.add(lblDiaChi);

        JPanel hotlinePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        hotlinePanel.add(lblHotlineTitle);
        hotlinePanel.add(lblHotline);

        topPanel.add(diaChiPanel);
        topPanel.add(hotlinePanel);

        JLabel lblChiTietTieuDe = new JLabel("CHI TIẾT PHIẾU ĐỔI TRẢ", SwingConstants.CENTER);
        lblChiTietTieuDe.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblChiTietTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(lblChiTietTieuDe);

        // Thông tin phiếu
        topPanel.add(taoDongThongTin("Mã phiếu đổi trả:", lblMaPhieuDT = new JLabel("")));
        topPanel.add(taoDongThongTin("Mã hoá đơn:", lblMaHD = new JLabel("")));
        topPanel.add(taoDongThongTin("Ngày lập:", lblNgayLap = new JLabel("")));
        topPanel.add(taoDongThongTin("Nhân viên:", lblNhanVien = new JLabel("")));
        topPanel.add(taoDongThongTin("Khách hàng:", lblKhachHang = new JLabel("")));

        add(topPanel, BorderLayout.NORTH);

        // ======= CENTER (Bảng thuốc + lý do) =======
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        String[] columns = {"Tên thuốc", "Số lượng", "Đơn vị", "Mức hoàn", "Tiền hoàn", "Ghi chú"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        tblThuoc = new JTable(model);
        JScrollPane scroll = new JScrollPane(tblThuoc);
        centerPanel.add(scroll);

        JPanel lyDoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblLyDoTitle = new JLabel("Lý do:");
        lblLyDoTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblLyDo = new JLabel("");
        lblLyDo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lyDoPanel.add(lblLyDoTitle);
        lyDoPanel.add(lblLyDo);
        centerPanel.add(lyDoPanel);

        add(centerPanel, BorderLayout.CENTER);

        // ======= BOTTOM (Tổng tiền + nút) =======
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JPanel tongTienPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTongTienTitle = new JLabel("Tổng tiền hoàn:");
        lblTongTienTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTongTienHoan = new JLabel("0 VND");
        lblTongTienHoan.setFont(new Font("SansSerif", Font.PLAIN, 15));
        tongTienPanel.add(lblTongTienTitle);
        tongTienPanel.add(lblTongTienHoan);
        bottomPanel.add(tongTienPanel);

        JPanel nutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
        btnInPhieu = taoButton("In phiếu", "/img/print.png");
        btnQuayLai = taoButton("Quay lại", "/img/signOut.png");
        btnInPhieu.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnQuayLai.setFont(new Font("SansSerif", Font.BOLD, 15));
        nutPanel.add(btnInPhieu);
        nutPanel.add(btnQuayLai);
        bottomPanel.add(nutPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        btnQuayLai.addActionListener(e -> {
            mainFrame.setUpNoiDung(new TimKiemPhieuDoiTra_GUI(mainFrame));
        });
    }

    private JPanel taoDongThongTin(String tieuDe, JLabel lblNoiDung) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 5));
        JLabel lblTieuDe = new JLabel(tieuDe);
        lblTieuDe.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblNoiDung.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblTieuDe);
        panel.add(lblNoiDung);
        return panel;
    }

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
