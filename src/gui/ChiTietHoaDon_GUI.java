package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.ToolCtrl;

import java.awt.*;
import java.util.Vector;

public class ChiTietHoaDon_GUI extends JPanel {
    private JLabel lblDiaChi, lblHotline, lblMaHD, lblNgayLap, lblNhanVien, lblKhachHang;
    private JLabel lblGhiChu, lblTongTien, lblTienNhan, lblTienThua;
    private JTable tblThuoc;
    private JButton btnInHoaDon, btnQuayLai, btnTaoPhieuDoiTra;
    private TrangChuQL_GUI mainFrame;
    Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();
    
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
        lblTieuDe.setFont(font1);
        lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlTop.add(lblTieuDe);

        // Địa chỉ (center)
        pnlTop.add(Box.createVerticalStrut(10));
        JPanel pnlDiaChi = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlDiaChi.setBackground(Color.WHITE);
        JLabel lblDC = tool.taoLabel("Địa chỉ: ");
        lblDiaChi = tool.taoLabel("");
        pnlDiaChi.add(lblDC);
        pnlDiaChi.add(lblDiaChi);
        pnlTop.add(pnlDiaChi);

        // Hotline (center)
        JPanel pnlHotline = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlHotline.setBackground(Color.WHITE);
        JLabel lblHL = tool.taoLabel("Hotline: ");
        lblHotline = tool.taoLabel("");
        pnlHotline.add(lblHL);
        pnlHotline.add(lblHotline);
        pnlTop.add(pnlHotline);

        // Tiêu đề chi tiết hoá đơn
        pnlTop.add(Box.createVerticalStrut(10));
        JLabel lblChiTiet = new JLabel("CHI TIẾT HOÁ ĐƠN", SwingConstants.CENTER);
        lblChiTiet.setFont(font1);
        lblChiTiet.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlTop.add(lblChiTiet);

//        pnlTop.add(Box.createVerticalStrut(10));
//        JPanel pnlMHD = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
//        JLabel lblMHD = tool.taoLabel("Mã hoá đơn: ");
//        lblMaHD = tool.taoLabel("");
//        pnlMHD.add(lblMHD);
//        pnlMHD.add(lblMaHD);
//        pnlTop.add(pnlMHD);
//        
//        JPanel pnlNL = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
//        JLabel lblNL = tool.taoLabel("Ngày lập: ");
//        lblNgayLap = tool.taoLabel("");
//        pnlNL.add(lblNL);
//        pnlNL.add(lblNgayLap);
//        pnlTop.add(pnlNL);
//            
//        JPanel pnlNV = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
//        JLabel lblNV = tool.taoLabel("Nhân viên: ");
//        lblNhanVien = tool.taoLabel("");
//        pnlNV.add(lblNV);
//        pnlNV.add(lblNhanVien);
//        pnlTop.add(pnlNV);
//        
//        JPanel pnlKH= new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
//        JLabel lblKH = tool.taoLabel("Khách hàng: ");
//        lblKhachHang = tool.taoLabel("");
//        pnlKH.add(lblKH);
//        pnlKH.add(lblKhachHang);
//        pnlTop.add(pnlKH);
        
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
        tblThuoc.getTableHeader().setFont(font2);

        JScrollPane scroll = new JScrollPane(tblThuoc);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách thuốc"));

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.add(scroll, BorderLayout.CENTER);

        JPanel pnlGhiChu = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        pnlGhiChu.add(new JLabel("Ghi chú:"));
        lblGhiChu = tool.taoLabel("");
        pnlGhiChu.add(lblGhiChu);
        pnlCenter.add(pnlGhiChu, BorderLayout.SOUTH);

        add(pnlCenter, BorderLayout.CENTER);

        // --- BOTTOM PANEL ---
        JPanel pnlBottom = new JPanel();
        pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
        pnlBottom.setBorder(new EmptyBorder(10, 0, 10, 0));
        pnlBottom.setBackground(Color.WHITE);
        
//        JPanel pnlTT = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
//        JLabel lblTT = tool.taoLabel("Tổng tiền: ");
//        lblTongTien = tool.taoLabel("");
//        pnlTT.add(lblNL);
//        pnlTT.add(lblNgayLap);
//        pnlBottom.add(pnlTT);
//            
//        JPanel pnlTN = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
//        JLabel lblTN = tool.taoLabel("Tiền nhận: ");
//        lblTienNhan = tool.taoLabel("");
//        pnlTN.add(lblTN);
//        pnlTN.add(lblTienNhan);
//        pnlBottom.add(pnlTN);
//        
//        JPanel pnlTThua= new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
//        JLabel lblTThua = tool.taoLabel("Tiền thừa: ");
//        lblKhachHang = tool.taoLabel("");
//        pnlKH.add(lblKH);
//        pnlKH.add(lblKhachHang);
//        pnlBottom.add(pnlKH);

        pnlBottom.add(taoDongThongTin("Tổng tiền:", lblTongTien = new JLabel("")));
        pnlBottom.add(taoDongThongTin("Tiền nhận:", lblTienNhan = new JLabel("")));
        pnlBottom.add(taoDongThongTin("Tiền thừa:", lblTienThua = new JLabel("")));

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 10));
        pnlButtons.setBackground(Color.WHITE);

        btnInHoaDon = tool.taoButton("In hoá đơn", "/picture/hoaDon/print.png");
        btnQuayLai = tool.taoButton("Quay lại", "/picture/hoaDon/signOut.png");
        btnTaoPhieuDoiTra = tool.taoButton("Tạo phiếu đổi trả", "/picture/hoaDon/plus.png");

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
        lblTen.setFont(font2);
        lbl.setFont(font2);
        panel.add(lblTen);
        panel.add(lbl);
        return panel;
    }

}
