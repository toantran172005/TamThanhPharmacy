package gui;

import java.awt.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser; // Thư viện chọn ngày
//import org.jfree.chart.*;
//import org.jfree.chart.plot.*;
//import org.jfree.data.category.DefaultCategoryDataset;

public class ThongKeHoaDon_GUI extends JFrame {
    private JDateChooser dpNgayBatDau, dpNgayKetThuc;
    private JButton btnThongKe, btnXuatExcel, btnLamMoi;
    private JLabel lblTongDoanhThu, lblTongHD, lblTBDT;
    private JComboBox<String> cmbTopTK;
    private JTable tblThongKeHD;

    public ThongKeHoaDon_GUI() {
        setTitle("Thống kê hóa đơn");
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        // ======== TOP (bộ lọc ngày, nút thống kê) ========
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel("THỐNG KÊ HÓA ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(lblTitle);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        JLabel lblTuNgay = new JLabel("Từ ngày:");
        dpNgayBatDau = new JDateChooser();
        dpNgayBatDau.setPreferredSize(new Dimension(150, 30));

        JLabel lblDenNgay = new JLabel("Đến ngày:");
        dpNgayKetThuc = new JDateChooser();
        dpNgayKetThuc.setPreferredSize(new Dimension(150, 30));

        btnThongKe = new JButton("Thống kê", new ImageIcon("img/statistic.png"));
        btnThongKe.setFont(new Font("Arial", Font.BOLD, 14));

        filterPanel.add(lblTuNgay);
        filterPanel.add(dpNgayBatDau);
        filterPanel.add(lblDenNgay);
        filterPanel.add(dpNgayKetThuc);
        filterPanel.add(btnThongKe);

        topPanel.add(filterPanel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ======== CENTER (biểu đồ + bảng thống kê) ========
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // --- Thông tin tổng quan ---
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        lblTongDoanhThu = new JLabel("Tổng doanh thu: 0 VND");
        lblTongHD = new JLabel("Tổng hóa đơn: 0");
        lblTBDT = new JLabel("Doanh thu TB/Hóa đơn: 0");

        for (JLabel lbl : new JLabel[]{lblTongDoanhThu, lblTongHD, lblTBDT}) {
            lbl.setFont(new Font("Arial", Font.PLAIN, 15));
            summaryPanel.add(lbl);
        }
        centerPanel.add(summaryPanel);

//        // --- Biểu đồ ---
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        dataset.addValue(2000000, "Doanh thu", "01/10");
//        dataset.addValue(2500000, "Doanh thu", "02/10");
//        dataset.addValue(1800000, "Doanh thu", "03/10");
//
//        JFreeChart chart = ChartFactory.createLineChart(
//                "Doanh thu theo ngày", "Ngày", "Doanh thu (VND)",
//                dataset, PlotOrientation.VERTICAL, false, true, false);
//        ChartPanel chartPanel = new ChartPanel(chart);
//        chartPanel.setPreferredSize(new Dimension(900, 400));
//        centerPanel.add(chartPanel);

        // --- Bảng top khách hàng ---
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        JPanel topTablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topTablePanel.add(new JLabel("Thống kê theo top:"));
        cmbTopTK = new JComboBox<>(new String[]{"5", "10", "20"});
        topTablePanel.add(cmbTopTK);
        tablePanel.add(topTablePanel, BorderLayout.NORTH);

        String[] columns = {"STT", "Tên khách hàng", "Số điện thoại", "Số hóa đơn", "Tổng mua"};
        Object[][] data = {};
        tblThongKeHD = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(tblThongKeHD);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(tablePanel);

        // --- Nút dưới cùng ---
        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        btnXuatExcel = new JButton("Xuất Excel", new ImageIcon("img/export.png"));
        btnLamMoi = new JButton("Làm mới", new ImageIcon("img/return.png"));
        bottomButtons.add(btnXuatExcel);
        bottomButtons.add(btnLamMoi);
        centerPanel.add(bottomButtons);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ThongKeHoaDon_GUI().setVisible(true));
    }
}
