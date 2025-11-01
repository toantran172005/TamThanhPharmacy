package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;

import controller.ToolCtrl;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LapPhieuDatHang_GUI extends JPanel {
    private JTextField txtSdt, txtTenKH, txtTuoi, txtSoLuong;
    private JComboBox<String> cmbSanPham, cmbDonVi;
    private JTable tblThuoc;
    private JTextArea txaGhiChu;
    private JButton btnThem, btnLamMoi, btnTaoPhieuDat;
    private JDateChooser ngayHen;
    Font font1 = new Font("Arial", Font.BOLD, 18);
  	Font font2 = new Font("Arial", Font.PLAIN, 15);
  	public ToolCtrl tool = new ToolCtrl();

    public LapPhieuDatHang_GUI() {
    	setLayout(new BorderLayout(0, 15));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(1027, 900));
		setBorder(new EmptyBorder(10, 20, 10, 20));

        // =================== TOP ===================
        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel lblTieuDe = new JLabel("LẬP PHIẾU ĐẶT THUỐC", SwingConstants.CENTER);
        lblTieuDe.setFont(font1);
        lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlTop.add(lblTieuDe);

        pnlTop.add(Box.createVerticalStrut(10));
        pnlTop.add(new JSeparator());
        pnlTop.add(Box.createVerticalStrut(15));

        // --- Hàng 1: SĐT, Tên KH, Tuổi ---
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
        row1.setBackground(Color.WHITE);

        txtSdt = tool.taoTextField("Số điện thoại...");
        txtTenKH = tool.taoTextField("Tên khách hàng...");
        txtTuoi = tool.taoTextField("Tuổi...");

        row1.add(taoLabel("Số điện thoại:", 15));
        row1.add(txtSdt);
        row1.add(taoLabel("Tên khách hàng:", 15));
        row1.add(txtTenKH);
        row1.add(taoLabel("Tuổi:", 15));
        row1.add(txtTuoi);
        pnlTop.add(row1);

        // --- Hàng 2: Sản phẩm, Số lượng ---
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
        row2.setBackground(Color.WHITE);

        cmbSanPham = tool.taoComboBox(new String[] {});
        cmbSanPham.setEditable(true);
        cmbSanPham.setPreferredSize(new Dimension(200, 30));

        txtSoLuong = tool.taoTextField("Số lượng...");

        row2.add(taoLabel("Sản phẩm:", 15));
        row2.add(cmbSanPham);
        row2.add(taoLabel("Số lượng:", 15));
        row2.add(txtSoLuong);
        pnlTop.add(row2);

        // --- Hàng 3: Ngày hẹn, Đơn vị, Thêm ---
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
        row3.setBackground(Color.WHITE);

        // --- JDateChooser ---
        ngayHen = new JDateChooser();
        ngayHen.setDateFormatString("dd-MM-yyyy"); // định dạng hiển thị
        ngayHen.setPreferredSize(new Dimension(150, 28));
        ngayHen.setDate(new Date()); // ngày hiện tại mặc định

        cmbDonVi = tool.taoComboBox(new String[] {});
        cmbDonVi.setPreferredSize(new Dimension(120, 30));

        btnThem = tool.taoButton("Thêm", "/picture/hoaDon/plus.png");

        row3.add(taoLabel("Ngày hẹn:", 15));
        row3.add(ngayHen);
        row3.add(taoLabel("Đơn vị:", 15));
        row3.add(cmbDonVi);
        row3.add(btnThem);
        pnlTop.add(row3);

        add(pnlTop, BorderLayout.NORTH);

        // =================== CENTER ===================
        String[] cols = { "STT", "Tên thuốc", "Số lượng", "Đơn vị", "Hoạt động" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
		tblThuoc = new JTable(model);
		tblThuoc.setRowHeight(25);
		tblThuoc.setFont(font2);
		tblThuoc.getTableHeader().setFont(font2);
		JScrollPane scroll = new JScrollPane(tblThuoc);
		add(scroll, BorderLayout.CENTER);
        

        // =================== BOTTOM ===================
        JPanel pnlBottom = new JPanel();
        pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
        pnlBottom.setBorder(new EmptyBorder(10, 20, 20, 20));
        pnlBottom.setBackground(Color.WHITE);

        // --- Ghi chú ---
        JPanel rowNote = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        rowNote.setBackground(Color.WHITE);
        txaGhiChu = new JTextArea(2, 45);
        txaGhiChu.setLineWrap(true);
        txaGhiChu.setWrapStyleWord(true);
        rowNote.add(taoLabel("Ghi chú:", 15));
        rowNote.add(new JScrollPane(txaGhiChu));
        pnlBottom.add(rowNote);

        // --- Nút chức năng ---
        JPanel rowBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        rowBtns.setBackground(Color.WHITE);
        btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/refresh.png");
        btnTaoPhieuDat = tool.taoButton("Tạo phiếu đặt", "/picture/hoaDon/plus.png");
        rowBtns.add(btnLamMoi);
        rowBtns.add(btnTaoPhieuDat);

        pnlBottom.add(rowBtns);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    // ======== Hàm tạo nhanh các thành phần ========

    private JLabel taoLabel(String text, int size) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font2);
        return lbl;
    }

    private JTextField taoTextField(String prompt, int width) {
        JTextField txt = new JTextField(prompt);
        txt.setPreferredSize(new Dimension(width, 30));
        txt.setFont(font2);
        txt.setForeground(Color.GRAY);
        txt.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txt.getText().equals(prompt)) {
                    txt.setText("");
                    txt.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txt.getText().isEmpty()) {
                    txt.setText(prompt);
                    txt.setForeground(Color.GRAY);
                }
            }
        });
        return txt;
    }

}
