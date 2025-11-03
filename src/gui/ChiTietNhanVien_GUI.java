package gui;

import controller.ToolCtrl;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class ChiTietNhanVien_GUI extends JPanel {
	public JLabel imgAnhNV;
	public JButton btnChonAnh, btnLamMoi, btnCapNhat, btnLuu;
	public JLabel lblTrangThai;
	public JTextField txtMaNV, txtTenNV, txtSdt, txtLuong, txtEmail, txtThue;
	public JComboBox<String> cmbChucVu, cmbGioiTinh;
	public JDateChooser dtpNgaySinh, dtpNgayVaoLam;

	public ToolCtrl tool = new ToolCtrl();
	public TrangChuQL_GUI mainFrame;

    public ChiTietNhanVien_GUI(TrangChuQL_GUI mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    public void initUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 25, 25, 25));
        setBackground(Color.WHITE);

        // =================== TIÊU ĐỀ ===================
        JLabel lblTitle = new JLabel("CHI TIẾT NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0x1E3A8A));
        add(lblTitle, BorderLayout.NORTH);

        // =================== CENTER: Nội dung ===================
        JPanel centerPanel = new JPanel(new BorderLayout(30, 0));
        centerPanel.setBackground(Color.WHITE);

        // =================== LEFT: Ảnh + Nút + Trạng thái ===================
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(280, 400));

        // --- Ảnh nhân viên ---
        imgAnhNV = new JLabel(loadPlaceholderImage(227, 263));
        imgAnhNV.setPreferredSize(new Dimension(227, 263));
        imgAnhNV.setHorizontalAlignment(SwingConstants.CENTER);
        imgAnhNV.setBorder(BorderFactory.createLineBorder(new Color(0xD1D1D1), 1, true));

        JPanel imgWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imgWrapper.setBackground(Color.WHITE);
        imgWrapper.add(imgAnhNV);

        // --- Nút chọn ảnh ---
        btnChonAnh = tool.taoButton("Chọn ảnh", "/picture/nhanVien/folder.png");
        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnWrapper.setBackground(Color.WHITE);
        btnWrapper.add(btnChonAnh);

        // --- Trạng thái ---
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setMaximumSize(new Dimension(300, 40));

        JLabel lblStatus = tool.taoLabel("Trạng thái: ");
        lblTrangThai = tool.taoLabel("Đang làm việc");
        lblTrangThai.setFont(new Font("Arial", Font.BOLD, 15));
        lblTrangThai.setForeground(new Color(0x006400));

        statusPanel.add(lblStatus);
        statusPanel.add(lblTrangThai);
        statusPanel.add(Box.createHorizontalGlue());

        // --- Gom nhóm ---
        JPanel contentGroup = new JPanel();
        contentGroup.setLayout(new BoxLayout(contentGroup, BoxLayout.Y_AXIS));
        contentGroup.setBackground(Color.WHITE);

        contentGroup.add(imgWrapper);
        contentGroup.add(Box.createVerticalStrut(10));
        contentGroup.add(btnWrapper);
        contentGroup.add(Box.createVerticalStrut(10));
        contentGroup.add(statusPanel);

        // --- Căn giữa dọc ---
        JPanel centeredWrapper = new JPanel(new GridBagLayout());
        centeredWrapper.setBackground(Color.WHITE);
        GridBagConstraints gbcCenter = new GridBagConstraints();
        gbcCenter.gridx = 0; gbcCenter.gridy = 0;
        gbcCenter.weightx = 1.0; gbcCenter.weighty = 1.0;
        gbcCenter.anchor = GridBagConstraints.CENTER;
        centeredWrapper.add(contentGroup, gbcCenter);

        leftPanel.add(centeredWrapper);
        centerPanel.add(leftPanel, BorderLayout.WEST);

        // =================== RIGHT: Form 2 cột + Nút ===================
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        // --- Form 2 cột ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(18, 0, 18, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Khởi tạo field
        txtMaNV = tool.taoTextField("Mã nhân viên tự động"); txtMaNV.setEnabled(false);
        txtTenNV = tool.taoTextField("Nhập tên nhân viên");
        cmbChucVu = tool.taoComboBox(new String[]{"Quản lý", "Nhân viên", "Bảo vệ"});
        txtSdt = tool.taoTextField("Nhập số điện thoại");
        txtLuong = tool.taoTextField("Nhập lương");

        cmbGioiTinh = tool.taoComboBox(new String[]{"Nam", "Nữ", "Khác"});
        dtpNgaySinh = tool.taoDateChooser(); dtpNgaySinh.setEnabled(false);
        dtpNgayVaoLam = tool.taoDateChooser(); dtpNgayVaoLam.setEnabled(false);
        txtEmail = tool.taoTextField("Nhập email");
        txtThue = tool.taoTextField("Tính tự động"); txtThue.setEnabled(false);

        // DÒNG 1
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createFormRow("Mã nhân viên: ", txtMaNV), gbc);
        gbc.gridx = 1;
        formPanel.add(createFormRow("Giới tính: ", cmbGioiTinh), gbc);

        // DÒNG 2
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createFormRow("Tên nhân viên: ", txtTenNV), gbc);
        gbc.gridx = 1;
        formPanel.add(createFormRow("Ngày sinh: ", dtpNgaySinh), gbc);

        // DÒNG 3
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createFormRow("Chức vụ: ", cmbChucVu), gbc);
        gbc.gridx = 1;
        formPanel.add(createFormRow("Ngày vào làm: ", dtpNgayVaoLam), gbc);

        // DÒNG 4
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createFormRow("Số điện thoại: ", txtSdt), gbc);
        gbc.gridx = 1;
        formPanel.add(createFormRow("Email: ", txtEmail), gbc);

        // DÒNG 5
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createFormRow("Lương: ", txtLuong), gbc);
        gbc.gridx = 1;
        formPanel.add(createFormRow("Thuế TNCN: ", txtThue), gbc);

        // --- Nút hành động ---
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        GridBagConstraints btnGbc = new GridBagConstraints();
        btnGbc.insets = new Insets(0, 12, 0, 12);

        btnLamMoi = tool.taoButton("Làm mới", "/picture/nhanVien/refresh.png");
        btnCapNhat = tool.taoButton("Cập nhật", "/picture/nhanVien/edit.png");
        btnLuu = tool.taoButton("Lưu", "/picture/nhanVien/diskette.png");

        btnGbc.gridx = 0; buttonPanel.add(btnLamMoi, btnGbc);
        btnGbc.gridx = 1; buttonPanel.add(btnCapNhat, btnGbc);
        btnGbc.gridx = 2; buttonPanel.add(btnLuu, btnGbc);

        rightPanel.add(formPanel, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        centerPanel.add(rightPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // =================== SỰ KIỆN ===================
        btnChonAnh.addActionListener(e -> btnChonAnhActionPerformed(e));
        btnLamMoi.addActionListener(e -> btnLamMoiActionPerformed(e));
        btnCapNhat.addActionListener(e -> btnCapNhatActionPerformed(e));
        btnLuu.addActionListener(e -> btnLuuActionPerformed(e));
    }

    // =================== TẠO DÒNG FORM (giữ lại) ===================
    public JPanel createFormRow(String labelText, JComponent field) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));

        JLabel lbl = tool.taoLabel(labelText);
        lbl.setPreferredSize(new Dimension(130, 40));
        lbl.setMinimumSize(new Dimension(130, 40));

        field.setPreferredSize(new Dimension(180, 40));

        row.add(lbl);
        row.add(Box.createHorizontalStrut(12));
        row.add(field);
        row.add(Box.createHorizontalGlue());

        return row;
    }

    // =================== PLACEHOLDER IMAGE ===================
    public ImageIcon loadPlaceholderImage(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(240, 240, 240));
        g2.fillRect(0, 0, w, h);
        g2.setColor(Color.GRAY);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("No Image", w / 2 - 50, h / 2);
        g2.dispose();
        return new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }

    // =================== SỰ KIỆN ===================
    public void btnChonAnhActionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                ImageIcon icon = new ImageIcon(fc.getSelectedFile().getPath());
                Image scaled = icon.getImage().getScaledInstance(227, 263, Image.SCALE_SMOOTH);
                imgAnhNV.setIcon(new ImageIcon(scaled));
                tool.hienThiThongBao("Thành công", "Đã chọn ảnh!", true);
            } catch (Exception ex) {
                tool.hienThiThongBao("Lỗi", "Không thể tải ảnh!", false);
            }
        }
    }

    public void btnLamMoiActionPerformed(ActionEvent e) {
        txtTenNV.setText("");
        txtSdt.setText("");
        txtLuong.setText("");
        txtEmail.setText("");
        cmbChucVu.setSelectedIndex(0);
        cmbGioiTinh.setSelectedIndex(0);
        imgAnhNV.setIcon(loadPlaceholderImage(227, 263));
        tool.hienThiThongBao("Thành công", "Đã làm mới form!", true);
    }

    public void btnCapNhatActionPerformed(ActionEvent e) {
        txtMaNV.setEnabled(true);
        dtpNgaySinh.setEnabled(true);
        dtpNgayVaoLam.setEnabled(true);
        txtThue.setEnabled(true);
        tool.hienThiThongBao("Thông báo", "Đã mở chế độ chỉnh sửa!", true);
    }

    public void btnLuuActionPerformed(ActionEvent e) {
        if (txtTenNV.getText().trim().isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Vui lòng nhập tên nhân viên!", false);
            return;
        }
        boolean confirm = tool.hienThiXacNhan("Xác nhận", "Lưu thông tin nhân viên?", null);
        if (confirm) {
            tool.hienThiThongBao("Thành công", "Lưu thành công!", true);
        }
    }
}