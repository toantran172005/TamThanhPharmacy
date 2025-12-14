package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import controller.KhuyenMaiCtrl;
import controller.ToolCtrl;
import java.awt.*;
import java.util.Date;

public class ChiTietKhuyenMai_GUI extends JPanel {

    public JTextField txtTenKM, txtSLM, txtSLT;
    public JComboBox<String> cmbLoaiKM, cmbThemThuoc;
    public JTable tblChiTietKM;
    public JButton btnThemThuoc, btnCapNhat, btnQuayLai;
    public JDateChooser dpNgayBD, dpNgayKT; 
    public KhuyenMaiCtrl kmCtrl;
    
    private final ToolCtrl tool = new ToolCtrl();

    public ChiTietKhuyenMai_GUI() {
    	kmCtrl = new KhuyenMaiCtrl(this);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        // ========== TOP PANEL ==========
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = tool.taoLabel("CHI TIẾT KHUYẾN MÃI");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(lblTitle);
        topPanel.add(Box.createVerticalStrut(20));

        // ----- Dòng 1: Tên KM + Loại KM -----
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        row1.setBackground(Color.WHITE);

        txtTenKM = tool.taoTextField("Tên khuyến mãi...");
        String[] items1 = {"Mua tặng", "Giảm giá"};
        cmbLoaiKM = tool.taoComboBox(items1);

        row1.add(tool.taoLabel("Tên Khuyến Mãi:"));
        row1.add(txtTenKM);
        row1.add(Box.createHorizontalStrut(40));
        row1.add(tool.taoLabel("Loại Khuyến Mãi:"));
        row1.add(cmbLoaiKM);
        topPanel.add(row1);
        topPanel.add(Box.createVerticalStrut(15));

        // ----- Dòng 2: Ngày bắt đầu + Ngày kết thúc -----
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        row2.setBackground(Color.WHITE);

        // nếu bạn dùng thư viện JDateChooser (JCalendar):
         dpNgayBD = tool.taoDateChooser();
         dpNgayKT = tool.taoDateChooser();

        row2.add(tool.taoLabel("Ngày Bắt Đầu:"));
        row2.add(dpNgayBD);
        row2.add(Box.createHorizontalStrut(40));
        row2.add(tool.taoLabel("Ngày Kết Thúc:"));
        row2.add(dpNgayKT);
        topPanel.add(row2);
        topPanel.add(Box.createVerticalStrut(15));

        // ----- Dòng 3: Số lượng mua/tặng -----
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        row3.setBackground(Color.WHITE);
        
        JPanel leftBox = new JPanel();
        leftBox.setLayout(new BoxLayout(leftBox, BoxLayout.Y_AXIS));
        leftBox.setBackground(Color.WHITE);
        leftBox.setPreferredSize(new Dimension(500, 100));
        
     // Mức khuyến mãi (%)
        JTextField txtMucKM = tool.taoTextField("");
        JButton btnTru = tool.taoButton("", "/picture/hoaDon/minus-sign.png");
        JButton btnCong = tool.taoButton("", "/picture/hoaDon/plus.png");
        JPanel pnlMKM = taoDongStepper("Mức khuyến mãi (%):", txtMucKM, btnTru, btnCong);
        leftBox.add(Box.createVerticalStrut(10));
        leftBox.add(pnlMKM);

        // Số lượng tặng
        JTextField txtSoLuongTang = tool.taoTextField("");
        JButton btnTru1 = tool.taoButton("", "/picture/hoaDon/minus-sign.png");
        JButton btnCong1 = tool.taoButton("", "/picture/hoaDon/plus.png");
        JPanel pnlSLT = taoDongStepper("Số lượng tặng:", txtSoLuongTang, btnTru1, btnCong1);
        leftBox.add(Box.createVerticalStrut(10));
        leftBox.add(pnlSLT);

        row3.add(leftBox);
        topPanel.add(row3);
        topPanel.add(Box.createVerticalStrut(15));

        // ----- Dòng 4: Thêm thuốc -----
        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        row4.setBackground(Color.WHITE);
        String[] items = {"Kem chống nắng", "Gà quay", "Vịt tiềm", "Lẩu gà lá éc"};
        cmbThemThuoc = tool.taoComboBox(items);
        btnThemThuoc = tool.taoButton("Thêm", "/picture/khachHang/plus.png");

        row4.add(tool.taoLabel("Thêm thuốc:"));
        row4.add(cmbThemThuoc);
        row4.add(btnThemThuoc);
        row4.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        topPanel.add(row4);
        add(topPanel, BorderLayout.NORTH);

        // ========== TABLE CENTER ==========
        String[] cols = {"", "Mã Thuốc", "Tên Thuốc", "Loại Khuyến Mãi", "Mức Khuyến Mãi", "Hoạt Động"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 0; // chỉ checkbox chọn
            }
        };

        tblChiTietKM = new JTable(model);
        tblChiTietKM.setRowHeight(38);
        tblChiTietKM.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tblChiTietKM.setBackground(Color.WHITE);
        tblChiTietKM.setForeground(Color.DARK_GRAY);

        JTableHeader header = tblChiTietKM.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(tblChiTietKM);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
        add(scroll, BorderLayout.CENTER);

        // ========== BOTTOM PANEL ==========
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 15));
        bottomPanel.setBackground(Color.WHITE);

        btnCapNhat = tool.taoButton("Cập nhật", "/picture/khachHang/edit.png");
        btnQuayLai = tool.taoButton("Quay lại", "/picture/khuyenMai/signOut.png");

        bottomPanel.add(btnCapNhat);
        bottomPanel.add(btnQuayLai);
        add(bottomPanel, BorderLayout.SOUTH);

        // ====== EVENT ======
        btnQuayLai.addActionListener(e -> tool.doiPanel(this, new DanhSachKhuyenMai_GUI()));
    }

    private JPanel taoDongStepper(String labelText, JTextField txt, JButton btnMinus, JButton btnPlus) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setBackground(Color.WHITE);

        JLabel lbl = tool.taoLabel(labelText);
        lbl.setPreferredSize(new Dimension(170, 25));

        JPanel stepPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        stepPanel.setBackground(Color.WHITE);
        txt.setHorizontalAlignment(JTextField.CENTER);
        txt.setPreferredSize(new Dimension(70, 35));

        stepPanel.add(btnMinus);
        stepPanel.add(txt);
        stepPanel.add(btnPlus);

        row.add(lbl);
        row.add(stepPanel);
        return row;
    }
    
    
    public void setData(
            String maKM,
            String tenKM,
            String hinhThuc,
            String mucKM,
            Object ngayBD,
            Object ngayKT
    ) {
        txtTenKM.setText(tenKM);
        cmbLoaiKM.setSelectedItem(hinhThuc);
        dpNgayBD.setDate((Date) ngayBD);
        dpNgayKT.setDate((Date) ngayKT);
    }

}
