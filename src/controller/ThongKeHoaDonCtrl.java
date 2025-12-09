package controller;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import com.toedter.calendar.JDateChooser;

import dao.HoaDonDAO;
import entity.KhachHang;
import gui.ThongKeHoaDon_GUI;

public class ThongKeHoaDonCtrl {

	public ThongKeHoaDon_GUI gui;
	public HoaDonDAO hdDAO = new HoaDonDAO();
	public ToolCtrl tool = new ToolCtrl();
	public JPanel mainCenter;

	public Map<String, Double> tongTienMap = new HashMap<>();
	public Map<String, Integer> tongDonMap = new HashMap<>();
	public java.util.List<KhachHang> listKH = new ArrayList<>();

    public ThongKeHoaDonCtrl(ThongKeHoaDon_GUI gui) {
        this.gui = gui;
        this.mainCenter = gui.getMainCenter();
        dangKySuKien();
    }

    public void dangKySuKien() {
        gui.getBtnThongKe().addActionListener(e -> thongKeKhachHang());
//        gui.getCmbTopTK().addActionListener(e -> thongKeTop());
        gui.getBtnLamMoi().addActionListener(e -> lamMoi());
        gui.getBtnXuatExcel().addActionListener(e -> xuatFileExcel());
    }

    // ========== THỐNG KÊ KHÁCH HÀNG ==========
    public void thongKeKhachHang() {
        LocalDate ngayBD = layNgay(gui.getDpNgayBatDau());
        LocalDate ngayKT = layNgay(gui.getDpNgayKetThuc());

        if (ngayBD == null) {
            tool.hienThiThongBao("Ngày bắt đầu rỗng", "Vui lòng chọn ngày bắt đầu!", false);
            return;
        } else if (ngayKT == null) {
            tool.hienThiThongBao("Ngày kết thúc rỗng", "Vui lòng chọn ngày kết thúc!", false);
            return;
        } else if (ngayBD.isAfter(ngayKT)) {
            tool.hienThiThongBao("Lỗi ngày", "Ngày bắt đầu phải trước ngày kết thúc!", false);
            return;
        }
        
        listKH = hdDAO.layListKHThongKe(ngayBD, ngayKT);
        System.out.println("Số KH lấy được: " + listKH.size());
        tongTienMap.clear();
        tongDonMap.clear();

        for (KhachHang kh : listKH) {
            double tongTien = hdDAO.layTongTien(kh.getMaKH());
            int tongDon = hdDAO.layTongDonHang(kh.getMaKH());
            tongTienMap.put(kh.getMaKH(), tongTien);
            tongDonMap.put(kh.getMaKH(), tongDon);
        }
        System.out.print("Đang thống kê");
        setDataChoLabel();
        veBieuDo(ngayBD, ngayKT);
        setDataChoTable();
    }

    // ========== LẤY NGÀY TỪ JDateChooser ==========
    public LocalDate layNgay(JDateChooser chooser) {
        if (chooser.getDate() == null) return null;
        return chooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // ========== CẬP NHẬT LABEL ==========
    public void setDataChoLabel() {
        int tongSoHoaDon = tongDonMap.values().stream().mapToInt(Integer::intValue).sum();
        double tongDoanhThu = tongTienMap.values().stream().mapToDouble(Double::doubleValue).sum();
        double tbDT = tongSoHoaDon == 0 ? 0 : tongDoanhThu / tongSoHoaDon;

        gui.getLblTongHD().setText("Tổng hóa đơn: " + tongSoHoaDon);
        gui.getLblTongDoanhThu().setText("Tổng doanh thu: " + tool.dinhDangVND(tongDoanhThu));
        gui.getLblTBDT().setText("Doanh thu TB/Hóa đơn: " + tool.dinhDangVND(tbDT));
    }

    // ========== VẼ BIỂU ĐỒ DOANH THU ==========
    public void veBieuDo(LocalDate ngayBD, LocalDate ngayKT) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<LocalDate, Double> doanhThuTheoNgay = new LinkedHashMap<>();
        LocalDate current = ngayBD;
        while (!current.isAfter(ngayKT)) {
            doanhThuTheoNgay.put(current, 0.0);
            current = current.plusDays(1);
        }

        Map<LocalDate, Double> dbData = hdDAO.layDoanhThuTheoNgay(ngayBD, ngayKT);
        for (Map.Entry<LocalDate, Double> e : dbData.entrySet()) {
            doanhThuTheoNgay.put(e.getKey(), e.getValue());
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
        for (Map.Entry<LocalDate, Double> e : doanhThuTheoNgay.entrySet()) {
            dataset.addValue(e.getValue(), "Doanh thu", e.getKey().format(fmt));
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Doanh thu theo ngày", "Ngày", "Doanh thu (VND)", dataset);
        
        org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
        org.jfree.chart.axis.CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setCategoryLabelPositions(
                org.jfree.chart.axis.CategoryLabelPositions.UP_45); // Xoay 45 độ cho dễ đọc
        xAxis.setMaximumCategoryLabelWidthRatio(1.0f); // Không cắt nhãn
        xAxis.setTickLabelsVisible(true);
        xAxis.setLowerMargin(0.01);
        xAxis.setUpperMargin(0.01);

        // Giới hạn zoom và auto-range cho dễ nhìn
        plot.getRangeAxis().setAutoRange(true);

        ChartPanel newChartPanel = new ChartPanel(chart);
        newChartPanel.setPreferredSize(new Dimension(900, 300));
        newChartPanel.setMaximumSize(new Dimension(900, 300));

        // Xóa biểu đồ cũ, thêm biểu đồ mới
        JPanel pnlBieuDo = gui.getPnlBieuDo();
        pnlBieuDo.removeAll();
        pnlBieuDo.add(newChartPanel, BorderLayout.CENTER);
        pnlBieuDo.revalidate();
        pnlBieuDo.repaint();
    }



    // ========== HIỂN THỊ DỮ LIỆU BẢNG ==========
    public void setDataChoTable() {
        DefaultTableModel model = (DefaultTableModel) gui.getTblThongKeHD().getModel();
        model.setRowCount(0);

        if (listKH == null || listKH.isEmpty()) return;

        // Sắp xếp giảm dần theo tổng mua
        listKH.sort((kh1, kh2) -> Double.compare(
                tongTienMap.getOrDefault(kh2.getMaKH(), 0.0),
                tongTienMap.getOrDefault(kh1.getMaKH(), 0.0)));

        String value = (String) gui.getCmbTopTK().getSelectedItem();
        int top = 0;
        if (value != null && !value.equals("Tất cả")) {
            top = Integer.parseInt(value);
        }

        java.util.List<KhachHang> tempList = top > 0
                ? listKH.subList(0, Math.min(top, listKH.size()))
                : listKH;

        int stt = 1;
        for (KhachHang kh : tempList) {
            model.addRow(new Object[]{
                    stt++,
                    kh.getTenKH(),
                    tool.chuyenSoDienThoai(kh.getSdt()),
                    tongDonMap.getOrDefault(kh.getMaKH(), 0),
                    tool.dinhDangVND(tongTienMap.getOrDefault(kh.getMaKH(), 0.0))
            });
        }

        // === Căn giữa dữ liệu trong bảng ===
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        javax.swing.JTable table = gui.getTblThongKeHD();

        // Căn giữa
        for (int i = 0; i < table.getColumnCount(); i++) {
            String colName = table.getColumnName(i);
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }


    // ========== LÀM MỚI ==========
    public void lamMoi() {
        tongTienMap.clear();
        tongDonMap.clear();
        listKH.clear();
        ((DefaultTableModel) gui.getTblThongKeHD().getModel()).setRowCount(0);
        gui.getLblTongDoanhThu().setText("Tổng doanh thu: 0 VND");
        gui.getLblTongHD().setText("Tổng hóa đơn: 0");
        gui.getLblTBDT().setText("Doanh thu TB/Hóa đơn: 0");
        gui.getDpNgayBatDau().setDate(null);
        gui.getDpNgayKetThuc().setDate(null);
        gui.getCmbTopTK().setSelectedIndex(0);
    }

    // ========== XUẤT EXCEL ==========
    public void xuatFileExcel() {
        try {
            DefaultTableModel model = (DefaultTableModel) gui.getTblThongKeHD().getModel();
            if (model.getRowCount() == 0) {
                tool.hienThiThongBao("Lỗi xuất Excel", "Không có dữ liệu để xuất!", false);
                return;
            }

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Lưu file Excel");
            chooser.setSelectedFile(new File("ThongKeHoaDon.xlsx"));
            int userSelection = chooser.showSaveDialog(gui);
            if (userSelection != JFileChooser.APPROVE_OPTION) return;

            File file = chooser.getSelectedFile();
            try (Workbook wb = new XSSFWorkbook()) {
                Sheet sheet = wb.createSheet("Thống kê hoá đơn");
                CellStyle boldStyle = wb.createCellStyle();
                Font boldFont = wb.createFont();
                boldFont.setBold(true);
                boldStyle.setFont(boldFont);

                Row header = sheet.createRow(0);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    Cell cell = header.createCell(i);
                    cell.setCellValue(model.getColumnName(i));
                    cell.setCellStyle(boldStyle);
                }

                for (int r = 0; r < model.getRowCount(); r++) {
                    Row row = sheet.createRow(r + 1);
                    for (int c = 0; c < model.getColumnCount(); c++) {
                        row.createCell(c).setCellValue(String.valueOf(model.getValueAt(r, c)));
                    }
                }

                for (int i = 0; i < model.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    wb.write(fos);
                }
            }

            tool.hienThiThongBao("Xuất Excel", "Xuất file Excel thành công!", true);
            Desktop.getDesktop().open(file);

        } catch (Exception e) {
            e.printStackTrace();
            tool.hienThiThongBao("Xuất Excel", "Xuất file thất bại!", false);
        }
    }
}
