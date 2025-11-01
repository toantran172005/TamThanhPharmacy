package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;

import controller.ToolCtrl;

public class NhapThuoc_GUI extends JPanel {

    private final ToolCtrl tool = new ToolCtrl();

    private JTable tblNhapThuoc;
    private JButton btnLamMoi, btnThemTep, btnLuu;

    public NhapThuoc_GUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 20, 10, 20));

        // =================== TOP ===================
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("NHẬP THUỐC VÀO KHO", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(0x2F4F4F));
        lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTieuDe.setBorder(new EmptyBorder(10, 0, 10, 0));
        topPanel.add(lblTieuDe);

        // HBox chứa nút
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 5));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(new EmptyBorder(0, 0, 5, 35));

        btnLamMoi = tool.taoButton("Làm mới", "/picture/thuoc/refresh.png");
        btnThemTep = tool.taoButton("Thêm tệp", "/picture/thuoc/file-import.png");

        btnPanel.add(btnLamMoi);
        btnPanel.add(btnThemTep);
        topPanel.add(btnPanel);

        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(0xDDDDDD));
        topPanel.add(separator);

        // Label “Danh sách thuốc nhập”
        JLabel lblDanhSach = new JLabel("Danh Sách Thuốc Nhập", SwingConstants.CENTER);
        lblDanhSach.setFont(new Font("Times New Roman", Font.BOLD, 16));
        lblDanhSach.setBorder(new EmptyBorder(10, 0, 5, 0));
        lblDanhSach.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(lblDanhSach);

        add(topPanel, BorderLayout.NORTH);

        // =================== CENTER (Table) ===================
        String[] columnNames = {
            "STT", "Mã thuốc", "Tên thuốc", "Số lô", "Dạng thuốc",
            "ĐVT", "Hạn dùng", "Số lượng", "Đơn giá", "Thuế (%)", "Loại thuế", "Thành tiền"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tblNhapThuoc = new JTable(model);
        tblNhapThuoc.setRowHeight(35);
        tblNhapThuoc.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tblNhapThuoc.setSelectionBackground(new Color(0xE3F2FD));
        tblNhapThuoc.setGridColor(new Color(0xDDDDDD));
        tblNhapThuoc.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Header
        JTableHeader header = tblNhapThuoc.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(0x333333));
        header.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));

        // Scrollpane
        JScrollPane scrollPane = new JScrollPane(tblNhapThuoc);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // =================== BOTTOM ===================
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 10));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 10, 100));

        btnLuu = tool.taoButton("Lưu", "/picture/thuoc/diskette.png");
        bottomPanel.add(btnLuu);

        add(bottomPanel, BorderLayout.SOUTH);

        // =================== SỰ KIỆN ===================
        btnLamMoi.addActionListener(e -> lamMoiBang());
        btnThemTep.addActionListener(e -> chonFileExcel());
        btnLuu.addActionListener(e -> luuDuLieu());
    }

    // =================== HÀM XỬ LÝ ===================
    private void lamMoiBang() {
        DefaultTableModel model = (DefaultTableModel) tblNhapThuoc.getModel();
        model.setRowCount(0);
    }

    private void chonFileExcel() {
    	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn tệp Excel để nhập thuốc");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "File Excel (.xlsx, .xls)", "xlsx", "xls"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
        }
    }


    private void luuDuLieu() {
        tool.hienThiThongBao("Thông báo", "Dữ liệu đã được lưu thành công!", true);
    }
}
