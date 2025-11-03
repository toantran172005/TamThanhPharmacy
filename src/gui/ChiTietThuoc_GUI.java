package gui;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import java.awt.*;

import controller.ChiTietPDHCtrl;
import controller.ThuocCtrl;
import controller.ToolCtrl;

public class ChiTietThuoc_GUI extends JPanel {

    public JLabel lblAnh;
    public JButton btnChonAnh, btnCapNhat, btnQuayLai;
    public JTextField txtMaSP, txtTenSP, txtDangThuoc, txtGiaBan;
    public JComboBox<String> cmbDonViTinh, cmbThue, cmbKeThuoc;
    public JDateChooser dpHanSuDung;
    public ToolCtrl tool = new ToolCtrl();
    public ThuocCtrl thCtrl;
    public ChiTietPDHCtrl ctCtrl;
    public JComboBox<String> cmbQuocGia;
    public String maThuoc;

    public ChiTietThuoc_GUI(String maThuoc) {
    	
    	this.thCtrl = new ThuocCtrl(this);
    	this.ctCtrl = new ChiTietPDHCtrl(this);
    	this.maThuoc = maThuoc;
    	
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ======= TIÊU ĐỀ =======
        JLabel lblTitle = new JLabel("CHI TIẾT SẢN PHẨM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ======= CENTER CONTENT =======
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        JPanel mainBox = new JPanel();
        mainBox.setLayout(new BoxLayout(mainBox, BoxLayout.X_AXIS));
        mainBox.setBackground(Color.WHITE);

        // ======= CỘT TRÁI: ẢNH =======
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(300, 0));

        // Ảnh thuốc
        lblAnh = new JLabel();
        lblAnh.setPreferredSize(new Dimension(240, 240));
        lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnh.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));

        // Nút chọn ảnh
        btnChonAnh = tool.taoButton("Chọn ảnh", "/picture/nhanVien/folder.png");
        btnChonAnh.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(lblAnh);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(btnChonAnh);

        // ======= CỘT PHẢI: DẠNG HBOX 2 VBOX =======
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 10));

        // === VBox trái ===
        JPanel vboxLeft = new JPanel();
        vboxLeft.setLayout(new BoxLayout(vboxLeft, BoxLayout.Y_AXIS));
        vboxLeft.setBackground(Color.WHITE);

        txtMaSP = tool.taoTextField("");
        txtMaSP.setEditable(false);
        txtTenSP = tool.taoTextField("");
        txtDangThuoc = tool.taoTextField("");
        txtGiaBan = tool.taoTextField("");
        dpHanSuDung = tool.taoDateChooser();
        
        vboxLeft.add(taoDong("Mã sản phẩm:", txtMaSP));
        vboxLeft.add(Box.createVerticalStrut(5));
        vboxLeft.add(taoDong("Tên sản phẩm:", txtTenSP));
        vboxLeft.add(Box.createVerticalStrut(5));
        vboxLeft.add(taoDong("Dạng thuốc:", txtDangThuoc));
        vboxLeft.add(Box.createVerticalStrut(5));
        vboxLeft.add(taoDong("Giá bán:", txtGiaBan));
        vboxLeft.add(Box.createVerticalStrut(5));
        vboxLeft.add(taoDong("Hạn sử dụng:", dpHanSuDung));

        // === VBox phải ===
        JPanel vboxRight = new JPanel();
        vboxRight.setLayout(new BoxLayout(vboxRight, BoxLayout.Y_AXIS));
        vboxRight.setBackground(Color.WHITE);

        cmbDonViTinh = tool.taoComboBox(new String[]{"Viên", "Vỉ", "Hộp"});
        cmbThue = tool.taoComboBox(new String[]{"0%", "5%", "8%", "10%"});
        cmbKeThuoc = tool.taoComboBox(new String[]{"Kệ A", "Kệ B", "Kệ C"});
        cmbQuocGia = tool.taoComboBox(new String[] {"Việt Nam", "Mỹ", "Nhật Bản"});

        vboxRight.add(taoDong("Đơn vị tính:", cmbDonViTinh));
        vboxRight.add(Box.createVerticalStrut(5));
        vboxRight.add(taoDong("Thuế VAT:", cmbThue));
        vboxRight.add(Box.createVerticalStrut(5));
        vboxRight.add(taoDong("Kệ thuốc:", cmbKeThuoc));
        vboxRight.add(Box.createVerticalStrut(5));
        vboxRight.add(taoDong("Quốc gia:", cmbQuocGia));

        // Gắn 2 VBox vào HBox
        rightPanel.add(vboxLeft);
        rightPanel.add(Box.createHorizontalStrut(40));
        rightPanel.add(vboxRight);

        // ======= BUTTONS =======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setBackground(Color.WHITE);

        btnQuayLai = tool.taoButton("Quay lại", "/picture/thuoc/return.png");
        btnCapNhat = tool.taoButton("Cập nhật", "/picture/khachHang/edit.png");

        buttonPanel.add(btnQuayLai);
        buttonPanel.add(btnCapNhat);

        // ======= GẮN TOÀN BỘ =======
        mainBox.add(leftPanel);
        mainBox.add(Box.createHorizontalStrut(40));
        mainBox.add(rightPanel);

        centerPanel.add(mainBox, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
        
        //=========== GẮN SỰ KIỆN ================
        ganSuKien();
    }
    
    //========= XỬ LÝ ================

	//gắn sự kiện
    public void ganSuKien() {
    	ctCtrl.xemChiTietThuoc(maThuoc);
    	btnQuayLai.addActionListener(e -> ctCtrl.quayLaiTrangTimKiem());
    	btnChonAnh.addActionListener(e -> thCtrl.chonFile());
    }

    private JPanel taoDong(String text, JComponent comp) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setBackground(Color.WHITE);
        JLabel lbl = tool.taoLabel(text);
        row.add(lbl);
        row.add(comp);

        return row;
    }
}
