package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import controller.ToolCtrl;

import java.awt.*;

public class DanhSachKhuyenMai_GUI extends JPanel {

    private JTextField txtTenKM;
    private JComboBox<String> cmbTrangThai;
    private JDateChooser dpNgay; 
    private JTable tblKhuyenMai;
    private JButton btnXemChiTiet, btnLamMoi, btnLichSuXoa, btnXoaTatCa;

    private final ToolCtrl tool = new ToolCtrl();

    public DanhSachKhuyenMai_GUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // ======= NORTH: TIÊU ĐỀ + BỘ LỌC =======
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("DANH SÁCH KHUYẾN MÃI", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        topPanel.add(lblTitle);

        // ======= HÀNG 1: Tên khuyến mãi + Nút =======
        JPanel row1 = new JPanel(new BorderLayout());
        row1.setBackground(Color.WHITE);

        // Left: nhập tên khuyến mãi
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        left.setBackground(Color.WHITE);
        JLabel lblTenKM = tool.taoLabel("Tên khuyến mãi:");
        txtTenKM = tool.taoTextField("Nhập tên khuyến mãi...");
        txtTenKM.setPreferredSize(new Dimension(220, 35));
        left.add(lblTenKM);
        left.add(txtTenKM);

        row1.add(left, BorderLayout.WEST);
        row1.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // ======= HÀNG 2: Trạng thái + Ngày + Nút =======
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        row2.setBackground(Color.WHITE);
        
        btnXemChiTiet = tool.taoButton("Xem chi tiết", "/picture/khuyenMai/find.png");
        btnLamMoi = tool.taoButton("Làm mới", "/picture/keThuoc/refresh.png");
        
        JLabel lblTrangThai = tool.taoLabel("Trạng thái:");
        cmbTrangThai = new JComboBox<>(new String[]{"Tất cả", "Đang hoạt động", "Hết hạn"});
        cmbTrangThai.setPreferredSize(new Dimension(200, 35));

        JLabel lblNgay = tool.taoLabel("Ngày:");
        dpNgay = tool.taoDateChooser();

        btnLichSuXoa = tool.taoButton("Lịch sử xoá", "/picture/nhanVien/document.png");

        row2.add(lblTrangThai);
        row2.add(cmbTrangThai);
        row2.add(Box.createHorizontalStrut(30));
        row2.add(lblNgay);
        row2.add(dpNgay);
        row2.add(Box.createHorizontalStrut(30));
        row2.add(btnLichSuXoa);
        row2.add(Box.createHorizontalStrut(30));
        row2.add(btnXemChiTiet);
        row2.add(Box.createHorizontalStrut(30));
        row2.add(btnLamMoi);
        row2.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        topPanel.add(row1);
        topPanel.add(row2);

        add(topPanel, BorderLayout.NORTH);

        // ======= CENTER: BẢNG DANH SÁCH =======
        String[] cols = {
                "Mã khuyến mãi", "Tên khuyến mãi",
                "Hình thức", "Mức khuyến mãi",
                "Ngày bắt đầu", "Ngày kết thúc",
                "Trạng thái", "Hoạt động"
        };

        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0; // chỉ cột chọn được phép check
            }
        };

        tblKhuyenMai = new JTable(model);
        tblKhuyenMai.setRowHeight(35);
        tblKhuyenMai.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tblKhuyenMai.setSelectionBackground(new Color(0xE3F2FD));
        tblKhuyenMai.setGridColor(new Color(0xDDDDDD));
        tblKhuyenMai.setBackground(Color.WHITE);
        tblKhuyenMai.setForeground(new Color(0x33, 0x33, 0x33));
        tblKhuyenMai.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader header = tblKhuyenMai.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(0x33, 0x33, 0x33));
        header.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));

        JScrollPane scroll = new JScrollPane(tblKhuyenMai);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));

        add(scroll, BorderLayout.CENTER);

        // ======= SỰ KIỆN =======
        btnLamMoi.addActionListener(e -> lamMoi());
        btnXemChiTiet.addActionListener(e -> tool.doiPanel(this, new ChiTietKhuyenMai_GUI()));
        btnLichSuXoa.addActionListener(e -> lichSuXoa());
    }

    // ====== CÁC HÀM XỬ LÝ SỰ KIỆN ======
    private void lamMoi() {
        txtTenKM.setText("");
        cmbTrangThai.setSelectedIndex(0);
    }

    private void lichSuXoa() {
        JOptionPane.showMessageDialog(this, "Mở lịch sử xoá khuyến mãi!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void xoaTatCa() {
        JOptionPane.showMessageDialog(this, "Xoá tất cả khuyến mãi!", "Thông báo", JOptionPane.WARNING_MESSAGE);
    }
}
