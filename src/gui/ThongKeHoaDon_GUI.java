package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser; // Thư viện chọn ngày
//import org.jfree.chart.*;
//import org.jfree.chart.plot.*;
//import org.jfree.data.category.DefaultCategoryDataset;

import controller.ToolCtrl;

public class ThongKeHoaDon_GUI extends JPanel {
    private JDateChooser dpNgayBatDau, dpNgayKetThuc;
    private JButton btnThongKe, btnXuatExcel, btnLamMoi;
    private JLabel lblTongDoanhThu, lblTongHD, lblTBDT;
    private JComboBox<String> cmbTopTK;
    private JTable tblThongKeHD;
    Font font1 = new Font("Arial", Font.BOLD, 18);
  	Font font2 = new Font("Arial", Font.PLAIN, 15);
  	public ToolCtrl tool = new ToolCtrl();

    public ThongKeHoaDon_GUI() {
    	setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1134, 617));
		setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        // ======== TOP (bộ lọc ngày, nút thống kê) ========
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel("THỐNG KÊ HÓA ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(font1);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(lblTitle);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        JLabel lblTuNgay = tool.taoLabel("Từ ngày:");
        dpNgayBatDau = tool.taoDateChooser();

        JLabel lblDenNgay = tool.taoLabel("Đến ngày:");
        dpNgayKetThuc = tool.taoDateChooser();

        btnThongKe = tool.taoButton("Thống kê","/picture/hoaDon/statistic.png");

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
        lblTongDoanhThu = tool.taoLabel("Tổng doanh thu: 0 VND");
        lblTongHD = tool.taoLabel("Tổng hóa đơn: 0");
        lblTBDT = tool.taoLabel("Doanh thu TB/Hóa đơn: 0");

        for (JLabel lbl : new JLabel[]{lblTongDoanhThu, lblTongHD, lblTBDT}) {
            lbl.setFont(font2);
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

        String[] columnNames = {"STT", "Tên khách hàng", "Số điện thoại", "Số hóa đơn", "Tổng mua"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		tblThongKeHD = new JTable(model);
		tblThongKeHD.setRowHeight(28);
		tblThongKeHD.getTableHeader().setFont(font2);
		tblThongKeHD.setFont(font2);

		// Đặt nền trắng cho bảng
		tblThongKeHD.setBackground(Color.WHITE);

		// Đặt nền trắng cho vùng header và vùng chứa
		tblThongKeHD.getTableHeader().setBackground(new Color(240, 240, 240)); // xám rất nhạt
		tblThongKeHD.setGridColor(new Color(200, 200, 200)); // Màu đường kẻ ô (nhẹ)
		tblThongKeHD.setShowGrid(true); // Bật hiển thị đường kẻ

		// Viền cho bảng
		tblThongKeHD.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

		// Nền của JScrollPane (bao quanh bảng)
		JScrollPane scrollPane = new JScrollPane(tblThongKeHD);
		scrollPane.getViewport().setBackground(Color.WHITE); // nền vùng chứa bảng
		scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách thuốc")); // viền ngoài

		// Căn giữa nội dung các ô
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblThongKeHD.getColumnCount(); i++) {
			tblThongKeHD.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		// Căn giữa tiêu đề cột
		JTableHeader header = tblThongKeHD.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(font2);
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		centerPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Nút dưới cùng ---
        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        btnXuatExcel = tool.taoButton("Xuất Excel", "/picture/hoaDon/export.png");
        btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/return.png");
        bottomButtons.add(btnXuatExcel);
        bottomButtons.add(btnLamMoi);
        centerPanel.add(bottomButtons);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }
}
