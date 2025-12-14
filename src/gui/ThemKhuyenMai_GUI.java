package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import controller.ToolCtrl;

public class ThemKhuyenMai_GUI extends JPanel {

	public JTextField txtTenKM, txtMucKM, txtSoLuongTang;
	public JComboBox<String> cmbPhuongThuc;
	public JDateChooser dpNgayBD, dpNgayKT;
	public JButton btnCong, btnTru, btnCong1, btnTru1, btnThem, btnLamMoi;
	public JTable tblThuocKhuyenMai;
	public JCheckBox chkSelect;

	public ToolCtrl tool = new ToolCtrl();

    public ThemKhuyenMai_GUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // ============ TOP: FORM THÔNG TIN =============
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Thêm Khuyến Mãi", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        topPanel.add(lblTitle);

        // HBox lớn chứa 2 cột trái phải
        JPanel mainHBox = new JPanel();
        mainHBox.setLayout(new BoxLayout(mainHBox, BoxLayout.X_AXIS));
        mainHBox.setBackground(Color.WHITE);

        // ======= LEFT COLUMN =======
        JPanel leftBox = new JPanel();
        leftBox.setLayout(new BoxLayout(leftBox, BoxLayout.Y_AXIS));
        leftBox.setBackground(Color.WHITE);
        leftBox.setPreferredSize(new Dimension(500, 250));

        // Tên khuyến mãi
        txtTenKM = tool.taoTextField("Tên khuyến mãi ...");
        leftBox.add(taoDong("Tên khuyến mãi:", txtTenKM));

        // Phương thức
        cmbPhuongThuc = new JComboBox<>(new String[]{"Giảm giá (%)", "Mua tặng"});
        leftBox.add(Box.createVerticalStrut(10));
        leftBox.add(taoDong("Phương thức:", cmbPhuongThuc));

        // Mức khuyến mãi (%)
        txtMucKM = tool.taoTextField("");
        btnTru = tool.taoButton("", "/picture/hoaDon/minus-sign.png");
        btnCong = tool.taoButton("", "/picture/hoaDon/plus.png");
        JPanel pnlMKM = taoDongStepper("Mức khuyến mãi (%):", txtMucKM, btnTru, btnCong);
        leftBox.add(Box.createVerticalStrut(10));
        leftBox.add(pnlMKM);

        // Số lượng tặng
        txtSoLuongTang = tool.taoTextField("");
        btnTru1 = tool.taoButton("", "/picture/hoaDon/minus-sign.png");
        btnCong1 = tool.taoButton("", "/picture/hoaDon/plus.png");
        JPanel pnlSLT = taoDongStepper("Số lượng tặng:", txtSoLuongTang, btnTru1, btnCong1);
        leftBox.add(Box.createVerticalStrut(10));
        leftBox.add(pnlSLT);
        //pnlSLT.setVisible(false);
        

        // ======= RIGHT COLUMN =======
        JPanel rightBox = new JPanel();
        rightBox.setLayout(new BoxLayout(rightBox, BoxLayout.Y_AXIS));
        rightBox.setBackground(Color.WHITE);
        rightBox.setPreferredSize(new Dimension(500, 250));

        // Ngày bắt đầu
        dpNgayBD = tool.taoDateChooser();
        rightBox.add(taoDong("Ngày bắt đầu:", dpNgayBD));

        // Ngày kết thúc
        dpNgayKT = tool.taoDateChooser();
        rightBox.add(Box.createVerticalStrut(10));
        rightBox.add(taoDong("Ngày kết thúc:", dpNgayKT));

        // Buttons: Thêm + Làm mới
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        btnRow.setBackground(Color.WHITE);

        btnThem = tool.taoButton("Thêm", "/picture/khuyenMai/plus.png");
        btnLamMoi = tool.taoButton("Làm mới", "/picture/keThuoc/refresh.png");

        btnRow.add(btnThem);
        btnRow.add(btnLamMoi);
        rightBox.add(Box.createVerticalStrut(10));
        rightBox.add(btnRow);

        mainHBox.add(leftBox);
        mainHBox.add(Box.createHorizontalStrut(50));
        mainHBox.add(rightBox);

        topPanel.add(mainHBox);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(new JSeparator());

        add(topPanel, BorderLayout.NORTH);

        // ============ CENTER: DANH SÁCH THUỐC =============
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);

        JLabel lblDanhSach = new JLabel("Danh Sách Thuốc Áp Dụng");
        lblDanhSach.setFont(new Font("Arial", Font.BOLD, 16));
        lblDanhSach.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        centerPanel.add(lblDanhSach, BorderLayout.NORTH);

        // Table
        String[] cols = {"", "Mã Thuốc", "Tên Thuốc", "Loại Thuốc", "Đơn vị", "Đơn giá"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // chỉ checkbox được chọn
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }
        };

        tblThuocKhuyenMai = new JTable(model);
        tblThuocKhuyenMai.setRowHeight(35);
        tblThuocKhuyenMai.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tblThuocKhuyenMai.setBackground(Color.WHITE);
        tblThuocKhuyenMai.setGridColor(new Color(0xDDDDDD));
        tblThuocKhuyenMai.setSelectionBackground(new Color(0xE3F2FD));

        JTableHeader header = tblThuocKhuyenMai.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(0x33, 0x33, 0x33));

        JScrollPane scroll = new JScrollPane(tblThuocKhuyenMai);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));

        centerPanel.add(scroll, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // ============ EVENTS ============
        btnLamMoi.addActionListener(e -> lamMoi());
        btnThem.addActionListener(e -> themKhuyenMai());
        btnCong.addActionListener(e -> tangGiaTri(txtMucKM));
        btnTru.addActionListener(e -> giamGiaTri(txtMucKM));
        btnCong1.addActionListener(e -> tangGiaTri(txtSoLuongTang));
        btnTru1.addActionListener(e -> giamGiaTri(txtSoLuongTang));
        
    }

    // ====== HÀM TẠO DÒNG ======
    public JPanel taoDong(String text, JComponent comp) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setBackground(Color.WHITE);

        JLabel lbl = tool.taoLabel(text);
        lbl.setPreferredSize(new Dimension(150, 25));
        comp.setPreferredSize(new Dimension(220, 35));

        row.add(lbl);
        row.add(comp);
        return row;
    }

    public JPanel taoDongStepper(String labelText, JTextField txt, JButton btnMinus, JButton btnPlus) {
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

    // ====== HÀM XỬ LÝ ======
    public void tangGiaTri(JTextField txt) {
        try {
            int value = Integer.parseInt(txt.getText().trim());
            txt.setText(String.valueOf(value + 1));
        } catch (Exception e) {
            txt.setText("1");
        }
    }

    public void giamGiaTri(JTextField txt) {
        try {
            int value = Integer.parseInt(txt.getText().trim());
            if (value > 0) txt.setText(String.valueOf(value - 1));
        } catch (Exception e) {
            txt.setText("0");
        }
    }

    public void lamMoi() {
        txtTenKM.setText("");
        txtMucKM.setText("");
        txtSoLuongTang.setText("");
    }

    public void themKhuyenMai() {
        JOptionPane.showMessageDialog(this, "Đã thêm khuyến mãi mới!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
