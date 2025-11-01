package gui;

import controller.ToolCtrl;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class ThemNhanVien_GUI extends JPanel {

    // === FXML fx:id components ===
    private JLabel imgAnhNV;                    // <ImageView fx:id="imgAnhNV" ...>
    private JButton btnChonAnh;                 // <Button fx:id="btnChonAnh" ...>
    private JTextField txtTenNV;                // <TextField fx:id="txtTenNV" ...>
    private JComboBox<String> cmbChucVu;        // <ComboBox fx:id="cmbChucVu" ...>
    private JTextField txtSdt;                  // <TextField fx:id="txtSdt" ...>
    private JTextField txtLuong;                // <TextField fx:id="txtLuong" ...>
    private JComboBox<String> cmbThue;          // <ComboBox fx:id="cmbThue" ...>
    private JComboBox<String> cmbGioiTinh;      // <ComboBox fx:id="cmbGioiTinh" ...>
    private JDateChooser dpNgaySinh;            // <DatePicker fx:id="dpNgaySinh" ...>
    private JDateChooser dpNgayVaoLam;          // <DatePicker fx:id="dpNgayVaoLam" ...>
    private JTextField txtEmail;                // <TextField fx:id="txtEmail" ...>
    private JButton btnLamMoi;                  // <Button fx:id="btnLamMoi" ...>
    private JButton btnThem;                    // <Button fx:id="btnThem" ...>

    private final ToolCtrl tool = new ToolCtrl();

    public ThemNhanVien_GUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 30, 30, 30));

        // =================== TOP: Tiêu đề ===================
        JLabel lblTitle = new JLabel("THÊM NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblTitle.setPreferredSize(new Dimension(997, 126));
        add(lblTitle, BorderLayout.NORTH);

        // =================== CENTER: Nội dung ===================
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));

        // --- Left: Ảnh + Chọn ảnh ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(300, 589));

        // Placeholder image
        imgAnhNV = new JLabel();
        imgAnhNV.setPreferredSize(new Dimension(227, 269));
        imgAnhNV.setHorizontalAlignment(SwingConstants.CENTER);
        imgAnhNV.setBorder(BorderFactory.createLineBorder(new Color(0xD1D1D1), 1, true));
        imgAnhNV.setIcon(loadPlaceholderImage(227, 269));

        JPanel imgWrapper = new JPanel(new FlowLayout());
        imgWrapper.setBorder(new EmptyBorder(80, 40, 0, 40));
        imgWrapper.add(imgAnhNV);
        leftPanel.add(imgWrapper);

        // Nút chọn ảnh
        btnChonAnh = tool.taoButton("Chọn ảnh", "/picture/nhanVien/folder.png");
        JPanel btnChonWrapper = new JPanel();
        btnChonWrapper.setBorder(new EmptyBorder(30, 0, 0, 0));
        btnChonWrapper.add(btnChonAnh);
        leftPanel.add(btnChonWrapper);

        // --- Right: Form nhập liệu ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(690, 628));

        // Hai cột form
        JPanel formRow = new JPanel();
        formRow.setLayout(new BoxLayout(formRow, BoxLayout.X_AXIS));
        formRow.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Cột 1
        JPanel col1 = createFormColumn(
                "Tên nhân viên: ", txtTenNV = tool.taoTextField(""),
                "Chức vụ: ", cmbChucVu = tool.taoComboBox(new String[]{"Quản lý", "Nhân viên", "Bảo vệ"}),
                "Số điện thoại: ", txtSdt = tool.taoTextField(""),
                "Lương: ", txtLuong = tool.taoTextField(""),
                "Thuế: ", cmbThue = tool.taoComboBox(new String[]{"0%", "10%", "20%"}),
                "", btnLamMoi = tool.taoButton("Làm mới", "/picture/nhanVien/refresh.png")
        );

        // Cột 2
        JPanel col2 = createFormColumn(
                "Giới tính: ", cmbGioiTinh = tool.taoComboBox(new String[]{"Nam", "Nữ", "Khác"}),
                "Ngày sinh: ", dpNgaySinh = tool.taoDateChooser(),
                "Ngày vào làm: ", dpNgayVaoLam = tool.taoDateChooser(),
                "Email: ", txtEmail = tool.taoTextField(""),
                "", new JLabel(), // placeholder
                "", btnThem = tool.taoButton("Thêm", "/picture/nhanVien/plus.png")
        );

        formRow.add(col1);
        formRow.add(Box.createHorizontalStrut(30));
        formRow.add(col2);
        rightPanel.add(formRow);

        // Kết hợp left + right
        centerPanel.add(leftPanel);
        centerPanel.add(Box.createHorizontalStrut(30));
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);

        // =================== Wire Events ===================
        btnChonAnh.addActionListener(e -> btnChonAnhActionPerformed(e));
        btnLamMoi.addActionListener(e -> btnLamMoiActionPerformed(e));
        btnThem.addActionListener(e -> btnThemActionPerformed(e));
    }

    // Helper: Tạo cột form (label + field)
    private JPanel createFormColumn(Object... components) {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setPreferredSize(new Dimension(345, 538));
        col.setMaximumSize(new Dimension(345, Short.MAX_VALUE));

        for (int i = 0; i < components.length; i += 2) {
            String labelText = (String) components[i];
            JComponent field = (JComponent) components[i + 1];

            if (labelText.isEmpty()) {
                JPanel row = new JPanel();
                row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
                row.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
                row.add(Box.createHorizontalGlue());
                row.add(field);
                row.add(Box.createHorizontalGlue());
                col.add(row);
                col.add(Box.createVerticalStrut(20));
                continue;
            }

            JLabel lbl = tool.taoLabel(labelText);
            lbl.setPreferredSize(new Dimension(110, 35));

            JPanel row = new JPanel();
            row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
            row.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
            row.add(Box.createHorizontalStrut(10));
            row.add(lbl);
            row.add(Box.createHorizontalStrut(10));
            row.add(field);
            row.add(Box.createHorizontalGlue());

            col.add(row);
            col.add(Box.createVerticalStrut(30));
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
                Image scaled = icon.getImage().getScaledInstance(227, 269, Image.SCALE_SMOOTH);
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
        cmbThue.setSelectedIndex(0);
        dpNgaySinh.setDate(null);
        dpNgayVaoLam.setDate(null);
        imgAnhNV.setIcon(loadPlaceholderImage(227, 269));
        tool.hienThiThongBao("Thành công", "Đã làm mới form!", true);
    }

    public void btnThemActionPerformed(ActionEvent e) {
        if (txtTenNV.getText().trim().isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Vui lòng nhập tên nhân viên!", false);
            return;
        }
        if (txtSdt.getText().trim().isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Vui lòng nhập số điện thoại!", false);
            return;
        }
        boolean confirm = tool.hienThiXacNhan("Xác nhận", "Thêm nhân viên mới?", null);
        if (confirm) {
            // TODO: Save to DB
            tool.hienThiThongBao("Thành công", "Thêm nhân viên thành công!", true);
            btnLamMoiActionPerformed(null); // reset form
        }
    }

}