package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dao.KhachHangDAO;
import entity.KhachHang;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;

public class ThongKeKhachHangCtrl {

	public ToolCtrl tool = new ToolCtrl();
	public KhachHangDAO khDAO = new KhachHangDAO();
	Map<String, Double> tongTienMap = new HashMap<>();
	Map<String, Integer> tongDonMap = new HashMap<>();

	@FXML
	public ScrollPane scrTKKH;
	@FXML
	public TableView<KhachHang> tblThongKeKH;
	@FXML
	public TableColumn<KhachHang, Integer> colSTT;
	@FXML
	public TableColumn<KhachHang, String> colMaKH;
	@FXML
	public TableColumn<KhachHang, String> colTenKH;
	@FXML
	public TableColumn<KhachHang, String> colSdt;
	@FXML
	public TableColumn<KhachHang, Integer> colSoLanMua;
	@FXML
	public TableColumn<KhachHang, String> colTongChiTieu;
	@FXML
	public ComboBox<String> cmbThongKeTop;
	@FXML
	public Button btnThongKe, btnXuatExcel, btnLamMoi;
	@FXML
	public Label lblTongKH, lblTongSLM, lblTongCT, lblChiTieuTB;
	@FXML
	public DatePicker dpNgayBD, dpNgayKT;
	@FXML
	public BarChart<String, Number> barChartTopKH;

	public List<KhachHang> listKHDaSapXep;
	@FXML
	public CategoryAxis xAxis;
	@FXML
	public NumberAxis yAxis;

	public ObservableList<KhachHang> listKH = FXCollections.observableArrayList();

	public void initialize() {
		setScrCuonNhanh();
		dinhDangNgayDatePicker();
		setItemChoComboBox();
		setHoatDong();
	}

	public void setDataChoTable(ObservableList<KhachHang> listKH) {
		if (listKH == null || listKH.isEmpty()) {
			tblThongKeKH.getItems().clear();
			return;
		}

		colSTT.setCellValueFactory(cellData -> {
			return new SimpleIntegerProperty(tblThongKeKH.getItems().indexOf(cellData.getValue()) + 1).asObject();
		});

		colMaKH.setCellValueFactory(new PropertyValueFactory<>("maKH"));
		colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
		colSdt.setCellValueFactory(cellData -> {
			return new SimpleStringProperty(tool.chuyenSoDienThoai(cellData.getValue().getSdt()));
		});
		colTongChiTieu.setCellValueFactory(cellData -> new SimpleStringProperty(
				tool.dinhDangVND(tongTienMap.getOrDefault(cellData.getValue().getMaKH(), 0.0))));
		colSoLanMua.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(tongDonMap.getOrDefault(cellData.getValue().getMaKH(), 0))
						.asObject());

		tblThongKeKH.setItems(listKH);
	}

	public void setScrCuonNhanh() {
		scrTKKH.addEventFilter(ScrollEvent.SCROLL, event -> {
			double deltaY = event.getDeltaY() * 5;
			scrTKKH.setVvalue(scrTKKH.getVvalue() - deltaY / scrTKKH.getContent().getBoundsInLocal().getHeight());
			event.consume();
		});

		tblThongKeKH.setOnScroll(e -> {
			double deltaY = e.getDeltaY() * 3;
			tblThongKeKH.scrollTo((int) (tblThongKeKH.getFocusModel().getFocusedIndex() - deltaY / 40));
		});

	}

	public void dinhDangNgayDatePicker() {
		tool.dinhDangDatePicker(dpNgayBD);
		tool.dinhDangDatePicker(dpNgayKT);
	}

	public void setHoatDong() {
		btnThongKe.setOnAction(event -> thongKeKhachHang());
		btnLamMoi.setOnAction(event -> lamMoiDuLieu());
		btnXuatExcel.setOnAction(event -> xuatFileExcel());
		cmbThongKeTop.setOnAction(event -> thongKeTheoTop());
	}

	public void xuatFileExcel() {
		try {
			// Lấy danh sách khách hàng từ TableView
			List<KhachHang> list = tblThongKeKH.getItems();
			if (list == null || list.isEmpty()) {
				tool.hienThiThongBao("Lỗi xuất file excel", "Không có dữ liệu nào để xuất file!", false);
				return;
			}

			// Hộp thoại chọn nơi lưu file
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Lưu file Excel");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
			fileChooser.setInitialFileName("ThongKeKhachHang.xlsx");
			File file = fileChooser.showSaveDialog(tblThongKeKH.getScene().getWindow());
			if (file == null)
				return;

			// Tạo workbook & sheet
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Thống kê khách hàng");

			// Tạo font in đậm cho header
			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerStyle.setFont(headerFont);

			// Tạo hàng tiêu đề
			Row headerRow = sheet.createRow(0);
			String[] headers = { "STT", "Mã KH", "Tên KH", "Số điện thoại", "Số lần mua", "Tổng chi tiêu (VND)" };
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(headerStyle);
			}

			// Ghi dữ liệu từng dòng
			int rowNum = 1;
			for (int i = 0; i < list.size(); i++) {
				KhachHang kh = list.get(i);
				Row row = sheet.createRow(rowNum++);

				row.createCell(0).setCellValue(i + 1);
				row.createCell(1).setCellValue(kh.getMaKH());
				row.createCell(2).setCellValue(kh.getTenKH());
				row.createCell(3).setCellValue(kh.getSdt());
				row.createCell(4).setCellValue(kh.getTongDonHang());
				row.createCell(5).setCellValue(kh.getTongTien());
			}

			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			try (FileOutputStream fos = new FileOutputStream(file)) {
				workbook.write(fos);
			}

			workbook.close();
			tool.hienThiThongBao("Xuất file excel", "Xuất file Excel thành công!", true);
			if (tool.hienThiXacNhan("Mở file excel", "Mở file excel vừa lưu?")) {
				java.awt.Desktop.getDesktop().open(file);
			}

		} catch (Exception e) {
			e.printStackTrace();
			tool.hienThiThongBao("Xuất file excel", "Xuất file Excel thất bại!", false);
		}
	}

	public void thongKeTheoTop() {
		if (cmbThongKeTop.getValue() == null || listKH == null)
			return;

		String value = cmbThongKeTop.getValue();
		ObservableList<KhachHang> listHienThi;

		if (value.equals("Tất cả")) {
			listHienThi = listKH;
		} else {
			int top = Integer.parseInt(value);
			listHienThi = FXCollections.observableArrayList(listKH.subList(0, Math.min(top, listKH.size())));
		}

		tblThongKeKH.setItems(listHienThi);
	}

	public void lamMoiDuLieu() {
		dpNgayBD.setValue(null);
		dpNgayKT.setValue(null);
		tblThongKeKH.getItems().clear();
		barChartTopKH.getData().clear();
		listKH.clear();
	}

	public void thongKeKhachHang() {
		LocalDate ngayBD = dpNgayBD.getValue();
		LocalDate ngayKT = dpNgayKT.getValue();

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

		listKH = khDAO.layListKHThongKe(ngayBD, ngayKT);
		FXCollections.sort(listKH, (kh1, kh2) -> Double.compare(kh2.getTongTien(), kh1.getTongTien()));
		for (KhachHang kh : listKH) {
			double tongTien = kh.getTongTien();
			int tongDon = kh.getTongDonHang();
			tongTienMap.put(kh.getMaKH(), tongTien);
			tongDonMap.put(kh.getMaKH(), tongDon);
		}
		setDataChoLabel();
		setDataChoBarChart();
		setDataChoTable(listKH);
	}

	public void setDataChoBarChart() {
		barChartTopKH.getData().clear();

		List<KhachHang> top5 = listKH.stream()
				.sorted((kh1, kh2) -> Double.compare(kh2.getTongTien(), kh1.getTongTien())).limit(5).toList();

		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Top 5 khách hàng chi tiêu cao nhất");

		for (KhachHang kh : top5) {
			series.getData().add(new XYChart.Data<>(kh.getTenKH(), kh.getTongTien()));
		}

		barChartTopKH.getData().add(series);

		xAxis.setLabel("Tên khách hàng");
		yAxis.setLabel("Tổng chi tiêu (VND)");
		barChartTopKH.setLegendVisible(false);
	}

	public void setDataChoLabel() {
		lblTongKH.setText(String.valueOf(listKH.size()));
		int tongSLM = 0;
		for (KhachHang kh : listKH) {
			tongSLM += kh.getTongDonHang();
		}
		lblTongSLM.setText(String.valueOf(tongSLM));
		double tongCT = 0;
		for (KhachHang kh : listKH) {
			tongCT += kh.getTongTien();
		}
		lblTongCT.setText(tool.dinhDangVND(tongCT));
		lblChiTieuTB.setText(tool.dinhDangVND(tongCT / listKH.size()));
	}

	public void setItemChoComboBox() {
		cmbThongKeTop.getItems().addAll("Tất cả", "1", "5", "10", "20");
		cmbThongKeTop.setValue("Tất cả");
	}

}
