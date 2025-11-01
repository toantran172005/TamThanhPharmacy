package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.ToolCtrl;

public class DangNhap_GUI extends JFrame implements ActionListener{
    private JTextField txtTenDangNhap;
    private JPasswordField txpPassClose;
    private JTextField txtPassOpen;
    private JLabel imgEye;
    private JLabel imgLogo;
    private JButton btnDangNhap;
    private boolean hienMatKhau = false;
    public ToolCtrl tool = new ToolCtrl();
    Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);

    public DangNhap_GUI() {
        setTitle("Đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // ======= PANEL TRÊN =======
        JPanel pnlTop = new JPanel(null);
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(new EmptyBorder(20, 0, 10, 0));
        pnlTop.setPreferredSize(new Dimension(480, 200));

        JLabel lblLogo = new JLabel();
        lblLogo.setBounds(140, 10, 200, 150);
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setIcon(scaleIcon("/picture/trangChu/logo.jpg", 200, 150)); // ✅ dùng scaleIcon()
        pnlTop.add(lblLogo);

        JLabel lblTieuDe = new JLabel("HIỆU THUỐC TAM THANH", SwingConstants.CENTER);
        lblTieuDe.setFont(font1);
        lblTieuDe.setBounds(100, 160, 280, 30);
        pnlTop.add(lblTieuDe);

        add(pnlTop, BorderLayout.NORTH);

        // ======= PANEL GIỮA =======
        JPanel pnlCenter = new JPanel(null);
        pnlCenter.setBackground(Color.WHITE);

        JLabel lblTenDangNhap = tool.taoLabel("Tên đăng nhập");
        lblTenDangNhap.setBounds(80, 20, 200, 25);
        pnlCenter.add(lblTenDangNhap);

        txtTenDangNhap = tool.taoTextField("Tên đăng nhập: ");
        txtTenDangNhap.setBounds(80, 50, 290, 34);
        pnlCenter.add(txtTenDangNhap);

        JLabel iconUser = new JLabel(scaleIcon("/picture/dangNhap/user.png", 33, 33)); // ✅
        iconUser.setBounds(375, 50, 40, 34);
        pnlCenter.add(iconUser);

        JLabel lblMatKhau = tool.taoLabel("Mật khẩu: ");
        lblMatKhau.setBounds(80, 100, 200, 25);
        pnlCenter.add(lblMatKhau);

        txpPassClose = new JPasswordField();
        txpPassClose.setFont(font2);
        txpPassClose.setBounds(80, 130, 290, 34);
        pnlCenter.add(txpPassClose);

        txtPassOpen = tool.taoTextField("Mật khẩu");
        txtPassOpen.setBounds(80, 130, 290, 34);
        txtPassOpen.setVisible(false);
        pnlCenter.add(txtPassOpen);

        imgEye = new JLabel(scaleIcon("/picture/dangNhap/eye.png", 34, 34)); // ✅
        imgEye.setBounds(375, 130, 40, 34);
        imgEye.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlCenter.add(imgEye);

        // Xử lý ẩn/hiện mật khẩu
        imgEye.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hienMatKhau = !hienMatKhau;
                if (hienMatKhau) {
                    txtPassOpen.setText(new String(txpPassClose.getPassword()));
                    txtPassOpen.setVisible(true);
                    txpPassClose.setVisible(false);
                    imgEye.setIcon(scaleIcon("/picture/dangNhap/closed-eyes.png", 34, 34)); // ✅
                } else {
                    txpPassClose.setText(txtPassOpen.getText());
                    txtPassOpen.setVisible(false);
                    txpPassClose.setVisible(true);
                    imgEye.setIcon(scaleIcon("/picture/dangNhap/eye.png", 34, 34)); // ✅
                }
            }
        });

        add(pnlCenter, BorderLayout.CENTER);

        // ======= PANEL DƯỚI =======
        JPanel pnlBottom = new JPanel(null);
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.setPreferredSize(new Dimension(480, 100));

        btnDangNhap = new JButton("Đăng Nhập");
        btnDangNhap.setFont(font2);
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setBackground(new Color(0, 174, 254));
        btnDangNhap.setBounds(80, 25, 290, 42);
        btnDangNhap.setFocusPainted(false);
        pnlBottom.add(btnDangNhap);

        // Xử lý sự kiện đăng nhập
        btnDangNhap.addActionListener(e -> chuyenDenTrangChuNhanVien());

        add(pnlBottom, BorderLayout.SOUTH);
    }

    // Hàm scaleIcon dùng để load & scale ảnh
    public ImageIcon scaleIcon(String path, int width, int height) {
        URL imgURL = getClass().getResource(path);
        if (imgURL == null) {
            System.err.println("Không tìm thấy ảnh: " + path);
            return null;
        }
        ImageIcon icon = new ImageIcon(imgURL);
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
    
    private void chuyenDenTrangChuNhanVien() {
        String ten = txtTenDangNhap.getText().trim();
        String pass = hienMatKhau ? txtPassOpen.getText().trim() : new String(txpPassClose.getPassword());

//        if (ten.isEmpty() || pass.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
//            return;
//        }

//        dao.TaiKhoanDAO tkDAO = new dao.TaiKhoanDAO();
//        String vaiTro = tkDAO.kiemTraDangNhap(ten, pass);
//
//        if (vaiTro == null) {
//            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//        } else {
//            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//
//            // Mở giao diện tương ứng với vai trò
//            if (vaiTro.equalsIgnoreCase("Quản lý")) {
                new TrangChuQL_GUI().setVisible(true);
//            } else {
//                new TrangChuNV_GUI().setVisible(true);
//            }

            // Đóng form đăng nhập
            this.dispose();
//        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
