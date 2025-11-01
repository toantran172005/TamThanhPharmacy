package gui;

import controller.ToolCtrl;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class ChiTietNhanVien_GUI extends JPanel {

    // === FXML fx:id components ===
    private JLabel imgAnhNV;               // <ImageView fx:id="imgAnhNV" ...>
    private JButton btnChonAnh;            // <Button fx:id="btnChonAnh" ...>
    private JLabel lblTrangThai;           // <Label fx:id="lblTrangThai" ...>
    private JTextField txtMaNV;            // <TextField fx:id="txtMaNV" ...>
    private JTextField txtTenNV;           // <TextField fx:id="txtTenNV" ...>
    private JComboBox<String> cmbChucVu;   // <ComboBox fx:id="cmbChucVu" ...>
    private JTextField txtSdt;             // <TextField fx:id="txtSdt" ...>
    private JTextField txtLuong;           // <TextField fx:id="txtLuong" ...>
    private JComboBox<String> cmbGioiTinh; // <ComboBox fx:id="cmbGioiTinh" ...>
    private JDateChooser dtpNgaySinh;      // <DatePicker fx:id="dtpNgaySinh" ...>
    private JDateChooser dtpNgayVaoLam;    // <DatePicker fx:id="dtpNgayVaoLam" ...>
    private JTextField txtEmail;           // <TextField fx:id="txtEmail" ...>
    private JTextField txtThue;            // <TextField fx:id="txtThue" ...>
    private JButton btnLamMoi;             // <Button fx:id="btnLamMoi" ...>
    private JButton btnCapNhat;            // <Button fx:id="btnCapNhat" ...>
    private JButton btnLuu;   
    private TrangChuQL_GUI mainFrame;

    private final ToolCtrl tool = new ToolCtrl();
    
    public ChiTietNhanVien_GUI(TrangChuQL_GUI mainFrame) {
		this.mainFrame = mainFrame;
		initUI();
	}


    public void initUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 20, 20, 20));

        // =================== TOP: Tiêu đề ===================
        JLabel lblTitle = new JLabel("CHI TIẾT NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblTitle.setPreferredSize(new Dimension(842, 68));
        add(lblTitle, BorderLayout.NORTH);

        // =================== CENTER: Nội dung ===================
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));

        // --- Left: Ảnh + Trạng thái ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(301, 567));

        // Ảnh nhân viên
        imgAnhNV = new JLabel();
        imgAnhNV.setPreferredSize(new Dimension(227, 263));
        imgAnhNV.setHorizontalAlignment(SwingConstants.CENTER);
        imgAnhNV.setBorder(BorderFactory.createLineBorder(new Color(0xD1D1D1), 1, true));
        imgAnhNV.setIcon(loadPlaceholderImage(227, 263));

        JPanel imgWrapper = new JPanel();
        imgWrapper.setLayout(new FlowLayout());
        imgWrapper.add(imgAnhNV);
        leftPanel.add(imgWrapper);

        leftPanel.add(Box.createVerticalStrut(10));

        // Nút chọn ảnh
        btnChonAnh = tool.taoButton("Chọn ảnh", "/picture/nhanVien/folder.png");
        JPanel btnChonWrapper = new JPanel();
        btnChonWrapper.add(btnChonAnh);
        leftPanel.add(btnChonWrapper);

        leftPanel.add(Box.createVerticalStrut(20));

        // Trạng thái
        JPanel statusRow = new JPanel();
        statusRow.setLayout(new BoxLayout(statusRow, BoxLayout.X_AXIS));
        statusRow.setMaximumSize(new Dimension(300, 40));
        statusRow.add(tool.taoLabel("Trang thái: "));
        lblTrangThai = tool.taoLabel("Đang làm việc");
        lblTrangThai.setForeground(new Color(0x006400)); // xanh lá
        statusRow.add(lblTrangThai);
        statusRow.add(Box.createHorizontalGlue());
        leftPanel.add(statusRow);

        // --- Right: Form nhập liệu ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(544, 567));

        // Hai cột form
        JPanel formRow = new JPanel();
        formRow.setLayout(new BoxLayout(formRow, BoxLayout.X_AXIS));

        // Cột 1
        JPanel col1 = createFormColumn(
                "Mã nhân viên: ", txtMaNV = tool.taoTextField(""),
                "Tên nhân viên: ", txtTenNV = tool.taoTextField(""),
                "Chức vụ: ", cmbChucVu = tool.taoComboBox(new String[]{"Quản lý", "Nhân viên", "Bảo vệ"}),
                "Số điện thoại: ", txtSdt = tool.taoTextField(""),
                "Lương: ", txtLuong = tool.taoTextField("")
        );
        txtMaNV.setEnabled(false);

        // Cột 2
        JPanel col2 = createFormColumn(
                "Giới tính: ", cmbGioiTinh = tool.taoComboBox(new String[]{"Nam", "Nữ", "Khác"}),
                "Ngày sinh: ", dtpNgaySinh = tool.taoDateChooser(),
                "Ngày vào làm: ", dtpNgayVaoLam = tool.taoDateChooser(),
                "Email: ", txtEmail = tool.taoTextField(""),
                "Thuế thu nhập cá nhân: ", txtThue = tool.taoTextField("")
        );
        dtpNgaySinh.setEnabled(false);
        dtpNgayVaoLam.setEnabled(false);
        txtThue.setEnabled(false);

        formRow.add(col1);
        formRow.add(Box.createHorizontalStrut(20));
        formRow.add(col2);
        rightPanel.add(formRow);

        rightPanel.add(Box.createVerticalStrut(30));

        // Nút hành động
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));

        btnLamMoi = tool.taoButton("Làm mới", "/picture/nhanVien/refresh.png");
        btnCapNhat = tool.taoButton("Cập nhật", "/picture/nhanVien/edit.png");
        btnLuu = tool.taoButton("Lưu", "/picture/nhanVien/diskette.png");

        JPanel p1 = new JPanel(); p1.add(btnLamMoi);
        JPanel p2 = new JPanel(); p2.add(btnCapNhat);
        JPanel p3 = new JPanel(); p3.add(btnLuu);

        p1.setMaximumSize(new Dimension(200, 60));
        p2.setMaximumSize(new Dimension(200, 60));
        p3.setMaximumSize(new Dimension(200, 60));

        btnPanel.add(p1);
        btnPanel.add(Box.createHorizontalStrut(20));
        btnPanel.add(p2);
        btnPanel.add(Box.createHorizontalStrut(20));
        btnPanel.add(p3);
        btnPanel.add(Box.createHorizontalGlue());

        rightPanel.add(btnPanel);

        // Kết hợp left + right
        centerPanel.add(leftPanel);
        centerPanel.add(Box.createHorizontalStrut(20));
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);

        // =================== Wire Events ===================
        btnChonAnh.addActionListener(e -> btnChonAnhActionPerformed(e));
        btnLamMoi.addActionListener(e -> btnLamMoiActionPerformed(e));
        btnCapNhat.addActionListener(e -> btnCapNhatActionPerformed(e));
        btnLuu.addActionListener(e -> btnLuuActionPerformed(e));
    }

    // Helper: Tạo cột form (label + field)
    private JPanel createFormColumn(Object... components) {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setPreferredSize(new Dimension(272, 567));
        col.setMaximumSize(new Dimension(272, Short.MAX_VALUE));

        for (int i = 0; i < components.length; i += 2) {
            String label = (String) components[i];
            JComponent field = (JComponent) components[i + 1];

            JLabel lbl = tool.taoLabel(label);
            lbl.setPreferredSize(new Dimension(120, 35));

            JPanel row = new JPanel();
            row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
            row.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
            row.add(lbl);
            row.add(Box.createHorizontalStrut(10));
            row.add(field);
            row.add(Box.createHorizontalGlue());

            col.add(row);
            col.add(Box.createVerticalStrut(10));
        }
        return col;
    }

    // Placeholder image
    private ImageIcon loadPlaceholderImage(int w, int h) {
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

    // =================== Controller Methods ===================
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
        // Mở các field bị disable
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
            // TODO: Save to DB
            tool.hienThiThongBao("Thành công", "Lưu thành công!", true);
        }
    }

}