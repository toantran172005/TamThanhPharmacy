package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controller.ToolCtrl;

import java.awt.*;

public class ChiTietPhieuDoiTra_GUI extends JPanel{

    private JLabel lblDiaChi, lblHotline, lblMaPhieuDT, lblMaHD, lblNgayLap,
                   lblNhanVien, lblKhachHang, lblLyDo, lblTongTienHoan;
    private JTable tblThuoc;
    private JButton btnInPhieu, btnQuayLai;
    private TrangChuQL_GUI mainFrame;
    Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

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
        lblTieuDe.setFont(font1);
        lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(lblTieuDe);

        JLabel lblDiaChiTitle = new JLabel("Địa chỉ: ");
        lblDiaChiTitle.setFont(font2);
        lblDiaChi = new JLabel("");
        lblDiaChi.setFont(font2);

        JLabel lblHotlineTitle = new JLabel("Hotline: ");
        lblHotlineTitle.setFont(font2);
        lblHotline = new JLabel("");
        lblHotline.setFont(font2);

        JPanel diaChiPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        diaChiPanel.add(lblDiaChiTitle);
        diaChiPanel.add(lblDiaChi);

        JPanel hotlinePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        hotlinePanel.add(lblHotlineTitle);
        hotlinePanel.add(lblHotline);

        topPanel.add(diaChiPanel);
        topPanel.add(hotlinePanel);

        JLabel lblChiTietTieuDe = new JLabel("CHI TIẾT PHIẾU ĐỔI TRẢ", SwingConstants.CENTER);
        lblChiTietTieuDe.setFont(font1);
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
        lblLyDoTitle.setFont(font1);
        lblLyDo = new JLabel("");
        lblLyDo.setFont(font1);
        lyDoPanel.add(lblLyDoTitle);
        lyDoPanel.add(lblLyDo);
        centerPanel.add(lyDoPanel);

        add(centerPanel, BorderLayout.CENTER);

        // ======= BOTTOM (Tổng tiền + nút) =======
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JPanel tongTienPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTongTienTitle = new JLabel("Tổng tiền hoàn:");
        lblTongTienTitle.setFont(font1);
        lblTongTienHoan = new JLabel("0 VND");
        lblTongTienHoan.setFont(font1);
        tongTienPanel.add(lblTongTienTitle);
        tongTienPanel.add(lblTongTienHoan);
        bottomPanel.add(tongTienPanel);

        JPanel nutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
        btnInPhieu = tool.taoButton("In phiếu", "/picture/hoaDon/print.png");
        btnQuayLai = tool.taoButton("Quay lại", "/picture/hoaDon//signOut.png");
        btnInPhieu.setFont(font2);
        btnQuayLai.setFont(font2);
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
        lblTieuDe.setFont(font2);
        lblNoiDung.setFont(font2);
        panel.add(lblTieuDe);
        panel.add(lblNoiDung);
        return panel;
    }
    
}
