package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.toedter.calendar.JDateChooser; // cần thư viện JCalendar
import controller.ToolCtrl;

public class ThongKeThuoc_GUI extends JPanel {

    private JTable tblThongKe;
    private JButton btnThongKe, btnLamMoi, btnLuu;
    private JDateChooser dpNgayBD, dpNgayKT;
    private final ToolCtrl tool = new ToolCtrl();

    public ThongKeThuoc_GUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        // ===== TOP =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Thống kê thuốc bán chạy");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        topPanel.add(lblTitle);

        // === Bộ lọc ngày ===
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        filterPanel.setBackground(Color.WHITE);

        dpNgayBD = new JDateChooser();
        dpNgayKT = new JDateChooser();

        dpNgayBD.setDateFormatString("dd/MM/yyyy");
        dpNgayKT.setDateFormatString("dd/MM/yyyy");
        dpNgayBD.setPreferredSize(new Dimension(160, 35));
        dpNgayKT.setPreferredSize(new Dimension(160, 35));

        filterPanel.add(tool.taoLabel("Ngày bắt đầu:"));
        filterPanel.add(dpNgayBD);
        filterPanel.add(tool.taoLabel("Ngày kết thúc:"));
        filterPanel.add(dpNgayKT);

        // === Nhóm nút chức năng ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
        buttonPanel.setBackground(Color.WHITE);

        btnThongKe = tool.taoButton("Thống kê", "/picture/hoaDon/statistic.png");
        btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/return.png");
        btnLuu = tool.taoButton("Lưu tệp", "/picture/thuoc/diskette.png");

        buttonPanel.add(btnThongKe);
        buttonPanel.add(btnLamMoi);
        buttonPanel.add(btnLuu);

        topPanel.add(filterPanel);
        topPanel.add(buttonPanel);
        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER: TABLE =====
        String[] cols = { "STT", "Mã thuốc", "Tên thuốc", "Số lượng bán", "Doanh thu" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tblThongKe = new JTable(model);
        tblThongKe.setRowHeight(36);
        tblThongKe.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tblThongKe.setSelectionBackground(new Color(0xE3F2FD));
        tblThongKe.setGridColor(new Color(0xDDDDDD));

        JTableHeader header = tblThongKe.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(0x333333));

        JScrollPane scrollTable = new JScrollPane(tblThongKe);
        scrollTable.setBorder(BorderFactory.createTitledBorder("Bảng Top 10 thuốc bán chạy nhất"));
        scrollTable.getViewport().setBackground(Color.WHITE);
        add(scrollTable, BorderLayout.CENTER);

        // ===== SOUTH: BIỂU ĐỒ (giả lập hoặc dùng JFreeChart) =====
        JPanel chartPanel = new JPanel();
        chartPanel.setPreferredSize(new Dimension(100, 300));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createTitledBorder("Biểu đồ số lượng bán được của top 10 thuốc"));

        JLabel fakeChart = new JLabel("🧭 (Khu vực biểu đồ - tích hợp JFreeChart sau)");
        fakeChart.setFont(new Font("Arial", Font.ITALIC, 15));
        chartPanel.add(fakeChart);

        add(chartPanel, BorderLayout.SOUTH);

        // ===== EVENTS =====
        btnThongKe.addActionListener(e -> onThongKe());
        btnLamMoi.addActionListener(e -> onLamMoi());
        btnLuu.addActionListener(e -> onLuu());
    }

    // =================== HANDLERS ===================
    private void onThongKe() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date bd = dpNgayBD.getDate();
        Date kt = dpNgayKT.getDate();

        if (bd == null || kt == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày bắt đầu và kết thúc!", "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        System.out.println("Thống kê từ " + sdf.format(bd) + " đến " + sdf.format(kt));
    }

    private void onLamMoi() {
        dpNgayBD.setDate(null);
        dpNgayKT.setDate(null);
        ((DefaultTableModel) tblThongKe.getModel()).setRowCount(0);
    }

    private void onLuu() {
        JOptionPane.showMessageDialog(this, "Chức năng lưu tệp đang được phát triển!", "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
