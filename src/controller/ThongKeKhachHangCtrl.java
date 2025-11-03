package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;

import dao.KhachHangDAO;
import entity.KhachHang;
import gui.ThongKeKhachHang_GUI;

public class ThongKeKhachHangCtrl {

	public ThongKeKhachHang_GUI thongKekhGUI;
	public ArrayList<KhachHang> listKHTK;
	public KhachHangDAO khDAO = new KhachHangDAO();
	public ToolCtrl tool = new ToolCtrl();
	// Thuộc tính tạm
	public Map<String, Integer> tongDonHangCache;
	public Map<String, Double> tongTienCache;

	public ThongKeKhachHangCtrl(ThongKeKhachHang_GUI thongKekhGUI) {
		super();
		this.thongKekhGUI = thongKekhGUI;
	}

	public void lamMoi() {
		thongKekhGUI.model.setRowCount(0);
		if (thongKekhGUI.ngayBD != null)
			thongKekhGUI.ngayBD.setDate(null);
		if (thongKekhGUI.ngayKT != null)
			thongKekhGUI.ngayKT.setDate(null);
		if (thongKekhGUI.lblTongKH != null)
			thongKekhGUI.lblTongKH.setText("0");
		if (thongKekhGUI.lblTongSLM != null)
			thongKekhGUI.lblTongSLM.setText("0");
		if (thongKekhGUI.lblTongCT != null)
			thongKekhGUI.lblTongCT.setText("0");
		if (thongKekhGUI.lblChiTieuTB != null)
			thongKekhGUI.lblChiTieuTB.setText("0");
		if (thongKekhGUI.barChart != null) {
			thongKekhGUI.barChart.getCategoryPlot().setDataset(new DefaultCategoryDataset());
			if (thongKekhGUI.chartPanel != null)
				thongKekhGUI.chartPanel.repaint();
		}
		if (tongDonHangCache != null)
			tongDonHangCache.clear();
		if (tongTienCache != null)
			tongTienCache.clear();
		if (listKHTK != null)
			listKHTK.clear();
	}

	public void thongKeTopKhach(int topN) {
		List<KhachHang> rows = new ArrayList<>(listKHTK);

		rows.sort((a, b) -> Double.compare(tongTienCache.getOrDefault(b.getMaKH(), 0.0),
				tongTienCache.getOrDefault(a.getMaKH(), 0.0)));

		List<KhachHang> topList = rows.size() > topN ? rows.subList(0, topN) : rows;

		thongKekhGUI.model.setRowCount(0);

		int stt = 1;
		for (KhachHang kh : topList) {
			int soLanMua = tongDonHangCache.getOrDefault(kh.getMaKH(), 0);
			double tongTien = tongTienCache.getOrDefault(kh.getMaKH(), 0.0);

			Object[] row = { stt++, kh.getMaKH(), kh.getTenKH(), tool.chuyenSoDienThoai(kh.getSdt()), soLanMua, tool.dinhDangVND(tongTien) };
			thongKekhGUI.model.addRow(row);
		}
	}

	public void thongKeKhachHang() {
		LocalDate ngayBD = tool.utilDateSangLocalDate(thongKekhGUI.ngayBD.getDate());
		LocalDate ngayKT = tool.utilDateSangLocalDate(thongKekhGUI.ngayKT.getDate());

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

		taoCache();
		listKHTK = khDAO.layListKHThongKe(ngayBD, ngayKT);
		// TABLE
		int topN = 5; // số khách muốn hiển thị trên BarChart
		listKHTK.sort((a, b) -> Double.compare(tongTienCache.getOrDefault(b.getMaKH(), 0.0),
				tongTienCache.getOrDefault(a.getMaKH(), 0.0)));
		List<KhachHang> topList = listKHTK.size() > topN ? listKHTK.subList(0, topN) : listKHTK;

		// --- TABLE + BAR CHART ---
		int stt = 1;
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		thongKekhGUI.model.setRowCount(0);

		for (KhachHang kh : listKHTK) {
			double tongTien = tongTienCache.getOrDefault(kh.getMaKH(), 0.0);
			int soLanMua = tongDonHangCache.getOrDefault(kh.getMaKH(), 0);

			Object[] row = { stt++, kh.getMaKH(), kh.getTenKH(), tool.chuyenSoDienThoai(kh.getSdt()), soLanMua,
					tool.dinhDangVND(tongTien) };
			thongKekhGUI.model.addRow(row);
		}

		// Chỉ thêm Top N khách vào BarChart
		for (KhachHang kh : topList) {
			double tongTien = tongTienCache.getOrDefault(kh.getMaKH(), 0.0);
			dataset.addValue(tongTien, "Tổng chi tiêu", kh.getTenKH());
		}

		thongKekhGUI.barChart.getCategoryPlot().setDataset(dataset);
		thongKekhGUI.chartPanel.repaint();
		// LABEL
		thongKekhGUI.lblTongKH.setText(String.valueOf(listKHTK.size()));
		double tongChiTieu = 0;
		for (KhachHang kh : listKHTK) {
			tongChiTieu += tongTienCache.getOrDefault(kh.getMaKH(), 0.0);
		}
		thongKekhGUI.lblTongCT.setText(tool.dinhDangVND(tongChiTieu));
		int tongSLM = 0;
		for (KhachHang kh : listKHTK) {
			tongSLM += tongDonHangCache.getOrDefault(kh.getMaKH(), 0);
		}
		thongKekhGUI.lblTongSLM.setText(String.valueOf(tongSLM));
		thongKekhGUI.lblChiTieuTB.setText(tool.dinhDangVND((double) tongChiTieu / listKHTK.size()));
	}

	public void taoCache() {
		if (tongDonHangCache == null || tongTienCache == null) {
			tongDonHangCache = khDAO.layTatCaTongDonHang();
			tongTienCache = khDAO.layTatCaTongTien();
		}
	}

	public Map<String, Integer> getTongDonHangCache() {
		return tongDonHangCache;
	}

	public Map<String, Double> getTongTienCache() {
		return tongTienCache;
	}

}
